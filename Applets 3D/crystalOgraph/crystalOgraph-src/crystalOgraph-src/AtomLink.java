import java.util.Vector;

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
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;



public class AtomLink extends BranchGroup implements ColorConstants {

	Atom a1, a2;
	float radius;
	Color3f color;
	AtomLink up;
	Vector down;
	boolean selected;
	BranchGroup sel;
	BranchGroup cyl;
	public boolean userMade;
	Vector hiddenIn;
	Cell cell;

	public AtomLink(Cell cell, Atom a1, Atom a2, float radius, Color3f color) {
		this.cell=cell;
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		
		this.a1=a1;
		this.a2=a2;
		this.radius=radius;
		this.color=color;
		Appearance app = new Appearance();
		app.setMaterial(new Material(color, black, color, white, 120.0f));
		cyl = createCylinder(cell.coord(a1.pos), cell.coord(a2.pos), radius, app, 12);
		this.addChild(cyl);
		Shape3D shape = ((Cylinder)((Group)cyl.getChild(0)).getChild(0)).getShape(Cylinder.BODY);
		shape.setUserData(this);
		try {
			shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
		} catch (Exception e){}
		this.down=new Vector(5, 5);
	}

	
	public void changeColor(Color3f c) {
		this.color = c;
		Appearance app = new Appearance();
		app.setMaterial(new Material(c, black, c, white, 120.0f));
		((Cylinder)((Group)cyl.getChild(0)).getChild(0)).setAppearance(app);
	}

	public void changeRadius(float r) {
		this.radius = r;

		this.removeChild(cyl);
		Appearance app = new Appearance();
		app.setMaterial(new Material(color, black, color, white, 120.0f));
		cyl = createCylinder(cell.coord(a1.pos), cell.coord(a2.pos), r, app, 12);
		Shape3D shape = ((Cylinder)((Group)cyl.getChild(0)).getChild(0)).getShape(Cylinder.BODY);
		shape.setUserData(this);
		try {
			shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
		} catch (Exception e){}
		
		this.addChild(cyl);

		if (selected) {
			unSelect();
			select();
		}
	}
	
	public void select() {
		Appearance app = new Appearance();
		app.setMaterial(new Material(yellow, black, yellow, white, 120.0f));
		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
		app.setTransparencyAttributes(transp);

		sel = createCylinder(cell.coord(a1.pos), cell.coord(a2.pos), radius*1.5f, app, 12);
		sel.setCapability(BranchGroup.ALLOW_DETACH);
		this.addChild(sel);
		selected=true;
	}

	public void unSelect() {
		this.removeChild(sel);
		selected=false;
		sel=null;
	}

	
	public static Group createLinkAbs(Point3d p1, Point3d p2, float radius, Color3f color) {
		Appearance app = new Appearance();
		app.setMaterial(new Material(color, black, color, white, 120.0f));
		return createCylinder(p1, p2, radius, app, 12);
	}

	public static BranchGroup createArrow(Point3d b, Point3d a, double radius, Appearance cylApp, int precision) {

		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		TransformGroup tg = createMatrix(center, unit);

		Cone cne = new Cone((float) radius*3, (float) radius*10, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		Cylinder cyl = new Cylinder((float) radius, height, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);

		Transform3D tcne = new Transform3D();
		tcne.set(new Vector3f(0, (float) (height/2-radius), 0));
		TransformGroup tgcne = new TransformGroup(tcne);
		tgcne.addChild(cne);
		tg.addChild(tgcne);

		tg.addChild(cyl);
		BranchGroup cylBg = new BranchGroup();
		cylBg.addChild(tg);
		return cylBg;
	  }


	public static BranchGroup createCylinder(Point3d b, Point3d a, double radius, Appearance cylApp, int precision) {
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		TransformGroup tg = createMatrix(center, unit);
		tg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);

		Cylinder cyl = new Cylinder((float) radius, height, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		cyl.setCapability(Cylinder.ENABLE_APPEARANCE_MODIFY);
		cyl.getShape(Cylinder.BODY).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.TOP).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.BOTTOM).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		
		tg.addChild(cyl);
		BranchGroup cylBg = new BranchGroup();
		cylBg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		cylBg.setCapability(BranchGroup.ALLOW_DETACH);
		cylBg.addChild(tg);
		return cylBg;
	  }



	private static float calculateHeight(Point3d b, Point3d a, Vector3f center, Vector3f unit) {
		Vector3f base = new Vector3f(b);
		Vector3f apex = new Vector3f(a);

		// calculate center of object
		center.x = (apex.x - base.x) / 2 + base.x;
		center.y = (apex.y - base.y) / 2 + base.y;
		center.z = (apex.z - base.z) / 2 + base.z;

		// calculate height of object and unit vector along cylinder axis
		unit.sub(apex, base);  // unit = apex - base;
		float height = unit.length();
		unit.normalize();
		return height;
	}

	private static TransformGroup createMatrix(Vector3f center, Vector3f unit) {
		/* A Java3D cylinder is created lying on the Y axis by default.
		   The idea here is to take the desired cylinder's orientation
		   and perform a tranformation on it to get it ONTO the Y axis.
		   Then this transformation matrix is inverted and used on a
		   newly-instantiated Java 3D cylinder. */

		// calculate vectors for rotation matrix
		// rotate object in any orientation, onto Y axis (exception handled below)
		// (see page 418 of Computer Graphics by Hearn and Baker)
		Vector3f uX = new Vector3f();
		Vector3f uY = new Vector3f();
		Vector3f uZ = new Vector3f();
		float magX;
		Transform3D rotateFix = new Transform3D();

		uY = new Vector3f(unit);
		uX.cross(unit, new Vector3f(0, 0, 1));
		magX = uX.length();
		// magX == 0 if object's axis is parallel to Z axis
		if (magX != 0) {
		  uX.z = uX.z / magX;
		  uX.x = uX.x / magX;
		  uX.y = uX.y / magX;
		  uZ.cross(uX, uY);
		}
		else {
		  // formula doesn't work if object's axis is parallel to Z axis
		  // so rotate object onto X axis first, then back to Y at end
		  float magZ;
		  // (switched z -> y,  y -> x, x -> z from code above)
		  uX = new Vector3f(unit);
		  uZ.cross(unit, new Vector3f(0, 1, 0));
		  magZ = uZ.length();
		  uZ.x = uZ.x / magZ;
		  uZ.y = uZ.y / magZ;
		  uZ.z = uZ.z / magZ;
		  uY.cross(uZ, uX);
		  // rotate object 90 degrees CCW around Z axis--from X onto Y
		  rotateFix.rotZ(-Math.PI / 2.0);
		}

		// create the rotation matrix
		Transform3D transMatrix = new Transform3D();
		Transform3D rotateMatrix =
		new Transform3D(new Matrix4f(uX.x, uX.y, uX.z, 0,
						 uY.x, uY.y, uY.z, 0,
						 uZ.x, uZ.y, uZ.z, 0,
						 0,  0,  0,  1));
		// invert the matrix; need to rotate it off of the Z axis
		rotateMatrix.invert();
		// rotate the cylinder into correct orientation
		transMatrix.mul(rotateMatrix);
		transMatrix.mul(rotateFix);
		// translate the cylinder away
		transMatrix.setTranslation(center);
		// create the transform group
		TransformGroup tg = new TransformGroup(transMatrix);
		
		return tg;
	}
}
