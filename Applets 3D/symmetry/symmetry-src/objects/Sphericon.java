package objects;

import javax.vecmath.Point3d;

import transformations.Inversion;
import transformations.Rotation;
import utils.BarBorder;
import utils.ColorConstants;
import utils.HVPanel;

public class Sphericon extends Polyedre {
	static int n = 32;
	public static Point3d[] points = new Point3d[2*(n+3)];
	public static int[][] faces = new int[n*4][3];
	public static int[][] edges = new int[n*2+4][2];
	public static double dx=0;
	public static boolean showFaces=true, showEdges=false;
	
	static {
		init();
	}
	
	public static void init() {
		points[0] = new Point3d(0+dx, -1, 0);
		points[1] = new Point3d(0+dx,  1, 0);
		for (int i=0; i<n+1; i++) {
			double a = i*(Math.PI/n);
			double x = Math.sin(a); 
			double z = Math.cos(a); 
			points[2+i] = new Point3d(x+dx, 0, z);
		}
		for (int i=0; i<n; i++) {
			faces[i][0] = 0;
			faces[i][1] = i+2;
			faces[i][2] = i+3;
		}
		for (int i=0; i<n; i++) {
			faces[n+i][0] = 1;
			faces[n+i][1] = i+3;
			faces[n+i][2] = i+2;
		}
		
		points[n+3+0] = new Point3d(0-dx, 0, -1);
		points[n+3+1] = new Point3d(0-dx, 0,  1);
		for (int i=0; i<n+1; i++) {
			double a = i*(Math.PI/n);
			double x = -Math.sin(a); 
			double y = Math.cos(a); 
			points[n+3+2+i] = new Point3d(x-dx, y, 0);
		}
		for (int i=0; i<n; i++) {
			faces[2*n+i][0] = n+3+0;
			faces[2*n+i][1] = n+3+i+2;
			faces[2*n+i][2] = n+3+i+3;
		}
		for (int i=0; i<n; i++) {
			faces[3*n+i][0] = n+3+1;
			faces[3*n+i][1] = n+3+i+3;
			faces[3*n+i][2] = n+3+i+2;
		}
		
		edges[0][0]=0; edges[0][1]=n+3+0;
		edges[1][0]=1; edges[1][1]=n+3+0;
		edges[2][0]=0; edges[2][1]=n+3+1;
		edges[3][0]=1; edges[3][1]=n+3+1;
		for (int i=0; i<n; i++) {
			edges[i+4][0] = i+2;
			edges[i+4][1] = i+3;
			edges[n+i+4][0] = n+3+i+2;
			edges[n+i+4][1] = n+3+i+3;
		}
	}
	
	public Sphericon() {
		super(points, faces, edges, showFaces, showEdges);
	}
	
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v(new BarBorder("4-fold axis rotations")));
		addTransform(new Rotation("-4", " ", rotPanel1, this, new Point3d(-1, 0, 0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel2, this, new Point3d(0, .7, .7), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("22", "2", rotPanel2, this, new Point3d(0, .7, -.7), new Point3d(0, 0, 0), 4, ColorConstants.green));
	}
}
