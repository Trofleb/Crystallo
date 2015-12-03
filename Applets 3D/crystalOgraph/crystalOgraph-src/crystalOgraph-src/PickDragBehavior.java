/*
 *	@(#)PickDragBehavior.java 1.10 02/04/01 15:03:32
 *
 * Copyright (c) 1996-2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN
 * OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed,licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Class:       PickDragBehavior
 * 
 * Description: Used to respond to mouse pick and drag events
 *              in the 3D window.
 *
 * Version:     1.0
 *
 */
public class PickDragBehavior extends Behavior implements MouseWheelListener {
   WakeupCriterion[] mouseEvents;
   WakeupOr mouseCriterion;
   int x, y;
   int x_last, y_last;
   double x_angle, y_angle, z_angle;
   double x_factor, y_factor;
   //Transform3D modelTrans;
   Transform3D transformX;
   Transform3D transformY;
   Canvas3D canvas3D;
   Univers univers;
	 
   PickRay pickRay = new PickRay();
   SceneGraphPath sceneGraphPath[];
   Appearance highlight;
   Model model;
   boolean editAtoms, editLinks;
   Atom selected;
   boolean diabledrag;
   boolean buttonIsRight, buttonIsMiddle;
   boolean ctrlIsDown, shiftIsDown, altIsDown, metaIsDown;
   
 	Point3d mousePos = new Point3d();
	BranchGroup dragSelection = null;
	Point3d secondPos = new Point3d();
	Transform3D plateTovWorldT3d = new Transform3D();
	Point3d eyePos = new Point3d();
	
	Point3d a, b, c, d, ae, ce;
	Vector4d[] vv4d = new Vector4d[6];
	BoundingPolytope boundingPolytop = new BoundingPolytope();
  PickBounds pickBounds = new PickBounds();

  Appearance app;
	
	Box selBox;
	Transform3D selBoxT3d = new Transform3D();
	TransformGroup selBoxTg = new TransformGroup();
	Matrix4d selBoxM4d = new Matrix4d();
  
	boolean selChanged;
	int k;
	
	 PickDragBehavior(Univers univers, Model model) {
      this.univers = univers;
			this.model = model;
	 		canvas3D = univers.getCanvas();

			editAtoms=false;
			editLinks=false;
			selected=null;
			diabledrag=false;

      //modelTrans = new Transform3D();
      transformX = new Transform3D();
      transformY = new Transform3D();

      Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
      Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
      Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

      highlight = new Appearance();
      highlight.setMaterial(new Material(green, black, green, white, 80.f));
      
      a = new Point3d(); 
      b = new Point3d(); 
      c = new Point3d(); 
      d = new Point3d(); 
      ae = new Point3d(); 
      ce = new Point3d(); 
      vv4d[0] = new Vector4d();
      vv4d[1] = new Vector4d();
      vv4d[2] = new Vector4d();
      vv4d[3] = new Vector4d();
      vv4d[4] = new Vector4d(0, 0, -1,  -15);
      vv4d[5] = new Vector4d(0, 0,  1, -1);

  		selBoxM4d.setIdentity();

  		app = new Appearance();
  		app.setMaterial(new Material(ColorConstants.blue, ColorConstants.black, ColorConstants.white, ColorConstants.white, 120.0f));
  		TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
  		app.setTransparencyAttributes(transp);
  		
  		selBox = new Box(1f, 1f, 0.01f, app);
  		dragSelection = new BranchGroup();
  		dragSelection.setCapability(BranchGroup.ALLOW_DETACH);
  		selBoxTg.addChild(selBox);
  		dragSelection.addChild(selBoxTg);
	 }

   public void initialize() {
      x = 0;
      y = 0;
      x_last = 0;
      y_last = 0;
      x_angle = 0;
      y_angle = 0;
      x_factor = .02;
      y_factor = .02;

      mouseEvents = new WakeupCriterion[4];
      mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
      mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
			mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
			mouseEvents[3] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
			//mouseEvents[] = new WakeupOnAWTEvent(MouseEvent.MOUSE_WHEEL);
      mouseCriterion = new WakeupOr(mouseEvents);
      wakeupOn (mouseCriterion);
   }

