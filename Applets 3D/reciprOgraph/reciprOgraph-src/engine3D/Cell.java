package engine3D;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import sg.Lattice;
import utils.ColorConstants;
import utils.Utils3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

public class Cell {
	private BranchGroup root;
	private Group parent;
	private Lattice sg;
	private TransformGroup tg0, tg0box;
	
	public Cell(Lattice sg, double repereSize, String[] labels) {
		set(sg, repereSize, labels, ColorConstants.white, ColorConstants.green);
	}
	public Cell(Lattice sg, double repereSize, String[] labels, Color3f box, Color3f repere) {
		set(sg, repereSize, labels, box, repere);
	}

	public void set(Lattice sg, double repereSize, String[] labels, Color3f box, Color3f repere) {
		this.sg = sg;
		if (root!=null) hide();
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_DETACH);
		createBox(box);
		createRepere(labels, (float)repereSize, repere);
	}
	
	public void show(Group parent) {
		this.parent=parent;
		parent.addChild(root);
	}
	public void hide() {
		parent.removeChild(root);
	}
	
	public void setBoxScale(double x, double y, double z) {
		Transform3D t3d = new Transform3D();
		Matrix3d m = new Matrix3d(x, 0, 0, 0, y, 0, 0, 0, z);
		t3d.set(m);
		tg0.setTransform(t3d);
		t3d = new Transform3D();
		Vector3d v = new Vector3d(1-.5*x, 1-.5*y, 1-.5*z);
		sg.transform(v);
		t3d.set(v);
		tg0box.setTransform(t3d);
	}
	
	private void createBox(Color3f c) {
		Appearance app = Utils3d.createApp(c, 0.7f);
		Box box = new Box(.5f, .5f, .5f, app);
		tg0 = new TransformGroup();
		tg0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D t3d = new Transform3D();
		t3d.set(sg.m);
		TransformGroup tg1 = new TransformGroup(t3d);
		Vector3d gg = new Vector3d(sg.g);
		gg.negate();
		t3d.set(sg.m, gg, 1);
		TransformGroup tg3 = new TransformGroup(t3d);
		root.addChild(tg3);

		tg0.addChild(box);
		tg1.addChild(tg0);
		root.addChild(tg1);
	}
	private void createRepere(String[] labels, float size, Color3f c) {
		Appearance app = Utils3d.createApp(c);
		Point3d o = new Point3d(0, 0, 0);
		Point3d e1 = new Point3d(.3, 0, 0);
		Point3d e2 = new Point3d(0, .3, 0);
		Point3d e3 = new Point3d(0, 0, .3);
		sg.transform(o);
		sg.transform(e1);
		sg.transform(e2);
		sg.transform(e3);
		tg0box = new TransformGroup();
		tg0box.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg0box.addChild(Utils3d.createArrow(o, e1, size, 2*size, 5*size, app, 20));
		tg0box.addChild(Utils3d.createArrow(o, e2, size, 2*size, 5*size, app, 20));
		tg0box.addChild(Utils3d.createArrow(o, e3, size, 2*size, 5*size, app, 20));
		tg0box.addChild(Utils3d.createAtom(o, size, app, 20));
		tg0box.addChild(Utils3d.createLegend(labels[0], e1, e1, size, app, false));
		tg0box.addChild(Utils3d.createLegend(labels[1], e2, e2, size, app, false));
		tg0box.addChild(Utils3d.createLegend(labels[2], e3, e3, size, app, false));
		root.addChild(tg0box);
	}
}



