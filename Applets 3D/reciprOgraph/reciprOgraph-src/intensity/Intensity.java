/* IntensityCalc - Intensity.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 2 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */

package intensity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ProgressMonitor;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Sphere;

import panes.ProjScreen;
import panes.SingleCrystalPane;
import powder.IntensitiesData;

import engine3D.Cell;
import engine3D.Univers;

import sg.Lattice;
import sg.SgSystem;
import sg.SgType;
import sg.SpaceGroup;
import structures.AtomSite;
import structures.CifFile;
import structures.CifFileParser;
import utils.ColorConstants;
import utils.Utils3d;

public class Intensity {
	private final static FormFactorTable ffTable = new FormFactorTable();
	public final static int hklMaxSize=30;
	public final static double scale3D=1d/24;
	
	public static Vector allInstances;
	public static Univers univers;
	private static boolean proj2D;
	public static double iMin=5;
	public static int hMax=3, kMax=3, lMax=3;
	public static int u=1, v=0, w=0, t=0;
	public static double scaleUser=1;
	
	private float[][][] miTable=new float[hklMaxSize*2+1][hklMaxSize*2+1][hklMaxSize*2+1]; // ... -2 -1 0 1 2 ...
	private float[][][] mfRe=new float[hklMaxSize*2+1][hklMaxSize*2+1][hklMaxSize*2+1];
	private float[][][] mfIm=new float[hklMaxSize*2+1][hklMaxSize*2+1][hklMaxSize*2+1];
	private float[][][] mtheta2=new float[hklMaxSize*2+1][hklMaxSize*2+1][hklMaxSize*2+1];
	private float[][][] mdhkl=new float[hklMaxSize*2+1][hklMaxSize*2+1][hklMaxSize*2+1];
	
	private ProjScreen projScreen;
	private Vector allTransforms;
	public Lattice lattice, reciprocal;
	public SpaceGroup sg;
	public AtomSite[] atoms;
	private Cell cell;
	private Appearance atomApp;
	private double scaleStruct=1;
	public boolean visible=true;
	private BranchGroup bgIntensity, bgPlan;
	private Point3d[][] sympos;
	private int[] atomsSymbol;
	private Vector3d n=new Vector3d(), e1=new Vector3d(), e3=new Vector3d();
	private Point3d o=new Point3d();
	private double i0;
	double secondBigestIntensity=1;
	
	public static void init(Univers univers, boolean proj2D) {
		Intensity.univers=univers;
		Intensity.proj2D=proj2D;
		if (univers!=null) {
			univers.scale(20);
		}
		allInstances = new Vector(20, 20);
	}
	
	public Intensity(CifFile cif) {
		this(cif, new Color(0, 0, 0, 100));
	}
	public Intensity(CifFile cif, Color color) {
		allInstances.add(this);
		if (univers!=null) {
			allTransforms = new Vector(10000, 10000);
			bgPlan = new BranchGroup();
			bgPlan.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			bgPlan.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			univers.root.addChild(bgPlan);
		}

		atomApp = Utils3d.createApp(ColorConstants.black);
		//atomApp = Utils3d.createApp(new Color3f(color));
		//atomApp = Utils3d.createApp(new Color3f(color), .5f);
		//atomApp = Utils3d.createAppOpaque(color, .5f);

		sg = cif.getSg();
		atoms = cif.getAtoms();
		lattice = sg.cell;
		reciprocal = lattice.reciprocal();
		invalidateIntensities();
		i0 = I(0, 0, 0);
		if (proj2D) {
			projScreen = new ProjScreen(color);
			projScreen.iMin = i0*iMin/100d;
		}
		calculateIntensities();
		refreshViews();
	}
	
	public void setCif(CifFile cif) {
		sg = cif.getSg();
		atoms = cif.getAtoms();
		lattice = sg.cell;
		reciprocal = lattice.reciprocal();
		invalidateIntensities();
		i0 = I(0, 0, 0);
		if (proj2D) {
			projScreen.iMin = i0*iMin/100d;
		}
		calculateIntensities();
		refreshViews();
	}
	
