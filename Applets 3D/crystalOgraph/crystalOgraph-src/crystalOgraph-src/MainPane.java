import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;

/* TestApplet - MainPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 nov. 06
 * 
 * nicolas.schoeni@epfl.ch
 */

public class MainPane extends JPanel implements CifFileDropper.CifFileOpener {
	private Grid grid;
	private Model model;
	public Univers univers;
	public Helper elementsCounter;
	public BottomPanel bottomPanel;
	public RightPanel rightPanel;
	public crystalOgraph applet;

	public MainPane(crystalOgraph applet) {
		this.applet = applet;
	  Shaker.frame = applet.frame;
	  
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setDismissDelay(20000);
		//ToolTipManager.sharedInstance().setInitialDelay(1);
		//ToolTipManager.sharedInstance().setReshowDelay(1);
		
		elementsCounter = new Helper();
		
		// create the model
		model = new Model(elementsCounter);
		
		univers = new Univers(model);
		univers.addChild(model);

		// create the grid
		grid = new Grid(model.cell, 1, 1, 1);
		univers.addChild(grid);

		elementsCounter.model=model;
	
		rightPanel = new RightPanel(applet, univers, model, grid, elementsCounter);
		bottomPanel = new BottomPanel(applet, univers, grid, model, rightPanel, elementsCounter);
		
		setLayout(new BorderLayout());
		JPanel pc = new JPanel();
		pc.setLayout(new BorderLayout());
		pc.add(univers.getCanvas());
		pc.setMinimumSize(new Dimension(1, 1));

	  JSplitPane pd = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pc, bottomPanel);
		pd.setResizeWeight(1);
		pd.setMinimumSize(new Dimension(0, 0));
		pd.setContinuousLayout(true);
	  
	  add("Center", pd);
		add("East", rightPanel);
		
		univers.reset();
		bottomPanel.addAtomToTable("", "", "0", "0", "0", "", Color.red);
		
		univers.show();
	}
	
	public void stop() {
		if (bottomPanel.cifViewer!=null)
			bottomPanel.cifViewer.show(false);
		if (bottomPanel.icsd!=null)
			bottomPanel.icsd.show(false);
		if (bottomPanel.help3d.frame!=null)
			bottomPanel.help3d.frame.dispose();
	}
	
	public void destroy() {
		if (univers!=null) univers.cleanup();
	}

	public void openFile(File f) {
		try {
			bottomPanel.loadCifFile(new FileInputStream(f));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(applet.frame, "File not recognized as a CIF file.");
			throw new RuntimeException(e);
		}
	}
}
