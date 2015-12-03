package objects;

import javax.vecmath.Point3d;

import transformations.Rotation;
import utils.ColorConstants;
import utils.HVPanel;

public class Rhomboedre extends Polyedre {
	private static final double d = 1;
	public static final Point3d[] points = new Point3d[] {
			new Point3d(-d, -d, -d),	
			new Point3d( 0,  0, -d),	// bottom	
			new Point3d( 0,  0, +d),	// top
			new Point3d(-d,  0,  0),	// left
			new Point3d(+d,  0,  0),	// right
			new Point3d( 0, -d,  0),	// near
			new Point3d( 0, +d,  0),	// far
			new Point3d(+d, +d, +d),	
	};
	public static final int[][] faces = new int[][] {
			{0, 5, 2, 3},
			{0, 1, 4, 5},
			{5, 4, 7, 2},
			{1, 4, 7, 6},
			{3, 6, 7, 2},
			{0, 1, 6, 3},
	};
	
	public Rhomboedre() {
		super(points, faces);
	}
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(-.6, .6, 0), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("22", "2", rotPanel1, this, new Point3d(-.6, 0, .6), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("23", "3", rotPanel1, this, new Point3d(0, -.6, .6), new Point3d(0, 0, 0), 4, ColorConstants.green));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("3-fold axis rotations"));
		addTransform(new Rotation("3", " ", rotPanel2, this, new Point3d(1, 1, 1), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
	}
}
