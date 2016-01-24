/* IntensityCalc - TriplePane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 8 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package panes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.media.j3d.Canvas3D;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import engine3D.Univers;

import utils.HVPanel;


public class TriplePane extends HVPanel.v {
	private JSplitPane splitPane;
	protected JPanel panel2D;
	protected HVPanel bottomPanel;
	public Univers univers;
	private JPanel panel3D;
	private Canvas3D canvas;
	
	public TriplePane() {
	}
	public TriplePane(JPanel panel2D, HVPanel bottomPanel) {
		setPanes(panel2D, bottomPanel);
	}
	
	public void setVisible(boolean v){
		super.setVisible(v);
		if (canvas!=null) canvas.setVisible(v);
	}

	public void setPanes(JPanel panel2D, HVPanel bottomPanel) {
		this.panel2D = panel2D;
		this.bottomPanel = bottomPanel;
		panel2D.setMinimumSize(new Dimension(1, 1));
		
		univers = new Univers();
		canvas = univers.getCanvas();
		panel3D = new JPanel() {
			public void setVisible(boolean v) {
		    super.setVisible(v);
		    canvas.setVisible(v);
			}			
		};
		panel3D.setMinimumSize(new Dimension(1, 1));
		panel3D.setLayout(new BorderLayout());
		panel3D.add(canvas);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel3D, panel2D);
		splitPane.setResizeWeight(0.5);
		splitPane.setContinuousLayout(true);
		
		expand(true);
		addComp(splitPane);
		expand(false);
		addSubPane(bottomPanel);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand()==null) return;
		if (e.getActionCommand().equals("horizontal")) {
			changeSplitPane(JSplitPane.HORIZONTAL_SPLIT, 0.6);
		}
		if (e.getActionCommand().equals("vertical")) {
			changeSplitPane(JSplitPane.VERTICAL_SPLIT, 0.72);
		}
	}
	private void changeSplitPane(int orientation, double weight) {
		splitPane.setOrientation(orientation);
		splitPane.setResizeWeight(weight);
		splitPane.resetToPreferredSizes();
		panel2D.repaint();
	}
}

