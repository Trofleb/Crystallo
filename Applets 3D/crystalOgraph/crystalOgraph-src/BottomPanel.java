import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import ColorComboBox.IncompatibleLookAndFeelException;


public class BottomPanel extends HVPanel.h implements ActionListener {
	private JButton clearAll, help, diffract;
	private JButton getFromICSD, showCif;
	private JTable table;
	private ButtonEditor tableButtonEditor;
	private Model model;
	private Univers univers;
	private Shaker shaker;
	public Icsd icsd;
	public  CifViewer cifViewer;
	public Help3d help3d;
	private RightPanel rightPanel;
	private Grid grid;
	protected boolean[] lastRowIsDef;
	private JScrollPane scrollPane;
	private crystalOgraph applet;
	
	public BottomPanel(crystalOgraph applet, Univers univers, Grid grid, Model model, RightPanel rightPanel, Helper elementsCounter) {		
		this.applet = applet;
		this.model = model;
		this.grid = grid;
		this.univers = univers;
		this.rightPanel = rightPanel;

		String[] columnNames = {"Label", "Atom",	"x", "y", "z", "radius Å", "color", "show", ""};
		DefaultTableModel tableModel = new DefaultTableModel() {
      public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
      }
		};
		
		table = new JTable(tableModel);
    for(int i=0; i<columnNames.length; i++) tableModel.addColumn(columnNames[i]);
    lastRowIsDef = new boolean[columnNames.length];
    for(int i=0; i<lastRowIsDef.length; i++) lastRowIsDef[i] = true;
    tableModel.addRow(new Object[]{"", "", "0.0", "0.0", "0.0", defaultAtomRadius(model.cell), nextColor(defaultAtomColor()), null, "Add"});
		
		table.setRowHeight(20);
		table.getColumnModel().getColumn(0).setPreferredWidth(10); 
		table.getColumnModel().getColumn(1).setPreferredWidth(10); 
		table.getColumnModel().getColumn(2).setPreferredWidth(10); 
		table.getColumnModel().getColumn(3).setPreferredWidth(10); 
		table.getColumnModel().getColumn(4).setPreferredWidth(10); 
		table.getColumnModel().getColumn(5).setPreferredWidth(30); 
		table.getColumnModel().getColumn(6).setPreferredWidth(30); 
		table.getColumnModel().getColumn(7).setPreferredWidth(20); 
		table.getColumnModel().getColumn(8).setPreferredWidth(40); 

		tableButtonEditor = new ButtonEditor();
		table.getColumnModel().getColumn(0).setCellEditor(new TextEditor());
		table.getColumnModel().getColumn(1).setCellEditor(new TextEditor());
		table.getColumnModel().getColumn(2).setCellEditor(new TextEditor());
		table.getColumnModel().getColumn(3).setCellEditor(new TextEditor());
		table.getColumnModel().getColumn(4).setCellEditor(new TextEditor());
		table.getColumnModel().getColumn(5).setCellEditor(new TextEditor());
		try {
			table.getColumnModel().getColumn(6).setCellEditor(new ComboBoxEditor());
		} catch (IncompatibleLookAndFeelException e1) {
			throw new RuntimeException(e1);
		}
		table.getColumnModel().getColumn(7).setCellEditor(new CheckBoxEditor());
		table.getColumnModel().getColumn(8).setCellEditor(tableButtonEditor);
		
		table.getColumnModel().getColumn(0).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(5).setCellRenderer(new TextRenderer());
		table.getColumnModel().getColumn(6).setCellRenderer(new ColorComboBoxRenderer());
		table.getColumnModel().getColumn(7).setCellRenderer(new CheckBoxRenderer());
		table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
				
		
		
