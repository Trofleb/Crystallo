package panes;

import intensity.Intensity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

import utils.ColorConstants;
import utils.HVPanel;
import utils.HVPanel.SliderAndValue;
import utils.HVPanel.h;
import utils.HVPanel.v;




import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;

import engine3D.Univers;

public class Single3DPane extends HVPanel.v {
	public Univers univers;
	
	public Single3DPane() {
		univers = new Univers();
		
		JPanel panel3d = new JPanel();
		panel3d.setLayout(new BorderLayout());
		panel3d.add(univers.getCanvas());
		HVPanel.h p = new HVPanel.h();
		p.expand(true);
		p.addComp(panel3d);
		expand(true);
		addSubPane(p);
	}
}
