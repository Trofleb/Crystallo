/*
 * crystalOgraph2 - MatVect.java
 * Author : Nicolas Schoeni
 * Creation : 24 mai 2005
 * nicolas.schoeni@epfl.ch
 */
package utils;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Vector;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class MatVect implements Serializable {
	public int[][] m;
	public Fraq[] v;

	public MatVect(int[][] m, Fraq[] v) {
		this.m = m;
		this.v = v;
	}

	public MatVect extractPart(int dim) {
		int[][] m2 = new int[dim][dim];
		Fraq[] v2 = new Fraq[dim];
		for (int i = 0; i < dim; i++) {
			v2[i] = new Fraq(this.v[i].n, this.v[i].d);
			for (int j = 0; j < dim; j++)
				m2[i][j] = this.m[i][j];
		}
		return new MatVect(m2, v2);
	}

	public boolean equals(Object o) {
		if (!(o instanceof MatVect))
			return false;
		MatVect mv = (MatVect) o;
		if (mv.m.length != this.m.length)
			return false;
		if (mv.v.length != this.v.length)
			return false;
		for (int i = 0; i < this.m.length; i++) {
			if (mv.m[i].length != this.m[i].length)
				return false;
			for (int j = 0; j < this.m[i].length; j++)
				if (mv.m[i][j] != this.m[i][j])
					return false;
		}
		for (int i = 0; i < this.v.length; i++)
			if (!mv.v[i].equals(this.v[i]))
				return false;
		return true;
	}

	public void add(Fraq[] u) {
		if (u.length != this.v.length)
			throw new RuntimeException("lengths don't match");
		for (int i = 0; i < this.v.length; i++)
			this.v[i].add(u[i]);
	}

	public double[] mul(double[] p) {
		double[] r = new double[p.length];
		if (p.length != this.v.length)
			throw new RuntimeException("lengths don't match");
		for (int i = 0; i < this.v.length; i++) {
			r[i] = this.v[i].toDouble();
			for (int j = 0; j < this.v.length; j++)
				r[i] += p[j] * this.m[i][j];
		}
		return r;
	}

	public Point3d mul(Point3d p) {
		Point3d r = new Point3d();
		if (this.v.length != 3)
			throw new RuntimeException("lengths don't match");
		r.x = this.v[0].toDouble() + p.x * this.m[0][0] + p.y * this.m[0][1] + p.z * this.m[0][2];
		r.y = this.v[1].toDouble() + p.x * this.m[1][0] + p.y * this.m[1][1] + p.z * this.m[1][2];
		r.z = this.v[2].toDouble() + p.x * this.m[2][0] + p.y * this.m[2][1] + p.z * this.m[2][2];
		return r;
	}

	public Vector3d mul(Vector3d p) {
		Vector3d r = new Vector3d();
		if (this.v.length != 3)
			throw new RuntimeException("lengths don't match");
		r.x = this.v[0].toDouble() + p.x * this.m[0][0] + p.y * this.m[0][1] + p.z * this.m[0][2];
		r.y = this.v[1].toDouble() + p.x * this.m[1][0] + p.y * this.m[1][1] + p.z * this.m[1][2];
		r.z = this.v[2].toDouble() + p.x * this.m[2][0] + p.y * this.m[2][1] + p.z * this.m[2][2];
		return r;
	}

	public Point2D.Double mul(Point2D.Double p) {
		Point2D.Double r = new Point2D.Double();
		if (this.v.length != 2)
			throw new RuntimeException("lengths don't match");
		r.x = this.v[0].toDouble() + p.x * this.m[0][0] + p.y * this.m[0][1];
		r.y = this.v[1].toDouble() + p.x * this.m[1][0] + p.y * this.m[1][1];
		return r;
	}

	public static MatVect[] multiParse(String s) {
		String[] ss = s.split(";");
		MatVect[] mm = new MatVect[ss.length];
		for (int i = 0; i < ss.length; i++)
			mm[i] = new MatVect(ss[i]);
		return mm;
	}

	public static boolean multiEquals(MatVect[] m1, MatVect[] m2) {
		if (m1.length != m2.length)
			return false;
		boolean[] bb = new boolean[m2.length];
		fl: for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2.length; j++)
				if (m1[i].equals(m2[j]) && bb[j] == false) {
					bb[j] = true;
					continue fl;
				}
			return false;
		}
		return true;
	}

	public MatVect(String s) {
		String[] ss = s.replaceAll(" ", "").split(",");

		this.m = new int[ss.length][ss.length];
		this.v = new Fraq[ss.length];

		for (int j = 0; j < ss.length; j++) {
			this.v[j] = new Fraq(0, 1);
			String[] ff = splitEq(ss[j].trim());
			for (int k = 0; k < ff.length; k++) {
				int val;
				ff[k] = ff[k].replaceAll(" ", "");
				switch (ff[k].charAt(0)) {
					case '+':
						val = 1;
						break;
					case '-':
						val = -1;
						break;
					default:
						throw new RuntimeException("\"" + ss[j] + "\"");
				}
				String r = ff[k].toLowerCase().substring(1);

				if (r.indexOf("x2") != -1 || r.indexOf("y") != -1) {
					if (Character.isDigit(r.charAt(0)))
						val *= (r.charAt(0) - '0');
					this.m[j][1] = val;
				} else if (r.indexOf("x3") != -1 || r.indexOf("z") != -1) {
					if (Character.isDigit(r.charAt(0)))
						val *= (r.charAt(0) - '0');
					this.m[j][2] = val;
				} else if (r.indexOf("x4") != -1 || r.indexOf("t") != -1) {
					if (Character.isDigit(r.charAt(0)))
						val *= (r.charAt(0) - '0');
					this.m[j][3] = val;
				} else if (r.indexOf("x1") != -1 || r.indexOf("x") != -1) {
					if (Character.isDigit(r.charAt(0)))
						val *= (r.charAt(0) - '0');
					this.m[j][0] = val;
				} else {
					int f = r.indexOf('/');
					if (f == -1)
						this.v[j] = new Fraq(Integer.parseInt(r) * val, 1);
					else
						this.v[j] = new Fraq(Integer.parseInt(r.substring(0, f)) * val,
								Integer.parseInt(r.substring(f + 1)));
				}
			}
		}
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < this.m.length; i++) {
			for (int j = 0; j < this.m[i].length; j++) {
				if (j != 0)
					s += " ";
				s += ((this.m[i][j] >= 0 ? " " : "") + this.m[i][j]);
			}
			s += (" | " + this.v[i]);
			s += "\n";
		}
		return s;
	}

	static final String[] vars = { "x", "y", "z", "u" };

	public String toEq() {
		String s = "";
		for (int i = 0; i < this.m.length; i++) {
			if (i != 0)
				s += ",";
			for (int j = 0; j < this.m[i].length; j++)
				if (this.m[i][j] == 0) {
				} else if (this.m[i][j] == 1) {
					if (s.length() != 0 && s.charAt(s.length() - 1) != ',')
						s += "+";
					s += vars[j];
				} else if (this.m[i][j] == -1) {
					s += "-";
					s += vars[j];
				} else if (this.m[i][j] > 1) {
					if (s.length() != 0 && s.charAt(s.length() - 1) != ',')
						s += "+";
					s += this.m[i][j];
					s += "*";
					s += vars[j];
				} else {
					s += this.m[i][j];
					s += "*";
					s += vars[j];
				}
			String f = this.v[i].toString();
			if (f.equals("0")) {
			} else {
				if (this.v[i].n > 0 && s.length() != 0 && s.charAt(s.length() - 1) != ',')
					s += "+";
				s += f;
			}
		}
		return s;
	}

	public static String toEq(MatVect[] mv) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mv.length; i++) {
			if (i != 0)
				sb.append(";");
			sb.append(mv[i].toEq());
		}
		return sb.toString();
	}

	static String[] splitEq(String s) {
		Vector v = new Vector(3, 3);
		if ("+-".indexOf(s.charAt(0)) == -1)
			s = "+" + s;
		for (int last = 0, i = 0; i <= s.length(); i++)
			if (i == s.length() || ("+-".indexOf(s.charAt(i)) != -1 && i > 0)) {
				v.add(s.substring(last, i));
				last = i;
			}
		return (String[]) v.toArray(new String[0]);
	}

	static class Fraq implements Serializable {
		public int n, d;

		public Fraq(int n, int d) {
			this.n = n;
			this.d = d;
		}

		public Fraq(String s) {
			int sgn = 1;
			if (s.charAt(0) == '-') {
				sgn = -1;
				s = s.substring(1);
			}
			if (s.charAt(0) == '+') {
				sgn = 1;
				s = s.substring(1);
			}
			int f = s.indexOf('/');
			if (f == -1) {
				this.n = Integer.parseInt(s) * sgn;
				this.d = 1;
			} else {
				this.n = Integer.parseInt(s.substring(0, f)) * sgn;
				this.d = Integer.parseInt(s.substring(f + 1));
			}
		}

		public boolean equals(Object o) {
			if (!(o instanceof Fraq))
				return false;
			return ((Fraq) o).n == this.n && ((Fraq) o).d == this.d;
		}

		public void add(Fraq f) {
			this.n = f.d * this.n + this.d * f.n;
			this.d = f.d * this.d;
			this.reduce();
		}

		public void reduce() {
			this.n = this.n % this.d; // modulo 1
			int[] pp = { 2, 3, 5, 7, 11, 13, 17 }; // etc..
			for (int i = 0; i < pp.length; i++)
				if (this.n % pp[i] == 0 && this.d % pp[i] == 0) {
					this.n /= pp[i];
					this.d /= pp[i];
					i--; // retry with the same one
				}
		}

		public String toString() {
			if (this.n == 0)
				return "0";
			if (this.d == 1)
				return "" + this.n;
			return this.n + "/" + this.d;
		}

		public double toDouble() {
			return ((double) this.n) / this.d;
		}
	}
}
