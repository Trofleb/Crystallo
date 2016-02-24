import java.awt.Font;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class Atom extends BranchGroup implements ColorConstants {
	Point3d pos;
	float radius;
	Color3f color;
	Appearance appBack;
	boolean selected;
	boolean hidden;
	Sphere sel;
	Atom up, symUp;
	Vector down, symDown;
	BranchGroup selBranch;
	BranchGroup atomBranch;
	int atomGroupIndex;
	String label;

	public Atom(Point3d pos, float radius, Color3f color, int index, boolean visible, Cell cell) {
		this.setCapability(Group.ALLOW_CHILDREN_READ);
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.pos = pos;
		this.radius = radius;
		this.color = color;
		this.selected = false;
		this.appBack = new Appearance();
		this.appBack.setMaterial(new Material(color, black, color, white, 120.0f));
		this.addChild(createAtom(pos, radius, this.appBack, this, cell));
		this.down = new Vector(5, 5);
		this.symDown = new Vector(5, 5);
		this.atomGroupIndex = index;
		this.hidden = !visible;
	}

	@Override
	public String toString() {
		return "Atom " + posToString(this.pos);
	}

	public static double round(double p) {
		return Math.round(p * 100f) / 100f;
	}

	public static float round(float p) {
		return Math.round(p * 100f) / 100f;
	}

	public static String posToString(double p) {
		return "" + Math.round(p * 100f) / 100f;
	}

	public static String posToString(Tuple3d p) {
		return "(" + posToString(p.x) + " " + posToString(p.y) + " " + posToString(p.z) + ")";
	}

	public static String posToString2(Tuple3d p) {
		return posToString(p.x) + " " + posToString(p.y) + " " + posToString(p.z);
	}

	public static String posToString(float[] p) {
		return posToString(new Point3d(p[0], p[1], p[2]));
	}

	public static Tuple3d round(Tuple3d p) {
		p.x = round(p.x);
		p.y = round(p.y);
		p.z = round(p.z);
		return p;
	}

	public static Tuple3f round(Tuple3f p) {
		p.x = round(p.x);
		p.y = round(p.y);
		p.z = round(p.z);
		return p;
	}

	public void setColor(Color3f color) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(color, black, color, white, 120.0f));

		BranchGroup bg1 = (BranchGroup) this.getChild(0);
		TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
		BranchGroup bg3 = (BranchGroup) tg2.getChild(0);
		((Sphere) bg3.getChild(0)).setAppearance(app);
	}

	public void setColorBack() {
		BranchGroup bg1 = (BranchGroup) this.getChild(0);
		TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
		BranchGroup bg3 = (BranchGroup) tg2.getChild(0);
		((Sphere) bg3.getChild(0)).setAppearance(this.appBack);
	}

	public void select() {
		Appearance app = new Appearance();
		app.setMaterial(new Material(yellow, black, yellow, white, 120.0f));
		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
		app.setTransparencyAttributes(transp);

		this.sel = new Sphere(this.radius * 1.2f, Primitive.GENERATE_NORMALS, 50, app);
		this.selBranch = new BranchGroup();
		this.selBranch.setCapability(BranchGroup.ALLOW_DETACH);
		this.selBranch.addChild(this.sel);

		BranchGroup bg1 = (BranchGroup) this.getChild(0);
		TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
		BranchGroup bg3 = (BranchGroup) tg2.getChild(0);
		bg3.addChild(this.selBranch);

		this.selected = true;
	}

	public void unSelect() {
		BranchGroup bg1 = (BranchGroup) this.getChild(0);
		TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
		BranchGroup bg3 = (BranchGroup) tg2.getChild(0);
		bg3.removeChild(this.selBranch);
		this.selected = false;
		this.sel = null;
	}

	public void tryReselect() {
		if (this.selected) {
			BranchGroup bg1 = (BranchGroup) this.getChild(0);
			TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
			BranchGroup bg3 = (BranchGroup) tg2.getChild(0);
			bg3.addChild(this.selBranch);
		}
	}

	public static Group createAtom(Point3d pos, float radius, Appearance app, Object userData, Cell cell) {

		Sphere s = new Sphere(radius, Primitive.GENERATE_NORMALS, 50, app);
		try {
			s.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
		} catch (Exception e) {
		}

		s.getShape().setCapability(Node.ALLOW_BOUNDS_READ);
		s.getShape().setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		Transform3D t = new Transform3D();
		t.set(new Vector3d(cell.coord(pos.x, pos.y, pos.z)));
		TransformGroup obj = new TransformGroup(t);
		obj.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj.setCapability(Group.ALLOW_CHILDREN_READ);

		BranchGroup atomBranch = new BranchGroup();
		atomBranch.setCapability(Group.ALLOW_CHILDREN_READ);
		atomBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		atomBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);
		atomBranch.addChild(s);
		obj.addChild(atomBranch);

		s.getShape().setUserData(userData);

		BranchGroup objBranch = new BranchGroup();
		objBranch.setCapability(BranchGroup.ALLOW_DETACH);
		objBranch.setCapability(Group.ALLOW_CHILDREN_READ);
		objBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		objBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);

		objBranch.addChild(obj);

		return objBranch;
	}

	public static Group createAtomAbs(Point3d pos, float radius, Color3f color, Object userData) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(color, black, color, white, 120.0f));

		Sphere s = new Sphere(radius, Primitive.GENERATE_NORMALS, 50, app);

		Transform3D t = new Transform3D();
		t.set(new Vector3d(pos.x, pos.y, pos.z));
		TransformGroup obj = new TransformGroup(t);
		obj.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		obj.addChild(s);

		s.getShape().setUserData(userData);
		return obj;
	}

	public void reCellPos(Cell cell) {
		Transform3D t = new Transform3D();
		t.set(new Vector3d(cell.coord(this.pos.x, this.pos.y, this.pos.z)));
		BranchGroup bg1 = (BranchGroup) this.getChild(0);
		TransformGroup tg2 = (TransformGroup) bg1.getChild(0);
		tg2.setTransform(t);
	}

	public static Group createLegend(String s, Point3d pos, Point3d rot, float size, Appearance app) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 2), new FontExtrusion());
		Text3D txt = new Text3D(f3d, s, new Point3f(0, 0, 0));
		OrientedShape3D textShape = new OrientedShape3D();
		textShape.setGeometry(txt);
		textShape.setAppearance(app);

		textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		textShape.setRotationPoint(new Point3f(rot));

		Transform3D tt3d = new Transform3D();
		tt3d.set(new Vector3d(pos.x, pos.y, pos.z));
		TransformGroup tt = new TransformGroup(tt3d);

		tt3d = new Transform3D();
		tt3d.set(size);
		TransformGroup ttt = new TransformGroup(tt3d);

		ttt.addChild(textShape);
		tt.addChild(ttt);
		bg.addChild(tt);
		return bg;
	}
}
