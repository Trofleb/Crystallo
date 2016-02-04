package engine3D;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import sg.Lattice;
import utils.ColorConstants;
import utils.Utils3d;

import com.sun.j3d.utils.geometry.Box;

public class Cell {
	private BranchGroup root;
	private Group parent;
	private Lattice sg;
	
	public Cell(Lattice sg, double repereSize, String[] labels) {
		set(sg, repereSize, labels, ColorConstants.white, ColorConstants.green);
	}
	public Cell(Lattice sg, double repereSize, String[] labels, Color3f box, Color3f repere) {
		set(sg, repereSize, labels, box, repere);
	}

	public void set(Lattice sg, double repereSize, String[] labels, Color3f box, Color3f repere) {
		this.sg = sg;
		if (root!=null) hide();
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_DETACH);
		createBox(box);
		createRepere(labels, (float)repereSize, repere);
	}
	
	public void show(Group parent) {
		this.parent=parent;
		parent.addChild(root);
	}
	public void hide() {
		parent.removeChild(root);
	}
	
	private void createBox(Color3f c) {
		/* On définit le rectangle de couleur*/
		// 2ème argument change la couleur du rectangle
		Appearance app = Utils3d.createApp(c, 0.7f);
		Box box = new Box(.5f, .5f, .5f, app);
		Transform3D t3d = new Transform3D();

		t3d.set(new Vector3d(.5, .5, .5));
		TransformGroup tg0 = new TransformGroup(t3d);
		
		t3d.set(sg.m);
		TransformGroup tg1 = new TransformGroup(t3d);

		t3d.set(sg.o);
		TransformGroup tg2 = new TransformGroup(t3d);
		
		tg0.addChild(box);
		tg1.addChild(tg0);
		tg2.addChild(tg1);
		root.addChild(tg2);
	}
	private void createRepere(String[] labels, float size, Color3f c) {
		Appearance app = Utils3d.createApp(c);
		double x = 1.0; // Longueur des fleches
		Point3d o = new Point3d(0, 0, 0);
		Point3d e1 = new Point3d(x, 0, 0);
		Point3d e2 = new Point3d(0, x, 0);
		Point3d e3 = new Point3d(0, 0, x);
		sg.transform(o);
		sg.transform(e1);
		sg.transform(e2);
		sg.transform(e3);
		/** 
		 * createArrow:
		 * point d'origine
		 * point de destination
		 * rayon du trait de la fleche
		 * rayon de la pointe de la fleche
		 * longueur de la pointe de la fleche
		 * appearance
		 * precision
		 */
		root.addChild(Utils3d.createArrow(o, e1, size, 2*size, 5*size, app, 20));
		root.addChild(Utils3d.createArrow(o, e2, size, 2*size, 5*size, app, 20));
		root.addChild(Utils3d.createArrow(o, e3, size, 2*size, 5*size, app, 20));
		root.addChild(Utils3d.createAtom(o, size, app, 20));
		root.addChild(Utils3d.createLegend(labels[0], e1, e1, size, app, false));
		root.addChild(Utils3d.createLegend(labels[1], e2, e2, size, app, false));
		root.addChild(Utils3d.createLegend(labels[2], e3, e3, size, app, false));
	}
}



