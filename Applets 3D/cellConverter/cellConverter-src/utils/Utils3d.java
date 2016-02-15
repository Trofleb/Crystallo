package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

public class Utils3d {

	public static TransformGroup putAtom(Group parent, float radius, Point3d p, Appearance app, double scale) {
		Sphere s = new Sphere(1, Primitive.GENERATE_NORMALS, 20, app);
		Transform3D t3d = new Transform3D();
		t3d.set(radius, new Vector3d(p));
		TransformGroup t = new TransformGroup(t3d);
		t3d.set(scale);
		TransformGroup t2 = new TransformGroup(t3d);
		t2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		t2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		t2.addChild(s);
		t.addChild(t2);
		parent.addChild(t);
		return t2;

		// BranchGroup bg = new BranchGroup();
		// bg.setCapability(BranchGroup.ALLOW_DETACH);
		// bg.addChild(t);
		// parent.addChild(bg);
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

	public static String posToString(float[] p) {
		return posToString(new Point3d(p[0], p[1], p[2]));
	}

	public static Point3d round(Point3d p) {
		p.x = round(p.x);
		p.y = round(p.y);
		p.z = round(p.z);
		return p;
	}

	public static Vector3d round(Vector3d p) {
		p.x = round(p.x);
		p.y = round(p.y);
		p.z = round(p.z);
		return p;
	}

	public static BranchGroup createPlan(Point3d p, Vector3d e1, Vector3d e2, Vector3d e3, double w, double h,
			Appearance app) {
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3d(p));
		TransformGroup transTg = new TransformGroup(t3d);
		transTg.addChild(new Shape3D(createQuad(e1, e2, e3, w, h), app));
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(transTg);
		return bg;
	}

	private static QuadArray createQuad(Vector3d e1, Vector3d e2, Vector3d e3, double w, double h) {
		Matrix3d m = new Matrix3d();
		Vector3d f1 = new Vector3d(e1);
		f1.normalize();
		Vector3d f2 = new Vector3d(e2);
		f2.normalize();
		Vector3d f3 = new Vector3d(e3);
		f3.normalize();
		m.setColumn(0, f1);
		m.setColumn(1, f2);
		m.setColumn(2, f3);

		Point3d p1 = new Point3d(-w / 2, 0, -h / 2);
		Point3d p2 = new Point3d(-w / 2, 0, +h / 2);
		Point3d p3 = new Point3d(+w / 2, 0, +h / 2);
		Point3d p4 = new Point3d(+w / 2, 0, -h / 2);
		m.transform(p1);
		m.transform(p2);
		m.transform(p3);
		m.transform(p4);

		QuadArray quad = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.NORMALS);
		quad.setCoordinate(0, p1);
		quad.setCoordinate(1, p2);
		quad.setCoordinate(2, p3);
		quad.setCoordinate(3, p4);

