package bottomPanel;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import bottomPanel.HVPanel.SliderAndValue;

import diffrac.DefaultValues;
import diffrac.Help;
import diffrac.Lattice;

import projScreen.ProjScreen;

import model3d.Animator;
import model3d.ColorConstants;
import model3d.Model3d;
import model3d.Net;
import model3d.Utils3d;

public class BottomPanel extends HVPanel.h {
	private Model3d model3d;
	private LatticePane lPane, rPane;
	private Parameters paramPane;
	public  Animation animPane;
	public  Help help;
	private Lattice lattice, reciprocal; 
	private int u, v, w;
	DefaultValues defaultValues;
	
	public BottomPanel(DefaultValues defaultValues, Model3d model3d) {
		super();
		this.defaultValues = defaultValues;
		this.model3d = model3d;
		help = new Help();
		lattice = new Lattice(defaultValues.lattice.a, defaultValues.lattice.b, defaultValues.lattice.c, defaultValues.lattice.alpha, defaultValues.lattice.beta, defaultValues.lattice.gamma);
		lattice.setOrientation(defaultValues.uvw[0], defaultValues.uvw[1], defaultValues.uvw[2]);
		reciprocal = lattice.reciprocal();
		
		addSubPane(lPane = new LatticePane("Unit cell", "", lattice));
		addSubPane(rPane = new LatticePane("Reciprocal lattice", "*", reciprocal));
		addSubPane(new CrystalSize());
		addSubPane(paramPane = new Parameters());
		addSubPane(animPane=new Animation());
		addSubPane(new Screen());
	}
	
