package u3d;

import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.TriangleStripArray;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

import utils.ColorConstants;
import utils.Utils3d;

public class TranspObject {
	private static final float transparency = 0.7f; 
	public  BranchGroup root;
	private Group parent;
	private Material material;
	private Point3d[] points;
	
	public TranspObject(Point3d[] points, int[][] faces, int[][] edges, boolean showFaces, boolean showFaceEdges) {
		this.points = points;
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_DETACH);
		//material = new Material(ColorConstants.white, ColorConstants.black, ColorConstants.white, ColorConstants.white, 128);
		material = new Material(ColorConstants.white, ColorConstants.black, ColorConstants.white, ColorConstants.white, 128);
		material.setCapability(Material.ALLOW_COMPONENT_WRITE);
		Appearance app = Utils3d.createApp(material, transparency);
		if (showFaces) root.addChild(createFaces(points, faces, app));
		if (showFaceEdges) root.addChild(createFaceEdges(points, faces, app));
		if (edges!=null) root.addChild(createEdges(points, edges, app));
	}
	public void attach(Group parent) {
		if (parent!=null) detach();
		this.parent = parent;
		parent.addChild(root);
	}
	public void detach() {
		if (parent==null) return;
		parent.removeChild(root);
		parent = null;
	}
	public void setColor(Color3f color) {
		material.setAmbientColor(color);
		material.setDiffuseColor(color);
		//material.setSpecularColor(color);
		//material.setEmissiveColor(color);
	}
	public boolean isTransformIdentity(Transform3D t3d) {
		for (int i=0; i<points.length; i++) {
			Point3d p = new Point3d(points[i]);
			t3d.transform(p);
			boolean found = false;
			for (int j=0; j<points.length; j++) {
				if (p.epsilonEquals(points[j], .0001)) {
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return true;
	}


	
	public static Shape3D createFaces(Point3d[] points, int[][] faces, Appearance app) {
		GeometryArray ga;
		int f = faces[0].length;
		if (f==4)
			ga = new QuadArray(f*faces.length, QuadArray.COORDINATES|QuadArray.NORMALS);
		else if (f==3)
			ga = new TriangleArray(f*faces.length, QuadArray.COORDINATES|QuadArray.NORMALS);
		else if (f>4) {
			int[][] hh = new int[faces.length*(f-2)][3];
			for (int i=0; i<faces.length; i++) {
				for (int j=0; j<f-2; j++) {
					hh[i*(f-2)+j][0] = faces[i][0];
					hh[i*(f-2)+j][1] = faces[i][j+1];
					hh[i*(f-2)+j][2] = faces[i][j+2];
				}			
			}
			faces = hh;
			f = 3;
			ga = new TriangleArray(f*faces.length, QuadArray.COORDINATES|QuadArray.NORMALS);
		}
		else throw new RuntimeException("Invalid vertex/face");
		
		Vector3d v1 = new Vector3d();
		Vector3d v2 = new Vector3d();
		Vector3d n = new Vector3d();
		
		for (int i=0; i<faces.length; i++) {
			v1.sub(points[faces[i][1]], points[faces[i][0]]);
			v2.sub(points[faces[i][2]], points[faces[i][0]]);
			n.cross(v1, v2);
			n.normalize();
			for (int j=0; j<f; j++) {
				ga.setNormal(i*f+j, new Vector3f(n));
				ga.setCoordinate(i*f+j, points[faces[i][j]]);
			}
		}
		return new Shape3D(ga, app); 
	}
	
	public static BranchGroup createFaceEdges(Point3d[] points, int[][] faces, Appearance app) {
		return createFaceEdges(points, faces, app, .005);
	}
	public static BranchGroup createFaceEdges(Point3d[] points, int[][] faces, Appearance app, double radius) {
		BranchGroup ed = new BranchGroup();
		Vector v = new Vector(10, 10);
		int f = faces[0].length;
		for (int i=0; i<faces.length; i++) {
			for (int j=0; j<f; j++) {
				int f1 = faces[i][j], f2 = faces[i][(j+1)%f];
				if (!v.contains(f1+" "+f2)) {
					ed.addChild(Utils3d.createCylinder(points[f1], points[f2], radius, app, 10));
					v.add(f1+" "+f2);
					v.add(f2+" "+f1);
				}
			}
		}
		return ed;
	}
	public static BranchGroup createEdges(Point3d[] points, int[][] edges, Appearance app) {
		return createEdges(points, edges, app, .005);
	}
	public static BranchGroup createEdges(Point3d[] points, int[][] edges, Appearance app, double radius) {
		BranchGroup ed = new BranchGroup();
		for (int i=0; i<edges.length; i++) {
			ed.addChild(Utils3d.createCylinder(points[edges[i][0]], points[edges[i][1]], radius, app, 10));
		}
		return ed;
	}
}
