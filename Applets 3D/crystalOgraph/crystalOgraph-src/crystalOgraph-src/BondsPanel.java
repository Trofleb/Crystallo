import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;

import ColorComboBox.IncompatibleLookAndFeelException;

public class BondsPanel extends JPanel implements ActionListener, ColorConstants {
	private JTextField boundsDist, boundsRadius;
	private JButton boundsMore, boundsLess;
	public JButton boundsAdd, boundsDel;
	private ColorChoice boundsColor;
	private Univers univers;
	private Model model; 

	public BondsPanel(Univers univers, Model model) {
		this.univers = univers;
		this.model = model;

		JLabel l;
		
		setBorder(new TitledBorder("Bonds"));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		JPanel p;
		
		GridBagConstraints cc = new GridBagConstraints();
		cc.fill = GridBagConstraints.HORIZONTAL;

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		p.add(l=new MultiLineToolTip.JLabel("Max dist"), cc);
		boundsDist = new MultiLineToolTip.JTextField();
		boundsDist.setColumns(4);
		boundsDist.addActionListener(this);
		cc.insets = new Insets(0,10,0,0);  
		p.add(boundsDist, cc);
		cc.insets = new Insets(0,0,0,0);  
		p.add(new MultiLineToolTip.JLabel(" Å"), cc);
		add(p, c);

		l.setToolTipText("All atoms which distance is equal or smaller\nthan this value are to be bonded.");
		boundsDist.setToolTipText("All atoms which distance is equal or smaller\nthan this value will be bonded when you press Enter.");
		
		
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		//GridBagConstraints cc = new GridBagConstraints();
		//cc.fill = GridBagConstraints.NONE;
		//cc.gridx = 0;
		boundsMore =new MultiLineToolTip.JButton("More");
		boundsMore.addActionListener(this);
		p.add(boundsMore);
		boundsLess =new MultiLineToolTip.JButton("Less");
		boundsLess.addActionListener(this);
		cc.gridx = 1;
		p.add(boundsLess);
		add(p, c);

		boundsMore.setToolTipText("Increase the bond distance to the next nearest atoms.");
		boundsLess.setToolTipText("Decrease the bond distance.");
		
		
		c.insets = new Insets(5,0,0,0);  
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		cc.gridx = GridBagConstraints.RELATIVE; cc.gridy=0; 
		p.add(l=new MultiLineToolTip.JLabel("Thickness"), cc);
		boundsRadius = new MultiLineToolTip.JTextField();
		boundsRadius.setColumns(3);
		cc.insets = new Insets(0,10,0,0);  
		boundsRadius.addActionListener(this);
		p.add(boundsRadius, cc);
		cc.insets = new Insets(0,0,0,0);  
		p.add(new MultiLineToolTip.JLabel(" Å"), cc);
		add(p, c);

		boundsRadius.setToolTipText("Change the radius of the selected bond.\nNew value for the next bond.");
		l.setToolTipText("Change the radius of the selected bond.\nNew value for the next bond.");

		c.insets = new Insets(0,0,0,0);  
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		cc.gridx = GridBagConstraints.RELATIVE; cc.gridy=0; 
		p.add(l=new MultiLineToolTip.JLabel("Color"), cc);
		try {
			boundsColor = new ColorChoice();
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		boundsColor.setPreferredSize(new Dimension(80, 20));
		cc.insets = new Insets(0,5,0,0);  
		boundsColor.addActionListener(this);
		p.add(boundsColor, cc);
		add(p, c);
		
		//boundsColor.setToolTipText("Change the color of the selected bond.\nNew value for the next bond.");
		l.setToolTipText("Change the color of the selected bond.\nNew value for the next bond.");
		
		c.insets = new Insets(5,0,0,0);  
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		//GridBagConstraints cc = new GridBagConstraints();
		//cc.fill = GridBagConstraints.NONE;
		//cc.gridx = 0;
		boundsAdd =new MultiLineToolTip.JButton("Create");
		boundsAdd.addActionListener(this);
		Insets margins = new Insets(2, 2, 2, 2);
		boundsAdd.setMargin(margins);
		p.add(boundsAdd);
		boundsDel =new MultiLineToolTip.JButton("Delete");
		boundsDel.addActionListener(this);
		boundsDel.setMargin(margins);
		cc.gridx = 1;
		p.add(boundsDel);
		add(p, c);
		
		reset();
	}
	
	public void reset() {
		boundsDist.setText("0.0");
		boundsRadius.setText("0.05");
		setAddEnable(false);
		setDelEnable(false);
		boundsColor.setSelectedColor(Color.white);
		model.currentColorLink = white;
		model.currentSizeLink = 0.05f;
	}
	
	
	public void setRadius(float r) {
		boundsRadius.setText(""+Atom.round(r));
	}
	
	public void setColor(Color c) {
		boundsColor.setSelectedColor(c);
	}
	
	public void removeBonds() {
		boundsDist.setText("0.0");
		model.boundUnder_hard(0.0);
	}
	
	public void rebond() {
		try {
			model.boundUnder_hard(TextFieldCoord.parseFraqValue(boundsDist.getText()));
		}
		catch (BadEntry e) {
			Shaker.shake("Invalide value in bond distance");
		}
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==boundsDist) {
			try {
				model.boundUnder_hard(TextFieldCoord.parseFraqValue(boundsDist.getText()));
			}
			catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		}
		else if (event.getSource()==boundsMore) {
			updateRadius();
			try {
				double d = model.minDistUp(TextFieldCoord.parseFraqValue(boundsDist.getText()));
				boundsDist.setText(""+Math.round(d*1000d)/1000d);
				model.boundUnder_hard(d);
			}
			catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		}
		else if (event.getSource()==boundsLess) {
			try {
				double d = model.minDistDown(TextFieldCoord.parseFraqValue(boundsDist.getText()));
				boundsDist.setText(""+Math.round(d*1000d)/1000d);
				model.boundUnder_hard(d);
			}
			catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		}
		else if (event.getSource()==boundsColor) {
			model.currentColorLink = new Color3f(boundsColor.getSelectedColor());
			if (model.selectedLink!=null)
				model.selectedLink.changeColor(model.currentColorLink);
		}
		else if (event.getSource()==boundsRadius) {
			updateRadius();
			if (model.selectedLink!=null)
				model.selectedLink.changeRadius(model.currentSizeLink);
		}
		else if (event.getSource()==boundsAdd) {
			updateRadius();
			AtomLink l = model.addLink((Atom)model.selectedAtoms.get(0), (Atom)model.selectedAtoms.get(1));
			if (l!=null) l.userMade=true;
			setAddEnable(false);
		}
		else if (event.getSource()==boundsDel) {
			model.removeLink(model.selectedLink);
			setDelEnable(false);
			if (model.selectedAtoms.size()==2 && model.findLink(((Atom)model.selectedAtoms.elementAt(0)).pos, ((Atom)model.selectedAtoms.elementAt(1)).pos)==null) boundsAdd.setEnabled(true);
		}
	}	
	
	
	public void setAddEnable(boolean b) {
		if (boundsAdd==null) return;
		boundsAdd.setEnabled(b);
		if (b) boundsAdd.setToolTipText("Link the two selected atoms.");
		else boundsAdd.setToolTipText("Click on two atoms to link them.");
	}
	
	public void setDelEnable(boolean b) {
		if (boundsDel==null) return;
			boundsDel.setEnabled(b);
		if (b) boundsDel.setToolTipText("Delete the selected bond.");
		else boundsDel.setToolTipText("Click over a bond in order to delete it.");
	}
	
	private void updateRadius() {
		try {
			model.currentSizeLink = TextFieldCoord.parseFraqValue(boundsRadius.getText());
		}
		catch (BadEntry e) {
			Shaker.shake("Invalide value in bond radius");
		}
	}
}