		scrollPane = new JScrollPane(table);

		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER) {
					int r = table.getSelectedRow();
					if (r==-1) return;
					if (((String)table.getValueAt(r, 8)).equals("Remove")) return;
					addRemovePressed(r, new JButton((String)table.getValueAt(r, 8)));
					table.setRowSelectionInterval(r, r);
				}
			}
		});
		
		
		showCif = new JButton("CIF file...");
	  showCif.setToolTipText("Crystal data in CIF format.");
  	getFromICSD = new JButton("Get from ICSD...");
		getFromICSD.setToolTipText("Select a crystal structure from the ICSD database.");
		clearAll = new MultiLineToolTip.JButton("Clear all");
		help = new MultiLineToolTip.JButton("Help");
		diffract = new MultiLineToolTip.JButton("Diffraction");
		
	  icsd = new Icsd(applet.getCodeBase());
	  icsd.setActionListener(this);
	  cifViewer = new CifViewer(applet);
	  cifViewer.addActionListener(this);
	  help3d = new Help3d(applet, rightPanel);
		
		setPreferredSize(new Dimension(500, 80));

	  bottom();
	  c().insets = new Insets(2, 2, 2, 2);
	  //c().insets.top = 2;
		addComp(scrollPane);
	  
	  HVPanel p1 = new HVPanel.v();
	  p1.addButton(showCif);
	  p1.addButton(getFromICSD);
	  if (crystalOgraph.isApplet) {
		  p1.addButton(diffract);
	  }
	  p1.addButton(clearAll);
	  
	  HVPanel p2 = new HVPanel.v();
		p2.addComp(elementsCounter);
	  p2.addComp(new JLabel("  "));
	  p2.addButton(help);

	  addSubPane(p1, false, false);
	  addSubPane(p2, false, false);
	}

	
	public void selectCurrentPos(Point3d pos) {
		float px = Math.round((pos.x<0?1+(pos.x%1):(pos.x%1))*100f)/100f;
		float py = Math.round((pos.y<0?1+(pos.y%1):(pos.y%1))*100f)/100f;
		float pz = Math.round((pos.z<0?1+(pos.z%1):(pos.z%1))*100f)/100f;
		
		for (int i=0; i<table.getRowCount(); i++) {
			if (((String)table.getValueAt(i, 8)).equals("Remove"))
				if (Float.parseFloat((String)table.getValueAt(i, 2))==px)
					if (Float.parseFloat((String)table.getValueAt(i, 3))==py)
						if (Float.parseFloat((String)table.getValueAt(i, 4))==pz) {
							table.setRowSelectionInterval(i, i);
							break;
						}
		}
	}

	public void clearAllAtoms() {
		table.getCellEditor(0, 8).cancelCellEditing();
		table.getCellEditor(0, 7).cancelCellEditing();
		table.getCellEditor(0, 6).cancelCellEditing();
		table.setRowSelectionInterval(0, 0);
		
		for (int i=table.getRowCount()-2; i>=0; i--) {
//			String sx = (String)table.getValueAt(i, 2);
//			String sy = (String)table.getValueAt(i, 3);
//			String sz = (String)table.getValueAt(i, 4);
			//removeAtom(sx, sy, sz);
			((DefaultTableModel)table.getModel()).removeRow(i);
		}

		model.hiddenAtomGroups.clear();
		model.hiddenLinksGroups.clear();
		model.hiddenAtomGroups.add(new Vector(50, 50));
		model.hiddenLinksGroups.add(new Vector(50, 50));
		
		model.root.removeAllChildren();
		model.atoms.clear();
		model.links.clear();
	}
	
	public void addAtomToTable(String name, String symbol, String sx, String sy, String sz, String radius, Color color) {
		if (model.hiddenAtomGroups.size()==0) {
			model.hiddenAtomGroups.add(new Vector(50, 50));
			model.hiddenLinksGroups.add(new Vector(50, 50));
		}
		
		//System.out.println("Add "+name+" "+sx+" "+sy+" "+sz+" "+radius+" "+color);
		int r = table.getRowCount()-1;
		color = setCurrentColor(color);
		radius = setCurrentRadius(radius);
		Point3d p = addAtomToModel(sx, sy, sz, r, name);
		//System.out.println(p);
		if (p==null) return;
		
		
//		sx = Atom.posToString(p.x);
//		sy = Atom.posToString(p.y);
//		sz = Atom.posToString(p.z);
		
//		sx = ""+p.x;
//		sy = ""+p.y;
//		sz = ""+p.z;

		((DefaultTableModel)table.getModel()).insertRow(r, new Object[]{name, symbol, sx, sy, sz, radius, color, new Boolean(true), "Remove"});
		model.hiddenAtomGroups.add(r, new Vector(50, 50));
		model.hiddenLinksGroups.add(r, new Vector(50, 50));
	}


	
	public Point3d addAtomToModel(String sx, String sy, String sz, int index, String label) {
		Point3d p;
		if (sx.length()==0) sx = "0";
		if (sy.length()==0) sy = "0";
		if (sz.length()==0) sz = "0";
		try {
			p = new Point3d(TextFieldCoord.parseFraqValue(sx, sy, sz));
			if (model.IsThereAnAtomTooClose(p)) {
				if (p.distance(model.getAtomTooClose().pos)<0.0001) {
					System.err.println("same pos");
				}
				else {
					Shaker.shake("Too close from another atom.");
					System.out.println(p+" "+model.getAtomTooClose().pos+" "+model.getAtomTooClose().radius+" "+model.currentSizeAtom);
				}
				p = null;
			}
			else p = model.addAtom(p, index, label);
		}
		catch (BadEntry e) {
			Shaker.shake("Bad entry in coordinates");
			p = null;
		}
		return p;
	}
	
	public void removeAtom(String sx, String sy, String sz) {
		try {
			Point3d p = new Point3d(TextFieldCoord.parseFraqValue(sx, sy, sz));
			//System.out.println(p);
			model.removeAtom(p);
		}
		catch (BadEntry e) {
			Shaker.shake("Bad entry in coordinates");
		}
	}
	
	public static Color defaultAtomColor() {
		return Color.red;
	}
	public static String defaultAtomRadius(Cell cell) {
		double r = Math.min(Math.min(cell.a, cell.b), cell.c)/15d;
		r = ((double)Math.round(r*10d))/10d;
		return ""+r;
	}
	
	
	public Color setCurrentColor(Color color) {
		if (color==null) color=defaultAtomColor();
		model.currentColorAtom = new Color3f(color);
		return color;
	}

	public String setCurrentRadius(String radius) {
		if (radius.trim().length()==0) {
			radius=defaultAtomRadius(model.cell);
		}
		try {
			model.currentSizeAtom = TextFieldCoord.parseFraqValue(radius);
			if (model.currentSizeAtom<0.02f) model.currentSizeAtom=0.02f;
			return Atom.posToString(model.currentSizeAtom);
		}
		catch (BadEntry e) {
			Shaker.shake("Bad entry for radius");
			return null;
		}
	}
	
	public void clearAll() {
		clearAllAtoms();
		rightPanel.expandPanel.reset();
		rightPanel.bondsPanel.reset();
		rightPanel.spaceGroupPanel.reset();
		rightPanel.cellConstraintsPanel.reset();
		rightPanel.bkgColorPanel.reset();
		model.boundUnder_hard(0);
		model.cell.setDefault();
		grid.reset();
		univers.reset();
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==getFromICSD) {
			icsd.show(true);
		}

		else if (event.getSource()==clearAll) {
			clearAll();
		}

		else if (event.getSource()==help) {
			help3d.show();
		}
		else if (event.getSource()==diffract) {
			try {
				URL u = new URL("http://escher.epfl.ch/servlet/crystalOgraph/cif");
				URLConnection con = u.openConnection();
				con.setDoOutput(true);
				PrintWriter out = new PrintWriter(con.getOutputStream());
				if (!rightPanel.IsJustImported()) cifViewer.generateCif(model.cell, table);
				String[] ss = cifViewer.getFile();
				for (int i=0; i<ss.length; i++) {
					out.println(ss[i]);
				}
				out.close();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while (true) {
					String s = in.readLine();
					if (s==null) break;
					System.out.println(s);
					if (s.equals("ok")) {
						//boolean newWindow = event.getModifiers()!=0;
						applet.getAppletContext().showDocument(new URL("http://escher.epfl.ch/reciprOgraph/openCif.html"), "reciprOgraph");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		else if (event.getSource()==showCif) {
			cifViewer.show(true);
			if (!rightPanel.IsJustImported()) cifViewer.generateCif(model.cell, table);
			cifViewer.showFile();
		}
		
		else if (event.getSource()==cifViewer) {
/*
			String[] ss = cifViewer.getFile();
			for (int i=0; i<ss.length; i++)
				System.out.println(ss[i]);
*/				
			Shaker.quiet=true;
			updateModelFromCIF();
			Shaker.quiet=false;
			rightPanel.modelIsNowImported();
			String s = cifViewer.getFormulaFromCif();
			applet.frame.setTitle("crystalOgraph"+(s.trim().length()==0?"":(" - "+s)));
		}
		
		else if (event.getSource()==icsd) {
			if (icsd.data!=null) cifViewer.setFile(icsd.data);
			Shaker.quiet=true;
			updateModelFromCIF();
			Shaker.quiet=false;
			rightPanel.modelIsNowImported();
			String s = cifViewer.getFormulaFromCif();
			applet.frame.setTitle("crystalOgraph"+(s.trim().length()==0?"":(" - "+s)));
		}
	}

	Color[] colors = {Color.red, Color.blue, Color.green, Color.yellow, Color.white, Color.magenta, Color.cyan};
	public Color nextColor(Color c) {
		for (int i=0; i<colors.length; i++)
			if (colors[i].equals(c)) return colors[(i+1)%colors.length];
		return colors[0];
	}
	
	public void loadCifFile(InputStream in) throws IOException {
		Vector v = new Vector(100, 50);
		BufferedReader stream = new BufferedReader(new InputStreamReader(in));
		while (true) {						
			String currentLine = stream.readLine();
			if (currentLine==null) break;
			v.add(currentLine);
		}
		stream.close();
		String[] ss = (String[])v.toArray(new String[0]);
		if (!cifViewer.checkFile(ss)) throw new RuntimeException();
		
		cifViewer.setFile(ss);
		Shaker.quiet=true;
		updateModelFromCIF();
		Shaker.quiet=false;
		rightPanel.modelIsNowImported();
		String s = cifViewer.getFormulaFromCif();
		applet.frame.setTitle("crystalOgraph"+(s.trim().length()==0?"":(" - "+s)));
	}
	
	public void updateModelFromCIF() {
		clearAllAtoms();
		model.boundUnder_hard(0);
		rightPanel.expandPanel.reset();
		univers.hideRoot();
		//rightPanel.bkgColorPanel.reset();
		cifViewer.getCellFromCIF2(model.cell);
		
		
		//System.out.println(Cell.currentCell.no);
		
		rightPanel.updateCell(model.cell);

		
		
		univers.reset();
		univers.showRoot();
		
		Vector aa = cifViewer.getAtomsFromCIF2();
		
		// removing error lines
		for (int i=0; i<aa.size(); i++) {
			if (!(aa.get(i) instanceof Vector)) {
				System.err.println(aa.remove(i));
			}
		}

		// merging symb and label to name
		for (int i=0; i<aa.size(); i++) {
			Vector a = (Vector)aa.get(i);
			a.add(a.get(0)==null?a.get(1):a.get(0));
		}
		
		// merging atoms with identical pos
		for (int i=0; i<aa.size(); i++) {
			Vector ai = (Vector)aa.get(i);
			Point3d pi = new Point3d(((Double)ai.get(2)).doubleValue(), ((Double)ai.get(3)).doubleValue(), ((Double)ai.get(4)).doubleValue()); 
			for (int j=0; j<i; j++) {
				Vector aj = (Vector)aa.get(j);
				Point3d pj = new Point3d(((Double)aj.get(2)).doubleValue(), ((Double)aj.get(3)).doubleValue(), ((Double)aj.get(4)).doubleValue()); 
				if (pj.distance(pi)<0.0001) {
					if (aj.get(5)!=null && ai.get(5)!=null && !aj.get(5).equals(ai.get(5))) aj.set(5, aj.get(5)+", "+ai.get(5));
					if (aj.get(1)!=null && ai.get(1)!=null && !aj.get(1).equals(ai.get(1))) aj.set(1, aj.get(1)+", "+ai.get(1));
					if (aj.get(0)!=null && ai.get(0)!=null && !aj.get(0).equals(ai.get(0))) aj.set(0, aj.get(0)+", "+ai.get(0));
					aa.remove(i);
					i--;
					break;
				}
			}		
		}		

		// adding colors
		Vector AtomsToColor = new Vector(5, 5);
		int k=0;
		for (int i=0; i<aa.size(); i++) {
			Vector a = (Vector)aa.get(i);
			String symb = (String) a.get(1); 
			if (symb==null || symb.length()==0) {
				Color c = colors[k++%colors.length];
				a.add(c);
			}
			else {
				if (!AtomsToColor.contains(symb)) AtomsToColor.add(symb);
				Color c = colors[AtomsToColor.indexOf((String)a.get(1))%colors.length];
				a.add(c);
			}
		}		

		// adding atoms to table
		for (int i=0; i<aa.size(); i++) {
			Vector a = (Vector)aa.get(i);
			addAtomToTable(""+a.get(0), ""+a.get(1), ""+a.get(2), ""+a.get(3), ""+a.get(4), "", (Color)a.get(6));
		}
	}
	
	
	public void addRemovePressed(int currentRow, JButton editor) {
		rightPanel.modelIsNowModified();
		if (editor.getText().equals("Add")) {
			Color c = setCurrentColor((Color)table.getValueAt(currentRow, 6));
			String r = setCurrentRadius((String)table.getValueAt(currentRow, 5));
			Point3d p = addAtomToModel((String)table.getValueAt(currentRow, 2), (String)table.getValueAt(currentRow, 3), (String)table.getValueAt(currentRow, 4), currentRow, (String)table.getValueAt(currentRow, 0));
			if (p!=null) {
				//((DefaultTableModel)table.getModel()).setValueAt("Remove", currentRow, 6);
				table.setValueAt("Remove", currentRow, 8);
				table.setValueAt(Atom.posToString(p.x), currentRow, 2);
				table.setValueAt(Atom.posToString(p.y), currentRow, 3);
				table.setValueAt(Atom.posToString(p.z), currentRow, 4);
				table.setValueAt(c, currentRow, 6);
				table.setValueAt(r, currentRow, 5);
				table.setValueAt(new Boolean(true), currentRow, 7);
				editor.setText("Remove");
				//((DefaultTableModel)table.getModel()).addRow(new Object[]{"", (String)table.getValueAt(currentRow, 1), (String)table.getValueAt(currentRow, 2), (String)table.getValueAt(currentRow, 3), r, c, "Add"});
				((DefaultTableModel)table.getModel()).addRow(new Object[]{"", "", "0.0", "0.0", "0.0", r, nextColor(c), null, "Add"});
				for(int i=0; i<lastRowIsDef.length; i++) lastRowIsDef[i] = true;

				scrollPane.getViewport().scrollRectToVisible(table.getCellRect(table.getRowCount()-1, 0, true));
				table.changeSelection(currentRow+1, 0, false, false);
				model.hiddenAtomGroups.add(new Vector(50, 50));
				model.hiddenLinksGroups.add(new Vector(50, 50));
			}
		}
		else if (editor.getText().equals("Remove")) {
			
			//System.out.println("del "+(String)table.getValueAt(currentRow, 1)+" "+ (String)table.getValueAt(currentRow, 2)+ " "+ (String)table.getValueAt(currentRow, 3));
			
			removeAtom((String)table.getValueAt(currentRow, 2), (String)table.getValueAt(currentRow, 3), (String)table.getValueAt(currentRow, 4));
			((DefaultTableModel)table.getModel()).removeRow(currentRow);
			
			model.hiddenAtomGroups.remove(currentRow);
			model.hiddenLinksGroups.remove(currentRow);
			
			if (currentRow==table.getRowCount()-1) {
				editor.setText("Add");
				table.setValueAt("Add", currentRow, 8);
			}
			else {
				editor.setText("Remove");
				table.setValueAt("Remove", currentRow, 8);
				
			}
		}
	}
	
	
	class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		private JButton editor;
		JTable table;
		int currentRow;

		public ButtonEditor() {
			editor = new JButton();
			editor.setMargin(new Insets(editor.getMargin().top, 0, editor.getMargin().bottom, 0));
			editor.addActionListener(this);
			Insets margins = new Insets(0, 0, 0, 0);
			editor.setMargin(margins);
		}

		
		
		public void actionPerformed(ActionEvent e) {
			addRemovePressed(currentRow, editor);
		}

	/*		
		public void cancelCellEditing() {
		//public void enSafe() {
			//editor.setText((String)table.getValueAt(currentRow, 6));
			super.cancelCellEditing();
		}
	*/		
		
		public Object getCellEditorValue() {
			return editor.getText();
		}

		public JButton editor() {
			return editor;
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
			this.table=table;
			currentRow=row;
			editor.setText((String)value);
	    return editor;
		}

	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setMargin(new Insets(getMargin().top, 0, getMargin().bottom, 0));
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			setText((String)value);
			return this;
		}
	}
	
	class TextRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			c.setForeground(row==table.getRowCount()-1&&lastRowIsDef[column]?Color.lightGray:Color.black);
			return c;
		}
	}

	
	class ColorComboBoxRenderer extends DefaultTableCellRenderer {
	  class Renderer extends Component {
	    private java.awt.Color color;

	    public void setColor(java.awt.Color color1) {
	      color = color1;
	    }
	    public java.awt.Color getColor() {
	      return color;
	    }
	    public void paint(java.awt.Graphics g) {
	      g.setColor(color);
	      g.fillRect(3, 3, getWidth()-7, getHeight()-7);
	      g.setColor(java.awt.Color.darkGray);
	      g.draw3DRect(3, 3, getWidth()-7, getHeight()-7, true);
	    }
	  }

	  protected Renderer renderer;

	  public ColorComboBoxRenderer() {
	    renderer = new Renderer();
	  }
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
	  	renderer.setColor((java.awt.Color)value);
	    return ((java.awt.Component) (renderer));
		}
	}
	
	

	class TextEditor extends AbstractCellEditor implements TableCellEditor {
		JTextField f; 
		String prevVal;
		JTable table;
		int currentRow;
		int currentCol;
		
		public TextEditor() {
			 f = new JTextField();
			 f.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode()==KeyEvent.VK_ENTER) {
							stopCellEditing();
							if (!((String)table.getValueAt(currentRow, 8)).equals("Remove"))
								addRemovePressed(currentRow, new JButton((String)table.getValueAt(currentRow, 8)));
						}
					}
			 });
		}

		public Object getCellEditorValue() {
			return f.getText();
		}

		public boolean stopCellEditing() {
			if (!prevVal.equals(f.getText())) {
				lastRowIsDef[currentCol] = false;
				//System.out.println("value changed:"+f.getText());
				if ("Remove".equals(table.getValueAt(currentRow, 8))) {
					rightPanel.modelIsNowModified();
					String[] v = new String[6];
					Color color;
					for (int i=0; i<v.length; i++) {
						v[i] = (String)table.getValueAt(currentRow, i);
					}
					color = (Color)table.getValueAt(currentRow, 6);
					
					//if (currentCol!=0) {
						model.unHide(currentRow);
						table.setValueAt(new Boolean(true), currentRow, 7);
						
						removeAtom(v[2], v[3], v[4]);
						String oldx=v[2], oldy=v[3], oldz=v[4];
						v[currentCol] = f.getText();
						setCurrentColor(color);
						
						String r = setCurrentRadius(v[5]);
						if (r==null) r = (String)table.getValueAt(currentRow, 5);
						table.setValueAt(r, currentRow, 5);
						if (currentCol==4) f.setText(r);
						Point3d p = addAtomToModel(v[2], v[3], v[4], currentRow, v[0]);
						if (p==null) {
							addAtomToModel(oldx, oldy, oldz, currentRow, v[0]);
							v[2] = (String)table.getValueAt(currentRow, 2);
							v[3] = (String)table.getValueAt(currentRow, 3);
							v[4] = (String)table.getValueAt(currentRow, 4);
						}
						else {
							v [2] = Atom.posToString(p.x);
							v [3] = Atom.posToString(p.y);
							v [4] = Atom.posToString(p.z);
						}
						if (currentCol==2) f.setText(v[2]);
						if (currentCol==3) f.setText(v[3]);
						if (currentCol==4) f.setText(v[4]);
					//}
					if (currentCol==2 || currentCol==3 || currentCol==4) {
						rightPanel.bondsPanel.removeBonds();
					}
					if (currentCol==5) {
						rightPanel.bondsPanel.rebond();
					}
				}
			}
			return super.stopCellEditing();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object data, boolean isSelected, int row, int column) {
			this.table=table;
			currentRow=row;
			currentCol=column;
			prevVal = (String) data;
			f.setText((String) data);
			f.selectAll();			
			return f;
		}
	}


	class CheckBoxEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