	public void debugDisplayCifInfo() {
		System.out.println(lattice);
		System.out.println("a="+Utils3d.posToString(lattice.x)+" b="+Utils3d.posToString(lattice.y)+" c="+Utils3d.posToString(lattice.z));
		System.out.println("a*="+Utils3d.posToString(reciprocal.x)+" b*="+Utils3d.posToString(reciprocal.y)+" c*="+Utils3d.posToString(reciprocal.z));
		System.out.println("alpha*="+Utils3d.posToString(reciprocal.alpha)+" beta*="+Utils3d.posToString(reciprocal.beta)+" gamma*="+Utils3d.posToString(reciprocal.gamma));
		System.out.println(sg.sg);
		System.out.println();
	
		for (int i=0; i<atoms.length; i++) {
			System.out.println(atoms[i]);
			Point3d[] pp = sg.getSymPos(new Point3d(atoms[i].x, atoms[i].y, atoms[i].z));
			int uniques=0;
			for (int j=0; j<pp.length; j++) {
				if (pp[j]!=null) {
					System.out.println(pp[j]);
					uniques++;
				}
			}
			System.out.println("positions:"+uniques+"/"+pp.length);
			System.out.println();
		}
	}
	
	public void remove() {
		if (univers!=null) {
			allTransforms.clear();
			if (bgIntensity!=null) univers.root.removeChild(bgIntensity);
			bgPlan.removeAllChildren();
			if (cell!=null) cell.hide();
		}
		if (proj2D) {
			projScreen.remove();
			projScreen=null;
		}
		allInstances.remove(this);
	}
	
	public void showCell() {
		if (univers==null) return;
		if (cell!=null) cell.hide();
		cell = new Cell(reciprocal, 0.003, new String[]{"a*", "b*", "c*"});
		cell.show(univers.root);
	}
	
	
	private void refreshViews() {
		if (univers!=null) {
			showIntensity3D();
		}
		if (proj2D) {
			calculateProjPlan();
			showProjection();
		}
		if (univers!=null && proj2D) {
			showProjPlan3D();
		}
	}
	
	public static void setHKL(int h, int k, int l) {
		//System.out.println("hkl="+h+" "+k+" "+l);
		if (Intensity.hMax==h&&Intensity.kMax==k&&Intensity.lMax==l) return;
		Intensity.hMax=h;
		Intensity.kMax=k;
		Intensity.lMax=l;
		SingleCrystalPane.needRePrint = true;
		for (int i=0; i<allInstances.size(); i++) {
			Intensity e = (Intensity) allInstances.get(i);
			e.calculateIntensities();
			e.refreshViews();
		}
	}
	
	public static void setUVWT(int u, int v, int w, int t) {
		//System.out.println("uvw t="+u+" "+v+" "+w+" "+t);
		if (Intensity.u==u&&Intensity.v==v&&Intensity.w==w&&Intensity.t==t) return;
		Intensity.u=u; Intensity.v=v; Intensity.w=w; Intensity.t=t;
		if (u==0&&v==0&&w==0) {
			for (int i=0; i<allInstances.size(); i++) {
				Intensity e = (Intensity) allInstances.get(i);
				e.projScreen.clearImage();
			}
			ProjScreen.refresh();
		}
		else {
			for (int i=0; i<allInstances.size(); i++) {
				Intensity e = (Intensity) allInstances.get(i);
				e.calculateIntensities();
				e.refreshViews();
			}
		}
	}
	
	public static void setProjSettings(boolean drawIndex, boolean drawI) {
		ProjScreen.drawIndex = drawIndex;
		ProjScreen.drawI = drawI;
		ProjScreen.refresh();
	}
	
	public static void setDispersion(double s) {
		if (!proj2D) return;
		for (int i=0; i<allInstances.size(); i++) {
			Intensity e = (Intensity) allInstances.get(i);
			e.projScreen.ldx = s*i*1;
		}
		ProjScreen.refresh();
	}
	
	public static void setScaleFactor(double s) {
		ProjScreen.scaleUser=s;
		scaleUser = s;
		if (univers!=null) {
			Transform3D t3d = new Transform3D();
			for (int i=0; i<allInstances.size(); i++) {
				Intensity e = (Intensity) allInstances.get(i);
				for (int j=0; j<e.allTransforms.size(); j++) {
					TransformGroup tg = ((TransformGroup)e.allTransforms.get(j));
					tg.getTransform(t3d);
					t3d.set(scaleUser*e.scaleStruct);
					tg.setTransform(t3d);
				}
			}
		}
		if (proj2D) ProjScreen.refresh();
	}

