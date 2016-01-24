/* IntensityCalc - AtomSite.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 3 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package structures;

import utils.Utils3d;


public class AtomSite {
	public String label;
	public double x, y, z;
	public final String atom, symbol;
	public final double occupancy, oxydation;
	public int multiplicity;
	public final char wyckoff;
	public final double isoB;
	
	public AtomSite(String atom, String symbol, String label, double x, double y, double z, double occupancy, double oxydation, int multiplicity, char wyckoff, double isoB) {
		this.atom = atom;
		this.symbol = symbol;
		this.label = label;
		this.x = x;
		this.y = y;
		this.z = z;
		this.occupancy = occupancy;
		this.oxydation = oxydation;
		this.multiplicity = multiplicity;
		this.wyckoff = wyckoff;
		this.isoB = isoB;
	}
	
	public AtomSite(AtomSite a) {
		this.atom = a.atom;
		this.symbol = a.symbol;
		this.label = a.label;
		this.x = a.x;
		this.y = a.y;
		this.z = a.z;
		this.occupancy = a.occupancy;
		this.oxydation = a.oxydation;
		this.multiplicity = a.multiplicity;
		this.wyckoff = a.wyckoff;
		this.isoB = a.isoB;
	}

	public String toString() {
		return atom+" "+symbol+" "+((oxydation>=0?"+":"")+Utils3d.posToString(oxydation))+" ("+Utils3d.posToString(x)+" "+Utils3d.posToString(y)+" "+Utils3d.posToString(z)+") "+Utils3d.posToString(occupancy);
	}
}
