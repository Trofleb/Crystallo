import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.NumberFormatter;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import dragNdrop.CifFileDropper;
import dragNdrop.CifFileOpener;
import engine3D.Model3d;
import engine3D.Model3d.Atom;
import engine3D.Model3d.Position;
import sg.Lattice;
import sg.SgType;
import sg.SpaceGroup;
import structures.AtomSite;
import structures.CifFile;
import structures.CifFileFilter;
import structures.Icsd;
import utils.HVPanel;

public class MainPane extends HVPanel.v implements CifFileOpener {
	public static final Color c1 = Color.blue, c2 = Color.red;
	public static final float r1 = .4f, r2 = .4f;

	private Model3d model3d1, model3d2;
	private BottomPanel bottomPanel;
	private Menu menu;
	private DoubleCifPane cifPane;
	private JTabbedPane tabbedPane;
	private CifFile cifIn, cifOut;
	private SpaceGroup sgIn;
	private SpaceGroup sgOut;
	private Lattice latticeIn, latticeOut;
	private AtomSite[] atomsIn, atomsOut;
	private SgType sgP1 = SgType.getSg(1);
	private File file;
	private Help help;
	private Icsd icsd;
	private Transformer transformer;
	private double volumeIn;
	private CellConverter applet;

	public MainPane(CellConverter applet) {
		this.applet = applet;
		this.bottomPanel = new BottomPanel();

		this.help = new Help();
		this.icsd = new Icsd(applet.getCodeBase());

		this.tabbedPane = new JTabbedPane() {
			public Component findComponentAt(int x, int y) {
				if (!this.contains(x, y))
					return null;
				int ncomponents = this.getComponentCount();
				for (int i = 0; i < ncomponents; i++) {
					Component comp = this.getComponentAt(i);
					if (comp != null) {
						if (comp instanceof Container) {
							if (comp.isVisible())
								comp = ((Container) comp).findComponentAt(x - comp.getX(), y - comp.getY());
						} else
							// locate is deprecated
							comp = comp.getComponentAt(x - comp.getX(), y - comp.getY());
						if (comp != null && comp.isVisible())
							return comp;
					}
				}
				return this;
			}
		};

		// model3d1 = new Model3d();
		// model3d2 = new Model3d();
		// JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		// model3d1.createPanel(), model3d2.createPanel());
		// splitPane.setResizeWeight(.5);
		// splitPane.setContinuousLayout(true);
		// tabbedPane.addTab("3D view", splitPane);

		this.model3d1 = new Model3d();
		this.model3d2 = new Model3d(this.model3d1.univers);
		this.model3d2.setDelta(-.05, -.05, -.05);
		JPanel p = this.model3d1.createPanel();
		this.tabbedPane.addTab("3D view", p);

		this.cifPane = new DoubleCifPane();
		this.tabbedPane.addTab("CIF view", this.cifPane.jPanel);
		p.setVisible(true);

		this.expand(true);
		this.addComp(this.tabbedPane);

		this.expand(false);
		this.addSubPane(this.bottomPanel);
	}

	public void stop() {
		this.help.show(false);
		this.icsd.show(false);
	}

	public void destroy() {
		this.model3d1.univers.cleanup();
		this.model3d2.univers.cleanup();
	}

	public void openFile(Reader in) throws Exception {
		CifFile cif = new CifFile(in);
		SpaceGroup sg = cif.getSg();
		this.atomsIn = cif.getAtoms();
		this.sgIn = sg;
		this.latticeIn = sg.cell;
		this.cifIn = cif;
		this.volumeIn = this.latticeIn.volume();
		this.cifPane.setCifLeft(this.cifIn);
		this.model3d1.setSg(sg, c1, "");
		this.model3d1.setAtoms(this.atomsIn, c1, r1);
		this.bottomPanel.inCell.set(this.latticeIn);
		this.transformer.setFile(this.sgIn, this.atomsIn);
		this.cifOut = new CifFile(this.cifIn.getData());
		this.cifOut.setSgP1();
		this.cifPane.setCifRight(this.cifOut);
		this.bottomPanel.transformPane.updateModel();
		if (!this.bottomPanel.transformPane.invalid)
			this.menu.enableSave(true);
	}

	public void openFile(File f) {
		try {
			this.openFile(new FileReader(f));
			this.file = f;
			this.applet.frame.setTitle("Cif Cell Converter - " + f.getName());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(this.applet.frame, "Can't read file or bad format !");
		}
	}

