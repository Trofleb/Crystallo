import java.awt.GridLayout;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;


/*
 * Created on 23 juin 2004
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

public class Cell {
	//public static final Cell defaultCell = new Cell(1, 0, 5, 5, 5, 90, 90, 90); 
	//public static Cell currentCell; 

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
		set(1, 0, 5, 5, 5, 90, 90, 90);
	}
	
	public Cell() {
		setDefault();
	}
	
	public Cell(Cell c) {
		set(c.no, c.choice, c.a, c.b, c.c, c.alpha, c.beta, c.gamma);
	}
	
	public Cell(int no, int choice, double a, double b, double c, double alpha, double beta, double gamma) {
		set(no, choice, a, b, c, alpha, beta, gamma);
	}
	
	public String toString() {
		return "n:"+no+" x:"+Atom.posToString(x)+" y:"+Atom.posToString(y)+" z:"+Atom.posToString(z);
	}

	
	public void set(Cell c) {
		set(c.no, c.choice, c.a, c.b, c.c, c.alpha, c.beta, c.gamma);
	}
	
	public void set(int no, int choice) {
		set(no, choice, a, b, c, alpha, beta, gamma);
	}
	public void set(double a, double b, double c, double alpha, double beta, double gamma) {
		set(no, choice, a, b, c, alpha, beta, gamma);
	}
	
	public void set(int no, int choice, double a, double b, double c, double alpha, double beta, double gamma) {
		this.a=a; this.b=b; this.c=c;
		this.alpha=alpha; this.beta=beta; this.gamma=gamma;
		this.no=no; this.choice=choice;

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
		
		matrix = new Matrix3d();
		matrix.setColumn(0, x);
		matrix.setColumn(1, y);
		matrix.setColumn(2, z);
	}

	
	private double sin(double a) {
		return Math.sin(a*Math.PI/180d);
	}
	private double cos(double a) {
		return Math.cos(a*Math.PI/180d);
	}
	
	public Point3d coord(Tuple3d v) {
		return coord(v.x, v.y, v.z);
	}
	public Point3d coord(Tuple3f v) {
		return coord(v.x, v.y, v.z);
	}
	public Point3d coord(double i, double j, double k) {
		Point3d p = new Point3d(i, j, k);
		matrix.transform(p);
		p.sub(g);
		return p;
	}
}
