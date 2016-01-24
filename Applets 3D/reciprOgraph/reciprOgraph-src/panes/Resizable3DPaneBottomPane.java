/* IntensityCalc - Resizable3DPaneBottomPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 8 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package panes;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import utils.HVPanel;
import engine3D.Univers;


public class Resizable3DPaneBottomPane extends HVPanel.v {
	public Univers univers;
	protected HVPanel bottomPanel;
	
	public Resizable3DPaneBottomPane(HVPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
		univers = new Univers();

		JPanel panel3d = new JPanel();
		panel3d.setLayout(new BorderLayout());
		panel3d.add(univers.getCanvas());
		panel3d.setMinimumSize(new Dimension(0, 50));
		
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel3d, bottomPanel.jPanel);
		sp.setResizeWeight(1);
		sp.setContinuousLayout(true);
		addComp(sp);
	}
}