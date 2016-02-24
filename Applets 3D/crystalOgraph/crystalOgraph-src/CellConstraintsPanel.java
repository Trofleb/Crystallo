import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class CellConstraintsPanel extends JPanel implements ActionListener {

	public TextFieldCoord cellSize;
	public TextFieldCoord cellAngles;
	private ActionListener actionListener;
	Cell cell;

	public CellConstraintsPanel(Cell cell, Univers univers, Grid grid) {
		this.cell = cell;

		this.setBorder(new TitledBorder("Cell constants"));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// c.insets = new Insets(0,0,0,0);
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		this.cellSize = new TextFieldCoord.v(new String[] { "a", "b", "c" }, " Å");
		this.cellSize.addActionListener(this);
		this.add(this.cellSize, c);
		this.cellAngles = new TextFieldCoord.v(new String[] { "alpha", "beta", "gamma" }, " °");
		this.cellAngles.addActionListener(this);
		this.add(this.cellAngles, c);

		// c.insets = new Insets(10,0,0,0);
		// drawCell = new JCheckBox("Show cell");
		// drawCell.addActionListener(this);
		// add(drawCell, c);
		// c.insets = new Insets(0,0,0,0);

		this.reset();
	}

	public void reset() {
		Cell def = Cell.defaultCell();
		this.cell.set(def.a, def.b, def.c, def.alpha, def.beta, def.gamma);
		// drawCell.setSelected(true);
		this.updateValues();
	}

	public void updateValues() {
		this.cellSize.setValue((float) this.cell.a, (float) this.cell.b, (float) this.cell.c);
		this.cellAngles.setValue((float) this.cell.alpha, (float) this.cell.beta, (float) this.cell.gamma);
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * if (e.getSource()==drawCell) {
		 * univers.beginModify();
		 * if (drawCell.isSelected()) grid.show();
		 * else grid.hide();
		 * univers.endModify();
		 * }
		 * else
		 */
		if (e.getSource() == this.cellSize)
			this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		else if (e.getSource() == this.cellAngles)
			this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));

	}

}
