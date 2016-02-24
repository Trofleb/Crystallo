package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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

import objects.*;

import u3d.TranspObject;
import u3d.Univers;
import utils.ColorConstants;
import utils.HVPanel;
import utils.HVPanel.SliderAndValue;
import utils.HVPanel.h;
import utils.HVPanel.v;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;

public class MainPane extends HVPanel.v {
	Univers univers;
	ButtonPanel buttonPanel;
	BottomPanel bottomPanel;
	Polyedre currentPoly;
	HVPanel fixedPane;
	
	public MainPane() {
		univers = new Univers(ColorConstants.white);
		
		HVPanel.h p = new HVPanel.h();
		p.expand(false);
		
		buttonPanel = new ButtonPanel();
		JScrollPane scrollPane = new JScrollPane(buttonPanel.jPanel);
		scrollPane.setMinimumSize(new Dimension(125, 0));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		p.addComp(scrollPane);
		
		JPanel panel3d = new JPanel();
		panel3d.setLayout(new BorderLayout());
		panel3d.add(univers.getCanvas());
		p.expand(true);
		p.addComp(panel3d);
		
		expand(true);
		addSubPane(p);
		expand(false);
		bottomPanel = new BottomPanel();
		//bottomPanel.jPanel.setMinimumSize(new Dimension(0, 100));
		addSubPane(bottomPanel);
		
		buttonPanel.actionPerformed(new ActionEvent(buttonPanel.buttons[0], 0, null));
	}
	
	private class ButtonPanel extends HVPanel.v {
		JToggleButton[] buttons;
		
		Polyedre[] polyedres = {new OrthoSphenoide(), new TetraSphenoide(), new Sphericon(), new Rhomboedre(), new Bipyramide(), new Tetraedre(), new Cube(), new Octaedre(), new Dodecaedre(), new Icosaedre()};
		String[] names = {"<html><center>Orthorhombic<p>sphenoid", "<html><center>Tetragonal<p>sphenoid", "Sphericon", "Rhombohedron", "<html><center>Trigonal<p>bipyramid", "Tetrahedron", "Cube", "Octahedron", "<html><center>Pentagonal<p>dodecahedron", "Icosahedron"};
		String[] files = {"orthosphenoid.png", "tetrasphenoid.png", "sphericon.png", "rhomboedre.png", "bipyramid.png", "tetraedre.png", "cube.png", "octaedre.png", "dodecaedre.png", "icosaedre.png"};
		
		public ButtonPanel() {
			top();
			expand(false);
			buttons = new JToggleButton[polyedres.length];
			for (int i=0; i<polyedres.length; i++) {
				if (files[i]==null) {
					buttons[i] = new JToggleButton(names[i]);
					buttons[i].setVerticalTextPosition(AbstractButton.CENTER);
					buttons[i].setHorizontalTextPosition(AbstractButton.CENTER);
				}
				else {
					URL u = getClass().getResource("/"+files[i]);
					buttons[i] = new JToggleButton(names[i], new ImageIcon(u));
					Insets insets = buttons[i].getMargin();
					insets.left=0; insets.right=0;
					buttons[i].setMargin(insets);
					buttons[i].setVerticalTextPosition(AbstractButton.BOTTOM);
					buttons[i].setHorizontalTextPosition(AbstractButton.CENTER);
					buttons[i].setHorizontalAlignment(AbstractButton.CENTER);
				}
				addButtonGroupped(buttons[i]);
			}
		}

		public void actionPerformed(ActionEvent e) {
			for (int i=0; i<buttons.length; i++) {
				if (e.getSource()==buttons[i]) {
					if (currentPoly!=null) currentPoly.detach();
					currentPoly = polyedres[i];
					currentPoly.attach(univers.getRoot(), bottomPanel.transformPanel);
					bottomPanel.transformPanel.jPanel.revalidate();
					bottomPanel.jPanel.revalidate();
					bottomPanel.jPanel.repaint();
				}
			}
		}
	}

	private Help help;

	public void destroy() {
		univers.cleanup();
	}
	public void stop() {
		help.show(false);
	}

	private class BottomPanel extends HVPanel.h {
		public HVPanel transformPanel;
		public BottomPanel() {
			help = new Help();
						
			HVPanel.v h1 = new HVPanel.v();
			HVPanel.h h2 = new HVPanel.h();

			h1.bottom();
			h1.fillSpace();
			h1.addSubPane(transformPanel = new HVPanel.h());

			fixedPane = new HVPanel.v();
			fixedPane.fillSpace();
			((HVPanel.v)fixedPane).bottom();
			//fixedPane.addButton(new JButton("Inversion"));
			fixedPane.addButton(new JButton("Help"));
			//fixedPane.addButton(new JButton("Reset"));
			
			h2.fillSpace();
			h2.right();
			h2.addSubPane(fixedPane);
			
			expand(false);
			left();
			addSubPane(h1);
			expand(false);
			right();
			addSubPane(h2);
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Reset")) {
				currentPoly.reset();
			}
			if (e.getActionCommand().equals("Help")) {
				help.show(true);
			}
		}
	}
}
