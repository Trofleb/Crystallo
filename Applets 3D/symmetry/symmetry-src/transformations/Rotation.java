/* Symmetry - Rotation.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 24 févr. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package transformations;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.SwingUtilities;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objects.Polyedre;

import u3d.TranspObject;
import utils.ColorConstants;
import utils.HVPanel;
import utils.Utils3d;


public class Rotation extends Transformation implements ColorConstants {
	private Material matUnsel;
	private static final Material matSel = new Material(black, black, black, white, 128);
	private double angle = 0;
	private Point3d p1, p2;
	private Thread highlightThread;
	private boolean highlighted = false;
	private Appearance axisApp;
	private boolean animating = false;
	private HVPanel.SliderAndValue slider;
	public boolean quiet = false;
	
	public Rotation(String axisName, String axisName2, HVPanel parentPanel, Polyedre polyedre, Point3d p1, Point3d p2, double length, Color3f c) {
		super(polyedre);
		this.matUnsel = new Material(c, black, c, white, 128);
		this.p1=p1; this.p2=p2;
		createPanel(axisName2, parentPanel);
		create3dAxis(axisName, polyedre.tgRot, length);
	}

	public void setAngle(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.set(rotMatrix(p1, p2, angle-this.angle));
		addTransform(t3d);
		this.angle = angle;
	}

/*	Thread unsafe !
	public void anim(final double delta) {
		if (animating) return;
		animating = true;
		axisApp.setMaterial(matSel);
		new Thread() {
			public void run() {
				int n = 55;
				for (int i=1; i<=n; i++) {
					double newAngle = angle+(delta/(double)n);
					setAngle(newAngle);
					slider.setValue((((angle*180/Math.PI)+180)%360)-180);
					angle = newAngle;
					try {sleep(30);} catch (Exception e) {}
				}
				axisApp.setMaterial(matUnsel);
				animating = false;
			}
		}.start();
	}
*/
	
	public void highlight() {
		if (highlighted) {
			highlightThread.interrupt();
		}
		else {
			highlighted = true;
			axisApp.setMaterial(matSel);
			highlightThread = new Thread() {
				public void run() {
					boolean done = false;
					while (!done) {
						try {
							sleep(500);
							done = true;
						}
						catch(InterruptedException e){}
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							axisApp.setMaterial(matUnsel);
						}
					});
					highlighted = false;
				}
			};
			highlightThread.start();
		}
	}
	
	public void reset() {
		super.reset();
		angle = 0;
		slider.setValue(0);
	}

	private void createPanel(final String axisName, HVPanel parentPanel) {
		slider = parentPanel.addSliderAndValueH(axisName, "°", -180, 180, 0, 0, 140);
		slider.slider.setMajorTickSpacing(6);
		slider.slider.setSnapToTicks(true);
		parentPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!quiet && e.getActionCommand().equals(axisName)) {
					highlight();
					setAngle(slider.getValue()*Math.PI/180);
				}
			}
		});
	}

	private void create3dAxis(String axisName, Group parentGroup, double length) {
		axisApp = new Appearance();
		axisApp.setMaterial(matUnsel);
		axisApp.setCapability(Appearance.ALLOW_MATERIAL_READ);
		axisApp.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		Vector3d v = new Vector3d();
		v.sub(p2, p1);
		v.scale(1.2);
		Point3d pp2 = new Point3d(v); 
		v.negate();
		Point3d pp1 = new Point3d(v); 
		parentGroup.addChild(Utils3d.createCylinder(pp1, pp2, .01, axisApp, 10));

		v.sub(p2, p1);
		Vector3d u = new Vector3d(v);
		u.normalize();
		u.scale(0.15);
		v.scale(1.2);
		v.add(u);
		Point3d pp3 = new Point3d(v); 
		v.negate();
		Point3d pp4 = new Point3d(v); 
		
		parentGroup.addChild(Utils3d.createLegendEx(axisName, pp3, pp3, .008f, axisApp));
		parentGroup.addChild(Utils3d.createLegendEx(axisName, pp4, pp4, .008f, axisApp));
	}

	public static Matrix4d rotMatrix(Point3d a, Point3d b, double alpha) {
		double dx = b.x - a.x, dy = b.y - a.y, dz = b.z - a.z;
	  double t  = Math.atan2(dy, dx);				// avec atan2(b,a) = atan(b/a)
	  double p  = Math.atan2(Math.sqrt(dx*dx + dy*dy), dz);
	  double ct = Math.cos(t), st = Math.sin(t), ct2 = ct * ct, st2 = st * st;
	  double cp = Math.cos(p), sp = Math.sin(p), cp2 = cp * cp, sp2 = sp * sp;
	  double ca = Math.cos(alpha);
	  double sa = Math.sin(alpha);
	  double c  = 1 - ca;
	  Matrix4d m = new Matrix4d();
	  m.m00 = ct2 * (ca * cp2 + sp2) + ca * st2;
	  m.m01 = sa  *  cp + c   * sp2  * ct * st;
	  m.m02 = sp  * (cp * ct  * c    - sa * st);
		m.m10 = sp2 *  ct * st  * c    - sa * cp;
		m.m11 = st2 * (ca * cp2 + sp2) + ca * ct2;
		m.m12 = sp  * (cp * st  * c    + sa * ct);
		m.m20 = sp  * (cp * ct  * c    + sa * st);
		m.m21 = sp  * (cp * st  * c    - sa * ct);
		m.m22 = ca  * sp2 + cp2;
		m.m30 = a.x  - a.x * m.m00 - a.y * m.m10 - a.z * m.m20;
		m.m31 = a.y  - a.x * m.m01 - a.y * m.m11 - a.z * m.m21;
		m.m32 = a.z  - a.x * m.m02 - a.y * m.m12 - a.z * m.m22;
		m.m33 = 1;
		return m;
	}
}
