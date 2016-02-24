import java.awt.Dimension;
import java.awt.Graphics;
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
	public boolean isParallel;
	private int showRootDept;
	private Model model;

	public Univers(Model model) {
		this.model = model;
		this.canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()) {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Toolkit.getDefaultToolkit().sync();
			}
		};
		this.u = new SimpleUniverse(this.canvas3D);

		this.showRootDept = 0;
		this.isParallel = false;

		// this is the root for all objects in the scene
		this.objRoot = new BranchGroup();
		this.objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		Transform3D t = new Transform3D();
		t.set(new Vector3d(0, 0, -10));
		this.objRootTemp = new TransformGroup(t);
		this.objRootTemp.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.objRootTemp.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		// create the environment
		this.createEnvironment();
		this.environment.addChild(this.objRootTemp);
		this.environment.setCapability(BranchGroup.ALLOW_DETACH);
		this.environment.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.environment.setCapability(Group.ALLOW_CHILDREN_WRITE);

		this.objRootTemp.addChild(this.objRoot);

		// Create the bounding leaf node
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
		BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
		this.environment.addChild(boundingLeaf);

		// Create the transform group node
		this.transformGroup = new TransformGroup();
		this.transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.transformGroup2 = new TransformGroup();
		this.transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.transformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.objRoot.addChild(this.transformGroup2);
		this.transformGroup2.addChild(this.transformGroup);

		// transformGroup2.addChild(new Help3d());

		this.behavior = new PickDragBehavior(this, model);
		this.behavior.setSchedulingBounds(bounds);
		this.transformGroup.addChild(this.behavior);
		this.behavior.addRotations(-Math.PI / 2d, -Math.PI / 2d, 0);
		this.canvas3D.addMouseWheelListener(this.behavior);

		// add mouse behaviors to the ViewingPlatform
		ViewingPlatform viewingPlatform = this.u.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();
	}

	public void show() {
		this.u.addBranchGraph(this.environment);
	}

	public void showRoot() {
		this.showRootDept++;
		if (this.showRootDept == 0)
			this.objRootTemp.addChild(this.objRoot);
	}

	public void hideRoot() {
		if (this.showRootDept == 0) {
			this.objRootTemp.removeChild(this.objRoot);
			try {
				Thread.sleep(20);
			} catch (Exception e) {
			}
		}
		this.showRootDept--;
	}

	public BufferedImage getScreenShot() {
		GraphicsContext3D ctx = this.canvas3D.getGraphicsContext3D();
		Dimension scrDim = this.canvas3D.getSize();
		// setting raster component
		ImageComponent2D ic = new ImageComponent2D(ImageComponent.FORMAT_RGB,
				new BufferedImage(scrDim.width, scrDim.height, BufferedImage.TYPE_INT_RGB));
		Raster ras = new Raster(new Point3f(-1f, -1f, -1f), Raster.RASTER_COLOR, 0, 0, scrDim.width, scrDim.height, ic,
				null);
		ctx.readRaster(ras);
		return ras.getImage().getImage();
	}

	public void parallelUnivers(boolean b) {
		boolean old_b = this.u.getViewer().getView().getProjectionPolicy() == View.PARALLEL_PROJECTION;

		this.hideRoot();

		this.u.getViewer().getView().setProjectionPolicy(b ? View.PARALLEL_PROJECTION : View.PERSPECTIVE_PROJECTION);
		if (old_b != b) {
			Transform3D modelTrans = this.getGlobalTransform();
			Transform3D t = new Transform3D();
			t.set(b ? .2 : 5);
			modelTrans.mul(t, modelTrans);
			this.setGlobalTransform(modelTrans);
		}

		this.showRoot();
		this.isParallel = b;
	}

	public BranchGroup getRoot() {
		return this.environment;
	}

	public void setGlobalTransform(Transform3D t) {
		this.transformGroup.setTransform(t);
	}

	public Transform3D getGlobalTransform() {
		Transform3D t = new Transform3D();
		this.transformGroup.getTransform(t);
		return t;
	}

	public void addChild(Node node) {
		this.transformGroup.addChild(node);
	}

	public Canvas3D getCanvas() {
		return this.canvas3D;
	}

	public void reset() {
		this.behavior.reset(this.model.cell);
	}

	public void setBackgroundColor(Color3f bgColor) {
		this.bgNode.setColor(bgColor);
	}

	public void cleanup() {
		this.u.cleanup();
	}

	private void createEnvironment() {
		// Create the root of the branch graph
		this.environment = new BranchGroup();

		// Create a bounds for the background and lights
		this.bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

		// create the background
		this.bgNode = new Background(black);
		this.bgNode.setCapability(Background.ALLOW_COLOR_WRITE);
		this.bgNode.setApplicationBounds(this.bounds);
		this.environment.addChild(this.bgNode);

		// Set up the ambient light
		Color3f ambientColor = new Color3f(0.4f, 0.4f, 0.4f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(this.bounds);
		this.environment.addChild(ambientLightNode);

		// Set up the directional lights
		Color3f light1Color = new Color3f(0.7f, 0.7f, 0.7f);
		Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f light2Color = new Color3f(0.7f, 0.7f, 0.7f);
		Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

		DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(this.bounds);
		this.environment.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(light2Color, light2Direction);
		light2.setInfluencingBounds(this.bounds);
		this.environment.addChild(light2);
	}

}
