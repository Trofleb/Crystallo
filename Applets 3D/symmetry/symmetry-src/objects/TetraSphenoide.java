package objects;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import transformations.Rotation;
import u3d.TranspObject;
import utils.BarBorder;
import utils.ColorConstants;
import utils.HVPanel;

public class TetraSphenoide extends Polyedre {

	public static final Point3d[] points = new Point3d[] {
			new Point3d(-1, -1, -2),	
			new Point3d(-1, +1, +2),	
			new Point3d(+1, -1, +2),	
			new Point3d(+1, +1, -2),	
	};
	public static final int[][] faces = new int[][] {
			{0, 1, 2},
			{0, 1, 3},
			{1, 2, 3},
			{0, 2, 3},
	};
	
	public TetraSphenoide() {
		super(points, faces);
	}
	protected void createRotPane() {
		HVPanel rotPanel1 = addPanel(new HVPanel.v("2-fold axis rotations"));
		addTransform(new Rotation("21", "1", rotPanel1, this, new Point3d(0, 1.2, 0), new Point3d(0, 0, 0), 5, ColorConstants.green));
		addTransform(new Rotation("22", "2", rotPanel1, this, new Point3d(1.2, 0, 0), new Point3d(0, 0, 0), 5, ColorConstants.green));

		HVPanel rotPanel2 = addPanel(new HVPanel.v(new BarBorder("4-fold axis rotations")));
		addTransform(new Rotation("-4", " ", rotPanel2, this, new Point3d(0, 0, 2.2), new Point3d(0, 0, 0), 5, ColorConstants.cyan));
	}
}
