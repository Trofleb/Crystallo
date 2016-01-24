/* crystalOgraph2 - ExpandPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 juin 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package panes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import utils.HVPanel;
import utils.HVPanel.FloatEditField;


public class ExpandPane implements ActionListener {
	public HVPanel.v panel;
	private ExpandChangedListener listener;
	private FloatEditField x, y, z;
	
	public ExpandPane() {
		panel = new HVPanel.v();
		panel.putExtraSpace();
		panel.expand(false);
		panel.bottom();

		HVPanel.v pe = new HVPanel.v();
		pe.setBorder(new TitledBorder("Cell expansion"));
		x = new HVPanel.FloatSpinnerEditField(" x", null, 2, 1f, "0.0", 1).to(pe);
		y = new HVPanel.FloatSpinnerEditField(" y", null, 2, 1f, "0.0", 1).to(pe);
		z = new HVPanel.FloatSpinnerEditField(" z", null, 2, 1f, "0.0", 1).to(pe);
		
		x.setMinimum(0);
		y.setMinimum(0);
		z.setMinimum(0);
		
		panel.addSubPane(pe);
	}

	public void actionPerformed(ActionEvent e) {
		if (listener!=null) {
			listener.expandChanged(x.getFloatValue(), y.getFloatValue(), z.getFloatValue());
		}
	}

	public void setExpandListener(ExpandChangedListener listener) {
		this.listener = listener;
	}
	
	public static interface ExpandChangedListener {
		public void expandChanged(double x, double y, double z);
	}
}
