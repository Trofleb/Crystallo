package objects;

import javax.media.j3d.Group;
import javax.vecmath.Point3d;

import transformations.*;
import u3d.TranspObject;
import utils.BarBorder;
import utils.ColorConstants;
import utils.HVPanel;

public class Tetraedre extends Polyedre {

	private static final Point3d[] points = new Point3d[] {
			new Point3d(-1, -1, -1),	
			new Point3d(-1, +1, +1),	
			new Point3d(+1, -1, +1),	
			new Point3d(+1, +1, -1),	
	};
	private static final int[][] faces = new int[][] {
			{0, 1, 2},
			{0, 1, 3},
			{1, 2, 3},
			{0, 2, 3},
	};
	
	public Tetraedre() {
		super(points, faces);
	}
	
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v(new BarBorder("4-fold axis rotations")));
		addTransform(new Rotation("-41", "1", rotPanel1, this, new Point3d(1.5, 0, 0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("-42", "2", rotPanel1, this, new Point3d(0, 1.5, 0), new Point3d(0, 0, 0), 4, ColorConstants.cyan));
		addTransform(new Rotation("-43", "3", rotPanel1, this, new Point3d(0, 0, 1.5), new Point3d(0, 0, 0), 4, ColorConstants.cyan));

		HVPanel rotPanel2 = addPanel(new HVPanel.v("3-fold axis rotations"));
		addTransform(new Rotation("31", "1", rotPanel2, this, new Point3d(0, 0, 0), new Point3d(1, 1, 1), 4, ColorConstants.green));
		addTransform(new Rotation("32", "2", rotPanel2, this, new Point3d(0, 0, 0), new Point3d(1, -1, -1), 4, ColorConstants.green));
		addTransform(new Rotation("33", "3", rotPanel2, this, new Point3d(0, 0, 0), new Point3d(-1, 1, -1), 4, ColorConstants.green));
		addTransform(new Rotation("34", "4", rotPanel2, this, new Point3d(0, 0, 0), new Point3d(-1, -1, 1), 4, ColorConstants.green));
	}
}
