package model3d;

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
		canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration()) {
	     public void paint(Graphics g) {
	        super.paint(g);
	        Toolkit.getDefaultToolkit().sync();
	    }
	  };
		u = new SimpleUniverse(canvas3D);
		
		// this is the root for all objects in the scene
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		
		// this transform group will receive the mouse actions
		tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(root);

		// this transform group will receive the reset transform
		reset = new TransformGroup();
		reset.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		reset.addChild(tg);

		// reset the transform group
		reset();
		
		// create the environment (lights, background, ...)
		createEnvironment();
		environment.addChild(reset);

		// the behavior reacts on mouse events 
		UniversBehavior behavior = new UniversBehavior();
		behavior.setSchedulingBounds(bounds);
		root.addChild(behavior);

		// also use mouse wheel to scale up/down
		canvas3D.addMouseWheelListener(new WheelMouseBehavior());

		// set the ViewingPlatform
		ViewingPlatform viewingPlatform = u.getViewingPlatform();
		viewingPlatform.setNominalViewingTransform();

		// show the whole thing up
		u.addBranchGraph(environment);
	}
	
	public boolean isParallel() {
		return u.getViewer().getView().getProjectionPolicy()==View.PARALLEL_PROJECTION;
	}
	
	public void setParallel(boolean b) {
		u.getViewer().getView().setProjectionPolicy(b?View.PARALLEL_PROJECTION:View.PERSPECTIVE_PROJECTION);
	}

	public Canvas3D getCanvas() {
		return canvas3D;
	}

	public void applyTransform(Transform3D t3d) {
		Transform3D cur = new Transform3D();
		tg.getTransform(cur);
		cur.mul(t3d, cur);
		tg.setTransform(cur);
	}

	public void rotX(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotX(angle*Math.PI/180);
		applyTransform(t3d);
	}
	public void rotY(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotY(angle*Math.PI/180);
		applyTransform(t3d);
	}
	public void rotZ(double angle) {
		Transform3D t3d = new Transform3D();
		t3d.rotZ(angle*Math.PI/180);
		applyTransform(t3d);
	}
	
	public void scale(double s) {
		Transform3D t3d = new Transform3D();
		t3d.set(s);
		applyTransform(t3d);
	}
	
	public void reset() {
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3d(0, 0, -5), .2);
		reset.setTransform(t3d);
	}
	
	public void setBackgroundColor(Color3f bgColor) {
		background.setColor(bgColor);
	}
	
	public void cleanup() {
		u.cleanup();
	}
	
	private void createEnvironment() {
		// Create the root of the branch graph
		environment = new BranchGroup();
         
		// Create a bounds for the background and lights
		bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.POSITIVE_INFINITY);

		// create the background
		background = new Background(new Color3f(1, 1, 1));
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setApplicationBounds(bounds);
		environment.addChild(background);

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

		BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
		environment.addChild(boundingLeaf);
	}
	
	private class WheelMouseBehavior implements MouseWheelListener {
		private Transform3D t3d = new Transform3D();

		public void mouseWheelMoved(MouseWheelEvent e) {
	    int i;
	    if (e.getWheelRotation()==0) i = 0;
	    else i = e.getWheelRotation()/Math.abs(e.getWheelRotation());
	  	t3d.set(1.0+((double)i)/10d);
      applyTransform(t3d);
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
			t3d = new Transform3D();
			mouseEvents = new WakeupCriterion[3];
      mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
      mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
			mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
      mouseCriterion = new WakeupOr(mouseEvents);
      wakeupOn(mouseCriterion);
		}
		public void processStimulus(Enumeration criteria) {
      WakeupCriterion wakeup;
      AWTEvent[] events;
      MouseEvent e;

      while (criteria.hasMoreElements()) {
         wakeup = (WakeupCriterion) criteria.nextElement();
         if (wakeup instanceof WakeupOnAWTEvent) {
            events = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
            for (int i=0; i<events.length; i++) { 
            	if (events[i] instanceof MouseEvent) {
            		e = (MouseEvent)events[i];
              	switch (e.getID()) {
            			case MouseEvent.MOUSE_PRESSED:
                    x = x_last = e.getX();
                  	y = y_last = e.getY();
                  	Selectable s = getPointedObject();
                  	if (s!=null) s.click();
            				break;
            			case MouseEvent.MOUSE_DRAGGED:
                    x = e.getX();
                  	y = e.getY();
	                  dx = x - x_last;
	                  dy = y - y_last;
	                  if (e.isAltDown()) {
	                  	t3d.set(1.0+dy/100d);
	                    applyTransform(t3d);
	                  }
	                  else if (e.isShiftDown()) {
	                  	int w = canvas3D.getWidth();
	                  	int h = canvas3D.getHeight();
	                  	Vector2d p1 = new Vector2d(x_last-w/2, y_last-h/2);
	                  	Vector2d p2 = new Vector2d(x-w/2, y-h/2);
	                  	Vector2d p3 = new Vector2d(1, 0);
	                  	boolean neg = p1.angle(p3)<p2.angle(p3);
	                  	neg = y-h/2>0?!neg:neg;
	                  	double alpha = p1.angle(p2);
	                    t3d.rotZ(neg?alpha:-alpha);
	                    applyTransform(t3d);
	                  }
	                  else if (e.isMetaDown() || e.isControlDown() || e.isAltGraphDown()) {
	                  	// TODO selection multiple
	                  	t3d.set(1.0+dy/100d);
	                    applyTransform(t3d);
	                  }
	                  else {
	                    t3d.rotX(dy*y_factor);
	                    applyTransform(t3d);
	                    t3d.rotY(dx*x_factor);
	                    applyTransform(t3d);
	                  }
	                  x_last = x;
	                  y_last = y;
            				break;
            			case MouseEvent.MOUSE_RELEASED:
            				break;
              	}
            	}
            }
         }
      }
      wakeupOn (mouseCriterion);
		}
		
		public Selectable getPointedObject() {
		 	Point3d mousePos = new Point3d();
			Transform3D plateTovWorldT3d = new Transform3D();
			Point3d eyePos = new Point3d();
		  PickRay pickRay = new PickRay();
		  SceneGraphPath sceneGraphPath[];

			canvas3D.getCenterEyeInImagePlate(eyePos);
			canvas3D.getPixelLocationInImagePlate(x, y, mousePos);
			canvas3D.getImagePlateToVworld(plateTovWorldT3d);

			plateTovWorldT3d.transform(eyePos);
			plateTovWorldT3d.transform(mousePos);
			
			Vector3d mouseVec;
			if (isParallel()) {
			   mouseVec = new Vector3d(0.f, 0.f, -1.f);
			}
			else {
			   mouseVec = new Vector3d();
			   mouseVec.sub(mousePos, eyePos);
			   mouseVec.normalize();
			}
			pickRay.set(mousePos, mouseVec);
			sceneGraphPath = root.pickAllSorted(pickRay);
	 
			if (sceneGraphPath != null) {
			   for (int j=0; j<sceneGraphPath.length; j++) {
				  if (sceneGraphPath[j] != null) {
					 Node node = sceneGraphPath[j].getObject();
					 if (node instanceof Shape3D) {
					 	 try {
					 	 	double dist[] = {0.0}; 
								boolean isRealHit = (( Shape3D) node).intersect(sceneGraphPath[j], pickRay, dist); 
							 	if (isRealHit) {
									Object userData = node.getUserData();
									if (userData != null && userData instanceof Selectable) {
										return (Selectable) userData;
									}
							 	}
							}
							catch (CapabilityNotSetException e) {}
					 }
				  }
			   }
			}
			return null;
		}
	}
}


