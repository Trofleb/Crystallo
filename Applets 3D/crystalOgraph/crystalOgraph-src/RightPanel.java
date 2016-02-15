import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.vecmath.Point3f;

public class RightPanel extends JPanel implements ActionListener {
	public SpaceGroupPanel spaceGroupPanel;
	public CellConstraintsPanel cellConstraintsPanel;
	public ExpandPanel expandPanel;
	public BondsPanel bondsPanel;
	public UniversPanel bkgColorPanel;
	public AtomsSelectPanel atomsSelectPanel;
	private Univers univers;
	private Model model;
	private Grid grid;
	private boolean justImported;

	public RightPanel(crystalOgraph applet, Univers univers, Model model, Grid grid, Helper elementsCounter) {
		this.univers = univers;
		this.model = model;
		this.grid = grid;

		JPanel p = new JPanel();
		// JScrollPane scrollPane = new JScrollPane(p);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane.setMinimumSize(new Dimension(50, 500));

		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		// c.anchor = GridBagConstraints.PAGE_START;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 1.0;
		c.weighty = 0;

		c.insets = new Insets(0, 0, 0, 0);

		this.spaceGroupPanel = new SpaceGroupPanel(univers, model, grid);
		this.spaceGroupPanel.addActionListener(this);
		p.add(this.spaceGroupPanel, c);

		this.cellConstraintsPanel = new CellConstraintsPanel(model.cell, univers, grid);
		this.cellConstraintsPanel.addActionListener(this);
		p.add(this.cellConstraintsPanel, c);

		this.expandPanel = new ExpandPanel(univers, model);
		p.add(this.expandPanel, c);

		this.bondsPanel = new BondsPanel(univers, model);
		p.add(this.bondsPanel, c);

		this.bkgColorPanel = new UniversPanel(applet, univers, grid);
		p.add(this.bkgColorPanel, c);

		c.weightx = 1.0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(10, 10, 10, 0);
		p.add(elementsCounter, c);
		elementsCounter.setBondsPanel(this.bondsPanel);

		c.weightx = 1.0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(0, 0, 0, 0);

		this.atomsSelectPanel = new AtomsSelectPanel(model);
		p.add(this.atomsSelectPanel, c);
		elementsCounter.linkToSelectPanel(this.atomsSelectPanel);

		c.weightx = 1.0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(10, 10, 10, 0);
		p.add(new JPanel(), c);

		/*
		 * c.weightx=1.0; c.weighty=1;
		 * c.anchor = GridBagConstraints.PAGE_END;
		 * JButton b = new MultiLineToolTip.JButton("lala");
		 * b.addMouseListener(spaceGroupPanel.popupListener);
		 * p.add(b, c);
		 */
		this.add(p);

		// bondsPanel.boundsAdd.addMouseListener(spaceGroupPanel.popupListener);

		// spaceGroupPanel.requestFocus();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.cellConstraintsPanel) {
			this.updateCellParams();
			this.updateCell();
			this.univers.reset();
			this.bondsPanel.removeBonds();
		} else if (e.getSource() == this.spaceGroupPanel) {
			this.cellConstraintsPanel.cellAngles.setValueX(90);
			this.cellConstraintsPanel.cellAngles.setValueY(90);
			this.cellConstraintsPanel.cellAngles.setValueZ(90);
			this.updateConstraints();
			this.bondsPanel.removeBonds();
		}
	}

	private void updateConstraints() {
		int[] cons = CellSymetries.getConstraints(this.spaceGroupPanel.getCellNo(),
				this.spaceGroupPanel.getCellVariant());
		this.cellConstraintsPanel.cellSize.setEditable(cons[0] == 1, cons[1] == 1, cons[2] == 1);
		this.cellConstraintsPanel.cellAngles.setEditable(cons[3] == 1, cons[4] == 1, cons[5] == 1);
		if (cons[3] > 1)
			this.cellConstraintsPanel.cellAngles.setValueX(cons[3]);
		if (cons[4] > 1)
			this.cellConstraintsPanel.cellAngles.setValueY(cons[4]);
		if (cons[5] > 1)
			this.cellConstraintsPanel.cellAngles.setValueZ(cons[5]);
		this.updateCellParams();
		this.updateCell();
	}

	public void updateCell(Cell cell) {
		// grid.reset();
		// model.refresh();
		// bondsPanel.reset();
		this.cellConstraintsPanel.updateValues();
		try {
			this.spaceGroupPanel.setCell(cell.no, cell.choice);
		} catch (Exception e) {
			Shaker.shake("Invalid space group");
		}
		this.updateConstraints();
	}

	public void updateCell() {
		this.modelIsNowModified();
		try {
			Point3f v = this.cellConstraintsPanel.cellSize.parseFraqValue();
			Point3f a = this.cellConstraintsPanel.cellAngles.parseAngleValue();
			if (v.x <= 0.0 || v.y <= 0.0 || v.z <= 0.0)
				throw new BadEntry();
			if (a.x < 0.0 || a.y < 0.0 || a.z < 0.0)
				throw new BadEntry();
			if (a.x >= 180.0 || a.y >= 180.0 || a.z >= 180.0)
				throw new BadEntry();

			this.model.cell.set(this.spaceGroupPanel.no, this.spaceGroupPanel.choice, v.x, v.y, v.z, a.x, a.y, a.z);
			this.grid.reset();
			this.model.refresh();

			this.bondsPanel.reset();
		} catch (BadEntry e) {
			Shaker.shake("Invalid cell constraints");
		}
	}

	public void updateCellParams() {
		try {
			int[] cons = CellSymetries.getConstraints(this.spaceGroupPanel.getCellNo(),
					this.spaceGroupPanel.getCellVariant());
			if (cons[1] == 0)
				this.cellConstraintsPanel.cellSize.setValueY(this.cellConstraintsPanel.cellSize.parseFraqValue().x);
			if (cons[2] == 0)
				this.cellConstraintsPanel.cellSize.setValueZ(this.cellConstraintsPanel.cellSize.parseFraqValue().x);
			if (cons[4] == 0)
				this.cellConstraintsPanel.cellAngles.setValueY(this.cellConstraintsPanel.cellAngles.parseFraqValue().x);
			if (cons[5] == 0)
				this.cellConstraintsPanel.cellAngles.setValueZ(this.cellConstraintsPanel.cellAngles.parseFraqValue().x);
		} catch (BadEntry e) {
			Shaker.shake("Invalid cell constraints");
		}

	}

	public void modelIsNowModified() {
		this.justImported = false;
	}

	public void modelIsNowImported() {
		this.justImported = true;
	}

	public boolean IsJustImported() {
		return this.justImported;
	}

}
