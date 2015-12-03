package objects;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import main.MainPane;

import transformations.*;
import u3d.TranspObject;
import utils.ColorConstants;
import utils.HVPanel;

public abstract class Polyedre {
	public TranspObject ref, clone;
	protected BranchGroup root;
	public Group parent;
	private boolean fusioned;
	private HVPanel parentPanel;
	private Vector paneList;
	private Vector transformList;
	public TransformGroup tgRot;
	public TransformGroup tgInv;
	public Color3f colorMatch = new Color3f(.2f, .2f, 1f);
	public Color3f colorNoMatch = new Color3f(1f, .2f, .2f);
	public Color3f colorRef = new Color3f(1f, 1f, 1f);
	
	public Polyedre(Point3d[] points, int[][] faces) {
		this(points, faces, null, true, true);
	}
	public Polyedre(Point3d[] points, int[][] faces, int[][] edges, boolean showFaces, boolean showFaceEdges) {
		root = new BranchGroup();
		root.setCapability(BranchGroup.ALLOW_DETACH);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		tgRot = new TransformGroup();
		tgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		root.addChild(tgRot);

		tgInv = new TransformGroup();
		tgInv.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgInv.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgRot.addChild(tgInv);
		
		ref = new TranspObject(points, faces, edges, showFaces, showFaceEdges);
		clone = new TranspObject(points, faces, edges, showFaces, showFaceEdges);
		ref.setColor(colorRef);
		clone.setColor(colorMatch);
		paneList = new Vector(5, 5);
		transformList = new Vector(5, 5);
		fusioned = true;
		createSubPanes();
		clone.attach(tgInv);
	}
	
	public void addTransform(Transformation transform) {
		transformList.add(transform);
	}
	
	public HVPanel addPanel(HVPanel panel) {
		paneList.add(panel);
		panel.center();
		return panel;
	}

	protected void createSubPanes() {
		createRotPane();
		createInvResetPane();
	}
	
	protected abstract void createRotPane();
	
	protected void createInvResetPane() {
		HVPanel invPanel = addPanel(new HVPanel.v());
		((HVPanel.v)invPanel).putExtraSpace();
		((HVPanel.v)invPanel).bottom();
		invPanel.expand(false);
		addTransform(new Inversion(invPanel, this));
		invPanel.addButton(new JButton("Reset"));
	}
	
	public void attach(Group parent, HVPanel parentPanel) {
		this.parent = parent;
		this.parentPanel = parentPanel;
		parent.addChild(root);
		for (int i=0; i<paneList.size(); i++) parentPanel.addSubPane((HVPanel)paneList.get(i));
	}
	public void detach() {
		if (parent==null) return;
		parent.removeChild(root);
		parent = null;
		for (int i=0; i<paneList.size(); i++) parentPanel.jPanel.remove(((HVPanel)paneList.get(i)).jPanel);
	}
	
	public void onTransformChanged() {
		Transform3D t3dRot = new Transform3D();
		tgRot.getTransform(t3dRot);
		Transform3D t3dInv = new Transform3D();
		tgInv.getTransform(t3dInv);
		t3dRot.mul(t3dInv);
		boolean identic = clone.isTransformIdentity(t3dRot);
		if (identic && !fusioned) {
			ref.detach();
			clone.setColor(colorMatch);
			fusioned = true;
		}
		else if (!identic && fusioned) {
			ref.attach(root);
			clone.setColor(colorNoMatch);
			fusioned = false;
		}
	}
	
	public void reset() {
		tgInv.setTransform(new Transform3D());
		tgRot.setTransform(new Transform3D());
		for (int i=0; i<transformList.size(); i++)
			((Transformation)transformList.get(i)).reset();
	}
}