	public void saveFile(File f) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(f));
			String[] ss = this.cifOut.getData();
			for (int i = 0; i < ss.length; i++)
				out.println(ss[i]);
			out.close();
			this.file = f;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this.applet.frame, "Can't write file !");
			e.printStackTrace(System.err);
		}
	}

	public void showOpenDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new CifFileFilter());
		if (chooser.showOpenDialog(this.applet.frame) == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			if (f == null)
				return;
			this.openFile(f);
		}
	}

	public void showSaveDialog() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new CifFileFilter();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		if (chooser.showSaveDialog(this.applet.frame) == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String name = f.getPath();
			if (name.toLowerCase().lastIndexOf(".cif") == -1)
				name += ".cif";
			f = new File(name);
			int r = 0;
			if (f.exists())
				r = JOptionPane.showConfirmDialog(this.applet.frame, "Overwrite file " + this.file.getName() + " ?",
						"File already exists", JOptionPane.YES_NO_OPTION);
			if (r == 0)
				this.saveFile(f);
		}
	}

	class DoubleCifPane extends HVPanel.h {
		JTextArea edit1, edit2;
		JScrollPane scrollPane1, scrollPane2;
		boolean ctrlDown = false;

		public DoubleCifPane() {
			this.edit1 = new JTextArea();
			this.edit2 = new JTextArea();
			this.edit1.setEditable(false);
			this.edit2.setEditable(false);
			this.scrollPane1 = new JScrollPane(this.edit1);
			this.scrollPane2 = new JScrollPane(this.edit2);
			this.addComp(this.scrollPane1);
			this.addComp(this.scrollPane2);

			KeyboardFocusManager kbfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			kbfm.addKeyEventDispatcher(new MyKeyboardManager());

			new DropTarget(this.edit1, new CifFileDropper(MainPane.this));
			new DropTarget(this.edit2, new CifFileDropper(MainPane.this));

			this.scrollPane1.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!DoubleCifPane.this.ctrlDown)
						DoubleCifPane.this.scrollPane2.getHorizontalScrollBar().setValue(e.getValue());
				}
			});
			this.scrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!DoubleCifPane.this.ctrlDown)
						DoubleCifPane.this.scrollPane2.getVerticalScrollBar().setValue(e.getValue());
				}
			});
			this.scrollPane2.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!DoubleCifPane.this.ctrlDown)
						DoubleCifPane.this.scrollPane1.getHorizontalScrollBar().setValue(e.getValue());
				}
			});
			this.scrollPane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!DoubleCifPane.this.ctrlDown)
						DoubleCifPane.this.scrollPane1.getVerticalScrollBar().setValue(e.getValue());
				}
			});
		}

		public void setCifLeft(CifFile cif) {
			this.edit1.setText(cif.toString());
			this.edit1.setCaretPosition(0);
		}

		public void setCifRight(CifFile cif) {
			this.edit2.setText(cif.toString());
			this.edit2.setCaretPosition(0);
		}

		public void updateCifRight(CifFile cif) {
			JScrollBar v = this.scrollPane2.getVerticalScrollBar();
			double f = (double) v.getValue() / (v.getMaximum() - v.getVisibleAmount());
			String[] ss = cif.getData();
			int l = (int) (ss.length * f);
			this.edit2.setText(cif.toString());
			int t = 0;
			try {
				for (int i = 0; i <= l; i++)
					t += ss[i].length() + 1;
			} catch (Exception e) {
				t = this.edit2.getDocument().getLength() - 2;
			}
			this.edit2.setCaretPosition(t + 1);
		}

		public class MyKeyboardManager extends DefaultKeyboardFocusManager {
			public boolean dispatchKeyEvent(KeyEvent e) {
				DoubleCifPane.this.ctrlDown = e.isControlDown() || e.isShiftDown() || e.isAltDown() || e.isMetaDown()
						|| e.isAltGraphDown();
				return super.dispatchKeyEvent(e);
			}
		}
	}

	public class Menu extends JMenuBar implements ActionListener {
		JMenuItem icsdItem, open, save, saveAs, resetp, resetP, helpItem, editCif, applyCif, discardCif;
		JMenuItem fav1, fav2, fav3, fav4, fav5, trans1, trans2;
		JCheckBoxMenuItem paralel, persp, center1, center2;
		JMenu cif;

		public Menu() {
			MainPane.this.menu = this;

			JMenu file = new JMenu("File");
			this.addMenuItem(this.open = new JMenuItem("Open input file..."), file);
			this.addMenuItem(this.save = new JMenuItem("Save output file"), file);
			this.addMenuItem(this.saveAs = new JMenuItem("Save output file as..."), file);
			file.addSeparator();
			this.addMenuItem(this.icsdItem = new JMenuItem("Get from ICSD..."), file);
			file.addSeparator();
			this.addMenuItem(this.fav1 = new JMenuItem("Diamond"), file);
			this.addMenuItem(this.fav2 = new JMenuItem("5,6,7, 90°,90°,90°"), file);
			this.addMenuItem(this.fav3 = new JMenuItem("5,5,5, 90°,90°,120°"), file);
			this.addMenuItem(this.fav4 = new JMenuItem("5,5,5, 60°,60°,60°"), file);
			this.addMenuItem(this.fav5 = new JMenuItem("3 atoms"), file);
			this.enableSave(false);
			this.add(file);

			JMenu transform = new JMenu("Transformation");
			this.addMenuItem(this.resetP = new JMenuItem("Reset transformation matrix"), transform);
			this.addMenuItem(this.resetp = new JMenuItem("Reset shift vector"), transform);
			transform.addSeparator();
			this.addMenuItem(this.trans1 = new JMenuItem("a'=a+b, b'=-a+b, c'=c"), transform);
			this.addMenuItem(this.trans2 = new JMenuItem("P \u2192 F"), transform);
			this.add(transform);

			this.cif = new JMenu("Cif");
			this.addMenuItem(this.editCif = new JMenuItem("Edit input file"), this.cif);
			this.addMenuItem(this.applyCif = new JMenuItem("Apply changes"), this.cif);
			this.addMenuItem(this.discardCif = new JMenuItem("Discard changes"), this.cif);
			this.applyCif.setEnabled(false);
			this.discardCif.setEnabled(false);
			this.add(this.cif);

			JMenu view3d = new JMenu("3D view");
			this.addMenuItem(this.persp = new JCheckBoxMenuItem("Perspective"), view3d);
			this.addMenuItem(this.paralel = new JCheckBoxMenuItem("Parallel"), view3d);
			ButtonGroup g1 = new ButtonGroup();
			g1.add(this.persp);
			g1.add(this.paralel);
			this.persp.setSelected(true);
			view3d.addSeparator();
			this.addMenuItem(this.center1 = new JCheckBoxMenuItem("Center original cell"), view3d);
			this.addMenuItem(this.center2 = new JCheckBoxMenuItem("Center transformed cell"), view3d);
			ButtonGroup g2 = new ButtonGroup();
			g2.add(this.center1);
			g2.add(this.center2);
			this.center2.setSelected(true);
			this.add(view3d);

			JMenu helpMenu = new JMenu("Help");
			this.addMenuItem(this.helpItem = new JMenuItem("Help"), helpMenu);
			this.add(helpMenu);

			MainPane.this.icsd.setActionListener(this);
		}

		public JMenuItem addMenuItem(JMenuItem item, JMenu menu) {
			menu.add(item);
			item.addActionListener(this);
			return item;
		}

		public void enableSave(boolean b) {
			this.save.setEnabled(b);
			this.saveAs.setEnabled(b);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == this.persp) {
				MainPane.this.model3d1.univers.setParallel(false);
				MainPane.this.model3d2.univers.setParallel(false);
			} else if (e.getSource() == this.paralel) {
				MainPane.this.model3d1.univers.setParallel(true);
				MainPane.this.model3d2.univers.setParallel(true);
			} else if (e.getSource() == this.center1)
				MainPane.this.bottomPanel.transformPane.centerOriginalCell();
			else if (e.getSource() == this.center2)
				MainPane.this.bottomPanel.transformPane.centerTransformedCell();
			else if (e.getSource() == this.fav1)
				try {
					MainPane.this
							.openFile(new InputStreamReader(CellConverter.class.getResource("/fav1.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			else if (e.getSource() == this.fav2)
				try {
					MainPane.this
							.openFile(new InputStreamReader(CellConverter.class.getResource("/fav2.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			else if (e.getSource() == this.fav3)
				try {
					MainPane.this
							.openFile(new InputStreamReader(CellConverter.class.getResource("/fav3.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			else if (e.getSource() == this.fav4)
				try {
					MainPane.this
							.openFile(new InputStreamReader(CellConverter.class.getResource("/fav4.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			else if (e.getSource() == this.fav5)
				try {
					MainPane.this
							.openFile(new InputStreamReader(CellConverter.class.getResource("/fav5.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			else if (e.getSource() == this.editCif) {
				if (MainPane.this.cifIn == null)
					return;
				this.editCif.setEnabled(false);
				this.applyCif.setEnabled(true);
				this.discardCif.setEnabled(true);
				MainPane.this.tabbedPane.setSelectedIndex(1);
				MainPane.this.cifPane.edit1.setEditable(true);
				MainPane.this.cifPane.edit1.setBackground(new Color(255, 200, 200));
				MainPane.this.menu.cif.setForeground(new Color(255, 0, 0));
			} else if (e.getSource() == this.applyCif) {
				this.editCif.setEnabled(true);
				this.applyCif.setEnabled(false);
				this.discardCif.setEnabled(false);
				MainPane.this.cifPane.edit1.setEditable(false);
				MainPane.this.cifPane.edit1.setBackground(Color.white);
				MainPane.this.menu.cif.setForeground(Color.black);
				try {
					MainPane.this.openFile(new StringReader(MainPane.this.cifPane.edit1.getText()));
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
					JOptionPane.showMessageDialog(MainPane.this.applet.frame, "Bad cif format !");
					MainPane.this.cifPane.setCifLeft(MainPane.this.cifIn);
				}
			} else if (e.getSource() == this.discardCif) {
				this.editCif.setEnabled(true);
				this.applyCif.setEnabled(false);
				this.discardCif.setEnabled(false);
				MainPane.this.cifPane.edit1.setEditable(false);
				MainPane.this.cifPane.edit1.setBackground(Color.white);
				MainPane.this.menu.cif.setForeground(Color.black);
				MainPane.this.cifPane.setCifLeft(MainPane.this.cifIn);
			} else if (e.getSource() == this.open)
				MainPane.this.showOpenDialog();
			else if (e.getSource() == this.trans1) {
				MainPane.this.bottomPanel.transformPane.P.set(1, 1, 0, -1, 1, 0, 0, 0, 1);
				MainPane.this.bottomPanel.transformPane.Q.m.invert(MainPane.this.bottomPanel.transformPane.P.m);
				mulNegMatVect(MainPane.this.bottomPanel.transformPane.p.v, MainPane.this.bottomPanel.transformPane.Q.m,
						MainPane.this.bottomPanel.transformPane.q.v);
				MainPane.this.bottomPanel.transformPane.P.updateFields();
				MainPane.this.bottomPanel.transformPane.Q.updateFields();
				MainPane.this.bottomPanel.transformPane.q.updateFields();
				MainPane.this.bottomPanel.transformPane.updateModel();
			} else if (e.getSource() == this.trans2) {
				MainPane.this.bottomPanel.transformPane.P.set(-1, 1, 1, 1, -1, 1, 1, 1, -1);
				MainPane.this.bottomPanel.transformPane.Q.m.invert(MainPane.this.bottomPanel.transformPane.P.m);
				mulNegMatVect(MainPane.this.bottomPanel.transformPane.p.v, MainPane.this.bottomPanel.transformPane.Q.m,
						MainPane.this.bottomPanel.transformPane.q.v);
				MainPane.this.bottomPanel.transformPane.P.updateFields();
				MainPane.this.bottomPanel.transformPane.Q.updateFields();
				MainPane.this.bottomPanel.transformPane.q.updateFields();
				MainPane.this.bottomPanel.transformPane.updateModel();
			} else if (e.getSource() == this.resetP) {
				MainPane.this.bottomPanel.transformPane.P.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				MainPane.this.bottomPanel.transformPane.Q.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				mulNegMatVect(MainPane.this.bottomPanel.transformPane.p.v, MainPane.this.bottomPanel.transformPane.Q.m,
						MainPane.this.bottomPanel.transformPane.q.v);
				MainPane.this.bottomPanel.transformPane.P.updateFields();
				MainPane.this.bottomPanel.transformPane.Q.updateFields();
				MainPane.this.bottomPanel.transformPane.q.updateFields();
				MainPane.this.bottomPanel.transformPane.updateModel();
			} else if (e.getSource() == this.resetp) {
				MainPane.this.bottomPanel.transformPane.p.set(0, 0, 0);
				MainPane.this.bottomPanel.transformPane.q.set(0, 0, 0);
				MainPane.this.bottomPanel.transformPane.p.updateFields();
				MainPane.this.bottomPanel.transformPane.q.updateFields();
				MainPane.this.bottomPanel.transformPane.updateModel();
			} else if (e.getSource() == this.helpItem)
				MainPane.this.help.show(true);
			else if (e.getSource() == this.icsdItem)
				MainPane.this.icsd.show(true);
			else if (e.getSource() == MainPane.this.icsd) {
				String[] data = MainPane.this.icsd.getData();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < data.length; i++) {
					sb.append(data[i]);
					sb.append("\n");
				}
				try {
					MainPane.this.openFile(new StringReader(sb.toString()));
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			} else if (e.getSource() == this.save) {
				if (MainPane.this.file == null)
					return;
				int r = JOptionPane.showConfirmDialog(MainPane.this.applet.frame,
						"Overwrite file " + MainPane.this.file.getName() + " ?", "Please confirm",
						JOptionPane.YES_NO_OPTION);
				if (r == 0)
					MainPane.this.saveFile(MainPane.this.file);
			} else if (e.getSource() == this.saveAs)
				MainPane.this.showSaveDialog();
		}
	}

	class BottomPanel extends HVPanel.v {
		LatticePane inCell, outCell;
		TransformPane transformPane;
		JButton openFile, saveFile, saveFileAs, help, resetP, resetp;

		public BottomPanel() {
			HVPanel.h p1 = new HVPanel.h();
			p1.addSubPane(this.inCell = new LatticePane("Input file", "", false));
			// p1.putExtraSpace();
			p1.addSubPane(this.transformPane = new TransformPane());
			// p1.putExtraSpace();
			p1.addSubPane(new ExpandPane());
			// p1.putExtraSpace();
			p1.addSubPane(this.outCell = new LatticePane("Output file", "'", false));
			this.addSubPane(p1);
		}

		class LatticePane extends HVPanel.v {
			FloatEditField a, b, c;
			FloatEditField alpha, beta, gamma;

			public LatticePane(String name, String suffix, boolean editable) {
				super(name);
				this.expand(false);
				this.a = new HVPanel.FloatEditField("a" + suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				this.a.edit.setEditable(editable);
				this.b = new HVPanel.FloatEditField("b" + suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				this.b.edit.setEditable(editable);
				this.c = new HVPanel.FloatEditField("c" + suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				this.c.edit.setEditable(editable);
				this.alpha = new HVPanel.FloatEditField("\u03b1" + suffix, "°", 5, Float.NaN, "#.##").to(this);
				this.alpha.edit.setEditable(editable);
				this.beta = new HVPanel.FloatEditField("\u03b2" + suffix, "°", 5, Float.NaN, "#.##").to(this);
				this.beta.edit.setEditable(editable);
				this.gamma = new HVPanel.FloatEditField("\u03b3" + suffix, "°", 5, Float.NaN, "#.##").to(this);
				this.gamma.edit.setEditable(editable);
			}

			public void set(Lattice lattice) {
				this.a.setValue((float) lattice.a);
				this.b.setValue((float) lattice.b);
				this.c.setValue((float) lattice.c);
				this.alpha.setValue((float) (lattice.alpha));
				this.beta.setValue((float) lattice.beta);
				this.gamma.setValue((float) lattice.gamma);
			}

			public void clear() {
				this.a.setValue(Float.NaN);
				this.b.setValue(Float.NaN);
				this.c.setValue(Float.NaN);
				this.alpha.setValue(Float.NaN);
				this.beta.setValue(Float.NaN);
				this.gamma.setValue(Float.NaN);
			}
		}

		class ExpandPane extends HVPanel.v {
			IntEditField[][] t = new IntEditField[6][];

			public ExpandPane() {
				super("Expansion");
				this.expand(false);
				this.t[0] = this.addItem(" x ");
				this.t[1] = this.addItem(" y ");
				this.t[2] = this.addItem(" z ");
				this.t[3] = this.addItem(" x' ");
				this.t[4] = this.addItem(" y' ");
				this.t[5] = this.addItem(" z' ");
			}

			public IntEditField[] addItem(String label) {
				HVPanel.h p = new HVPanel.h();
				IntEditField p1 = new IntSpinnerEditField(null, null, 1, 0).to(p);
				p.addComp(new JLabel(label));
				IntEditField p2 = new IntSpinnerEditField(null, null, 1, 0).to(p);
				this.addSubPane(p);
				return new IntEditField[] { p1, p2 };
			}

			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == this.t[0][0])
					MainPane.this.model3d1.setExpand(this.t[0][0].getIntValue() + 1, MainPane.this.model3d1.eym,
							MainPane.this.model3d1.ezm, MainPane.this.model3d1.exp, MainPane.this.model3d1.eyp,
							MainPane.this.model3d1.ezp);
				else if (e.getSource() == this.t[0][1])
					MainPane.this.model3d1.setExpand(MainPane.this.model3d1.exm, MainPane.this.model3d1.eym,
							MainPane.this.model3d1.ezm, this.t[0][1].getIntValue() + 1, MainPane.this.model3d1.eyp,
							MainPane.this.model3d1.ezp);
				else if (e.getSource() == this.t[1][0])
					MainPane.this.model3d1.setExpand(MainPane.this.model3d1.exm, this.t[1][0].getIntValue() + 1,
							MainPane.this.model3d1.ezm, MainPane.this.model3d1.exp, MainPane.this.model3d1.eyp,
							MainPane.this.model3d1.ezp);
				else if (e.getSource() == this.t[1][1])
					MainPane.this.model3d1.setExpand(MainPane.this.model3d1.exm, MainPane.this.model3d1.eym,
							MainPane.this.model3d1.ezm, MainPane.this.model3d1.exp, this.t[1][1].getIntValue() + 1,
							MainPane.this.model3d1.ezp);
				else if (e.getSource() == this.t[2][0])
					MainPane.this.model3d1.setExpand(MainPane.this.model3d1.exm, MainPane.this.model3d1.eym,
							this.t[2][0].getIntValue() + 1, MainPane.this.model3d1.exp, MainPane.this.model3d1.eyp,
							MainPane.this.model3d1.ezp);
				else if (e.getSource() == this.t[2][1])
					MainPane.this.model3d1.setExpand(MainPane.this.model3d1.exm, MainPane.this.model3d1.eym,
							MainPane.this.model3d1.ezm, MainPane.this.model3d1.exp, MainPane.this.model3d1.eyp,
							this.t[2][1].getIntValue() + 1);
				else if (e.getSource() == this.t[3][0])
					MainPane.this.model3d2.setExpand(this.t[3][0].getIntValue() + 1, MainPane.this.model3d2.eym,
							MainPane.this.model3d2.ezm, MainPane.this.model3d2.exp, MainPane.this.model3d2.eyp,
							MainPane.this.model3d2.ezp);
				else if (e.getSource() == this.t[3][1])
					MainPane.this.model3d2.setExpand(MainPane.this.model3d2.exm, MainPane.this.model3d2.eym,
							MainPane.this.model3d2.ezm, this.t[3][1].getIntValue() + 1, MainPane.this.model3d2.eyp,
							MainPane.this.model3d2.ezp);
				else if (e.getSource() == this.t[4][0])
					MainPane.this.model3d2.setExpand(MainPane.this.model3d2.exm, this.t[4][0].getIntValue() + 1,
							MainPane.this.model3d2.ezm, MainPane.this.model3d2.exp, MainPane.this.model3d2.eyp,
							MainPane.this.model3d2.ezp);
				else if (e.getSource() == this.t[4][1])
					MainPane.this.model3d2.setExpand(MainPane.this.model3d2.exm, MainPane.this.model3d2.eym,
							MainPane.this.model3d2.ezm, MainPane.this.model3d2.exp, this.t[4][1].getIntValue() + 1,
							MainPane.this.model3d2.ezp);
				else if (e.getSource() == this.t[5][0])
					MainPane.this.model3d2.setExpand(MainPane.this.model3d2.exm, MainPane.this.model3d2.eym,
							this.t[5][0].getIntValue() + 1, MainPane.this.model3d2.exp, MainPane.this.model3d2.eyp,
							MainPane.this.model3d2.ezp);
				else if (e.getSource() == this.t[5][1])
					MainPane.this.model3d2.setExpand(MainPane.this.model3d2.exm, MainPane.this.model3d2.eym,
							MainPane.this.model3d2.ezm, MainPane.this.model3d2.exp, MainPane.this.model3d2.eyp,
							this.t[5][1].getIntValue() + 1);
			}
		}

		class TransformPane extends HVPanel.v {
			EditMatrix Q, P;
			EditVector p, q;
			boolean invalid = false;
			Color originalBorderColor;

			public TransformPane() {
				super("Transformation");
				this.originalBorderColor = ((TitledBorder) this.jPanel.getBorder()).getTitleColor();

				HVPanel.h p1 = new HVPanel.h();
				p1.addComp(new JLabel("P = "));
				this.P = new EditMatrix().to(p1);
				p1.putExtraSpace(10);
				p1.expand(false);
				p1.addComp(new JLabel("origin shift p = "));
				this.p = new EditVector().to(p1);
				p1.expand(true);
				p1.putExtraSpace();
				p1.expand(false);
				p1.addComp(new JLabel(" (a', b', c') = (a, b, c) P"));
				this.addSubPane(p1);

				this.putExtraSpace(3);

				HVPanel.h p2 = new HVPanel.h();
				p2.addComp(new JLabel("Q = P"));
				p2.addComp(new JLabel("\u05be"));
				p2.addComp(new JLabel("\u00b9 = "));
				this.Q = new EditMatrix().to(p2);
				p2.putExtraSpace(10);
				p2.addComp(new JLabel("q = \u2013Q p = "));
				this.q = new EditVector().to(p2);
				p2.putExtraSpace(10);
				p2.expand(true);
				p2.putExtraSpace();
				p2.expand(false);

				p2.addComp(new OpenBracket());
				p2.addSubPane(new VerticalVectorText(" x'", " y '", " z'"));
				p2.addComp(new CloseBracket());
				p2.addComp(new JLabel(" = Q "));
				p2.addComp(new OpenBracket());
				p2.addSubPane(new VerticalVectorText(" x", " y", " z"));
				p2.addComp(new CloseBracket());
				p2.addComp(new JLabel(" + q"));

				this.addSubPane(p2);

				this.P.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				this.Q.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				this.p.set(0, 0, 0);
				this.q.set(0, 0, 0);
				MainPane.this.transformer = new Transformer(this.P.m, this.Q.m, this.q.v);
			}

			public void displayError(String err) {
				if (err == null) {
					TitledBorder b = new TitledBorder("Transformation");
					b.setTitleColor(this.originalBorderColor);
					this.setBorder(b);
				} else {
					TitledBorder b = new TitledBorder(err);
					b.setTitleColor(new Color(150, 0, 0));
					this.setBorder(b);
				}
			}

			public void displayWarning(String err) {
				if (err == null) {
					TitledBorder b = new TitledBorder("Transformation");
					b.setTitleColor(this.originalBorderColor);
					this.setBorder(b);
				} else {
					TitledBorder b = new TitledBorder(err);
					b.setTitleColor(new Color(150, 150, 0));
					this.setBorder(b);
				}
			}

			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				if (o == this.P)
					try {
						this.Q.m.invert(this.P.m);
						this.Q.updateFields();
						mulNegMatVect(this.p.v, this.Q.m, this.q.v);
						this.q.updateFields();
						this.displayError(null);
						this.invalid = false;
						if (MainPane.this.latticeIn != null)
							MainPane.this.menu.enableSave(true);
						this.updateModel();
					} catch (Exception ex) {
						this.displayError("Invalid transformation : P is not inversible ! ");
						this.Q.invalidate();
						this.invalid = true;
						MainPane.this.menu.enableSave(false);
						mulNegMatVect(this.p.v, this.Q.m, this.q.v);
						this.q.updateFields();
						this.updateModel();
					}
				if (o == this.Q)
					try {
						this.P.m.invert(this.Q.m);
						this.P.updateFields();
						mulNegMatVect(this.p.v, this.Q.m, this.q.v);
						this.q.updateFields();
						this.displayError(null);
						this.invalid = false;
						if (MainPane.this.latticeIn != null)
							MainPane.this.menu.enableSave(true);
						this.updateModel();
					} catch (Exception ex) {
						this.displayError("Invalid transformation : Q is not inversible ! ");
						this.P.invalidate();
						this.invalid = true;
						MainPane.this.menu.enableSave(false);
						mulNegMatVect(this.p.v, this.Q.m, this.q.v);
						this.q.updateFields();
						this.updateModel();
					}
				if (o == this.p) {
					mulNegMatVect(this.p.v, this.Q.m, this.q.v);
					this.q.updateFields();
					this.updateModel();
				}
				if (o == this.q) {
					mulNegMatVect(this.q.v, this.P.m, this.p.v);
					this.p.updateFields();
					this.updateModel();
				}
			}

			private void updateModel() {
				// no file, nothing to do
				if (MainPane.this.latticeIn == null)
					return;

				// transformation is invalid, clear output model
				if (this.invalid) {
					this.centerOriginalCell();
					MainPane.this.model3d2.clear();
					MainPane.this.bottomPanel.outCell.clear();
					return;
				}

				// recalculate the transformation
				MainPane.this.transformer.setTranform(this.P.m, this.Q.m, this.q.v);
				MainPane.this.atomsOut = MainPane.this.transformer.getTransformedAtoms();
				MainPane.this.sgOut = MainPane.this.transformer.getTransformedSg();
				MainPane.this.latticeOut = MainPane.this.sgOut.cell;

				if (MainPane.this.menu.center1.isSelected())
					this.centerOriginalCell();
				else
					this.centerTransformedCell();
				MainPane.this.model3d2.setAtoms(MainPane.this.atomsOut, c2, r2);

				MainPane.this.bottomPanel.outCell.set(MainPane.this.latticeOut);
				MainPane.this.cifOut.updateLattice(MainPane.this.latticeOut, (int) Math.round(
						MainPane.this.cifIn.getUnitZ() * MainPane.this.latticeOut.volume() / MainPane.this.volumeIn));
				MainPane.this.cifOut.updateAtoms(MainPane.this.atomsOut);
				MainPane.this.cifPane.updateCifRight(MainPane.this.cifOut);

				if (!this.checkTransformIsCorrect())
					this.displayError("Invalid transformation, please check your transformation ! ");
				else if (this.P.m.determinant() < 0)
					this.displayWarning("Transformation has negative determinant !");
				else
					this.displayError(null);
			}

			public void centerOriginalCell() {
				MainPane.this.latticeIn = new Lattice(MainPane.this.latticeIn.x, MainPane.this.latticeIn.y,
						MainPane.this.latticeIn.z);
				MainPane.this.sgIn = new SpaceGroup(MainPane.this.sgIn.sg, MainPane.this.latticeIn);
				MainPane.this.model3d1.setSg(MainPane.this.sgIn, c1, "");
				Vector3d v = new Vector3d();
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.x, MainPane.this.latticeIn.x, v);
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.y, MainPane.this.latticeIn.y, v);
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.z, MainPane.this.latticeIn.z, v);
				v.add(MainPane.this.latticeIn.o);
				MainPane.this.latticeOut = new Lattice(MainPane.this.latticeOut.x, MainPane.this.latticeOut.y,
						MainPane.this.latticeOut.z, v);
				MainPane.this.sgOut = new SpaceGroup(MainPane.this.sgP1, MainPane.this.latticeOut);
				MainPane.this.model3d2.setSg(MainPane.this.sgOut, c2, "'");
			}

			public void centerTransformedCell() {
				MainPane.this.latticeOut = new Lattice(MainPane.this.latticeOut.x, MainPane.this.latticeOut.y,
						MainPane.this.latticeOut.z);
				MainPane.this.sgOut = new SpaceGroup(MainPane.this.sgP1, MainPane.this.latticeOut);
				MainPane.this.model3d2.setSg(MainPane.this.sgOut, c2, "'");
				Vector3d v = new Vector3d();
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.x, MainPane.this.latticeIn.x, v);
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.y, MainPane.this.latticeIn.y, v);
				v.scaleAdd(MainPane.this.bottomPanel.transformPane.p.v.z, MainPane.this.latticeIn.z, v);
				v.sub(MainPane.this.latticeOut.o, v);
				MainPane.this.latticeIn = new Lattice(MainPane.this.latticeIn.x, MainPane.this.latticeIn.y,
						MainPane.this.latticeIn.z, v);
				MainPane.this.sgIn = new SpaceGroup(MainPane.this.sgIn.sg, MainPane.this.latticeIn);
				MainPane.this.model3d1.setSg(MainPane.this.sgIn, c1, "");
			}

			// private void updateq() {
			// Q.m.transform(p.v, q.v);
			// q.v.negate();
			// if (q.v.x==-0) q.v.x=0;
			// if (q.v.y==-0) q.v.y=0;
			// if (q.v.z==-0) q.v.z=0;
			// q.updateFields();
			// }

			// private void updateOutLattice(boolean do3d) {
			// if (latticeIn==null) return;
			// if (!do3d) {
			// model3d2.clear();
			// return;
			// }
			//// System.out.println("x:"+round(latticeIn.x));
			//// System.out.println("y:"+round(latticeIn.y));
			//// System.out.println("z:"+round(latticeIn.z));
			//
			//
			// Matrix3d m = new Matrix3d();
			// m.setColumn(0, latticeIn.x);
			// m.setColumn(1, latticeIn.y);
			// m.setColumn(2, latticeIn.z);
			//
			// m.mul(P.m);
			//
			// Vector3d x = new Vector3d();
			// Vector3d y = new Vector3d();
			// Vector3d z = new Vector3d();
			// m.getColumn(0, x);
			// m.getColumn(1, y);
			// m.getColumn(2, z);
			//
			//
			//// System.out.println("x':"+round(x));
			//// System.out.println("y':"+round(y));
			//// System.out.println("z':"+round(z));
			//
			// System.out.println("det(P)="+P.m.determinant());
			//
			// if (menu.center1.isSelected()) {
			// latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z);
			// sgIn = new SpaceGroup(sgIn.sg, latticeIn);
			// model3d1.setSg(sgIn, c1, "");
			//
			// Vector3d v = new Vector3d();
			// v.scaleAdd(p.v.x, latticeIn.x, v);
			// v.scaleAdd(p.v.y, latticeIn.y, v);
			// v.scaleAdd(p.v.z, latticeIn.z, v);
			// v.add(latticeIn.o);
			// latticeOut = new Lattice(x, y, z, v);
			//
			// bottomPanel.outCell.set(latticeOut);
			// sgOut = new SpaceGroup(sgP1, latticeOut);
			// model3d2.setSg(sgOut, c2, "'");
			// }
			// else {
			// latticeOut = new Lattice(x, y, z);
			// sgOut = new SpaceGroup(sgP1, latticeOut);
			// model3d2.setSg(sgOut, c2, "'");
			// Vector3d v = new Vector3d();
			// v.scaleAdd(p.v.x, latticeIn.x, v);
			// v.scaleAdd(p.v.y, latticeIn.y, v);
			// v.scaleAdd(p.v.z, latticeIn.z, v);
			// v.sub(latticeOut.o, v);
			// latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z,
			// v);
			// sgIn = new SpaceGroup(sgIn.sg, latticeIn);
			// model3d1.setSg(sgIn, c1, "");
			// }
			//
			// cifOut.setCellA(latticeOut.a);
			// cifOut.setCellB(latticeOut.b);
			// cifOut.setCellC(latticeOut.c);
			// cifOut.setCellAlpha(latticeOut.alpha);
			// cifOut.setCellBeta(latticeOut.beta);
			// cifOut.setCellGamma(latticeOut.gamma);
			//
			// try {
			// cifOut.setCellVolume(latticeOut.a*latticeOut.b*latticeOut.c);
			// } catch (RuntimeException e) {}
			// try {
			// double vin = latticeIn.a*latticeIn.b*latticeIn.c;
			// double vout = latticeOut.a*latticeOut.b*latticeOut.c;
			// double uz = cifIn.getUnitZ();
			// cifOut.setUnitZ((int)Math.round(uz*vout/vin));
			// } catch (RuntimeException e) {}
			// }
			// private void updateOutAtoms(boolean do3d) {
			// if (latticeIn==null) return;
			// if (!do3d) {
			// model3d2.clear();
			// return;
			// }
			// Vector aa = new Vector(100, 100);
			// for (int i=0; i<atomsIn.length; i++) {
			// Vector3d v = new Vector3d(atomsIn[i].x, atomsIn[i].y,
			// atomsIn[i].z);
			// Vector3d[] vv = sgIn.getSymPos(v);
			// for (int j=0; j<vv.length; j++) {
			// if (vv[j]==null) continue;
			// AtomSite a = new AtomSite(atomsIn[i]);
			// Vector3d u = new Vector3d(vv[j]);
			// Q.m.transform(u);
			// u.add(q.v);
			// a.x = u.x;
			// a.y = u.y;
			// a.z = u.z;
			// aa.add(a);
			// }
			// }
			// atomsOut = (AtomSite[]) aa.toArray(new AtomSite[0]);
			// model3d2.setAtoms(atomsOut, c2, r2);
			// Vector v = checkTranslations();
			// System.out.println();
			// cifOut.transformAtoms(cifIn, Q.m, q.v, v);
			// if (!checkTransformIsCorrect()) {
			// displayError("Invalid transformation, please check your
			// transformation ! ");
			// }
			// else {
			// displayError(null);
			// }
			// }
			// private void refreshOutCif() {
			// if (latticeIn==null) return;
			// cifPane.updateCifRight(cifOut);
			// }

			private boolean checkTransformIsCorrect() {
				for (int i = 0; i < MainPane.this.model3d2.atoms.size(); i++) {
					Atom a = (Atom) MainPane.this.model3d2.atoms.get(i);
					for (int j = 0; j < a.positions.size(); j++) {
						Point3d p = new Point3d(((Position) a.positions.get(j)).pos);
						MainPane.this.latticeOut.transform(p);
						MainPane.this.latticeIn.reverse(p);
						p = SpaceGroup.modCell(p);
						boolean found = false;
						for (int k = 0; k < MainPane.this.model3d1.atoms.size(); k++) {
							Position pos = MainPane.this.model3d1
									.getAtomHere(((Atom) MainPane.this.model3d1.atoms.get(k)).positions, p);
							if (pos != null) {
								found = true;
								break;
							}
						}
						if (!found)
							return false;
					}
				}
				return true;
			}

			// private Vector checkTranslations() {
			// Vector v = new Vector();
			// Point3d p100 = new Point3d(1, 0, 0);
			// Q.m.transform(p100);
			// Point3d p010 = new Point3d(0, 1, 0);
			// Q.m.transform(p010);
			// Point3d p001 = new Point3d(0, 0, 1);
			// Q.m.transform(p001);
			//
			// int n = (int)Math.abs(Math.round(P.m.determinant()));
			// if (n==1) return v;
			//
			//
			// //System.out.println("n="+n);
			// System.out.println("100 -> "+round(p100));
			// System.out.println("010 -> "+round(p010));
			// System.out.println("001 -> "+round(p001));
			//
			//
			// Vector3d t = new Vector3d();
			// Vector3d tx = new Vector3d();
			// Vector3d ty = new Vector3d();
			// Vector3d tz = new Vector3d();
			//
			// for (int l=1; l<5; l++) {
			// for (int i=0; i<=l; i++) {
			// for (int j=0; j<=l; j++) {
			// for (int k=0; k<=l; k++) {
			// if (i==0&j==0&&k==0) continue;
			// if (i<l&&j<l&&k<l) continue;
			// tx.scale(i, p100);
			// ty.scale(j, p010);
			// tz.scale(k, p001);
			// t.add(tx, ty);
			// t.add(tz);
			// t= round(t);
			// //System.out.println(i+" "+j+" "+k+":"+t);
			// if (t.x>-1&&t.y>-1&&t.z>-1&&t.x<0.9999&&t.y<0.9999&&t.z<0.9999) {
			// boolean found=false;
			// for (int m=0; m<v.size(); m++) {
			// if
			// (round(SpaceGroup.modCell((Vector3d)v.get(m))).equals(round(SpaceGroup.modCell(t))))
			// {
			// found=true;
			// break;
			// }
			// }
			// if (found==false) {
			// addTranslation(t);
			// v.add(new Vector3d(t));
			// System.out.println((i>0?(i>1?(i+"x"):"x"):"")+(j>0?(i>0?"+":"")+(j>1?(j+"y"):"y"):"")+(k>0?(i>0||j>0?"+":"")+(k>1?(k+"z"):"z"):"")+"
			// "+(round(t)));
			// n--;
			// if (n==1) return v;
			// }
			// }
			// }
			// }
			// }
			// }
			// System.out.println("Out of loop!!!");
			// return v;
			//
			//// iLoop: for (int i=0; i<10; i++) {
			//// tx.scale(i, p100);
			//// for (int j=0; j<10; j++) {
			//// ty.scale(j, p010);
			//// for (int k=0; k<10; k++) {
			//// if (i==0&&j==0&&k==0) continue;
			//// tz.scale(k, p001);
			//// System.out.println(i+" "+j+" "+k+":"+tz);
			//// //if (tz.x>0.99999||tz.y>0.99999||tz.z>0.99999) break;
			////
			//// t.add(tx, ty);
			//// t.add(tz);
			//// if (t.x>0.99999||t.y>0.99999||t.z>0.99999) continue;
			//// if (t.x<0||t.y<0||t.z<0) continue;
			//// System.out.println(i+" "+j+" "+k+" "+round(t));
			//// addTranslation(t);
			//// v.add(new Vector3d(t));
			//// n--;
			//// System.out.println(n);
			//// if (n==1) return v;
			//// }
			//// //if (ty.x>0.99999||ty.y>0.99999||ty.z>0.99999) break;
			//// }
			//// //if (tx.x>0.99999||tx.y>0.99999||tx.z>0.99999) break;
			//// }
			//// return v;
			// }
			//
			//
			// private void addTranslation(Vector3d v) {
			// Vector3d r = new Vector3d();
			// for (int i=0; i<atomsOut.length; i++) {
			// r.set((atomsOut[i].x+v.x)%1, (atomsOut[i].y+v.y)%1,
			// (atomsOut[i].z+v.z)%1);
			// r = SpaceGroup.modCell(r);
			// System.out.println("new atom:"+r);
			// //System.out.println(model3d2.getAtomHere(((Atom)(model3d2.atoms.get(i))).positions,
			// new Point3d(r)));
			// AtomSite a = new AtomSite(atomsOut[i].atom, atomsOut[i].symbol,
			// atomsOut[i].label, r.x, r.y, r.z, atomsOut[i].occupancy,
			// atomsOut[i].oxydation);
			// model3d2.addAtom(a, c2, r2);
			// }
			// }
		}
	}

	public static void mulNegMatVect(Tuple3d p, Matrix3d m, Vector3d q) {
		m.transform(p, q);
		q.negate();
		if (q.x == -0)
			q.x = 0;
		if (q.y == -0)
			q.y = 0;
		if (q.z == -0)
			q.z = 0;
	}

	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(1000 * p.x) / 1000d, Math.round(1000 * p.y) / 1000d,
				Math.round(1000 * p.z) / 1000d);
	}

	public static Vector3d round(Vector3d p) {
		return new Vector3d(Math.round(1000 * p.x) / 1000d, Math.round(1000 * p.y) / 1000d,
				Math.round(1000 * p.z) / 1000d);
	}
}

class EditMatrix {
	private VerticalVector[] edit;
	public Matrix3d m;
	private HVPanel.h p;

	public EditMatrix() {
		this.edit = new VerticalVector[3];
		this.p = new HVPanel.h() {
			public void actionPerformed(ActionEvent e) {
				double d = EditVector.parseFrac("" + ((VerticalVector) e.getSource()).edit[e.getID()].getValue());
				int col = e.getSource() == EditMatrix.this.edit[0] ? 0
						: (e.getSource() == EditMatrix.this.edit[1] ? 1 : 2);
				EditMatrix.this.m.setElement(e.getID(), col, d);
				super.actionPerformed(new ActionEvent(EditMatrix.this, 0, ""));
			}
		};
		this.p.addComp(new OpenBracket());
		this.p.addSubPane(this.edit[0] = new VerticalVector());
		this.p.addSubPane(this.edit[1] = new VerticalVector());
		this.p.addSubPane(this.edit[2] = new VerticalVector());
		this.p.addComp(new CloseBracket());
		this.m = new Matrix3d();
	}

	public EditMatrix to(HVPanel parent) {
		parent.addSubPane(this.p);
		return this;
	}

	public void set(Matrix3d m) {
		this.m.set(m);
		this.updateFields();
	}

	public void set(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21,
			double m22) {
		this.m.m00 = m00;
		this.m.m01 = m01;
		this.m.m02 = m02;
		this.m.m10 = m10;
		this.m.m11 = m11;
		this.m.m12 = m12;
		this.m.m20 = m20;
		this.m.m21 = m21;
		this.m.m22 = m22;
		this.updateFields();
	}

	public void updateFields() {
		if (this.m.m00 == -0)
			this.m.m00 = 0;
		if (this.m.m10 == -0)
			this.m.m10 = 0;
		if (this.m.m20 == -0)
			this.m.m20 = 0;
		if (this.m.m01 == -0)
			this.m.m01 = 0;
		if (this.m.m11 == -0)
			this.m.m11 = 0;
		if (this.m.m21 == -0)
			this.m.m21 = 0;
		if (this.m.m02 == -0)
			this.m.m02 = 0;
		if (this.m.m12 == -0)
			this.m.m12 = 0;
		if (this.m.m22 == -0)
			this.m.m22 = 0;
		this.edit[0].set(this.m.m00, this.m.m10, this.m.m20);
		this.edit[1].set(this.m.m01, this.m.m11, this.m.m21);
		this.edit[2].set(this.m.m02, this.m.m12, this.m.m22);
	}

	public void invalidate() {
		this.m.m00 = Double.NaN;
		this.m.m01 = Double.NaN;
		this.m.m02 = Double.NaN;
		this.m.m10 = Double.NaN;
		this.m.m11 = Double.NaN;
		this.m.m12 = Double.NaN;
		this.m.m20 = Double.NaN;
		this.m.m21 = Double.NaN;
		this.m.m22 = Double.NaN;
		this.updateFields();
	}

	public void setColor(Color c) {
		this.edit[0].setColor(c);
		this.edit[1].setColor(c);
		this.edit[2].setColor(c);
	}

	public void clearColor() {
		Color c = this.edit[0].originalColor;
		this.edit[0].setColor(c);
		this.edit[1].setColor(c);
		this.edit[2].setColor(c);
	}
}

class EditVector {
	private VerticalVector edit;
	public Vector3d v;
	private HVPanel.h p;

	public EditVector() {
		this.p = new HVPanel.h() {
			public void actionPerformed(ActionEvent e) {
				VerticalVector vv = ((VerticalVector) e.getSource());
				EditVector.this.v.set(parseFrac("" + vv.edit[0].getValue()), parseFrac("" + vv.edit[1].getValue()),
						parseFrac("" + vv.edit[2].getValue()));
				super.actionPerformed(new ActionEvent(EditVector.this, 0, ""));
			}
		};
		this.p.addComp(new OpenBracket());
		this.p.addSubPane(this.edit = new VerticalVector());
		this.p.addComp(new CloseBracket());
		this.v = new Vector3d();
	}

	public EditVector to(HVPanel parent) {
		parent.addSubPane(this.p);
		return this;
	}

	public void set(Vector3d v) {
		this.v.set(v);
		this.updateFields();
	}

	public void set(double x, double y, double z) {
		this.v.set(x, y, z);
		this.updateFields();
	}

	public void updateFields() {
		if (this.v.x == -0)
			this.v.x = 0;
		if (this.v.y == -0)
			this.v.y = 0;
		if (this.v.z == -0)
			this.v.z = 0;
		this.edit.set(this.v.x, this.v.y, this.v.z);
	}

	public static double parseFrac(String s) throws NumberFormatException {
		int p = s.indexOf('/');
		if (p == -1)
			return Double.parseDouble(s);
		return Double.parseDouble(s.substring(0, p)) / Double.parseDouble(s.substring(p + 1));
	}
}

class OpenBracket extends JComponent {
	static final int w = 5;

	public OpenBracket() {
		this.setPreferredSize(new Dimension(w, 0));
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		int r = this.getHeight() / 2;
		g.drawOval(0, this.getHeight() / 2 - r, this.getHeight() / 4, r * 2);
	}
}

class CloseBracket extends JComponent {
	static final int w = 5;

	public CloseBracket() {
		this.setPreferredSize(new Dimension(w, 0));
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		int r = this.getHeight() / 2;
		g.drawOval(-this.getHeight() / 4 + w - 1, this.getHeight() / 2 - r, this.getHeight() / 4, r * 2);
	}
}

class VerticalVector extends HVPanel.v implements PropertyChangeListener {
	public JFormattedTextField[] edit;
	public boolean quiet = false;
	public Color originalColor;

	public VerticalVector() {
		this.edit = new JFormattedTextField[3];
		this.expand(false);
		NumberFormatter f = createFormatter("#.##");
		for (int i = 0; i < this.edit.length; i++) {
			this.addComp(this.edit[i] = new JFormattedTextField(f) {
				// I've overwritten this because an exception is thrown when
				// fractional
				// value is entered. The exception seems to have no use.
				protected void setFormatter(AbstractFormatter arg0) {
					try {
						super.setFormatter(arg0);
					} catch (RuntimeException e) {
					}
				}
			});
			this.edit[i].setColumns(3);
			this.edit[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.edit[i].addPropertyChangeListener(this);
		}
		this.originalColor = this.edit[0].getBackground();
	}

	public void setColor(Color c) {
		this.edit[0].setBackground(c);
		this.edit[1].setBackground(c);
		this.edit[2].setBackground(c);
	}

	public void set(double x, double y, double z) {
		this.quiet = true;
		this.edit[0].setValue(new Double(x));
		this.edit[1].setValue(new Double(y));
		this.edit[2].setValue(new Double(z));
		this.quiet = false;
	}

	public void propertyChange(PropertyChangeEvent e) {
		if (!this.quiet && e.getPropertyName().equals("value") && !e.getNewValue().equals(e.getOldValue())) {
			int row = e.getSource() == this.edit[0] ? 0 : (e.getSource() == this.edit[1] ? 1 : 2);
			this.actionPerformed(new ActionEvent(this, row, ""));
		}
	}

	public static NumberFormatter createFormatter(String format) {
		DecimalFormat df = new DecimalFormat(format) {
			public Object parseObject(String s) throws ParseException {
				int p = s.indexOf('/');
				if (p != -1) {
					super.parseObject(s.substring(0, p));
					super.parseObject(s.substring(p + 1));
					return s;
				}
				return super.parseObject(s);
			}

			public Number parse(String text, ParsePosition pos) {
				return super.parse(text.trim(), pos);
			}
		};
		DecimalFormatSymbols s = df.getDecimalFormatSymbols();
		s.setDecimalSeparator('.');
		s.setNaN(" ");
		df.setDecimalFormatSymbols(s);
		return new NumberFormatter(df);
	}
}

class VerticalVectorText extends HVPanel.v {
	public VerticalVectorText(String x, String y, String z) {
		this.expand(false);
		this.addComp(new JLabel(x));
		this.addComp(new JLabel(y));
		this.addComp(new JLabel(z));
	}
}
