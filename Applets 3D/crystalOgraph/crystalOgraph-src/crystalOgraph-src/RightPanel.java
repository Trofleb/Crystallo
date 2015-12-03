import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
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
	private boolean resetAngle;
	
	public RightPanel(crystalOgraph applet, Univers univers, Model model, Grid grid, Helper elementsCounter) {		
		this.univers = univers;
		this.model = model;
		this.grid = grid;

		resetAngle = false;
		
		
		JPanel p = new JPanel();
		//JScrollPane scrollPane = new JScrollPane(p);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setMinimumSize(new Dimension(50, 500));
		
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		//c.anchor = GridBagConstraints.PAGE_START;
		c.anchor = GridBagConstraints.NORTH;
		c.weightx=1.0; c.weighty=0;
		
		c.insets = new Insets(0,0,0,0);  

		spaceGroupPanel = new SpaceGroupPanel(univers, model, grid);
		spaceGroupPanel.addActionListener(this);
		p.add(spaceGroupPanel, c);
				
		cellConstraintsPanel = new CellConstraintsPanel(model.cell, univers, grid);
		cellConstraintsPanel.addActionListener(this);
		p.add(cellConstraintsPanel, c);
				
		expandPanel = new ExpandPanel(univers, model);
		p.add(expandPanel, c);
		
		bondsPanel = new BondsPanel(univers, model);
		p.add(bondsPanel, c);
		
		bkgColorPanel = new UniversPanel(applet, univers, grid);
		p.add(bkgColorPanel, c);

		c.weightx=1.0; c.weighty=1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(10,10,10,0);  
		p.add(elementsCounter, c);
		elementsCounter.setBondsPanel(bondsPanel);
		
		c.weightx=1.0; c.weighty=1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(0,0,0,0);  
		
		atomsSelectPanel = new AtomsSelectPanel(model);
		p.add(atomsSelectPanel, c);
		elementsCounter.linkToSelectPanel(atomsSelectPanel);
		
		c.weightx=1.0; c.weighty=1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.insets = new Insets(10,10,10,0);  
		p.add(new JPanel(), c);

		
		
		
		
/*		
		c.weightx=1.0; c.weighty=1;
		c.anchor = GridBagConstraints.PAGE_END;
		JButton b = new MultiLineToolTip.JButton("lala");
		b.addMouseListener(spaceGroupPanel.popupListener);
		p.add(b, c);
*/		
		add(p);

		//bondsPanel.boundsAdd.addMouseListener(spaceGroupPanel.popupListener);
		
		//spaceGroupPanel.requestFocus();
		
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==cellConstraintsPanel) {
			updateCellParams();
			updateCell();
			univers.reset();
			bondsPanel.removeBonds();
		}
		else if (e.getSource()==spaceGroupPanel) {
			cellConstraintsPanel.cellAngles.setValueX(90);
			cellConstraintsPanel.cellAngles.setValueY(90);
			cellConstraintsPanel.cellAngles.setValueZ(90);
			updateConstraints();
			bondsPanel.removeBonds();
		}
	}	



	private void updateConstraints() {
		int[] cons = CellSymetries.getConstraints(spaceGroupPanel.getCellNo(), spaceGroupPanel.getCellVariant());
		cellConstraintsPanel.cellSize.setEditable(cons[0]==1, cons[1]==1, cons[2]==1);
		cellConstraintsPanel.cellAngles.setEditable(cons[3]==1, cons[4]==1, cons[5]==1);
		if (cons[3]>1) cellConstraintsPanel.cellAngles.setValueX(cons[3]);
		if (cons[4]>1) cellConstraintsPanel.cellAngles.setValueY(cons[4]);
		if (cons[5]>1) cellConstraintsPanel.cellAngles.setValueZ(cons[5]);
		updateCellParams();
		updateCell();
	}
	
	
	public void updateCell(Cell cell) {
		//grid.reset();
		//model.refresh();
		//bondsPanel.reset();
		cellConstraintsPanel.updateValues();
		try {
			spaceGroupPanel.setCell(cell.no, cell.choice);
		}
		catch(Exception e) {
			Shaker.shake("Invalid space group");
		}
		updateConstraints();
	}

	public void updateCell() {
		modelIsNowModified();
		try {
			Point3f v = cellConstraintsPanel.cellSize.parseFraqValue();
			Point3f a = cellConstraintsPanel.cellAngles.parseAngleValue();
			if (v.x<=0.0 || v.y<=0.0 || v.z<=0.0) throw new BadEntry();
			if (a.x<0.0 || a.y<0.0 || a.z<0.0) throw new BadEntry();
			if (a.x>=180.0 || a.y>=180.0 || a.z>=180.0) throw new BadEntry();
			
			model.cell.set(spaceGroupPanel.no, spaceGroupPanel.choice, v.x, v.y, v.z, a.x, a.y, a.z);
			grid.reset();
			model.refresh();
			
			bondsPanel.reset();
		}
		catch(BadEntry e) {
			Shaker.shake("Invalid cell constraints");
		}
	}
	
	
	public void updateCellParams() {
		try {
			int[] cons = CellSymetries.getConstraints(spaceGroupPanel.getCellNo(), spaceGroupPanel.getCellVariant());
			if (cons[1]==0) cellConstraintsPanel.cellSize.setValueY(cellConstraintsPanel.cellSize.parseFraqValue().x);
			if (cons[2]==0) cellConstraintsPanel.cellSize.setValueZ(cellConstraintsPanel.cellSize.parseFraqValue().x);
			if (cons[4]==0) cellConstraintsPanel.cellAngles.setValueY(cellConstraintsPanel.cellAngles.parseFraqValue().x);
			if (cons[5]==0) cellConstraintsPanel.cellAngles.setValueZ(cellConstraintsPanel.cellAngles.parseFraqValue().x);
		}
		catch (BadEntry e) {
			Shaker.shake("Invalid cell constraints");
		}
		
	}

	public void modelIsNowModified() {
		justImported = false;
	}
	public void modelIsNowImported() {
		justImported = true;
	}
	public boolean IsJustImported() {
		return justImported;
	}

}
