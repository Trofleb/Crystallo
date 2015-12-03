import java.awt.Color;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Link;
import javax.media.j3d.Material;
import javax.media.j3d.MultipleParentException;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;

/*
 * Created on 17 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
		this.watcher=watcher;
		
		cell = new Cell();
		
		atoms = new Vector(10, 10);
		links = new Vector(10, 10);

		selectedAtoms = new Vector(10, 10);
		hiddenAtoms = new Vector(10, 10);
		hiddenLinks = new Vector(10, 10);
		selectedLink = null;
		
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		root.setCapability(BranchGroup.ALLOW_DETACH);
		setExpand(1, 1, 1);
		this.addChild(root);
	}


		public String atomsToString(Vector v, String indent) {
			String s="";
			for (int i=0; i<v.size(); i++) {
				Atom a = (Atom)v.get(i);
				s+=atomToString(a, indent);
			}
			return s;
		}

		public String atomToString(Atom a, String indent) {
		String s="";
		s+=indent+a+"\n";
		if (a.up!=null) s+=indent+" up:"+a.up+"\n";
		if (a.symUp!=null) s+=indent+" symUp:"+a.symUp+"\n";
		if (a.down.size()>0) {
			s+=indent+" down:"+a.down.size()+"\n";
			s+=atomsToString(a.down, indent+" ");
		}
		if (a.symDown.size()>0) {
			s+=indent+" SymDown:"+a.symDown.size()+"\n";
			s+=atomsToString(a.symDown, indent+" ");
		}
		return s;
	}
	
	public String toString() {
		String s = atoms.size()==0?"No atoms":"Atoms:\n";
		s+=atomsToString(atoms, " ");
		return s;
	}
	
	public void changeColorSelected(Color3f color) {
		for (int i=0; i<selectedAtoms.size(); i++) {
			Atom a = (Atom)selectedAtoms.get(i);
			a.setColor(color);
		}
	}
	public void changeColorSelectedBack() {
		for (int i=0; i<selectedAtoms.size(); i++) {
			Atom a = (Atom)selectedAtoms.get(i);
			a.setColorBack();
		}
	}
	
	public void hideSelected() {
		for (int i=0; i<selectedAtoms.size(); i++) {
			Atom a = (Atom)selectedAtoms.get(i);
			root.removeChild(a);
			a.unSelect();
			watcher.incHiddenAtoms();
			hiddenAtoms.add(a);
			a.hidden = true;
		}
		hideFloatLinks();
		selectedAtoms = new Vector(10, 10);
		watcher.setNbAtomSelected(0);
		watcher.setShowAllEnable(true);
		clearDistLabel();
	}

	public void hideNotSelected() {
		Vector v = new Vector(10, 10); 
		gimmeAllAtoms(v, atoms);
		for (int i=0; i<v.size(); i++) {
			Atom a = (Atom)v.get(i);
			if (!a.hidden && !selectedAtoms.contains(a) && !hiddenAtoms.contains(a)) {
				root.removeChild(a);
				hiddenAtoms.add(a);
				watcher.incHiddenAtoms();
				a.hidden = true;
			}
			else {
				a.unSelect();
			}
		}
		hideFloatLinks();
		selectedAtoms = new Vector(10, 10);
		watcher.setNbAtomSelected(0);
		watcher.setShowAllEnable(true);
		clearDistLabel();
	}
	
	public void unHide() {
		for (int i=0; i<hiddenAtoms.size(); i++) {
			Atom a = (Atom)hiddenAtoms.get(i);
			root.addChild(a);
			a.hidden = false;
		}

		for (int i=0; i<hiddenLinks.size(); i++) {
			AtomLink l = (AtomLink)hiddenLinks.get(i);
			root.addChild(l);
		}
		hiddenLinks.clear();

		watcher.setShowAllEnable(false);
		watcher.decHiddenAtoms(hiddenAtoms.size());
		hiddenAtoms.clear();
	}
	
	public void hideFloatLinks() {
		Vector ll = new Vector(10, 10); 
		gimmeAllLinks(ll, links);
		for (int i=0; i<ll.size(); i++) {
			AtomLink l = (AtomLink)ll.get(i);
			if ((l.a1.hidden || l.a2.hidden) && !hiddenLinks.contains(l)) {
				root.removeChild(l);
				hiddenLinks.add(l);
			}
		}
	}
	
	
	public void clearSelectionAndHidden() {
		for (int i=0; i<selectedAtoms.size(); i++) {
			Atom a = (Atom)selectedAtoms.get(i);
			a.unSelect();
		}
		
		for (int i=0; i<hiddenAtoms.size(); i++) {
			Atom a = (Atom)hiddenAtoms.get(i);
			a.hidden = false;
		}
		selectedAtoms.clear();
		hiddenAtoms.clear();
		hiddenLinks.clear();
		selectedLink = null;
		watcher.setNbAtomSelected(0);
		watcher.setHiddenAtoms(0);
		watcher.setShowAllEnable(false);
		watcher.setCropEnable(false);
		watcher.setCutEnable(false);
		watcher.setColorChoiceEnable(false);
	}
	
	
	Atom atomTooClose;
	
	public boolean IsThereAnAtomTooClose(Point3d pos) {
		for (int i=0; i<atoms.size(); i++) {
			atomTooClose = (Atom)atoms.get(i);
			if (cell.coord(pos).distance(cell.coord(atomTooClose.pos))<=atomTooClose.radius/2) return true;
//			if (Cell.currentCell.coord(pos).distance(Cell.currentCell.coord(a.pos))<=0.1) return true;
		}
		return false;
	}
	
	public Atom getAtomTooClose() {
		return atomTooClose;
	}
	
	private Atom putAtomInSpace(Point3d pos, float size, Color3f color, int index, boolean hidden) {
		Atom a = new Atom(new Point3d(pos), size, color, index, !hidden, cell);
		if (!hidden) root.addChild(a);
		return a;
	}
	
	private Atom putAtomInSpace(Point3d pos, int index, boolean hidden) {
		return putAtomInSpace(pos, currentSizeAtom, currentColorAtom, index, hidden);
	}

	private AtomLink putLinkInSpace(Atom a1, Atom a2, float size, Color3f color) {
		AtomLink l = new AtomLink(cell, a1, a2, size, color);
		root.addChild(l);
		return l;
	}

	private AtomLink putLinkInSpace(Atom a1, Atom a2) {
		return putLinkInSpace(a1, a2, currentSizeLink, currentColorLink);
	}

	public Point3d addAtom(Point3d pos, int index, String label) {
		Point3d pmod = modCell(pos);
		for (int i=0; i<atoms.size(); i++)
			if (arePosEquiv(((Atom)atoms.get(i)).pos, pmod))
				return null;

		Atom a = putAtomInSpace(pmod, index, false);
		atoms.add(a);
		a.label=label;
		symmetryAdd(a);
		
		watcher.watchNbAtom(countInCellAtoms(), countTotalAtoms());
		return pmod;
	}



	public void removeAtom(Point3d pos) {
		Point3d pmod = modCell(pos);
		Atom a;
		for (int i=0; i<atoms.size(); i++) {
			a = (Atom)atoms.get(i);
			if (arePosEquiv(a.pos, pmod)) {
				//System.out.println("Del "+Atom.posToString(pmod));
				removeAtom(a);
			}
		}
		
	}

	Vector hiddenAtomGroups = new Vector(50, 50);
	Vector hiddenLinksGroups = new Vector(100, 100);
	
	public void hideAtom(int index) {
		Atom a;
		Vector v = (Vector) hiddenAtomGroups.get(index);
		for (int i=0; i<atoms.size(); i++) {
			a = (Atom)atoms.get(i);
			if (a.atomGroupIndex==index) {
				for (int j=0; j<a.down.size(); j++) {
					Atom aa = (Atom)a.down.get(j);
					root.removeChild(aa);
					if (aa.selected) {
						aa.unSelect();
						watcher.decrementSelAtoms();
					}
					if (!aa.hidden) watcher.incHiddenAtoms();
					aa.hidden = true;
					v.add(aa);
					selectedAtoms.remove(aa);
					hiddenAtoms.remove(aa);
				}
				for (int j=0; j<a.symDown.size(); j++) {
					Atom aa = (Atom)a.symDown.get(j);
					for (int k=0; k<aa.down.size(); k++) {
						Atom aaa = (Atom)aa.down.get(k);
						root.removeChild(aaa);
						if (aaa.selected) {
							aaa.unSelect();
							watcher.decrementSelAtoms();
						}
						if (!aaa.hidden) watcher.incHiddenAtoms();
						aaa.hidden = true;
						v.add(aaa);
						selectedAtoms.remove(aaa);
						hiddenAtoms.remove(aaa);
					}
					root.removeChild(aa);
					if (aa.selected) {
						aa.unSelect();
						watcher.decrementSelAtoms();
					}
					if (!aa.hidden) watcher.incHiddenAtoms();
					aa.hidden = true;
					v.add(aa);
					selectedAtoms.remove(aa);
					hiddenAtoms.remove(aa);
				}
				root.removeChild(a);
				if (a.selected) {
					a.unSelect();
					watcher.decrementSelAtoms();
				}
				if (!a.hidden) watcher.incHiddenAtoms();
				a.hidden = true;
				v.add(a);
				selectedAtoms.remove(a);
				hiddenAtoms.remove(a);
			}
		}
		hideFloatLinks2(index);
		clearDistLabel();
		//refresh2(); //TODO
		
		watcher.setShowAllEnable(!hiddenAtoms.isEmpty());

	}

	public void unHide(int index) {
		Vector v = (Vector) hiddenAtomGroups.get(index);
		Vector w = (Vector) hiddenLinksGroups.get(index);
		for (int i=0; i<v.size(); i++) {
			Atom a = (Atom)v.get(i);
			root.addChild(a);
			a.hidden = false;
			watcher.decHiddenAtoms();
		}
		v.clear();

		for (int i=0; i<w.size(); i++) {
			AtomLink l = (AtomLink)w.get(i);
			if (!l.a1.hidden && !l.a2.hidden) {
				root.addChild(l);
				l.hiddenIn = null;
			}
		}
		w.clear();
	}
	public void hideFloatLinks2(int index) {
		Vector w = (Vector) hiddenLinksGroups.get(index);
		Vector ll = new Vector(10, 10); 
		gimmeAllLinks(ll, links);
		for (int i=0; i<ll.size(); i++) {
			AtomLink l = (AtomLink)ll.get(i);
			if ((l.a1.atomGroupIndex==index || l.a2.atomGroupIndex==index) && !hiddenLinks.contains(l) && !w.contains(l)) {
				root.removeChild(l);
				w.add(l);
				l.hiddenIn = w;
			}
		}
	}

	
	public void delLinksForAtom(Atom a) {
		for (int i=links.size()-1; i>=0; i--) {
			AtomLink l = (AtomLink)links.get(i);
			if (l.a1==a || l.a2==a)  {
				root.removeChild(l);
				links.remove(i);
			}
			for (int j=l.down.size()-1; j>=0; j--) {
				AtomLink ll = (AtomLink)l.down.get(j);
				if (ll.a1==a || ll.a2==a)  {
					root.removeChild(ll);
					links.remove(j);
				}
			}
		}
	}
	
	
	
	public void selectLink(AtomLink l) {
		if (distance!=null) {
			root.removeChild(distance);
			distance = null;
		}

		if (selectedLink==l) {
			l.unSelect();
			selectedLink = null;
		}
		else {
			if (selectedLink!=null) selectedLink.unSelect();
			selectedLink = l;
			l.select();
			watcher.setCurrBoundSize(l.radius);
			watcher.setCurrBoundColor(l.color.get());
		}
		if (selectedLink!=null) {
			double d = distance(l.a1, l.a2);
			Appearance app = new Appearance();
			app.setMaterial(new Material(l.color, black, l.color, white, 120.0f));
			Point3d p = new Point3d(l.a1.pos);
			Vector3d v = new Vector3d();
			v.sub(l.a2.pos, l.a1.pos);
			v.scale(0.5);
			p.add(v);
			float s = (float)((cell.a+cell.b+cell.c)/3.0/25.0);
			distance = Atom.createLegend("d="+(Math.round(d*100)/100.0)+"Å", cell.coord(p), cell.coord(p), s, app);
			root.addChild(distance);
		}

		watcher.setBondDelEnable(selectedLink!=null);
	}

	
	public void selUnselAtom(Atom a, boolean limitedTo2) {
		clearDistLabel();

		if (selectedAtoms.contains(a)) {
			a.unSelect();
			selectedAtoms.remove(a);
			watcher.unSelectAtomInTable(a.pos);
		}
		else {
			if (limitedTo2 && selectedAtoms.size()>2) {
				clearAtomSelection();
			}
			if (limitedTo2 && selectedAtoms.size()==2) {
				((Atom)selectedAtoms.get(0)).unSelect();
				selectedAtoms.remove(0);
				watcher.unSelectAtomInTable(((Atom)selectedAtoms.get(0)).pos);
			}
			a.select();
			selectedAtoms.add(a);
			watcher.selectAtomInTable(a.pos);
		}
		watcher.setNbAtomSelected(selectedAtoms.size());
	}
	
	public void selectMultipleAtoms(Atom a) {
		clearDistLabel();
		
		if (!selectedAtoms.contains(a)) {
			a.select();
			selectedAtoms.add(a);
			watcher.selectAtomInTable(a.pos);
		}
	}
	
	public void jobsAfterSelAtom() {
		watcher.setBondAddEnable(selectedAtoms.size()==2);
		showDistLabel();
	}
	
	
	public void clearAtomSelection() {
		clearDistLabel();
		for (int i=0; i<selectedAtoms.size(); i++) {
			Atom a = (Atom)selectedAtoms.get(i);
			a.unSelect();
			watcher.unSelectAtomInTable(a.pos);
		}
		selectedAtoms.clear();
		watcher.setBondAddEnable(false);
		//watcher.setNbAtomSelected(0);
	}
	
	
	public void clearDistLabel() {
		if (distance!=null) {
			root.removeChild(distance);
			distance = null;
		}
	}
	
	private void showDistLabel() {
		if (selectedAtoms.size()==2) {
			clearDistLabel();
			double d = distance((Atom)selectedAtoms.get(0), (Atom)selectedAtoms.get(1));
			Appearance app = new Appearance();
			app.setMaterial(new Material(currentColorLink, black, currentColorLink, white, 120.0f));
			Point3d p = new Point3d(((Atom)selectedAtoms.get(0)).pos);
			Vector3d v = new Vector3d();
			v.sub(((Atom)selectedAtoms.get(1)).pos, ((Atom)selectedAtoms.get(0)).pos);
			v.scale(0.5);
			p.add(v);
			float s = (float)((cell.a+cell.b+cell.c)/3.0/25.0);
			distance = Atom.createLegend("d="+(Math.round(d*100)/100.0)+"Å", cell.coord(p), new Point3d(0,0,0), s, app);
			root.addChild(distance);
		}
	}
	
	
	public void removeAtom(Atom a) {

		
		//System.out.println("remove atom");//TODO
/*		
		symmetryDel(a);
		root.removeChild(a);
		atoms.remove(a);
		selectedAtoms.remove(a);
		hiddenAtoms.remove(a);
		clearDistLabel();
		//removeFloatLinks();
		
*/		
		
		if (selectedAtoms.contains(a)) {
			a.unSelect();
			selectedAtoms.remove(a);
		}
		a = modCell(a);
		if (a.symUp!=null) a=a.symUp;
		delLinksForAtom(a);
		atoms.remove(a);
		
		refresh();
		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
		watcher.watchNbAtom(countInCellAtoms(), countTotalAtoms());
	}

	public boolean arePosEquiv(Point3d p1, Point3d p2) {
		return Math.abs(cell.coord(p1).distance(cell.coord(p2))) < 0.1;
	}

	public double distance(Atom a1, Atom a2) {
		return Math.abs(cell.coord(a1.pos).distance(cell.coord(a2.pos)));
	}

	public int countInCellAtoms() {
		return atoms.size();
	}
	
	public int countTotalAtoms() {
		return countTotalAtoms(atoms);
	}
	public int countTotalAtoms(Vector v) {
		int n=v.size();
		for (int i=0; i<v.size(); i++) {
			n+=countTotalAtoms(((Atom)v.get(i)).down);
			n+=countTotalAtoms(((Atom)v.get(i)).symDown);
		}
		return n;
	}

	public int countSymAtoms() {
		int i, n;
		for (n=0,i=0; i<atoms.size(); i++) n+=((Atom)atoms.get(i)).symDown.size(); 
		return n;
	}

	public int countInCellLinks() {
		return links.size();
	}

	public int countTotalLinks() {
		int n=countInCellLinks();
		for (int i=0; i<links.size(); i++)
			for (int j=0; j<((AtomLink)links.get(i)).down.size(); j++)
				n++;
		return n;
	}

	public Atom modCell(Atom a) {
		return a.up==null?a:a.up;
	}

	public Point3d modCell(Point3d p) {
		return new Point3d(p.x<0?(1-((-p.x)%1))%1:p.x%1, p.y<0?(1-((-p.y)%1))%1:p.y%1, p.z<0?(1-((-p.z)%1))%1:p.z%1);	
	}


	public void gimmeAllAtoms(Vector t, Vector v) {
		for (int i=0; i<v.size(); i++) {
			Atom a = (Atom)v.get(i);
			t.add(a);
			gimmeAllAtoms(t, a.down);
			gimmeAllAtoms(t, a.symDown);
		}
	}
	
	public void gimmeAllLinks(Vector t, Vector v) {
		for (int i=0; i<v.size(); i++) {
			AtomLink l = (AtomLink)v.get(i);
			t.add(l);
			gimmeAllLinks(t, l.down);
		}
	}
	
	
	
	public double minDistUp(double prev) {
		double dmax = 1000.0;
		double dalt = dmax;
		Vector v = new Vector(10, 10);
		gimmeAllAtoms(v, atoms);
		for (int i=0; i<v.size(); i++) {
			for (int j=i+1; j<v.size(); j++) {
				Atom a1 = (Atom)v.get(i);
				Atom a2 = (Atom)v.get(j);
				if (a1.hidden || a2.hidden) continue;
				double d = Math.round(distance(a1, a2)*1000d)/1000d;
				if (d>Math.round(prev*1000d)/1000d && d<Math.round(dmax*1000d)/1000d) dmax=d;
				if (d>=Math.round(prev*1000d)/1000d) dalt=d;
			}
		}
		currentDist = dmax>900.0?dalt:dmax;
		currentDist = currentDist>900.0?0.0:currentDist;
		return currentDist;
	}
	
	public double minDistDown(double prev) {
		double dmin = 0.0;
		Vector v = new Vector(10, 10);
		gimmeAllAtoms(v, atoms);
		
		for (int i=0; i<v.size(); i++) {
			for (int j=i+1; j<v.size(); j++) {
				Atom a1 = (Atom)v.get(i);
				Atom a2 = (Atom)v.get(j);
				if (a1.hidden || a2.hidden) continue;
				double d = Math.round(distance((Atom)v.get(i), (Atom)v.get(j))*1000d)/1000d;
				if (d<Math.round(prev*1000d)/1000d && d>Math.round(dmin*1000d)/1000d) dmin=d;
			}
		}
		currentDist=dmin;
		return dmin;
	}
	
	public void boundUnder_soft(double d) {
		currentDist = d;
		Vector v = new Vector(10, 10);
		gimmeAllAtoms(v, atoms);
		
		for (int i=links.size()-1; i>=0; i--) {
			AtomLink l = (AtomLink)links.get(i);
			double dd = Math.round(distance(l.a1, l.a2)*1000d)/1000d;
			//ok1 = l.a1.pos.x<=expX && l.a1.pos.y<=expY && l.a1.pos.z<=expZ;
			//ok2 = l.a2.pos.x<=expX && l.a2.pos.y<=expY && l.a2.pos.z<=expZ;
			if ((dd>d&&!l.userMade) || !isAtomInBounds(l.a1) || !isAtomInBounds(l.a2))  {
				root.removeChild(l);
				links.remove(i);
				if (l.hiddenIn!=null) l.hiddenIn.remove(l);
			}
			for (int j=l.down.size()-1; j>=0; j--) {
				AtomLink ll = (AtomLink)l.down.get(j);
				double ddd = Math.round(distance(ll.a1, ll.a2)*1000d)/1000d;
				//ok1 = ll.a1.pos.x<=expX && ll.a1.pos.y<=expY && ll.a1.pos.z<=expZ;
				//ok2 = ll.a2.pos.x<=expX && ll.a2.pos.y<=expY && ll.a2.pos.z<=expZ;
				if ((ddd>d&&!l.userMade) || !isAtomInBounds(ll.a1) || !isAtomInBounds(ll.a2))  {
					root.removeChild(ll);
					l.down.remove(j);
					if (ll.hiddenIn!=null) ll.hiddenIn.remove(ll);
				}
			}
		}

		
		Atom a1, a2;
		for (int i=0; i<v.size(); i++) {
			for (int j=i+1; j<v.size(); j++) {
				a1 = (Atom)v.get(i);
				a2 = (Atom)v.get(j);
				if (a1.hidden || a2.hidden) continue;
				//ok1 = a1.pos.x<=expX && a1.pos.y<=expY && a1.pos.z<=expZ;
				//ok2 = a2.pos.x<=expX && a2.pos.y<=expY && a2.pos.z<=expZ;
				double dd = Math.round(distance(a1, a2)*1000d)/1000d;
				if (dd<=Math.round(d*1000d)/1000d && isAtomInBounds(a1) && isAtomInBounds(a2)) {
					addLink(a1, a2);
				}
			}
		}
		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
	}

	public void boundUnder_hard(double d) {
		currentDist = d;
		Vector v = new Vector(10, 10);
		gimmeAllAtoms(v, atoms);
		
		for (int i=links.size()-1; i>=0; i--) {
			AtomLink l = (AtomLink)links.get(i);
			double dd = Math.round(distance(l.a1, l.a2)*1000d)/1000d;
			//ok1 = l.a1.pos.x<=expX && l.a1.pos.y<=expY && l.a1.pos.z<=expZ;
			//ok2 = l.a2.pos.x<=expX && l.a2.pos.y<=expY && l.a2.pos.z<=expZ;
			if (dd>d || !isAtomInBounds(l.a1) || !isAtomInBounds(l.a2))  {
				root.removeChild(l);
				links.remove(i);
				if (l.hiddenIn!=null) l.hiddenIn.remove(l);
			}
			for (int j=l.down.size()-1; j>=0; j--) {
				AtomLink ll = (AtomLink)l.down.get(j);
				double ddd = Math.round(distance(ll.a1, ll.a2)*1000d)/1000d;
				//ok1 = ll.a1.pos.x<=expX && ll.a1.pos.y<=expY && ll.a1.pos.z<=expZ;
				//ok2 = ll.a2.pos.x<=expX && ll.a2.pos.y<=expY && ll.a2.pos.z<=expZ;
				if (ddd>d || !isAtomInBounds(ll.a1) || !isAtomInBounds(ll.a2))  {
					root.removeChild(ll);
					l.down.remove(j);
					if (ll.hiddenIn!=null) ll.hiddenIn.remove(ll);
				}
			}
		}

		
		Atom a1, a2;
		for (int i=0; i<v.size(); i++) {
			for (int j=i+1; j<v.size(); j++) {
				a1 = (Atom)v.get(i);
				a2 = (Atom)v.get(j);
				if (a1.hidden || a2.hidden) continue;
				//ok1 = a1.pos.x<=expX && a1.pos.y<=expY && a1.pos.z<=expZ;
				//ok2 = a2.pos.x<=expX && a2.pos.y<=expY && a2.pos.z<=expZ;
				double dd = Math.round(distance(a1, a2)*1000d)/1000d;
				if (dd<=Math.round(d*1000d)/1000d && isAtomInBounds(a1) && isAtomInBounds(a2)) {
					addLink(a1, a2);
				}
			}
		}
		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
	}
	
	public Atom findAtom(Point3d p) {
		return findAtom(atoms, p);
	}
	public Atom findAtom(Vector v, Point3d p) {
		Atom a, aa;
		for (int i=0; i<v.size(); i++) {
			a = (Atom)v.get(i);
			if (arePosEquiv(a.pos, p)) return a;
			aa = findAtom(a.down, p);
			if (aa!=null) return aa;
			aa = findAtom(a.symDown, p);
			if (aa!=null) return aa;
		}
		return null;
	}
	
	public AtomLink findLink(Point3d p1, Point3d p2) {
		return findLink(links, p1, p2);
	}
	public AtomLink findLink(Vector v, Point3d p1, Point3d p2) {
		AtomLink l, ll;
		for (int i=0; i<v.size(); i++) {
			l = (AtomLink)v.get(i);
			if (arePosEquiv(l.a1.pos, p1) && arePosEquiv(l.a2.pos, p2)) return l;
			if (arePosEquiv(l.a1.pos, p2) && arePosEquiv(l.a2.pos, p1)) return l;
			ll = findLink(l.down, p1, p2);
			if (ll!=null) return ll;
		}
		return null;
	}

	public AtomLink addLink(Atom a1, Atom a2) {
		if (findLink(a1.pos, a2.pos)!=null) {
			return null;
		}
		AtomLink l = putLinkInSpace(a1, a2);
		links.add(l);
		expandAdd(l);
		
		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
		return l;
	}
	
	public void removeLink(AtomLink l) {
		if (l.up!=null) l=l.up;
		links.remove(l);
		expandDel(l);
		root.removeChild(l);

		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
	}
	