//		public void cancelCellEditing() {
//			p.setBackground(table.getBackground());
//			editor.setBackground(table.getBackground());
//			super.cancelCellEditing();
//		}
//		public boolean stopCellEditing() {
//			p.setBackground(table.getBackground());
//			editor.setBackground(table.getBackground());
//			return super.stopCellEditing();
//		}
		private JCheckBox editor;
		private JPanel p;
		private int row;
		private JTable table;
		public CheckBoxEditor() {
			editor = new JCheckBox();
			p = new JPanel();
			editor.addActionListener(this);
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER); 
			layout.setVgap(0);
			p.setLayout(layout);
			p.add(editor);
		}
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
			this.row=row;
			this.table=table;
			editor.setVisible(value!=null); 
			if (value!=null) editor.setSelected(((Boolean)value).booleanValue());
			p.setBackground(table.getSelectionBackground());
			editor.setBackground(table.getSelectionBackground());
			return p;
		}
		public Object getCellEditorValue() {
			if (!editor.isVisible()) return null;
			return new Boolean(editor.isSelected());
		}
		public void actionPerformed(ActionEvent e) {
			table.setValueAt(editor.isVisible()?(editor.isSelected()?new Boolean(true):new Boolean(false)):null, row, 7);
			if (row==table.getRowCount()-1) return;
			if (editor.isSelected()) {
				model.unHide(row);
			}
			else {
				model.hideAtom(row);
			}
		}
	}
	class CheckBoxRenderer extends JPanel implements TableCellRenderer {
		JCheckBox checkBox;
		public CheckBoxRenderer() {
			checkBox = new JCheckBox();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER); 
			layout.setVgap(0);
			setLayout(layout);
			add(checkBox);
		}
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			checkBox.setVisible(value!=null); 
			if (value!=null) checkBox.setSelected(((Boolean)value).booleanValue());
			checkBox.setBackground(selected?table.getSelectionBackground():table.getBackground()); 
			setBackground(selected?table.getSelectionBackground():table.getBackground()); 
			return this;
		}
	}


	class ComboBoxEditor extends DefaultCellEditor implements TableCellEditor {
		Color prevVal;
		JTable table;
		int currentRow;
		int currentCol;
		
		public ComboBoxEditor() throws IncompatibleLookAndFeelException {
			super(new ColorChoice());
			((JComboBox)getComponent()).setLightWeightPopupEnabled(false);
			prevVal=null;
			 addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode()==KeyEvent.VK_ENTER) {
						stopCellEditing();
						if (!((String)table.getValueAt(currentRow, 8)).equals("Remove"))
							addRemovePressed(currentRow, new JButton((String)table.getValueAt(currentRow, 8)));
					}
				}
		 });
		}

		public boolean stopCellEditing() {
			if (prevVal!=((ColorChoice)getComponent()).getSelectedColor()) {
				lastRowIsDef[currentCol] = false;
				if ("Remove".equals(table.getValueAt(currentRow, 8))) {

					model.unHide(currentRow);
					table.setValueAt(new Boolean(true), currentRow, 7);
					
					String l = (String)table.getValueAt(currentRow, 0);
					String x = (String)table.getValueAt(currentRow, 2);
					String y = (String)table.getValueAt(currentRow, 3);
					String z = (String)table.getValueAt(currentRow, 4);
					String r = (String)table.getValueAt(currentRow, 5);
					Color  c = ((ColorChoice)getComponent()).getSelectedColor();
					removeAtom(x, y, z);
					setCurrentColor(c);
					setCurrentRadius(r);
					
					addAtomToModel(x, y, z, currentRow, l);
					
					rightPanel.bondsPanel.rebond();
				}
			}
			return super.stopCellEditing();
		}
		
		public Component getTableCellEditorComponent(JTable table, Object data, boolean isSelected, int row, int column) {
			this.table=table;
			currentRow=row;
			currentCol=column;
			prevVal = (Color)data;
			((ColorChoice)this.getComponent()).setSelectedColor((Color)data);
			return super.getTableCellEditorComponent(table, data, isSelected, row, column);
		}
	}
	
}







