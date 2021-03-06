/* IntensityCalc - Resizable3DPaneBottomPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 8 ao�t 2005
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


public class ProjPaneBottomPane extends HVPanel.v {
	private JSplitPane splitPane;
	protected JPanel panel2D;
	protected HVPanel bottomPanel;

	
	public ProjPaneBottomPane() {
	}
	public ProjPaneBottomPane(JPanel panel2D, HVPanel bottomPanel) {
		setPanes(panel2D, bottomPanel);
	}
	public void setPanes(JPanel panel2D, HVPanel bottomPanel) {
		this.panel2D = panel2D;
		this.bottomPanel = bottomPanel;
		panel2D.setMinimumSize(new Dimension(0, 0));
		
		expand(true);
		addComp(panel2D);
		expand(false);
		addSubPane(bottomPanel);
	}
}