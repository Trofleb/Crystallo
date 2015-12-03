/* Symmetry - Inversion.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 24 févr. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package transformations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;

import objects.Polyedre;

import u3d.TranspObject;
import utils.HVPanel;


public class Inversion extends Transformation {
	private boolean animating = false;
	private JToggleButton button;
	
	public Inversion(HVPanel parentPanel, Polyedre polyedre) {
		super(polyedre);
		button = new JToggleButton("Inversion");
		parentPanel.addButton(button);
		parentPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Inversion")) {
					anim();
				}
			}
		});
	}

	public void anim() {
		if (animating) return;
		animating = true;
		new Thread() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						button.setEnabled(false);
					}
				});
				int n = 30;
				Transform3D t3dBakup = new Transform3D();
				polyedre.tgInv.getTransform(t3dBakup);
				Transform3D t3d = new Transform3D();
				for (int i=n-1; i>=-n; i--) {
					if (i==0) continue;
					t3d.set(((double)i)/n);
					t3d.mul(t3dBakup, t3d);
					polyedre.tgInv.setTransform(t3d);
					if (i==n-1||i==-n) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								polyedre.onTransformChanged();
							}
						});
					}
					try {sleep(30);} catch (Exception e) {}
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						button.setSelected(button.isSelected());
						button.setEnabled(true);
					}
				});
				animating = false;
			}
		}.start();
	}
	
	public void reset() {
		super.reset();
		button.setSelected(false);
	}
}
