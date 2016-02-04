import java.util.Vector;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import sg.Lattice;
import sg.SgType;
import sg.SpaceGroup;
import structures.AtomSite;

public class Transformer {
	private SpaceGroup sgIn;
	private SpaceGroup sgOut;
	private Lattice latticeOut;
	private SgType sgP1;
	private AtomSite[] atomsIn;
	private AtomSite[] atomsOut;
	private AtomSite[] atomsP1;
	private Matrix3d P, Q;
	private Vector3d q;

	public Transformer(Matrix3d P, Matrix3d Q, Vector3d q) {
		this.P=P; this.Q=Q; this.q=q;
		sgP1 = SgType.getSg(1);
		sgOut = new SpaceGroup(sgP1, latticeOut);
	}

	public void setTranform(Matrix3d P, Matrix3d Q, Vector3d q) {
		this.P=P; this.Q=Q; this.q=q;
		calculateLatticeOut();
		calculateAtomsOut();
	}

	public void setFile(SpaceGroup sg, AtomSite[] atoms) {
		sgIn = sg;
		atomsIn = atoms;
		transformToP1();
		// warning: setFile must be followed by setTransform !
		//calculateLatticeOut();	
		//calculateAtomsOut();
	}

	public AtomSite[] getTransformedAtoms() {
		return atomsOut;
	}
	public SpaceGroup getTransformedSg() {
		sgOut.cell = latticeOut;
		return sgOut;
	}

	private void calculateLatticeOut() {
		Matrix3d m = new Matrix3d();
		m.setColumn(0, sgIn.cell.x);
		m.setColumn(1, sgIn.cell.y);
		m.setColumn(2, sgIn.cell.z);
		m.mul(P);
		Vector3d x = new Vector3d();
		Vector3d y = new Vector3d();
		Vector3d z = new Vector3d();
		m.getColumn(0, x);
		m.getColumn(1, y);
		m.getColumn(2, z);
		latticeOut = new Lattice(x, y, z);
	}

	private void transformToP1() {	// atomsIn -> atomsP1
		Vector aa = new Vector(100, 100);
		for (int i=0; i<atomsIn.length; i++) {
			Vector3d v = new Vector3d(atomsIn[i].x, atomsIn[i].y, atomsIn[i].z);
			Vector3d[] vv = sgIn.getSymPos(v);
			for (int j=0, k=0; j<vv.length; j++) {
				if (vv[j]==null) continue;
				AtomSite a = new AtomSite(atomsIn[i]);
				//vv[j]=SpaceGroup.modCell(vv[j]);
				a.x = vv[j].x;
				a.y = vv[j].y;
				a.z = vv[j].z;
				if (vv.length>1) a.label = a.label+"_s"+(k++); 
				a.multiplicity = 1;
				aa.add(a);
			}
		}
		atomsP1 = (AtomSite[]) aa.toArray(new AtomSite[0]);
	}

	private void calculateAtomsOut() {	// atomsP1 -> atomsOut
		Vector aa = new Vector(100, 100);
		Vector v = calculateTranslations();
		for (int i=0; i<atomsP1.length; i++) {
			AtomSite a = new AtomSite(atomsP1[i]);
			Vector3d u = new Vector3d(atomsP1[i].x, atomsP1[i].y, atomsP1[i].z);
			Q.transform(u);
			u.add(q);
			u=SpaceGroup.modCell(round(u));
			a.x = u.x;
			a.y = u.y;
			a.z = u.z;
			aa.add(a);
			for (int j=0; j<v.size(); j++) {
				AtomSite aj = new AtomSite(a);
				Vector3d uj = new Vector3d(a.x, a.y, a.z);
				uj.add((Vector3d)v.get(j));
				uj=SpaceGroup.modCell(round(uj));
				aj.x = uj.x;
				aj.y = uj.y;
				aj.z = uj.z;
				aj.label = aj.label+"_t"+(j+1); 
				aa.add(aj);
			}
			if (v.size()>0) a.label = a.label+"_t0"; 
		}
		atomsOut = (AtomSite[]) aa.toArray(new AtomSite[0]);
	}

	private Vector calculateTranslations() {
		System.out.println("det(P)="+P.determinant());

		Vector v = new Vector();
		int n = (int)Math.abs(Math.round(P.determinant()));
		if (n==1) return v;

		Point3d p100 = new Point3d(1, 0, 0);
		Q.transform(p100);
		Point3d p010 = new Point3d(0, 1, 0);
		Q.transform(p010);
		Point3d p001 = new Point3d(0, 0, 1);
		Q.transform(p001);

		p100 = SpaceGroup.modCell(p100);
		p010 = SpaceGroup.modCell(p010);
		p001 = SpaceGroup.modCell(p001);

		System.out.println("100 -> "+round(p100));
		System.out.println("010 -> "+round(p010));
		System.out.println("001 -> "+round(p001));

		Vector3d t = new Vector3d(); 
		Vector3d tx = new Vector3d(); 
		Vector3d ty = new Vector3d(); 
		Vector3d tz = new Vector3d();

		for (int l=1; l<20; l++) {
			for (int i=0; i<=l; i++) {
				for (int j=0; j<=l; j++) {
					for (int k=0; k<=l; k++) {
						if (i==0&j==0&&k==0) continue;
						if (i<l&&j<l&&k<l) continue;
						tx.scale(i, p100);
						ty.scale(j, p010);
						tz.scale(k, p001);
						t.add(tx, ty);
						t.add(tz);
						t = round(SpaceGroup.modCell(t));
						//System.out.println(i+" "+j+" "+k+":"+t);
						if (!isAlreadyInOrZero(t, v)) {
							v.add(new Vector3d(t));
							System.out.println((i>0?(i>1?(i+"x"):"x"):"")+(j>0?(i>0?"+":"")+(j>1?(j+"y"):"y"):"")+(k>0?(i>0||j>0?"+":"")+(k>1?(k+"z"):"z"):"")+"  "+(round(t)));
							n--;
							if (n==1) {
								System.out.println();
								return v;
							}
						}
					}
				}
			}
		}
		System.err.println("Out of loop!!!");
		return v;
	}

	public static boolean isAlreadyInOrZero(Vector3d p, Vector v) {
		Vector3d q = round(SpaceGroup.modCell(p));
		if ((q.x==0||q.x==-0)&&(q.y==0||q.y==-0)&&(q.z==0||q.z==-0))
			return true;
		for (int i=0; i<v.size(); i++) {
			if (round(SpaceGroup.modCell((Vector3d)v.get(i))).equals(q)) {
				return true;
			}
		}
		return false;
	}

	public static Point3d round(Point3d p) {
		return new Point3d(Math.round(100000*p.x)/100000d, Math.round(100000*p.y)/100000d, Math.round(100000*p.z)/100000d);
	}
	public static Vector3d round(Vector3d p) {
		return new Vector3d(Math.round(100000*p.x)/100000d, Math.round(100000*p.y)/100000d, Math.round(100000*p.z)/100000d);
	}
}
