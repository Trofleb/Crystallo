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
	private int nbTotal, nbSel, nbHidden;
	private JButton cut, crop, showAll;
	private Model model;
	private ColorChoice colorChoice;
	
	public AtomsSelectPanel(Model model) {
		this.model = model;
		setBorder(new TitledBorder("Atoms selection"));

		addComp(selected=new JLabel());
		addComp(hidden=new JLabel());
		HVPanel.h p1 = new HVPanel.h();
		p1.addButton(cut = new JButton("Hide"));
		p1.addButton(crop = new JButton("Keep"));
		addSubPane(p1);
		addButton(showAll = new JButton("Show back all"));
		showAll.setEnabled(false);
		
		HVPanel.h p2 = new HVPanel.h();
		try {
			p2.addComp(new JLabel("Color"));
			p2.expandH(true);
			p2.addComp(colorChoice=new ColorChoice());
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		addSubPane(p2);
		
		JButton originalColorButton = new JButton("Original color");
		originalColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AtomsSelectPanel.this.model.changeColorSelectedBack();
			}
		});
		
		colorChoice.setAdditionalCustomButton(originalColorButton);
		colorChoice.setPreferredSize(new Dimension(50, 20));
		colorChoice.setSelectedIndex(0);
		colorChoice.addActionListener(this);
		colorChoice.setEnabled(false);
		
		updateLabels();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==cut) {
			model.hideSelected();
		}
		else if (e.getSource()==crop) {
			model.hideNotSelected();
		}
		else if (e.getSource()==showAll) {
			model.unHide();
		}
		else if (e.getSource()==colorChoice) {
			Color c = colorChoice.getSelectedColor();
			model.changeColorSelected(new Color3f(c));
		}
	}
	
	public void setColorChoiceEnable(boolean b) {
		colorChoice.setEnabled(b);
	}
	public void setCropEnable(boolean b) {
		crop.setEnabled(b);
	}
	public void setCutEnable(boolean b) {
		cut.setEnabled(b);
	}
	public void setShowAllEnable(boolean b) {
		showAll.setEnabled(b);
	}
	
	
	public void setTotalAtoms(int n) {
		nbTotal = n;
		updateLabels();
	}
	public void setSelectedAtoms(int n) {
		nbSel = n;
		updateLabels();
	}
	
	public void setHiddenAtoms(int n) {
		nbHidden = n;
		updateLabels();
	}
	
	public void incHiddenAtoms() {
		nbHidden++;
		updateLabels();
	}
	public void decHiddenAtoms() {
		nbHidden--;
		updateLabels();
	}
	public void decHiddenAtoms(int n) {
		nbHidden-=n;
		updateLabels();
	}

	public void decrementSelAtoms() {
		nbSel--;
		updateLabels();
	}
	
	private void updateLabels() {
		//selected.setText("Selected: "+nbSel+" / "+nbTotal);
		selected.setText(nbSel==0?"No selected atom":(nbSel+" selected Atom"+(nbSel>1?"s":"")));
		//hidden.setText("Hidden: "+nbHidden+" / "+nbTotal);
		hidden.setText(nbHidden==0?"No hidden atom":(nbHidden+" hidden Atom"+(nbHidden>1?"s":"")));
		if (nbSel==0) {
			cut.setEnabled(false);
			crop.setEnabled(false);
			colorChoice.setEnabled(false);
		}
		else {
			cut.setEnabled(true);
			crop.setEnabled(true);
			colorChoice.setEnabled(true);
		}
	}
}
