package objects;

import javax.vecmath.Point3d;

import transformations.Rotation;
import utils.ColorConstants;
import utils.HVPanel;

public class Dodecaedre extends Polyedre {

	private static final double t = (Math.sqrt(5.0)+1.0)/2.0;
	private static final double u = (Math.sqrt(5.0)-1.0)/2.0;
	private static final Point3d[] points = new Point3d[] {
			new Point3d(-1, -1, -1),	
			new Point3d(-1, -1, +1),	
			new Point3d(-1, +1, +1),	
			new Point3d(-1, +1, -1),	
			new Point3d(+1, -1, -1),	
			new Point3d(+1, -1, +1),	
			new Point3d(+1, +1, +1),	
			new Point3d(+1, +1, -1),	
			new Point3d( u,  0,  t),	
			new Point3d(-u,  0,  t),	
			new Point3d( u,  0, -t),	
			new Point3d(-u,  0, -t),	
			new Point3d(t,  u,  0),	
			new Point3d(t, -u,  0),	
			new Point3d(-t,  u,  0),	
			new Point3d(-t, -u,  0),	
			new Point3d(0,  t,  u),	
			new Point3d(0,  t, -u),	
			new Point3d(0, -t,  u),	
			new Point3d(0, -t, -u),	
	};
	private static final int[][] faces = new int[][] {
			{14, 2, 9, 1, 15}, 
			{2, 16, 6, 8, 9}, 
			{9, 8, 5, 18, 1}, 
			{6, 12, 13, 5, 8}, 
			{16, 17, 7, 12, 6}, 
			{2, 14, 3, 17, 16}, 
			{14, 15, 0, 11, 3}, 
			{1, 18, 19, 0, 15}, 
			{18, 5, 13, 4, 19}, 
			{0, 19, 4, 10, 11}, 
			{4, 13, 12, 7, 10}, 
			{11, 10, 7, 17, 3}, 
	};
	
	public Dodecaedre() {
		super(points, faces);
	}
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(1.8,  0,  0), new Point3d(0, 0, 0), 4, ColorConstants.green));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("3-fold axis rotations"));
		addTransform(new Rotation("31", "1", rotPanel2, this, new Point3d(1, 1, 1), new Point3d(0, 0, 0), 5, ColorConstants.cyan));

		HVPanel rotPanel3 = addPanel(new HVPanel.v("5-fold axis rotations"));
		addTransform(new Rotation("51", "1", rotPanel3, this, new Point3d(1, t,  0), new Point3d(0, 0, 0), 4, ColorConstants.yellow));
	}
}
