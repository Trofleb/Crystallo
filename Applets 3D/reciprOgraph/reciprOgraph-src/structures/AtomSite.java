/* IntensityCalc - AtomSite.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 3 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package structures;

import java.util.HashMap;
import java.util.Vector;

import utils.Utils3d;


public class AtomSite {
	public final String atom, symbol, label;
	public final double x, y, z;
	public final double occupancy, oxydation;
	
	public AtomSite(String atom, String symbol, String label, double x, double y, double z, double occupancy, double oxydation) {
		this.atom = atom;
		this.symbol = symbol;
		this.label = label;
		this.x = x;
		this.y = y;
		this.z = z;
		this.occupancy = occupancy;
		this.oxydation = oxydation;
	}
	
	public String toString() {
		return atom+" "+symbol+" "+((oxydation>=0?"+":"")+Utils3d.posToString(oxydation))+" ("+Utils3d.posToString(x)+" "+Utils3d.posToString(y)+" "+Utils3d.posToString(z)+") "+Utils3d.posToString(occupancy);
	}
}