	public static void setImin(double ipercent) {
		if (!proj2D) return;
		iMin=ipercent;
		for (int i=0; i<allInstances.size(); i++) {
			Intensity e = (Intensity) allInstances.get(i);
			e.projScreen.iMin = e.i0*iMin/100d;
		}
		ProjScreen.refresh();
	}
	
	public void setVisible(boolean visible) {
		if (univers!=null) {
			if (this.visible!=visible) {
				this.visible=visible;
				showIntensity3D();
			}
		}
		if (proj2D) {
			projScreen.visible = visible;
			ProjScreen.refresh();
		}
	}

	public void setColor(Color color) {
		if (univers!=null) {
			atomApp = Utils3d.createApp(new Color3f(color), .5f);
			//atomApp = Utils3d.createAppOpaque(color, .5f);
			showIntensity3D();
		}
		if (proj2D) projScreen.setColor(color);
	}
	

	private void invalidateIntensities() {
		sympos = new Point3d[atoms.length][];
		atomsSymbol = new int[atoms.length];
		secondBigestIntensity=1;
		for (int i=0; i<atoms.length; i++) {
			atomsSymbol[i] = ffTable.getAtomicNo(atoms[i].symbol);
			sympos[i] = sg.getSymPos(new Point3d(atoms[i].x, atoms[i].y, atoms[i].z));
		}

		for (int i=0; i<miTable.length; i++) {
			for (int j=0; j<miTable[i].length; j++) {
				for (int k=0; k<miTable[i][j].length; k++) {
					miTable[i][j][k] = -1;
				}
			}
		}
	}
	
