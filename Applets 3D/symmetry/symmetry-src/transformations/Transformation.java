/* Symmetry - Transformation.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 24 févr. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package transformations;

import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import objects.Polyedre;

import u3d.TranspObject;


public class Transformation {
	protected Polyedre polyedre;
	
	public Transformation (Polyedre polyedre) {
		this.polyedre = polyedre;
	}
	public void reset() {
		polyedre.onTransformChanged();
	}

	public void addTransform(Transform3D t3d) {
		Transform3D t = new Transform3D();
		polyedre.tgRot.getTransform(t);
		t.mul(t3d);
		polyedre.tgRot.setTransform(t);
		polyedre.onTransformChanged();
	}
}


