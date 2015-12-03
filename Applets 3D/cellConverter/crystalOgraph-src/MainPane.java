


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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.NumberFormatter;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import sg.Lattice;
import sg.SgType;
import sg.SpaceGroup;
import structures.AtomSite;
import structures.CifFile;
import structures.CifFileFilter;
import structures.Icsd;
import utils.HVPanel;
import dragNdrop.CifFileDropper;
import dragNdrop.CifFileOpener;
import engine3D.Model3d;
import engine3D.Model3d.Atom;
import engine3D.Model3d.Position;

public class MainPane extends HVPanel.v implements CifFileOpener {
	public static final Color c1=Color.blue, c2=Color.red;
	public static final float r1=.4f, r2=.4f;
	
	Model3d model3d1, model3d2;
	BottomPanel bottomPanel;
	Menu menu;
	DoubleCifPane cifPane;
	JTabbedPane tabbedPane;
	CifFile cifIn, cifOut;
	SpaceGroup sgIn;
	SpaceGroup sgOut;
	Lattice latticeIn, latticeOut;
	AtomSite[] atomsIn, atomsOut;
	SgType sgP1 = SgType.getSg(1); 
	File file;
	Help help;
	Icsd icsd; 
	Transformer transformer;
	double volumeIn;
	CellConverter applet;
	
	public MainPane(CellConverter applet) {
		this.applet = applet;
		bottomPanel=new BottomPanel();

		help = new Help();
		icsd = new Icsd(applet.getCodeBase()); 
		
		tabbedPane = new JTabbedPane() {
			public Component findComponentAt(int x, int y) {
				if (!contains(x, y)) {
					return null;
				}
				int ncomponents = getComponentCount();
				for (int i = 0 ; i < ncomponents ; i++) {
					Component comp = getComponentAt(i);
					if (comp != null) {
						if (comp instanceof Container) {
							if(comp.isVisible()) 
								comp = ((Container)comp).
								findComponentAt(x - comp.getX(), y - comp.getY());
						} else {
							// locate is deprecated
							comp = comp.locate(x - comp.getX(), y - comp.getY());
						}
						if (comp != null && comp.isVisible()) {
							return comp;
						}
					}
				}
				return this;
			}
		};
		
//		model3d1 = new Model3d();
//		model3d2 = new Model3d();
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, model3d1.createPanel(), model3d2.createPanel());
//		splitPane.setResizeWeight(.5);
//		splitPane.setContinuousLayout(true);
//		tabbedPane.addTab("3D view", splitPane);

		model3d1 = new Model3d();
		model3d2 = new Model3d(model3d1.univers);
		model3d2.setDelta(-.05, -.05, -.05);
		JPanel p = model3d1.createPanel();
		tabbedPane.addTab("3D view", p);
		
		cifPane = new DoubleCifPane(); 
		tabbedPane.addTab("CIF view", cifPane.jPanel);
		p.setVisible(true);
		
		expand(true);
		addComp(tabbedPane);
		
