package model3d;


import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JCheckBox;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.j3d.geom.Torus;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

import diffrac.DefaultValues;
import diffrac.Lattice;

import projScreen.ProjScreen;
import transformations.OrientationClass;
import transformations.PrecessionClass;

public class Model3d implements ColorConstants {
	public Univers univers;
	public VirtualSphere s;
	public ProjScreen3d p3d;
	public Net net;
	public ProjScreen projScreen;
	public boolean persistant = true;
	public Mask3d mask3d;
	public boolean mask = false;
	public Rays rays;
	private DefaultValues defaultValues;
	private double hCyl, hFlat;
	public OrientationClass orientationClass;
	public PrecessionClass precessionClass;
	
	public Model3d(DefaultValues defaultValues, ProjScreen projScreen) {
		this.defaultValues = defaultValues;
		univers = new Univers();
		univers.rotX(-90);
		univers.rotY(-90);
		
		orientationClass = new OrientationClass();
		precessionClass = new PrecessionClass();
		
		this.projScreen = projScreen;
		s = new VirtualSphere(defaultValues, defaultValues.lambda);
		setFlatScreen();

		Lattice r = defaultValues.lattice.reciprocal();
		net = new Net(orientationClass, precessionClass, defaultValues, r.x, r.y, r.z, defaultValues.crystalX, defaultValues.crystalY, defaultValues.crystalZ);

		net.gonioHead.setY(s.lambdaToRadius(defaultValues.lambda));
		
		mask3d = new Mask3d(precessionClass, defaultValues, p3d.y, 2, p3d.w, p3d.h);
		
		univers.root.addChild(s);
		univers.root.addChild(net);
		
		rays = new Rays();
		univers.root.addChild(rays);
		
		//Debug.transparentScreen(Debug.root, new Vector3d(0,0,0), new Vector3d(1,0,0), new Vector3d(0,0,1), new Vector3d(0,1,0), 2, 2, ColorConstants.green);
		//Debug.point(Debug.root, new Point3d(0, 2, 0), ColorConstants.black, .1);
	}
	
	public void setMask(boolean enabled) {
		if (!mask&&enabled) univers.root.addChild(mask3d);
		if (mask&&!enabled) univers.root.removeChild(mask3d);
		mask = enabled;
	}

	public synchronized void clearAllRays() {
		if (!persistant) projScreen.clearImage();
		rays.removeAllRays();
		for (int i=-net.xMax; i<=net.xMax; i++)
			for (int j=-net.yMax; j<=net.yMax; j++)
				for (int k=-net.zMax; k<=net.zMax; k++) {
					net.unHighlight(i, j, k);
				}
	}

	private Point3d v = new Point3d();
	private Point3d q = new Point3d();
	private Vector3d n = new Vector3d();
	private Vector3d c = new Vector3d();
	private Vector3d u = new Vector3d();
	private Transform3D tOP = new Transform3D();
	private Point3d sReversed = new Point3d();
	private Vector3d e1 = new Vector3d();
	private Vector3d e3 = new Vector3d();
	
