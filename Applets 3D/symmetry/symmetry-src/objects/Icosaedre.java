package objects;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import transformations.Rotation;
import u3d.TranspObject;
import utils.ColorConstants;
import utils.HVPanel;

public class Icosaedre extends Polyedre {

	private static final double t = (Math.sqrt(5.0)+1.0)/2.0;
	private static final Point3d[] points = new Point3d[] {
			new Point3d( 1,  0,  t),	
			new Point3d(-1,  0,  t),	
			new Point3d( 1,  0, -t),	
			new Point3d(-1,  0, -t),	
			new Point3d(t,  1,  0),	
			new Point3d(t, -1,  0),	
			new Point3d(-t,  1,  0),	
			new Point3d(-t, -1,  0),	
			new Point3d(0,  t,  1),	
			new Point3d(0,  t, -1),	
			new Point3d(0, -t,  1),	
			new Point3d(0, -t, -1),	
	};
	private static final int[][] faces = new int[][] {
			{1, 10, 7},
			{1, 7, 6}, 
			{7, 10, 11}, 
			{10, 5, 11}, 
			{11, 5, 2}, 
			{7, 11, 3}, 
			{3, 11, 2}, 
			{6, 7, 3}, 
			{6, 3, 9}, 
			{9, 3, 2}, 
			{2, 5, 4}, 
			{9, 2, 4}, 
			{9, 4, 8}, 
			{6, 9, 8}, 
			{1, 6, 8}, 
			{10, 1, 0}, 
			{1, 8, 0}, 
			{0, 8, 4}, 
			{5, 10, 0}, 
			{5, 0, 4}, 
	};
	
	public Icosaedre() {
		super(points, faces);
	}
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(1.8, 0, 0), new Point3d(0, 0, 0), 5, ColorConstants.green));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("3-fold axis rotations"));
		addTransform(new Rotation("31", "1", rotPanel2, this, new Point3d(1, 1, 1), new Point3d(0, 0, 0), 5, ColorConstants.cyan));

		HVPanel rotPanel3 = addPanel(new HVPanel.v("5-fold axis rotations"));
		addTransform(new Rotation("51", "1", rotPanel3, this, new Point3d(1, 0, t), new Point3d(0, 0, 0), 5, ColorConstants.yellow));
	}
}
