
/*
 * @(#)PickDragBehavior.java 1.10 02/04/01 15:03:32
 * Copyright (c) 1996-2002 Sun Microsystems, Inc. All Rights Reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * - Redistribution in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
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
 * You acknowledge that Software is not designed,licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Enumeration;

import javax.media.j3d.*;
import javax.media.nativewindow.util.Point;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.org.apache.xalan.internal.xsltc.cmdline.Transform;

/**
 * Class: PickDragBehavior
 * <p>
 * Description: Used to respond to mouse pick and drag events
 * in the 3D window.
 * <p>
 * Version: 1.0
 */
public class PickDragBehavior extends Behavior implements MouseWheelListener {
    WakeupCriterion[] mouseEvents;
    WakeupOr mouseCriterion;
    int x, y;
    int x_last, y_last;
    double x_angle, y_angle, z_angle;
    double x_factor, y_factor;
    // Transform3D modelTrans;
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
        //		this.model.compile();
        //		this.model.root.compile();
        this.canvas3D = univers.getCanvas();

        this.editAtoms = false;
        this.editLinks = false;
        this.selected = null;
        this.diabledrag = false;

        // modelTrans = new Transform3D();
        this.transformX = new Transform3D();
        this.transformY = new Transform3D();

        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f green = new Color3f(0.0f, 1.0f, 0.0f);

        this.highlight = new Appearance();
        this.highlight.setMaterial(new Material(green, black, green, white, 80.f));

        this.a = new Point3d();
        this.b = new Point3d();
        this.c = new Point3d();
        this.d = new Point3d();
        this.ae = new Point3d();
        this.ce = new Point3d();
        this.vv4d[0] = new Vector4d();
        this.vv4d[1] = new Vector4d();
        this.vv4d[2] = new Vector4d();
        this.vv4d[3] = new Vector4d();
        this.vv4d[4] = new Vector4d(0, 0, -1, -15);
        this.vv4d[5] = new Vector4d(0, 0, 1, -1);

        this.selBoxM4d.setIdentity();

        this.app = new Appearance();
        this.app.setMaterial(new Material(ColorConstants.blue, ColorConstants.black, ColorConstants.white,
                ColorConstants.white, 120.0f));
        TransparencyAttributes transp = new TransparencyAttributes(TransparencyAttributes.NICEST, .6f);
        this.app.setTransparencyAttributes(transp);

