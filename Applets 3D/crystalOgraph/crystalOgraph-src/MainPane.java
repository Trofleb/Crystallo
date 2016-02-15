import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;

/*
 * TestApplet - MainPane.java
 * Author : Nicolas Schoeni
 * Creation : 23 nov. 06
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
		// ToolTipManager.sharedInstance().setInitialDelay(1);
		// ToolTipManager.sharedInstance().setReshowDelay(1);

		this.elementsCounter = new Helper();

		// create the model
		this.model = new Model(this.elementsCounter);

		this.univers = new Univers(this.model);
		this.univers.addChild(this.model);

		// create the grid
		this.grid = new Grid(this.model.cell, 1, 1, 1);
		this.univers.addChild(this.grid);

		this.elementsCounter.model = this.model;

		this.rightPanel = new RightPanel(applet, this.univers, this.model, this.grid, this.elementsCounter);
		this.bottomPanel = new BottomPanel(applet, this.univers, this.grid, this.model, this.rightPanel,
				this.elementsCounter);

		this.setLayout(new BorderLayout());
		JPanel pc = new JPanel();
		pc.setLayout(new BorderLayout());
		pc.add(this.univers.getCanvas());
		pc.setMinimumSize(new Dimension(1, 1));

		JSplitPane pd = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pc, this.bottomPanel);
		pd.setResizeWeight(1);
		pd.setMinimumSize(new Dimension(0, 0));
		pd.setContinuousLayout(true);

		this.add("Center", pd);
		this.add("East", this.rightPanel);

		this.univers.reset();
		this.bottomPanel.addAtomToTable("", "", "0", "0", "0", "", Color.red);

		this.univers.show();
	}

	public void stop() {
		if (this.bottomPanel.cifViewer != null)
			this.bottomPanel.cifViewer.show(false);
		if (this.bottomPanel.icsd != null)
			this.bottomPanel.icsd.show(false);
		if (this.bottomPanel.help3d.frame != null)
			this.bottomPanel.help3d.frame.dispose();
	}

	public void destroy() {
		if (this.univers != null)
			this.univers.cleanup();
	}

	@Override
	public void openFile(File f) {
		try {
			this.bottomPanel.loadCifFile(new FileInputStream(f));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.applet.frame, "File not recognized as a CIF file.");
			throw new RuntimeException(e);
		}
	}
}