 	public void reset(Cell cell) {
		Transform3D modelTrans = univers.getGlobalTransform();
    double initScale = 4.0/((cell.a+cell.b+cell.c)/3.0);

    if (univers.isParallel) initScale*=.2; 
    
    
    modelTrans.set(new Vector3d(0, 0, 0), initScale);
    univers.setGlobalTransform(modelTrans);
	}

 	
 	
 	
   public void addTransform(Transform3D t) {
		Transform3D modelTrans = univers.getGlobalTransform();
    modelTrans.mul(t, modelTrans);
    univers.setGlobalTransform(modelTrans);
   }
   
   public void addRotations(double rx, double ry, double rz) {
		Transform3D t = new Transform3D();
		t.rotX(rx);
    addTransform(t);
		t = new Transform3D();
		t.rotY(ry);
    addTransform(t);
		t = new Transform3D();
		t.rotZ(rz);
    addTransform(t);
   }
   
   
   public void processStimulus (Enumeration criteria) {
      WakeupCriterion wakeup;
      AWTEvent[] event;
      int id;
      int dx, dy;

      while (criteria.hasMoreElements()) {
         wakeup = (WakeupCriterion) criteria.nextElement();
         if (wakeup instanceof WakeupOnAWTEvent) {
            event = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
            for (int i=0; i<event.length; i++) { 
               id = event[i].getID();

               if (id == KeyEvent.KEY_PRESSED) {
               	int c = ((KeyEvent)event[i]).getKeyCode();
               	double scale;
               	switch (c) {
               		case KeyEvent.VK_UP: {
                  	scale = 1.0-1/50d;
               			transformY.set(scale);
               			Transform3D modelTrans = univers.getGlobalTransform();
               			modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
               			break;
               		}
               		case KeyEvent.VK_DOWN: {
                  	scale = 1.0+1/50d;
               			transformY.set(scale);
               			Transform3D modelTrans = univers.getGlobalTransform();
                    modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
               			break;
               		}
               		case KeyEvent.VK_LEFT:
               			break;
               		case KeyEvent.VK_RIGHT:
               			break;
               	}
               }
               
/*               
               else if (id == MouseEvent.MOUSE_WHEEL) {
                i = ((MouseWheelEvent)event[i]).getWheelRotation();
               	System.out.println("wheel "+i);
              	double scale = 1.0+((double)i)/100d;
              	transformY.set(scale);
                modelTrans.mul(transformY, modelTrans);
                univers.setGlobalTransform(modelTrans);
               }
*/               
               if (id == MouseEvent.MOUSE_DRAGGED && !diabledrag) {

                  x = ((MouseEvent)event[i]).getX();
                  y = ((MouseEvent)event[i]).getY();

                  dx = x - x_last;
                  dy = y - y_last;

                  x_angle = dy * y_factor;
                  y_angle = dx * x_factor;

                  if (ctrlIsDown || metaIsDown || ((MouseEvent)event[i]).isControlDown() || ((MouseEvent)event[i]).isMetaDown()) {
                  	//System.out.println("drag "+mousePos+" "+secondPos);

                  	if (dragSelection!=null) univers.getRoot().removeChild(dragSelection);

										model.clearAtomSelection();
                  	
                		canvas3D.getPixelLocationInImagePlate(x, y, secondPos);
                		canvas3D.getImagePlateToVworld(plateTovWorldT3d);
                		canvas3D.getCenterEyeInImagePlate(eyePos);
                		plateTovWorldT3d.transform(secondPos);
                		plateTovWorldT3d.transform(eyePos);
                		
                		selBoxM4d.m00 = (secondPos.x-mousePos.x)/2;
                		selBoxM4d.m11 = (secondPos.y-mousePos.y)/2;
                		selBoxM4d.m03 = (mousePos.x+secondPos.x)/2;
                  	selBoxM4d.m13 = (mousePos.y+secondPos.y)/2;
                		
                  	selBoxT3d.set(selBoxM4d);
                  	selBoxTg.setTransform(selBoxT3d);

                  	univers.getRoot().addChild(dragSelection);
                		
                		a.set(mousePos.x, mousePos.y, 0);
                		b.set(mousePos.x, secondPos.y, 0);
                		c.set(secondPos.x, secondPos.y, 0);
                		d.set(secondPos.x, mousePos.y, 0);

                		if (univers.isParallel) {
                  		ae.set(mousePos.x, mousePos.y, 50);
                  		ce.set(secondPos.x, secondPos.y, 50);
                  		calcPlan(vv4d[0], a, ae, b, d);
                  		calcPlan(vv4d[1], c, b, ce, d);
                  		calcPlan(vv4d[2], c, ce, d, b);
                  		calcPlan(vv4d[3], a, d, ae, b);
                		} 
                		else {
                  		calcPlan(vv4d[0], a, eyePos, b, d);
                  		calcPlan(vv4d[1], c, b, eyePos, d);
                  		calcPlan(vv4d[2], d, c, eyePos, a);
                  		calcPlan(vv4d[3], d, eyePos, a, c);
                		}
                		boundingPolytop.setPlanes(vv4d);
                		pickBounds.set(boundingPolytop);
                		
                		sceneGraphPath = model.root.pickAll(pickBounds);//TODO optimize
                		k = 0;
            				if (sceneGraphPath != null) {
            				   for (int j=0; j<sceneGraphPath.length; j++) {
            					  if (sceneGraphPath[j] != null) {
            						 Node node = sceneGraphPath[j].getObject();
            						 if (node instanceof Shape3D) {
            						 	 try {
            						 	 	//double dist[] = {0.0}; 
            								//boolean isRealHit = (( Shape3D) node).intersect(sceneGraphPath[j], pickRay, dist); 
            						 	 	//if (isRealHit) {
            										Object userData = node.getUserData();
            										if (userData != null) {
            											if (userData instanceof Atom) {
            												//((Atom)userData).select();
            				               	selChanged = true;
            												model.selectMultipleAtoms((Atom)userData);
            												k++;
            											}
            										}
            								//}
            								}
            								catch (CapabilityNotSetException ex) {}
            						 }
            					  }
            				   }
            				}
            				model.watcher.setNbAtomSelected(k);
                		
                  }
                  else if (((MouseEvent)event[i]).isAltDown() || buttonIsMiddle || altIsDown) {
                  	double scale = 1.0+(y-y_last)/100d;
                  	transformY.set(scale);
               			Transform3D modelTrans = univers.getGlobalTransform();
                    modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
                  }
/*
                  else if (((MouseEvent)event[i]).isAltDown() || ((MouseEvent)event[i]).isAltGraphDown()) {
                  	Vector3d v = new Vector3d((x-x_last)*0.02, -(y-y_last)*0.02, 0);
                  	transformY.set(v);
                    modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
                  }
*/                  
                  else if (((MouseEvent)event[i]).isShiftDown() || buttonIsRight || shiftIsDown) {
                  	int w = canvas3D.getWidth();
                  	int h = canvas3D.getHeight();
                  	Vector2d p1 = new Vector2d(x_last-w/2, y_last-h/2);
                  	Vector2d p2 = new Vector2d(x-w/2, y-h/2);
                  	Vector2d p3 = new Vector2d(1, 0);
                  	
                  	boolean neg = p1.angle(p3)<p2.angle(p3);
                  	neg = y-h/2>0?!neg:neg;
                  	double alpha = p1.angle(p2);
                  	
                  	transformX.rotZ(0);
                    transformY.rotZ(neg?alpha:-alpha);
               			Transform3D modelTrans = univers.getGlobalTransform();
                    modelTrans.mul(transformX, modelTrans);
                    modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
                  }
                  else {
                    transformX.rotX(x_angle);
                    transformY.rotY(y_angle);
               			Transform3D modelTrans = univers.getGlobalTransform();
                    modelTrans.mul(transformX, modelTrans);
                    modelTrans.mul(transformY, modelTrans);
                    univers.setGlobalTransform(modelTrans);
                  }

                  //modelTrans.mul(transformX, modelTrans);
                  //modelTrans.mul(transformY, modelTrans);
                  //transformGroup.setTransform(modelTrans);

                  //System.out.println(modelTrans);
                  
                  
                  x_last = x;
                  y_last = y;
               }
						   else if (id == MouseEvent.MOUSE_RELEASED) {
								//System.out.println("released");
						   	diabledrag=false;
								if (selChanged) model.jobsAfterSelAtom();
              	if (dragSelection!=null) univers.getRoot().removeChild(dragSelection);
						   }
               else if (id == MouseEvent.MOUSE_PRESSED) {
               	selChanged = false;

               	buttonIsRight = ((MouseEvent)event[i]).getButton()==MouseEvent.BUTTON3;
               	buttonIsMiddle = ((MouseEvent)event[i]).getButton()==MouseEvent.BUTTON2;
               	ctrlIsDown = ((MouseEvent)event[i]).isControlDown();
               	shiftIsDown = ((MouseEvent)event[i]).isShiftDown();
               	altIsDown = ((MouseEvent)event[i]).isAltDown();
								metaIsDown = ((MouseEvent)event[i]).isMetaDown();
               	
						   	diabledrag=false;
						   	
                  x = x_last = ((MouseEvent)event[i]).getX();
                  y = y_last = ((MouseEvent)event[i]).getY();

									Shape3D obj = getPointedObject(true, true, false);
									if (obj!=null) {
										Object data = obj.getUserData();
										if (data instanceof AtomLink) {
											diabledrag=true;
											model.selectLink((AtomLink)data);
										}
										if (data instanceof Atom) {
											diabledrag=true;
			               	selChanged = true;
											model.selUnselAtom((Atom)data, !(ctrlIsDown||metaIsDown));
										}
									}
               }
            } 
         }
      }
      wakeupOn (mouseCriterion);
   }


  
  private Vector3d oa=new Vector3d(), ob=new Vector3d();
  private Vector3d planeN = new Vector3d();
  private double planeD;
  private Vector3d ptov = new Vector3d();
  
