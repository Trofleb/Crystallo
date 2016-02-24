package engine3D;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Enumeration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingLeaf;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.PickRay;
import javax.media.j3d.SceneGraphPath;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Univers {
	private Canvas3D canvas3D;
	private SimpleUniverse u;
	private BranchGroup environment;
	private TransformGroup tg, reset;
	private BoundingSphere bounds;
	private Background background;
	public BranchGroup root;

	public Univers() {
		this.canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()) {
			public void paint(Graphics g) {
				super.paint(g);
				Toolkit.getDefaultToolkit().sync();
			}
		};
		this.u = new SimpleUniverse(this.canvas3D);

		// this is the root for all objects in the scene
		this.root = new BranchGroup();
		this.root.setCapability(Group.ALLOW_CHILDREN_READ);
		this.root.setCapability(Group.ALLOW_CHILDREN_WRITE);
		this.root.setCapability(Group.ALLOW_CHILDREN_EXTEND);

		// this transform group will receive the mouse actions
		this.tg = new TransformGroup();
		this.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		this.tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.tg.addChild(this.root);

		// this transform group will receive the reset transform
		this.reset = new TransformGroup();
		this.reset.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		this.reset.addChild(this.tg);

		// reset the transform group
		this.reset();

		// create the environment (lights, background, ...)
		this.createEnvironment();
		this.environment.addChild(this.reset);

		// the behavior reacts on mouse events
		UniversBehavior behavior = new UniversBehavior();
		behavior.setSchedulingBounds(this.bounds);
		this.root.addChild(behavior);

		// also use mouse wheel to scale up/down
		this.canvas3D.addMouseWheelListener(new WheelMouseBehavior());

		// set the ViewingPlatform
		ViewingPlatform viewingPlatform = this.u.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();

		// show the whole thing up
		this.u.addBranchGraph(this.environment);
	}

	public boolean isParallel() {
		return this.u.getViewer().getView().getProjectionPolicy() == View.PARALLEL_PROJECTION;
	}

	public void setParallel(boolean b) {
		this.u.getViewer().getView().setProjectionPolicy(b ? View.PARALLEL_PROJECTION : View.PERSPECTIVE_PROJECTION);
	}

	public Canvas3D getCanvas() {
		return this.canvas3D;
	}

	public void applyTransform(Transform3D t3d) {
		Transform3D cur = new Transform3D();
		this.tg.getTransform(cur);
		cur.mul(t3d, cur);
		this.tg.setTransform(cur);
	}

	public void rotX(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotX(angle * Math.PI / 180);
		this.applyTransform(t3d);
	}

	public void rotY(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotY(angle * Math.PI / 180);
		this.applyTransform(t3d);
	}

	public void rotZ(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotZ(angle * Math.PI / 180);
		this.applyTransform(t3d);
	}

	public void scale(double s) {
		Transform3D t3d = new Transform3D();
		t3d.set(s);
		this.applyTransform(t3d);
	}

	public void reset() {
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3d(0, 0, -5), .2);
		this.reset.setTransform(t3d);
	}

	public void setBackgroundColor(Color3f bgColor) {
		this.background.setColor(bgColor);
	}

	public void cleanup() {
		this.u.cleanup();
	}

	private void createEnvironment() {
		// Create the root of the branch graph
		this.environment = new BranchGroup();

		// Create a bounds for the background and lights
		this.bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY);

		// create the background
		this.background = new Background(new Color3f(1, 1, 1));
		this.background.setCapability(Background.ALLOW_COLOR_WRITE);
		this.background.setApplicationBounds(this.bounds);
		this.environment.addChild(this.background);

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

		BoundingLeaf boundingLeaf = new BoundingLeaf(this.bounds);
		this.environment.addChild(boundingLeaf);
	}

	private class WheelMouseBehavior implements MouseWheelListener {
		private Transform3D t3d = new Transform3D();

		public void mouseWheelMoved(MouseWheelEvent e) {
			int i;
			if (e.getWheelRotation() == 0)
				i = 0;
			else
				i = e.getWheelRotation() / Math.abs(e.getWheelRotation());
			this.t3d.set(1.0 + (i) / 10d);
			Univers.this.applyTransform(this.t3d);
		}
	}

	public interface Selectable {
		public void click();
	}

	private class UniversBehavior extends Behavior {
		private static final double x_factor = .02;
		private static final double y_factor = .02;

		private WakeupCriterion[] mouseEvents;
		private WakeupOr mouseCriterion;
		private int x, y, x_last, y_last, dx, dy;
		private Transform3D t3d;

		public void initialize() {
			this.t3d = new Transform3D();
			this.mouseEvents = new WakeupCriterion[3];
			this.mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
			this.mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
			this.mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
			this.mouseCriterion = new WakeupOr(this.mouseEvents);
			this.wakeupOn(this.mouseCriterion);
		}

		public void processStimulus(Enumeration criteria) {
			WakeupCriterion wakeup;
			AWTEvent[] events;
			MouseEvent e;

			while (criteria.hasMoreElements()) {
				wakeup = (WakeupCriterion) criteria.nextElement();
				if (wakeup instanceof WakeupOnAWTEvent) {
					events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
					for (int i = 0; i < events.length; i++)
						if (events[i] instanceof MouseEvent) {
							e = (MouseEvent) events[i];
							switch (e.getID()) {
								case MouseEvent.MOUSE_PRESSED:
									this.x = this.x_last = e.getX();
									this.y = this.y_last = e.getY();
									Selectable s = this.getPointedObject();
									if (s != null)
										s.click();
									break;
								case MouseEvent.MOUSE_DRAGGED:
									this.x = e.getX();
									this.y = e.getY();
									this.dx = this.x - this.x_last;
									this.dy = this.y - this.y_last;
									if (e.isAltDown()) {
										this.t3d.set(1.0 + this.dy / 100d);
										Univers.this.applyTransform(this.t3d);
									} else if (e.isShiftDown()) {
										int w = Univers.this.canvas3D.getWidth();
										int h = Univers.this.canvas3D.getHeight();
										Vector2d p1 = new Vector2d(this.x_last - w / 2, this.y_last - h / 2);
										Vector2d p2 = new Vector2d(this.x - w / 2, this.y - h / 2);
										Vector2d p3 = new Vector2d(1, 0);
										boolean neg = p1.angle(p3) < p2.angle(p3);
										neg = this.y - h / 2 > 0 ? !neg : neg;
										double alpha = p1.angle(p2);
										this.t3d.rotZ(neg ? alpha : -alpha);
										Univers.this.applyTransform(this.t3d);
									} else if (e.isMetaDown() || e.isControlDown() || e.isAltGraphDown()) {
										// TODO selection multiple
										this.t3d.set(1.0 + this.dy / 100d);
										Univers.this.applyTransform(this.t3d);
									} else {
										this.t3d.rotX(this.dy * y_factor);
										Univers.this.applyTransform(this.t3d);
										this.t3d.rotY(this.dx * x_factor);
										Univers.this.applyTransform(this.t3d);
									}
									this.x_last = this.x;
									this.y_last = this.y;
									break;
								case MouseEvent.MOUSE_RELEASED:
									break;
							}
						}
				}
			}
			this.wakeupOn(this.mouseCriterion);
		}

		public Selectable getPointedObject() {
			Point3d mousePos = new Point3d();
			Transform3D plateTovWorldT3d = new Transform3D();
			Point3d eyePos = new Point3d();
			PickRay pickRay = new PickRay();
			SceneGraphPath sceneGraphPath[];

			Univers.this.canvas3D.getCenterEyeInImagePlate(eyePos);
			Univers.this.canvas3D.getPixelLocationInImagePlate(this.x, this.y, mousePos);
			Univers.this.canvas3D.getImagePlateToVworld(plateTovWorldT3d);

			plateTovWorldT3d.transform(eyePos);
			plateTovWorldT3d.transform(mousePos);

			Vector3d mouseVec;
			if (Univers.this.isParallel())
				mouseVec = new Vector3d(0.f, 0.f, -1.f);
			else {
				mouseVec = new Vector3d();
				mouseVec.sub(mousePos, eyePos);
				mouseVec.normalize();
			}
			pickRay.set(mousePos, mouseVec);
			sceneGraphPath = Univers.this.root.pickAllSorted(pickRay);

			if (sceneGraphPath != null)
				for (int j = 0; j < sceneGraphPath.length; j++)
					if (sceneGraphPath[j] != null) {
						Node node = sceneGraphPath[j].getObject();
						if (node instanceof Shape3D)
							try {
								double dist[] = { 0.0 };
								boolean isRealHit = ((Shape3D) node).intersect(sceneGraphPath[j], pickRay, dist);
								if (isRealHit) {
									Object userData = node.getUserData();
									if (userData != null && userData instanceof Selectable)
										return (Selectable) userData;
								}
							} catch (CapabilityNotSetException e) {
							}
					}
			return null;
		}
	}
}
