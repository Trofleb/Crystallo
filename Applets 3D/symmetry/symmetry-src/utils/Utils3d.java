package utils;
import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

public class Utils3d {

	
	public static void changeCylinder(BranchGroup cyl, Point3d b, Point3d a) {
		TransformGroup tg = ((TransformGroup)cyl.getChild(0));
		TransformGroup tgh = (TransformGroup)((TransformGroup)cyl.getChild(0)).getChild(0);
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		createMatrix(tg, center, unit);
		Transform3D th = new Transform3D();
		th.set(new Matrix3d(1, 0, 0, 0, height, 0, 0, 0, 1));
		tgh.setTransform(th);
	}
	
	public static void changeCylinderApp(BranchGroup cyl, Appearance app) {
		Cylinder c = ((Cylinder)((TransformGroup)((TransformGroup)cyl.getChild(0)).getChild(0)).getChild(0));
		c.setAppearance(app);
	}
	
	public static BranchGroup createCylinder(Point3d b, Point3d a, double radius, Appearance cylApp, int precision) {
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		TransformGroup tg = new TransformGroup();  
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		createMatrix(tg, center, unit);
		Transform3D th = new Transform3D();
		th.set(new Matrix3d(1, 0, 0, 0, height, 0, 0, 0, 1));
		TransformGroup tgh = new TransformGroup(th);  
		tgh.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgh.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		tgh.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tgh.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
		Cylinder cyl = new Cylinder((float) radius, 1f, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		cyl.setCapability(Cylinder.ENABLE_APPEARANCE_MODIFY);
		cyl.getShape(Cylinder.BODY).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.TOP).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.BOTTOM).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		
		BranchGroup bbg = new BranchGroup();
		bbg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bbg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bbg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bbg.setCapability(BranchGroup.ALLOW_DETACH);
		bbg.addChild(cyl);
		
