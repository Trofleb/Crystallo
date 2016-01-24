package panes;

import intensity.Intensity;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dragNdrop.CifFileOpener;

import panes.*;
import powder.PowderPane;
import sg.SpaceGroup;
import structures.CifEditor;
import structures.CifFile;
import structures.CifFileFilter;
import structures.Icsd;
import sun.security.action.GetBooleanAction;
import utils.HVPanel;
import utils.HVPanel.EditField;
import utils.HVPanel.FloatEditField;
import utils.HVPanel.IntEditField;
import utils.HVPanel.SliderAndValueH;

/* TestApplet - MainPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 nov. 06
 * 
 * nicolas.schoeni@epfl.ch
 */

public class MainPane extends TriplePane implements CifFileOpener {
	CifEditor cifViewer;
	Help help;
	Icsd icsd;
	public PowderPane powderPane;
	public Intensity intensity;
	public CifFile cifFile;
	public SingleCrystalPane singleCrystalPane;
	public MainApp applet;

	public void openFile(File f) {
		try {
			CifFile cif = new CifFile(f);
			SpaceGroup sg = cif.getSg();	// just to check if file is correct
			cif.getAtoms();
			if (intensity==null) intensity = new Intensity(cif);
			else intensity.setCif(cif);
			intensity.showCell();
			cifFile = cif;
			String s = cif.getFormula();
			if (applet.frame!=null) applet.frame.setTitle("ReciprOgraph"+(s.trim().length()==0?"":(" - "+s)));
			powderPane.dataset.needRecalculate=true;
			SingleCrystalPane.needRePrint = true;
		} catch (AccessControlException e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(applet.frame, "You can't drop a file to this window because you run it as an untrusted web applet.");
		} catch (Exception e) {
			e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(applet.frame, "Can't read file or bad format !");
		}
	}

