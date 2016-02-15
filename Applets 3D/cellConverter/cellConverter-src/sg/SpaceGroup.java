/*
 * crystalOgraph2 - SpaceGroup.java
 * Author : Nicolas Schoeni
 * Creation : 4 avr. 2005
 * nicolas.schoeni@epfl.ch
 */
package sg;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class SpaceGroup {
	public Lattice cell;
	public SgType sg;
	public SgSystem system;

	public SpaceGroup(SgType sg, Lattice cell) {
		this.sg = sg;
		this.cell = cell;
		this.system = SgSystem.getSystem(sg.dim, sg.systemIndex, sg.systemVariantIndex);
	}

	public Point2D.Double[] getSymPos(Point2D.Double p) { // not uniques !!
		Point2D.Double[] r = new Point2D.Double[this.sg.symPos.length];
		for (int i = 0; i < r.length; i++)
			r[i] = modCell(this.sg.symPos[i].mul(p));
		return r;
	}

	public Point3d[] getSymPos(Point3d p) { // unique positions (may be slow..)
		Point3d[] r = new Point3d[this.sg.symPos.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = modCell(this.sg.symPos[i].mul(p));
			Point3d pi = new Point3d(r[i]);
			this.cell.transform(pi);
			for (int j = 0; j < i; j++) {
				if (r[j] == null)
					continue;
				Point3d pj = new Point3d(r[j]);
				this.cell.transform(pj);
				if (this.arePosEquals(pi, pj)) {
					r[i] = null;
					break;
				}
			}
		}
		return r;
	}

	public Vector3d[] getSymPos(Vector3d p) { // unique positions (may be
												// slow..)
		Vector3d[] r = new Vector3d[this.sg.symPos.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = modCell(this.sg.symPos[i].mul(p));
			Point3d pi = new Point3d(r[i]);
			this.cell.transform(pi);
			for (int j = 0; j < i; j++) {
				if (r[j] == null)
					continue;
				Point3d pj = new Point3d(r[j]);
				this.cell.transform(pj);
				if (this.arePosEquals(pi, pj)) {
					r[i] = null;
					break;
				}
			}
		}
		return r;
	}

	private static final double epsilon = 0.5; // Angstrom

	public boolean arePosEquals(Point3d p1, Point3d p2) {
		return p1.distance(p2) < epsilon;
	}

	public static Point3d modCell(Point3d p) {
		return new Point3d(p.x < 0 ? (1 - ((-p.x) % 1)) % 1 : p.x % 1, p.y < 0 ? (1 - ((-p.y) % 1)) % 1 : p.y % 1,
				p.z < 0 ? (1 - ((-p.z) % 1)) % 1 : p.z % 1);
	}

	public static Vector3d modCell(Vector3d p) {
		return new Vector3d(p.x < 0 ? (1 - ((-p.x) % 1)) % 1 : p.x % 1, p.y < 0 ? (1 - ((-p.y) % 1)) % 1 : p.y % 1,
				p.z < 0 ? (1 - ((-p.z) % 1)) % 1 : p.z % 1);
	}

	public static Point2D.Double modCell(Point2D.Double p) {
		return new Point2D.Double(p.x < 0 ? (1 - ((-p.x) % 1)) % 1 : p.x % 1,
				p.y < 0 ? (1 - ((-p.y) % 1)) % 1 : p.y % 1);
	}

	public static void main(String[] args) {
		SgSystem.main(args);
		SgType.main(args);
	}
}
