package sg;
import javax.vecmath.Matrix3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class Lattice {
	public double a, b, c;
	public double alpha, beta, gamma;		// in degrees !!
	public Vector3d x, y, z;
	public Matrix3d m;
	public Vector3d g;
	
	public Lattice() {
		setDefault();
	}
	
	public Lattice(double a, double b, double c, double alpha, double beta, double gamma) {
		set(a, b, c, alpha, beta, gamma);
	}
	
	public void setDefault() {
		set(5, 5, 5, 90, 90, 90);
	}

	public Lattice(Lattice l) {
		set(l.a, l.b, l.c, l.alpha, l.beta, l.gamma);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (a==b && b==c) sb.append("a,b,c="+a);
		else if (a==b) sb.append("a,b="+a+" c="+c);
		else if (a==c) sb.append("a,c="+a+" b="+b);
		else if (b==c) sb.append("a="+a+" b,c="+b);
		else sb.append("a="+a+" b="+b+" c="+c);
		
		if (alpha!=90) {
			sb.append(" \u03b1");
			if (beta==alpha) sb.append(",\u03b2");
			if (gamma==alpha) sb.append(",\u03b3");
			sb.append("="+(int)alpha);
		}
		if (beta!=90 && beta!=alpha) {
			sb.append(" \u03b2");
			if (gamma==beta) sb.append(",\u03b3");
			sb.append("="+(int)beta);
		}
		if (gamma!=90 && gamma!=alpha && gamma!=beta) {
			sb.append(" \u03b3");
			sb.append("="+(int)gamma);
		}
		return sb.toString();
		//return "a="+a+" b="+b+" c="+c+" alpha="+alpha+" beta="+beta+" gamma="+gamma;
	}

	public void set(double a, double b, double c, double alpha, double beta, double gamma) {
		this.a=a; this.b=b; this.c=c;
		this.alpha=alpha; this.beta=beta; this.gamma=gamma;
		update();
	}
	
	public void applyConstraints(SgSystem sys) {
		if (!sys.bFree) b = a;
		if (!sys.cFree) c = a;
		if (sys.alpha!=SgSystem.FREE) alpha = sys.alpha;
		if (sys.beta==SgSystem.LIKE_ALPHA) beta = sys.alpha;
		if (sys.gamma==SgSystem.LIKE_ALPHA) gamma = sys.alpha;
		if (sys.beta!=SgSystem.LIKE_ALPHA && sys.beta!=SgSystem.FREE) beta = sys.beta;
		if (sys.gamma!=SgSystem.LIKE_ALPHA && sys.gamma!=SgSystem.FREE) gamma = sys.gamma;
		update();
	}

	public void update() {
		double y0 = b*cos(gamma);
		double y1 = b*Math.sqrt(1d-cos(alpha)*cos(alpha)-cos(beta)*cos(beta)-cos(gamma)*cos(gamma)+2d*cos(alpha)*cos(beta)*cos(gamma))/sin(beta);
		double y2 = b*(cos(alpha)-cos(beta)*cos(gamma))/sin(beta);
		
		x = new Vector3d(a, 0, 0);
		y = new Vector3d(y0, y1, y2);
		z = new Vector3d(c*cos(beta), 0, c*sin(beta));
		
		g = new Vector3d(); 
		g.add(x, y);
		g.add(z);
		g.scale(.5);
		
		m = new Matrix3d();
		m.setColumn(0, x);
		m.setColumn(1, y);
		m.setColumn(2, z);
	}
	
	
	public double v() {
		return a*b*c*Math.sqrt(1-cos2(alpha)-cos2(beta)-cos2(gamma)+2*cos(alpha)*cos(beta)*cos(gamma));
	}
	
	public Lattice reciprocal() {
		double a0 = b*c*sin(alpha)/v();
		double b0 = a*c*sin(beta)/v();
		double c0 = a*b*sin(gamma)/v();
		double alpha0 = Math.acos((cos(beta)*cos(gamma)-cos(alpha))/sin(beta)*sin(gamma))*180.0/Math.PI;
		double beta0 = Math.acos((cos(gamma)*cos(alpha)-cos(beta))/sin(gamma)*sin(alpha))*180.0/Math.PI;
		double gamma0 = Math.acos((cos(alpha)*cos(beta)-cos(gamma))/sin(alpha)*sin(beta))*180.0/Math.PI;
		Lattice r = new Lattice(a0, b0, c0, alpha0, beta0, gamma0);
		r.x.cross(y, z);
		r.y.cross(z, x);
		r.z.cross(x, y);
		r.x.normalize();
		r.y.normalize();
		r.z.normalize();
		r.x.scale(a0);
		r.y.scale(b0);
		r.z.scale(c0);
		r.g.add(r.x, r.y);
		r.g.add(r.z);
		r.g.scale(.5);
		r.m = new Matrix3d();
		r.m.setColumn(0, r.x);
		r.m.setColumn(1, r.y);
		r.m.setColumn(2, r.z);
		
		return r;
	}

	public static Vector3d[] reciprocal(Vector3d x, Vector3d y, Vector3d z) {
		Vector3d[] r = new Vector3d[3];
		r[0] = new Vector3d();
		r[1] = new Vector3d();
		r[2] = new Vector3d();
		r[0].cross(y, z);
		r[1].cross(z, x);
		r[2].cross(x, y);
		r[0].normalize();
		r[1].normalize();
		r[2].normalize();
		r[0].scale(x.length());
		r[1].scale(y.length());
		r[2].scale(z.length());
		return r;
	}
	
	private static double sin(double a) {		// arguments in degrees !!
		return Math.sin(a*Math.PI/180d);
	}
	private static double cos(double a) {
		return Math.cos(a*Math.PI/180d);
	}
	private static double sin2(double a) {
		return Math.pow(sin(a), 2);
	}
	private static double cos2(double a) {
		return Math.pow(cos(a), 2);
	}

	public void transform(Tuple3d p) {
		m.transform(p);
		p.sub(g);
	}
}
