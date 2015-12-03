import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Node;
import javax.media.j3d.Raster;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Univers implements ColorConstants {
	private Canvas3D canvas3D;
	private SimpleUniverse u;
	private BranchGroup environment;
	private BranchGroup objRoot;
	private TransformGroup transformGroup, transformGroup2;
	private PickDragBehavior behavior;
	private BoundingSphere bounds;
	private Background bgNode;
	private TransformGroup objRootTemp;
	private int modelModify;
	public boolean isParallel;
	private int showRootDept;
	private Model model;
	
	public Univers(Model model) {
		this.model = model;
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()) {
	     public void paint(Graphics g) {
	        super.paint(g);
	        Toolkit.getDefaultToolkit().sync();
	    }
	  };
		u = new SimpleUniverse(canvas3D);
		
		showRootDept = 0;
		isParallel = false;
		
		// this is the root for all objects in the scene
		objRoot = new BranchGroup();
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);
		
		Transform3D t = new Transform3D();
		t.set(new Vector3d(0, 0, -10));
		objRootTemp = new TransformGroup(t);
		objRootTemp.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		objRootTemp.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
		// create the environment
		createEnvironment();
		environment.addChild(objRootTemp);
		environment.setCapability(BranchGroup.ALLOW_DETACH);
		environment.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		environment.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		objRootTemp.addChild(objRoot);
		
		
		// Create the bounding leaf node
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 1000.0);
		BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
		environment.addChild(boundingLeaf);

		// Create the transform group node
		transformGroup = new TransformGroup();
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		transformGroup2 = new TransformGroup();
		transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRoot.addChild(transformGroup2);
		transformGroup2.addChild(transformGroup);
		
		//transformGroup2.addChild(new Help3d());
		
		
		behavior = new PickDragBehavior(this, model);
		behavior.setSchedulingBounds(bounds);
		transformGroup.addChild(behavior);
		behavior.addRotations(-Math.PI/2d, -Math.PI/2d, 0);
		canvas3D.addMouseWheelListener(behavior);

		// add mouse behaviors to the ViewingPlatform
		ViewingPlatform viewingPlatform = u.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();
	}

	
	public void show() {
		u.addBranchGraph(environment);
		modelModify = 0;
	}

	public void showRoot() {
		showRootDept++;
		if (showRootDept==0) {
			objRootTemp.addChild(objRoot);
		}
	}
	public void hideRoot() {
		if (showRootDept==0) {
			objRootTemp.removeChild(objRoot);
			try {
				Thread.sleep(20);
			} catch (Exception e) {}
		}
		showRootDept--;
	}
	
	
	public BufferedImage getScreenShot() {
		GraphicsContext3D ctx = canvas3D.getGraphicsContext3D();
		Dimension scrDim = canvas3D.getSize();
		// setting raster component
		ImageComponent2D ic = new ImageComponent2D(ImageComponent.FORMAT_RGB, new BufferedImage(scrDim.width, scrDim.height, BufferedImage.TYPE_INT_RGB));
		Raster ras = new Raster(new Point3f(-1f, -1f, -1f), Raster.RASTER_COLOR, 0, 0, scrDim.width, scrDim.height, ic, null);
		ctx.readRaster(ras);
		return ras.getImage().getImage();
	}
	
	
	
	public void parallelUnivers(boolean b) {
		boolean old_b = u.getViewer().getView().getProjectionPolicy()==View.PARALLEL_PROJECTION;
		
		hideRoot();
		
		u.getViewer().getView().setProjectionPolicy(b?View.PARALLEL_PROJECTION:View.PERSPECTIVE_PROJECTION);
		if (old_b!=b) {
			Transform3D modelTrans = getGlobalTransform();
			Transform3D t = new Transform3D();
			t.set(b?.2:5);
	    modelTrans.mul(t, modelTrans);
	    setGlobalTransform(modelTrans);
		}

		showRoot();
		isParallel = b;
	}

	
	public BranchGroup getRoot() {
		return environment;
	}
	
	public void setGlobalTransform(Transform3D t) {
    transformGroup.setTransform(t);
	}

	public Transform3D getGlobalTransform() {
		Transform3D t = new Transform3D();
		transformGroup.getTransform(t);
		return t;
	}
	
	public void addChild(Node node) {
		transformGroup.addChild(node);
	}
	
	public Canvas3D getCanvas() {
		return canvas3D;
	}
		
	public void reset() {
		behavior.reset(model.cell);
	}
	
	public void setBackgroundColor(Color3f bgColor) {
		bgNode.setColor(bgColor);
	}
	
	public void cleanup() {
		u.cleanup();
		}
	
	private void createEnvironment() {
		// Create the root of the branch graph
		environment = new BranchGroup();
         
		// Create a bounds for the background and lights
		bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

		// create the background
		bgNode = new Background(black);
		bgNode.setCapability(Background.ALLOW_COLOR_WRITE);
		bgNode.setApplicationBounds(bounds);
		environment.addChild(bgNode);

		// Set up the ambient light
		Color3f ambientColor = new Color3f(0.4f, 0.4f, 0.4f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		environment.addChild(ambientLightNode);

		// Set up the directional lights
		Color3f light1Color = new Color3f(0.7f, 0.7f, 0.7f);
		Vector3f light1Direction  = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f light2Color = new Color3f(0.7f, 0.7f, 0.7f);
		Vector3f light2Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);

		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		environment.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
		light2.setInfluencingBounds(bounds);
		environment.addChild(light2);
	}
	
}