	public double I(int h, int k, int l) {
		double sumRe=0, sumIm=0; 
		double bcCosAlphaStar2 = reciprocal.b*reciprocal.c*Math.cos(reciprocal.alpha*Math.PI/180d)/2d;
		double acCosBetaStar2 = reciprocal.a*reciprocal.c*Math.cos(reciprocal.beta*Math.PI/180d)/2d;
		double abCosGammaStar2 = reciprocal.a*reciprocal.b*Math.cos(reciprocal.gamma*Math.PI/180d)/2d;
		double H = h*h*reciprocal.a*reciprocal.a/4d + k*k*reciprocal.b*reciprocal.b/4d + l*l*reciprocal.c*reciprocal.c/4d + +k*l*bcCosAlphaStar2+h*l*acCosBetaStar2+h*k*abCosGammaStar2;
		for (int i=0; i<atoms.length; i++) {
			double f = ffTable.scat2(atomsSymbol[i], H);
			for (int j=0; j<sympos[i].length; j++) {
				if (sympos[i][j]!=null) {
					double phi = 2*Math.PI*(h*sympos[i][j].x + k*sympos[i][j].y + l*sympos[i][j].z);
					sumRe += f*atoms[i].occupancy*Math.cos(phi);
					sumIm += f*atoms[i].occupancy*Math.sin(phi);
				}
			}
		}
		return sumRe*sumRe + sumIm*sumIm;
	}
	public double I(int h, int k, int l, double H) {
		if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) return miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize];
		double sumRe=0, sumIm=0; 
		for (int i=0; i<atoms.length; i++) {
			double f = ffTable.scat2(atomsSymbol[i], H);
			for (int j=0; j<sympos[i].length; j++) {
				if (sympos[i][j]!=null) {
					double phi = 2*Math.PI*(h*sympos[i][j].x + k*sympos[i][j].y + l*sympos[i][j].z);
					sumRe += f*atoms[i].occupancy*Math.cos(phi);
					sumIm += f*atoms[i].occupancy*Math.sin(phi);
				}
			}
		}
		double I = sumRe*sumRe + sumIm*sumIm;
		return I;
	}

	public void calcI(int h, int k, int l, double H) {
		double sumRe=0, sumIm=0; 
		for (int i=0; i<atoms.length; i++) {
			double f = ffTable.scat2(atomsSymbol[i], H)*atoms[i].occupancy;
			for (int j=0; j<sympos[i].length; j++) {
				if (sympos[i][j]!=null) {
					double phi = 2*Math.PI*(h*sympos[i][j].x + k*sympos[i][j].y + l*sympos[i][j].z);
					sumRe += f*Math.cos(phi);
					sumIm += f*Math.sin(phi);
				}
			}
		}
		mfRe[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = (float)sumRe;
		mfIm[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = (float)sumIm;
		miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = (float)(sumRe*sumRe + sumIm*sumIm);

		double hsqrt = Math.sqrt(H);
		mdhkl[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = H==0?0:1f/(2f*(float)hsqrt);
		mtheta2[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = (float)(2d*Math.asin(IntensitiesData.lambda*hsqrt)*180d/Math.PI);

		if (h!=0||k!=0||l!=0) {
			secondBigestIntensity = Math.max(miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize], secondBigestIntensity);
		}		
	}
	
	public void putI(int hFrom, int kFrom, int lFrom, int hTo, int kTo, int lTo) {
		hFrom+=hklMaxSize; kFrom+=hklMaxSize; lFrom+=hklMaxSize;
		hTo+=hklMaxSize; kTo+=hklMaxSize; lTo+=hklMaxSize;
		mfRe[hTo][kTo][lTo] = mfRe[hFrom][kFrom][lFrom];
		mfIm[hTo][kTo][lTo] = mfIm[hFrom][kFrom][lFrom];
		miTable[hTo][kTo][lTo] = miTable[hFrom][kFrom][lFrom];
		mdhkl[hTo][kTo][lTo] = mdhkl[hFrom][kFrom][lFrom];
		mtheta2[hTo][kTo][lTo] = mtheta2[hFrom][kFrom][lFrom];
	}
	
	private void calculateIntensitiesTriclinic() {
		double bcCosAlphaStar2 = reciprocal.b*reciprocal.c*Math.cos(reciprocal.alpha*Math.PI/180d)/2d;
		double acCosBetaStar2 = reciprocal.a*reciprocal.c*Math.cos(reciprocal.beta*Math.PI/180d)/2d;
		double abCosGammaStar2 = reciprocal.a*reciprocal.b*Math.cos(reciprocal.gamma*Math.PI/180d)/2d;
		for (int h=-hMax; h<=hMax; h++) {
			double has2 = h*h*reciprocal.a*reciprocal.a/4d;
			for (int k=-kMax; k<=kMax; k++) {
				double kbs2 = k*k*reciprocal.b*reciprocal.b/4d;
				for (int l=0; l<=lMax; l++) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) continue;
					//if (h*u+k*v+l*w!=t) continue;	// layer specific
					double lcs2 = l*l*reciprocal.c*reciprocal.c/4d;
					double H = has2+kbs2+lcs2+k*l*bcCosAlphaStar2+h*l*acCosBetaStar2+h*k*abCosGammaStar2;
					calcI(h, k, l, H);
					putI(h, k, l, -h, -k, -l);
				}
			}
		}
		scaleStruct=1/(2*secondBigestIntensity);
	}
	private void calculateIntensitiesOrthorhombic() {
		for (int h=0; h<=hMax; h++) {
			double has2 = h*h*reciprocal.a*reciprocal.a/4d;
			for (int k=0; k<=kMax; k++) {
				double kbs2 = k*k*reciprocal.b*reciprocal.b/4d;
				for (int l=0; l<=lMax; l++) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) continue;
					//if (h*u+k*v+l*w!=t) continue;	// layer specific
					double lcs2 = l*l*reciprocal.c*reciprocal.c/4d;
					double H = has2+kbs2+lcs2;
					calcI(h, k, l, H);
					putI(h, k, l,  h,  k, -l);
					putI(h, k, l,  h, -k,  l);
					putI(h, k, l,  h, -k, -l);
					putI(h, k, l, -h,  k,  l);
					putI(h, k, l, -h,  k, -l);
					putI(h, k, l, -h, -k,  l);
					putI(h, k, l, -h, -k, -l);
				}
			}
		}
		scaleStruct=1/(2*secondBigestIntensity);
	}
	private void calculateIntensitiesMonoclinicAlpha() {
		double bcCosAlphaStar2 = reciprocal.b*reciprocal.c*Math.cos(reciprocal.alpha*Math.PI/180d)/2d;
		for (int h=0; h<=hMax; h++) {
			double has2 = h*h*reciprocal.a*reciprocal.a/4d;
			for (int k=0; k<=kMax; k++) {
				double kbs2 = k*k*reciprocal.b*reciprocal.b/4d;
				for (int l=-lMax; l<=lMax; l++) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) continue;
					//if (h*u+k*v+l*w!=t) continue;	// layer specific
					double lcs2 = l*l*reciprocal.c*reciprocal.c/4d;
					double H = has2+kbs2+lcs2+k*l*bcCosAlphaStar2;
					calcI(h, k, l, H);
					putI(h, k, l, -h, -k, -l);
					putI(h, k, l, -h,  k,  l);
					putI(h, k, l,  h, -k, -l);
				}
			}
		}
		scaleStruct=1/(2*secondBigestIntensity);
	}
	private void calculateIntensitiesMonoclinicBeta() {
		double acCosBetaStar2 = reciprocal.a*reciprocal.c*Math.cos(reciprocal.beta*Math.PI/180d)/2d;
		for (int h=0; h<=hMax; h++) {
			double has2 = h*h*reciprocal.a*reciprocal.a/4d;
			for (int k=0; k<=kMax; k++) {
				double kbs2 = k*k*reciprocal.b*reciprocal.b/4d;
				for (int l=-lMax; l<=lMax; l++) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) continue;
					//if (h*u+k*v+l*w!=t) continue;	// layer specific
					double lcs2 = l*l*reciprocal.c*reciprocal.c/4d;
					double H = has2+kbs2+lcs2+h*l*acCosBetaStar2;
					calcI(h, k, l, H);
					putI(h, k, l, -h, -k, -l);
					putI(h, k, l,  h, -k,  l);
					putI(h, k, l, -h,  k, -l);
				}
			}
		}
		scaleStruct=1/(2*secondBigestIntensity);
	}
	private void calculateIntensitiesMonoclinicGamma() {
		double abCosGammaStar2 = reciprocal.a*reciprocal.b*Math.cos(reciprocal.gamma*Math.PI/180d)/2d;
		for (int h=0; h<=hMax; h++) {
			double has2 = h*h*reciprocal.a*reciprocal.a/4d;
			for (int k=-kMax; k<=kMax; k++) {
				double kbs2 = k*k*reciprocal.b*reciprocal.b/4d;
				for (int l=0; l<=lMax; l++) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]!=-1) continue;
					//if (h*u+k*v+l*w!=t) continue;	// layer specific
					double lcs2 = l*l*reciprocal.c*reciprocal.c/4d;
					double H = has2+kbs2+lcs2+h*k*abCosGammaStar2;
					calcI(h, k, l, H);
					putI(h, k, l, -h, -k, -l);
					putI(h, k, l, -h, -k,  l);
					putI(h, k, l,  h,  k, -l);
				}
			}
		}
		scaleStruct=1/(2*secondBigestIntensity);
	}
	
	private void calculateIntensities() {
		calculateIntensitiesTriclinic();
	}
	
