import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.swing.JPanel;

import com.sun.j3d.utils.universe.SimpleUniverse;

/*
 * Created on 23 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Preview extends JPanel {
	BranchGroup previewEnv;
	SimpleUniverse previewUniverse;
	Group obj;

	public Preview(int x, int y) {
		/*
		 * Panel p=new Panel();
		 * Canvas3D canvas3D = new
		 * Canvas3D(SimpleUniverse.getPreferredConfiguration());
		 * canvas3D.setSize(x, y);
		 * previewEnv = Test3D.createEnvironment();
		 * previewEnv.setCapability(BranchGroup.ALLOW_DETACH);
		 * previewEnv.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		 * previewEnv.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		 * previewUniverse = new SimpleUniverse(canvas3D);
		 * View view = previewUniverse.getViewer().getView();
		 * view.setProjectionPolicy(View.PARALLEL_PROJECTION);
		 * ViewingPlatform viewingPlatform =
		 * previewUniverse.getViewingPlatform();
		 * viewingPlatform.setNominalViewingTransform();
		 * previewUniverse.addBranchGraph(previewEnv);
		 * p.add(canvas3D);
		 * add(p);
		 */
	}

	public void setObject(Group o) {
		this.previewUniverse.getLocale().removeBranchGraph(this.previewEnv);
		if (this.obj != null)
			this.previewEnv.removeChild(this.obj);
		this.previewEnv.addChild(o);
		this.previewUniverse.getLocale().addBranchGraph(this.previewEnv);
		this.obj = o;
	}

}