	public synchronized void doRays(boolean adjustR) {
		clearAllRays();
		n.set(0, 1, 0);
		e1.set(1, 0, 0);
		e3.set(0, 0, 1);
		precessionClass.apply(n);
		precessionClass.apply(e1);
		precessionClass.apply(e3);
		c.set(0, p3d.y, 0);
		double cn = c.dot(n);
		tOP.mul(precessionClass.t3d, orientationClass.t3d);
		sReversed.set(s.center);
		precessionClass.reverse(sReversed);
		orientationClass.reverse(sReversed);
		double rMask = Math.sin(precessionClass.mu)*p3d.y;
		Point3d cMask = mask3d.center();		
		
//		Debug.root.removeAllChildren();
//		Debug.point(Debug.root, c, ColorConstants.orange, 0.1);
//		Debug.vector(Debug.root, e1, c, ColorConstants.red, 0.05);
//		Debug.vector(Debug.root, n, c, ColorConstants.green, 0.05);
//		Debug.vector(Debug.root, e3, c, ColorConstants.blue, 0.05);
//		
		for (int h=-net.xMax; h<=net.xMax; h++)
			for (int k=-net.yMax; k<=net.yMax; k++)
				for (int l=-net.zMax; l<=net.zMax; l++) {
					if (h==0&&k==0&&l==0) continue;
					Point3d p = net.points[h+net.xMax][k+net.yMax][l+net.zMax];
					
					if (p==null) continue;
					
					double r = s.radius;
					double d = sReversed.distance(p)-r;
					if (d>0 || Math.abs(d)>defaultValues.dotSize) continue;
					//TODO: ? if (Math.abs(d)>DefaultValues.dotSize) continue;

					q.set(p);
					tOP.transform(q);
					
					if (adjustR) {
						r = -(q.x*q.x+q.y*q.y+q.z*q.z)/(2*q.y);
						if (Double.isInfinite(r) || Double.isNaN(r) || r<=0d) continue;
					}
					
					v.set(q.x, q.y+r, q.z);
					if (!p3d.projPoint(v, n, cn)) continue;

					if (p3d instanceof ProjScreen3d.Cylindric) {
						// cylindric is much simple because no precession allowed
						u.set(v);
					}
					else {
						u.sub(v, c);
						u.set(e1.dot(u), n.dot(u), e3.dot(u));
					}
					
					Point.Double p2d = p3d.proj3dTo2d(u);
					if (p2d==null) continue;

					if (mask) d = Math.abs(v.distance(cMask)-rMask);
					if (mask && d>.1) {
						v.scale(defaultValues.maskDistFract);
					}
					else {
						float intensity = net.intensity(h, k, l);
						projScreen.drawPoint(p2d, intensity, (byte)h, (byte)k, (byte)l);
					}
					rays.addRay(s.center, q, v);
					net.highlight(h, k, l);
				}
	}
	

	public void doLaue() {
		n.set(0, 1, 0);
		e1.set(1, 0, 0);
		e3.set(0, 0, 1);
		precessionClass.apply(n);
		precessionClass.apply(e1);
		precessionClass.apply(e3);
		c.set(0, p3d.y, 0);
		double cn = c.dot(n);
		tOP.mul(precessionClass.t3d, orientationClass.t3d);
		
		for (int h=-net.xMax; h<=net.xMax; h++)
			for (int k=-net.yMax; k<=net.yMax; k++)
				for (int l=-net.zMax; l<=net.zMax; l++) {
					Point3d p = net.points[h+net.xMax][k+net.yMax][l+net.zMax];
					if (p==null) continue;
					q.set(p);
					tOP.transform(q);
					double r = -(q.x*q.x+q.y*q.y+q.z*q.z)/(2*q.y);
					if (Double.isInfinite(r) || Double.isNaN(r) || r<=0d) continue;
					v.set(q.x, q.y+r, q.z);
					if (!p3d.projPoint(v, n, cn)) continue;
					u.sub(v, c);
					u.set(e1.dot(u), n.dot(u), e3.dot(u));
					Point.Double p2d = p3d.proj3dTo2d(u);
					if (p2d==null) continue;
					float intensity = net.intensity(h, k, l);
					projScreen.drawPoint(p2d, intensity, (byte)h, (byte)k, (byte)l);
				}
	}
	
	private void setScreenType(ProjScreen3d s, double h) {
		double w, y;
		if (p3d==null) {
			hCyl = defaultValues.hCylScreen;
			hFlat = defaultValues.hFlatScreen;
			w = defaultValues.wScreen;
			y = defaultValues.zScreen;
		}
		else {
			w=p3d.w;
			y=p3d.y;
			if (p3d instanceof ProjScreen3d.Flat) hFlat = p3d.h;
			else hCyl = p3d.h;
			univers.root.removeChild(p3d);
		}
		p3d = s;
		p3d.setSize(w, h);
		p3d.setPos(y);
		univers.root.addChild(p3d);
	}
	public void setFlatScreen() {
		setScreenType(new ProjScreen3d.Flat(precessionClass), p3d==null?defaultValues.hFlatScreen:hFlat);
		projScreen.setImageSize(p3d.w, p3d.h, false);
	}
	public void setCylindricScreen() {
		setScreenType(new ProjScreen3d.Cylindric(precessionClass), p3d==null?defaultValues.hCylScreen:hCyl);
		projScreen.setImageSize(p3d.y*Math.PI*2, p3d.h, true);
	}
	public void setScreenSize(double w, double h) {
		projScreen.setImageSize((p3d instanceof ProjScreen3d.Flat)?w:(p3d.y*Math.PI*2), h, p3d instanceof ProjScreen3d.Cylindric);
		p3d.setSize(w, h);
	}
	
