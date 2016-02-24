import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

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
	public Icsd icsd;
	public CifViewer cifViewer;
	public Help3d help3d;
	private RightPanel rightPanel;
	private Grid grid;
	protected boolean[] lastRowIsDef;
	private JScrollPane scrollPane;
	private crystalOgraph applet;

	public BottomPanel(crystalOgraph applet, Univers univers, Grid grid, Model model, RightPanel rightPanel,
			Helper elementsCounter) {
		this.applet = applet;
		this.model = model;
		this.grid = grid;
		this.univers = univers;
		this.rightPanel = rightPanel;

		String[] columnNames = { "Label", "Atom", "x", "y", "z", "radius Å", "color", "show", "" };
		DefaultTableModel tableModel = new DefaultTableModel() {
			@Override
			public Class getColumnClass(int c) {
				return this.getValueAt(0, c).getClass();
			}
		};

		this.table = new JTable(tableModel);
		for (int i = 0; i < columnNames.length; i++)
			tableModel.addColumn(columnNames[i]);
		this.lastRowIsDef = new boolean[columnNames.length];
		for (int i = 0; i < this.lastRowIsDef.length; i++)
			this.lastRowIsDef[i] = true;
		tableModel.addRow(new Object[] { "", "", "0.0", "0.0", "0.0", defaultAtomRadius(model.cell),
				this.nextColor(defaultAtomColor()), null, "Add" });

		this.table.setRowHeight(20);
		this.table.getColumnModel().getColumn(0).setPreferredWidth(10);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(10);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(10);
		this.table.getColumnModel().getColumn(3).setPreferredWidth(10);
		this.table.getColumnModel().getColumn(4).setPreferredWidth(10);
		this.table.getColumnModel().getColumn(5).setPreferredWidth(30);
		this.table.getColumnModel().getColumn(6).setPreferredWidth(30);
		this.table.getColumnModel().getColumn(7).setPreferredWidth(20);
		this.table.getColumnModel().getColumn(8).setPreferredWidth(40);

		this.tableButtonEditor = new ButtonEditor();
		this.table.getColumnModel().getColumn(0).setCellEditor(new TextEditor());
		this.table.getColumnModel().getColumn(1).setCellEditor(new TextEditor());
		this.table.getColumnModel().getColumn(2).setCellEditor(new TextEditor());
		this.table.getColumnModel().getColumn(3).setCellEditor(new TextEditor());
		this.table.getColumnModel().getColumn(4).setCellEditor(new TextEditor());
		this.table.getColumnModel().getColumn(5).setCellEditor(new TextEditor());
		try {
			this.table.getColumnModel().getColumn(6).setCellEditor(new ComboBoxEditor());
		} catch (IncompatibleLookAndFeelException e1) {
			throw new RuntimeException(e1);
		}
		this.table.getColumnModel().getColumn(7).setCellEditor(new CheckBoxEditor());
		this.table.getColumnModel().getColumn(8).setCellEditor(this.tableButtonEditor);

		this.table.getColumnModel().getColumn(0).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(1).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(2).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(3).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(4).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(5).setCellRenderer(new TextRenderer());
		this.table.getColumnModel().getColumn(6).setCellRenderer(new ColorComboBoxRenderer());
		this.table.getColumnModel().getColumn(7).setCellRenderer(new CheckBoxRenderer());
		this.table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());

		this.scrollPane = new JScrollPane(this.table);

		this.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					int r = BottomPanel.this.table.getSelectedRow();
					if (r == -1)
						return;
					if (((String) BottomPanel.this.table.getValueAt(r, 8)).equals("Remove"))
						return;
					BottomPanel.this.addRemovePressed(r, new JButton((String) BottomPanel.this.table.getValueAt(r, 8)));
					BottomPanel.this.table.setRowSelectionInterval(r, r);
				}
			}
		});

		this.showCif = new JButton("CIF file...");
		this.showCif.setToolTipText("Crystal data in CIF format.");
		this.getFromICSD = new JButton("Get from ICSD...");
		this.getFromICSD.setToolTipText("Select a crystal structure from the ICSD database.");
		this.clearAll = new MultiLineToolTip.JButton("Clear all");
		this.help = new MultiLineToolTip.JButton("Help");
		this.diffract = new MultiLineToolTip.JButton("Diffraction");

		this.icsd = new Icsd(applet.getCodeBase());
		this.icsd.setActionListener(this);
		this.cifViewer = new CifViewer(applet);
		this.cifViewer.addActionListener(this);
		this.help3d = new Help3d(applet, rightPanel);

		this.setPreferredSize(new Dimension(500, 80));

		this.bottom();
		this.c().insets = new Insets(2, 2, 2, 2);
		// c().insets.top = 2;
		this.addComp(this.scrollPane);

		HVPanel p1 = new HVPanel.v();
		p1.addButton(this.showCif);
		p1.addButton(this.getFromICSD);
		if (crystalOgraph.isApplet)
			p1.addButton(this.diffract);
		p1.addButton(this.clearAll);

		HVPanel p2 = new HVPanel.v();
		p2.addComp(elementsCounter);
		p2.addComp(new JLabel("  "));
		p2.addButton(this.help);

		this.addSubPane(p1, false, false);
		this.addSubPane(p2, false, false);
	}

	public void selectCurrentPos(Point3d pos) {
		float px = Math.round((pos.x < 0 ? 1 + (pos.x % 1) : (pos.x % 1)) * 100f) / 100f;
		float py = Math.round((pos.y < 0 ? 1 + (pos.y % 1) : (pos.y % 1)) * 100f) / 100f;
		float pz = Math.round((pos.z < 0 ? 1 + (pos.z % 1) : (pos.z % 1)) * 100f) / 100f;

		for (int i = 0; i < this.table.getRowCount(); i++)
			if (((String) this.table.getValueAt(i, 8)).equals("Remove"))
				if (Float.parseFloat((String) this.table.getValueAt(i, 2)) == px)
					if (Float.parseFloat((String) this.table.getValueAt(i, 3)) == py)
						if (Float.parseFloat((String) this.table.getValueAt(i, 4)) == pz) {
							this.table.setRowSelectionInterval(i, i);
							break;
						}
	}

	public void clearAllAtoms() {
		this.table.getCellEditor(0, 8).cancelCellEditing();
		this.table.getCellEditor(0, 7).cancelCellEditing();
		this.table.getCellEditor(0, 6).cancelCellEditing();
		this.table.setRowSelectionInterval(0, 0);

		for (int i = this.table.getRowCount() - 2; i >= 0; i--)
			// String sx = (String)table.getValueAt(i, 2);
			// String sy = (String)table.getValueAt(i, 3);
			// String sz = (String)table.getValueAt(i, 4);
			// removeAtom(sx, sy, sz);
			((DefaultTableModel) this.table.getModel()).removeRow(i);

		this.model.hiddenAtomGroups.clear();
		this.model.hiddenLinksGroups.clear();
		this.model.hiddenAtomGroups.add(new Vector(50, 50));
		this.model.hiddenLinksGroups.add(new Vector(50, 50));

		this.model.root.removeAllChildren();
		this.model.atoms.clear();
		this.model.links.clear();
	}

	public void addAtomToTable(String name, String symbol, String sx, String sy, String sz, String radius,
			Color color) {
		if (this.model.hiddenAtomGroups.size() == 0) {
			this.model.hiddenAtomGroups.add(new Vector(50, 50));
			this.model.hiddenLinksGroups.add(new Vector(50, 50));
		}

		// System.out.println("Add "+name+" "+sx+" "+sy+" "+sz+" "+radius+"
		// "+color);
		int r = this.table.getRowCount() - 1;
		color = this.setCurrentColor(color);
		radius = this.setCurrentRadius(radius);
		Point3d p = this.addAtomToModel(sx, sy, sz, r, name);
		// System.out.println(p);
		if (p == null)
			return;

		// sx = Atom.posToString(p.x);
		// sy = Atom.posToString(p.y);
		// sz = Atom.posToString(p.z);

		// sx = ""+p.x;
		// sy = ""+p.y;
		// sz = ""+p.z;

		((DefaultTableModel) this.table.getModel()).insertRow(r,
				new Object[] { name, symbol, sx, sy, sz, radius, color, new Boolean(true), "Remove" });
		this.model.hiddenAtomGroups.add(r, new Vector(50, 50));
		this.model.hiddenLinksGroups.add(r, new Vector(50, 50));
	}

	public Point3d addAtomToModel(String sx, String sy, String sz, int index, String label) {
		Point3d p;
		if (sx.length() == 0)
			sx = "0";
		if (sy.length() == 0)
			sy = "0";
		if (sz.length() == 0)
			sz = "0";
		try {
			p = new Point3d(TextFieldCoord.parseFraqValue(sx, sy, sz));
			if (this.model.IsThereAnAtomTooClose(p)) {
				if (p.distance(this.model.getAtomTooClose().pos) < 0.0001)
					System.err.println("same pos");
				else {
					Shaker.shake("Too close from another atom.");
					System.out.println(p + " " + this.model.getAtomTooClose().pos + " "
							+ this.model.getAtomTooClose().radius + " " + this.model.currentSizeAtom);
				}
				p = null;
			} else
				p = this.model.addAtom(p, index, label);
		} catch (BadEntry e) {
			Shaker.shake("Bad entry in coordinates");
			p = null;
		}
		return p;
	}

	public void removeAtom(String sx, String sy, String sz) {
		try {
			Point3d p = new Point3d(TextFieldCoord.parseFraqValue(sx, sy, sz));
			// System.out.println(p);
			this.model.removeAtom(p);
		} catch (BadEntry e) {
			Shaker.shake("Bad entry in coordinates");
		}
	}

	public static Color defaultAtomColor() {
		return Color.red;
	}

	public static String defaultAtomRadius(Cell cell) {
		double r = Math.min(Math.min(cell.a, cell.b), cell.c) / 15d;
		r = (Math.round(r * 10d)) / 10d;
		return "" + r;
	}

	public Color setCurrentColor(Color color) {
		if (color == null)
			color = defaultAtomColor();
		this.model.currentColorAtom = new Color3f(color);
		return color;
	}

	public String setCurrentRadius(String radius) {
		if (radius.trim().length() == 0)
			radius = defaultAtomRadius(this.model.cell);
		try {
			this.model.currentSizeAtom = TextFieldCoord.parseFraqValue(radius);
			if (this.model.currentSizeAtom < 0.02f)
				this.model.currentSizeAtom = 0.02f;
			return Atom.posToString(this.model.currentSizeAtom);
		} catch (BadEntry e) {
			Shaker.shake("Bad entry for radius");
			return null;
		}
	}

	public void clearAll() {
		this.clearAllAtoms();
		this.rightPanel.expandPanel.reset();
		this.rightPanel.bondsPanel.reset();
		this.rightPanel.spaceGroupPanel.reset();
		this.rightPanel.cellConstraintsPanel.reset();
		this.rightPanel.bkgColorPanel.reset();
		this.model.boundUnder_hard(0);
		this.model.cell.setDefault();
		this.grid.reset();
		this.univers.reset();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.getFromICSD)
			this.icsd.show(true);
		else if (event.getSource() == this.clearAll)
			this.clearAll();
		else if (event.getSource() == this.help)
			this.help3d.show();
		else if (event.getSource() == this.diffract)
			try {
				URL u = new URL("http://escher.epfl.ch/servlet/crystalOgraph/cif");
				URLConnection con = u.openConnection();
				con.setDoOutput(true);
				PrintWriter out = new PrintWriter(con.getOutputStream());
				if (!this.rightPanel.IsJustImported())
					this.cifViewer.generateCif(this.model.cell, this.table);
				String[] ss = this.cifViewer.getFile();
				for (int i = 0; i < ss.length; i++)
					out.println(ss[i]);
				out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while (true) {
					String s = in.readLine();
					if (s == null)
						break;
					System.out.println(s);
					if (s.equals("ok"))
						// boolean newWindow = event.getModifiers()!=0;
						this.applet.getAppletContext().showDocument(
								new URL("http://escher.epfl.ch/reciprOgraph/openCif.html"), "reciprOgraph");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		else if (event.getSource() == this.showCif) {
			this.cifViewer.show(true);
			if (!this.rightPanel.IsJustImported())
				this.cifViewer.generateCif(this.model.cell, this.table);
			this.cifViewer.showFile();
		}

		else if (event.getSource() == this.cifViewer) {
			/*
			 * String[] ss = cifViewer.getFile();
			 * for (int i=0; i<ss.length; i++)
			 * System.out.println(ss[i]);
			 */
			Shaker.quiet = true;
			this.updateModelFromCIF();
			Shaker.quiet = false;
			this.rightPanel.modelIsNowImported();
			String s = this.cifViewer.getFormulaFromCif();
			this.applet.frame.setTitle("crystalOgraph" + (s.trim().length() == 0 ? "" : (" - " + s)));
		}

		else if (event.getSource() == this.icsd) {
			if (this.icsd.data != null)
				this.cifViewer.setFile(this.icsd.data);
			Shaker.quiet = true;
			this.updateModelFromCIF();
			Shaker.quiet = false;
			this.rightPanel.modelIsNowImported();
			String s = this.cifViewer.getFormulaFromCif();
			this.applet.frame.setTitle("crystalOgraph" + (s.trim().length() == 0 ? "" : (" - " + s)));
		}
	}

	Color[] colors = { Color.red, Color.blue, Color.green, Color.yellow, Color.white, Color.magenta, Color.cyan };

	public Color nextColor(Color c) {
		for (int i = 0; i < this.colors.length; i++)
			if (this.colors[i].equals(c))
				return this.colors[(i + 1) % this.colors.length];
		return this.colors[0];
	}

	public void loadCifFile(InputStream in) throws IOException {
		Vector v = new Vector(100, 50);
		BufferedReader stream = new BufferedReader(new InputStreamReader(in));
		while (true) {
			String currentLine = stream.readLine();
			if (currentLine == null)
				break;
			v.add(currentLine);
		}
		stream.close();
		String[] ss = (String[]) v.toArray(new String[0]);
		if (!this.cifViewer.checkFile(ss))
			throw new RuntimeException();

		this.cifViewer.setFile(ss);
		Shaker.quiet = true;
		this.updateModelFromCIF();
		Shaker.quiet = false;
		this.rightPanel.modelIsNowImported();
		String s = this.cifViewer.getFormulaFromCif();
		this.applet.frame.setTitle("crystalOgraph" + (s.trim().length() == 0 ? "" : (" - " + s)));
	}

	public void updateModelFromCIF() {
		this.clearAllAtoms();
		this.model.boundUnder_hard(0);
		this.rightPanel.expandPanel.reset();
		this.univers.hideRoot();
		// rightPanel.bkgColorPanel.reset();
		this.cifViewer.getCellFromCIF2(this.model.cell);

		// System.out.println(Cell.currentCell.no);

		this.rightPanel.updateCell(this.model.cell);

		this.univers.reset();
		this.univers.showRoot();

		Vector aa = this.cifViewer.getAtomsFromCIF2();

		// removing error lines
		for (int i = 0; i < aa.size(); i++)
			if (!(aa.get(i) instanceof Vector))
				System.err.println(aa.remove(i));

		// merging symb and label to name
		for (int i = 0; i < aa.size(); i++) {
			Vector a = (Vector) aa.get(i);
			a.add(a.get(0) == null ? a.get(1) : a.get(0));
		}

		// merging atoms with identical pos
		for (int i = 0; i < aa.size(); i++) {
			Vector ai = (Vector) aa.get(i);
			Point3d pi = new Point3d(((Double) ai.get(2)).doubleValue(), ((Double) ai.get(3)).doubleValue(),
					((Double) ai.get(4)).doubleValue());
			for (int j = 0; j < i; j++) {
				Vector aj = (Vector) aa.get(j);
				Point3d pj = new Point3d(((Double) aj.get(2)).doubleValue(), ((Double) aj.get(3)).doubleValue(),
						((Double) aj.get(4)).doubleValue());
				if (pj.distance(pi) < 0.0001) {
					if (aj.get(5) != null && ai.get(5) != null && !aj.get(5).equals(ai.get(5)))
						aj.set(5, aj.get(5) + ", " + ai.get(5));
					if (aj.get(1) != null && ai.get(1) != null && !aj.get(1).equals(ai.get(1)))
						aj.set(1, aj.get(1) + ", " + ai.get(1));
					if (aj.get(0) != null && ai.get(0) != null && !aj.get(0).equals(ai.get(0)))
						aj.set(0, aj.get(0) + ", " + ai.get(0));
					aa.remove(i);
					i--;
					break;
				}
			}
		}

		// adding colors
		Vector AtomsToColor = new Vector(5, 5);
		int k = 0;
		for (int i = 0; i < aa.size(); i++) {
			Vector a = (Vector) aa.get(i);
			String symb = (String) a.get(1);
			if (symb == null || symb.length() == 0) {
				Color c = this.colors[k++ % this.colors.length];
				a.add(c);
			} else {
				if (!AtomsToColor.contains(symb))
					AtomsToColor.add(symb);
				Color c = this.colors[AtomsToColor.indexOf(a.get(1)) % this.colors.length];
				a.add(c);
			}
		}

		// adding atoms to table
		for (int i = 0; i < aa.size(); i++) {
			Vector a = (Vector) aa.get(i);
			this.addAtomToTable("" + a.get(0), "" + a.get(1), "" + a.get(2), "" + a.get(3), "" + a.get(4), "",
					(Color) a.get(6));
		}
	}

	public void addRemovePressed(int currentRow, JButton editor) {
		this.rightPanel.modelIsNowModified();
		if (editor.getText().equals("Add")) {
			Color c = this.setCurrentColor((Color) this.table.getValueAt(currentRow, 6));
			String r = this.setCurrentRadius((String) this.table.getValueAt(currentRow, 5));
			Point3d p = this.addAtomToModel((String) this.table.getValueAt(currentRow, 2),
					(String) this.table.getValueAt(currentRow, 3), (String) this.table.getValueAt(currentRow, 4),
					currentRow, (String) this.table.getValueAt(currentRow, 0));
			if (p != null) {
				// ((DefaultTableModel)table.getModel()).setValueAt("Remove",
				// currentRow, 6);
				this.table.setValueAt("Remove", currentRow, 8);
				this.table.setValueAt(Atom.posToString(p.x), currentRow, 2);
				this.table.setValueAt(Atom.posToString(p.y), currentRow, 3);
				this.table.setValueAt(Atom.posToString(p.z), currentRow, 4);
				this.table.setValueAt(c, currentRow, 6);
				this.table.setValueAt(r, currentRow, 5);
				this.table.setValueAt(new Boolean(true), currentRow, 7);
				editor.setText("Remove");
				// ((DefaultTableModel)table.getModel()).addRow(new Object[]{"",
				// (String)table.getValueAt(currentRow, 1),
				// (String)table.getValueAt(currentRow, 2),
				// (String)table.getValueAt(currentRow, 3), r, c, "Add"});
				((DefaultTableModel) this.table.getModel())
						.addRow(new Object[] { "", "", "0.0", "0.0", "0.0", r, this.nextColor(c), null, "Add" });
				for (int i = 0; i < this.lastRowIsDef.length; i++)
					this.lastRowIsDef[i] = true;

				this.scrollPane.getViewport()
						.scrollRectToVisible(this.table.getCellRect(this.table.getRowCount() - 1, 0, true));
				this.table.changeSelection(currentRow + 1, 0, false, false);
				this.model.hiddenAtomGroups.add(new Vector(50, 50));
				this.model.hiddenLinksGroups.add(new Vector(50, 50));
			}
		} else if (editor.getText().equals("Remove")) {

			// System.out.println("del "+(String)table.getValueAt(currentRow,
			// 1)+" "+ (String)table.getValueAt(currentRow, 2)+ " "+
			// (String)table.getValueAt(currentRow, 3));

			this.removeAtom((String) this.table.getValueAt(currentRow, 2),
					(String) this.table.getValueAt(currentRow, 3), (String) this.table.getValueAt(currentRow, 4));
			((DefaultTableModel) this.table.getModel()).removeRow(currentRow);

			this.model.hiddenAtomGroups.remove(currentRow);
			this.model.hiddenLinksGroups.remove(currentRow);

			if (currentRow == this.table.getRowCount() - 1) {
				editor.setText("Add");
				this.table.setValueAt("Add", currentRow, 8);
			} else {
				editor.setText("Remove");
				this.table.setValueAt("Remove", currentRow, 8);

			}
		}
	}

	class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		private JButton editor;
		JTable table;
		int currentRow;

		public ButtonEditor() {
			this.editor = new JButton();
			this.editor.setMargin(new Insets(this.editor.getMargin().top, 0, this.editor.getMargin().bottom, 0));
			this.editor.addActionListener(this);
			Insets margins = new Insets(0, 0, 0, 0);
			this.editor.setMargin(margins);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			BottomPanel.this.addRemovePressed(this.currentRow, this.editor);
		}

		/*
		 * public void cancelCellEditing() {
		 * //public void enSafe() {
		 * //editor.setText((String)table.getValueAt(currentRow, 6));
		 * super.cancelCellEditing();
		 * }
		 */

		@Override
		public Object getCellEditorValue() {
			return this.editor.getText();
		}

		public JButton editor() {
			return this.editor;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row,
				int column) {
			this.table = table;
			this.currentRow = row;
			this.editor.setText((String) value);
			return this.editor;
		}

	}

	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			this.setMargin(new Insets(this.getMargin().top, 0, this.getMargin().bottom, 0));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			this.setText((String) value);
			return this;
		}
	}

	class TextRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			c.setForeground(row == table.getRowCount() - 1 && BottomPanel.this.lastRowIsDef[column] ? Color.lightGray
					: Color.black);
			return c;
		}
	}

	class ColorComboBoxRenderer extends DefaultTableCellRenderer {
		class Renderer extends Component {
			private java.awt.Color color;

			public void setColor(java.awt.Color color1) {
				this.color = color1;
			}

			public java.awt.Color getColor() {
				return this.color;
			}

			@Override
			public void paint(java.awt.Graphics g) {
				g.setColor(this.color);
				g.fillRect(3, 3, this.getWidth() - 7, this.getHeight() - 7);
				g.setColor(java.awt.Color.darkGray);
				g.draw3DRect(3, 3, this.getWidth() - 7, this.getHeight() - 7, true);
			}
		}

		protected Renderer renderer;

		public ColorComboBoxRenderer() {
			this.renderer = new Renderer();
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			this.renderer.setColor((java.awt.Color) value);
			return ((this.renderer));
		}
	}

	class TextEditor extends AbstractCellEditor implements TableCellEditor {
		JTextField f;
		String prevVal;
		JTable table;
		int currentRow;
		int currentCol;

		public TextEditor() {
			this.f = new JTextField();
			this.f.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						TextEditor.this.stopCellEditing();
						if (!((String) TextEditor.this.table.getValueAt(TextEditor.this.currentRow, 8))
								.equals("Remove"))
							BottomPanel.this.addRemovePressed(TextEditor.this.currentRow, new JButton(
									(String) TextEditor.this.table.getValueAt(TextEditor.this.currentRow, 8)));
					}
				}
			});
		}

		@Override
		public Object getCellEditorValue() {
			return this.f.getText();
		}

		@Override
		public boolean stopCellEditing() {
			if (!this.prevVal.equals(this.f.getText())) {
				BottomPanel.this.lastRowIsDef[this.currentCol] = false;
				// System.out.println("value changed:"+f.getText());
				if ("Remove".equals(this.table.getValueAt(this.currentRow, 8))) {
					BottomPanel.this.rightPanel.modelIsNowModified();
					String[] v = new String[6];
					Color color;
					for (int i = 0; i < v.length; i++)
						v[i] = (String) this.table.getValueAt(this.currentRow, i);
					color = (Color) this.table.getValueAt(this.currentRow, 6);

					// if (currentCol!=0) {
					BottomPanel.this.model.unHide(this.currentRow);
					this.table.setValueAt(new Boolean(true), this.currentRow, 7);

					BottomPanel.this.removeAtom(v[2], v[3], v[4]);
					String oldx = v[2], oldy = v[3], oldz = v[4];
					v[this.currentCol] = this.f.getText();
					BottomPanel.this.setCurrentColor(color);

					String r = BottomPanel.this.setCurrentRadius(v[5]);
					if (r == null)
						r = (String) this.table.getValueAt(this.currentRow, 5);
					this.table.setValueAt(r, this.currentRow, 5);
					if (this.currentCol == 4)
						this.f.setText(r);
					Point3d p = BottomPanel.this.addAtomToModel(v[2], v[3], v[4], this.currentRow, v[0]);
					if (p == null) {
						BottomPanel.this.addAtomToModel(oldx, oldy, oldz, this.currentRow, v[0]);
						v[2] = (String) this.table.getValueAt(this.currentRow, 2);
						v[3] = (String) this.table.getValueAt(this.currentRow, 3);
						v[4] = (String) this.table.getValueAt(this.currentRow, 4);
					} else {
						v[2] = Atom.posToString(p.x);
						v[3] = Atom.posToString(p.y);
						v[4] = Atom.posToString(p.z);
					}
					if (this.currentCol == 2)
						this.f.setText(v[2]);
					if (this.currentCol == 3)
						this.f.setText(v[3]);
					if (this.currentCol == 4)
						this.f.setText(v[4]);
					// }
					if (this.currentCol == 2 || this.currentCol == 3 || this.currentCol == 4)
						BottomPanel.this.rightPanel.bondsPanel.removeBonds();
					if (this.currentCol == 5)
						BottomPanel.this.rightPanel.bondsPanel.rebond();
				}
			}
			return super.stopCellEditing();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object data, boolean isSelected, int row,
				int column) {
			this.table = table;
			this.currentRow = row;
			this.currentCol = column;
			this.prevVal = (String) data;
			this.f.setText((String) data);
			this.f.selectAll();
			return this.f;
		}
	}

	class CheckBoxEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		// public void cancelCellEditing() {
		// p.setBackground(table.getBackground());
		// editor.setBackground(table.getBackground());
		// super.cancelCellEditing();
		// }
		// public boolean stopCellEditing() {
		// p.setBackground(table.getBackground());
		// editor.setBackground(table.getBackground());
		// return super.stopCellEditing();
		// }
		private JCheckBox editor;
		private JPanel p;
		private int row;
		private JTable table;

		public CheckBoxEditor() {
			this.editor = new JCheckBox();
			this.p = new JPanel();
			this.editor.addActionListener(this);
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
			layout.setVgap(0);
			this.p.setLayout(layout);
			this.p.add(this.editor);
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row,
				int column) {
			this.row = row;
			this.table = table;
			this.editor.setVisible(value != null);
			if (value != null)
				this.editor.setSelected(((Boolean) value).booleanValue());
			this.p.setBackground(table.getSelectionBackground());
			this.editor.setBackground(table.getSelectionBackground());
			return this.p;
		}

		@Override
		public Object getCellEditorValue() {
			if (!this.editor.isVisible())
				return null;
			return new Boolean(this.editor.isSelected());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.table.setValueAt(this.editor.isVisible()
					? (this.editor.isSelected() ? new Boolean(true) : new Boolean(false)) : null, this.row, 7);
			if (this.row == this.table.getRowCount() - 1)
				return;
			if (this.editor.isSelected())
				BottomPanel.this.model.unHide(this.row);
			else
				BottomPanel.this.model.hideAtom(this.row);
		}
	}

	class CheckBoxRenderer extends JPanel implements TableCellRenderer {
		JCheckBox checkBox;

		public CheckBoxRenderer() {
			this.checkBox = new JCheckBox();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
			layout.setVgap(0);
			this.setLayout(layout);
			this.add(this.checkBox);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			this.checkBox.setVisible(value != null);
			if (value != null)
				this.checkBox.setSelected(((Boolean) value).booleanValue());
			this.checkBox.setBackground(selected ? table.getSelectionBackground() : table.getBackground());
			this.setBackground(selected ? table.getSelectionBackground() : table.getBackground());
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
			((JComboBox) this.getComponent()).setLightWeightPopupEnabled(false);
			this.prevVal = null;
			BottomPanel.this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						ComboBoxEditor.this.stopCellEditing();
						if (!((String) ComboBoxEditor.this.table.getValueAt(ComboBoxEditor.this.currentRow, 8))
								.equals("Remove"))
							BottomPanel.this.addRemovePressed(ComboBoxEditor.this.currentRow, new JButton(
									(String) ComboBoxEditor.this.table.getValueAt(ComboBoxEditor.this.currentRow, 8)));
					}
				}
			});
		}

		@Override
		public boolean stopCellEditing() {
			if (this.prevVal != ((ColorChoice) this.getComponent()).getSelectedColor()) {
				BottomPanel.this.lastRowIsDef[this.currentCol] = false;
				if ("Remove".equals(this.table.getValueAt(this.currentRow, 8))) {

					BottomPanel.this.model.unHide(this.currentRow);
					this.table.setValueAt(new Boolean(true), this.currentRow, 7);

					String l = (String) this.table.getValueAt(this.currentRow, 0);
					String x = (String) this.table.getValueAt(this.currentRow, 2);
					String y = (String) this.table.getValueAt(this.currentRow, 3);
					String z = (String) this.table.getValueAt(this.currentRow, 4);
					String r = (String) this.table.getValueAt(this.currentRow, 5);
					Color c = ((ColorChoice) this.getComponent()).getSelectedColor();
					BottomPanel.this.removeAtom(x, y, z);
					BottomPanel.this.setCurrentColor(c);
					BottomPanel.this.setCurrentRadius(r);

					BottomPanel.this.addAtomToModel(x, y, z, this.currentRow, l);

					BottomPanel.this.rightPanel.bondsPanel.rebond();
				}
			}
			return super.stopCellEditing();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object data, boolean isSelected, int row,
				int column) {
			this.table = table;
			this.currentRow = row;
			this.currentCol = column;
			this.prevVal = (Color) data;
			((ColorChoice) this.getComponent()).setSelectedColor((Color) data);
			return super.getTableCellEditorComponent(table, data, isSelected, row, column);
		}
	}

}
