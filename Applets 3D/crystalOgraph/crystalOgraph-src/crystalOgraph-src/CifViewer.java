import java.applet.Applet;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;



public class CifViewer implements ActionListener {
	private ActionListener actionListener;
	JFrame frame;
	JButton okButton;
	JButton cancelButton;
	private String[] data;
	JTextArea edit;
	JMenuItem
  	erase = new JMenuItem("New"),
		open = new JMenuItem("Open..."),
		save = new JMenuItem("Save..."),
		cut = new JMenuItem("Cut"),
		copy = new JMenuItem("Copy"),
		paste = new JMenuItem("Paste");
	JMenuBar menu;
	Clipboard clipbd;
	private CifFileParser cifData;
	private crystalOgraph applet;
	
	public CifViewer(crystalOgraph applet) {
		this.applet = applet;
		frame = new JFrame("CIF representation");

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.setContentPane(p);
		
		edit = new JTextArea();
		edit.setColumns(50);
		edit.setRows(30);
		edit.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(edit);
		edit.setDragEnabled(true);
		
		JPanel pp = new JPanel();
		okButton = new JButton("Apply changes");
		cancelButton = new JButton("Close");
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		pp.add(okButton);
		pp.add(cancelButton);

		createMenu();

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth=3;
		c.gridx=0; c.gridy=GridBagConstraints.RELATIVE;
		c.weightx=1; c.weighty=1;
		c.insets = new Insets(5,5,0,5);  
		p.add(scrollPane, c);
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth=1;
		c.insets = new Insets(10,0,0,0);  
		c.weightx=0; c.weighty=0;
		c.gridx=2; c.gridy=GridBagConstraints.RELATIVE;
		p.add(pp, c);
		
		frame.pack();
		frame.validate();
	}
	
	public void createMenu() {
		JMenu file = new JMenu("File");
		SecurityManager security = System.getSecurityManager();
		try {
			if (security!=null) security.checkRead(".");
			menu = new JMenuBar();
			file.add(erase);
			file.add(open);
			menu.add(file);
			erase.addActionListener(this);
			open.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Haven't read permission");
		}
		try {
			if (security!=null) security.checkWrite(".");
			file.add(save);
			save.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Haven't write permission");
		}
		try {
			if (security!=null) security.checkSystemClipboardAccess();
			clipbd = frame.getToolkit().getSystemClipboard();
			if (menu==null) menu = new JMenuBar();
			JMenu edit = new JMenu("Edit");
			edit.add(cut);
			edit.add(copy);
			edit.add(paste);
			menu.add(edit);
			cut.addActionListener(this);
			copy.addActionListener(this);
			paste.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Can't acces clipboard");
		}
		if (menu!=null) frame.setJMenuBar(menu);
	}
	
	public void show(boolean show) {
		frame.setVisible(show);
		if (show) frame.toFront();
	}

	public boolean checkFile(String[] s) {
		CifFileParser t = new CifFileParser(s);
		return getPosEqFromCif2(t)!=null;
	}
	
	public void setFile(String[] s) {
		data=s;
		//for (int i=0; i<s.length; i++) System.out.println(s[i]);
		cifData = new CifFileParser(data);
		if (frame.isVisible()) showFile();
	}

	public void showFile() {
		edit.setText("");
		for (int i=0; i<data.length; i++) {
			edit.append(data[i]);
			edit.append("\n");
		}
		edit.setCaretPosition(0);
	}

	public void updateFile() {
		data = new String[edit.getLineCount()];
		try {
			for (int i=0; i<data.length; i++) {
				int p1 = edit.getLineStartOffset(i);
				int p2 = edit.getLineEndOffset(i);
				data[i] = edit.getText(p1, p2-p1);
				if (data[i].length()>0 && data[i].charAt(data[i].length()-1) < 32) data[i] = data[i].substring(0, data[i].length()-1);
				if (data[i].length()>0 && data[i].charAt(data[i].length()-1) < 32) data[i] = data[i].substring(0, data[i].length()-1);
			}
			cifData = new CifFileParser(data);
			//System.out.println(cifData);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public String[] getFile() {
		return data;
	}
	
	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	
	public class CifFileFilter extends FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) return true;