	private boolean sync = true;
	class LatticePane extends HVPanel.v {
		private EditField a, b, c, alpha, beta, gamma;
		public LatticePane(String name, String suffix, Lattice def) {
			setBorder(new TitledBorder(name));
			HVPanel p1 = new HVPanel.v();
			a = p1.addFloatField("a"+suffix, "Å⁻¹", 4, (float)def.a, "0.00");
			b = p1.addFloatField("b"+suffix, "Å⁻¹", 4, (float)def.b, "0.00");
			c = p1.addFloatField("c"+suffix, "Å⁻¹", 4, (float)def.c, "0.00");
			HVPanel p2 = new HVPanel.v();
			alpha = p2.addIntField("alpha"+suffix, "°", 3, (int)Math.round(def.alpha));
			beta = p2.addIntField("beta"+suffix, "°", 3, (int)Math.round(def.beta));
			gamma = p2.addIntField("gamma"+suffix, "°", 3, (int)Math.round(def.gamma));
			addSubPane(p1);
			addSubPane(p2);
		}
		public void put(Lattice l) {
			a.setValue(new Float(l.a));
			b.setValue(new Float(l.b));
			c.setValue(new Float(l.c));
			alpha.setValue(new Float(l.alpha));
			beta.setValue(new Float(l.beta));
			gamma.setValue(new Float(l.gamma));
		}
		public void actionPerformed(ActionEvent e) {
			if (sync) {
				sync = false;
				if (this==lPane) {
					HVPanel.quiet = true;
					lattice = new Lattice(a.getFloatValue(), b.getFloatValue(), c.getFloatValue(), alpha.getFloatValue(), beta.getFloatValue(), gamma.getFloatValue());
					lattice.setOrientation(u, v, w);
					reciprocal = lattice.reciprocal();
					rPane.put(reciprocal);
					HVPanel.quiet = false;
				}
				else if (this==rPane) {
					HVPanel.quiet = true;
					reciprocal = new Lattice(a.getFloatValue(), b.getFloatValue(), c.getFloatValue(), alpha.getFloatValue(), beta.getFloatValue(), gamma.getFloatValue());
					Lattice l = reciprocal.reciprocal();
					lattice = new Lattice(l.a, l.b, l.c, l.alpha, l.beta, l.gamma);
					lattice.setOrientation(u, v, w);
					reciprocal = lattice.reciprocal();
					lPane.put(lattice);
					HVPanel.quiet = false;
				}
				sync = true;
			}
			model3d.net.setLattice(reciprocal);
			model3d.doRays(false);
		}
	}

	
	class CrystalSize extends HVPanel.v {
		private EditField x, y, z;
		public CrystalSize() {
			setBorder(new TitledBorder("Lattice"));
			HVPanel p1 = new HVPanel.v();
			x = p1.addIntFieldSpinner("h max", null, 2, defaultValues.crystalX);
			y = p1.addIntFieldSpinner("k max", null, 2, defaultValues.crystalY);
			z = p1.addIntFieldSpinner("l  max", null, 2, defaultValues.crystalZ);
			((NumberFormatter)x.edit.getFormatter()).setMinimum(new Integer(0));
			((NumberFormatter)y.edit.getFormatter()).setMinimum(new Integer(0));
			((NumberFormatter)z.edit.getFormatter()).setMinimum(new Integer(0));
			HVPanel p0 = new HVPanel.h();
			p0.addSubPane(p1);
			
			HVPanel.v p2 = new HVPanel.v();
			p2.addButton(new JButton("More"));
			p2.addButton(new JButton("Less"));
			addSubPane(p0);
			addSubPane(p2);
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("More")) {
				HVPanel.quiet = true;
				x.setValue(new Integer(Math.round(x.getFloatValue())+1));
				y.setValue(new Integer(Math.round(y.getFloatValue())+1));
				z.setValue(new Integer(Math.round(z.getFloatValue())+1));
				HVPanel.quiet = false;
			}
			else if (e.getActionCommand().equals("Less")) {
				int a;
				HVPanel.quiet = true;
				x.setValue(new Integer((a=(int)x.getFloatValue())==0?0:a-1));
				y.setValue(new Integer((a=(int)y.getFloatValue())==0?0:a-1));
				z.setValue(new Integer((a=(int)z.getFloatValue())==0?0:a-1));
				HVPanel.quiet = false;
			}
			model3d.net.setCrystalSize((int)x.getFloatValue(), (int)y.getFloatValue(), (int)z.getFloatValue());
			model3d.doRays(false);
		}
	}
	
	public class Parameters extends HVPanel.v {
		public SliderAndValue rotX, rotY, rotZ, lambda, precess;
		public EditField uvw;
		public Parameters() {
			setBorder(new TitledBorder("Parameters"));
			this.expand(true);
			HVPanel.v p1 = new HVPanel.v();
			p1.expand(true);
			rotX = p1.addSliderAndValueH("Omega", "°", -180, 180, defaultValues.omega, 0, 120);
			rotY = p1.addSliderAndValueH("Chi", "°", -180, 180, defaultValues.chi, 0, 120);
			rotZ = p1.addSliderAndValueH("Phi", "°", -180, 180, defaultValues.phi, 0, 120);
			lambda = p1.addSliderAndValueH("Lambda", "°", .2f, 3.5f, (float)defaultValues.lambda, 2, 120);
			precess = p1.addSliderAndValueH("Precession", "°", -180, 180, defaultValues.precession, 0, 120);

			HVPanel.v p3 = new HVPanel.v();
			p3.putExtraSpace();
			uvw = p3.addSpecialFormatField("u v w", null, 5, defaultValues.uvw, new ThreeCoordsFormat());
			uvw.edit.setMinimumSize(new Dimension(60, 20));
			p3.expand(false);
			p3.bottom();
			
			HVPanel.h p2 = new HVPanel.h();
			p2.addButton(new JButton("Reset angles"));
			p2.putExtraSpace();
			p2.addSubPane(p3);
			
			addSubPane(p1);
			bottom();
			addSubPane(p2);
			
			u = defaultValues.uvw[0];
			v = defaultValues.uvw[1];
			w = defaultValues.uvw[2];
			model3d.orientationClass.setOmega(defaultValues.omega);
			model3d.orientationClass.setChi(defaultValues.chi);
			model3d.orientationClass.setPhi(defaultValues.phi);
			model3d.precessionClass.setAngle(defaultValues.mu);
			model3d.precessionClass.setRotation(defaultValues.precession);
		}
		public void actionPerformed(ActionEvent e) {
			boolean ajustR = false;
			if (e.getActionCommand().equals("Lambda")) {
				double l = ((SliderAndValue)e.getSource()).getValue();
				model3d.s.setLambda(l);
				model3d.net.gonioHead.setY(model3d.s.lambdaToRadius(l));
				ajustR = true;
			}
			else if (e.getActionCommand().equals("Omega")) {
				model3d.orientationClass.setOmega(((SliderAndValue)e.getSource()).getValue());
			}
			else if (e.getActionCommand().equals("Chi")) {
				model3d.orientationClass.setChi(((SliderAndValue)e.getSource()).getValue());
			}
			else if (e.getActionCommand().equals("Phi")) {
				model3d.orientationClass.setPhi(((SliderAndValue)e.getSource()).getValue());
			}
			else if (e.getActionCommand().equals("Precession")) {
				model3d.precessionClass.setRotation(((SliderAndValue)e.getSource()).getValue());
			}
			else if (e.getActionCommand().equals("Reset angles")) {
				rotX.setValue(0);
				rotY.setValue(0);
				rotZ.setValue(0);
				precess.setValue(0);
//				HVPanel.quiet = true;
//				uvw.edit.setText("0 1 0");
//				HVPanel.quiet = false;
				model3d.projScreen.clearImage();
			}
			else if (e.getActionCommand().equals("u v w")) {
				HVPanel.quiet = true;
				int[] ii = ((int[])((EditField)e.getSource()).getValue());
				u=ii[0]; v=ii[1]; w=ii[2];
				lattice.setOrientation(ii[0], ii[1], ii[2]);
				reciprocal = lattice.reciprocal(); 
				model3d.net.setLattice(reciprocal);
				HVPanel.quiet = false;
			}
			model3d.doRays(ajustR);
		}
		private int sgn(double d) {
			return d==0?0:(d>0?1:-1);
		}
		private void set(double omega, double chi, double phi) {
			HVPanel.quiet = true;
			rotX.setValue(omega);
			rotY.setValue(chi);
			rotZ.setValue(phi);
			model3d.orientationClass.setOmega(omega);
			model3d.orientationClass.setChi(chi);
			model3d.orientationClass.setPhi(phi);
			HVPanel.quiet = false;
		}
	}
	
	
	public class Animation extends HVPanel.h {
		private EditField from, to, angle;
		private JCheckBox fromToEnable;
		public  Animator animator;
		private JButton laue;
		private SliderAndValue speed;
		private JToggleButton precession;
		private JCheckBox mask;
		
		public Animation() {
			setBorder(new TitledBorder("Animation"));
			HVPanel.v p1 = new HVPanel.v();
			
			HVPanel p2 = new HVPanel.h();
			p2.addButton(new JToggleButton("Omega"));
			p2.addButton(new JToggleButton("Chi"));
			p2.addButton(new JToggleButton("Phi"));
			p1.addSubPane(p2);
			
			HVPanel p3 = new HVPanel.h();
			p3.addButton(new JToggleButton("Sequential"));
			p3.addButton(new JToggleButton("Debye-Scherrer"));
			p1.addSubPane(p3);

			HVPanel.h p5 = new HVPanel.h();
			p5.left();
			p5.addButton(fromToEnable = new JCheckBox(""));
			HVPanel.v p61 = new HVPanel.v();
			p61.expand(false);
			from = p61.addIntField("", "°", 2, defaultValues.startAngle);
			p5.addSubPane(p61);
			HVPanel.v p62 = new HVPanel.v();
			p62.expand(false);
			to = p62.addIntField("-", "°", 2, defaultValues.stopAngle);
			p5.addSubPane(p62);
			p5.putExtraSpace();
			p1.addSubPane(p5);
			p5.expand(true);
			speed = p5.addSliderAndValueH("Speed", null, 1, 20, defaultValues.speed, 0, 80);
			
			HVPanel p7 = new HVPanel.h();
			p7.addButton(new JToggleButton("Lambda"));
			p7.addButton(laue=new JButton("Laue"));
			p1.addSubPane(p7);
			//p1.putExtraSpace();
			
			HVPanel.h p8 = new HVPanel.h();
			p8.expand(true);
			p8.addButton(precession=new JToggleButton("Precession"));
			HVPanel.v p9 = new HVPanel.v();
			p9.expand(false);
			angle = p9.addIntFieldSpinner(" Angle", "°", 2, defaultValues.mu);
			model3d.precessionClass.setAngle(defaultValues.mu);
			model3d.mask3d.setR(Math.sin(model3d.precessionClass.mu)*(model3d.p3d.y*defaultValues.maskDistFract));
			
			p8.addSubPane(p9);
			p8.addButton(mask=new JCheckBox("Mask"));
			p1.addSubPane(p8);
			
			addSubPane(p1);
			laue.setForeground(Color.blue);
			animator = new Animator();
			animator.from = defaultValues.startAngle;
			animator.to = defaultValues.stopAngle;
			animator.fromToEnable = false;
			animator.speed = 1;
			
			setSpeed();
			
			fromToEnable.setSelected(false);
			from.setEnable(false);
			to.setEnable(false);
		}

		public void setSpeed() {
			if (fromToEnable.isSelected()) {
				int d = Math.round(to.getFloatValue()-from.getFloatValue());
				animator.speed = (int)Math.round(speed.getValue()*d/360);
				if (animator.speed==0) animator.speed=1;
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == fromToEnable) {
				boolean b = ((JCheckBox)e.getSource()).isSelected();
				animator.fromToEnable = b;
				from.setEnable(b);
				to.setEnable(b);
				if (!b) animator.speed = (int)Math.round(speed.getValue());
			}
			else if (e.getSource() == from) {
				animator.from = from.getFloatValue();
			}
			else if (e.getSource() == to) {
				animator.to = to.getFloatValue();
			}
			else if (e.getSource() == speed) {
				animator.speed = speed.getValue();
			}
			else if (e.getActionCommand().equals("Omega")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateSingleAngle(paramPane.rotX, paramPane.rotX.getValue(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Chi")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateSingleAngle(paramPane.rotY, paramPane.rotY.getValue(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Phi")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateSingleAngle(paramPane.rotZ, paramPane.rotZ.getValue(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Lambda")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateLambda(paramPane.lambda, paramPane.lambda.getMin(), paramPane.lambda.getMax(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Debye-Scherrer")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateRandom(paramPane.rotX, paramPane.rotY, paramPane.rotZ, (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Laue")) {
				model3d.projScreen.clearImage();
				model3d.doLaue();
			}
			else if (e.getActionCommand().equals("Sequential")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animateSequential(paramPane.rotX, paramPane.rotY, paramPane.rotZ, paramPane.rotX.getValue(), paramPane.rotY.getValue(), paramPane.rotZ.getValue(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
			else if (e.getActionCommand().equals("Speed")) {
				animator.speed = (int)Math.round(((SliderAndValue)e.getSource()).getValue());
				setSpeed();
			}
			else if (e.getActionCommand().equals(" Angle")) {
				double mu = angle.getFloatValue();
				model3d.precessionClass.setAngle(mu);
				model3d.mask3d.setR(Math.sin(model3d.precessionClass.mu)*(model3d.p3d.y*defaultValues.maskDistFract));
				model3d.doRays(false);
			}
			else if (e.getActionCommand().equals("Mask")) {
				model3d.setMask(((JCheckBox)e.getSource()).isSelected());
				model3d.doRays(false);
			}
			else if (e.getActionCommand().equals("Precession")) {
				if (((JToggleButton)e.getSource()).isSelected()) {
					animator.animatePrecession(paramPane.precess, paramPane.precess.getValue(), (JToggleButton)e.getSource());
				}
				else {
					animator.stopAnimation();
				}
			}
		}
	}
	
	class Screen extends HVPanel.v {
		private EditField w, h, y;
		private JCheckBox persistant;
		private boolean flat;
		public Screen() {
			setBorder(new TitledBorder("Screen"));
			HVPanel.h p1 = new HVPanel.h();
			w=p1.addIntFieldSpinner("Size ", null, 2, (int)defaultValues.wScreen);
			h=p1.addIntFieldSpinner(" x ", "cm", 2, (int)defaultValues.hFlatScreen);
			((NumberFormatter)w.edit.getFormatter()).setMinimum(new Integer(1));
			((NumberFormatter)h.edit.getFormatter()).setMinimum(new Integer(1));
			addSubPane(p1);
			HVPanel.h p10 = new HVPanel.h();
			y = p10.addIntFieldSpinner("Distance ", "cm", 2, (int)defaultValues.zScreen);
			addSubPane(p10);
			
			HVPanel p11 = new HVPanel.h();
			p11.addButtonGroupped(new JRadioButton("Flat"));
			p11.addButtonGroupped(new JRadioButton("Cylindric"));
			addSubPane(p11);
			addButton(persistant = new JCheckBox("Persistant"));
			persistant.setSelected(true);
			HVPanel p2 = new HVPanel.h();
			p2.addButton(new JButton("Clear"));
			//p2.addButton(new JButton("Snap"));
			p2.addButton(new JButton("Help"));
			addSubPane(p2);
			flat = true;
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Size ")||e.getActionCommand().equals(" x ")) {
				double dw = w.getFloatValue(), dh = h.getFloatValue();
				model3d.setScreenSize(dw, dh);
			}
			else if (e.getActionCommand().equals("Distance ")) {
				model3d.projScreen.clearImage();
				double d = y.getFloatValue();
				model3d.p3d.setPos(d);
				model3d.mask3d.setY(d);
			}
			else if (e.getActionCommand().equals("Flat")) {
				if (flat) return;
				flat=true;
				super.actionPerformed(new ActionEvent(this, 0, "horizontal"));
				w.setEnable(true);
				model3d.setFlatScreen();
				h.setValue(new Double(model3d.p3d.h));
				w.setValue(new Double(model3d.p3d.w));

				model3d.univers.scale(2);
				model3d.precessionClass.setAngle(animPane.angle.getFloatValue());
				model3d.precessionClass.setRotation(paramPane.precess.getValue());
				paramPane.precess.setEnabled(true);
				animPane.angle.setEnable(true);
				animPane.precession.setEnabled(true);
				animPane.mask.setEnabled(true);
				model3d.setMask(animPane.mask.isSelected());
			}
			else if (e.getActionCommand().equals("Cylindric")) {
				if (!flat) return;
				flat=false;
				super.actionPerformed(new ActionEvent(this, 0, "vertical"));
				w.setEnable(false);
				w.name.setEnabled(true);
				model3d.setCylindricScreen();
				h.setValue(new Double(model3d.p3d.h));
				w.edit.setText("");

				model3d.univers.scale(.5);
				model3d.precessionClass.setAngle(0);
				model3d.precessionClass.setRotation(0);
				paramPane.precess.setEnabled(false);
				animPane.angle.setEnable(false);
				animPane.precession.setEnabled(false);
				animPane.mask.setEnabled(false);
				model3d.setMask(false);
			}
			else if (e.getActionCommand().equals("Persistant")) {
				model3d.persistant = ((JCheckBox)e.getSource()).isSelected();
			}
			else if (e.getActionCommand().equals("Clear")) {
				model3d.projScreen.clearImage();
			}
			else if (e.getActionCommand().equals("Help")) {
				help.show(true);
			}
			model3d.doRays(false);
		}
	}
	
	
	
	class ThreeCoordsFormat extends DefaultFormatter {
		public ThreeCoordsFormat() {
			setOverwriteMode(false);
		}	
		private String[] split(String s) {
			if (s.length()==3)
				return new String[] {""+s.charAt(0), ""+s.charAt(1), ""+s.charAt(2)};
			else {
				Vector v = new Vector(3, 3);
				for (int last=0, i=0; i<=s.length(); i++) {
					if (i==s.length() || (("+-".indexOf(s.charAt(i))!=-1 || Character.isDigit(s.charAt(i))) && i>0 && s.charAt(i-1)==' ')) {
						v.add(s.substring(last, i).trim());
						last=i;
					}
				}
				return (String[]) v.toArray(new String[0]);
			}
		}
	
		public Object stringToValue(String text) throws ParseException {
			try {
				String[] ss = split(text);
/*
				String[] ss = text.trim().split("( *)( |\\+|\\-)( *)");
				if (ss.length==1 && ss[0].length()==3)
					ss = new String[] {""+ss[0].charAt(0), ""+ss[0].charAt(1), ""+ss[0].charAt(2)};
				else if (ss.length==2) {
					if (ss[0].length()==2)
						ss = new String[] {""+ss[0].charAt(0), ""+ss[0].charAt(1), ""+ss[0].charAt(2)};
					else if (ss[1].length()==2)
						ss = new String[] {""+ss[0].charAt(0), ""+ss[0].charAt(1), ""+ss[0].charAt(2)};
					else
						throw new ParseException("", 0);
				}
				else if (ss.length==3);
				else
					throw new ParseException("", 0);
					
					
					for (int i=0; i<ss.length; i++) {
						if (ss[i].replaceAll("\\+|\\-", "").length()>1) {
						}
					}
*/					
				if (ss.length!=3) throw new ParseException("", 0);
				int[] ii = new int [3];
				for (int i=0; i<ss.length; i++) {
					ii[i] = Integer.parseInt(ss[i]);
				}
				
				return ii;
			} 
			catch (Exception e) {
/*				new Thread() {
					public void run() {
						Color c = paramPane.uvw.edit.getBackground();
						paramPane.uvw.edit.setBackground(Color.red);
						try {Thread.sleep(100);} catch(Exception e){};
						paramPane.uvw.edit.setBackground(c);
					}
				}.start();*/
				throw new ParseException("", 0);
			}
		}
		public String valueToString(Object v) throws ParseException {
			if (v==null) return "null";
			if (!(v instanceof int[])) return ""+v;
			return ((int[])v)[0]+" "+((int[])v)[1]+" "+((int[])v)[2];
		}
	}
	
	
	public static void main(String[] a) {
		String[] ss = "2357".split("");
		for (int i=0; i<ss.length; i++) {
			System.out.println("*"+ss[i]);
		}
		
	}
	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(1000*p.x)/1000d, Math.round(1000*p.y)/1000d, Math.round(1000*p.z)/1000d);
	}
	public static Vector3d round(Vector3d p) {
		return new Vector3d(Math.round(1000*p.x)/1000d, Math.round(1000*p.y)/1000d, Math.round(1000*p.z)/1000d);
	}
}