/*	
	public void setCell(Cell cell) {
		this.cell=cell;
		links.clear();
		refresh();
	}
*/	

	public void setExpand(float x, float y, float z) {
		expX=x; expY=y; expZ=z;
		refresh();
	}

	public Point3f getExpand() {
		return new Point3f (expX, expY, expZ);
	}

	
	
	
	public void refresh() {
		//System.out.println("refresh");  //TODO
		
		//new Throwable().printStackTrace();
		
		
		
		
		
		clearSelectionAndHidden();
		
		root.removeAllChildren();
		
		
		//selectedAtoms.removeAllElements();

/*		
		if (distance!=null) {
			root.removeChild(distance);
			distance = null;
		}
		selectedLink = null;
		selectedAtoms.clear();
*/		
		watcher.setBondDelEnable(selectedLink!=null);
		watcher.setBondAddEnable(selectedAtoms.size()==2);
		
		
		for (int i=0; i<atoms.size(); i++) {
			Atom a = (Atom)atoms.get(i);
			a.reCellPos(cell);
			if (!a.hidden) root.addChild(a);
			else watcher.incHiddenAtoms();
			a.down.clear();
			a.symDown.clear();
			((Vector)hiddenAtomGroups.get(a.atomGroupIndex)).clear();
			if (a.hidden) ((Vector)hiddenAtomGroups.get(a.atomGroupIndex)).add(a); 
			symmetryAdd(a);
		}
		
		
		
		
		
		for (int i=links.size()-1; i>=0;i--) {
			AtomLink l = (AtomLink)links.get(i); 
			//if (l.a1.hidden || l.a2.hidden) continue;
			if (!l.userMade) {
				links.remove(i);
				if (l.hiddenIn!=null) l.hiddenIn.remove(l);
			}
			else {
				if (!l.a1.hidden && !l.a2.hidden)
					root.addChild(l);
				expandAdd(l);
				for (int j=0; j<l.down.size(); j++) ((AtomLink)l.down.get(j)).userMade=true;
			}
			for (int j=0; j<l.down.size(); j++) {
				AtomLink ll = (AtomLink)l.down.get(j);
				try {
					if (!isAtomInBounds(ll.a1) || !isAtomInBounds(ll.a2))	{
						if (ll.hiddenIn!=null) ll.hiddenIn.remove(ll);
						l.down.remove(ll);
					}
					else {
						if (ll.userMade && !l.a1.hidden && !l.a2.hidden) root.addChild(ll); 
					}
				} catch (MultipleParentException e) {};
			}
		}
		
		
		//Vector w = (Vector) hiddenLinksGroups.get(index);
		
		
		//links.clear();

		boundUnder_soft(currentDist);
		
		//System.gc();
		watcher.watchNbAtom(countInCellAtoms(), countTotalAtoms());
		watcher.watchNbLink(countInCellLinks(), countTotalLinks());
	
	}

	
	
	
	public boolean isAtomInBounds(Atom a) {
		return (a.pos.x<=expX && a.pos.x>=(expX>=1?(-expX+1):0) && a.pos.y<=expY && a.pos.y>=(expY>=1?(-expY+1):0) && a.pos.z<=expZ && a.pos.z>=(expZ>=1?(-expZ+1):0));
	}
	
	
	public void symmetryAdd(Atom a) {
		Atom aa;
		Point3d p;
		Vector v = ((Vector)hiddenAtomGroups.get(a.atomGroupIndex));

		float[][] pos;
		if (cell.no==0) pos = CellSymetries.posCustom(cell.m_ccell, (float)a.pos.x, (float)a.pos.y, (float)a.pos.z);
		else pos = CellGen.pos(cell.no, cell.choice, (float)a.pos.x, (float)a.pos.y, (float)a.pos.z);
		
		for (int i=0; i<pos.length; i++) {
			p = modCell(new Point3d(pos[i][0], pos[i][1], pos[i][2]));
			if (findAtom(p)==null) {
				aa = putAtomInSpace(p, a.radius, a.color, a.atomGroupIndex, a.hidden);
				a.symDown.add(aa);
				aa.symUp=a;
				if (aa.hidden) {
					v.add(aa); 
					watcher.incHiddenAtoms();
				}
				expandAdd(aa);
			}
		}
		expandAdd(a);
	}
	
	public void symmetryDel(Atom a) {
		expandDel(modCell(a));

		for (int i=0; i<a.symDown.size(); i++) {
			Atom aa = (Atom)a.symDown.get(i);
			root.removeChild(aa);
			expandDel(aa);
		}
		if (a.symUp!=null) {
			for (int i=0; i<a.symUp.symDown.size(); i++) {
				Atom aa = (Atom)a.symUp.symDown.get(i);
				root.removeChild(aa);
				expandDel(aa);
			}
			a.symUp.symDown.clear();
			root.removeChild(a.symUp);
			expandDel(a.symUp);
		}
		a.symDown.clear();
	}
	
	
	public void expandAdd(Atom a) {
		Atom aa, aaa;

		Vector v = ((Vector)hiddenAtomGroups.get(a.atomGroupIndex));
		for (int i=0; i<atoms.size(); i++) {
			aa = (Atom)atoms.get(i);
			
			//if (aa.pos.x>expX || aa.pos.y>expY || aa.pos.z>expZ)
			if (!isAtomInBounds(aa))
				root.removeChild(aa);
			for (int j=0; j<aa.symDown.size(); j++) {
				aaa = (Atom)aa.symDown.get(j);
				if (!isAtomInBounds(aaa))
				//if (aaa.pos.x>expX || aaa.pos.y>expY || aaa.pos.z>expZ)
					root.removeChild(aaa);
			}
		}
		
		for (double i=-Math.ceil(expX-1); i<=Math.floor(expX+1)+1; i++)
			for (double j=-Math.ceil(expY-1); j<=Math.floor(expY+1)+1; j++)
				for (double k=-Math.ceil(expZ-1); k<=Math.floor(expZ+1)+1; k++)
					if (a.pos.x%1+i<=expX && a.pos.x%1+i>=(expX>=1?(-expX+1):0) && a.pos.y%1+j<=expY && a.pos.y%1+j>=(expY>=1?(-expY+1):0) && a.pos.z%1+k<=expZ && a.pos.z%1+k>=(expZ>=1?(-expZ+1):0))
						if (!(a.pos.x%1+i==a.pos.x && a.pos.y%1+j==a.pos.y && a.pos.z%1+k==a.pos.z)) {
							aa = putAtomInSpace(new Point3d(a.pos.x%1+i, a.pos.y%1+j, a.pos.z%1+k), a.radius, a.color, a.atomGroupIndex, a.hidden);
							a.down.add(aa);
							aa.up=a;
							if (aa.hidden) {
								v.add(aa); 
								watcher.incHiddenAtoms();
							}
						}
	}

	public void expandDel(Atom a) {
		for (int i=0; i<a.down.size(); i++) {
			Atom aa = (Atom)a.down.get(i);
			root.removeChild(aa);
		}
		if (a.up!=null) {
			for (int i=0; i<a.up.down.size(); i++) {
				Atom aa = (Atom)a.up.down.get(i);
				root.removeChild(aa);
			}
			a.up.down.clear();
			root.removeChild(a.up);
		}
		a.down.clear();
	}

	

	private void expandAllLinksForAnAtom(AtomLink l, Atom a) {
		boolean ok3;
		Point3d p = new Point3d(l.a2.pos.x-(l.a1.pos.x-a.pos.x), l.a2.pos.y-(l.a1.pos.y-a.pos.y), l.a2.pos.z-(l.a1.pos.z-a.pos.z));
		Atom aa = findAtom(p);
		if (aa!=null) {
			//ok1 = a.pos.x<=expX && a.pos.y<=expY && a.pos.z<=expZ;
			//ok2 = aa.pos.x<=expX && aa.pos.y<=expY && aa.pos.z<=expZ;
			ok3 = arePosEquiv(modCell(a.pos), modCell(l.a1.pos)) || arePosEquiv(modCell(a.pos), modCell(l.a2.pos)) || arePosEquiv(modCell(aa.pos), modCell(l.a1.pos)) || arePosEquiv(modCell(aa.pos), modCell(l.a2.pos)); 
			if (findLink(a.pos, aa.pos)==null && isAtomInBounds(a) && isAtomInBounds(aa) && ok3) {
				if (!a.hidden && !aa.hidden) {
					AtomLink ll = putLinkInSpace(a, aa, l.radius, l.color);
					l.down.add(ll);
					ll.up=l;
				}
			}
		}
	}
	

	public void expandAdd(AtomLink l) {
		expandAdd(atoms, l);
	}
	public void expandAdd(Vector v, AtomLink l) {
		Atom a;
		for (int i=0; i<v.size(); i++) {
			a = (Atom)v.get(i);
			expandAllLinksForAnAtom(l, a);
			expandAdd(a.down, l);
			expandAdd(a.symDown, l);
		}
	}
	
			
		
	public void expandDel(AtomLink l) {
		for (int i=0; i<l.down.size(); i++) 
			root.removeChild((AtomLink)l.down.get(i));
		if (l.up!=null) {
			for (int i=0; i<l.up.down.size(); i++)
				root.removeChild((AtomLink)l.up.down.get(i));
			l.up.down.clear();
			root.removeChild(l.up);
		}
		l.down.clear();
	}
}

