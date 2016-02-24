import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.MultipleParentException;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

/*
 * Created on 17 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Model extends BranchGroup implements ColorConstants {

	Vector atoms, links;
	BranchGroup root;
	float currentSizeAtom;
	Color3f currentColorAtom;
	float currentSizeLink;
	Color3f currentColorLink;
	private float expX, expY, expZ;
	Helper watcher;
	double currentDist = 0;
	Group distance;

	Vector selectedAtoms;
	AtomLink selectedLink;

	Vector hiddenAtoms;
	Vector hiddenLinks;

	Cell cell;

	public Model(Helper watcher) {
		super();
		this.watcher = watcher;

		this.cell = new Cell();

		this.atoms = new Vector(10, 10);
		this.links = new Vector(10, 10);

		this.selectedAtoms = new Vector(10, 10);
		this.hiddenAtoms = new Vector(10, 10);
		this.hiddenLinks = new Vector(10, 10);
		this.selectedLink = null;

		this.root = new BranchGroup();
		this.root.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.root.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.root.setCapability(Group.ALLOW_CHILDREN_READ);
		this.root.setCapability(BranchGroup.ALLOW_DETACH);
		this.setExpand(1, 1, 1);
		this.addChild(this.root);
	}

	public String atomsToString(Vector v, String indent) {
		String s = "";
		for (int i = 0; i < v.size(); i++) {
			Atom a = (Atom) v.get(i);
			s += this.atomToString(a, indent);
		}
		return s;
	}

	public String atomToString(Atom a, String indent) {
		String s = "";
		s += indent + a + "\n";
		if (a.up != null)
			s += indent + " up:" + a.up + "\n";
		if (a.symUp != null)
			s += indent + " symUp:" + a.symUp + "\n";
		if (a.down.size() > 0) {
			s += indent + " down:" + a.down.size() + "\n";
			s += this.atomsToString(a.down, indent + " ");
		}
		if (a.symDown.size() > 0) {
			s += indent + " SymDown:" + a.symDown.size() + "\n";
			s += this.atomsToString(a.symDown, indent + " ");
		}
		return s;
	}

	@Override
	public String toString() {
		String s = this.atoms.size() == 0 ? "No atoms" : "Atoms:\n";
		s += this.atomsToString(this.atoms, " ");
		return s;
	}

	public void changeColorSelected(Color3f color) {
		for (int i = 0; i < this.selectedAtoms.size(); i++) {
			Atom a = (Atom) this.selectedAtoms.get(i);
			a.setColor(color);
		}
	}

	public void changeColorSelectedBack() {
		for (int i = 0; i < this.selectedAtoms.size(); i++) {
			Atom a = (Atom) this.selectedAtoms.get(i);
			a.setColorBack();
		}
	}

	public void hideSelected() {
		for (int i = 0; i < this.selectedAtoms.size(); i++) {
			Atom a = (Atom) this.selectedAtoms.get(i);
			this.root.removeChild(a);
			a.unSelect();
			this.watcher.incHiddenAtoms();
			this.hiddenAtoms.add(a);
			a.hidden = true;
		}
		this.hideFloatLinks();
		this.selectedAtoms = new Vector(10, 10);
		this.watcher.setNbAtomSelected(0);
		this.watcher.setShowAllEnable(true);
		this.clearDistLabel();
	}

	public void hideNotSelected() {
		Vector v = new Vector(10, 10);
		this.gimmeAllAtoms(v, this.atoms);
		for (int i = 0; i < v.size(); i++) {
			Atom a = (Atom) v.get(i);
			if (!a.hidden && !this.selectedAtoms.contains(a) && !this.hiddenAtoms.contains(a)) {
				this.root.removeChild(a);
				this.hiddenAtoms.add(a);
				this.watcher.incHiddenAtoms();
				a.hidden = true;
			} else
				a.unSelect();
		}
		this.hideFloatLinks();
		this.selectedAtoms = new Vector(10, 10);
		this.watcher.setNbAtomSelected(0);
		this.watcher.setShowAllEnable(true);
		this.clearDistLabel();
	}

	public void unHide() {
		for (int i = 0; i < this.hiddenAtoms.size(); i++) {
			Atom a = (Atom) this.hiddenAtoms.get(i);
			this.root.addChild(a);
			a.hidden = false;
		}

		for (int i = 0; i < this.hiddenLinks.size(); i++) {
			AtomLink l = (AtomLink) this.hiddenLinks.get(i);
			this.root.addChild(l);
		}
		this.hiddenLinks.clear();

		this.watcher.setShowAllEnable(false);
		this.watcher.decHiddenAtoms(this.hiddenAtoms.size());
		this.hiddenAtoms.clear();
	}

	public void hideFloatLinks() {
		Vector ll = new Vector(10, 10);
		this.gimmeAllLinks(ll, this.links);
		for (int i = 0; i < ll.size(); i++) {
			AtomLink l = (AtomLink) ll.get(i);
			if ((l.a1.hidden || l.a2.hidden) && !this.hiddenLinks.contains(l)) {
				this.root.removeChild(l);
				this.hiddenLinks.add(l);
			}
		}
	}

	public void clearSelectionAndHidden() {
		for (int i = 0; i < this.selectedAtoms.size(); i++) {
			Atom a = (Atom) this.selectedAtoms.get(i);
			a.unSelect();
		}

		for (int i = 0; i < this.hiddenAtoms.size(); i++) {
			Atom a = (Atom) this.hiddenAtoms.get(i);
			a.hidden = false;
		}
		this.selectedAtoms.clear();
		this.hiddenAtoms.clear();
		this.hiddenLinks.clear();
		this.selectedLink = null;
		this.watcher.setNbAtomSelected(0);
		this.watcher.setHiddenAtoms(0);
		this.watcher.setShowAllEnable(false);
		this.watcher.setCropEnable(false);
		this.watcher.setCutEnable(false);
		this.watcher.setColorChoiceEnable(false);
	}

	Atom atomTooClose;

	public boolean IsThereAnAtomTooClose(Point3d pos) {
		for (int i = 0; i < this.atoms.size(); i++) {
			this.atomTooClose = (Atom) this.atoms.get(i);
			if (this.cell.coord(pos).distance(this.cell.coord(this.atomTooClose.pos)) <= this.atomTooClose.radius / 2)
				return true;
			// if
			// (Cell.currentCell.coord(pos).distance(Cell.currentCell.coord(a.pos))<=0.1)
			// return true;
		}
		return false;
	}

	public Atom getAtomTooClose() {
		return this.atomTooClose;
	}

	private Atom putAtomInSpace(Point3d pos, float size, Color3f color, int index, boolean hidden) {
		Atom a = new Atom(new Point3d(pos), size, color, index, !hidden, this.cell);
		if (!hidden)
			this.root.addChild(a);
		return a;
	}

	private Atom putAtomInSpace(Point3d pos, int index, boolean hidden) {
		return this.putAtomInSpace(pos, this.currentSizeAtom, this.currentColorAtom, index, hidden);
	}

	private AtomLink putLinkInSpace(Atom a1, Atom a2, float size, Color3f color) {
		AtomLink l = new AtomLink(this.cell, a1, a2, size, color);
		this.root.addChild(l);
		return l;
	}

	private AtomLink putLinkInSpace(Atom a1, Atom a2) {
		return this.putLinkInSpace(a1, a2, this.currentSizeLink, this.currentColorLink);
	}

	public Point3d addAtom(Point3d pos, int index, String label) {
		Point3d pmod = this.modCell(pos);
		for (int i = 0; i < this.atoms.size(); i++)
			if (this.arePosEquiv(((Atom) this.atoms.get(i)).pos, pmod))
				return null;

		Atom a = this.putAtomInSpace(pmod, index, false);
		this.atoms.add(a);
		a.label = label;
		this.symmetryAdd(a);

		this.watcher.watchNbAtom(this.countInCellAtoms(), this.countTotalAtoms());
		return pmod;
	}

	public void removeAtom(Point3d pos) {
		Point3d pmod = this.modCell(pos);
		Atom a;
		for (int i = 0; i < this.atoms.size(); i++) {
			a = (Atom) this.atoms.get(i);
			if (this.arePosEquiv(a.pos, pmod))
				// System.out.println("Del "+Atom.posToString(pmod));
				this.removeAtom(a);
		}

	}

	Vector hiddenAtomGroups = new Vector(50, 50);
	Vector hiddenLinksGroups = new Vector(100, 100);

	public void hideAtom(int index) {
		Atom a;
		Vector v = (Vector) this.hiddenAtomGroups.get(index);
		for (int i = 0; i < this.atoms.size(); i++) {
			a = (Atom) this.atoms.get(i);
			if (a.atomGroupIndex == index) {
				for (int j = 0; j < a.down.size(); j++) {
					Atom aa = (Atom) a.down.get(j);
					this.root.removeChild(aa);
					if (aa.selected) {
						aa.unSelect();
						this.watcher.decrementSelAtoms();
					}
					if (!aa.hidden)
						this.watcher.incHiddenAtoms();
					aa.hidden = true;
					v.add(aa);
					this.selectedAtoms.remove(aa);
					this.hiddenAtoms.remove(aa);
				}
				for (int j = 0; j < a.symDown.size(); j++) {
					Atom aa = (Atom) a.symDown.get(j);
					for (int k = 0; k < aa.down.size(); k++) {
						Atom aaa = (Atom) aa.down.get(k);
						this.root.removeChild(aaa);
						if (aaa.selected) {
							aaa.unSelect();
							this.watcher.decrementSelAtoms();
						}
						if (!aaa.hidden)
							this.watcher.incHiddenAtoms();
						aaa.hidden = true;
						v.add(aaa);
						this.selectedAtoms.remove(aaa);
						this.hiddenAtoms.remove(aaa);
					}
					this.root.removeChild(aa);
					if (aa.selected) {
						aa.unSelect();
						this.watcher.decrementSelAtoms();
					}
					if (!aa.hidden)
						this.watcher.incHiddenAtoms();
					aa.hidden = true;
					v.add(aa);
					this.selectedAtoms.remove(aa);
					this.hiddenAtoms.remove(aa);
				}
				this.root.removeChild(a);
				if (a.selected) {
					a.unSelect();
					this.watcher.decrementSelAtoms();
				}
				if (!a.hidden)
					this.watcher.incHiddenAtoms();
				a.hidden = true;
				v.add(a);
				this.selectedAtoms.remove(a);
				this.hiddenAtoms.remove(a);
			}
		}
		this.hideFloatLinks2(index);
		this.clearDistLabel();
		// refresh2(); //TODO

		this.watcher.setShowAllEnable(!this.hiddenAtoms.isEmpty());

	}

	public void unHide(int index) {
		Vector v = (Vector) this.hiddenAtomGroups.get(index);
		Vector w = (Vector) this.hiddenLinksGroups.get(index);
		for (int i = 0; i < v.size(); i++) {
			Atom a = (Atom) v.get(i);
			this.root.addChild(a);
			a.hidden = false;
			this.watcher.decHiddenAtoms();
		}
		v.clear();

		for (int i = 0; i < w.size(); i++) {
			AtomLink l = (AtomLink) w.get(i);
			if (!l.a1.hidden && !l.a2.hidden) {
				this.root.addChild(l);
				l.hiddenIn = null;
			}
		}
		w.clear();
	}

	public void hideFloatLinks2(int index) {
		Vector w = (Vector) this.hiddenLinksGroups.get(index);
		Vector ll = new Vector(10, 10);
		this.gimmeAllLinks(ll, this.links);
		for (int i = 0; i < ll.size(); i++) {
			AtomLink l = (AtomLink) ll.get(i);
			if ((l.a1.atomGroupIndex == index || l.a2.atomGroupIndex == index) && !this.hiddenLinks.contains(l)
					&& !w.contains(l)) {
				this.root.removeChild(l);
				w.add(l);
				l.hiddenIn = w;
			}
		}
	}

	public void delLinksForAtom(Atom a) {
		for (int i = this.links.size() - 1; i >= 0; i--) {
			AtomLink l = (AtomLink) this.links.get(i);
			if (l.a1 == a || l.a2 == a) {
				this.root.removeChild(l);
				this.links.remove(i);
			}
			for (int j = l.down.size() - 1; j >= 0; j--) {
				AtomLink ll = (AtomLink) l.down.get(j);
				if (ll.a1 == a || ll.a2 == a) {
					this.root.removeChild(ll);
					this.links.remove(j);
				}
			}
		}
	}

	public void selectLink(AtomLink l) {
		if (this.distance != null) {
			this.root.removeChild(this.distance);
			this.distance = null;
		}

		if (this.selectedLink == l) {
			l.unSelect();
			this.selectedLink = null;
		} else {
			if (this.selectedLink != null)
				this.selectedLink.unSelect();
			this.selectedLink = l;
			l.select();
			this.watcher.setCurrBoundSize(l.radius);
			this.watcher.setCurrBoundColor(l.color.get());
		}
		if (this.selectedLink != null) {
			double d = this.distance(l.a1, l.a2);
			Appearance app = new Appearance();
			app.setMaterial(new Material(l.color, black, l.color, white, 120.0f));
			Point3d p = new Point3d(l.a1.pos);
			Vector3d v = new Vector3d();
			v.sub(l.a2.pos, l.a1.pos);
			v.scale(0.5);
			p.add(v);
			float s = (float) ((this.cell.a + this.cell.b + this.cell.c) / 3.0 / 25.0);
			this.distance = Atom.createLegend("d=" + (Math.round(d * 100) / 100.0) + "Å", this.cell.coord(p),
					this.cell.coord(p), s, app);
			this.root.addChild(this.distance);
		}

		this.watcher.setBondDelEnable(this.selectedLink != null);
	}

	public void selUnselAtom(Atom a, boolean limitedTo2) {
		this.clearDistLabel();

		if (this.selectedAtoms.contains(a)) {
			a.unSelect();
			this.selectedAtoms.remove(a);
			this.watcher.unSelectAtomInTable(a.pos);
		} else {
			if (limitedTo2 && this.selectedAtoms.size() > 2)
				this.clearAtomSelection();
			if (limitedTo2 && this.selectedAtoms.size() == 2) {
				((Atom) this.selectedAtoms.get(0)).unSelect();
				this.selectedAtoms.remove(0);
				this.watcher.unSelectAtomInTable(((Atom) this.selectedAtoms.get(0)).pos);
			}
			a.select();
			this.selectedAtoms.add(a);
			this.watcher.selectAtomInTable(a.pos);
		}
		this.watcher.setNbAtomSelected(this.selectedAtoms.size());
	}

	public void selectMultipleAtoms(Atom a) {
		this.clearDistLabel();

		if (!this.selectedAtoms.contains(a)) {
			a.select();
			this.selectedAtoms.add(a);
			this.watcher.selectAtomInTable(a.pos);
		}
	}

	public void jobsAfterSelAtom() {
		this.watcher.setBondAddEnable(this.selectedAtoms.size() == 2);
		this.showDistLabel();
	}

	public void clearAtomSelection() {
		this.clearDistLabel();
		for (int i = 0; i < this.selectedAtoms.size(); i++) {
			Atom a = (Atom) this.selectedAtoms.get(i);
			a.unSelect();
			this.watcher.unSelectAtomInTable(a.pos);
		}
		this.selectedAtoms.clear();
		this.watcher.setBondAddEnable(false);
		// watcher.setNbAtomSelected(0);
	}

	public void clearDistLabel() {
		if (this.distance != null) {
			this.root.removeChild(this.distance);
			this.distance = null;
		}
	}

	private void showDistLabel() {
		if (this.selectedAtoms.size() == 2) {
			this.clearDistLabel();
			double d = this.distance((Atom) this.selectedAtoms.get(0), (Atom) this.selectedAtoms.get(1));
			Appearance app = new Appearance();
			app.setMaterial(new Material(this.currentColorLink, black, this.currentColorLink, white, 120.0f));
			Point3d p = new Point3d(((Atom) this.selectedAtoms.get(0)).pos);
			Vector3d v = new Vector3d();
			v.sub(((Atom) this.selectedAtoms.get(1)).pos, ((Atom) this.selectedAtoms.get(0)).pos);
			v.scale(0.5);
			p.add(v);
			float s = (float) ((this.cell.a + this.cell.b + this.cell.c) / 3.0 / 25.0);
			this.distance = Atom.createLegend("d=" + (Math.round(d * 100) / 100.0) + "Å", this.cell.coord(p),
					new Point3d(0, 0, 0), s, app);
			this.root.addChild(this.distance);
		}
	}

	public void removeAtom(Atom a) {

		// System.out.println("remove atom");//TODO
		/*
		 * symmetryDel(a);
		 * root.removeChild(a);
		 * atoms.remove(a);
		 * selectedAtoms.remove(a);
		 * hiddenAtoms.remove(a);
		 * clearDistLabel();
		 * //removeFloatLinks();
		 */

		if (this.selectedAtoms.contains(a)) {
			a.unSelect();
			this.selectedAtoms.remove(a);
		}
		a = this.modCell(a);
		if (a.symUp != null)
			a = a.symUp;
		this.delLinksForAtom(a);
		this.atoms.remove(a);

		this.refresh();
		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());
		this.watcher.watchNbAtom(this.countInCellAtoms(), this.countTotalAtoms());
	}

	public boolean arePosEquiv(Point3d p1, Point3d p2) {
		return Math.abs(this.cell.coord(p1).distance(this.cell.coord(p2))) < 0.1;
	}

	public double distance(Atom a1, Atom a2) {
		return Math.abs(this.cell.coord(a1.pos).distance(this.cell.coord(a2.pos)));
	}

	public int countInCellAtoms() {
		return this.atoms.size();
	}

	public int countTotalAtoms() {
		return this.countTotalAtoms(this.atoms);
	}

	public int countTotalAtoms(Vector v) {
		int n = v.size();
		for (int i = 0; i < v.size(); i++) {
			n += this.countTotalAtoms(((Atom) v.get(i)).down);
			n += this.countTotalAtoms(((Atom) v.get(i)).symDown);
		}
		return n;
	}

	public int countSymAtoms() {
		int i, n;
		for (n = 0, i = 0; i < this.atoms.size(); i++)
			n += ((Atom) this.atoms.get(i)).symDown.size();
		return n;
	}

	public int countInCellLinks() {
		return this.links.size();
	}

	public int countTotalLinks() {
		int n = this.countInCellLinks();
		for (int i = 0; i < this.links.size(); i++)
			for (int j = 0; j < ((AtomLink) this.links.get(i)).down.size(); j++)
				n++;
		return n;
	}

	public Atom modCell(Atom a) {
		return a.up == null ? a : a.up;
	}

	public Point3d modCell(Point3d p) {
		return new Point3d(p.x < 0 ? (1 - ((-p.x) % 1)) % 1 : p.x % 1, p.y < 0 ? (1 - ((-p.y) % 1)) % 1 : p.y % 1,
				p.z < 0 ? (1 - ((-p.z) % 1)) % 1 : p.z % 1);
	}

	public void gimmeAllAtoms(Vector t, Vector v) {
		for (int i = 0; i < v.size(); i++) {
			Atom a = (Atom) v.get(i);
			t.add(a);
			this.gimmeAllAtoms(t, a.down);
			this.gimmeAllAtoms(t, a.symDown);
		}
	}

	public void gimmeAllLinks(Vector t, Vector v) {
		for (int i = 0; i < v.size(); i++) {
			AtomLink l = (AtomLink) v.get(i);
			t.add(l);
			this.gimmeAllLinks(t, l.down);
		}
	}

	public double minDistUp(double prev) {
		double dmax = 1000.0;
		double dalt = dmax;
		Vector v = new Vector(10, 10);
		this.gimmeAllAtoms(v, this.atoms);
		for (int i = 0; i < v.size(); i++)
			for (int j = i + 1; j < v.size(); j++) {
				Atom a1 = (Atom) v.get(i);
				Atom a2 = (Atom) v.get(j);
				if (a1.hidden || a2.hidden)
					continue;
				double d = Math.round(this.distance(a1, a2) * 1000d) / 1000d;
				if (d > Math.round(prev * 1000d) / 1000d && d < Math.round(dmax * 1000d) / 1000d)
					dmax = d;
				if (d >= Math.round(prev * 1000d) / 1000d)
					dalt = d;
			}
		this.currentDist = dmax > 900.0 ? dalt : dmax;
		this.currentDist = this.currentDist > 900.0 ? 0.0 : this.currentDist;
		return this.currentDist;
	}

	public double minDistDown(double prev) {
		double dmin = 0.0;
		Vector v = new Vector(10, 10);
		this.gimmeAllAtoms(v, this.atoms);

		for (int i = 0; i < v.size(); i++)
			for (int j = i + 1; j < v.size(); j++) {
				Atom a1 = (Atom) v.get(i);
				Atom a2 = (Atom) v.get(j);
				if (a1.hidden || a2.hidden)
					continue;
				double d = Math.round(this.distance((Atom) v.get(i), (Atom) v.get(j)) * 1000d) / 1000d;
				if (d < Math.round(prev * 1000d) / 1000d && d > Math.round(dmin * 1000d) / 1000d)
					dmin = d;
			}
		this.currentDist = dmin;
		return dmin;
	}

	public void boundUnder_soft(double d) {
		this.currentDist = d;
		Vector v = new Vector(10, 10);
		this.gimmeAllAtoms(v, this.atoms);

		for (int i = this.links.size() - 1; i >= 0; i--) {
			AtomLink l = (AtomLink) this.links.get(i);
			double dd = Math.round(this.distance(l.a1, l.a2) * 1000d) / 1000d;
			// ok1 = l.a1.pos.x<=expX && l.a1.pos.y<=expY && l.a1.pos.z<=expZ;
			// ok2 = l.a2.pos.x<=expX && l.a2.pos.y<=expY && l.a2.pos.z<=expZ;
			if ((dd > d && !l.userMade) || !this.isAtomInBounds(l.a1) || !this.isAtomInBounds(l.a2)) {
				this.root.removeChild(l);
				this.links.remove(i);
				if (l.hiddenIn != null)
					l.hiddenIn.remove(l);
			}
			for (int j = l.down.size() - 1; j >= 0; j--) {
				AtomLink ll = (AtomLink) l.down.get(j);
				double ddd = Math.round(this.distance(ll.a1, ll.a2) * 1000d) / 1000d;
				// ok1 = ll.a1.pos.x<=expX && ll.a1.pos.y<=expY &&
				// ll.a1.pos.z<=expZ;
				// ok2 = ll.a2.pos.x<=expX && ll.a2.pos.y<=expY &&
				// ll.a2.pos.z<=expZ;
				if ((ddd > d && !l.userMade) || !this.isAtomInBounds(ll.a1) || !this.isAtomInBounds(ll.a2)) {
					this.root.removeChild(ll);
					l.down.remove(j);
					if (ll.hiddenIn != null)
						ll.hiddenIn.remove(ll);
				}
			}
		}

		Atom a1, a2;
		for (int i = 0; i < v.size(); i++)
			for (int j = i + 1; j < v.size(); j++) {
				a1 = (Atom) v.get(i);
				a2 = (Atom) v.get(j);
				if (a1.hidden || a2.hidden)
					continue;
				// ok1 = a1.pos.x<=expX && a1.pos.y<=expY && a1.pos.z<=expZ;
				// ok2 = a2.pos.x<=expX && a2.pos.y<=expY && a2.pos.z<=expZ;
				double dd = Math.round(this.distance(a1, a2) * 1000d) / 1000d;
				if (dd <= Math.round(d * 1000d) / 1000d && this.isAtomInBounds(a1) && this.isAtomInBounds(a2))
					this.addLink(a1, a2);
			}
		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());
	}

	public void boundUnder_hard(double d) {
		this.currentDist = d;
		Vector v = new Vector(10, 10);
		this.gimmeAllAtoms(v, this.atoms);

		for (int i = this.links.size() - 1; i >= 0; i--) {
			AtomLink l = (AtomLink) this.links.get(i);
			double dd = Math.round(this.distance(l.a1, l.a2) * 1000d) / 1000d;
			// ok1 = l.a1.pos.x<=expX && l.a1.pos.y<=expY && l.a1.pos.z<=expZ;
			// ok2 = l.a2.pos.x<=expX && l.a2.pos.y<=expY && l.a2.pos.z<=expZ;
			if (dd > d || !this.isAtomInBounds(l.a1) || !this.isAtomInBounds(l.a2)) {
				this.root.removeChild(l);
				this.links.remove(i);
				if (l.hiddenIn != null)
					l.hiddenIn.remove(l);
			}
			for (int j = l.down.size() - 1; j >= 0; j--) {
				AtomLink ll = (AtomLink) l.down.get(j);
				double ddd = Math.round(this.distance(ll.a1, ll.a2) * 1000d) / 1000d;
				// ok1 = ll.a1.pos.x<=expX && ll.a1.pos.y<=expY &&
				// ll.a1.pos.z<=expZ;
				// ok2 = ll.a2.pos.x<=expX && ll.a2.pos.y<=expY &&
				// ll.a2.pos.z<=expZ;
				if (ddd > d || !this.isAtomInBounds(ll.a1) || !this.isAtomInBounds(ll.a2)) {
					this.root.removeChild(ll);
					l.down.remove(j);
					if (ll.hiddenIn != null)
						ll.hiddenIn.remove(ll);
				}
			}
		}

		Atom a1, a2;
		for (int i = 0; i < v.size(); i++)
			for (int j = i + 1; j < v.size(); j++) {
				a1 = (Atom) v.get(i);
				a2 = (Atom) v.get(j);
				if (a1.hidden || a2.hidden)
					continue;
				// ok1 = a1.pos.x<=expX && a1.pos.y<=expY && a1.pos.z<=expZ;
				// ok2 = a2.pos.x<=expX && a2.pos.y<=expY && a2.pos.z<=expZ;
				double dd = Math.round(this.distance(a1, a2) * 1000d) / 1000d;
				if (dd <= Math.round(d * 1000d) / 1000d && this.isAtomInBounds(a1) && this.isAtomInBounds(a2))
					this.addLink(a1, a2);
			}
		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());
	}

	public Atom findAtom(Point3d p) {
		return this.findAtom(this.atoms, p);
	}

	public Atom findAtom(Vector v, Point3d p) {
		Atom a, aa;
		for (int i = 0; i < v.size(); i++) {
			a = (Atom) v.get(i);
			if (this.arePosEquiv(a.pos, p))
				return a;
			aa = this.findAtom(a.down, p);
			if (aa != null)
				return aa;
			aa = this.findAtom(a.symDown, p);
			if (aa != null)
				return aa;
		}
		return null;
	}

	public AtomLink findLink(Point3d p1, Point3d p2) {
		return this.findLink(this.links, p1, p2);
	}

	public AtomLink findLink(Vector v, Point3d p1, Point3d p2) {
		AtomLink l, ll;
		for (int i = 0; i < v.size(); i++) {
			l = (AtomLink) v.get(i);
			if (this.arePosEquiv(l.a1.pos, p1) && this.arePosEquiv(l.a2.pos, p2))
				return l;
			if (this.arePosEquiv(l.a1.pos, p2) && this.arePosEquiv(l.a2.pos, p1))
				return l;
			ll = this.findLink(l.down, p1, p2);
			if (ll != null)
				return ll;
		}
		return null;
	}

	public AtomLink addLink(Atom a1, Atom a2) {
		if (this.findLink(a1.pos, a2.pos) != null)
			return null;
		AtomLink l = this.putLinkInSpace(a1, a2);
		this.links.add(l);
		this.expandAdd(l);

		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());
		return l;
	}

	public void removeLink(AtomLink l) {
		if (l.up != null)
			l = l.up;
		this.links.remove(l);
		this.expandDel(l);
		this.root.removeChild(l);

		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());
	}

	/*
	 * public void setCell(Cell cell) {
	 * this.cell=cell;
	 * links.clear();
	 * refresh();
	 * }
	 */

	public void setExpand(float x, float y, float z) {
		this.expX = x;
		this.expY = y;
		this.expZ = z;
		this.refresh();
	}

	public Point3f getExpand() {
		return new Point3f(this.expX, this.expY, this.expZ);
	}

	public void refresh() {
		// System.out.println("refresh"); //TODO

		// new Throwable().printStackTrace();

		this.clearSelectionAndHidden();

		this.root.removeAllChildren();

		// selectedAtoms.removeAllElements();

		/*
		 * if (distance!=null) {
		 * root.removeChild(distance);
		 * distance = null;
		 * }
		 * selectedLink = null;
		 * selectedAtoms.clear();
		 */
		this.watcher.setBondDelEnable(this.selectedLink != null);
		this.watcher.setBondAddEnable(this.selectedAtoms.size() == 2);

		for (int i = 0; i < this.atoms.size(); i++) {
			Atom a = (Atom) this.atoms.get(i);
			a.reCellPos(this.cell);
			if (!a.hidden)
				this.root.addChild(a);
			else
				this.watcher.incHiddenAtoms();
			a.down.clear();
			a.symDown.clear();
			((Vector) this.hiddenAtomGroups.get(a.atomGroupIndex)).clear();
			if (a.hidden)
				((Vector) this.hiddenAtomGroups.get(a.atomGroupIndex)).add(a);
			this.symmetryAdd(a);
		}

		for (int i = this.links.size() - 1; i >= 0; i--) {
			AtomLink l = (AtomLink) this.links.get(i);
			// if (l.a1.hidden || l.a2.hidden) continue;
			if (!l.userMade) {
				this.links.remove(i);
				if (l.hiddenIn != null)
					l.hiddenIn.remove(l);
			} else {
				if (!l.a1.hidden && !l.a2.hidden)
					this.root.addChild(l);
				this.expandAdd(l);
				for (int j = 0; j < l.down.size(); j++)
					((AtomLink) l.down.get(j)).userMade = true;
			}
			for (int j = 0; j < l.down.size(); j++) {
				AtomLink ll = (AtomLink) l.down.get(j);
				try {
					if (!this.isAtomInBounds(ll.a1) || !this.isAtomInBounds(ll.a2)) {
						if (ll.hiddenIn != null)
							ll.hiddenIn.remove(ll);
						l.down.remove(ll);
					} else if (ll.userMade && !l.a1.hidden && !l.a2.hidden)
						this.root.addChild(ll);
				} catch (MultipleParentException e) {
				}
				;
			}
		}

		// Vector w = (Vector) hiddenLinksGroups.get(index);

		// links.clear();

		this.boundUnder_soft(this.currentDist);

		// System.gc();
		this.watcher.watchNbAtom(this.countInCellAtoms(), this.countTotalAtoms());
		this.watcher.watchNbLink(this.countInCellLinks(), this.countTotalLinks());

	}

	public boolean isAtomInBounds(Atom a) {
		return (a.pos.x <= this.expX && a.pos.x >= (this.expX >= 1 ? (-this.expX + 1) : 0) && a.pos.y <= this.expY
				&& a.pos.y >= (this.expY >= 1 ? (-this.expY + 1) : 0) && a.pos.z <= this.expZ
				&& a.pos.z >= (this.expZ >= 1 ? (-this.expZ + 1) : 0));
	}

	public void symmetryAdd(Atom a) {
		Atom aa;
		Point3d p;
		Vector v = ((Vector) this.hiddenAtomGroups.get(a.atomGroupIndex));

		float[][] pos;
		if (this.cell.no == 0)
			pos = CellSymetries.posCustom(this.cell.m_ccell, (float) a.pos.x, (float) a.pos.y, (float) a.pos.z);
		else
			pos = CellGen.pos(this.cell.no, this.cell.choice, (float) a.pos.x, (float) a.pos.y, (float) a.pos.z);

		for (int i = 0; i < pos.length; i++) {
			p = this.modCell(new Point3d(pos[i][0], pos[i][1], pos[i][2]));
			if (this.findAtom(p) == null) {
				aa = this.putAtomInSpace(p, a.radius, a.color, a.atomGroupIndex, a.hidden);
				a.symDown.add(aa);
				aa.symUp = a;
				if (aa.hidden) {
					v.add(aa);
					this.watcher.incHiddenAtoms();
				}
				this.expandAdd(aa);
			}
		}
		this.expandAdd(a);
	}

	public void symmetryDel(Atom a) {
		this.expandDel(this.modCell(a));

		for (int i = 0; i < a.symDown.size(); i++) {
			Atom aa = (Atom) a.symDown.get(i);
			this.root.removeChild(aa);
			this.expandDel(aa);
		}
		if (a.symUp != null) {
			for (int i = 0; i < a.symUp.symDown.size(); i++) {
				Atom aa = (Atom) a.symUp.symDown.get(i);
				this.root.removeChild(aa);
				this.expandDel(aa);
			}
			a.symUp.symDown.clear();
			this.root.removeChild(a.symUp);
			this.expandDel(a.symUp);
		}
		a.symDown.clear();
	}

	public void expandAdd(Atom a) {
		Atom aa, aaa;

		Vector v = ((Vector) this.hiddenAtomGroups.get(a.atomGroupIndex));
		for (int i = 0; i < this.atoms.size(); i++) {
			aa = (Atom) this.atoms.get(i);

			// if (aa.pos.x>expX || aa.pos.y>expY || aa.pos.z>expZ)
			if (!this.isAtomInBounds(aa))
				this.root.removeChild(aa);
			for (int j = 0; j < aa.symDown.size(); j++) {
				aaa = (Atom) aa.symDown.get(j);
				if (!this.isAtomInBounds(aaa))
					// if (aaa.pos.x>expX || aaa.pos.y>expY || aaa.pos.z>expZ)
					this.root.removeChild(aaa);
			}
		}

		for (double i = -Math.ceil(this.expX - 1); i <= Math.floor(this.expX + 1) + 1; i++)
			for (double j = -Math.ceil(this.expY - 1); j <= Math.floor(this.expY + 1) + 1; j++)
				for (double k = -Math.ceil(this.expZ - 1); k <= Math.floor(this.expZ + 1) + 1; k++)
					if (a.pos.x % 1 + i <= this.expX && a.pos.x % 1 + i >= (this.expX >= 1 ? (-this.expX + 1) : 0)
							&& a.pos.y % 1 + j <= this.expY
							&& a.pos.y % 1 + j >= (this.expY >= 1 ? (-this.expY + 1) : 0)
							&& a.pos.z % 1 + k <= this.expZ
							&& a.pos.z % 1 + k >= (this.expZ >= 1 ? (-this.expZ + 1) : 0))
						if (!(a.pos.x % 1 + i == a.pos.x && a.pos.y % 1 + j == a.pos.y && a.pos.z % 1 + k == a.pos.z)) {
							aa = this.putAtomInSpace(new Point3d(a.pos.x % 1 + i, a.pos.y % 1 + j, a.pos.z % 1 + k),
									a.radius, a.color, a.atomGroupIndex, a.hidden);
							a.down.add(aa);
							aa.up = a;
							if (aa.hidden) {
								v.add(aa);
								this.watcher.incHiddenAtoms();
							}
						}
	}

	public void expandDel(Atom a) {
		for (int i = 0; i < a.down.size(); i++) {
			Atom aa = (Atom) a.down.get(i);
			this.root.removeChild(aa);
		}
		if (a.up != null) {
			for (int i = 0; i < a.up.down.size(); i++) {
				Atom aa = (Atom) a.up.down.get(i);
				this.root.removeChild(aa);
			}
			a.up.down.clear();
			this.root.removeChild(a.up);
		}
		a.down.clear();
	}

	private void expandAllLinksForAnAtom(AtomLink l, Atom a) {
		boolean ok3;
		Point3d p = new Point3d(l.a2.pos.x - (l.a1.pos.x - a.pos.x), l.a2.pos.y - (l.a1.pos.y - a.pos.y),
				l.a2.pos.z - (l.a1.pos.z - a.pos.z));
		Atom aa = this.findAtom(p);
		if (aa != null) {
			// ok1 = a.pos.x<=expX && a.pos.y<=expY && a.pos.z<=expZ;
			// ok2 = aa.pos.x<=expX && aa.pos.y<=expY && aa.pos.z<=expZ;
			ok3 = this.arePosEquiv(this.modCell(a.pos), this.modCell(l.a1.pos))
					|| this.arePosEquiv(this.modCell(a.pos), this.modCell(l.a2.pos))
					|| this.arePosEquiv(this.modCell(aa.pos), this.modCell(l.a1.pos))
					|| this.arePosEquiv(this.modCell(aa.pos), this.modCell(l.a2.pos));
			if (this.findLink(a.pos, aa.pos) == null && this.isAtomInBounds(a) && this.isAtomInBounds(aa) && ok3)
				if (!a.hidden && !aa.hidden) {
					AtomLink ll = this.putLinkInSpace(a, aa, l.radius, l.color);
					l.down.add(ll);
					ll.up = l;
				}
		}
	}

	public void expandAdd(AtomLink l) {
		this.expandAdd(this.atoms, l);
	}

	public void expandAdd(Vector v, AtomLink l) {
		Atom a;
		for (int i = 0; i < v.size(); i++) {
			a = (Atom) v.get(i);
			this.expandAllLinksForAnAtom(l, a);
			this.expandAdd(a.down, l);
			this.expandAdd(a.symDown, l);
		}
	}

	public void expandDel(AtomLink l) {
		for (int i = 0; i < l.down.size(); i++)
			this.root.removeChild((AtomLink) l.down.get(i));
		if (l.up != null) {
			for (int i = 0; i < l.up.down.size(); i++)
				this.root.removeChild((AtomLink) l.up.down.get(i));
			l.up.down.clear();
			this.root.removeChild(l.up);
		}
		l.down.clear();
	}
}
