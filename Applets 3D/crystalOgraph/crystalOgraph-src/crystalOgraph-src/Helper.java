
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		atomCount = new MultiLineToolTip.JLabel("0 Atom                       ");
		linkCount = new MultiLineToolTip.JLabel("0 Link                       ");
		add(atomCount, c);
		add(linkCount, c);
		
		
		list = new PositionsList();
		atomCount.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2){
        	list.showOnOff(model);
        }
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
		if (bondsPanel!=null)
			bondsPanel.setAddEnable(b);
	}
	
	public void setBondDelEnable(boolean b) {
		if (bondsPanel!=null)
			bondsPanel.setDelEnable(b);
	}
	
	
	public void watchNbAtom(int inCell, int total) {
		if (atomCount!=null) {
			atomCount.setText(total+" Atom"+(total>1?"s":""));
			atomCount.setToolTipText(inCell+" independant"+(inCell>1?"s":"")+", "+total+" total.\n(double click to show list)");
			nbAtom = total;
		}
		if (atomsSelectPanel!=null) {
			atomsSelectPanel.setTotalAtoms(total);
		}
	}

	public void watchNbLink(int inCell, int total) {
		if (linkCount!=null) {
			linkCount.setText(total+" Link"+(total>1?"s":""));
			linkCount.setToolTipText(inCell+" independant"+(inCell>1?"s":"")+", "+total+" total.");
		}
	}
	
	public void setCurrBoundSize(float s) {
		bondsPanel.setRadius(s);
	}

	public void setCurrBoundColor(Color c) {
		bondsPanel.setColor(c);
	}
	
	public void setNbAtomSelected(int n) {
		if (atomsSelectPanel!=null)	atomsSelectPanel.setSelectedAtoms(n);
	}

	public void setHiddenAtoms(int n) {
		if (atomsSelectPanel!=null)	atomsSelectPanel.setHiddenAtoms(n);
	}
	
	public void decrementSelAtoms() {
		if (atomsSelectPanel!=null) {
			atomsSelectPanel.decrementSelAtoms();
		}
	}
	public void incHiddenAtoms() {
		if (atomsSelectPanel!=null) atomsSelectPanel.incHiddenAtoms();
	}
	public void decHiddenAtoms() {
		if (atomsSelectPanel!=null) atomsSelectPanel.decHiddenAtoms();
	}
	public void decHiddenAtoms(int n) {
		if (atomsSelectPanel!=null) atomsSelectPanel.decHiddenAtoms(n);
	}

	public void setCropEnable(boolean b) {
		if (atomsSelectPanel!=null) atomsSelectPanel.setCropEnable(b);
	}
	public void setCutEnable(boolean b) {
		if (atomsSelectPanel!=null) atomsSelectPanel.setCutEnable(b);
	}
	public void setShowAllEnable(boolean b) {
		if (atomsSelectPanel!=null) atomsSelectPanel.setShowAllEnable(b);
	}
	public void setColorChoiceEnable(boolean b) {
		if (atomsSelectPanel!=null) atomsSelectPanel.setColorChoiceEnable(b);
	}
	
	public void selectAtomInTable(Point3d pos) {
		
	}

	public void unSelectAtomInTable(Point3d pos) {
		
	}
}
