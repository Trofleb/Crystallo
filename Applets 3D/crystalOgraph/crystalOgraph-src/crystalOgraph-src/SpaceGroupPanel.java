import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.vecmath.Point3f;

public class SpaceGroupPanel extends JPanel implements ActionListener {

	public JPopupMenu popup;
	private JTextField cellType, cellNo;
	private JButton cellChoice;
	public int no, category, choice;
	private Univers univers;
	private Model model; 
	private Grid grid;
	private ActionListener actionListener;
	private Color defaultColor;
	public MouseListener popupListener;
	
	
	
	public SpaceGroupPanel(Univers univers, Model model, Grid grid) {
		this.univers = univers;
		this.model = model;
		this.grid = grid;
		
		setBorder(new TitledBorder("Space Group"));
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,0,0);  


		cellNo = new MultiLineToolTip.JTextField();
		cellNo.setColumns(3);
		cellNo.addActionListener(this);
		cellType = new MultiLineToolTip.JTextField();
		cellType.setColumns(6);
		cellType.addActionListener(this);
		cellChoice = new MultiLineToolTip.JButton(" ");

		cellChoice.addActionListener(this);
		defaultColor = cellChoice.getBackground();
		
		cellNo.setToolTipText("Space group number.\n(Varies between 1 and 230)");
		cellType.setToolTipText("Space group symbol.\n(Select one in the popup menu)");
		cellChoice.setToolTipText("No variant available for the current space group.");
		
		
		
		
		Font f = cellChoice.getFont();
		Font ff = new Font(f.getName(), f.getStyle(), 3*f.getSize()/4+2);
		cellChoice.setFont(ff);
		Insets margins = new Insets(0, 0, 0, 0);
		cellChoice.setMargin(margins);
		
		c.gridx = 0; c.gridy = 0;
		add(cellNo, c);
		c.gridx = 1; c.gridy = 0;
		add(cellType, c);
		c.gridx = 0; c.gridy = 1;
		c.gridwidth=2;
		add(cellChoice, c);

		//Create the popup menu.
		popup = new JPopupMenu();
		popupListener = new PopupListener(popup);
		cellType.addMouseListener(popupListener);
		
		populateChooser();
		reset();
	}

	
	private void populateChooser() {
		int max=30;
		for (int i=0; i<CellSymetries.categoriesName.length; i++) {
			JMenu item = addCategory(CellSymetries.categoriesName[i]);
			int n = CellSymetries.cellTypes[i].length;
			if (n>max) {
				GridLayout menuGrid = new GridLayout((int)Math.ceil(n/Math.ceil(n/(double)max)), (int)Math.ceil(n/(double)max)); 
				item.getPopupMenu().setLayout(menuGrid); 
			}

			for (int j=0; j<CellSymetries.cellTypes[i].length; j++) {
				addItem(item, CellSymetries.cellTypes[i][j]);
			}
		}
	}
	
	private JMenu addCategory(String s) {
		JMenu menuItem = new JMenu(s);
		popup.add(menuItem);
		return menuItem;
	}

	private JMenuItem addItem(JMenu parent, String s) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.addActionListener(this);
		parent.add(menuItem);
		return menuItem;
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource()==cellNo) {
				setCell(Integer.parseInt(cellNo.getText()), 0);
			}
			else if (e.getSource()==cellType) {
				setCell(cellType.getText());
			}
			else if (e.getSource()==cellChoice) {
				setCellVariant((choice+1)%CellSymetries.howManyChoices(no));
			}
			else {
				setCell(((JMenuItem)e.getSource()).getText());
			}
		}
		catch (Exception ex) {
			Shaker.shake("Bad entry in space group");
			//ex.printStackTrace(System.err);
			try {
				setCell(no, choice);
			}catch(Exception ee){}
		}
		actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}
	
	

	
	public void reset() {
		try {
			Cell def = Cell.defaultCell();
			setCell(def.no, 0);
			setCellVariant(def.choice);
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		} catch (Exception e){}
	}
	
	public int getCellNo() {
		return no;
	}
	
	public int getCellVariant() {
		return choice;
	}

	public int getCellCategory() {
		return category;
	}

	public void setCellVariant(int choice) {
		this.choice=choice;
		if (CellSymetries.hasChoice(no)) {
			cellChoice.setEnabled(true);
			//cellChoice.setForeground(Color.red);
			cellChoice.setBackground(Color.yellow);
			cellChoice.setText(CellSymetries.getChoice(no, choice));
			cellType.setText(CellSymetries.buildSGname(no, choice));
			cellChoice.setToolTipText("The current space group has "+CellSymetries.howManyChoices(no)+" variants in memory.");
		}
		else {
			cellChoice.setEnabled(false);
			cellChoice.setBackground(defaultColor);
			cellChoice.setText(" ");
			cellChoice.setToolTipText("No variant available for the current space group.");
		}
	}
	
	public void setCell(int n, int c) throws BadEntry {
		if (n==0) {
			cellType.setText(model.cell.m_sgName);
			cellNo.setText(""+model.cell.m_sgNo);
			cellChoice.setEnabled(false);
			cellChoice.setBackground(defaultColor);
			cellChoice.setText("Non-standard SG !");
			cellChoice.setToolTipText("This is a non standard space group.\nThe equivalent positions have been taken from the CIF file.");
			no=0;
			category=0;
			choice=0;
		}
		else {
			for (int i=0; i<CellSymetries.categoriesBaseNo.length; i++) {
				if (n>=CellSymetries.categoriesBaseNo[i]&&n<CellSymetries.categoriesBaseNo[i+1]) {
					String s = CellSymetries.cellTypes[i][n-CellSymetries.categoriesBaseNo[i]];
					cellType.setText(s);
					cellNo.setText(""+n);
					no=n;
					category=i;
					choice = c;
					setCellVariant(choice);
					return;
				}
			}
			throw new BadEntry();
		}
	}

	public void setCell(String s) throws BadEntry {
		for (int i=0; i<CellSymetries.categoriesName.length; i++) {
			//int n2 = CellSymetries.cellTypes[i].length;
			for (int j=0; j<CellSymetries.cellTypes[i].length; j++) {
				if (s.equalsIgnoreCase(CellSymetries.cellTypes[i][j])) {
					no=(j+CellSymetries.categoriesBaseNo[i]);
					category=i;
					cellType.setText(CellSymetries.cellTypes[i][j]);
					cellNo.setText(""+no);
					setCellVariant(0);
					return;
				}
			}
		}
		throw new BadEntry();
	}
}


class PopupListener extends MouseAdapter {
	JPopupMenu popup;
	
	public PopupListener(JPopupMenu popup) {
		this.popup=popup;
	}
	
	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
//		popup.show(e.getComponent().getParent().getParent(), e.getX(), e.getY());
		popup.show(e.getComponent(), e.getX(), e.getY());
	}
}
