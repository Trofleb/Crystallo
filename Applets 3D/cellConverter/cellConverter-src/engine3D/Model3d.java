package engine3D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import sg.SpaceGroup;
import structures.AtomSite;
import structures.AtomType;
import utils.ColorConstants;

public class Model3d {
	public static final double epsilon = 0.02;

	public Univers univers;
	public Vector atoms;
	private SpaceGroup sg;
	public double exp, eyp, ezp, exm, eym, ezm;
	private Vector3d vectorOrigin = new Vector3d();
	private Cell cell;
	private BranchGroup root;
	private BranchGroup deltaroot;
	private TransformGroup delta;

	public Model3d() {
		this(new Univers());
	}

	public Model3d(Univers univers) {
		this.univers = univers;
		this.atoms = new Vector(50, 100);
		this.setExpand(1, 1, 1);
		BranchGroup bg = new BranchGroup();
		this.root = new BranchGroup();
		this.root.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.root.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.deltaroot = new BranchGroup();
		this.deltaroot.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.deltaroot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.delta = new TransformGroup();
		this.delta.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.delta.addChild(this.deltaroot);
		this.root.addChild(this.delta);
		bg.addChild(this.root);
		univers.root.addChild(bg);
	}

	public void setDelta(double x, double y, double z) {
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3d(x, y, z));
		this.delta.setTransform(t3d);
	}

	public Model3d(SpaceGroup sg, AtomSite[] sites) {
		this();
		this.setSg(sg);
		this.setAtoms(sites);
	}

	public JPanel createPanel() {
		JPanel p = new JPanel() {
			public void setVisible(boolean v) {
				super.setVisible(v);
				Model3d.this.univers.getCanvas().setVisible(v);
			}
		};
		p.setLayout(new BorderLayout());
		p.add(this.univers.getCanvas());
		p.setMinimumSize(new Dimension(1, 1));
		return p;
	}

	public void setAtoms(AtomSite[] sites) {
		this.setAtoms(sites, null, -1);
	}

	public void setAtoms(AtomSite[] sites, Color c, float r) {
		this.removeAll();
		for (int i = 0; i < sites.length; i++) {
			Point3d p = new Point3d(sites[i].x, sites[i].y, sites[i].z);
			AtomType at = new AtomType(sites[i].symbol);
			float radius = (float) at.atomicRadius();
			if (radius == 0)
				radius = 0.3f;
			if (r != -1)
				radius = r;
			Color3f color = new Color3f(c == null ? at.color() : c);
			new Atom(this.deltaroot, p, radius, color, true);
		}
	}

	public void addAtom(AtomSite site, Color c, float r) {
		Point3d p = new Point3d(site.x, site.y, site.z);
		AtomType at = new AtomType(site.symbol);
		float radius = (float) at.atomicRadius();
		if (radius == 0)
			radius = 0.3f;
		if (r != -1)
			radius = r;
		Color3f color = new Color3f(c == null ? at.color() : c);
		new Atom(this.deltaroot, p, radius, color, true);
	}

	public void setExpand(double ex, double ey, double ez) {
		this.exm = this.exp = ex;
		this.eym = this.eyp = ey;
		this.ezm = this.ezp = ez;
		for (int i = 0; i < this.atoms.size(); i++)
			((Atom) this.atoms.get(i)).changeExpand();
	}

	public void setExpand(double exm, double eym, double ezm, double exp, double eyp, double ezp) {
		this.exm = exm;
		this.eym = eym;
		this.ezm = ezm;
		this.exp = exp;
		this.eyp = eyp;
		this.ezp = ezp;
		for (int i = 0; i < this.atoms.size(); i++)
			((Atom) this.atoms.get(i)).changeExpand();
	}

	public void setSg(SpaceGroup sg) {
		this.setSg(sg, null, "");
	}

	public void setSg(SpaceGroup sg, Color c, String suffix) {
		this.sg = sg;
		if (this.cell != null)
			this.cell.hide();
		if (c == null)
			this.cell = new Cell(sg.cell, 0.06, new String[] { "a" + suffix, "b" + suffix, "c" + suffix });
		else {
			Color3f cf = new Color3f(c);
			this.cell = new Cell(sg.cell, 0.06, new String[] { "a" + suffix, "b" + suffix, "c" + suffix }, cf, cf);
		}
		this.cell.show(this.root);
		this.vectorOrigin.set(sg.cell.o);
		for (int i = 0; i < this.atoms.size(); i++)
			((Atom) this.atoms.get(i)).changeCell();
	}

	public void removeAll() {
		while (!this.atoms.isEmpty())
			((Atom) this.atoms.get(0)).remove();
	}

	public void clear() {
		if (this.cell != null)
			this.cell.hide();
		this.removeAll();
	}

	public Position getAtomHere(Vector v, Point3d p) {
		for (int i = 0; i < v.size(); i++) {
			Position a = ((Position) v.get(i));
			if (!a.hidden && Math.abs(a.pos.distance(p)) < epsilon)
				return a;
		}
		return null;
	}

	public boolean areSamePos(Point3d p1, Point3d p2) {
		return Math.abs(p1.distance(p2)) < epsilon;
	}

	public class Atom {
		public Vector positions;
		private Point3d gpos; // logical coord
		private float radius;
		private Color3f color;
		private boolean selected, hidden;
		private Group root;
		private Point3d[] symPos; // logical coords

		public Atom(Group root, Point3d gpos, float radius, Color3f color, boolean visible) {
			Model3d.this.atoms.add(this);
			this.positions = new Vector(50, 100);
			this.root = root;
			this.gpos = gpos;
			this.radius = radius;
			this.color = color;
			this.hidden = !visible;

			// round to handle calculations errors, modcell to handle negative
			// or strange coordinates
			gpos = SpaceGroup.modCell(this.round(gpos));

			this.updateSymPos();
			this.populate();
		}

		private void changeExpand() {
			this.populate();
		}

		private void changeCell() {
			this.updateSymPos();
			this.populate();
		}

		public void changePos(Point3d newPos) {
			this.gpos = newPos;
			this.updateSymPos();
			this.populate();
		}

		public Point3d getPos() {
			return this.gpos;
		}

		private void populate() {
			// set up for a new population
			Vector oldPop = this.positions;
			this.positions = new Vector(50, 50);

			// first browse the old population and make changes
			for (int i = 0; i < oldPop.size(); i++) {
				Position a = (Position) oldPop.get(i);
				if (a.symPosNo < this.symPos.length) {
					Point3d sPos = this.symPos[a.symPosNo];
					if (sPos != null) {
						Point3d newPos = new Point3d(sPos);
						newPos.add(a.expandTranslation);
						if (this.isPosInBounds(newPos) && Model3d.this.getAtomHere(this.positions, newPos) == null) {
							a.changePos(newPos);
							this.positions.add(a);
							// System.out.println("move "+a);
							continue;
						}
					}
				}
				// invalid, duplicate or out of bounds, remove
				// System.out.println("del "+a);
				a.del();
			}

			// now add potentially new appeared atoms
			for (double i = -Math.ceil(Model3d.this.exm - 1); i <= Math.floor(Model3d.this.exp + 1) + 1; i++)
				for (double j = -Math.ceil(Model3d.this.eym - 1); j <= Math.floor(Model3d.this.eyp + 1) + 1; j++)
					for (double k = -Math.ceil(Model3d.this.ezm - 1); k <= Math.floor(Model3d.this.ezp + 1) + 1; k++) {
						Vector3d v = new Vector3d(i, j, k);
						for (int l = 0; l < this.symPos.length; l++) {
							if (this.symPos[l] == null)
								continue;
							Point3d p = new Point3d(this.symPos[l]);
							p.add(v);
							// TODO
							if (this.isPosInBounds(p) && Model3d.this.getAtomHere(this.positions, p) == null) {
								// if (isPosInBounds(round(p)) &&
								// getAtomHere(positions, p)==null) {
								Position a = new Position(this.root, this, p, v, this.radius, this.color, l,
										!this.hidden);
								this.positions.add(a);
								// if (!isPosInBounds(p))System.out.println("xx
								// "+p);
							}
						}
					}
		}

		private void updateSymPos() {
			this.symPos = Model3d.this.sg.getSymPos(this.gpos);
		}

		public Point3d round(Point3d p) {
			return new Point3d(Math.round(1000 * p.x) / 1000d, Math.round(1000 * p.y) / 1000d,
					Math.round(1000 * p.z) / 1000d);
		}

		public boolean isPosInBounds(Point3d p) {
			return (p.x <= Model3d.this.exp && p.x >= (Model3d.this.exm >= 1 ? (-Model3d.this.exm + 1) : 0)
					&& p.y <= Model3d.this.eyp && p.y >= (Model3d.this.eym >= 1 ? (-Model3d.this.eym + 1) : 0)
					&& p.z <= Model3d.this.ezp && p.z >= (Model3d.this.ezm >= 1 ? (-Model3d.this.ezm + 1) : 0));
		}

		public void remove() {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).del();
			this.positions.clear();
			Model3d.this.atoms.remove(this);
		}

		public void changeRadius(float r) {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).changeRadius(r);
			this.radius = r;
		}

		public void changeColor(Color3f c) {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).changeColor(c);
			this.color = c;
		}

		public void select() {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).select();
			this.selected = true;
		}

		public void unSelect() {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).unSelect();
			this.selected = false;
		}

		public void hide() {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).hide();
			this.hidden = true;
		}

		public void unHide() {
			for (int i = 0; i < this.positions.size(); i++)
				((Position) this.positions.get(i)).unHide();
			this.hidden = false;
		}

		public boolean isHidden() {
			return this.hidden;
		}

		public boolean isSelected() {
			return this.selected;
		}
	}

	public class Position {
		private float radius;
		private Color3f color;
		public Point3d pos;
		private Group root, parent;
		private boolean selected, hidden;
		private Atom atom;
		private Appearance app;
		private Transform3D t3d;
		private BranchGroup selectionBG;
		private BranchGroup positionedBG;
		private Vector3d expandTranslation; // read only, cause multiple atoms
											// may share it
		private int symPosNo;
		private TransformGroup tg;

		public Position(Group parent, Atom atom, Point3d pos, Vector3d expandTranslation, float radius, Color3f color,
				int symPosNo, boolean visible) {
			// System.out.println("Atom "+pos+" "+this);
			this.parent = parent;
			this.atom = atom;
			this.pos = pos;
			this.color = color;
			this.radius = radius;
			this.expandTranslation = expandTranslation;
			this.symPosNo = symPosNo;
			this.selected = false;
			this.hidden = !visible;
			this.root = this.createAtom();
			if (visible)
				parent.addChild(this.root);
		}

		public String toString() {
			return "" + this.pos;
		}

		private void del() {
			if (this.selected)
				this.unSelect();
			this.parent.removeChild(this.root);
			this.parent = null;
			this.root = null;
			this.atom = null;
			this.hidden = false;
		}

		private void changePos(Point3d newPos) {
			this.pos = newPos;
			Vector3d d = new Vector3d(this.pos);
			Model3d.this.sg.cell.transform(d);
			// d.sub(d, origin);
			// d.sub(d, delta);
			this.t3d.set(this.radius, d);
			this.tg.setTransform(this.t3d);
		}

		public Atom getGroup() {
			return this.atom;
		}

		public void changeRadius(float newRadius) {
			this.radius = newRadius;
			Vector3d d = new Vector3d(this.pos);
			Model3d.this.sg.cell.transform(d);
			// d.sub(d, origin);
			// d.sub(d, delta);
			this.t3d.set(newRadius, d);
			this.tg.setTransform(this.t3d);
		}

		public void changeColor(Color3f newColor) {
			this.color = newColor;
			this.app.setMaterial(new Material(newColor, ColorConstants.black, newColor, ColorConstants.white, 120.0f));
		}

		public void select() {
			if (this.selectionBG == null)
				this.createSelectionHollow();
			this.positionedBG.addChild(this.selectionBG);
			this.selected = true;
		}

		public void unSelect() {
			this.positionedBG.removeChild(this.selectionBG);
			this.selected = false;
		}

		public void hide() {
			this.parent.removeChild(this.root);
			this.hidden = true;
		}

		public void unHide() {
			this.parent.addChild(this.root);
			this.hidden = false;
		}

		private Group createAtom() {
			this.app = new Appearance();
			this.app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
			this.app.setMaterial(
					new Material(this.color, ColorConstants.black, this.color, ColorConstants.white, 120.0f));

			Sphere s = new Sphere(1f, Primitive.GENERATE_NORMALS, 50, this.app);
			try {
				s.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
			} catch (Exception e) {
			}

			s.getShape().setCapability(Node.ALLOW_BOUNDS_READ);

			Vector3d d = new Vector3d(this.pos);
			Model3d.this.sg.cell.transform(d);
			// d.sub(origin);
			// d.sub(delta);

			this.t3d = new Transform3D();
			this.t3d.set(this.radius, d);
			this.tg = new TransformGroup(this.t3d);
			this.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			this.tg.setCapability(Group.ALLOW_CHILDREN_READ);

			this.positionedBG = new BranchGroup();
			this.positionedBG.setCapability(Group.ALLOW_CHILDREN_READ);
			this.positionedBG.setCapability(Group.ALLOW_CHILDREN_EXTEND);
			this.positionedBG.setCapability(Group.ALLOW_CHILDREN_WRITE);
			this.positionedBG.addChild(s);
			this.tg.addChild(this.positionedBG);

			s.getShape().setUserData(this);

			BranchGroup objBranch = new BranchGroup();
			objBranch.setCapability(BranchGroup.ALLOW_DETACH);
			objBranch.setCapability(Group.ALLOW_CHILDREN_READ);
			objBranch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
			objBranch.setCapability(Group.ALLOW_CHILDREN_WRITE);

			objBranch.addChild(this.tg);

			return objBranch;
		}

		public void createSelectionHollow() {
			Appearance app = new Appearance();
			app.setMaterial(new Material(ColorConstants.yellow, ColorConstants.black, ColorConstants.yellow,
					ColorConstants.white, 120.0f));
			TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
			app.setTransparencyAttributes(transp);

			Sphere sel = new Sphere(1.2f, Primitive.GENERATE_NORMALS, 50, app);
			this.selectionBG = new BranchGroup();
			this.selectionBG.setCapability(BranchGroup.ALLOW_DETACH);
			this.selectionBG.addChild(sel);
		}
	}

	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(100000 * p.x) / 100000d, Math.round(100000 * p.y) / 100000d,
				Math.round(100000 * p.z) / 100000d);
	}
}