/*	
	private void calculateIntensities_OLD() {
		//long t0 = new Date().getTime();
		boolean opt = univers==null;
		
		double cosAlphaStar2 = Math.cos(reciprocal.alpha*Math.PI/180d)/2d;
		double cosBetaStar2 = Math.cos(reciprocal.beta*Math.PI/180d)/2d;
		double cosGammaStar2 = Math.cos(reciprocal.gamma*Math.PI/180d)/2d;
		boolean alpha90 = (90==(int)Math.round(reciprocal.alpha));
		boolean beta90 = (90==(int)Math.round(reciprocal.beta));
		boolean gamma90 = (90==(int)Math.round(reciprocal.gamma));
		
		for (int h=0; h<=hMax; h++) {
			double has2 = h*reciprocal.a/2d;
			has2=has2*has2;
			for (int k=0; k<=kMax; k++) {
				double kbs2 = k*reciprocal.b/2d;
				kbs2=kbs2*kbs2;
				for (int l=0; l<=lMax; l++) {
					if (iTable[h][k*2][l*2]!=-1) continue;
					if (opt&&h*u+k*v+l*w!=t) continue;	// layer specific
					
					double lcs2 = l*reciprocal.c/2d;
					lcs2=lcs2*lcs2;
					
					double H = has2+kbs2+lcs2;
					
					
					if (!alpha90) H += k*l*reciprocal.b*reciprocal.c*cosAlphaStar2;
					if (!beta90) H += h*l*reciprocal.a*reciprocal.c*cosBetaStar2;
					if (!gamma90) H += h*k*reciprocal.a*reciprocal.b*cosGammaStar2;
					
						
					for (int i=0; i<atoms.length; i++) {
						double f = ffTable.scat2(atomsSymbol[i], H);
						double fo = f*atoms[i].occupancy;
						
						for (int j=0; j<sympos[i].length; j++) {
							if (sympos[i][j]!=null) {
								double phi_nnn = 2*Math.PI*(-h*sympos[i][j].x - k*sympos[i][j].y - l*sympos[i][j].z);
								double phi_nnp = 2*Math.PI*(-h*sympos[i][j].x - k*sympos[i][j].y + l*sympos[i][j].z);
								double phi_npn = 2*Math.PI*(-h*sympos[i][j].x + k*sympos[i][j].y - l*sympos[i][j].z);
								double phi_npp = 2*Math.PI*(-h*sympos[i][j].x + k*sympos[i][j].y + l*sympos[i][j].z);

								
								fRe[h][k*2][l*2] += fo*Math.cos(phi_nnn);
								fRe[h][k*2][l*2+1] += fo*Math.cos(phi_nnp);
								fRe[h][k*2+1][l*2] += fo*Math.cos(phi_npn);
								fRe[h][k*2+1][l*2+1] += fo*Math.cos(phi_npp);

								fIm[h][k*2][l*2] += fo*Math.sin(phi_nnn);
								fIm[h][k*2][l*2+1] += fo*Math.sin(phi_nnp);
								fIm[h][k*2+1][l*2] += fo*Math.sin(phi_npn);
								fIm[h][k*2+1][l*2+1] += fo*Math.sin(phi_npp);
								
								
							}
						}
					}
					iTable[h][k*2][l*2]=fRe[h][k*2][l*2]*fRe[h][k*2][l*2] + fIm[h][k*2][l*2]*fIm[h][k*2][l*2];
					iTable[h][k*2][l*2+1]=fRe[h][k*2][l*2+1]*fRe[h][k*2][l*2+1] + fIm[h][k*2][l*2+1]*fIm[h][k*2][l*2+1];
					iTable[h][k*2+1][l*2]=fRe[h][k*2+1][l*2]*fRe[h][k*2+1][l*2] + fIm[h][k*2+1][l*2]*fIm[h][k*2+1][l*2];
					iTable[h][k*2+1][l*2+1]=fRe[h][k*2+1][l*2+1]*fRe[h][k*2+1][l*2+1] + fIm[h][k*2+1][l*2+1]*fIm[h][k*2+1][l*2+1];
					
					double hsqrt = Math.sqrt(H);
					dhkl[h][k][l] = H==0?0:1f/(2f*(float)hsqrt);
					theta2[h][k][l] = 2*(float)Math.asin(IntensitiesData.lambda*hsqrt);
					theta2[h][k][l] = theta2[h][k][l]*180/(float)Math.PI;

					
					System.out.println(h+" "+k+" "+l);
					
					
					if (h==3 && k==3 && l==0) {
						System.out.println(h*k*reciprocal.a*reciprocal.b*cosGammaStar2*4);
						System.out.println((H*4)+" "+dhkl[h][k][l]+" "+theta2[h][k][l]);
					}
					
					
					
					
					if (h!=0||k!=0||l!=0) {
						secondBigestIntensity = Math.max(iTable[h][k*2][l*2], secondBigestIntensity);
						secondBigestIntensity = Math.max(iTable[h][k*2][l*2+1], secondBigestIntensity);
						secondBigestIntensity = Math.max(iTable[h][k*2+1][l*2], secondBigestIntensity);
						secondBigestIntensity = Math.max(iTable[h][k*2+1][l*2+1], secondBigestIntensity);
					}
				}
			}
		}
		//scaleStruct=1/iTable[0][0][0];
		scaleStruct=1/(2*secondBigestIntensity);
		//long t1 = new Date().getTime();
		//System.out.println("calculateIntensities(): "+(t1-t0)+"ms");
	}
*/
	
	public static void recalculateTheta2Table() {
		for (int i=0; i<allInstances.size(); i++) {
			Intensity e = (Intensity) allInstances.get(i);
			for (int h=-hMax; h<=hMax; h++) {
				for (int k=-kMax; k<=kMax; k++) {
					for (int l=-lMax; l<=lMax; l++) {
						e.mtheta2[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize] = (float)(Math.asin(IntensitiesData.lambda*(1d/(2d*e.mdhkl[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize])))*360d/Math.PI);
					}
				}
			}
		}
	}
	
	
	public void printList() {
		SingleCrystalPane.clear();
		for (int h=hMax; h>=-hMax; h--) {
			for (int k=kMax; k>=-kMax; k--) {
				for (int l=lMax; l>=-lMax; l--) {
					if (miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]<0.001) continue;
					if (Double.isNaN(mtheta2[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize])) continue;
					SingleCrystalPane.add(h, k, l, mdhkl[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize], mtheta2[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize], mfRe[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize], mfIm[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize], miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize]);					
				}
			}
		}
	}
	
	
	private void showIntensity3D() {
		allTransforms.clear();
		if (bgIntensity!=null) univers.root.removeChild(bgIntensity);
		if (!visible || hMax*kMax*lMax>10*10*10) return;
		
		bgIntensity = new BranchGroup();
		bgIntensity.setCapability(BranchGroup.ALLOW_DETACH);
		BranchGroup bg = new BranchGroup();
		bg.setCapability(0);
		bg.setPickable(false);
		
		int hklMax = Math.max(hMax, Math.max(kMax, lMax));
		if (cell!=null) {
			cell.setBoxScale((double)hMax/hklMax, (double)kMax/hklMax, (double)lMax/hklMax);
		}
		
		for (int h=-hMax; h<=hMax; h++) {
			for (int k=-kMax; k<=kMax; k++) {
				for (int l=-lMax; l<=lMax; l++) {
					if (h==0&&k==0&&l==0) continue;
					double I = miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize];
					if (I<.1) continue;
					putAtom(bg, .5*h/hklMax+.5, .5*k/hklMax+.5, .5*l/hklMax+.5, I*scale3D, scaleStruct*scaleUser);
				}
			}
		}
		bg.compile();
		bgIntensity.addChild(bg);
		univers.root.addChild(bgIntensity);
	}
	
	private void putAtom(BranchGroup bg, double x, double y, double z, double r, double scale) {
		Point3d p = new Point3d(x, y, z);
		reciprocal.transform(p);
		allTransforms.add(Utils3d.putAtom(bg, (float)r, p, atomApp, scale));
	}
	
	private void calculateProjPlan() {
		n.set(0, 0, 0);
		n.scaleAdd(u, lattice.x, n);
		n.scaleAdd(v, lattice.y, n);
		n.scaleAdd(w, lattice.z, n);
		double f = 1d/n.length();
		n.scale(1d/40);

		if (u==0 && v==0 && w==0) return;
		if (u!=0 && v==0 && w==0) e1 = new Vector3d(reciprocal.y);
		else if (u==0 && v!=0 && w==0) e1 = new Vector3d(reciprocal.x);
		else if (u==0 && v==0 && w!=0) e1 = new Vector3d(reciprocal.x);
		else if (u==0 && v!=0 && w!=0) e1 = new Vector3d(reciprocal.x);
		else if (u!=0 && v==0 && w!=0) e1 = new Vector3d(reciprocal.y);
		else if (u!=0 && v!=0 && w==0) e1 = new Vector3d(reciprocal.z);
		else e1 = new Vector3d(1, 1, (-n.x-n.y)/n.z);

		e3.cross(e1, n);
		n.normalize();
		e1.normalize();
		e3.normalize();
		
		o.set(n);
		o.x *= ((f*t)/(hMax*2));
		o.y *= ((f*t)/(kMax*2));
		o.z *= ((f*t)/(lMax*2));
	}
	
	private void showProjPlan3D() {
		bgPlan.removeAllChildren();
		BranchGroup plan = Utils3d.createPlan(o, e1, n, e3, .4, .4, Utils3d.createAppOpaque(new Color(50, 50, 255, 150), .3f));		
		bgPlan.addChild(plan);
	}
	
	private void debugShowDirectRepere() {
		float size = 0.003f;
		Appearance app = Utils3d.createApp(ColorConstants.red);
		Point3d ro = new Point3d(0, 0, 0);
		Vector3d r1 = new Vector3d(.3, 0, 0);
		Vector3d r2 = new Vector3d(0, .3, 0);
		Vector3d r3 = new Vector3d(0, 0, .3);
		reciprocal.transform(ro);
		lattice.transform(r1);
		lattice.transform(r2);
		lattice.transform(r3);
		r1.add(lattice.g);
		r2.add(lattice.g);
		r3.add(lattice.g);
		r1.scale(1d/20);
		r2.scale(1d/20);
		r3.scale(1d/20);
		bgPlan.addChild(Utils3d.createVector("a", ro, r1, size, app, 20));
		bgPlan.addChild(Utils3d.createVector("b", ro, r2, size, app, 20));
		bgPlan.addChild(Utils3d.createVector("c", ro, r3, size, app, 20));
	}
	
	private void debugShowPlanVectors() {
		bgPlan.addChild(Utils3d.createAtom(o, .01f, Utils3d.createApp(ColorConstants.blue), 20));
		bgPlan.addChild(Utils3d.createVector("uvw", o, n, 0.003f, Utils3d.createApp(ColorConstants.blue), 20));
		bgPlan.addChild(Utils3d.createVector("e1", o, e1, 0.003f, Utils3d.createApp(ColorConstants.red), 20));
		bgPlan.addChild(Utils3d.createVector("e3", o, e3, 0.003f, Utils3d.createApp(ColorConstants.yellow), 20));
	}
	
	private void showProjection() {
		int hklMax = Math.max(hMax, Math.max(kMax, lMax));
		projScreen.clearImage();
		Vector3d p = new Vector3d();
		for (int h=-hMax; h<=hMax; h++) {
			for (int k=-kMax; k<=kMax; k++) {
				for (int l=-lMax; l<=lMax; l++) {
					if (h==0&&k==0&&l==0) continue;
					double I = miTable[h+hklMaxSize][k+hklMaxSize][l+hklMaxSize];
					if (I<.0001) continue;
					if (h*u+k*v+l*w==t) {
						p.set(.5*h/hklMax+.5, .5*k/hklMax+.5, .5*l/hklMax+.5);
						reciprocal.transform(p);
						p.sub(o);
						projScreen.addPoint(e1.dot(p), e3.dot(p), I*scaleStruct, h+" "+k+" "+l, I);
					}
				}
			}
		}
		ProjScreen.refresh();
	}

	
	public static void main(String[] args) throws IOException {
		SgSystem.static_init();
		SgType.staticInit();
		Intensity.init(new Univers(), false);

		
		for (int i=0; i<SgSystem.nbSystems(3); i++) {
			System.out.println(SgSystem.getSystem(3, i, 0).name);
		}
		
		
		Intensity intensity = new Intensity(new CifFile(Intensity.class.getResource("/Corundum(alpha)Al2O3.cif").openStream()));
		//Intensity intensity = new Intensity(new CifFile(new FileInputStream("C:\\Documents and Settings\\nschoeni\\Bureau\\ivan\\multi\\(TS)1 T.cif")));

		System.out.println(intensity.reciprocal.a);
		System.out.println(intensity.reciprocal.b);
		System.out.println(intensity.reciprocal.c);
		System.out.println(intensity.reciprocal.alpha);
		System.out.println(intensity.reciprocal.beta);
		System.out.println(intensity.reciprocal.gamma);
		
		
		hMax=kMax=lMax=2;
		intensity.calculateIntensities();
		System.exit(0);
	}

	
	private static double sin(double a) {		// arguments in degrees !!
		return Math.sin(a*Math.PI/180d);
	}
	private static double cos(double a) {
		return Math.cos(a*Math.PI/180d);
	}
}
