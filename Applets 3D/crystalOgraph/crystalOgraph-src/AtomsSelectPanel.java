import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.vecmath.Color3f;

import ColorComboBox.IncompatibleLookAndFeelException;

public class AtomsSelectPanel extends HVPanel.v {
	private JLabel selected, hidden;
	private int nbSel, nbHidden;
	private JButton cut, crop, showAll;
	private Model model;
	private ColorChoice colorChoice;

	public AtomsSelectPanel(Model model) {
		this.model = model;
		this.setBorder(new TitledBorder("Atoms selection"));

		this.addComp(this.selected = new JLabel());
		this.addComp(this.hidden = new JLabel());
		HVPanel.h p1 = new HVPanel.h();
		p1.addButton(this.cut = new JButton("Hide"));
		p1.addButton(this.crop = new JButton("Keep"));
		this.addSubPane(p1);
		this.addButton(this.showAll = new JButton("Show back all"));
		this.showAll.setEnabled(false);

		HVPanel.h p2 = new HVPanel.h();
		try {
			p2.addComp(new JLabel("Color"));
			p2.expandH(true);
			p2.addComp(this.colorChoice = new ColorChoice());
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		this.addSubPane(p2);

		JButton originalColorButton = new JButton("Original color");
		originalColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AtomsSelectPanel.this.model.changeColorSelectedBack();
			}
		});

		this.colorChoice.setAdditionalCustomButton(originalColorButton);
		this.colorChoice.setPreferredSize(new Dimension(50, 20));
		this.colorChoice.setSelectedIndex(0);
		this.colorChoice.addActionListener(this);
		this.colorChoice.setEnabled(false);

		this.updateLabels();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.cut)
			this.model.hideSelected();
		else if (e.getSource() == this.crop)
			this.model.hideNotSelected();
		else if (e.getSource() == this.showAll)
			this.model.unHide();
		else if (e.getSource() == this.colorChoice) {
			Color c = this.colorChoice.getSelectedColor();
			this.model.changeColorSelected(new Color3f(c));
		}
	}

	public void setColorChoiceEnable(boolean b) {
		this.colorChoice.setEnabled(b);
	}

	public void setCropEnable(boolean b) {
		this.crop.setEnabled(b);
	}

	public void setCutEnable(boolean b) {
		this.cut.setEnabled(b);
	}

	public void setShowAllEnable(boolean b) {
		this.showAll.setEnabled(b);
	}

	public void setTotalAtoms(int n) {
		this.updateLabels();
	}

	public void setSelectedAtoms(int n) {
		this.nbSel = n;
		this.updateLabels();
	}

	public void setHiddenAtoms(int n) {
		this.nbHidden = n;
		this.updateLabels();
	}

	public void incHiddenAtoms() {
		this.nbHidden++;
		this.updateLabels();
	}

	public void decHiddenAtoms() {
		this.nbHidden--;
		this.updateLabels();
	}

	public void decHiddenAtoms(int n) {
		this.nbHidden -= n;
		this.updateLabels();
	}

	public void decrementSelAtoms() {
		this.nbSel--;
		this.updateLabels();
	}

	private void updateLabels() {
		// selected.setText("Selected: "+nbSel+" / "+nbTotal);
		this.selected.setText(
				this.nbSel == 0 ? "No selected atom" : (this.nbSel + " selected Atom" + (this.nbSel > 1 ? "s" : "")));
		// hidden.setText("Hidden: "+nbHidden+" / "+nbTotal);
		this.hidden.setText(this.nbHidden == 0 ? "No hidden atom"
				: (this.nbHidden + " hidden Atom" + (this.nbHidden > 1 ? "s" : "")));
		if (this.nbSel == 0) {
			this.cut.setEnabled(false);
			this.crop.setEnabled(false);
			this.colorChoice.setEnabled(false);
		} else {
			this.cut.setEnabled(true);
			this.crop.setEnabled(true);
			this.colorChoice.setEnabled(true);
		}
	}
}
