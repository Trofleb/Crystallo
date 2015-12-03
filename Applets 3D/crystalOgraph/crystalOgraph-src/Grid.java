import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Geometry;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

/*
 * Created on 21 juin 2004
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
public class Grid extends BranchGroup implements ColorConstants {
	static final Color3f ambWhite = new Color3f(0.3f, 0.3f, 0.3f);
	static final Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);

	private TransformGroup root;
	private BranchGroup grid, repere;
	int granx, grany, granz;
	BranchGroup rootBranch;
	boolean showed;
	Cell cell;
	
	public Grid(Cell cell, int granx, int grany, int granz) {
		super();
		this.cell=cell;
		
		setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		root = new TransformGroup();
		root.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		rootBranch = new BranchGroup();
		rootBranch.setCapability(BranchGroup.ALLOW_DETACH);
		
		this.granx=granx;
		this.grany=grany;
		this.granz=granz;
		
		reset();
		show();
		rootBranch.addChild(root);
		
	}

	
	public void reset() {
		createGrid();
		showRepere();
	}
	
	public void show() {
		if (showed) return;
		addChild(rootBranch);
		showed=true;
	}

	public void hide() {
		showed=false;
		removeAllChildren();
	}
	
	public void setGranularity(int granx, int grany, int granz) {
		this.granx=granx;
		this.grany=grany;
		this.granz=granz;
		createGrid();
	}

	private void createGrid() {
		if (grid!=null) {
			root.removeChild(grid);
		}
		grid = new BranchGroup();
		grid.setCapability(BranchGroup.ALLOW_DETACH);
		
//		Appearance whiteApp = createApp(white);
//		Appearance cyanApp = createApp(cyan);
//		Appearance greenApp = createApp(green);

		//System.out.println(cell.a);
		//double r = Math.min(Math.min(cell.a, cell.b), cell.c)/200d;
		
		Appearance app = new Appearance();
		Material mat = new Material(white, black, white, white, 128);
		app.setMaterial(mat);
		mat.setLightingEnable(true);
		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .7f);
		app.setTransparencyAttributes(transp);
    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_NONE);
    pa.setBackFaceNormalFlip(false);
    app.setPolygonAttributes(pa);
		
		
		Box c = new Box(.5f, .5f, .5f, app);
		Transform3D t3d1 = new Transform3D();
		t3d1.set(cell.matrix);
		TransformGroup tg1 = new TransformGroup(t3d1);
		tg1.addChild(c);
		grid.addChild(tg1);
		
		root.addChild(grid);
	}
	
	
	private void createGrid22() {
		if (grid!=null) {
			root.removeChild(grid);
		}
		grid = new BranchGroup();
		grid.setCapability(BranchGroup.ALLOW_DETACH);
		
		Appearance whiteApp = createApp(white);
		Appearance cyanApp = createApp(cyan);
		Appearance greenApp = createApp(green);

		//System.out.println(cell.a);
		double r = Math.min(Math.min(cell.a, cell.b), cell.c)/200d;
		
		Appearance app = new Appearance();
		app.setMaterial(new Material(white, white, black, white, 120.0f));
		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .85f);
		app.setTransparencyAttributes(transp);

		Box c = new Box(.5f, .5f, .5f, app);
		Transform3D t3d1 = new Transform3D();
		t3d1.set(cell.matrix);
		TransformGroup tg1 = new TransformGroup(t3d1);
		tg1.addChild(c);
		grid.addChild(tg1);
		
		double gx = 1d/((double)0+1);
		double gy = 1d/((double)0+1);
		double gz = 1d/((double)0+1);

		
		for (double i=0; i<=1; i+=gz) {
			for (double j=0; j<=1; j+=gy) {
				for (double k=0; k<=1; k+=gx) {
					Vector3d vLog = new Vector3d(k, j, i);
					Vector3d vPhy = new Vector3d(cell.coord(k, j, i));
					Transform3D transform3D = new Transform3D();
					transform3D.set(vPhy);
					TransformGroup transformGroup = new TransformGroup(transform3D);
					
					if (i+j+k==0) {
						Sphere s = new Sphere((float)r*3, Sphere.GENERATE_NORMALS|Sphere.ENABLE_APPEARANCE_MODIFY, 10, greenApp);
						Shape3D shape = s.getShape(); 
						
						//System.out.println(shape.getGeometry().isCompiled()+" "+shape.getGeometry().isLive());
						
						shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
						
						shape.setUserData(new GridNode(new Point3d(vLog))); 
						transformGroup.addChild(s);
						grid.addChild(transformGroup);
					}
					
					if (k!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k-gx, j, i)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i==0||i==1)&&(j==0||j==1)&&(k-gx==0||k-gx==1)?whiteApp:cyanApp, 5));
					if (j!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k, j-gy, i)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i==0||i==1)&&(j-gy==0||j-gy==1)&&(k==0||k==1)?whiteApp:cyanApp, 5));
					if (i!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k, j, i-gz)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i-gz==0||i-gz==1)&&(j==0||j==1)&&(k==0||k==1)?whiteApp:cyanApp, 5));
			  }
		   }
		}
		//grid.compile();
		root.addChild(grid);
	}

	public void createGrid23() {
		if (grid!=null) {
			root.removeChild(grid);
		}
		grid = new BranchGroup();

		
		Appearance whiteApp = createApp(white);
//		Appearance cyanApp = createApp(cyan);
//		Appearance greenApp = createApp(green);

		Appearance app = new Appearance();
		app.setMaterial(new Material(white, white, black, white, 120.0f));
		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .85f);
		app.setTransparencyAttributes(transp);

		double r = Math.min(Math.min(cell.a, cell.b), cell.c)/200d;
		
		double gx = 1d/((double)granx+1);
		double gy = 1d/((double)grany+1);
		double gz = 1d/((double)granz+1);

		for (double i=0; i<=1; i+=gz) {
			for (double j=0; j<=1; j+=gy) {
				for (double k=0; k<=1; k+=gx) {
					Vector3d vLog = new Vector3d(k, j, i);
					Vector3d vPhy = new Vector3d(cell.coord(k, j, i));
					Transform3D transform3D = new Transform3D();
					transform3D.set(vPhy);
					TransformGroup transformGroup = new TransformGroup(transform3D);

					Box c = new Box(.05f, .05f, .05f, app);
					
/*					
					Sphere s = new Sphere(.02f, Sphere.GENERATE_NORMALS |
							  Sphere.ENABLE_APPEARANCE_MODIFY,
							  10, i+j+k==0?greenApp:(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)?whiteApp:cyanApp);
*/	
					Shape3D shape;

					shape = c.getShape(0); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 
					shape = c.getShape(1); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 
					shape = c.getShape(2); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 
					shape = c.getShape(3); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 
					shape = c.getShape(4); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 
					shape = c.getShape(5); 
					shape.getGeometry().setCapability(Geometry.ALLOW_INTERSECT);
					shape.setUserData(new GridNode(new Point3d(vLog))); 

					transformGroup.addChild(c);
					grid.addChild(transformGroup);
					if (k!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k-gx, j, i)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i==0||i==1)&&(j==0||j==1)&&(k-gx==0||k-gx==1)?whiteApp:app, 3));
					if (j!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k, j-gy, i)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i==0||i==1)&&(j-gy==0||j-gy==1)&&(k==0||k==1)?whiteApp:app, 3));
					if (i!=0) grid.addChild(AtomLink.createCylinder(new Point3d(vPhy), new Point3d(cell.coord(k, j, i-gz)), r, /*i+j==0||i+k==0||j+k==0?greenApp:*/(i==0||i==1)&&(j==0||j==1)&&(k==0||k==1)||(i-gz==0||i-gz==1)&&(j==0||j==1)&&(k==0||k==1)?whiteApp:app, 3));
			  }
		   }
		}
		//grid.compile();
		root.addChild(grid);
	}

	private Appearance createApp(Color3f color) {
		Appearance app = new Appearance();
		Material mat = new Material(ambWhite, black, color, specular, 100f);
		mat.setLightingEnable(true);
		app.setMaterial(mat);
		return app;
	}

	private void showRepere() {
		Appearance app = createApp(green);
		
		if (repere!=null) root.removeChild(repere);
		repere = new BranchGroup();
		repere.setCapability(BranchGroup.ALLOW_DETACH);

		double r = Math.min(Math.min(cell.a, cell.b), cell.c)/100d;
		double d = Math.min(Math.min(cell.a, cell.b), cell.c)/4d;
		//double e = Math.min(Math.min(cell.a, cell.b), cell.c)/4d;
		
		Point3d o = new Point3d(cell.coord(0, 0, 0));
		repere.addChild(AtomLink.createArrow(o, distance(o, cell.coord(new Point3d(.25, 0, 0)), d), r, app, 12));
		repere.addChild(AtomLink.createArrow(o, distance(o, cell.coord(new Point3d(0, .25, 0)), d), r, app, 12));
		repere.addChild(AtomLink.createArrow(o, distance(o, cell.coord(new Point3d(0, 0, .25)), d), r, app, 12));
		
		repere.addChild(Atom.createLegend("x", distance(o, cell.coord(new Point3d(.3, -.05, -.05)), d+d/5), new Point3d(.05, -.05, -.05), (float)d/5f, app));
		repere.addChild(Atom.createLegend("y", distance(o, cell.coord(new Point3d(-.05, .3, -.05)), d+d/5), new Point3d(-.05, .05, -.05), (float)d/5f, app));
		repere.addChild(Atom.createLegend("z", distance(o, cell.coord(new Point3d(-.05, -.05, .3)), d+d/5), new Point3d(-.05, -.05, .05), (float)d/5f, app));
		
		repere.compile();
		root.addChild(repere);
	}
	

	private Point3d distance(Tuple3d p1, Tuple3d p2, double l) {
		Vector3d v = new Vector3d();
		v.sub(p2, p1);
		v.normalize();
		v.scale(l);
		p2.add(p1, v);
		return new Point3d(p2);
	}
	
	
}