		tgh.addChild(bbg);
		tg.addChild(tgh);
		BranchGroup cylBg = new BranchGroup();
		cylBg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		cylBg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		cylBg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		cylBg.setCapability(BranchGroup.ALLOW_DETACH);
		cylBg.addChild(tg);
		return cylBg;
	  }
	
	public static BranchGroup createArrow(Point3d b, Point3d a, double radius, double radiusArrow, double lenArrow, Appearance cylApp, int precision) {
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit)-(float)lenArrow;
		Vector3d h = new Vector3d();
		h.sub(a, new Vector3d(center));
		h.normalize();
		h.scale(lenArrow/2);
		center.sub(center, new Vector3f(h));
		TransformGroup tg = new TransformGroup();  
		createMatrix(tg, center, unit);

		//Cone cne = new Cone((float) radius*3, (float) radius*10, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		//Cylinder cyl = new Cylinder((float) radius, height, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		
		Cone cne = new Cone((float)radiusArrow, (float)lenArrow, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		Cylinder cyl = new Cylinder((float)radius, height, Cylinder.GENERATE_NORMALS, precision, 1, cylApp);

		Transform3D tcne = new Transform3D();
		tcne.set(new Vector3f(0, (float)(height/2f+lenArrow/2f), 0));
		TransformGroup tgcne = new TransformGroup(tcne);
		tgcne.addChild(cne);
		tg.addChild(tgcne);

		tg.addChild(cyl);
		BranchGroup cylBg = new BranchGroup();
		cylBg.addChild(tg);
		return cylBg;
	  }

	
	public static Group createLegend(String s, Point3d pos, Point3d rot, float size, Appearance app, boolean centered) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 2), new FontExtrusion());
		Text3D txt = new Text3D(f3d, s, new Point3f(0, 0, 0), centered?Text3D.ALIGN_CENTER:Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT); 
		OrientedShape3D textShape = new OrientedShape3D();
		textShape.setGeometry(txt);
		textShape.setAppearance(app);
	
		textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		textShape.setRotationPoint(new Point3f(rot));
	
		Transform3D tt3d = new Transform3D();
		tt3d.set(size, new Vector3d(pos));
		TransformGroup tt = new TransformGroup(tt3d);
		
		tt.addChild(textShape);
		bg.addChild(tt);
		return bg;
	}

	public static Group createLegendEx(String s, Point3d pos, Point3d rot, float size, Appearance app) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		OrientedShape3D textShape = new OrientedShape3D();
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 20), new FontExtrusion());

		if (s.charAt(0)=='-') {
			s=s.substring(1);
			Text3D txt1 = new Text3D(f3d, "\u2013", new Point3f(0, 11, 0), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT); 
			textShape.addGeometry(txt1);
		}
		if (s.length()>1) {
			Font3D f3d2 = new Font3D(new Font(null, Font.PLAIN, 12), new FontExtrusion());
			Text3D txt2 = new Text3D(f3d2, s.substring(1), new Point3f(0, -3, 0), Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT); 
			textShape.addGeometry(txt2);
		}
		Text3D txt3 = new Text3D(f3d, s.substring(0, 1), new Point3f(0, 0, 0), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT); 
		textShape.addGeometry(txt3);
		textShape.setAppearance(app);
	
		textShape.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);
		textShape.setRotationPoint(new Point3f(rot));
	
		Transform3D tt3d = new Transform3D();
		tt3d.set(size, new Vector3d(pos));
		TransformGroup tt = new TransformGroup(tt3d);
		
		tt.addChild(textShape);
		bg.addChild(tt);
		return bg;
	}
	
	public static Group createFixedLegend(String s, Point3d pos, float size, Appearance app, boolean centered) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 2), new FontExtrusion());
		Text3D txt = new Text3D(f3d, s, new Point3f(0, 0, 0), centered?Text3D.ALIGN_CENTER:Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT); 
		Shape3D textShape = new Shape3D();
		textShape.setGeometry(txt);
		textShape.setAppearance(app);
	
		Transform3D tt3d = new Transform3D();
		tt3d.rotX(Math.PI/2);
		TransformGroup t = new TransformGroup(tt3d);
	
		tt3d = new Transform3D();
		tt3d.set(new Vector3d(pos.x, pos.y, pos.z));
		TransformGroup tt = new TransformGroup(tt3d);

		tt3d = new Transform3D();
		tt3d.set(size);
		TransformGroup ttt = new TransformGroup(tt3d);
		
		
		t.addChild(textShape);
		ttt.addChild(t);
		tt.addChild(ttt);
		bg.addChild(tt);
		return bg;
	}

	public static Tuple3d mul(Tuple3d t, double scale) {
		Tuple3d r = (Tuple3d) t.clone();
		r.scale(scale);
		return r;
	}
	public static Vector3d mul(Vector3d t, double scale) {
		return (Vector3d)mul((Tuple3d)t, scale);
	}
	public static Point3d mul(Point3d t, double scale) {
		return (Point3d)mul((Tuple3d)t, scale);
	}
	
	public static Vector3d norm(Vector3d t) {
		Vector3d r = new Vector3d(t);
		r.normalize();
		return r;
	}

	public static String posToString(double p) {
		return ""+Math.round(p*100f)/100f;
	}
	
	public static String posToString(Tuple3d p) {
		return "("+posToString(p.x)+" "+posToString(p.y)+" "+posToString(p.z)+")";
	}
	public static String posToString(float[] p) {
		return posToString(new Point3d(p[0], p[1], p[2]));
	}
	
	public static BranchGroup createRepere(Color3f colorText, Color3f colorArrows, Color3f colorCenter, String[] names, float sizeText, float sizeArrows, double deltaText, double deltaArrows, Vector3d x, Vector3d y, Vector3d z) {
		Appearance app1 = createApp(colorText);
		Appearance app2 = createApp(colorArrows);
		BranchGroup repere = new BranchGroup();

		if (colorCenter!=null) repere.addChild(new Sphere(.05f, Sphere.GENERATE_NORMALS, 10, createApp(colorCenter)));
		
		Point3d o = new Point3d(0, 0, 0);
		repere.addChild(createArrow(o, new Point3d(mul(x, (x.length()+deltaArrows)/x.length())), sizeArrows, sizeArrows*2f, sizeArrows*6f, app2, 12));
		repere.addChild(createArrow(o, new Point3d(mul(y, (y.length()+deltaArrows)/y.length())), sizeArrows, sizeArrows*2f, sizeArrows*6f, app2, 12));
		repere.addChild(createArrow(o, new Point3d(mul(z, (z.length()+deltaArrows)/z.length())), sizeArrows, sizeArrows*2f, sizeArrows*6f, app2, 12));
		
		repere.addChild(createLegend(names[0], new Point3d(mul(x, (x.length()+deltaText)/x.length())), o, sizeText, app1, false));
		repere.addChild(createLegend(names[1], new Point3d(mul(y, (y.length()+deltaText)/y.length())), o, sizeText, app1, false));
		repere.addChild(createLegend(names[2], new Point3d(mul(z, (z.length()+deltaText)/z.length())), o, sizeText, app1, false));
		return repere;
	}

	public static BranchGroup createNamedVector(String name, Point3d p1, Point3d p2, Point3d p3, float size, Color3f colorText, Color3f colorArrow) {
		Appearance app1 = createApp(colorText);
		Appearance app2 = createApp(colorArrow);
		BranchGroup r = new BranchGroup();
		r.addChild(createArrow(p1, p2, .03*size, .1*size, .4*size, app2, 12));
		r.addChild(createFixedLegend(name, p3, .15f*size, app1, true));
		return r;
	}
	
	
	static final Color3f ambWhite = new Color3f(0.3f, 0.3f, 0.3f);
	static final Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);
	static final Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	public static Appearance createApp(Color3f color) {
		Appearance app = new Appearance();
		Material mat = new Material(color, black, color, specular, 128);
		mat.setLightingEnable(true);
		app.setMaterial(mat);
		return app;
	}

	public static Appearance createApp(Material mat, float transp) {
		Appearance app = new Appearance();
		app.setMaterial(mat);
		app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, transp));
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_NONE);
    pa.setBackFaceNormalFlip(false);
    app.setPolygonAttributes(pa);
    return app;
	}

	public static Appearance createApp(Color3f color, float transp) {
		Material mat = new Material(color, black, color, specular, 128);
		mat.setLightingEnable(true);
		return createApp(mat, transp);
	}	
	
	public static Shape3D createCircle(double radius, int resolution, Color4f c) {
    int length = resolution + 1;
    LineStripArray lsa = new LineStripArray(length, GeometryArray.COORDINATES|GeometryArray.COLOR_4, new int[] {length});
    // first and last points
    Point3d pt0 = new Point3d(radius, 0, 0);
    lsa.setCoordinate(0, pt0);
    if (c!=null) lsa.setColor(0, c);
    // computed points
    Point3f pt = new Point3f();
    for (int i = 1; i < 32; i++) {
        pt.x = (float)(radius * Math.cos(i * Math.PI / 16));
        pt.y = (float)(radius * Math.sin(i * Math.PI / 16));
        lsa.setCoordinate(i, pt);
        if (c!=null) lsa.setColor(i, c);
    }
    lsa.setCoordinate(32, pt0);
    if (c!=null) lsa.setColor(32, c);
    return new Shape3D(lsa);
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

	private static void createMatrix(TransformGroup tgOut, Vector3f center, Vector3f unit) {
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
		
		tgOut.setTransform(transMatrix);
		
		//TransformGroup tg = new TransformGroup(transMatrix);
		//return tg;
	}

	
	public static void dumpNode(Group g, String prefix) {
		for (int i=0; i<g.numChildren(); i++) {
			try {
				Node n = g.getChild(i);
				System.out.println(prefix+n);
				if (n instanceof Group)
				dumpNode((Group)n, prefix+"  ");
			} catch (CapabilityNotSetException e) {
				System.out.println(prefix+"??");
			}
		}
	}

	public static Object pickElementOfType(Group g, Class type, int no) {
		int k=no;
		for (int i=0; i<g.numChildren(); i++) {
			Node n = g.getChild(i);
			if (type.isInstance(n)) {
				if (k==0) return n;
				else k--;
			}
			if (n instanceof Group) {
				Object r = pickElementOfType((Group)n, type, no);
				if (r!=null) return r;
			}
		}
		return null;
	}
}