		Vector3f e2f = new Vector3f(f2);
		quad.setNormal(0, e2f);
		quad.setNormal(1, e2f);
		quad.setNormal(2, e2f);
		quad.setNormal(3, e2f);
		return quad;
	}

	public static BranchGroup createAtom(Point3d p, float radius, Appearance app, int precision) {
		Sphere s = new Sphere(1, Primitive.GENERATE_NORMALS, precision, app);
		Transform3D t3d = new Transform3D();
		t3d.set(radius, new Vector3d(p));
		TransformGroup t = new TransformGroup(t3d);
		t.addChild(s);
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.addChild(t);
		return bg;
	}

	public static void changeCylinder(BranchGroup cyl, Point3d b, Point3d a) {
		TransformGroup tg = ((TransformGroup) cyl.getChild(0));
		TransformGroup tgh = (TransformGroup) ((TransformGroup) cyl.getChild(0)).getChild(0);
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		createMatrix(tg, center, unit);
		Transform3D th = new Transform3D();
		th.set(new Matrix3d(1, 0, 0, 0, height, 0, 0, 0, 1));
		tgh.setTransform(th);
	}

	public static void changeCylinderApp(BranchGroup cyl, Appearance app) {
		Cylinder c = ((Cylinder) ((TransformGroup) ((TransformGroup) cyl.getChild(0)).getChild(0)).getChild(0));
		c.setAppearance(app);
	}

	public static BranchGroup createCylinder(Point3d b, Point3d a, double radius, Appearance cylApp, int precision) {
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit);
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(Group.ALLOW_CHILDREN_READ);
		createMatrix(tg, center, unit);
		Transform3D th = new Transform3D();
		th.set(new Matrix3d(1, 0, 0, 0, height, 0, 0, 0, 1));
		TransformGroup tgh = new TransformGroup(th);
		tgh.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgh.setCapability(Group.ALLOW_CHILDREN_READ);

		Cylinder cyl = new Cylinder((float) radius, 1f, Primitive.GENERATE_NORMALS, precision, 1, cylApp);
		cyl.setCapability(Primitive.ENABLE_APPEARANCE_MODIFY);
		cyl.getShape(Cylinder.BODY).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.TOP).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		cyl.getShape(Cylinder.BOTTOM).setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		BranchGroup bbg = new BranchGroup();
		bbg.setCapability(Group.ALLOW_CHILDREN_WRITE);
		bbg.setCapability(Group.ALLOW_CHILDREN_READ);
		bbg.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		bbg.setCapability(BranchGroup.ALLOW_DETACH);
		bbg.addChild(cyl);

		tgh.addChild(bbg);
		tg.addChild(tgh);
		BranchGroup cylBg = new BranchGroup();
		cylBg.setCapability(Group.ALLOW_CHILDREN_READ);
		cylBg.setCapability(Group.ALLOW_CHILDREN_WRITE);
		cylBg.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		cylBg.setCapability(BranchGroup.ALLOW_DETACH);
		cylBg.addChild(tg);
		return cylBg;
	}

	public static BranchGroup createArrow(Point3d b, Point3d a, double radius, double radiusArrow, double lenArrow,
			Appearance cylApp, int precision) {
		Vector3f center = new Vector3f();
		Vector3f unit = new Vector3f();
		float height = calculateHeight(b, a, center, unit) - (float) lenArrow;
		Vector3d h = new Vector3d();
		h.sub(a, new Vector3d(center));
		h.normalize();
		h.scale(lenArrow / 2);
		center.sub(center, new Vector3f(h));
		TransformGroup tg = new TransformGroup();
		createMatrix(tg, center, unit);

		// Cone cne = new Cone((float) radius*3, (float) radius*10,
		// Cylinder.GENERATE_NORMALS, precision, 1, cylApp);
		// Cylinder cyl = new Cylinder((float) radius, height,
		// Cylinder.GENERATE_NORMALS, precision, 1, cylApp);

		Cone cne = new Cone((float) radiusArrow, (float) lenArrow, Primitive.GENERATE_NORMALS, precision, 1, cylApp);
		Cylinder cyl = new Cylinder((float) radius, height, Primitive.GENERATE_NORMALS, precision, 1, cylApp);

		Transform3D tcne = new Transform3D();
		tcne.set(new Vector3f(0, (float) (height / 2f + lenArrow / 2f), 0));
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
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 8), new FontExtrusion());
		// Text3D(Font3D font3D, java.lang.String string, Point3f position, int
		// alignment, int path)
		Text3D txt = new Text3D(f3d, s, new Point3f(2, 2, 2), centered ? Text3D.ALIGN_CENTER : Text3D.ALIGN_FIRST,
				Text3D.PATH_RIGHT);
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

	public static Group createFixedLegend(String s, Point3d pos, float size, Appearance app, boolean centered) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		Font3D f3d = new Font3D(new Font(null, Font.PLAIN, 2), new FontExtrusion());
		Text3D txt = new Text3D(f3d, s, new Point3f(0, 0, 0), centered ? Text3D.ALIGN_CENTER : Text3D.ALIGN_FIRST,
				Text3D.PATH_RIGHT);
		Shape3D textShape = new Shape3D();
		textShape.setGeometry(txt);
		textShape.setAppearance(app);

		Transform3D tt3d = new Transform3D();
		tt3d.rotX(Math.PI / 2);
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
		return (Vector3d) mul((Tuple3d) t, scale);
	}

	public static Point3d mul(Point3d t, double scale) {
		return (Point3d) mul((Tuple3d) t, scale);
	}

	public static Vector3d norm(Vector3d t) {
		Vector3d r = new Vector3d(t);
		r.normalize();
		return r;
	}

	public static BranchGroup createRepere(Color3f colorText, Color3f colorArrows, Color3f colorCenter, String[] names,
			float sizeText, float sizeArrows, double deltaText, double deltaArrows, Vector3d x, Vector3d y,
			Vector3d z) {
		Appearance app1 = createApp(colorText);
		Appearance app2 = createApp(colorArrows);
		BranchGroup repere = new BranchGroup();

		if (colorCenter != null)
			repere.addChild(new Sphere(sizeArrows, Primitive.GENERATE_NORMALS, 10, createApp(colorCenter)));

		Point3d o = new Point3d(0, 0, 0);
		repere.addChild(createArrow(o, new Point3d(mul(x, (x.length() + deltaArrows) / x.length())), sizeArrows,
				sizeArrows * 2f, sizeArrows * 6f, app2, 12));
		repere.addChild(createArrow(o, new Point3d(mul(y, (y.length() + deltaArrows) / y.length())), sizeArrows,
				sizeArrows * 2f, sizeArrows * 6f, app2, 12));
		repere.addChild(createArrow(o, new Point3d(mul(z, (z.length() + deltaArrows) / z.length())), sizeArrows,
				sizeArrows * 2f, sizeArrows * 6f, app2, 12));

		repere.addChild(createLegend(names[0], new Point3d(mul(x, (x.length() + deltaText) / x.length())), o, sizeText,
				app1, false));
		repere.addChild(createLegend(names[1], new Point3d(mul(y, (y.length() + deltaText) / y.length())), o, sizeText,
				app1, false));
		repere.addChild(createLegend(names[2], new Point3d(mul(z, (z.length() + deltaText) / z.length())), o, sizeText,
				app1, false));
		return repere;
	}

	public static BranchGroup createVector(String name, Point3d p, Vector3d v, float size, Appearance app,
			int precision) {
		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		Point3d p2 = new Point3d(v);
		p2.add(p);
		bg.addChild(Utils3d.createArrow(p, p2, size, 2 * size, 5 * size, app, precision));
		bg.addChild(Utils3d.createLegend(name, p2, p2, size, app, true));
		return bg;
	}

	public static BranchGroup createNamedVector(String name, Point3d p1, Point3d p2, Point3d p3, float size,
			Color3f colorText, Color3f colorArrow) {
		Appearance app1 = createApp(colorText);
		Appearance app2 = createApp(colorArrow);
		BranchGroup r = new BranchGroup();
		r.addChild(createArrow(p1, p2, .03 * size, .1 * size, .4 * size, app2, 12));
		r.addChild(createFixedLegend(name, p3, .15f * size, app1, true));
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

	public static Appearance createAppOpaque(Color c, float transp) {
		Appearance app = new Appearance();

		BufferedImage i = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, 64, 64);
		ImageComponent2D image = new ImageComponent2D(ImageComponent.FORMAT_RGBA, i, false, false);

		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);

		app.setTexture(texture);
		app.setTextureAttributes(new TextureAttributes());

		app.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, transp));

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		app.setPolygonAttributes(pa);

		return app;
	}

	private static float calculateHeight(Point3d b, Point3d a, Vector3f center, Vector3f unit) {
		Vector3f base = new Vector3f(b);
		Vector3f apex = new Vector3f(a);

		// calculate center of object
		center.x = (apex.x - base.x) / 2 + base.x;
		center.y = (apex.y - base.y) / 2 + base.y;
		center.z = (apex.z - base.z) / 2 + base.z;

		// calculate height of object and unit vector along cylinder axis
		unit.sub(apex, base); // unit = apex - base;
		float height = unit.length();
		unit.normalize();
		return height;
	}

	private static void createMatrix(TransformGroup tgOut, Vector3f center, Vector3f unit) {
		/*
		 * A Java3D cylinder is created lying on the Y axis by default. The idea
		 * here is to take the desired cylinder's orientation and perform a
		 * tranformation on it to get it ONTO the Y axis. Then this
		 * transformation matrix is inverted and used on a newly-instantiated
		 * Java 3D cylinder.
		 */

		// calculate vectors for rotation matrix
		// rotate object in any orientation, onto Y axis (exception handled
		// below)
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
		} else {
			// formula doesn't work if object's axis is parallel to Z axis
			// so rotate object onto X axis first, then back to Y at end
			float magZ;
			// (switched z -> y, y -> x, x -> z from code above)
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
		Transform3D rotateMatrix = new Transform3D(
				new Matrix4f(uX.x, uX.y, uX.z, 0, uY.x, uY.y, uY.z, 0, uZ.x, uZ.y, uZ.z, 0, 0, 0, 0, 1));
		// invert the matrix; need to rotate it off of the Z axis
		rotateMatrix.invert();
		// rotate the cylinder into correct orientation
		transMatrix.mul(rotateMatrix);
		transMatrix.mul(rotateFix);
		// translate the cylinder away
		transMatrix.setTranslation(center);
		// create the transform group

		tgOut.setTransform(transMatrix);

		// TransformGroup tg = new TransformGroup(transMatrix);
		// return tg;
	}

	public static void dumpNode(Group g, String prefix) {
		for (int i = 0; i < g.numChildren(); i++)
			try {
				Node n = g.getChild(i);
				System.out.println(prefix + n);
				if (n instanceof Group)
					dumpNode((Group) n, prefix + "  ");
			} catch (CapabilityNotSetException e) {
				System.out.println(prefix + "??");
			}
	}

	public static Object pickElementOfType(Group g, Class type, int no) {
		int k = no;
		for (int i = 0; i < g.numChildren(); i++) {
			Node n = g.getChild(i);
			if (type.isInstance(n))
				if (k == 0)
					return n;
				else
					k--;
			if (n instanceof Group) {
				Object r = pickElementOfType((Group) n, type, no);
				if (r != null)
					return r;
			}
		}
		return null;
	}
}