  public void calcPlan(Vector4d plan4d, Point3d o, Point3d a, Point3d b, Point3d q) {
  	oa.sub(a, o);
  	ob.sub(b, o);
  	planeN.cross(oa, ob);
  	planeN.normalize();
  	if ((q.x-o.x)/planeN.x>0 || (q.y-o.y)/planeN.y>0 || (q.z-o.z)/planeN.z>0)
  		planeN.negate();

  	ptov.set(o);
  	planeD = -planeN.dot(ptov);

  	plan4d.set(planeN.x, planeN.y, planeN.z, planeD);
  }
   
 	public void mouseWheelMoved(MouseWheelEvent e) {
    //int i = e.getWheelRotation();
    int i;
    if (e.getWheelRotation()==0) i = 0;
    else i = e.getWheelRotation()/Math.abs(e.getWheelRotation());
  	double scale = 1.0+((double)i)/10d;
  	transformY.set(scale);
		Transform3D modelTrans = univers.getGlobalTransform();
    modelTrans.mul(transformY, modelTrans);
    univers.setGlobalTransform(modelTrans);
	}

 	
	public Shape3D getPointedObject(boolean catchAtoms, boolean catchLinks, boolean catchNodes) {
		canvas3D.getCenterEyeInImagePlate(eyePos);
		canvas3D.getPixelLocationInImagePlate(x, y, mousePos);
		canvas3D.getImagePlateToVworld(plateTovWorldT3d);

		plateTovWorldT3d.transform(eyePos);
		plateTovWorldT3d.transform(mousePos);
		
		Vector3d mouseVec;
		if (univers.isParallel) {
		   mouseVec = new Vector3d(0.f, 0.f, -1.f);
		}
		else {
		   mouseVec = new Vector3d();
		   mouseVec.sub(mousePos, eyePos);
		   mouseVec.normalize();
		}

		pickRay.set(mousePos, mouseVec);
		sceneGraphPath = univers.getRoot().pickAllSorted(pickRay);
 
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
								if (userData != null) {
									//System.out.println(posID.getCoord());
									if (((userData instanceof Atom)&&catchAtoms) || ((userData instanceof AtomLink)&&catchLinks) || ((userData instanceof GridNode)&&catchNodes))
										return (Shape3D) node;
								}
						 	}
						}
						catch (CapabilityNotSetException e) {
						   // Catch all CapabilityNotSet exceptions and
						   // throw them away, prevents renderer from
						   // locking up when encountering "non-selectable"
						   // objects.
						}
				 }
			  }
		   }
		}
		return null;
	}


}


