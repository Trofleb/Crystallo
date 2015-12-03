/* Symmetry - Test.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 28 févr. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleFanArray;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import objects.*;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;

import u3d.TranspObject;
import u3d.Univers;
import u3d.Univers.Selectable;
import utils.ColorConstants;
import utils.Utils3d;

public class Test extends JApplet {
	Univers univers;
	BranchGroup root = new BranchGroup(); 
	public JFrame frame;
	public static JApplet applet;

	public void init() {
		applet = this;
		if (getParameter("window") != null && getParameter("window").equals("true")) {
			getContentPane().add(new Label("Applet launched. Refresh page to load again..."));
			frame = createFrame();
		  frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
					stop();
					destroy();
		    }
		  });
		}
		else {
			frame = null;
			JPanel p = new JPanel();
			p.setLayout(new BorderLayout());
			p.add(univers.getCanvas());
		  setContentPane(p);
		  univers.getCanvas().addKeyListener(keyListener);
		}
	}
	
	public static void main(String[] args) {
		applet = null;
		Test test = new Test();
		test.frame = test.createFrame();
		test.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void destroy() {
		univers.cleanup();
	}

	public JFrame createFrame() {
		frame = new JFrame("Symmetry");
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(univers.getCanvas());
	  frame.setContentPane(p);
	  univers.getCanvas().addKeyListener(keyListener);
		frame.setSize(900, 700);
	  frame.setVisible(true);
	  frame.validate();
	  return frame;
	}
	
	
	public Test() {
		univers = new Univers();
		univers.setBackgroundColor(ColorConstants.white);

		//showGrayModel();
		SphericonTest();
	}

	KeyListener keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			String k = KeyEvent.getKeyText(e.getKeyCode());
			if (k.equals("S")) {
				root.removeAllChildren();
				Sphericon.dx=Sphericon.dx<.1?.4:0;
				Sphericon.init();
				TranspObject p = new Sphericon().ref;
				p.attach(root);
			}
			else if (k.equals("F")) {
				root.removeAllChildren();
				Sphericon.showFaces=!Sphericon.showFaces;
				TranspObject p = new Sphericon().ref;
				p.attach(root);
			}
			else if (k.equals("E")) {
				root.removeAllChildren();
				Sphericon.showEdges=!Sphericon.showEdges;
				TranspObject p = new Sphericon().ref;
				p.attach(root);
			}
		}
	};
	
	public BranchGroup biCone(float radius, float height, Color3f color) {
		Appearance coneApp = Utils3d.createApp(color);
		coneApp.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL,PolygonAttributes.CULL_NONE, 0.0f));
		Cone cone1 = new Cone(radius, height, Cone.GENERATE_NORMALS, coneApp);
		cone1.removeChild(Cone.CAP);
		
//		System.out.println(((TriangleFanArray)((Shape3D)cone1.getChild(0)).getGeometry()).getCoordinates(index, coordinates));
		
		
		Transform3D t3d1 = new Transform3D();
		t3d1.set(new Vector3d(0, .5, 0));
		TransformGroup t1 = new TransformGroup(t3d1);
		t1.addChild(cone1);

		Cone cone2 = new Cone(radius, height, Cone.GENERATE_NORMALS, coneApp);
		cone2.removeChild(Cone.CAP);
		Transform3D t3d21 = new Transform3D();
		t3d21.set(new Matrix3d(1, 0, 0, 0, -1, 0, 0, 0, 1));
		TransformGroup t21 = new TransformGroup(t3d21);
		t21.addChild(cone2);
		Transform3D t3d22 = new Transform3D();
		t3d22.set(new Vector3d(0, -.5, 0));
		TransformGroup t22 = new TransformGroup(t3d22);
		t22.addChild(t21);
		
		BranchGroup e = new BranchGroup();
		e.addChild(t1);
		e.addChild(t22);
		return e;
	}
	
	public void SphericonTest() {
//		BranchGroup biCone = biCone(1, 1, new Color3f(1, 0, 0));
//		univers.getRoot().addChild(biCone);

		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		TranspObject p = new Sphericon().ref;
		p.attach(root);
		univers.getRoot().addChild(root);
	}
	
	
	
	public void drawFaces() {
		double s = 1; 
		Point3d[] planPoints = {
				new Point3d(0, -s, -s), new Point3d(0, -s, s), new Point3d(0, s, s), new Point3d(0, s, -s),
				new Point3d(-s, 0, -s), new Point3d(-s, 0, s), new Point3d(s, 0, s), new Point3d(s, 0, -s),
				new Point3d(-s, -s, 0), new Point3d(-s, s, 0), new Point3d(s, s, 0), new Point3d(s, -s, 0),
		};
		BranchGroup rep = Utils3d.createRepere(ColorConstants.green, ColorConstants.green,ColorConstants.green, new String[]{"x","y","z"}, .003f, .06f, 0.2, 0, new Vector3d(s, 0, 0), new Vector3d(0, s, 0), new Vector3d(0, 0, s));
		Material mat = new Material(ColorConstants.yellow, ColorConstants.black, ColorConstants.yellow, ColorConstants.white, 128);
		int[][] planFaces = {{0,1,2,3}};
		Shape3D plan = TranspObject.createFaces(planPoints, planFaces, Utils3d.createApp(mat, 0.6f));
		BranchGroup bgPlan = new BranchGroup();
		bgPlan.setCapability(BranchGroup.ALLOW_DETACH);
		bgPlan.addChild(plan);
		bgPlan.addChild(rep);
		univers.getRoot().addChild(bgPlan);
	}


	public void showGrayModel() {
		BranchGroup e = TranspObject.createEdges(Rhomboedre.points, Rhomboedre.faces, Utils3d.createApp(ColorConstants.black), .02);
		Arrete.setArretesClickables(e);
//		Arrete.setArreteLight(e, 0);
//		Arrete.setArreteLight(e, 1);
//		Arrete.setArreteLight(e, 3);
//		Arrete.setArreteLight(e, 6);
		univers.getRoot().addChild(e);
	}

}




class Arrete implements Selectable {
	Cylinder c;
	Group p, pp;
	Arrete(Cylinder c, Group p, Group pp) {
		this.c=c; this.p=p; this.pp=pp;
	}
	public void click() {
		pp.removeChild(p);
		Appearance a = Utils3d.createApp(new Color3f(.5f, .5f, .5f));
		Cylinder c2 = new Cylinder(.01f, 1f, Cylinder.GENERATE_NORMALS, 10, 1, a);
		BranchGroup p2 = new BranchGroup();
		p2.addChild(c2);
		pp.addChild(p2);
	}
	public static void setArreteLight(BranchGroup root, int i) {
		BranchGroup b = (BranchGroup)Utils3d.pickElementOfType(root, BranchGroup.class, i);
		Cylinder c = (Cylinder)Utils3d.pickElementOfType(b, Cylinder.class, 0);
		Group p = ((Group)c.getParent());
		p.removeChild(c);
		Appearance a = Utils3d.createApp(new Color3f(.5f, .5f, .5f));
		Cylinder c2 = new Cylinder(.01f, 1f, Cylinder.GENERATE_NORMALS, 10, 1, a);
		p.addChild(c2);
	}
	public static void setArretesClickables(BranchGroup root) {
		for (int i=0; i<root.numChildren(); i++) {
			Cylinder c = (Cylinder)Utils3d.pickElementOfType((Group)root.getChild(i), Cylinder.class, 0);
			for (int j=0; j<c.numChildren(); j++) {
				c.getChild(j).setUserData(new Arrete(c, (Group)c.getParent(), (Group)(((Group)c.getParent()).getParent())));
				((Shape3D)c.getChild(j)).getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
			}
		}
	}
}
