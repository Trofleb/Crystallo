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
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;

import ColorComboBox.IncompatibleLookAndFeelException;

public class BondsPanel extends JPanel implements ActionListener, ColorConstants {
	private JTextField boundsDist, boundsRadius;
	private JButton boundsMore, boundsLess;
	public JButton boundsAdd, boundsDel;
	private ColorChoice boundsColor;
	private Model model;

	public BondsPanel(Univers univers, Model model) {
		this.model = model;

		JLabel l;

		this.setBorder(new TitledBorder("Bonds"));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		JPanel p;

		GridBagConstraints cc = new GridBagConstraints();
		cc.fill = GridBagConstraints.HORIZONTAL;

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		p.add(l = new MultiLineToolTip.JLabel("Max dist"), cc);
		this.boundsDist = new MultiLineToolTip.JTextField();
		this.boundsDist.setColumns(4);
		this.boundsDist.addActionListener(this);
		cc.insets = new Insets(0, 10, 0, 0);
		p.add(this.boundsDist, cc);
		cc.insets = new Insets(0, 0, 0, 0);
		p.add(new MultiLineToolTip.JLabel(" Å"), cc);
		this.add(p, c);

		l.setToolTipText("All atoms which distance is equal or smaller\nthan this value are to be bonded.");
		this.boundsDist.setToolTipText(
				"All atoms which distance is equal or smaller\nthan this value will be bonded when you press Enter.");

		p = new JPanel();
		p.setLayout(new GridBagLayout());
		// GridBagConstraints cc = new GridBagConstraints();
		// cc.fill = GridBagConstraints.NONE;
		// cc.gridx = 0;
		this.boundsMore = new MultiLineToolTip.JButton("More");
		this.boundsMore.addActionListener(this);
		p.add(this.boundsMore);
		this.boundsLess = new MultiLineToolTip.JButton("Less");
		this.boundsLess.addActionListener(this);
		cc.gridx = 1;
		p.add(this.boundsLess);
		this.add(p, c);

		this.boundsMore.setToolTipText("Increase the bond distance to the next nearest atoms.");
		this.boundsLess.setToolTipText("Decrease the bond distance.");

		c.insets = new Insets(5, 0, 0, 0);
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		cc.gridx = GridBagConstraints.RELATIVE;
		cc.gridy = 0;
		p.add(l = new MultiLineToolTip.JLabel("Thickness"), cc);
		this.boundsRadius = new MultiLineToolTip.JTextField();
		this.boundsRadius.setColumns(3);
		cc.insets = new Insets(0, 10, 0, 0);
		this.boundsRadius.addActionListener(this);
		p.add(this.boundsRadius, cc);
		cc.insets = new Insets(0, 0, 0, 0);
		p.add(new MultiLineToolTip.JLabel(" Å"), cc);
		this.add(p, c);

		this.boundsRadius.setToolTipText("Change the radius of the selected bond.\nNew value for the next bond.");
		l.setToolTipText("Change the radius of the selected bond.\nNew value for the next bond.");

		c.insets = new Insets(0, 0, 0, 0);
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		cc.gridx = GridBagConstraints.RELATIVE;
		cc.gridy = 0;
		p.add(l = new MultiLineToolTip.JLabel("Color"), cc);
		try {
			this.boundsColor = new ColorChoice();
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		this.boundsColor.setPreferredSize(new Dimension(80, 20));
		cc.insets = new Insets(0, 5, 0, 0);
		this.boundsColor.addActionListener(this);
		p.add(this.boundsColor, cc);
		this.add(p, c);

		// boundsColor.setToolTipText("Change the color of the selected
		// bond.\nNew value for the next bond.");
		l.setToolTipText("Change the color of the selected bond.\nNew value for the next bond.");

		c.insets = new Insets(5, 0, 0, 0);
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		// GridBagConstraints cc = new GridBagConstraints();
		// cc.fill = GridBagConstraints.NONE;
		// cc.gridx = 0;
		this.boundsAdd = new MultiLineToolTip.JButton("Create");
		this.boundsAdd.addActionListener(this);
		Insets margins = new Insets(2, 2, 2, 2);
		this.boundsAdd.setMargin(margins);
		p.add(this.boundsAdd);
		this.boundsDel = new MultiLineToolTip.JButton("Delete");
		this.boundsDel.addActionListener(this);
		this.boundsDel.setMargin(margins);
		cc.gridx = 1;
		p.add(this.boundsDel);
		this.add(p, c);

		this.reset();
	}

	public void reset() {
		this.boundsDist.setText("0.0");
		this.boundsRadius.setText("0.05");
		this.setAddEnable(false);
		this.setDelEnable(false);
		this.boundsColor.setSelectedColor(Color.white);
		this.model.currentColorLink = white;
		this.model.currentSizeLink = 0.05f;
	}

	public void setRadius(float r) {
		this.boundsRadius.setText("" + Atom.round(r));
	}

	public void setColor(Color c) {
		this.boundsColor.setSelectedColor(c);
	}

	public void removeBonds() {
		this.boundsDist.setText("0.0");
		this.model.boundUnder_hard(0.0);
	}

	public void rebond() {
		try {
			this.model.boundUnder_hard(TextFieldCoord.parseFraqValue(this.boundsDist.getText()));
		} catch (BadEntry e) {
			Shaker.shake("Invalide value in bond distance");
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.boundsDist)
			try {
				this.model.boundUnder_hard(TextFieldCoord.parseFraqValue(this.boundsDist.getText()));
			} catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		else if (event.getSource() == this.boundsMore) {
			this.updateRadius();
			try {
				double d = this.model.minDistUp(TextFieldCoord.parseFraqValue(this.boundsDist.getText()));
				this.boundsDist.setText("" + Math.round(d * 1000d) / 1000d);
				this.model.boundUnder_hard(d);
			} catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		} else if (event.getSource() == this.boundsLess)
			try {
				double d = this.model.minDistDown(TextFieldCoord.parseFraqValue(this.boundsDist.getText()));
				this.boundsDist.setText("" + Math.round(d * 1000d) / 1000d);
				this.model.boundUnder_hard(d);
			} catch (BadEntry e) {
				Shaker.shake("Invalide value in bond distance");
			}
		else if (event.getSource() == this.boundsColor) {
			this.model.currentColorLink = new Color3f(this.boundsColor.getSelectedColor());
			if (this.model.selectedLink != null)
				this.model.selectedLink.changeColor(this.model.currentColorLink);
		} else if (event.getSource() == this.boundsRadius) {
			this.updateRadius();
			if (this.model.selectedLink != null)
				this.model.selectedLink.changeRadius(this.model.currentSizeLink);
		} else if (event.getSource() == this.boundsAdd) {
			this.updateRadius();
			AtomLink l = this.model.addLink((Atom) this.model.selectedAtoms.get(0),
					(Atom) this.model.selectedAtoms.get(1));
			if (l != null)
				l.userMade = true;
			this.setAddEnable(false);
		} else if (event.getSource() == this.boundsDel) {
			this.model.removeLink(this.model.selectedLink);
			this.setDelEnable(false);
			if (this.model.selectedAtoms.size() == 2
					&& this.model.findLink(((Atom) this.model.selectedAtoms.elementAt(0)).pos,
							((Atom) this.model.selectedAtoms.elementAt(1)).pos) == null)
				this.boundsAdd.setEnabled(true);
		}
	}

	public void setAddEnable(boolean b) {
		if (this.boundsAdd == null)
			return;
		this.boundsAdd.setEnabled(b);
		if (b)
			this.boundsAdd.setToolTipText("Link the two selected atoms.");
		else
			this.boundsAdd.setToolTipText("Click on two atoms to link them.");
	}

	public void setDelEnable(boolean b) {
		if (this.boundsDel == null)
			return;
		this.boundsDel.setEnabled(b);
		if (b)
			this.boundsDel.setToolTipText("Delete the selected bond.");
		else
			this.boundsDel.setToolTipText("Click over a bond in order to delete it.");
	}

	private void updateRadius() {
		try {
			this.model.currentSizeLink = TextFieldCoord.parseFraqValue(this.boundsRadius.getText());
		} catch (BadEntry e) {
			Shaker.shake("Invalide value in bond radius");
		}
	}
}