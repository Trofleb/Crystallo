import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class SpaceGroupPanel extends JPanel implements ActionListener {

	public JPopupMenu popup;
	private JTextField cellType, cellNo;
	private JButton cellChoice;
	public int no, category, choice;
	private Model model;
	private ActionListener actionListener;
	private Color defaultColor;
	public MouseListener popupListener;

	public SpaceGroupPanel(Univers univers, Model model, Grid grid) {
		this.model = model;
		this.setBorder(new TitledBorder("Space Group"));

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);

		this.cellNo = new MultiLineToolTip.JTextField();
		this.cellNo.setColumns(3);
		this.cellNo.addActionListener(this);
		this.cellType = new MultiLineToolTip.JTextField();
		this.cellType.setColumns(6);
		this.cellType.addActionListener(this);
		this.cellChoice = new MultiLineToolTip.JButton(" ");

		this.cellChoice.addActionListener(this);
		this.defaultColor = this.cellChoice.getBackground();

		this.cellNo.setToolTipText("Space group number.\n(Varies between 1 and 230)");
		this.cellType.setToolTipText("Space group symbol.\n(Select one in the popup menu)");
		this.cellChoice.setToolTipText("No variant available for the current space group.");

		Font f = this.cellChoice.getFont();
		Font ff = new Font(f.getName(), f.getStyle(), 3 * f.getSize() / 4 + 2);
		this.cellChoice.setFont(ff);
		Insets margins = new Insets(0, 0, 0, 0);
		this.cellChoice.setMargin(margins);

		c.gridx = 0;
		c.gridy = 0;
		this.add(this.cellNo, c);
		c.gridx = 1;
		c.gridy = 0;
		this.add(this.cellType, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		this.add(this.cellChoice, c);

		// Create the popup menu.
		this.popup = new JPopupMenu();
		this.popupListener = new PopupListener(this.popup);
		this.cellType.addMouseListener(this.popupListener);

		this.populateChooser();
		this.reset();
	}

	private void populateChooser() {
		int max = 30;
		for (int i = 0; i < CellSymetries.categoriesName.length; i++) {
			JMenu item = this.addCategory(CellSymetries.categoriesName[i]);
			int n = CellSymetries.cellTypes[i].length;
			if (n > max) {
				GridLayout menuGrid = new GridLayout((int) Math.ceil(n / Math.ceil(n / (double) max)),
						(int) Math.ceil(n / (double) max));
				item.getPopupMenu().setLayout(menuGrid);
			}

			for (int j = 0; j < CellSymetries.cellTypes[i].length; j++)
				this.addItem(item, CellSymetries.cellTypes[i][j]);
		}
	}

	private JMenu addCategory(String s) {
		JMenu menuItem = new JMenu(s);
		this.popup.add(menuItem);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == this.cellNo)
				this.setCell(Integer.parseInt(this.cellNo.getText()), 0);
			else if (e.getSource() == this.cellType)
				this.setCell(this.cellType.getText());
			else if (e.getSource() == this.cellChoice)
				this.setCellVariant((this.choice + 1) % CellSymetries.howManyChoices(this.no));
			else
				this.setCell(((JMenuItem) e.getSource()).getText());
		} catch (Exception ex) {
			Shaker.shake("Bad entry in space group");
			// ex.printStackTrace(System.err);
			try {
				this.setCell(this.no, this.choice);
			} catch (Exception ee) {
			}
		}
		this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}

	public void reset() {
		try {
			Cell def = Cell.defaultCell();
			this.setCell(def.no, 0);
			this.setCellVariant(def.choice);
			this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
		} catch (Exception e) {
		}
	}

	public int getCellNo() {
		return this.no;
	}

	public int getCellVariant() {
		return this.choice;
	}

	public int getCellCategory() {
		return this.category;
	}

	public void setCellVariant(int choice) {
		this.choice = choice;
		if (CellSymetries.hasChoice(this.no)) {
			this.cellChoice.setEnabled(true);
			// cellChoice.setForeground(Color.red);
			this.cellChoice.setBackground(Color.yellow);
			this.cellChoice.setText(CellSymetries.getChoice(this.no, choice));
			this.cellType.setText(CellSymetries.buildSGname(this.no, choice));
			this.cellChoice.setToolTipText(
					"The current space group has " + CellSymetries.howManyChoices(this.no) + " variants in memory.");
		} else {
			this.cellChoice.setEnabled(false);
			this.cellChoice.setBackground(this.defaultColor);
			this.cellChoice.setText(" ");
			this.cellChoice.setToolTipText("No variant available for the current space group.");
		}
	}

	public void setCell(int n, int c) throws BadEntry {
		if (n == 0) {
			this.cellType.setText(this.model.cell.m_sgName);
			this.cellNo.setText("" + this.model.cell.m_sgNo);
			this.cellChoice.setEnabled(false);
			this.cellChoice.setBackground(this.defaultColor);
			this.cellChoice.setText("Non-standard SG !");
			this.cellChoice.setToolTipText(
					"This is a non standard space group.\nThe equivalent positions have been taken from the CIF file.");
			this.no = 0;
			this.category = 0;
			this.choice = 0;
		} else {
			for (int i = 0; i < CellSymetries.categoriesBaseNo.length; i++)
				if (n >= CellSymetries.categoriesBaseNo[i] && n < CellSymetries.categoriesBaseNo[i + 1]) {
					String s = CellSymetries.cellTypes[i][n - CellSymetries.categoriesBaseNo[i]];
					this.cellType.setText(s);
					this.cellNo.setText("" + n);
					this.no = n;
					this.category = i;
					this.choice = c;
					this.setCellVariant(this.choice);
					return;
				}
			throw new BadEntry();
		}
	}

	public void setCell(String s) throws BadEntry {
		for (int i = 0; i < CellSymetries.categoriesName.length; i++)
			// int n2 = CellSymetries.cellTypes[i].length;
			for (int j = 0; j < CellSymetries.cellTypes[i].length; j++)
				if (s.equalsIgnoreCase(CellSymetries.cellTypes[i][j])) {
					this.no = (j + CellSymetries.categoriesBaseNo[i]);
					this.category = i;
					this.cellType.setText(CellSymetries.cellTypes[i][j]);
					this.cellNo.setText("" + this.no);
					this.setCellVariant(0);
					return;
				}
		throw new BadEntry();
	}
}

class PopupListener extends MouseAdapter {
	JPopupMenu popup;

	public PopupListener(JPopupMenu popup) {
		this.popup = popup;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.maybeShowPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
		// popup.show(e.getComponent().getParent().getParent(), e.getX(),
		// e.getY());
		this.popup.show(e.getComponent(), e.getX(), e.getY());
	}
}
