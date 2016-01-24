package structures;

import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

import utils.HVPanel;



public class CifEditor implements ActionListener {
	private ActionListener actionListener;
	private JFrame frame;
	private JButton okButton;
	private JButton cancelButton;
	private String[] data;
	private JTextArea edit;
	private JMenuItem
		erase = new JMenuItem("New"),
		open = new JMenuItem("Open..."),
		save = new JMenuItem("Save..."),
		cut = new JMenuItem("Cut"),
		copy = new JMenuItem("Copy"),
		paste = new JMenuItem("Paste");
	private Clipboard clipbd;
	private JMenuBar menu;
	public File file;
	
  public CifEditor() {
		frame = new JFrame("CIF representation");
		frame.setSize(500, 600);
		createMenu();

		edit = new JTextArea();
		edit.setEditable(true);
		edit.setMargin(new Insets(5, 5, 5, 5));
    JScrollPane scrollPane = new JScrollPane(edit);

    HVPanel.v p = new HVPanel.v();
		p.expand(true);
    p.addComp(scrollPane);

    HVPanel.h p1 = new HVPanel.h();
    p1.expand(false);
    p1.center();
		p1.addButton(okButton=new JButton("Apply changes"));
		p1.putExtraSpace(10);
		p1.addButton(cancelButton = new JButton("Close"));
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
    p.expand(false);
		p.putExtraSpace(5);
    p.bottom();
    p.addSubPane(p1);

		frame.setContentPane(p.jPanel);
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
  
	public void show() {
		frame.setVisible(true);
		frame.toFront();
	}
	public void show(String windowName) {
		frame.setTitle(windowName);
		show();
	}
	
	public void setFile(String[] data) {
		this.data=data;
		putToEditor(data);
	}
	
	private void putToEditor(String[] ss) {
		edit.setText("");
		for (int i=0; i<ss.length; i++) {
			edit.append(ss[i]);
			edit.append("\n");
		}
		edit.setCaretPosition(0);
	}
	
	private String[] getEditorContent() {
		String[] ss = new String[edit.getLineCount()];
		try {
			for (int i=0; i<ss.length; i++) {
				int p1 = edit.getLineStartOffset(i);
				int p2 = edit.getLineEndOffset(i);
				ss[i] = edit.getText(p1, p2-p1);
				if (ss[i].length()>0 && ss[i].charAt(ss[i].length()-1) < 32) ss[i] = ss[i].substring(0, ss[i].length()-1);
				if (ss[i].length()>0 && ss[i].charAt(ss[i].length()-1) < 32) ss[i] = ss[i].substring(0, ss[i].length()-1);
			}
		} catch (BadLocationException e) {
			e.printStackTrace(System.err);
		}
		return ss;
	}
	
	public String[] getFile() {
		return data;
	}
	
	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
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
					File f = chooser.getSelectedFile();
					BufferedReader in = new BufferedReader(new FileReader(f));
					Vector v = new Vector(100, 200);
					while (true) {						
						String s = in.readLine();
						if (s==null) break;
						v.add(s);
					}
					putToEditor((String[]) v.toArray(new String[0]));
					in.close();
					file = f;
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Can't read file !");
					throw new RuntimeException(ex);
				}
			}
		}
		else if (e.getSource()==save) {
			JFileChooser chooser = new JFileChooser();
			FileFilter filter = new CifFileFilter();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			if(chooser.showSaveDialog(frame)==JFileChooser.APPROVE_OPTION) {
				try {
					File f = chooser.getSelectedFile();
					String name = f.getPath();
					if (name.toLowerCase().lastIndexOf(".cif")==-1) name+=".cif";
					f = new File(name);
					PrintStream out = new PrintStream(new FileOutputStream(name));
					String[] ss = getEditorContent();
					for (int i=0; i<ss.length; i++) {
						out.println(ss[i]);
					}
					out.close();
					file = f;
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Can't write file !");
					throw new RuntimeException(ex);
				}
			}
		}
		else if (e.getSource()==cut) {
			String selection = edit.getSelectedText();
			if(selection == null) selection = edit.getText();
			if (clipbd!=null) {
				StringSelection clipString = new StringSelection(selection);
				clipbd.setContents(clipString, clipString);
				edit.replaceRange("", edit.getSelectionStart(), edit.getSelectionEnd());
			}
		}
		else if (e.getSource()==copy) {
			String selection = edit.getSelectedText();
			if(selection == null) selection = edit.getText();
			if (clipbd!=null) {
				StringSelection clipString = new StringSelection(selection);
				clipbd.setContents(clipString, clipString);
			}
		}
		else if (e.getSource()==paste) {
			if (clipbd!=null) {
				try{
					Transferable clipData = clipbd.getContents(this);
					String clipString = (String)clipData.getTransferData(DataFlavor.stringFlavor);
					edit.replaceRange(clipString, edit.getSelectionStart(), edit.getSelectionEnd());
				} catch(Exception ex) {
					System.err.println("Not String flavor");
				}
			}
		}
		else if (e.getSource()==okButton) {
			data = getEditorContent();
			frame.setVisible(false);
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		}
		else if (e.getSource()==cancelButton) {
			frame.setVisible(false);
		}
	}
		
		public static void main(String[] args) throws IOException {
			URL u = CifEditor.class.getResource("/1913_Hull, W.H.;Bragg, W.L._C_F d -3 m S_Carbon.cif");
			InputStream in = u.openStream();
			CifFile cif = new CifFile(new InputStreamReader(in));
			
			String[] ss = CifFile.generateCif(cif.getSg(), cif.getAtoms());
			for (int i=0; i<ss.length; i++) {
				System.out.println(ss[i]);
			}
		}
	}
