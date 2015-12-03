package objects;

import javax.vecmath.Point3d;

import transformations.Inversion;
import transformations.Rotation;
import utils.BarBorder;
import utils.ColorConstants;
import utils.HVPanel;

public class Bipyramide extends Polyedre {
	
	private static final double t = Math.sqrt(3.0)/2.0; 
	public static final Point3d[] points = new Point3d[] {
			new Point3d( 0,   1,  0),	
			new Point3d(-t, -.5,  0),	
			new Point3d( t, -.5,  0),	
			new Point3d( 0,   0, -1.5),	
			new Point3d( 0,   0,  1.5),	
	};
	public static final int[][] faces = new int[][] {
			{0, 1, 4},
			{1, 2, 4},
			{0, 4, 2},
			{1, 3, 2},
			{2, 3, 0},
			{0, 3, 1},
	};
	
	public Bipyramide() {
		super(points, faces);
	}
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(0, 1, 0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("22", "2", rotPanel1, this, new Point3d(-t, -.5, 0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("23", "3", rotPanel1, this, new Point3d(t, -.5,  0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));

		HVPanel rotPanel2 = addPanel(new HVPanel.v(new BarBorder("6-fold axis rotations")));
		addTransform(new Rotation("-6", " ", rotPanel2, this, new Point3d(0,  0, 1.5), new Point3d(0, 0, 0), 4, ColorConstants.green));
	}
}
