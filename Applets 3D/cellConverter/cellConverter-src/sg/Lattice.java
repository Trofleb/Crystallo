package sg;

import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Lattice {
	public double a, b, c;
	public double alpha, beta, gamma; // in degrees !!
	public Vector3d x, y, z;
	public Matrix3d m;
	public Vector3d o;

	public Lattice() {
		this(5, 5, 5, 90, 90, 90);
	}

	public Lattice(double a, double b, double c, double alpha, double beta, double gamma) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		double y1 = b * cos(gamma);
		double y2 = b * Math.sqrt(1d - cos(alpha) * cos(alpha) - cos(beta) * cos(beta) - cos(gamma) * cos(gamma)
				+ 2d * cos(alpha) * cos(beta) * cos(gamma)) / sin(beta);
		double y3 = b * (cos(alpha) - cos(beta) * cos(gamma)) / sin(beta);
		this.x = new Vector3d(a, 0, 0);
		this.y = new Vector3d(y1, y2, y3);
		this.z = new Vector3d(c * cos(beta), 0, c * sin(beta));
		this.o = new Vector3d();
		this.o.add(this.x, this.y);
		this.o.add(this.z);
		this.o.scale(-.5);
		this.m = new Matrix3d();
		this.m.setColumn(0, this.x);
		this.m.setColumn(1, this.y);
		this.m.setColumn(2, this.z);
	}

	public Lattice(Vector3d x, Vector3d y, Vector3d z, Vector3d o) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.o = o;
		this.alpha = y.angle(z) * 180d / Math.PI;
		this.beta = x.angle(z) * 180d / Math.PI;
		this.gamma = x.angle(y) * 180d / Math.PI;
		this.a = x.length();
		this.b = y.length();
		this.c = z.length();
		this.m = new Matrix3d();
		this.m.setColumn(0, x);
		this.m.setColumn(1, y);
		this.m.setColumn(2, z);
	}

	public Lattice(Vector3d x, Vector3d y, Vector3d z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.alpha = y.angle(z) * 180d / Math.PI;
		this.beta = x.angle(z) * 180d / Math.PI;
		this.gamma = x.angle(y) * 180d / Math.PI;
		this.a = x.length();
		this.b = y.length();
		this.c = z.length();
		this.o = new Vector3d();
		this.o.add(x, y);
		this.o.add(z);
		this.o.scale(-.5);
		this.m = new Matrix3d();
		this.m.setColumn(0, x);
		this.m.setColumn(1, y);
		this.m.setColumn(2, z);
	}

	public Lattice(Lattice l) {
		this(l.x, l.y, l.z, l.o);
	}

	public Lattice(double a, double b, double c, double alpha, double beta, double gamma, int u, int v, int w) {
		this(a, b, c, alpha, beta, gamma);
		this.setOrientation(u, v, w);
	}

	public void setOrientation(int u, int v, int w) {
		Vector3d e2 = new Vector3d(0, 1, 0);
		Vector3d p = new Vector3d(u * this.x.x + v * this.y.x + w * this.z.x,
				u * this.x.y + v * this.y.y + w * this.z.y, u * this.x.z + v * this.y.z + w * this.z.z);
		double angle = p.angle(e2);
		Vector3d n = new Vector3d();
		n.cross(e2, p);
		Matrix3d r = rotationMatrix(angle, n);
		Transform3D t3d = new Transform3D();
		t3d.set(r);

		t3d.transform(this.x);
		t3d.transform(this.y);
		t3d.transform(this.z);

		this.o = new Vector3d();
		this.o.add(this.x, this.y);
		this.o.add(this.z);
		this.o.scale(-.5);
		this.m = new Matrix3d();
		this.m.setColumn(0, this.x);
		this.m.setColumn(1, this.y);
		this.m.setColumn(2, this.z);
	}

	public static Matrix3d rotationMatrix(double angle, Vector3d v) {
		double c = Math.cos(angle);
		double s = Math.sin(angle);
		double t = 1 - c;
		double n = v.length();
		double vx = v.x / n;
		double vy = v.y / n;
		double vz = v.z / n;
		Matrix3d m = new Matrix3d();
		m.m00 = t * vx * vx + c;
		m.m01 = t * vy * vx + s * vz;
		m.m02 = t * vz * vx - s * vy;
		m.m10 = t * vx * vy - s * vz;
		m.m11 = t * vy * vy + c;
		m.m12 = t * vz * vy + s * vx;
		m.m20 = t * vx * vz + s * vy;
		m.m21 = t * vy * vz - s * vx;
		m.m22 = t * vz * vz + c;
		return m;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.a == this.b && this.b == this.c)
			sb.append("a,b,c=" + this.a);
		else if (this.a == this.b)
			sb.append("a,b=" + this.a + " c=" + this.c);
		else if (this.a == this.c)
			sb.append("a,c=" + this.a + " b=" + this.b);
		else if (this.b == this.c)
			sb.append("a=" + this.a + " b,c=" + this.b);
		else
			sb.append("a=" + this.a + " b=" + this.b + " c=" + this.c);

		if (this.alpha != 90) {
			sb.append(" \u03b1");
			if (this.beta == this.alpha)
				sb.append(",\u03b2");
			if (this.gamma == this.alpha)
				sb.append(",\u03b3");
			sb.append("=" + (int) this.alpha);
		}
		if (this.beta != 90 && this.beta != this.alpha) {
			sb.append(" \u03b2");
			if (this.gamma == this.beta)
				sb.append(",\u03b3");
			sb.append("=" + (int) this.beta);
		}
		if (this.gamma != 90 && this.gamma != this.alpha && this.gamma != this.beta) {
			sb.append(" \u03b3");
			sb.append("=" + (int) this.gamma);
		}
		return sb.toString();
		// return "a="+a+" b="+b+" c="+c+" alpha="+alpha+" beta="+beta+"
		// gamma="+gamma;
	}

	public double volume() {
		return this.a * this.b * this.c * Math.sqrt(1 - cos2(this.alpha) - cos2(this.beta) - cos2(this.gamma)
				+ 2 * cos(this.alpha) * cos(this.beta) * cos(this.gamma));
	}

	public static double volume(Vector3d x, Vector3d y, Vector3d z) {
		Vector3d v = new Vector3d();
		v.cross(y, z);
		return v.dot(x);
	}

	public Lattice reciprocal() {
		Vector3d[] vv = reciprocal(this.x, this.y, this.z);
		return new Lattice(vv[0], vv[1], vv[2]);
	}

	public static Vector3d[] reciprocal(Vector3d x, Vector3d y, Vector3d z) {
		double iv = 1d / volume(x, y, z);
		Vector3d[] r = new Vector3d[3];
		r[0] = new Vector3d();
		r[1] = new Vector3d();
		r[2] = new Vector3d();
		r[0].cross(y, z);
		r[1].cross(z, x);
		r[2].cross(x, y);
		r[0].scale(iv);
		r[1].scale(iv);
		r[2].scale(iv);
		return r;
	}

	private static double sin(double a) { // arguments in degrees !!
		return Math.sin(a * Math.PI / 180d);
	}

	private static double cos(double a) {
		return Math.cos(a * Math.PI / 180d);
	}

	private static double cos2(double a) {
		return Math.pow(cos(a), 2);
	}

	public static Vector3d round(Vector3d p) {
		if (Double.isNaN(p.x) || Double.isInfinite(p.x) || Double.isNaN(p.y) || Double.isInfinite(p.y)
				|| Double.isNaN(p.z) || Double.isInfinite(p.z))
			return p;
		return new Vector3d(Math.round(1000 * p.x) / 1000d, Math.round(1000 * p.y) / 1000d,
				Math.round(1000 * p.z) / 1000d);
	}

	public void transform(Tuple3d p) {
		this.m.transform(p);
		p.add(this.o);
	}

	public void reverse(Tuple3d p) {
		p.sub(this.o);
		Matrix3d n = new Matrix3d(this.m);
		n.invert();
		n.transform(p);
	}
}
