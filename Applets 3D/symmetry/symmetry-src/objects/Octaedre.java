package objects;

import javax.vecmath.Point3d;

import transformations.Rotation;
import utils.ColorConstants;
import utils.HVPanel;

public class Octaedre extends Polyedre {

	private static final Point3d[] points = new Point3d[] {
			new Point3d( 0,  0, -1.8),	
			new Point3d( 0,  0,  1.8),	
			new Point3d( 0, -1.8,  0),	
			new Point3d( 0,  1.8,  0),	
			new Point3d(-1.8,  0,  0),	
			new Point3d( 1.8,  0,  0),	
	};
	private static final int[][] faces = new int[][] {
			{1, 2, 5},
			{1, 3, 5},
			{1, 3, 4},
			{1, 2, 4},
			{0, 2, 5},
			{0, 3, 5},
			{0, 3, 4},
			{0, 2, 4},
	};
	
	public Octaedre() {
		super(points, faces);
	}
	
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(1, 1, 0), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("22", "2", rotPanel1, this, new Point3d(-1, 1, 0), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("23", "3", rotPanel1, this, new Point3d(0, 1, 1), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("24", "4", rotPanel1, this, new Point3d(0, -1, 1), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("25", "5", rotPanel1, this, new Point3d(1, 0, 1), new Point3d(0, 0, 0), 4, ColorConstants.green));
		addTransform(new Rotation("26", "6", rotPanel1, this, new Point3d(-1, 0, 1), new Point3d(0, 0, 0), 4, ColorConstants.green));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("3-fold axis rotations"));
		addTransform(new Rotation("31", "1", rotPanel2, this, new Point3d(1, 1, 1), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("32", "2", rotPanel2, this, new Point3d(1, -1, 1), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("33", "3", rotPanel2, this, new Point3d(-1, -1, 1), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("34", "4", rotPanel2, this, new Point3d(-1, 1, 1), new Point3d(0, 0, 0), 4, ColorConstants.cyan));

		HVPanel rotPanel3 = addPanel(new HVPanel.v("4-fold axis rotations"));
		addTransform(new Rotation("41", "1", rotPanel3, this, new Point3d(1.75,  0,  0), new Point3d(0, 0, 0), 4, ColorConstants.yellow));
		addTransform(new Rotation("42", "2", rotPanel3, this, new Point3d(0,  1.75,  0), new Point3d(0, 0, 0), 4, ColorConstants.yellow));
		addTransform(new Rotation("43", "3", rotPanel3, this, new Point3d(0,  0,  1.75), new Point3d(0, 0, 0), 4, ColorConstants.yellow));
	}
}
