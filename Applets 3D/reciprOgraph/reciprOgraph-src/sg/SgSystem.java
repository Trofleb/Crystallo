/* crystalOgraph2 - SgSystem.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 mai 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package sg;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;


public class SgSystem implements Serializable {
	static SgSystem[][][] systems;
	static int[] dimensions;
	
	public final int dim; 
	public final String name; 
	public final String variant;
	public final boolean bFree, cFree;
	public final int alpha, beta, gamma;
	public static final int FREE = -1;
	public static final int LIKE_ALPHA = -2;
	
	public Lattice getDefaultConstants() {
		int aa = (alpha==FREE?90:alpha);
		int bb = (beta==FREE?90:(beta==LIKE_ALPHA?aa:beta));
		int gg = (gamma==FREE?90:(gamma==LIKE_ALPHA?aa:gamma));
		return new Lattice(5, 5, 5, aa, bb, gg);
	}
	
	public String toString() {
		return 
			dim+"D "+
			name+" "+
			(variant.length()>0?(variant+" "):"")+
			"a"+(bFree?"<>":"=")+"b"+(cFree?"<>":"=")+"c, "+
			"alpha"+(beta==LIKE_ALPHA?"=beta":"")+(gamma==LIKE_ALPHA?"=gamma":"")+(alpha==FREE?" free":"="+alpha)+(beta==LIKE_ALPHA?"":(", beta"+(beta==FREE?" free":"="+beta)))+(gamma==LIKE_ALPHA?"":(", gamma"+(gamma==FREE?" free":"="+gamma)));
	}
	
	public static int nbDimensions() {
		return systems.length;
	}
	public static int nbSystems(int dim) {
		return systems[dim2index(dim)].length;
	}
	public static int nbVariants(int dim, int sys) {
		return systems[dim2index(dim)][sys].length;
	}
	
	public static SgSystem getSystem(int dim, int index, int variant) {
		return systems[dim2index(dim)][index][variant];
	}
	
	static int dim2index(int dim) {
		for (int i=0; i<dimensions.length; i++) {
			if (dimensions[i]==dim) return i;
		}
		throw new RuntimeException("Dimension not found");
	}
	
	private SgSystem(int dim, String name, String variant, boolean bFree, boolean cFree, int alpha, int beta, int gamma) {
		this.dim = dim;
		this.name = name;
		this.variant = variant;
		this.bFree = bFree;
		this.cFree = cFree;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
	}
	
/*
	static {
		try {
			URL u = SgSystem.class.getResource("/systems.obj");
			ObjectInputStream in = new ObjectInputStream(u.openStream());
			//ObjectInputStream in = new ObjectInputStream(new FileInputStream("systems.obj"));
			systems = (SgSystem[][][]) in.readObject();
			dimensions = (int[]) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
*/
	
	public static void static_init() {
		if (systems!=null) return;
		try {
			URL u = SgSystem.class.getResource("/systems.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			String[] dd = in.readLine().split(" ");
			dimensions = new int[dd.length];
			
			HashMap[] mm = new HashMap[dimensions.length];
			Vector[] vv = new Vector[dimensions.length];
			for (int i=0; i<dimensions.length; i++) {
				mm[i] = new HashMap();
				vv[i] = new Vector();
				dimensions[i] = Integer.parseInt(dd[i]);
			}
			while (true) {
				String s = in.readLine();
				if (s==null) break;
				String[] ss = s.split("\\|");
				
				for (int i=0; i<dd.length; i++) {
					if (dd[i].equals(ss[0])) {
						SgSystem sys = new SgSystem(Integer.parseInt(ss[0]), ss[1], ss[2], Boolean.valueOf(ss[3]).booleanValue(), Boolean.valueOf(ss[4]).booleanValue(), Integer.parseInt(ss[5]), Integer.parseInt(ss[6]), Integer.parseInt(ss[7]));
						
						if (mm[i].containsKey(sys.name)) {
							((Vector)mm[i].get(sys.name)).add(sys);
						}
						else {
							Vector v = new Vector(20, 20);
							v.add(sys);
							mm[i].put(sys.name, v);
							vv[i].add(v);
						}
					}
				}
			}
			systems = new SgSystem[dimensions.length][][];
			for (int i=0; i<dimensions.length; i++) {
				systems[i] = new SgSystem[vv[i].size()][];
				for (int j=0; j<systems[i].length; j++) {
					systems[i][j] = (SgSystem[]) ((Vector)vv[i].get(j)).toArray(new SgSystem[0]);
				}
			}
		} catch (NumberFormatException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	// the main needs to be executed when you change the text file
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("systems.txt")));
			String[] dd = in.readLine().split(" ");
			int[] dims = new int[dd.length];
			
			
			
			HashMap[] mm = new HashMap[dims.length];
			Vector[] vv = new Vector[dims.length];
			for (int i=0; i<dims.length; i++) {
				mm[i] = new HashMap();
				vv[i] = new Vector();
				dims[i] = Integer.parseInt(dd[i]);
			}
			while (true) {
				String s = in.readLine();
				if (s==null) break;
				String[] ss = s.split("\\|");
				
				for (int i=0; i<dd.length; i++) {
					if (dd[i].equals(ss[0])) {
						SgSystem sys = new SgSystem(Integer.parseInt(ss[0]), ss[1], ss[2], Boolean.valueOf(ss[3]).booleanValue(), Boolean.valueOf(ss[4]).booleanValue(), Integer.parseInt(ss[5]), Integer.parseInt(ss[6]), Integer.parseInt(ss[7]));
						
						if (mm[i].containsKey(sys.name)) {
							((Vector)mm[i].get(sys.name)).add(sys);
						}
						else {
							Vector v = new Vector(20, 20);
							v.add(sys);
							mm[i].put(sys.name, v);
							vv[i].add(v);
						}
					}
				}
			}
			SgSystem[][][] ss = new SgSystem[dims.length][][];
			for (int i=0; i<dims.length; i++) {
				ss[i] = new SgSystem[vv[i].size()][];
				for (int j=0; j<ss[i].length; j++) {
					ss[i][j] = (SgSystem[]) ((Vector)vv[i].get(j)).toArray(new SgSystem[0]);
				}
			}
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("systems.obj"));
			out.writeObject(ss);
			out.writeObject(dims);
			out.close();

		
			for (int i=0; i<ss.length; i++) {
				for (int j=0; j<ss[i].length; j++) {
					for (int k=0; k<ss[i][j].length; k++) {
						System.out.println((ss[i][j].length>1?" ":"")+ss[i][j][k]);
					}
				}
				System.out.println();
			}
		
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