		expand(false);
		addSubPane(bottomPanel);
	}

	public void stop() {
		help.show(false);
		icsd.show(false);
	}
	public void destroy() {
		model3d1.univers.cleanup();
		model3d2.univers.cleanup();
	}
	
	public void openFile(Reader in) throws Exception {
		CifFile cif = new CifFile(in);
		SpaceGroup sg = cif.getSg();
		atomsIn = cif.getAtoms();
		sgIn = sg;
		latticeIn = sg.cell;
		cifIn = cif;
		volumeIn = latticeIn.volume();
		cifPane.setCifLeft(cifIn);
		model3d1.setSg(sg, c1, "");
		model3d1.setAtoms(atomsIn, c1, r1);
		bottomPanel.inCell.set(latticeIn);
		transformer.setFile(sgIn, atomsIn);
		cifOut = new CifFile(cifIn.getData());
		cifOut.setSgP1();
		cifPane.setCifRight(cifOut);
		bottomPanel.transformPane.updateModel();
		if (!bottomPanel.transformPane.invalid) menu.enableSave(true);
	}

	public void openFile(File f) {
		try {
			openFile(new FileReader(f));
			file=f;
			applet.frame.setTitle("Cif Cell Converter - "+f.getName());
		} catch (Exception e) {
			e.printStackTrace(System.err);
  		JOptionPane.showMessageDialog(applet.frame, "Can't read file or bad format !");
		}
	}
	
	public void saveFile(File f) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(f));
			String[] ss = cifOut.getData();
			for (int i=0; i<ss.length; i++) {
				out.println(ss[i]);
			}
			out.close();
			file = f;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(applet.frame, "Can't write file !");
			e.printStackTrace(System.err);
		}
	}
	
	public void showOpenDialog() {
    JFileChooser chooser = new JFileChooser();
    chooser.setMultiSelectionEnabled(false);
    chooser.setFileFilter(new CifFileFilter());
    if(chooser.showOpenDialog(applet.frame)==JFileChooser.APPROVE_OPTION) {
    	File f = chooser.getSelectedFile();
    	if (f==null) return;
      openFile(f);
    }
	}

	public void showSaveDialog() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new CifFileFilter();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		if(chooser.showSaveDialog(applet.frame)==JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String name = f.getPath();
			if (name.toLowerCase().lastIndexOf(".cif")==-1) name+=".cif";
			f = new File(name);
			int r=0;
			if (f.exists()) {
				r = JOptionPane.showConfirmDialog(applet.frame, "Overwrite file "+file.getName()+" ?", "File already exists", JOptionPane.YES_NO_OPTION);
			}
			if (r==0) saveFile(f);
		}
	}
	
	class DoubleCifPane extends HVPanel.h {
		JTextArea edit1, edit2;
		JScrollPane scrollPane1, scrollPane2;
		boolean ctrlDown = false;
		
		public DoubleCifPane() {
			edit1 = new JTextArea();
			edit2 = new JTextArea();
			edit1.setEditable(false);
			edit2.setEditable(false);
	    scrollPane1 = new JScrollPane(edit1);
	    scrollPane2 = new JScrollPane(edit2);
	    addComp(scrollPane1);
	    addComp(scrollPane2);
	    
	    KeyboardFocusManager kbfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	    kbfm.addKeyEventDispatcher(new MyKeyboardManager());
	    
			new DropTarget(edit1, new CifFileDropper(MainPane.this)); 
			new DropTarget(edit2, new CifFileDropper(MainPane.this)); 
	    
	    scrollPane1.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!ctrlDown) scrollPane2.getHorizontalScrollBar().setValue(e.getValue());
				}
	    });
	    scrollPane1.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!ctrlDown) scrollPane2.getVerticalScrollBar().setValue(e.getValue());
				}
	    });
	    scrollPane2.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!ctrlDown) scrollPane1.getHorizontalScrollBar().setValue(e.getValue());
				}
	    });
	    scrollPane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					if (!ctrlDown) scrollPane1.getVerticalScrollBar().setValue(e.getValue());
				}
	    });
		}
		public void setCifLeft(CifFile cif) {
			edit1.setText(cif.toString());
			edit1.setCaretPosition(0);
		}
		public void setCifRight(CifFile cif) {
			edit2.setText(cif.toString());
			edit2.setCaretPosition(0);
		}
		public void updateCifRight(CifFile cif) {
			JScrollBar v = scrollPane2.getVerticalScrollBar();
			double f = (double)v.getValue()/(v.getMaximum()-v.getVisibleAmount());
			String[] ss = cif.getData();
			int l = (int)(ss.length*f);
			edit2.setText(cif.toString());
			int t=0;
			try {
				for (int i=0; i<=l; i++) {
					t+=ss[i].length()+1;
				}
			} catch (Exception e) {
				t=edit2.getDocument().getLength()-2;
			}
			edit2.setCaretPosition(t+1);
		}

		public class MyKeyboardManager extends DefaultKeyboardFocusManager {
			public boolean dispatchKeyEvent(KeyEvent e) {
				ctrlDown = e.isControlDown()||e.isShiftDown()||e.isAltDown()||e.isMetaDown()||e.isAltGraphDown();
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
			menu=this;
			
			JMenu file = new JMenu("File");
			addMenuItem(open=new JMenuItem("Open input file..."), file);
			addMenuItem(save=new JMenuItem("Save output file"), file);
			addMenuItem(saveAs=new JMenuItem("Save output file as..."), file);
			file.addSeparator();
			addMenuItem(icsdItem=new JMenuItem("Get from ICSD..."), file);
			file.addSeparator();
			addMenuItem(fav1=new JMenuItem("Diamond"), file);
			addMenuItem(fav2=new JMenuItem("5,6,7, 90°,90°,90°"), file);
			addMenuItem(fav3=new JMenuItem("5,5,5, 90°,90°,120°"), file);
			addMenuItem(fav4=new JMenuItem("5,5,5, 60°,60°,60°"), file);
			addMenuItem(fav5=new JMenuItem("3 atoms"), file);
			enableSave(false);
			add(file);
			
			JMenu transform = new JMenu("Transformation");
			addMenuItem(resetP=new JMenuItem("Reset transformation matrix"), transform);
			addMenuItem(resetp=new JMenuItem("Reset shift vector"), transform);
			transform.addSeparator();
			addMenuItem(trans1=new JMenuItem("a'=a+b, b'=-a+b, c'=c"), transform);
			addMenuItem(trans2=new JMenuItem("P \u2192 F"), transform);
			add(transform);
			
			cif = new JMenu("Cif");
			addMenuItem(editCif=new JMenuItem("Edit input file"), cif);
			addMenuItem(applyCif=new JMenuItem("Apply changes"), cif);
			addMenuItem(discardCif=new JMenuItem("Discard changes"), cif);
			applyCif.setEnabled(false);
			discardCif.setEnabled(false);
			add(cif);

			JMenu view3d = new JMenu("3D view");
			addMenuItem(persp=new JCheckBoxMenuItem("Perspective"), view3d);
			addMenuItem(paralel=new JCheckBoxMenuItem("Parallel"), view3d);
			ButtonGroup g1 = new ButtonGroup();
			g1.add(persp);
			g1.add(paralel);
			persp.setSelected(true);
			view3d.addSeparator();
			addMenuItem(center1=new JCheckBoxMenuItem("Center original cell"), view3d);
			addMenuItem(center2=new JCheckBoxMenuItem("Center transformed cell"), view3d);
			ButtonGroup g2 = new ButtonGroup();
			g2.add(center1);
			g2.add(center2);
			center2.setSelected(true);
			add(view3d);

			JMenu helpMenu = new JMenu("Help");
			addMenuItem(helpItem=new JMenuItem("Help"), helpMenu);
			add(helpMenu);
			
			icsd.setActionListener(this);
		}

		public JMenuItem addMenuItem(JMenuItem item, JMenu menu) {
			menu.add(item);
			item.addActionListener(this);
			return item;
		}
		
		public void enableSave(boolean b) {
			save.setEnabled(b);
			saveAs.setEnabled(b);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==persp) {
				model3d1.univers.setParallel(false);
				model3d2.univers.setParallel(false);
			}
			else if (e.getSource()==paralel) {
				model3d1.univers.setParallel(true);
				model3d2.univers.setParallel(true);
			}
			else if (e.getSource()==center1) {
				bottomPanel.transformPane.centerOriginalCell();
			}
			else if (e.getSource()==center2) {
				bottomPanel.transformPane.centerTransformedCell();
			}
			else if (e.getSource()==fav1) {
				try {
					openFile(new InputStreamReader(CellConverter.class.getResource("/fav1.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else if (e.getSource()==fav2) {
				try {
					openFile(new InputStreamReader(CellConverter.class.getResource("/fav2.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else if (e.getSource()==fav3) {
				try {
					openFile(new InputStreamReader(CellConverter.class.getResource("/fav3.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else if (e.getSource()==fav4) {
				try {
					openFile(new InputStreamReader(CellConverter.class.getResource("/fav4.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else if (e.getSource()==fav5) {
				try {
					openFile(new InputStreamReader(CellConverter.class.getResource("/fav5.cif").openStream()));
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else if (e.getSource()==editCif) {
				if (cifIn==null) return;
				editCif.setEnabled(false);
				applyCif.setEnabled(true);
				discardCif.setEnabled(true);
				tabbedPane.setSelectedIndex(1);
				cifPane.edit1.setEditable(true);
				cifPane.edit1.setBackground(new Color(255, 200, 200));
				menu.cif.setForeground(new Color(255, 0, 0));
			}
			else if (e.getSource()==applyCif) {
				editCif.setEnabled(true);
				applyCif.setEnabled(false);
				discardCif.setEnabled(false);
				cifPane.edit1.setEditable(false);
				cifPane.edit1.setBackground(Color.white);
				menu.cif.setForeground(Color.black);
				try {
					openFile(new StringReader(cifPane.edit1.getText()));
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
		  		JOptionPane.showMessageDialog(applet.frame, "Bad cif format !");
					cifPane.setCifLeft(cifIn);
				}
			}
			else if (e.getSource()==discardCif) {
				editCif.setEnabled(true);
				applyCif.setEnabled(false);
				discardCif.setEnabled(false);
				cifPane.edit1.setEditable(false);
				cifPane.edit1.setBackground(Color.white);
				menu.cif.setForeground(Color.black);
				cifPane.setCifLeft(cifIn);
			}
			else if (e.getSource()==open) {
				showOpenDialog();
			}
			else if (e.getSource()==trans1) {
				bottomPanel.transformPane.P.set(1, 1, 0, -1, 1, 0, 0, 0, 1);
				bottomPanel.transformPane.Q.m.invert(bottomPanel.transformPane.P.m);
				mulNegMatVect(bottomPanel.transformPane.p.v, bottomPanel.transformPane.Q.m, bottomPanel.transformPane.q.v);
				bottomPanel.transformPane.P.updateFields();
				bottomPanel.transformPane.Q.updateFields();
				bottomPanel.transformPane.q.updateFields();
				bottomPanel.transformPane.updateModel();
			}
			else if (e.getSource()==trans2) {
				bottomPanel.transformPane.P.set(-1, 1, 1, 1, -1, 1, 1, 1, -1);
				bottomPanel.transformPane.Q.m.invert(bottomPanel.transformPane.P.m);
				mulNegMatVect(bottomPanel.transformPane.p.v, bottomPanel.transformPane.Q.m, bottomPanel.transformPane.q.v);
				bottomPanel.transformPane.P.updateFields();
				bottomPanel.transformPane.Q.updateFields();
				bottomPanel.transformPane.q.updateFields();
				bottomPanel.transformPane.updateModel();
			}
			else if (e.getSource()==resetP) {
				bottomPanel.transformPane.P.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				bottomPanel.transformPane.Q.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				mulNegMatVect(bottomPanel.transformPane.p.v, bottomPanel.transformPane.Q.m, bottomPanel.transformPane.q.v);
				bottomPanel.transformPane.P.updateFields();
				bottomPanel.transformPane.Q.updateFields();
				bottomPanel.transformPane.q.updateFields();
				bottomPanel.transformPane.updateModel();
			}
			else if (e.getSource()==resetp) {
				bottomPanel.transformPane.p.set(0, 0, 0);
				bottomPanel.transformPane.q.set(0, 0, 0);
				bottomPanel.transformPane.p.updateFields();
				bottomPanel.transformPane.q.updateFields();
				bottomPanel.transformPane.updateModel();
			}
			else if (e.getSource()==helpItem) {
				help.show(true);
			}
			else if (e.getSource()==icsdItem) {
				icsd.show(true);
			}
			else if (e.getSource()==icsd) {
				String[] data = icsd.getData();
				StringBuffer sb = new StringBuffer();
				for (int i=0; i<data.length; i++) {
					sb.append(data[i]);
					sb.append("\n");
				}
				try {
					openFile(new StringReader(sb.toString()));
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
			else if (e.getSource()==save) {
				if (file==null)return;
				int r = JOptionPane.showConfirmDialog(applet.frame, "Overwrite file "+file.getName()+" ?", "Please confirm", JOptionPane.YES_NO_OPTION);
				if (r==0) saveFile(file);
			}
			else if (e.getSource()==saveAs) {
				showSaveDialog();
			}
		}
	}
	
	class BottomPanel extends HVPanel.v {
		LatticePane inCell, outCell;
		TransformPane transformPane;
		JButton openFile, saveFile, saveFileAs, help, resetP, resetp;
		public BottomPanel() {
			HVPanel.h p1 = new HVPanel.h();
			p1.addSubPane(inCell=new LatticePane("Input file", "", false));
			//p1.putExtraSpace();
			p1.addSubPane(transformPane=new TransformPane());
			//p1.putExtraSpace();
			p1.addSubPane(new ExpandPane());
			//p1.putExtraSpace();
			p1.addSubPane(outCell=new LatticePane("Output file", "'", false));
			addSubPane(p1);
		}
		
		
		class LatticePane extends HVPanel.v {
			FloatEditField a, b, c;
			FloatEditField alpha, beta, gamma;
			
			public LatticePane(String name, String suffix, boolean editable) {
				super(name);
				expand(false);
				a = new HVPanel.FloatEditField("a"+suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				a.edit.setEditable(editable);
				b = new HVPanel.FloatEditField("b"+suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				b.edit.setEditable(editable);
				c = new HVPanel.FloatEditField("c"+suffix, "\u00c5", 5, Float.NaN, "#.#####").to(this);
				c.edit.setEditable(editable);
				alpha = new HVPanel.FloatEditField("\u03b1"+suffix, "°", 5, Float.NaN, "#.##").to(this);
				alpha.edit.setEditable(editable);
				beta = new HVPanel.FloatEditField("\u03b2"+suffix, "°", 5, Float.NaN, "#.##").to(this);
				beta.edit.setEditable(editable);
				gamma = new HVPanel.FloatEditField("\u03b3"+suffix, "°", 5, Float.NaN, "#.##").to(this);
				gamma.edit.setEditable(editable);
			}
			
			public void set(Lattice lattice) {
				a.setValue((float)lattice.a);
				b.setValue((float)lattice.b);
				c.setValue((float)lattice.c);
				alpha.setValue((float)(lattice.alpha));
				beta.setValue((float)lattice.beta);
				gamma.setValue((float)lattice.gamma);
			}
			public void clear() {
				a.setValue(Float.NaN);
				b.setValue(Float.NaN);
				c.setValue(Float.NaN);
				alpha.setValue(Float.NaN);
				beta.setValue(Float.NaN);
				gamma.setValue(Float.NaN);
			}
		}

		class ExpandPane extends HVPanel.v {
			IntEditField[][] t = new IntEditField[6][];
			public ExpandPane() {
				super("Expansion");
				expand(false);
				t[0] = addItem(" x ");
				t[1] = addItem(" y ");
				t[2] = addItem(" z ");
				t[3] = addItem(" x' ");
				t[4] = addItem(" y' ");
				t[5] = addItem(" z' ");
			}
			public IntEditField[] addItem(String label) {
				HVPanel.h p = new HVPanel.h();
				IntEditField p1 = new IntSpinnerEditField(null, null, 1, 0).to(p);
				p.addComp(new JLabel(label));
				IntEditField p2 = new IntSpinnerEditField(null, null, 1, 0).to(p);
				addSubPane(p);
				return new IntEditField[] {p1, p2};
			}
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==t[0][0]) model3d1.setExpand(t[0][0].getIntValue()+1, model3d1.eym, model3d1.ezm, model3d1.exp, model3d1.eyp, model3d1.ezp);
				else if (e.getSource()==t[0][1]) model3d1.setExpand(model3d1.exm, model3d1.eym, model3d1.ezm, t[0][1].getIntValue()+1, model3d1.eyp, model3d1.ezp);
				else if (e.getSource()==t[1][0]) model3d1.setExpand(model3d1.exm, t[1][0].getIntValue()+1, model3d1.ezm, model3d1.exp, model3d1.eyp, model3d1.ezp);
				else if (e.getSource()==t[1][1]) model3d1.setExpand(model3d1.exm, model3d1.eym, model3d1.ezm, model3d1.exp, t[1][1].getIntValue()+1, model3d1.ezp);
				else if (e.getSource()==t[2][0]) model3d1.setExpand(model3d1.exm, model3d1.eym, t[2][0].getIntValue()+1, model3d1.exp, model3d1.eyp, model3d1.ezp);
				else if (e.getSource()==t[2][1]) model3d1.setExpand(model3d1.exm, model3d1.eym, model3d1.ezm, model3d1.exp, model3d1.eyp, t[2][1].getIntValue()+1);
				else if (e.getSource()==t[3][0]) model3d2.setExpand(t[3][0].getIntValue()+1, model3d2.eym, model3d2.ezm, model3d2.exp, model3d2.eyp, model3d2.ezp);
				else if (e.getSource()==t[3][1]) model3d2.setExpand(model3d2.exm, model3d2.eym, model3d2.ezm, t[3][1].getIntValue()+1, model3d2.eyp, model3d2.ezp);
				else if (e.getSource()==t[4][0]) model3d2.setExpand(model3d2.exm, t[4][0].getIntValue()+1, model3d2.ezm, model3d2.exp, model3d2.eyp, model3d2.ezp);
				else if (e.getSource()==t[4][1]) model3d2.setExpand(model3d2.exm, model3d2.eym, model3d2.ezm, model3d2.exp, t[4][1].getIntValue()+1, model3d2.ezp);
				else if (e.getSource()==t[5][0]) model3d2.setExpand(model3d2.exm, model3d2.eym, t[5][0].getIntValue()+1, model3d2.exp, model3d2.eyp, model3d2.ezp);
				else if (e.getSource()==t[5][1]) model3d2.setExpand(model3d2.exm, model3d2.eym, model3d2.ezm, model3d2.exp, model3d2.eyp, t[5][1].getIntValue()+1);
			}
		}
		
		class TransformPane extends HVPanel.v {
			EditMatrix Q, P;
			EditVector p, q;
			boolean invalid = false;
			Color originalBorderColor;
			
			public TransformPane() {
				super("Transformation");
				originalBorderColor = ((TitledBorder)jPanel.getBorder()).getTitleColor();
				
				HVPanel.h p1 = new HVPanel.h();
				p1.addComp(new JLabel("P = "));
				P = new EditMatrix().to(p1);
				p1.putExtraSpace(10);
				p1.expand(false);
				p1.addComp(new JLabel("origin shift p = "));
				p = new EditVector().to(p1);
				p1.expand(true);
				p1.putExtraSpace();
				p1.expand(false);
				p1.addComp(new JLabel(" (a', b', c') = (a, b, c) P"));
				addSubPane(p1);

				putExtraSpace(3);
				
				HVPanel.h p2 = new HVPanel.h();
				p2.addComp(new JLabel("Q = P"));
				p2.addComp(new JLabel("\u05be"));
				p2.addComp(new JLabel("\u00b9 = "));
				Q = new EditMatrix().to(p2);
				p2.putExtraSpace(10);
				p2.addComp(new JLabel("q = \u2013Q p = "));
				q = new EditVector().to(p2);
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
				
				addSubPane(p2);
				
				P.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				Q.set(1, 0, 0, 0, 1, 0, 0, 0, 1);
				p.set(0, 0, 0);
				q.set(0, 0, 0);
				transformer = new Transformer(P.m, Q.m, q.v);
			}
			
			public void displayError(String err) {
				if (err==null) {
					TitledBorder b = new TitledBorder("Transformation");
					b.setTitleColor(originalBorderColor);
					setBorder(b);
				}
				else {
					TitledBorder b = new TitledBorder(err);
					b.setTitleColor(new Color(150, 0, 0));
					setBorder(b);
				}
			}
			public void displayWarning(String err) {
				if (err==null) {
					TitledBorder b = new TitledBorder("Transformation");
					b.setTitleColor(originalBorderColor);
					setBorder(b);
				}
				else {
					TitledBorder b = new TitledBorder(err);
					b.setTitleColor(new Color(150, 150, 0));
					setBorder(b);
				}
			}
			
			public void actionPerformed(ActionEvent e) {
				Object o = e.getSource();
				if (o==P) {
					try {
						Q.m.invert(P.m);
						Q.updateFields();
						mulNegMatVect(p.v, Q.m, q.v);
						q.updateFields();
						displayError(null);
						invalid = false;
						if (latticeIn!=null) menu.enableSave(true);
						updateModel();
					} catch (Exception ex) {
						displayError("Invalid transformation : P is not inversible ! ");
						Q.invalidate();
						invalid = true;
						menu.enableSave(false);
						mulNegMatVect(p.v, Q.m, q.v);
						q.updateFields();
						updateModel();
					}
				}
				if (o==Q) {
					try {
						P.m.invert(Q.m);
						P.updateFields();
						mulNegMatVect(p.v, Q.m, q.v);
						q.updateFields();
						displayError(null);
						invalid = false;
						if (latticeIn!=null) menu.enableSave(true);
						updateModel();
					} catch (Exception ex) {
						displayError("Invalid transformation : Q is not inversible ! ");
						P.invalidate();
						invalid = true;
						menu.enableSave(false);
						mulNegMatVect(p.v, Q.m, q.v);
						q.updateFields();
						updateModel();
					}
				}
				if (o==p) {
					mulNegMatVect(p.v, Q.m, q.v);
					q.updateFields();
					updateModel();
				}
				if (o==q) {
					mulNegMatVect(q.v, P.m, p.v);
					p.updateFields();
					updateModel();
				}
			}
			
			private void updateModel() {
				// no file, nothing to do
				if (latticeIn==null) return;
				
				// transformation is invalid, clear output model
				if (invalid) {
					centerOriginalCell();
					model3d2.clear();
					bottomPanel.outCell.clear();
					return;
				}
				
				// recalculate the transformation
				transformer.setTranform(P.m, Q.m, q.v);
				atomsOut = transformer.getTransformedAtoms();
				sgOut = transformer.getTransformedSg();
				latticeOut = sgOut.cell;
				
				if (menu.center1.isSelected()) centerOriginalCell();
				else centerTransformedCell();
				model3d2.setAtoms(atomsOut, c2, r2);
				
				bottomPanel.outCell.set(latticeOut);
				cifOut.updateLattice(latticeOut, (int)Math.round(cifIn.getUnitZ()*latticeOut.volume()/volumeIn));
				cifOut.updateAtoms(atomsOut);
				cifPane.updateCifRight(cifOut);
				
				if (!checkTransformIsCorrect()) {
					displayError("Invalid transformation, please check your transformation ! ");
				}
				else {
					if (P.m.determinant()<0) {
						displayWarning("Transformation has negative determinant !");
					}
					else {
						displayError(null);
					}
				}
			}
			
			public void centerOriginalCell() {
				latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z);
				sgIn = new SpaceGroup(sgIn.sg, latticeIn);
				model3d1.setSg(sgIn, c1, "");
				Vector3d v = new Vector3d();
				v.scaleAdd(bottomPanel.transformPane.p.v.x, latticeIn.x, v);
				v.scaleAdd(bottomPanel.transformPane.p.v.y, latticeIn.y, v);
				v.scaleAdd(bottomPanel.transformPane.p.v.z, latticeIn.z, v);
				v.add(latticeIn.o);
				latticeOut = new Lattice(latticeOut.x, latticeOut.y, latticeOut.z, v);
				sgOut = new SpaceGroup(sgP1, latticeOut);
				model3d2.setSg(sgOut, c2, "'");
			}
			
			public void centerTransformedCell() {
				latticeOut = new Lattice(latticeOut.x, latticeOut.y, latticeOut.z);
				sgOut = new SpaceGroup(sgP1, latticeOut);
				model3d2.setSg(sgOut, c2, "'");
				Vector3d v = new Vector3d();
				v.scaleAdd(bottomPanel.transformPane.p.v.x, latticeIn.x, v);
				v.scaleAdd(bottomPanel.transformPane.p.v.y, latticeIn.y, v);
				v.scaleAdd(bottomPanel.transformPane.p.v.z, latticeIn.z, v);
				v.sub(latticeOut.o, v);
				latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z, v);
				sgIn = new SpaceGroup(sgIn.sg, latticeIn);
				model3d1.setSg(sgIn, c1, "");
			}
			
			
//			private void updateq() {
//				Q.m.transform(p.v, q.v);
//				q.v.negate();
//				if (q.v.x==-0) q.v.x=0;
//				if (q.v.y==-0) q.v.y=0;
//				if (q.v.z==-0) q.v.z=0;
//				q.updateFields();
//			}
			
			
//			private void updateOutLattice(boolean do3d) {
//				if (latticeIn==null) return;
//				if (!do3d) {
//					model3d2.clear();
//					return;
//				}
////				System.out.println("x:"+round(latticeIn.x));
////				System.out.println("y:"+round(latticeIn.y));
////				System.out.println("z:"+round(latticeIn.z));
//
//				
//				Matrix3d m = new Matrix3d();
//				m.setColumn(0, latticeIn.x);
//				m.setColumn(1, latticeIn.y);
//				m.setColumn(2, latticeIn.z);
//				
//				m.mul(P.m);
//				
//				Vector3d x = new Vector3d();
//				Vector3d y = new Vector3d();
//				Vector3d z = new Vector3d();
//				m.getColumn(0, x);
//				m.getColumn(1, y);
//				m.getColumn(2, z);
//				
//				
////				System.out.println("x':"+round(x));
////				System.out.println("y':"+round(y));
////				System.out.println("z':"+round(z));
//				
//				System.out.println("det(P)="+P.m.determinant());
//				
//				if (menu.center1.isSelected()) {
//					latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z);
//					sgIn = new SpaceGroup(sgIn.sg, latticeIn);
//					model3d1.setSg(sgIn, c1, "");
//
//					Vector3d v = new Vector3d();
//					v.scaleAdd(p.v.x, latticeIn.x, v);
//					v.scaleAdd(p.v.y, latticeIn.y, v);
//					v.scaleAdd(p.v.z, latticeIn.z, v);
//					v.add(latticeIn.o);
//					latticeOut = new Lattice(x, y, z, v);
//					
//					bottomPanel.outCell.set(latticeOut);
//					sgOut = new SpaceGroup(sgP1, latticeOut);
//					model3d2.setSg(sgOut, c2, "'");
//				}
//				else {
//					latticeOut = new Lattice(x, y, z);
//					sgOut = new SpaceGroup(sgP1, latticeOut);
//					model3d2.setSg(sgOut, c2, "'");
//					Vector3d v = new Vector3d();
//					v.scaleAdd(p.v.x, latticeIn.x, v);
//					v.scaleAdd(p.v.y, latticeIn.y, v);
//					v.scaleAdd(p.v.z, latticeIn.z, v);
//					v.sub(latticeOut.o, v);
//					latticeIn = new Lattice(latticeIn.x, latticeIn.y, latticeIn.z, v);
//					sgIn = new SpaceGroup(sgIn.sg, latticeIn);
//					model3d1.setSg(sgIn, c1, "");
//				}
//				
//				cifOut.setCellA(latticeOut.a);
//				cifOut.setCellB(latticeOut.b);
//				cifOut.setCellC(latticeOut.c);
//				cifOut.setCellAlpha(latticeOut.alpha);
//				cifOut.setCellBeta(latticeOut.beta);
//				cifOut.setCellGamma(latticeOut.gamma);
//				
//				try {
//					cifOut.setCellVolume(latticeOut.a*latticeOut.b*latticeOut.c);
//				} catch (RuntimeException e) {}
//				try {
//					double vin = latticeIn.a*latticeIn.b*latticeIn.c;
//					double vout = latticeOut.a*latticeOut.b*latticeOut.c;
//					double uz = cifIn.getUnitZ();
//					cifOut.setUnitZ((int)Math.round(uz*vout/vin));
//				} catch (RuntimeException e) {}
//			}
//			private void updateOutAtoms(boolean do3d) {
//				if (latticeIn==null) return;
//				if (!do3d) {
//					model3d2.clear();
//					return;
//				}
//				Vector aa = new Vector(100, 100);
//				for (int i=0; i<atomsIn.length; i++) {
//					Vector3d v = new Vector3d(atomsIn[i].x, atomsIn[i].y, atomsIn[i].z);
//					Vector3d[] vv = sgIn.getSymPos(v);
//					for (int j=0; j<vv.length; j++) {
//						if (vv[j]==null) continue;
//						AtomSite a = new AtomSite(atomsIn[i]);
//						Vector3d u = new Vector3d(vv[j]);
//						Q.m.transform(u);
//						u.add(q.v);
//						a.x = u.x;
//						a.y = u.y;
//						a.z = u.z;
//						aa.add(a);
//					}
//				}
//				atomsOut = (AtomSite[]) aa.toArray(new AtomSite[0]);
//				model3d2.setAtoms(atomsOut, c2, r2);
//				Vector v = checkTranslations();
//				System.out.println();
//				cifOut.transformAtoms(cifIn, Q.m, q.v, v);
//				if (!checkTransformIsCorrect()) {
//					displayError("Invalid transformation, please check your transformation ! ");
//				}
//				else {
//					displayError(null);
//				}
//			}
//			private void refreshOutCif() {
//				if (latticeIn==null) return;
//				cifPane.updateCifRight(cifOut);
//			}
			
			private boolean checkTransformIsCorrect() {
				for (int i=0; i<model3d2.atoms.size(); i++) {
					Atom a = (Atom) model3d2.atoms.get(i);
					for (int j=0; j<a.positions.size(); j++) {
						Point3d p = new Point3d(((Position)a.positions.get(j)).pos);
						latticeOut.transform(p);
						latticeIn.reverse(p);
						p = SpaceGroup.modCell(p);
						boolean found = false;
						for (int k=0; k<model3d1.atoms.size(); k++) {
							Position pos = model3d1.getAtomHere(((Atom)model3d1.atoms.get(k)).positions, p);
							if (pos!=null) {
								found = true;
								break;
							}
						}
						if (!found) return false;
					}
				}
				return true;
			}
			
//			private Vector checkTranslations() {
//				Vector v = new Vector();
//				Point3d p100 = new Point3d(1, 0, 0);
//				Q.m.transform(p100);
//				Point3d p010 = new Point3d(0, 1, 0);
//				Q.m.transform(p010);
//				Point3d p001 = new Point3d(0, 0, 1);
//				Q.m.transform(p001);
//
//				int n = (int)Math.abs(Math.round(P.m.determinant()));
//				if (n==1) return v;
//
//				
//				//System.out.println("n="+n);
//				System.out.println("100 -> "+round(p100));
//				System.out.println("010 -> "+round(p010));
//				System.out.println("001 -> "+round(p001));
//				
//				
//				Vector3d t = new Vector3d(); 
//				Vector3d tx = new Vector3d(); 
//				Vector3d ty = new Vector3d(); 
//				Vector3d tz = new Vector3d();
//				
//				for (int l=1; l<5; l++) {
//					for (int i=0; i<=l; i++) {
//						for (int j=0; j<=l; j++) {
//							for (int k=0; k<=l; k++) {
//								if (i==0&j==0&&k==0) continue;
//								if (i<l&&j<l&&k<l) continue;
//								tx.scale(i, p100);
//								ty.scale(j, p010);
//								tz.scale(k, p001);
//								t.add(tx, ty);
//								t.add(tz);
//								t= round(t);
//								//System.out.println(i+" "+j+" "+k+":"+t);
//								if (t.x>-1&&t.y>-1&&t.z>-1&&t.x<0.9999&&t.y<0.9999&&t.z<0.9999) {
//									boolean found=false;
//									for (int m=0; m<v.size(); m++) {
//										if (round(SpaceGroup.modCell((Vector3d)v.get(m))).equals(round(SpaceGroup.modCell(t)))) {
//											found=true;
//											break;
//										}
//									}
//									if (found==false) {
//										addTranslation(t);
//										v.add(new Vector3d(t));
//										System.out.println((i>0?(i>1?(i+"x"):"x"):"")+(j>0?(i>0?"+":"")+(j>1?(j+"y"):"y"):"")+(k>0?(i>0||j>0?"+":"")+(k>1?(k+"z"):"z"):"")+"  "+(round(t)));
//										n--;
//										if (n==1) return v;
//									}
//								}
//							}
//						}
//					}
//				}
//				System.out.println("Out of loop!!!");
//				return v;
//				
////				iLoop: for (int i=0; i<10; i++) {
////					tx.scale(i, p100);
////					for (int j=0; j<10; j++) {
////						ty.scale(j, p010);
////						for (int k=0; k<10; k++) {
////							if (i==0&&j==0&&k==0) continue;
////							tz.scale(k, p001);
////							System.out.println(i+" "+j+" "+k+":"+tz);
////							//if (tz.x>0.99999||tz.y>0.99999||tz.z>0.99999) break;
////							
////							t.add(tx, ty);
////							t.add(tz);
////							if (t.x>0.99999||t.y>0.99999||t.z>0.99999) continue;
////							if (t.x<0||t.y<0||t.z<0) continue;
////							System.out.println(i+" "+j+" "+k+" "+round(t));
////							addTranslation(t);
////							v.add(new Vector3d(t));
////							n--;
////							System.out.println(n);
////							if (n==1) return v;
////						}
////						//if (ty.x>0.99999||ty.y>0.99999||ty.z>0.99999) break;
////					}
////					//if (tx.x>0.99999||tx.y>0.99999||tx.z>0.99999) break;
////				}
////				return v;
//			}
//			
//			
//			private void addTranslation(Vector3d v) {
//				Vector3d r = new Vector3d();
//				for (int i=0; i<atomsOut.length; i++) {
//					r.set((atomsOut[i].x+v.x)%1, (atomsOut[i].y+v.y)%1, (atomsOut[i].z+v.z)%1);
//					r = SpaceGroup.modCell(r);
//					System.out.println("new atom:"+r);
//					//System.out.println(model3d2.getAtomHere(((Atom)(model3d2.atoms.get(i))).positions, new Point3d(r)));
//					AtomSite a = new AtomSite(atomsOut[i].atom, atomsOut[i].symbol, atomsOut[i].label, r.x, r.y, r.z, atomsOut[i].occupancy, atomsOut[i].oxydation);
//					model3d2.addAtom(a, c2, r2);
//				}
//			}
		}
	}
	
	public static void mulNegMatVect(Tuple3d p, Matrix3d m, Vector3d q) {
		m.transform(p, q);
		q.negate();
		if (q.x==-0) q.x=0;
		if (q.y==-0) q.y=0;
		if (q.z==-0) q.z=0;
	}
	
	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(1000*p.x)/1000d, Math.round(1000*p.y)/1000d, Math.round(1000*p.z)/1000d);
	}
	public static Vector3d round(Vector3d p) {
		return new Vector3d(Math.round(1000*p.x)/1000d, Math.round(1000*p.y)/1000d, Math.round(1000*p.z)/1000d);
	}
}

class EditMatrix {
	private VerticalVector[] edit;
	public Matrix3d m;
	private HVPanel.h p;
	
	public EditMatrix() {
		edit = new VerticalVector[3];
		p = new HVPanel.h() {
			public void actionPerformed(ActionEvent e) {
				double d = EditVector.parseFrac(""+((VerticalVector)e.getSource()).edit[e.getID()].getValue());
				int col = e.getSource()==edit[0]?0:(e.getSource()==edit[1]?1:2);
				m.setElement(e.getID(), col, d);
				super.actionPerformed(new ActionEvent(EditMatrix.this, 0, ""));
			}
		};
		p.addComp(new OpenBracket());
		p.addSubPane(edit[0]=new VerticalVector());
		p.addSubPane(edit[1]=new VerticalVector());
		p.addSubPane(edit[2]=new VerticalVector());
		p.addComp(new CloseBracket());
		m = new Matrix3d();
	}
	public EditMatrix to(HVPanel parent) {
		parent.addSubPane(p);
		return this;
	}
	public void set(Matrix3d m) {
		this.m.set(m);
		updateFields();
	}
	public void set(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
		m.m00=m00; m.m01=m01; m.m02=m02; 
		m.m10=m10; m.m11=m11; m.m12=m12;
		m.m20=m20; m.m21=m21; m.m22=m22;
		updateFields();
	}
	public void updateFields() {
		if (m.m00==-0) m.m00=0; if (m.m10==-0) m.m10=0; if (m.m20==-0) m.m20=0;
		if (m.m01==-0) m.m01=0; if (m.m11==-0) m.m11=0; if (m.m21==-0) m.m21=0;
		if (m.m02==-0) m.m02=0; if (m.m12==-0) m.m12=0; if (m.m22==-0) m.m22=0;
		edit[0].set(m.m00, m.m10, m.m20);
		edit[1].set(m.m01, m.m11, m.m21);
		edit[2].set(m.m02, m.m12, m.m22);
	}
	public void invalidate() {
		m.m00=Double.NaN; m.m01=Double.NaN; m.m02=Double.NaN; 
		m.m10=Double.NaN; m.m11=Double.NaN; m.m12=Double.NaN;
		m.m20=Double.NaN; m.m21=Double.NaN; m.m22=Double.NaN;
		updateFields();
	}
	public void setColor(Color c) {
		edit[0].setColor(c);
		edit[1].setColor(c);
		edit[2].setColor(c);
	}
	public void clearColor() {
		Color c = edit[0].originalColor;
		edit[0].setColor(c);
		edit[1].setColor(c);
		edit[2].setColor(c);
	}
}

class EditVector {
	private VerticalVector edit;
	public Vector3d v;
	private HVPanel.h p;
	
	public EditVector() {
		p = new HVPanel.h() {
			public void actionPerformed(ActionEvent e) {
				VerticalVector vv = ((VerticalVector)e.getSource());
				v.set(parseFrac(""+vv.edit[0].getValue()), parseFrac(""+vv.edit[1].getValue()), parseFrac(""+vv.edit[2].getValue())); 
				super.actionPerformed(new ActionEvent(EditVector.this, 0, ""));
			}
		};
		p.addComp(new OpenBracket());
		p.addSubPane(edit=new VerticalVector());
		p.addComp(new CloseBracket());
		v = new Vector3d();
	}
	public EditVector to(HVPanel parent) {
		parent.addSubPane(p);
		return this;
	}
	public void set(Vector3d v) {
		this.v.set(v);
		updateFields();
	}
	public void set(double x, double y, double z) {
		this.v.set(x, y, z);
		updateFields();
	}
	public void updateFields() {
		if (v.x==-0) v.x=0; if (v.y==-0) v.y=0; if (v.z==-0) v.z=0;
		edit.set(v.x, v.y, v.z);
	}
	
	public static double parseFrac(String s) throws NumberFormatException {
		int p = s.indexOf('/');
		if (p==-1) return Double.parseDouble(s);
		return Double.parseDouble(s.substring(0, p))/Double.parseDouble(s.substring(p+1));
	}
}

class OpenBracket extends JComponent {
	static final int w=5;
	public OpenBracket() {
		setPreferredSize(new Dimension(w, 0));
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		int r=getHeight()/2;
		g.drawOval(0, getHeight()/2-r, getHeight()/4, r*2);
	}
}
class CloseBracket extends JComponent {
	static final int w=5;
	public CloseBracket() {
		setPreferredSize(new Dimension(w, 0));
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		int r=getHeight()/2;
		g.drawOval(-getHeight()/4+w-1, getHeight()/2-r, getHeight()/4, r*2);
	}
}

class VerticalVector extends HVPanel.v implements PropertyChangeListener {
	public JFormattedTextField[] edit;
	public boolean quiet = false;
	public Color originalColor;
	
	public VerticalVector() {
		edit = new JFormattedTextField[3];
		expand(false);
		NumberFormatter f = createFormatter("#.##");
		for (int i=0; i<edit.length; i++) {
			addComp(edit[i]=new JFormattedTextField(f) {
				// I've overwritten this because an exception is thrown when fractional
				// value is entered. The exception seems to have no use.
				protected void setFormatter(AbstractFormatter arg0) {
					try {
						super.setFormatter(arg0);
					} catch (RuntimeException e) {}
				}
			});
			edit[i].setColumns(3);
			edit[i].setHorizontalAlignment(JTextField.CENTER);
			edit[i].addPropertyChangeListener(this);
		}
		originalColor = edit[0].getBackground();
	}
	
	public void setColor(Color c) {
		edit[0].setBackground(c);
		edit[1].setBackground(c);
		edit[2].setBackground(c);
	}
	
	public void set(double x, double y, double z) {
		quiet = true;
		edit[0].setValue(new Double(x));
		edit[1].setValue(new Double(y));
		edit[2].setValue(new Double(z));
		quiet = false;
	}
	public void propertyChange(PropertyChangeEvent e) {
		if (!quiet && e.getPropertyName().equals("value") && !e.getNewValue().equals(e.getOldValue())) {
			int row = e.getSource()==edit[0]?0:(e.getSource()==edit[1]?1:2);
			actionPerformed(new ActionEvent(this, row, ""));
		}
	}
	public static NumberFormatter createFormatter(String format) {
		DecimalFormat df = new DecimalFormat(format) {
			public Object parseObject(String s) throws ParseException {
				int p = s.indexOf('/');
				if (p!=-1) {
					super.parseObject(s.substring(0, p));
					super.parseObject(s.substring(p+1));
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
		expand(false);
		addComp(new JLabel(x));
		addComp(new JLabel(y));
		addComp(new JLabel(z));
	}
}
