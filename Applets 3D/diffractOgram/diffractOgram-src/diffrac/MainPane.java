package diffrac;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import model3d.Model3d;
import projScreen.ProjScreen;
import bottomPanel.BottomPanel;
import bottomPanel.HVPanel;

/* TestApplet - MainPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 nov. 06
 * 
 * nicolas.schoeni@epfl.ch
 */

public class MainPane extends HVPanel.v {
	public Model3d model3d;
	private JSplitPane splitPane;
	private ProjScreen projected;
	private BottomPanel bottomPanel;
	private DefaultValues defaultValues;
	
	public MainPane(DefaultValues defaultValues) {
		this.defaultValues = defaultValues;
		projected = new ProjScreen();
		projected.setMinimumSize(new Dimension(0, 0));
		
		JPanel panel3d = new JPanel();
		panel3d.setMinimumSize(new Dimension(1, 1));
		panel3d.setLayout(new BorderLayout());
		model3d = new Model3d(defaultValues, projected);
		panel3d.add(model3d.univers.getCanvas());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel3d, projected);
		splitPane.setResizeWeight(0.6);
		splitPane.setMinimumSize(new Dimension(0, 0));
		splitPane.setContinuousLayout(true);
		//splitPane.setOneTouchExpandable(true);
		
		bottomPanel = new BottomPanel(defaultValues, model3d);
		
		expand(true);
		addComp(splitPane);
		expand(false);
		addSubPane(bottomPanel);
		
	  model3d.doRays(false);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("horizontal")) {
			changeSplitPane(JSplitPane.HORIZONTAL_SPLIT, 0.3);
		}
		if (e.getActionCommand().equals("vertical")) {
			changeSplitPane(JSplitPane.VERTICAL_SPLIT, 0.72);
		}
	}
	
	private void changeSplitPane(int orientation, double weight) {
		splitPane.setOrientation(orientation);
		splitPane.setResizeWeight(weight);
		splitPane.resetToPreferredSizes();
		projected.setImageDefaultOrigin(orientation==JSplitPane.VERTICAL_SPLIT);
		projected.repaint();
	}
	
	public void stop() {
		if (bottomPanel.help!=null) bottomPanel.help.show(false);
		bottomPanel.animPane.animator.stopAnimation();
	}
	public void destroy() {
		model3d.destroy();
	}
}