        this.selBox = new Box(1f, 1f, 0.01f, this.app);
        this.dragSelection = new BranchGroup();
        this.dragSelection.setCapability(BranchGroup.ALLOW_DETACH);
        this.selBoxTg.addChild(this.selBox);
        this.dragSelection.addChild(this.selBoxTg);
    }

    @Override
    public void initialize() {
        this.x = 0;
        this.y = 0;
        this.x_last = 0;
        this.y_last = 0;
        this.x_angle = 0;
        this.y_angle = 0;
        this.x_factor = .02;
        this.y_factor = .02;

        this.mouseEvents = new WakeupCriterion[4];
        this.mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        this.mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        this.mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
        this.mouseEvents[3] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        // mouseEvents[] = new WakeupOnAWTEvent(MouseEvent.MOUSE_WHEEL);
        this.mouseCriterion = new WakeupOr(this.mouseEvents);
        this.wakeupOn(this.mouseCriterion);
    }

    public void reset(Cell cell) {
        Transform3D modelTrans = this.univers.getGlobalTransform();
        double initScale = 4.0 / ((cell.a + cell.b + cell.c) / 3.0);

        if (this.univers.isParallel)
            initScale *= .2;

        modelTrans.set(new Vector3d(0, 0, 0), initScale);
        this.univers.setGlobalTransform(modelTrans);
    }

    public void addTransform(Transform3D t) {
        Transform3D modelTrans = this.univers.getGlobalTransform();
        modelTrans.mul(t, modelTrans);
        this.univers.setGlobalTransform(modelTrans);
    }

    public void addRotations(double rx, double ry, double rz) {
        Transform3D t = new Transform3D();
        t.rotX(rx);
        this.addTransform(t);
        t = new Transform3D();
        t.rotY(ry);
        this.addTransform(t);
        t = new Transform3D();
        t.rotZ(rz);
        this.addTransform(t);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] event;
        int id;
        int dx, dy;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                for (int i = 0; i < event.length; i++) {
                    id = event[i].getID();

                    if (id == KeyEvent.KEY_PRESSED) {
                        int c = ((KeyEvent) event[i]).getKeyCode();
                        double scale;
                        switch (c) {
                            case KeyEvent.VK_UP: {
                                scale = 1.0 - 1 / 50d;
                                this.transformY.set(scale);
                                Transform3D modelTrans = this.univers.getGlobalTransform();
                                modelTrans.mul(this.transformY, modelTrans);
                                this.univers.setGlobalTransform(modelTrans);
                                break;
                            }
                            case KeyEvent.VK_DOWN: {
                                scale = 1.0 + 1 / 50d;
                                this.transformY.set(scale);
                                Transform3D modelTrans = this.univers.getGlobalTransform();
                                modelTrans.mul(this.transformY, modelTrans);
                                this.univers.setGlobalTransform(modelTrans);
                                break;
                            }
                            case KeyEvent.VK_LEFT:
                                break;
                            case KeyEvent.VK_RIGHT:
                                break;
                        }
                    }

					/*
                     * else if (id == MouseEvent.MOUSE_WHEEL) {
					 * i = ((MouseWheelEvent)event[i]).getWheelRotation();
					 * System.out.println("wheel "+i);
					 * double scale = 1.0+((double)i)/100d;
					 * transformY.set(scale);
					 * modelTrans.mul(transformY, modelTrans);
					 * univers.setGlobalTransform(modelTrans);
					 * }
					 */
                    // TODO : FIX THIS !!!
                    if (false && id == MouseEvent.MOUSE_DRAGGED && !this.diabledrag) {

                        this.x = ((MouseEvent) event[i]).getX();
                        this.y = ((MouseEvent) event[i]).getY();

                        dx = this.x - this.x_last;
                        dy = this.y - this.y_last;

                        this.x_angle = dy * this.y_factor;
                        this.y_angle = dx * this.x_factor;

                        if (this.ctrlIsDown || this.metaIsDown || ((MouseEvent) event[i]).isControlDown()
                                || ((MouseEvent) event[i]).isMetaDown()) {
                            // System.out.println("drag "+mousePos+"
                            // "+secondPos);

                            if (this.dragSelection != null)
                                this.univers.getRoot().removeChild(this.dragSelection);

                            this.model.clearAtomSelection();

                            this.canvas3D.getPixelLocationInImagePlate(this.x, this.y, this.secondPos);
                            this.canvas3D.getImagePlateToVworld(this.plateTovWorldT3d);
                            this.canvas3D.getCenterEyeInImagePlate(this.eyePos);
                            this.plateTovWorldT3d.transform(this.secondPos);
                            this.plateTovWorldT3d.transform(this.eyePos);

                            this.selBoxM4d.m00 = (this.secondPos.x - this.mousePos.x) / 2;
                            this.selBoxM4d.m11 = (this.secondPos.y - this.mousePos.y) / 2;
                            this.selBoxM4d.m03 = (this.mousePos.x + this.secondPos.x) / 2;
                            this.selBoxM4d.m13 = (this.mousePos.y + this.secondPos.y) / 2;

                            this.selBoxT3d.set(this.selBoxM4d);
                            this.selBoxTg.setTransform(this.selBoxT3d);

                            this.univers.getRoot().addChild(this.dragSelection);

                            this.a.set(this.mousePos.x, this.mousePos.y, 0);
                            this.b.set(this.mousePos.x, this.secondPos.y, 0);
                            this.c.set(this.secondPos.x, this.secondPos.y, 0);
                            this.d.set(this.secondPos.x, this.mousePos.y, 0);

                            if (this.univers.isParallel) {
                                this.ae.set(this.mousePos.x, this.mousePos.y, 50);
                                this.ce.set(this.secondPos.x, this.secondPos.y, 50);
                                this.calcPlan(this.vv4d[0], this.a, this.ae, this.b, this.d);
                                this.calcPlan(this.vv4d[1], this.c, this.b, this.ce, this.d);
                                this.calcPlan(this.vv4d[2], this.c, this.ce, this.d, this.b);
                                this.calcPlan(this.vv4d[3], this.a, this.d, this.ae, this.b);
                            } else {
                                this.calcPlan(this.vv4d[0], this.a, this.eyePos, this.b, this.d);
                                this.calcPlan(this.vv4d[1], this.c, this.b, this.eyePos, this.d);
                                this.calcPlan(this.vv4d[2], this.d, this.c, this.eyePos, this.a);
                                this.calcPlan(this.vv4d[3], this.d, this.eyePos, this.a, this.c);
                            }
                            this.boundingPolytop.setPlanes(this.vv4d);
                            this.pickBounds.set(this.boundingPolytop);

                            this.model.clearAtomSelection();
                            this.model.watcher.setNbAtomSelected(0);
                            this.model.watcher.removeAll();
                            this.model.selectedAtoms.removeAllElements();

                            boolean found = false;
                            int count = 0;
                            do {
                                try {
                                    this.sceneGraphPath = this.model.root.pickAll(this.pickBounds);
                                    found = true;
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    System.out.println("pickAll has thrown a nullPointer " + ++count + " times let's try again");
                                }
                            } while (!found && count < 10);

                            if (count == 0) {
                                System.out.println("did it in one time");
                            }

                            this.sceneGraphPath = null;

                            // FIXME: pickAll fait un nullPointerExeption parfois
                            // temporary workaroud: on catch le nullPointerException
                            //this.sceneGraphPath = this.model.root.pickAll(this.pickBounds);
                            // optimize
                            this.k = 0;
                            if (this.sceneGraphPath != null) {
                                for (int j = 0; j < this.sceneGraphPath.length; j++)
                                    if (this.sceneGraphPath[j] != null) {
                                        Node node = this.sceneGraphPath[j].getObject();
                                        if (node instanceof Shape3D)
                                            try {
                                                // double dist[] = {0.0};
                                                // boolean isRealHit = ((
                                                // Shape3D)
                                                // node).intersect(sceneGraphPath[j],
                                                // pickRay, dist);
                                                // if (isRealHit) {
                                                Object userData = node.getUserData();
                                                if (userData != null)
                                                    if (userData instanceof Atom) {
                                                        // ((Atom)userData).select();
                                                        this.selChanged = true;
                                                        this.model.selectMultipleAtoms((Atom) userData);
                                                        this.k++;
                                                    }
                                            } catch (CapabilityNotSetException ex) {
                                            }
                                    }
                            }

                            this.model.watcher.setNbAtomSelected(this.k);

                        } else if (((MouseEvent) event[i]).isAltDown() || this.buttonIsMiddle || this.altIsDown) {
                            double scale = 1.0 + (this.y - this.y_last) / 100d;
                            this.transformY.set(scale);
                            Transform3D modelTrans = this.univers.getGlobalTransform();
                            modelTrans.mul(this.transformY, modelTrans);
                            this.univers.setGlobalTransform(modelTrans);
                        }
						/*
						 * else if (((MouseEvent)event[i]).isAltDown() ||
						 * ((MouseEvent)event[i]).isAltGraphDown()) {
						 * Vector3d v = new Vector3d((x-x_last)*0.02,
						 * -(y-y_last)*0.02, 0);
						 * transformY.set(v);
						 * modelTrans.mul(transformY, modelTrans);
						 * univers.setGlobalTransform(modelTrans);
						 * }
						 */
                        else if (((MouseEvent) event[i]).isShiftDown() || this.buttonIsRight || this.shiftIsDown) {
                            int w = this.canvas3D.getWidth();
                            int h = this.canvas3D.getHeight();
                            Vector2d p1 = new Vector2d(this.x_last - w / 2, this.y_last - h / 2);
                            Vector2d p2 = new Vector2d(this.x - w / 2, this.y - h / 2);
                            Vector2d p3 = new Vector2d(1, 0);

                            boolean neg = p1.angle(p3) < p2.angle(p3);
                            neg = this.y - h / 2 > 0 ? !neg : neg;
                            double alpha = p1.angle(p2);

                            this.transformX.rotZ(0);
                            this.transformY.rotZ(neg ? alpha : -alpha);
                            Transform3D modelTrans = this.univers.getGlobalTransform();
                            modelTrans.mul(this.transformX, modelTrans);
                            modelTrans.mul(this.transformY, modelTrans);
                            this.univers.setGlobalTransform(modelTrans);
                        } else {
                            this.transformX.rotX(this.x_angle);
                            this.transformY.rotY(this.y_angle);
                            Transform3D modelTrans = this.univers.getGlobalTransform();
                            modelTrans.mul(this.transformX, modelTrans);
                            modelTrans.mul(this.transformY, modelTrans);
                            this.univers.setGlobalTransform(modelTrans);
                        }

                        // modelTrans.mul(transformX, modelTrans);
                        // modelTrans.mul(transformY, modelTrans);
                        // transformGroup.setTransform(modelTrans);

                        // System.out.println(modelTrans);

                        this.x_last = this.x;
                        this.y_last = this.y;
                    } else if (id == MouseEvent.MOUSE_RELEASED) {
                        // System.out.println("released");
                        this.diabledrag = false;
                        if (this.selChanged)
                            this.model.jobsAfterSelAtom();
                        if (this.dragSelection != null)
                            this.univers.getRoot().removeChild(this.dragSelection);
                    } else if (id == MouseEvent.MOUSE_PRESSED) {
                        this.selChanged = false;

                        this.buttonIsRight = ((MouseEvent) event[i]).getButton() == MouseEvent.BUTTON3;
                        this.buttonIsMiddle = ((MouseEvent) event[i]).getButton() == MouseEvent.BUTTON2;
                        this.ctrlIsDown = ((MouseEvent) event[i]).isControlDown();
                        this.shiftIsDown = ((MouseEvent) event[i]).isShiftDown();
                        this.altIsDown = ((MouseEvent) event[i]).isAltDown();
                        this.metaIsDown = ((MouseEvent) event[i]).isMetaDown();

                        this.diabledrag = false;

                        this.x = this.x_last = ((MouseEvent) event[i]).getX();
                        this.y = this.y_last = ((MouseEvent) event[i]).getY();

                        Shape3D obj = this.getPointedObject(true, true, false);
                        if (obj != null) {
                            Object data = obj.getUserData();
                            if (data instanceof AtomLink) {
                                this.diabledrag = true;
                                this.model.selectLink((AtomLink) data);
                            }
                            if (data instanceof Atom) {
                                this.diabledrag = true;
                                this.selChanged = true;
                                this.model.selUnselAtom((Atom) data, !(this.ctrlIsDown || this.metaIsDown));
                            }
                        }
                    }
                }
            }
        }
        this.wakeupOn(this.mouseCriterion);
    }

    private Vector3d oa = new Vector3d(), ob = new Vector3d();
    private Vector3d planeN = new Vector3d();
    private double planeD;
    private Vector3d ptov = new Vector3d();

    public void calcPlan(Vector4d plan4d, Point3d o, Point3d a, Point3d b, Point3d q) {
        this.oa.sub(a, o);
        this.ob.sub(b, o);
        this.planeN.cross(this.oa, this.ob);
        this.planeN.normalize();
        if ((q.x - o.x) / this.planeN.x > 0 || (q.y - o.y) / this.planeN.y > 0 || (q.z - o.z) / this.planeN.z > 0)
            this.planeN.negate();

        this.ptov.set(o);
        this.planeD = -this.planeN.dot(this.ptov);

        plan4d.set(this.planeN.x, this.planeN.y, this.planeN.z, this.planeD);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // int i = e.getWheelRotation();
        int i;
        if (e.getWheelRotation() == 0)
            i = 0;
        else
            i = e.getWheelRotation() / Math.abs(e.getWheelRotation());
        double scale = 1.0 + (i) / 10d;
        this.transformY.set(scale);
        Transform3D modelTrans = this.univers.getGlobalTransform();
        modelTrans.mul(this.transformY, modelTrans);
        this.univers.setGlobalTransform(modelTrans);
    }

    public Shape3D getPointedObject(boolean catchAtoms, boolean catchLinks, boolean catchNodes) {
        this.canvas3D.getCenterEyeInImagePlate(this.eyePos);
        this.canvas3D.getPixelLocationInImagePlate(this.x, this.y, this.mousePos);
        this.canvas3D.getImagePlateToVworld(this.plateTovWorldT3d);

        this.plateTovWorldT3d.transform(this.eyePos);
        this.plateTovWorldT3d.transform(this.mousePos);

        Vector3d mouseVec;
        if (this.univers.isParallel)
            mouseVec = new Vector3d(0.f, 0.f, -1.f);
        else {
            mouseVec = new Vector3d();
            mouseVec.sub(this.mousePos, this.eyePos);
            mouseVec.normalize();
        }

        this.pickRay.set(this.mousePos, mouseVec);
        this.sceneGraphPath = this.univers.getRoot().pickAllSorted(this.pickRay);

        if (this.sceneGraphPath != null)
            for (int j = 0; j < this.sceneGraphPath.length; j++)
                if (this.sceneGraphPath[j] != null) {
                    Node node = this.sceneGraphPath[j].getObject();
                    if (node instanceof Shape3D)
                        try {
                            double dist[] = {0.0};
                            boolean isRealHit = ((Shape3D) node).intersect(this.sceneGraphPath[j], this.pickRay, dist);
                            if (isRealHit) {
                                Object userData = node.getUserData();
                                if (userData != null)
                                    // System.out.println(posID.getCoord());
                                    if (((userData instanceof Atom) && catchAtoms)
                                            || ((userData instanceof AtomLink) && catchLinks)
                                            || ((userData instanceof GridNode) && catchNodes))
                                        return (Shape3D) node;
                            }
                        } catch (CapabilityNotSetException e) {
                            // Catch all CapabilityNotSet exceptions and
                            // throw them away, prevents renderer from
                            // locking up when encountering "non-selectable"
                            // objects.
                        }
                }
        return null;
    }

}
