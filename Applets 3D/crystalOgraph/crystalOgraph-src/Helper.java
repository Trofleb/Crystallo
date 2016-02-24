
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.vecmath.Point3d;

public class Helper extends JPanel {
	private JLabel atomCount, linkCount;
	private BondsPanel bondsPanel;
	public int nbAtom;
	private AtomsSelectPanel atomsSelectPanel;
	public PositionsList list;
	public Model model;

	public Helper() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		this.atomCount = new MultiLineToolTip.JLabel("0 Atom                       ");
		this.linkCount = new MultiLineToolTip.JLabel("0 Link                       ");
		this.add(this.atomCount, c);
		this.add(this.linkCount, c);

		this.list = new PositionsList();
		this.atomCount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					Helper.this.list.showOnOff(Helper.this.model);
			}
		});
	}

	public void linkToSelectPanel(AtomsSelectPanel atomsSelectPanel) {
		this.atomsSelectPanel = atomsSelectPanel;
	}

	public void setBondsPanel(BondsPanel bondsPanel) {
		this.bondsPanel = bondsPanel;
	}

	public void setBondAddEnable(boolean b) {
		if (this.bondsPanel != null)
			this.bondsPanel.setAddEnable(b);
	}

	public void setBondDelEnable(boolean b) {
		if (this.bondsPanel != null)
			this.bondsPanel.setDelEnable(b);
	}

	public void watchNbAtom(int inCell, int total) {
		if (this.atomCount != null) {
			this.atomCount.setText(total + " Atom" + (total > 1 ? "s" : ""));
			this.atomCount.setToolTipText(inCell + " independant" + (inCell > 1 ? "s" : "") + ", " + total
					+ " total.\n(double click to show list)");
			this.nbAtom = total;
		}
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setTotalAtoms(total);
	}

	public void watchNbLink(int inCell, int total) {
		if (this.linkCount != null) {
			this.linkCount.setText(total + " Link" + (total > 1 ? "s" : ""));
			this.linkCount.setToolTipText(inCell + " independant" + (inCell > 1 ? "s" : "") + ", " + total + " total.");
		}
	}

	public void setCurrBoundSize(float s) {
		this.bondsPanel.setRadius(s);
	}

	public void setCurrBoundColor(Color c) {
		this.bondsPanel.setColor(c);
	}

	public void setNbAtomSelected(int n) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setSelectedAtoms(n);
	}

	public void setHiddenAtoms(int n) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setHiddenAtoms(n);
	}

	public void decrementSelAtoms() {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.decrementSelAtoms();
	}

	public void incHiddenAtoms() {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.incHiddenAtoms();
	}

	public void decHiddenAtoms() {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.decHiddenAtoms();
	}

	public void decHiddenAtoms(int n) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.decHiddenAtoms(n);
	}

	public void setCropEnable(boolean b) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setCropEnable(b);
	}

	public void setCutEnable(boolean b) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setCutEnable(b);
	}

	public void setShowAllEnable(boolean b) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setShowAllEnable(b);
	}

	public void setColorChoiceEnable(boolean b) {
		if (this.atomsSelectPanel != null)
			this.atomsSelectPanel.setColorChoiceEnable(b);
	}

	public void selectAtomInTable(Point3d pos) {

	}

	public void unSelectAtomInTable(Point3d pos) {

	}
}