	public void showOpenDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileFilter(new CifFileFilter());
		if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			File[] f = chooser.getSelectedFiles();
			if (f==null) return;
			for (int i=0; i<f.length; i++) {
				openFile(f[i]);
			}
		}
	}

	class MainTab extends HVPanel.h implements ChangeListener {
		JTabbedPane jtab;

		public MainTab(Container mainPane) {
			jtab = new JTabbedPane();
			jtab.addTab("Single crystal", mainPane);
			jtab.addTab("Powder diagram", (powderPane=new PowderPane(intensity)).jPanel);
			jtab.addTab("Structure factors", singleCrystalPane.jPanel);
			addComp(jtab);
			jtab.setSelectedIndex(0);
			jtab.addChangeListener(this);
		}
		public void stateChanged(ChangeEvent e) {
			if (jtab.getSelectedIndex()==2) {
				SingleCrystalPane.show();
			}
		}
	}
	
	public void stop() {
		help.show(false);
		icsd.show(false);
		cifViewer.show(false);
	}
	public void destroy() {
		Intensity.univers.cleanup();
	}
	
	
	public MainPane(MainApp applet) {
		this.applet = applet;
		ProjScreen.scaleZoom = 6;
		ProjScreen.scale2D = 7;
		ProjScreen.colorIndex=new Color(20, 20, 240, 255); 
		ProjScreen.colorIntensity=new Color(240, 20, 20, 255);
		setPanes(ProjScreen.createPanel(), new BottomPanel());
		Intensity.iMin = 0;
		Intensity.init(univers, true);
		cifViewer = new CifEditor();
		cifViewer.setActionListener(this);
		icsd = new Icsd(applet.getCodeBase());
		icsd.setActionListener(this);
		help = new Help();
		singleCrystalPane=new SingleCrystalPane(this);
		
		if (applet.getBoolParameter("openCif")) {
			try {
				URL u = new URL("http://escher.epfl.ch/servlet/crystalOgraph/cif");
				CifFile c = new CifFile(u.openStream());
				intensity = new Intensity(c);
				intensity.showCell();
				cifFile = c;
				String s = cifFile.getFormula();
				applet.frame.setTitle(MainApp.title+" - "+(s.trim().length()==0?"structure imported from crystalOgraph":s));
			} catch (Exception e) {
				e.printStackTrace(System.err);
				JOptionPane.showMessageDialog(applet.frame, "Can't read file or bad format !");
			}
		}
		else {
			try {
				URL u = getClass().getResource("/1913_Hull, W.H.;Bragg, W.L._C_F d -3 m S_Carbon.cif");
				InputStream in = u.openStream();
				CifFile cif = new CifFile(in);
				intensity = new Intensity(cif);
				intensity.showCell();
				cifFile = cif;
				applet.frame.setTitle(MainApp.title+" - Diamond");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==cifViewer) {
			try {
				String[] ss = cifViewer.getFile();
				CifFile cif = new CifFile(ss);
				SpaceGroup sg = cif.getSg();	// just to check if file is correct
				cif.getAtoms();
				cifFile = cif;
				if (intensity==null) intensity = new Intensity(cif);
				else intensity.setCif(cif);
				intensity.showCell();
				String s = cif.getFormula();
				if (applet.frame!=null) applet.frame.setTitle("ReciprOgraph"+(s.trim().length()==0?"":(" - "+s)));
				powderPane.dataset.needRecalculate=true;
				SingleCrystalPane.needRePrint = true;
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
				JOptionPane.showMessageDialog(applet.frame, "Bad CIF format !");
			}
		}
		if (e.getSource()==icsd) {
			String[] data = icsd.getData();
			CifFile cif = new CifFile(data);					
			SpaceGroup sg = cif.getSg();	// just to check if file is correct
			cif.getAtoms();
			cifFile = cif;
			if (intensity==null) intensity = new Intensity(cif);
			else intensity.setCif(cif);
			intensity.showCell();
			String s = cif.getFormula();
			if (applet.frame!=null) applet.frame.setTitle("ReciprOgraph"+(s.trim().length()==0?"":(" - "+s)));
			powderPane.dataset.needRecalculate=true;
			SingleCrystalPane.needRePrint = true;
		}
	}

	class BottomPanel extends HVPanel.h {
		UVWpane uVWpane;
		HKLpane hKLpane;
		OpenPane openPane;
		int n = Intensity.hMax*Intensity.u + Intensity.kMax*Intensity.v + Intensity.lMax*Intensity.w;

		public BottomPanel() {
			expand(false);
			left();
			addSubPane(openPane=new OpenPane());
			putExtraSpace(5);
			addSubPane(hKLpane=new HKLpane());
			putExtraSpace(5);
			addSubPane(uVWpane=new UVWpane());
			putExtraSpace(5);
			addSubPane(new UniversPane());
			putExtraSpace(5);
			addSubPane(new ScaleFactors());
		}

		class OpenPane extends HVPanel.h {
			private JButton openCifButton, getIcsdButton, helpButton;

			public OpenPane() {
				setBorder(new TitledBorder("Files"));
				expand(false);
				HVPanel.v p1 = new HVPanel.v(); 
				p1.expand(false);
				p1.bottom();
				p1.addButton(getIcsdButton=new JButton("ICSD"));
				p1.addButton(openCifButton=new JButton("CIF file"));
				p1.addButton(helpButton=new JButton("Help"));
				addSubPane(p1);
			}

			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==openCifButton) {
					cifViewer.setFile(cifFile.data);
					cifViewer.show(cifFile.getFormula());
				}
				else if (e.getSource()==getIcsdButton) {
					icsd.show(true);
				}
				else if (e.getSource()==helpButton) {
					help.show(true);
				}
			}
		}

		class UniversPane extends HVPanel.v {
			private JRadioButton parallel, perspective;
			public UniversPane() {
				setBorder(new TitledBorder("3D view"));
				addButtonGroupped(perspective=new JRadioButton("Perspective"));
				addButtonGroupped(parallel=new JRadioButton("Parallel"));
			}
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==parallel) {
					univers.setParallel(true);
				}
				else if (e.getSource()==perspective) {
					univers.setParallel(false);
				}
			}
		}

		public int abs(int a) {
			return a<0?-a:a;
		}

		public void changeN() {
			n = Intensity.hMax*abs(Intensity.u) + Intensity.kMax*abs(Intensity.v) + Intensity.lMax*abs(Intensity.w);
			uVWpane.tEdit.setMinimum(-n);
			uVWpane.tEdit.setMaximum(n);
			int t = uVWpane.tEdit.getIntValue();
			if (t>n) {
				uVWpane.tEdit.setValue(n);
				Intensity.setUVWT(Intensity.u, Intensity.v, Intensity.w, n);
			}
			if (t<-n) {
				uVWpane.tEdit.setValue(-n);
				Intensity.setUVWT(Intensity.u, Intensity.v, Intensity.w, -n);
			}
			uVWpane.tSlide.slider.setMinimum(-n);
			uVWpane.tSlide.slider.setMaximum(n);
			Hashtable labelTable = new Hashtable();
			labelTable.put(new Integer(-n), new JLabel("-"+n) );
			labelTable.put(new Integer(n), new JLabel("+"+n) );
			labelTable.put(new Integer(0), new JLabel("0") );
			uVWpane.tSlide.slider.setLabelTable(labelTable);
			uVWpane.tSlide.slider.setMajorTickSpacing(n);
		}

		class HKLpane extends HVPanel.h {
			public boolean quiet=false;
			private IntEditField x, y, z;
			private JCheckBox linked;
			public HKLpane() {
				setBorder(new TitledBorder("Bounds"));
				HVPanel.v p1 = new HVPanel.v(); 
				x = new HVPanel.IntSpinnerEditField(" h max", null, 2, Intensity.hMax).to(p1);
				y = new HVPanel.IntSpinnerEditField(" k max", null, 2, Intensity.kMax).to(p1);
				z = new HVPanel.IntSpinnerEditField(" l max", null, 2, Intensity.lMax).to(p1);
				x.setMinimum(0);
				y.setMinimum(0);
				z.setMinimum(0);
				x.setMaximum(Intensity.hklMaxSize);
				y.setMaximum(Intensity.hklMaxSize);
				z.setMaximum(Intensity.hklMaxSize);
				HVPanel.v p2 = new HVPanel.v(); 
				p2.bottom();
				p2.putExtraSpace();
				p2.addButton(linked=new JCheckBox("h=k=l"));
				addSubPane(p1);
				addSubPane(p2);			
				linked.setSelected(true);
				y.setEnable(false);
				z.setEnable(false);
			}

			public void actionPerformed(ActionEvent e) {
				int h = Integer.parseInt(""+x.getValue());
				int k = Integer.parseInt(""+y.getValue());
				int l = Integer.parseInt(""+z.getValue());

				if (e.getSource()==linked) {
					boolean b = linked.isSelected();
					y.setEnable(!b);
					z.setEnable(!b);
					if (b) {
						Object v = x.getValue();
						quiet=true;
						y.setValue(v);
						z.setValue(v);
						quiet=false;
						Intensity.setHKL(h, h, h);
					}
				}
				else if (e.getSource()==x) {
					if (linked.isSelected()) {
						Object v = x.getValue();
						quiet=true;
						y.setValue(v);
						z.setValue(v);
						quiet=false;
						k=l=h;
					}
					Intensity.setHKL(h, k, l);
				}
				else if (e.getSource()==y) {
					if (!linked.isSelected()) {
						Intensity.setHKL(h, k, l);
					}
				}
				else if (e.getSource()==z) {
					if (!linked.isSelected()) {
						Intensity.setHKL(h, k, l);
					}
				}
				if (!quiet) {
					uVWpane.quiet=true;
					changeN();
					uVWpane.quiet=false;
				}
			}
		}

		class UVWpane extends HVPanel.h {
			public boolean quiet=false;
			private JButton b100, b010, b001, b111;
			private JCheckBox drawIndex, drawI;
			private EditField x, y, z;
			private SliderAndValueH tSlide;
			private IntEditField tEdit;
			private FloatEditField iMin;

			public UVWpane() {
				setBorder(new TitledBorder("Projection"));
				HVPanel.v p1 = new HVPanel.v(); 
				x = new HVPanel.IntSpinnerEditField(" u", null, 2, Intensity.u).to(p1);
				y = new HVPanel.IntSpinnerEditField(" v", null, 2, Intensity.v).to(p1);
				z = new HVPanel.IntSpinnerEditField(" w", null, 2, Intensity.w).to(p1);
				HVPanel.v p2 = new HVPanel.v();
				Insets insets = new Insets(0, 0, 0, 0);
				Font font = new Font(null, Font.PLAIN, 10);
				b100 = new JButton("100");
				b100.setMargin(insets);
				b100.setFont(font);
				p2.addButton(b100);
				b010 = new JButton("010");
				b010.setMargin(insets);
				b010.setFont(font);
				p2.addButton(b010);
				b001 = new JButton("001");
				b001.setMargin(insets);
				b001.setFont(font);
				p2.addButton(b001);
				expand(false);
				addSubPane(p1);
				addSubPane(p2);

				HVPanel.v p5 = new HVPanel.v(); 
				HVPanel.h p3 = new HVPanel.h(); 
				p3.expand(false);
				p3.left();
				tEdit = new HVPanel.IntSpinnerEditField("Layer index   ", null, 2, Intensity.t).to(p3);
				tEdit.setMinimum(-n);
				tEdit.setMaximum(n);

				p3.expand(true);
				p3.putExtraSpace();
				p3.expand(false);
				p3.addComp(new JLabel("Show "));
				p3.addButton(drawIndex=new JCheckBox("index"));
				p3.addButton(drawI=new JCheckBox("value"));
				drawIndex.setSelected(ProjScreen.drawIndex);
				drawI.setSelected(ProjScreen.drawI);

//				p3.putExtraSpace(5);
//				iMin = new HVPanel.FloatSpinnerEditField("Min", "%", 2, (float)Intensity.iMin, "0.0", 1).to(p3);
//				iMin.setMinimum(0);
//				iMin.setMaximum(100);

				p5.addSubPane(p3);

				HVPanel.h p4 = new HVPanel.h(); 
				p4.expand(true);
				tSlide = new HVPanel.SliderAndValueH(null, null, -n, n, Intensity.t, 0, 300).to(p4);
				Hashtable labelTable = new Hashtable();
				labelTable.put(new Integer(-n), new JLabel("-"+n) );
				labelTable.put(new Integer(n), new JLabel("+"+n) );
				labelTable.put(new Integer(0), new JLabel("0") );
				tSlide.slider.setSnapToTicks(true);
				tSlide.slider.setLabelTable(labelTable);
				tSlide.slider.setMajorTickSpacing(n);
				tSlide.slider.setMinorTickSpacing(1);
				tSlide.slider.setPaintTicks(true);
				tSlide.slider.setPaintLabels(true);
				tSlide.slider.setMinimumSize(new Dimension(300, 45));
				tSlide.slider.setPreferredSize(new Dimension(300, 45));
				tSlide.setValue(Intensity.t);
				p5.addSubPane(p4);
				addSubPane(p5);
			}
			public void actionPerformed(ActionEvent e) {
				if (quiet==true) return;
				int u=Intensity.u, v=Intensity.v, w=Intensity.w;

				if (e.getSource()==b100) {
					quiet=true;
					x.setValue(new Integer(1));
					y.setValue(new Integer(0));
					z.setValue(new Integer(0));
					quiet=false;
					u=1; v=0; w=0;
				}
				else if (e.getSource()==b010) {
					quiet=true;
					x.setValue(new Integer(0));
					y.setValue(new Integer(1));
					z.setValue(new Integer(0));
					quiet=false;
					u=0; v=1; w=0;
				}
				else if (e.getSource()==b001) {
					quiet=true;
					x.setValue(new Integer(0));
					y.setValue(new Integer(0));
					z.setValue(new Integer(1));
					quiet=false;
					u=0; v=0; w=1;
				}
				else if (e.getSource()==b111) {
					quiet=true;
					x.setValue(new Integer(1));
					y.setValue(new Integer(1));
					z.setValue(new Integer(1));
					quiet=false;
					u=1; v=1; w=1;
				}
				else if (e.getSource()==x) {
					u=Integer.parseInt(""+x.getValue());
				}
				else if (e.getSource()==y) {
					v=Integer.parseInt(""+y.getValue());
				}
				else if (e.getSource()==z) {
					w=Integer.parseInt(""+z.getValue());
				}
				else if (e.getSource()==tSlide) {
					quiet=true;
					tEdit.setValue((int)tSlide.getValue());
					quiet=false;
				}
				else if (e.getSource()==tEdit) {
					quiet=true;
					tSlide.setValue(Integer.parseInt(""+tEdit.getValue()));
					quiet=false;
				}
				else if (e.getSource()==iMin) {
					quiet=true;
					Intensity.setImin(iMin.getFloatValue());
					quiet=false;
					return;
				}
				if (!quiet) {
					Intensity.setUVWT(u, v, w, (int)tSlide.getValue());
					Intensity.setProjSettings(drawIndex.isSelected(), drawI.isSelected());
				}
				quiet=true;
				changeN();
				quiet=false;
			}
		}
		class ScaleFactors extends HVPanel.h {
			private SliderAndValueH scale;
			public ScaleFactors() {
				setBorder(new TitledBorder("Scale factor"));
				expand(false);
				HVPanel.v p2 = new HVPanel.v(); 
				p2.expand(false);
				scale = new HVPanel.SliderAndValueH(null, null, 1, 201, Intensity.scaleUser*100, 0, 100).to(p2);

				scale.slider.setMajorTickSpacing(100);
				scale.slider.setMinorTickSpacing(20);
				scale.slider.setPaintTicks(true);

				scale.slider.setMinimumSize(new Dimension(100, 30));
				scale.slider.setPreferredSize(new Dimension(100, 30));

				addSubPane(p2);
			}
			public void actionPerformed(ActionEvent e) {
				if (e.getSource()==scale) {
					Intensity.setScaleFactor(scale.getValue()/100);
				}
			}
		}
	}
}

