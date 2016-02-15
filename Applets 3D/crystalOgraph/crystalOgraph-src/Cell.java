import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;

/*
 * Created on 23 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class Cell {
	// public static final Cell defaultCell = new Cell(1, 0, 5, 5, 5, 90, 90,
	// 90);
	// public static Cell currentCell;

	private Vector3d x, y, z;
	public Matrix3d matrix;
	Vector3d g;
	public int no, choice;
	public double a, b, c, alpha, beta, gamma;

	public String m_sgName;
	public int m_sgNo;
	public CellSymetries.CustomCell m_ccell;

	public static Cell defaultCell() {
		return new Cell(1, 0, 5, 5, 5, 90, 90, 90);
	}

	public void setDefault() {
		this.set(1, 0, 5, 5, 5, 90, 90, 90);
	}

	public Cell() {
		this.setDefault();
	}

	public Cell(Cell c) {
		this.set(c.no, c.choice, c.a, c.b, c.c, c.alpha, c.beta, c.gamma);
	}

	public Cell(int no, int choice, double a, double b, double c, double alpha, double beta, double gamma) {
		this.set(no, choice, a, b, c, alpha, beta, gamma);
	}

	@Override
	public String toString() {
		return "n:" + this.no + " x:" + Atom.posToString(this.x) + " y:" + Atom.posToString(this.y) + " z:"
				+ Atom.posToString(this.z);
	}

	public void set(Cell c) {
		this.set(c.no, c.choice, c.a, c.b, c.c, c.alpha, c.beta, c.gamma);
	}

	public void set(int no, int choice) {
		this.set(no, choice, this.a, this.b, this.c, this.alpha, this.beta, this.gamma);
	}

	public void set(double a, double b, double c, double alpha, double beta, double gamma) {
		this.set(this.no, this.choice, a, b, c, alpha, beta, gamma);
	}

	public void set(int no, int choice, double a, double b, double c, double alpha, double beta, double gamma) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.no = no;
		this.choice = choice;

		double y0 = b * this.cos(gamma);
		double y1 = b
				* Math.sqrt(1d - this.cos(alpha) * this.cos(alpha) - this.cos(beta) * this.cos(beta)
						- this.cos(gamma) * this.cos(gamma) + 2d * this.cos(alpha) * this.cos(beta) * this.cos(gamma))
				/ this.sin(beta);
		double y2 = b * (this.cos(alpha) - this.cos(beta) * this.cos(gamma)) / this.sin(beta);

		this.x = new Vector3d(a, 0, 0);
		this.y = new Vector3d(y0, y1, y2);
		this.z = new Vector3d(c * this.cos(beta), 0, c * this.sin(beta));

		this.g = new Vector3d();
		this.g.add(this.x, this.y);
		this.g.add(this.z);
		this.g.scale(.5);

		this.matrix = new Matrix3d();
		this.matrix.setColumn(0, this.x);
		this.matrix.setColumn(1, this.y);
		this.matrix.setColumn(2, this.z);
	}

	private double sin(double a) {
		return Math.sin(a * Math.PI / 180d);
	}

	private double cos(double a) {
		return Math.cos(a * Math.PI / 180d);
	}

	public Point3d coord(Tuple3d v) {
		return this.coord(v.x, v.y, v.z);
	}

	public Point3d coord(Tuple3f v) {
		return this.coord(v.x, v.y, v.z);
	}

	public Point3d coord(double i, double j, double k) {
		Point3d p = new Point3d(i, j, k);
		this.matrix.transform(p);
		p.sub(this.g);
		return p;
	}
}
