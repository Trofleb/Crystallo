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
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import sg.SpaceGroup;
import structures.AtomSite;
import structures.AtomType;
import utils.ColorConstants;

import com.sun.j3d.utils.geometry.Sphere;

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
		atoms = new Vector(50, 100);
		setExpand(1, 1, 1);
		BranchGroup bg = new BranchGroup();
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		deltaroot = new BranchGroup();
		deltaroot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		deltaroot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		delta = new TransformGroup();
		delta.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		delta.addChild(deltaroot);
		root.addChild(delta);
		bg.addChild(root);
		univers.root.addChild(bg);
	}
	
	public void setDelta(double x, double y, double z) {
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3d(x, y, z));
		delta.setTransform(t3d);
	}
	public Model3d(SpaceGroup sg, AtomSite[] sites) {
		this();
		setSg(sg);
		setAtoms(sites);
	}
	
	public JPanel createPanel() {
		JPanel p = new JPanel() {
			public void setVisible(boolean v){
				super.setVisible(v);
				univers.getCanvas().setVisible(v);
			}
		};
		p.setLayout(new BorderLayout());
		p.add(univers.getCanvas());
		p.setMinimumSize(new Dimension(1, 1));
		return p;
	}
	
	public void setAtoms(AtomSite[] sites) {
		setAtoms(sites, null, -1);
	}
	public void setAtoms(AtomSite[] sites, Color c, float r) {
		removeAll();
		for (int i=0; i<sites.length; i++) {
			Point3d p = new Point3d(sites[i].x, sites[i].y, sites[i].z);
			AtomType at = new AtomType(sites[i].symbol);
			float radius = (float) at.atomicRadius();
			if (radius==0) radius = 0.3f;
			if (r!=-1) radius=r;
			Color3f color = new Color3f(c==null?at.color():c);
			new Atom(deltaroot, p, radius, color, true);
		}
	}
	
	public void addAtom(AtomSite site, Color c, float r) {
		Point3d p = new Point3d(site.x, site.y, site.z);
		AtomType at = new AtomType(site.symbol);
		float radius = (float) at.atomicRadius();
		if (radius==0) radius = 0.3f;
		if (r!=-1) radius=r;
		Color3f color = new Color3f(c==null?at.color():c);
		new Atom(deltaroot, p, radius, color, true);
	}
	
	public void setExpand(double ex, double ey, double ez) {
		this.exm = this.exp = ex;
		this.eym = this.eyp = ey;
		this.ezm = this.ezp = ez;
		for (int i=0; i<atoms.size(); i++)
			((Atom)atoms.get(i)).changeExpand();
	}
	public void setExpand(double exm, double eym, double ezm, double exp, double eyp, double ezp) {
		this.exm = exm;
		this.eym = eym;
		this.ezm = ezm;
		this.exp = exp;
		this.eyp = eyp;
		this.ezp = ezp;
		for (int i=0; i<atoms.size(); i++)
			((Atom)atoms.get(i)).changeExpand();
	}
	public void setSg(SpaceGroup sg) {
		setSg(sg, null, "");
	}
	public void setSg(SpaceGroup sg, Color c, String suffix) {
		this.sg = sg;
		if (cell!=null) cell.hide();
		if (c==null) {
			cell = new Cell(sg.cell, 0.06, new String[]{"a"+suffix, "b"+suffix, "c"+suffix});
		}
		else {
			Color3f cf = new Color3f(c);
			cell = new Cell(sg.cell, 0.06, new String[]{"a"+suffix, "b"+suffix, "c"+suffix}, cf, cf);
		}
		cell.show(root);
		vectorOrigin.set(sg.cell.o);
		for (int i=0; i<atoms.size(); i++)
			((Atom)atoms.get(i)).changeCell();
	}
	
	public void removeAll() {
		while (!atoms.isEmpty()) {
			((Atom)atoms.get(0)).remove();
		}
	}
	
	public void clear() {
		if (cell!=null) cell.hide();
		removeAll();
	}
	
	public Position getAtomHere(Vector v, Point3d p) {
		for (int i=0; i<v.size(); i++) {
			Position a = ((Position)v.get(i));
			if (!a.hidden && Math.abs(a.pos.distance(p))<epsilon) return a;
		}
		return null;
	}
	public boolean areSamePos(Point3d p1, Point3d p2) {
		return Math.abs(p1.distance(p2))<epsilon;
	}
	
	
	public class Atom {
		public Vector positions;
		private Point3d gpos;		// logical coord
		private float radius;
		private Color3f color;
		private boolean selected, hidden;
		private Group root;
		private Point3d[] symPos;	// logical coords
		
		public Atom(Group root, Point3d gpos, float radius, Color3f color, boolean visible) {
			atoms.add(this);
			positions = new Vector(50, 100);
			this.root = root;
			this.gpos = gpos;
			this.radius = radius;
			this.color = color;
			this.hidden = !visible;

			// round to handle calculations errors, modcell to handle negative 
			// or strange coordinates
			gpos = SpaceGroup.modCell(round(gpos));
			
			updateSymPos();
			populate();
		}

		private void changeExpand() {
			populate();
		}

		private void changeCell() {
			updateSymPos();
			populate();
		}
		
		public void changePos(Point3d newPos) {
			gpos = newPos;
			updateSymPos();
			populate();
		}
		
		public Point3d getPos() {
			return gpos;
		}
		
		private void populate() {
			// set up for a new population
			Vector oldPop = positions;
			positions = new Vector(50, 50);

			// first browse the old population and make changes
			for (int i=0; i<oldPop.size(); i++) {
				Position a = (Position)oldPop.get(i);
				if (a.symPosNo<symPos.length) {
					Point3d sPos = symPos[a.symPosNo];
					if (sPos!=null) {
						Point3d newPos = new Point3d(sPos);
						newPos.add(a.expandTranslation);
						if (isPosInBounds(newPos) && getAtomHere(positions, newPos)==null) {
							a.changePos(newPos);
							positions.add(a);
							//System.out.println("move "+a);
							continue;
						}
					}
				}
				// invalid, duplicate or out of bounds, remove
				//System.out.println("del "+a);
				a.del();
			}
			
			// now add potentially new appeared atoms
			for (double i=-Math.ceil(exm-1); i<=Math.floor(exp+1)+1; i++) {
				for (double j=-Math.ceil(eym-1); j<=Math.floor(eyp+1)+1; j++) {
					for (double k=-Math.ceil(ezm-1); k<=Math.floor(ezp+1)+1; k++) {
						Vector3d v = new Vector3d(i, j, k);
						for (int l=0; l<symPos.length; l++) {
							if (symPos[l]==null) continue;
							Point3d p = new Point3d(symPos[l]);
							p.add(v);
							//TODO
							if (isPosInBounds(p) && getAtomHere(positions, p)==null) {
//							if (isPosInBounds(round(p)) && getAtomHere(positions, p)==null) {
								Position a = new Position(root, this, p, v, radius, color, l, !hidden);
								positions.add(a);
								//if (!isPosInBounds(p))System.out.println("xx "+p);
							}
						}
					}
				}
			}
		}

		private void updateSymPos() {
			symPos = sg.getSymPos(gpos); 
		}

		public Point3d round(Point3d p) {
			return new Point3d(Math.round(1000*p.x)/1000d, Math.round(1000*p.y)/1000d, Math.round(1000*p.z)/1000d);
		}
		
		public boolean isPosInBounds(Point3d p) {
			return (p.x<=exp && p.x>=(exm>=1?(-exm+1):0) && p.y<=eyp && p.y>=(eym>=1?(-eym+1):0) && p.z<=ezp && p.z>=(ezm>=1?(-ezm+1):0));
		}
		
		public void remove() {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).del();
			positions.clear();
			atoms.remove(this);
		}

		public void changeRadius(float r) {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).changeRadius(r);
			radius = r;
		}
		
		public void changeColor(Color3f c) {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).changeColor(c);
			color = c;
		}

		public void select() {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).select();
			selected = true;
		}
		
		public void unSelect() {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).unSelect();
			selected = false;
		}

		public void hide() {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).hide();
			hidden = true;
		}
		
		public void unHide() {
			for (int i=0; i<positions.size(); i++)
				((Position)positions.get(i)).unHide();
			hidden = false;
		}
		
		public boolean isHidden() {
			return hidden;
		}
		
		public boolean isSelected() {
			return selected;
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
		private Vector3d expandTranslation;	//read only, cause multiple atoms may share it
		private int symPosNo;
		private TransformGroup tg;
		
		public Position(Group parent, Atom atom, Point3d pos, Vector3d expandTranslation, float radius, Color3f color, int symPosNo, boolean visible) {
			//System.out.println("Atom "+pos+" "+this);
			this.parent = parent;
			this.atom = atom;
			this.pos = pos;
			this.color = color;
			this.radius = radius;
			this.expandTranslation = expandTranslation;
			this.symPosNo = symPosNo;
			this.selected = false; 
			this.hidden = !visible;
			root = createAtom();
			if (visible) parent.addChild(root);
		}

		public String toString() {
			return ""+pos;
		}
		
		private void del() {
			if (selected) unSelect();
			parent.removeChild(root);
			parent = null;
			root = null;
			atom = null;
			hidden = false;
		}
		private void changePos(Point3d newPos) {
			this.pos = newPos;
			Vector3d d = new Vector3d(pos);
			sg.cell.transform(d);
			//d.sub(d, origin);
			//d.sub(d, delta);
			t3d.set(radius, d);
			tg.setTransform(t3d);
		}
		
		public Atom getGroup() {
			return atom;
		}
		
		public void changeRadius(float newRadius) {
			this.radius = newRadius;
			Vector3d d = new Vector3d(pos);
			sg.cell.transform(d);
			//d.sub(d, origin);
			//d.sub(d, delta);
			t3d.set(newRadius, d);
			tg.setTransform(t3d);
		}
		
		public void changeColor(Color3f newColor) {
			this.color = newColor;
			app.setMaterial(new Material(newColor, ColorConstants.black, newColor, ColorConstants.white, 120.0f));
		}

		public void select() {
			if (selectionBG==null) createSelectionHollow();
			positionedBG.addChild(selectionBG);
			selected = true;
		}
		
		public void unSelect() {
			positionedBG.removeChild(selectionBG);
			selected = false;
		}

		public void hide() {
			parent.removeChild(root);
			hidden = true;
		}
		
		public void unHide() {
			parent.addChild(root);
			hidden = false;
		}
				
		private Group createAtom() {
			app = new Appearance();
			app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
			app.setMaterial(new Material(color, ColorConstants.black, color, ColorConstants.white, 120.0f));

			Sphere s = new Sphere(1f, Sphere.GENERATE_NORMALS, 50, app);
			try {
				s.getShape().getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
			} catch (Exception e){}

			s.getShape().setCapability(Shape3D.ALLOW_BOUNDS_READ);
			
			Vector3d d = new Vector3d(pos);
			sg.cell.transform(d);
			//d.sub(origin);
			//d.sub(delta);
			
			t3d = new Transform3D();
			t3d.set(radius, d);
			tg = new TransformGroup(t3d);
			tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
			
			positionedBG = new BranchGroup();
			positionedBG.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			positionedBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			positionedBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			positionedBG.addChild(s);
			tg.addChild(positionedBG);
			
			s.getShape().setUserData(this);

			BranchGroup objBranch = new BranchGroup(); 
			objBranch.setCapability(BranchGroup.ALLOW_DETACH);
			objBranch.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
			objBranch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			objBranch.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
					
			objBranch.addChild(tg);
			
			return objBranch;
		}
		
		public void createSelectionHollow() {
			Appearance app = new Appearance();
			app.setMaterial(new Material(ColorConstants.yellow, ColorConstants.black, ColorConstants.yellow, ColorConstants.white, 120.0f));
			TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
			app.setTransparencyAttributes(transp);

			Sphere sel = new Sphere(1.2f, Sphere.GENERATE_NORMALS, 50, app);
			selectionBG = new BranchGroup();
			selectionBG.setCapability(BranchGroup.ALLOW_DETACH);
			selectionBG.addChild(sel);
		}
	}

	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(100000*p.x)/100000d, Math.round(100000*p.y)/100000d, Math.round(100000*p.z)/100000d);
	}
}