	public void destroy() {
		if (univers!=null) univers.cleanup();
	}






//public synchronized void doRays3(boolean dummy) {
//	//Debug.clear();
//	
//	clearAllRays();
//	Point3d v = new Point3d();
//	Vector3d u = new Vector3d();
//	Point3d s2 = new Point3d(s.center);
//	Precession.reverse(s2);
//	Orientation.reverse(s2);
//	Vector3d n = new Vector3d(0, 1, 0);
//	Orientation.reverse(n);
//	Vector3d c = new Vector3d(0, p3d.y, 0);
//	Precession.reverse(c);
//	Orientation.reverse(c);
//	Vector3d e1 = new Vector3d(1, 0, 0);
//	Vector3d e3 = new Vector3d(0, 0, 1);
//	Orientation.reverse(e1);
//	Orientation.reverse(e3);
//
//	
////	Debug.vector(Debug.MpMo, e1, c, ColorConstants.yellow, .02);
////	Debug.vector(Debug.MpMo, e3, c, ColorConstants.yellow, .02);
////	Debug.point(Debug.MpMo, c, ColorConstants.blue, .05);
////	Debug.vector(Debug.MpMo, n, c, ColorConstants.blue, .03);
////
////	Debug.vector(Debug.root, e1, c, ColorConstants.yellow, .02);
////	Debug.vector(Debug.root, e3, c, ColorConstants.yellow, .02);
////	Debug.point(Debug.root, c, ColorConstants.blue, .05);
////	Debug.vector(Debug.root, n, c, ColorConstants.blue, .03);
////	Debug.point(Debug.root, s2, ColorConstants.green, .1);
////	Debug.transparentSphere(Debug.root, s2, new Color(.7f, 1f, .7f, .5f), s.radius);
////	Debug.transparentScreen(Debug.root, c, e1, n, e3, 10, 10, new Color3f(.8f, 1f, .8f));
//
//	
//	
//	double cn = c.dot(n);
//	double rMask = Math.sin(Precession.mu)*p3d.y;
//	Point3d cMask = mask3d.center();
//	Precession.reverse(cMask);
//	Orientation.reverse(cMask);
//	
//	for (int h=-net.xMax; h<=net.xMax; h++)
//		for (int k=-net.yMax; k<=net.yMax; k++)
//			for (int l=-net.zMax; l<=net.zMax; l++) {
//				//if (h==0&&k==0&l==0) continue;
//				Point3d p = net.points[h+net.xMax][k+net.yMax][l+net.zMax];
//				double d = s2.distance(p)-s.radius;
//
//				boolean isp = net.isProjected[h+net.xMax][k+net.yMax][l+net.zMax]; 
//				net.isProjected[h+net.xMax][k+net.yMax][l+net.zMax] = false; 
//
//				if (d>0 || Math.abs(d)>DefaultValues.dotSize) continue;
//				
//				
//				//if (Math.abs(d)>DefaultValues.dotSize) continue;
//				//if (net.sgn[h+net.xMax][k+net.yMax][l+net.zMax]==d>0) continue;
//				
//				//Debug.point(Debug.root, p, ColorConstants.green, .05);
//
//				Point3d q = new Point3d(p);
//				Orientation.apply(q);
//				Precession.apply(q);
//				
//				double r = -(q.x*q.x+q.y*q.y+q.z*q.z)/(2*q.y);
//				if (Double.isInfinite(r) || Double.isNaN(r) || r<=0d) continue;
//				
//				Point3d s3 = new Point3d(0, -r, 0);
//				Precession.reverse(s3);
//				Orientation.reverse(s3);
//				
//				
//				v.sub(p, s3);
//
//				
//
//				
//				//Debug.vector(Debug.root, v, s2, ColorConstants.green, .02);
//				
//				if (!p3d.projPoint(v, n, cn)) continue;
//				
//				//Debug.vector(Debug.root, v, Rays.o, ColorConstants.green, .02);
//				
//				u.sub(v, c);
//				
////				Debug.vector(Debug.root, u, c, ColorConstants.orange, .02);
////				Debug.vector(Debug.MpMo, u, c, ColorConstants.magenta, .02);
//
//				u.set(e1.dot(u), n.dot(u), e3.dot(u));
//				
////				Vector3d ppp = new Vector3d(v);
////				Orientation.apply(ppp);
////				Point.Double p2d = p3d.proj3dTo2d(ppp);
//				
//				Point.Double p2d = p3d.proj3dTo2d(u);
//				if (p2d==null) continue;
//
//				// now leave if already projected
//				net.isProjected[h+net.xMax][k+net.yMax][l+net.zMax] = true; 
//				//if (isp) continue;
//				
//				
//				if (mask) d = Math.abs(v.distance(cMask)-rMask);
//				if (mask && d>.05) {
//					v.scale(DefaultValues.maskDistFract);
//				}
//				else {
//					float intensity = net.intensity(h, k, l);
//					double f = Math.abs(p3d.y*2);
//					if (f<8) f = 8;
//					intensity*=f;
//					if (intensity > 1)intensity = 1;
//					//if (intensity < .5f)intensity = .5f;
//					
////					float intensity = 1;
//					
//					projScreen.drawPoint(p2d, intensity, (byte)h, (byte)k, (byte)l);
//				}
//				rays.addRay(s2, p, v);
//				net.highlight(h, k, l);
//			}
//}



//public synchronized void doRays2222() {
//	clearAllRays();
//	double r2m4 = s.radius*s.radius*4;
//	double rMask = Math.sin(Precession.mu)*p3d.y;
//	Point3d cMask = mask3d.center();
//	Point3d from=null, p, proj, antiCenter=null;
//	float intensity;
//	double rAdj, t2, yAdj;
//	
//	
//	if (variant!='l') antiCenter = net.antiRot(s.center);
////	if (variant!='l') {
////		antiCenter = new Point3d(s.center); 
////		Orientation.reverse(antiCenter);
////	}
//	
//	
//	
//	else antiCenter = null;
//	boolean rAdjCalculated = false;
//	for (int h=-net.xMax; h<=net.xMax; h++)
//		for (int k=-net.yMax; k<=net.yMax; k++)
//			for (int l=-net.zMax; l<=net.zMax; l++) {
//				
//				if (variant==' '||variant=='p') { // after a clear screen
//					p = net.getCoord(h, k, l);
//					Precession.apply(p);
//					if (p==null) continue;
//					if (net.dist2(h, k, l)>r2m4) continue;
//					if (p.y>0) continue;
//					if (Math.abs(p.distance(s.center)-s.radius)>DefaultValues.dotSize) continue;
//					rAdj = -(p.x*p.x+p.y*p.y+p.z*p.z)/(2*p.y);
//					if (Double.isInfinite(rAdj) || Double.isNaN(rAdj) || rAdj<=0d) continue;
//					rAdjCalculated = true;
//					if (rAdj+s.center.y>0.001) continue;  // abort if the point is approx
//					from = s.center;
//				}
//				else if (variant=='l') { // movement on lambda
//					if (net.rotChanged) { // we need to recalculate new rotation for each point
//						p = net.getCoord(h, k, l);
//						Precession.apply(p);
//						if (p==null) continue;
//						if (net.dist2(h, k, l)>r2m4) continue;
//						if (p.y>0) continue;
//						if (Math.abs(p.distance(s.center)-s.radius)>DefaultValues.dotSizeBig) continue;
//					}
//					else { // rotations are already calculated
//						if (net.dist2(h, k, l)>r2m4) continue;
//						p = net.getCoord(h, k, l);
//						Precession.apply(p);
//						if (p==null) continue;
//						if (p.y>0) continue;
//						if (Math.abs(p.distance(s.center)-s.radius)>DefaultValues.dotSizeBig) continue;
//					}
//					rAdj = -(p.x*p.x+p.y*p.y+p.z*p.z)/(2*p.y);
//					if (Double.isInfinite(rAdj) || Double.isNaN(rAdj) || rAdj<=0d) continue;
//					rAdjCalculated = true;
//					from = new Point3d(0, -rAdj, 0);
//				}
//				else {
//					if (net.dist2(h, k, l)>r2m4) continue;
//					p = net.getCoordRef(h, k, l);
//					if (p==null) continue;
//
//					
//					
//					if (antiCenter==null) antiCenter = net.antiRot(s.center);
////					if (antiCenter==null) {
////						antiCenter = new Point3d(s.center); 
////						Orientation.reverse(antiCenter);
////					}
//					
//					
//					if (Math.abs(p.distance(antiCenter)-s.radius)>DefaultValues.dotSizeBig) continue;
//					p = net.getCoord(h, k, l);
//					Precession.apply(p);
//					t2 = net.dist2(h, k, l);
//					yAdj = t2/(2*s.radius);
//					p.y = yAdj*(p.y<0?-1:1);
//					p.x = Math.sqrt(t2-yAdj*yAdj-p.z*p.z)*(p.x<0?-1:1);
//					from = s.center;
//				}
//				
//				proj = p3d.getProjPoint(from, p);
//				if (proj==null) continue;
//				if (!p3d.isInside(proj)) continue;
//				
//				boolean masked = false;
//				if (mask) {
//					double d = Math.abs(proj.distance(cMask)-rMask);
//					masked = (d>.1);
//					if (masked) {
//						proj.scale(DefaultValues.maskDistFract);
//					}
//				}
//				if (!mask || !masked) {
//					intensity = net.intensity(h, k, l);
//					//intensity*=3*Math.max(Math.max(net.xMax, net.yMax), net.zMax);
//					double f = Math.abs(p3d.y*2);
//					if (f<8) f = 8;
//					intensity*=f;
//					if (intensity > 1)intensity = 1;
//					Point.Double p2d = p3d.proj3dTo2d(proj);
//					projScreen.drawPoint(p2d, intensity, (byte)h, (byte)k, (byte)l);
//				}
//				rays.addRay(s.center, p, proj);
//				net.highlight(h, k, l);
//			}
//	//p3d.setImage(projScreen.image);
//	//if (variant=='l') net.rotChanged=false;
//	
//	
//	net.rotChanged=true;
//}

//public void doLaue() {
//	Point3d p, proj;
//	double rAdj;
//	float intensity;
//	for (int h=-net.xMax; h<=net.xMax; h++)
//		for (int k=-net.yMax; k<=net.yMax; k++)
//			for (int l=-net.zMax; l<=net.zMax; l++) {
//				p = net.getCoord(h, k, l);
//				Precession.apply(p);
//				if (p==null) continue;
//
//				rAdj = -(p.x*p.x+p.y*p.y+p.z*p.z)/(2*p.y);
//				if (Double.isInfinite(rAdj) || Double.isNaN(rAdj) || rAdj<=0d) continue;
//				intensity = net.intensity(h, k, l);
//				double f = Math.abs(p3d.y*2);
//				if (f<8) f = 8;
//				intensity*=f;
//				if (intensity > 1)intensity = 1;
//				proj = p3d.getProjPoint(new Point3d(0, -rAdj, 0), p);
//				if (proj==null) continue;
//				projScreen.drawPoint(p3d.proj3dTo2d(proj), intensity, (byte)h, (byte)k, (byte)l);
//			}
//}


}