	    String extension = getExtension(f);
	    if (extension!=null && extension.equals("cif")) return true;
	    return false;
		}

		public String getDescription() {
			return "CIF files";
		}
		public String getExtension(File f) {
      String ext = null;
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==erase) {
			edit.setText("");
		}
		else if (e.getSource()==open) {
			JFileChooser chooser = new JFileChooser();
			FileFilter filter = new CifFileFilter();
	    chooser.setFileFilter(filter);
	    if(chooser.showOpenDialog(frame)==JFileChooser.APPROVE_OPTION) {
	    	try {
		    	BufferedReader file = new BufferedReader(new FileReader(chooser.getSelectedFile()));
					Vector v = new Vector(20, 20);
					while (true) {						
						String s = file.readLine();
						if (s==null) break;
						v.add(s);
					}
					data = (String[]) v.toArray(new String[0]);
					showFile();
		    	file.close();
	    	}
	    	catch (Exception ex) {
	    		System.err.println(e);
	    	}
	    }
		}
		else if (e.getSource()==save) {
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			FileFilter filter = new CifFileFilter();
	    chooser.setFileFilter(filter);
	    if(chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
				try {
					String name = chooser.getSelectedFile().getPath();
					if (name.toLowerCase().lastIndexOf(".cif")==-1) name+=".cif";
					PrintStream file = new PrintStream(new FileOutputStream(name));
					for (int i=0; i<data.length; i++) {
						file.println(data[i]);
					}
		    	file.close();
				}
	    	catch (Exception ex) {
	    		System.err.println(e);
	    	}
	    }
		}
		else if (e.getSource()==cut) {
	     String selection = edit.getSelectedText();
	     if(selection == null) selection = edit.getText();
	     StringSelection clipString = new StringSelection(selection);
	     clipbd.setContents(clipString, clipString);
	     edit.replaceRange("", edit.getSelectionStart(), edit.getSelectionEnd());
		}
		else if (e.getSource()==copy) {
	     String selection = edit.getSelectedText();
	     if(selection == null) selection = edit.getText();
	     StringSelection clipString = new StringSelection(selection);
	     clipbd.setContents(clipString, clipString);
		}
		else if (e.getSource()==paste) {
	     try{
	     	 Transferable clipData = clipbd.getContents(this);
	       String clipString = (String)clipData.getTransferData(DataFlavor.stringFlavor);
	       //System.out.println("*");
	       //System.out.println(clipString);
	       //System.out.println("#");
	       edit.replaceRange(clipString, edit.getSelectionStart(), edit.getSelectionEnd());
	     } catch(Exception ex) {
	       System.err.println("Not String flavor");
	     }
		}
		
		else if (e.getSource()==okButton) {
			updateFile();
			frame.dispose();
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		}
		else if (e.getSource()==cancelButton) {
			frame.dispose();
		}
	}

	public void generateCif(Cell cell, JTable table) {
		Vector v = new Vector(10, 10);
		v.add("data_generated_by_crystalOgraph");
		v.add("_cell_length_a                  "+Math.round(cell.a*10000d)/10000d);
		v.add("_cell_length_b                  "+Math.round(cell.b*10000d)/10000d);
		v.add("_cell_length_c                  "+Math.round(cell.c*10000d)/10000d);
		v.add("_cell_angle_alpha               "+Math.round(cell.alpha*100d)/100d);
		v.add("_cell_angle_beta                "+Math.round(cell.beta*100d)/100d);
		v.add("_cell_angle_gamma               "+Math.round(cell.gamma*100d)/100d);
		v.add("_symmetry_space_group_name_H-M '"+CellSymetries.buildSGname(cell.no, cell.choice)+"'");
		v.add("_symmetry_Int_Tables_number    '"+cell.no+"'");
		v.add("");
		String[] eq = CellSymetries.getEquations(cell.no, cell.choice);
		if (eq.length>0) {
			v.add("loop_");
			v.add("_symmetry_equiv_pos_site_id");
			v.add("_symmetry_equiv_pos_as_xyz");
			for (int i=0; i<eq.length; i++) v.add("  "+(i+1)+"     '"+eq[i]+"'");
			v.add("");
		}
		v.add("loop_");
		v.add("_atom_site_label");
		v.add("_atom_site_type_symbol");
		v.add("_atom_site_fract_x");
		v.add("_atom_site_fract_y");
		v.add("_atom_site_fract_z");
		int k=8;
		for (int i=0; i<table.getRowCount()-1; i++)
			k = Math.max(Math.max(((String)table.getValueAt(i, 0)).length(), ((String)table.getValueAt(i, 1)).length()), k);

		//System.out.println(k);
		
		for (int i=0; i<table.getRowCount()-1; i++) {
			String s = "";
			if (!((String)table.getValueAt(i, 8)).equals("Remove")) break;
			String s1 = ""+(String)table.getValueAt(i, 0);
			if (s1.trim().length()==0) s1 = "O" + (i+1);
			s1 = fillSpace(s1, k+1);
			s += s1;

			String s2 = ""+(String)table.getValueAt(i, 1);
			if (s2.trim().length()==0) s2 = "O";
			s2 = fillSpace(s2, k+1);
			s += s2;

			s += fillSpace(cutMaxLen((String)table.getValueAt(i, 2), 9), 10);
			s += fillSpace(cutMaxLen((String)table.getValueAt(i, 3), 9), 10);
			s += fillSpace(cutMaxLen((String)table.getValueAt(i, 4), 9), 10);
			v.add(s);
		}
		v.add("");
		v.add(" ");
		data = (String[]) v.toArray(new String[0]);
	}
	
	public String cutMaxLen(String s, int max) {
		return s.substring(0, Math.min(max, s.length()));
	}
	
	public String fillSpace(String s, int spaces) {
		int m = spaces-s.length();
		for (int i=0; i<m; i++) s=s+" ";
		return s;
	}

	
	public static String[] getPosEqFromCif2(CifFileParser cifData) {
		Vector v = new Vector(10, 10);
		for (int i=0; i<cifData.loops.size(); i++) {
			CifFileParser.Loop l = ((CifFileParser.Loop)cifData.loops.get(i));
			int eq = l.header.indexOf("_symmetry_equiv_pos_as_xyz");
			if (eq!=-1) {
				for (int j=0; j<l.lines.size(); j++) {
					Vector u = (Vector) l.lines.get(j);
					v.add(""+u.get(eq));
				}
				return (String[]) v.toArray(new String[0]);
			}
		}
		return null;
	}
	
	public String[] getPosEqFromCif2() {
		Vector v = new Vector(10, 10);
		for (int i=0; i<cifData.loops.size(); i++) {
			CifFileParser.Loop l = ((CifFileParser.Loop)cifData.loops.get(i));
			int eq = l.header.indexOf("_symmetry_equiv_pos_as_xyz");
			if (eq!=-1) {
				for (int j=0; j<l.lines.size(); j++) {
					Vector u = (Vector) l.lines.get(j);
					v.add(""+u.get(eq));
				}
				return (String[]) v.toArray(new String[0]);
			}
		}
		return null;
	}

	public String getFormulaFromCif() {
		String s = cifData.getStringField("_chemical_formula_structural", "");
		if (s.trim().length()==0)
			s = cifData.getStringField("_chemical_formula_sum", "");
		return s;
	}
	
	public void getCellFromCIF2(Cell cell) {
		float a = (float) cifData.getDoubleField("_cell_length_a", 1);
		float b = (float) cifData.getDoubleField("_cell_length_b", 1);
		float c = (float) cifData.getDoubleField("_cell_length_c", 1);
		float alpha = (float) cifData.getDoubleField("_cell_angle_alpha", 90);
		float beta = (float) cifData.getDoubleField("_cell_angle_beta", 90);
		float gamma = (float) cifData.getDoubleField("_cell_angle_gamma", 90);
		
		int n = cifData.getIntField("_symmetry_Int_Tables_number", 0);
		String sgName = cifData.getStringField("_symmetry_space_group_name_H-M", null);
		int variant = 0;
		
		if (sgName==null) {
			if (n<1 || n>230) {
				String[] eq = getPosEqFromCif2();
				if (eq.length==0) {
					n=1;
				}
				else {
					try {
						cell.m_ccell = CellSymetries.setCustomSG(eq, " ");
						cell.m_sgName = sgName;
						cell.m_sgNo = n;
						n = 0;
					} 
					catch (CellSymetries.ParseException e) {
						JOptionPane.showMessageDialog(frame, "A problem occured during parsing the equivalent positions.\n"+e.getMessage());
						n=1;
					}
				}
			}
		}
		else {
			variant = CellSymetries.parseChoice(sgName);
			int nn = CellSymetries.getSGno(sgName);
			if (nn==0) {
				String[] eq = getPosEqFromCif2();
				if (eq.length==0) {
					JOptionPane.showMessageDialog(frame, "Non-standard space group ("+sgName+"). \nI don't know the settings of this space group\nand they aren't specified in the CIF file.\nSorry, please choose another one.");
					if (n<1 || n>230) n=1;
				}
				else {
					//for (int j=0; j<eq.length; j++) System.out.println(eq[j]);
					try {
						cell.m_ccell = CellSymetries.setCustomSG(eq, sgName);
						cell.m_sgName = sgName;
						cell.m_sgNo = n;
						n = 0;
					} 
					catch (CellSymetries.ParseException e) {
						JOptionPane.showMessageDialog(frame, "A problem occured during parsing the equivalent positions.\n"+e.getMessage());
						n=1;
					}
				}
			}
			else {
				if (n>=1 && n<=230) {
					if (n!=nn) {
						JOptionPane.showMessageDialog(frame, "Space group number and symbol don't match.\nNo = "+n+"\nSymbol = "+sgName);
					}
				}
				else {
					n=nn;
				}
			}
		}
		cell.set(n, variant, a, b, c, alpha, beta, gamma);
	}

	
	// returns a vector of vectors 
	// 1st: one entry for each line
	// 2nd: lbl, sym, x, y, z     elements can be null, String or Double
	public Vector getAtomsFromCIF2() {
		Vector v = new Vector(10, 10);
		for (int i=0; i<cifData.loops.size(); i++) {
			CifFileParser.Loop l = ((CifFileParser.Loop)cifData.loops.get(i));
			int x = l.header.indexOf("_atom_site_fract_x");
			int y = l.header.indexOf("_atom_site_fract_y");
			int z = l.header.indexOf("_atom_site_fract_z");
			if (x!=-1 && y!=-1 && z!=-1) {
				int lbl = l.header.indexOf("_atom_site_label");
				int sym = l.header.indexOf("_atom_site_type_symbol");
				l: for (int j=0; j<l.lines.size(); j++) {
					Vector u = (Vector) l.lines.get(j);
					if (u.size()==l.header.size()) {
						Vector w =  new Vector(5, 1);
						w.add((lbl==-1?null:(""+u.get(lbl))));
						w.add((sym==-1?null:(""+u.get(sym))));
						w.add(new Double(CifFileParser.parseCoord((String)u.get(x))));
						w.add(new Double(CifFileParser.parseCoord((String)u.get(y))));
						w.add(new Double(CifFileParser.parseCoord((String)u.get(z))));
						for (int k=0; k<w.size(); k++) {
							if (w.get(k) instanceof Double && (((Double)w.get(k)).isNaN())) {
								v.add("Bad Atom : "+CifFileParser.ArrayToString((String[])u.toArray(new String[0])));
								continue l;
							}
						}
						v.add(w);
					}
					else {
						v.add("Bad Atom : "+CifFileParser.ArrayToString((String[])u.toArray(new String[0])));
					}
				}
				return v;
			}
		}
		return null;
	}
}

