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
	private JMenuItem erase = new JMenuItem("New"), open = new JMenuItem("Open..."), save = new JMenuItem("Save..."),
			cut = new JMenuItem("Cut"), copy = new JMenuItem("Copy"), paste = new JMenuItem("Paste");
	private Clipboard clipbd;
	private JMenuBar menu;
	public File file;

	public CifEditor() {
		this.frame = new JFrame("CIF representation");
		this.frame.setSize(500, 600);
		this.createMenu();

		this.edit = new JTextArea();
		this.edit.setEditable(true);
		this.edit.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(this.edit);

		HVPanel.v p = new HVPanel.v();
		p.expand(true);
		p.addComp(scrollPane);

		HVPanel.h p1 = new HVPanel.h();
		p1.expand(false);
		p1.center();
		p1.addButton(this.okButton = new JButton("Apply changes"));
		p1.putExtraSpace(10);
		p1.addButton(this.cancelButton = new JButton("Close"));
		this.okButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
		p.expand(false);
		p.putExtraSpace(5);
		p.bottom();
		p.addSubPane(p1);

		this.frame.setContentPane(p.jPanel);
		this.frame.validate();
	}

	public void createMenu() {
		JMenu file = new JMenu("File");
		SecurityManager security = System.getSecurityManager();
		try {
			if (security != null)
				security.checkRead(".");
			this.menu = new JMenuBar();
			file.add(this.erase);
			file.add(this.open);
			this.menu.add(file);
			this.erase.addActionListener(this);
			this.open.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Haven't read permission");
		}
		try {
			if (security != null)
				security.checkWrite(".");
			file.add(this.save);
			this.save.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Haven't write permission");
		}
		try {
			if (security != null)
				security.checkSystemClipboardAccess();
			this.clipbd = this.frame.getToolkit().getSystemClipboard();
			if (this.menu == null)
				this.menu = new JMenuBar();
			JMenu edit = new JMenu("Edit");
			edit.add(this.cut);
			edit.add(this.copy);
			edit.add(this.paste);
			this.menu.add(edit);
			this.cut.addActionListener(this);
			this.copy.addActionListener(this);
			this.paste.addActionListener(this);
		} catch (SecurityException e) {
			System.out.println("Can't acces clipboard");
		}
		if (this.menu != null)
			this.frame.setJMenuBar(this.menu);
	}

	public void show() {
		this.frame.setVisible(true);
		this.frame.toFront();
	}

	public void show(String windowName) {
		this.frame.setTitle(windowName);
		this.show();
	}

	public void setFile(String[] data) {
		this.data = data;
		this.putToEditor(data);
	}

	private void putToEditor(String[] ss) {
		this.edit.setText("");
		for (int i = 0; i < ss.length; i++) {
			this.edit.append(ss[i]);
			this.edit.append("\n");
		}
		this.edit.setCaretPosition(0);
	}

	private String[] getEditorContent() {
		String[] ss = new String[this.edit.getLineCount()];
		try {
			for (int i = 0; i < ss.length; i++) {
				int p1 = this.edit.getLineStartOffset(i);
				int p2 = this.edit.getLineEndOffset(i);
				ss[i] = this.edit.getText(p1, p2 - p1);
				if (ss[i].length() > 0 && ss[i].charAt(ss[i].length() - 1) < 32)
					ss[i] = ss[i].substring(0, ss[i].length() - 1);
				if (ss[i].length() > 0 && ss[i].charAt(ss[i].length() - 1) < 32)
					ss[i] = ss[i].substring(0, ss[i].length() - 1);
			}
		} catch (BadLocationException e) {
			e.printStackTrace(System.err);
		}
		return ss;
	}

	public String[] getFile() {
		return this.data;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.erase)
			this.edit.setText("");
		else if (e.getSource() == this.open) {
			JFileChooser chooser = new JFileChooser();
			FileFilter filter = new CifFileFilter();
			chooser.setFileFilter(filter);
			if (chooser.showOpenDialog(this.frame) == JFileChooser.APPROVE_OPTION)
				try {
					File f = chooser.getSelectedFile();
					BufferedReader in = new BufferedReader(new FileReader(f));
					Vector v = new Vector(100, 200);
					while (true) {
						String s = in.readLine();
						if (s == null)
							break;
						v.add(s);
					}
					this.putToEditor((String[]) v.toArray(new String[0]));
					in.close();
					this.file = f;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this.frame, "Can't read file !");
					throw new RuntimeException(ex);
				}
		} else if (e.getSource() == this.save) {
			JFileChooser chooser = new JFileChooser();
			FileFilter filter = new CifFileFilter();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			if (chooser.showSaveDialog(this.frame) == JFileChooser.APPROVE_OPTION)
				try {
					File f = chooser.getSelectedFile();
					String name = f.getPath();
					if (name.toLowerCase().lastIndexOf(".cif") == -1)
						name += ".cif";
					f = new File(name);
					PrintStream out = new PrintStream(new FileOutputStream(name));
					String[] ss = this.getEditorContent();
					for (int i = 0; i < ss.length; i++)
						out.println(ss[i]);
					out.close();
					this.file = f;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this.frame, "Can't write file !");
					throw new RuntimeException(ex);
				}
		} else if (e.getSource() == this.cut) {
			String selection = this.edit.getSelectedText();
			if (selection == null)
				selection = this.edit.getText();
			if (this.clipbd != null) {
				StringSelection clipString = new StringSelection(selection);
				this.clipbd.setContents(clipString, clipString);
				this.edit.replaceRange("", this.edit.getSelectionStart(), this.edit.getSelectionEnd());
			}
		} else if (e.getSource() == this.copy) {
			String selection = this.edit.getSelectedText();
			if (selection == null)
				selection = this.edit.getText();
			if (this.clipbd != null) {
				StringSelection clipString = new StringSelection(selection);
				this.clipbd.setContents(clipString, clipString);
			}
		} else if (e.getSource() == this.paste) {
			if (this.clipbd != null)
				try {
					Transferable clipData = this.clipbd.getContents(this);
					String clipString = (String) clipData.getTransferData(DataFlavor.stringFlavor);
					this.edit.replaceRange(clipString, this.edit.getSelectionStart(), this.edit.getSelectionEnd());
				} catch (Exception ex) {
					System.err.println("Not String flavor");
				}
		} else if (e.getSource() == this.okButton) {
			this.data = this.getEditorContent();
			this.frame.setVisible(false);
			this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		} else if (e.getSource() == this.cancelButton)
			this.frame.setVisible(false);
	}

	public static void main(String[] args) throws IOException {
		URL u = CifEditor.class.getResource("/1913_Hull, W.H.;Bragg, W.L._C_F d -3 m S_Carbon.cif");
		InputStream in = u.openStream();
		CifFile cif = new CifFile(new InputStreamReader(in));

		String[] ss = CifFile.generateCif(cif.getSg(), cif.getAtoms());
		for (int i = 0; i < ss.length; i++)
			System.out.println(ss[i]);
	}
}
