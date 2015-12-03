import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.vecmath.Point3f;

public class CellConstraintsPanel extends JPanel implements ActionListener {

	public TextFieldCoord cellSize;
	public TextFieldCoord cellAngles;
	private Univers univers;
	private Grid grid;
	private ActionListener actionListener;
	Cell cell;

	public CellConstraintsPanel(Cell cell, Univers univers, Grid grid) {
		this.univers = univers;
		this.grid = grid;
		this.cell = cell;
		
		setBorder(new TitledBorder("Cell constants"));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.insets = new Insets(0,0,0,0);  
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		
		cellSize = new TextFieldCoord.v(new String[] {"a", "b", "c"}, " Å");
		cellSize.addActionListener(this); 
		add(cellSize, c);
		cellAngles = new TextFieldCoord.v(new String[] {"alpha", "beta", "gamma"}, " °");
		cellAngles.addActionListener(this); 
		add(cellAngles, c);
		
		//c.insets = new Insets(10,0,0,0);  
		//drawCell = new JCheckBox("Show cell");
		//drawCell.addActionListener(this);
		//add(drawCell, c);
		//c.insets = new Insets(0,0,0,0);  
		
		reset();
	}
	
	public void reset() {
		Cell def = Cell.defaultCell();
		cell.set(def.a, def.b, def.c, def.alpha, def.beta, def.gamma);
		//drawCell.setSelected(true);
		updateValues();
	}

	public void updateValues() {
		cellSize.setValue((float)cell.a, (float)cell.b, (float)cell.c);
		cellAngles.setValue((float)cell.alpha, (float)cell.beta, (float)cell.gamma);
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	public void actionPerformed(ActionEvent e) {
/*
		if (e.getSource()==drawCell) {
			univers.beginModify();
			if (drawCell.isSelected()) grid.show();
			else grid.hide();
			univers.endModify();
		}
		else */ 
		if (e.getSource()==cellSize) {
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		}
		else if (e.getSource()==cellAngles) {
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		}
		
	}	

	
}
