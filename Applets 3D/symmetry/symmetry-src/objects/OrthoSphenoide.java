package objects;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import transformations.Rotation;
import u3d.TranspObject;
import utils.ColorConstants;
import utils.HVPanel;

public class OrthoSphenoide extends Polyedre {

	public static final Point3d[] points = new Point3d[] {
			new Point3d(-.75, -1, -2),	
			new Point3d(-.75, +1, +2),	
			new Point3d(+.75, -1, +2),	
			new Point3d(+.75, +1, -2),	
	};
	public static final int[][] faces = new int[][] {
			{0, 1, 2},
			{0, 1, 3},
			{1, 2, 3},
			{0, 2, 3},
	};
	
	public OrthoSphenoide() {
		super(points, faces);
	}
	
	protected void createRotPane() {
		HVPanel rotPanel = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel, this, new Point3d(0, 0, 2.2), new Point3d(0, 0, 0), 5, ColorConstants.green));
		addTransform(new Rotation("22", "2", rotPanel, this, new Point3d(0, 1.2, 0), new Point3d(0, 0, 0), 5, ColorConstants.green));
		addTransform(new Rotation("23", "3", rotPanel, this, new Point3d(1.2, 0, 0), new Point3d(0, 0, 0), 5, ColorConstants.green));
	}
}
