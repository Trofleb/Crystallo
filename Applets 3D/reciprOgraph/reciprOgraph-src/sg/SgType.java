/* crystalOgraph2 - SgType.java
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
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import utils.MatVect;


public class SgType implements Serializable {
	protected static SgType[][][][] sgs;

	public final int dim; 
	public final int no; 
	public final int dotNo; 
	public final int variant;
	public final String name;
	public  String fullName;
	public final String[] otherNames;
	public final String suffix;
	public final String variantDesc;
	public final int systemIndex;
	public final int systemVariantIndex;
	public final MatVect[] symPos; 
	public final int laueGroup;
	
	private SgType(int dim, int no, int dotNo, int variant, String name, String fullName, String[] otherNames, String suffix, String variantDesc, int systemIndex, int systemVariantIndex, MatVect[] symPos) {
		this.dim = dim;
		this.no = no;
		this.dotNo = dotNo;
		this.variant = variant;
		this.name = name;
		this.fullName = fullName;
		this.otherNames = otherNames;
		this.suffix = suffix;
		this.variantDesc = variantDesc;
		this.systemIndex = systemIndex;
		this.systemVariantIndex = systemVariantIndex;
		this.symPos = symPos;
		this.laueGroup = dim==3?findLaueGroup(no):-1;
	}

	public static final int LG_TRICLINIC = 1;
	public static final int LG_MONOCLINIC = 2;
	public static final int LG_ORTHORHOMBIC = 3;
	public static final int LG_TETRAGONAL_M = 4;
	public static final int LG_TETRAGONAL_MMM = 5;
	public static final int LG_TRIGONAL = 6;
	public static final int LG_TRIGONAL_M = 7;
	public static final int LG_HEXAGONAL_M = 8;
	public static final int LG_HEXAGONAL_MMM = 9;
	public static final int LG_CUBIC = 10;
	public static final int LG_CUBIC_M = 11;

	
	private static int findLaueGroup(int no) {
		if (no>=1&&no<=2) return LG_TRICLINIC;
		else if (no>=3&&no<=15) return LG_MONOCLINIC;
		else if (no>=16&&no<=74) return LG_ORTHORHOMBIC;
		else if (no>=75&&no<=88) return LG_TETRAGONAL_M;
		else if (no>=89&&no<=142) return LG_TETRAGONAL_MMM;
		else if (no>=143&&no<=148) return LG_TRIGONAL;
		else if (no>=149&&no<=167) return LG_TRIGONAL_M;
		else if (no>=168&&no<=176) return LG_HEXAGONAL_M;
		else if (no>=177&&no<=194) return LG_HEXAGONAL_MMM;
		else if (no>=195&&no<=206) return LG_CUBIC;
		else if (no>=207&&no<=230) return LG_CUBIC_M;
		return -1;
	}
	
	
	
	// dim is 2 or 3, no and dotNo start from 1, variant from 0
	public static SgType getSg(int dim, int no, int dotNo, int variant) {
		return sgs[SgSystem.dim2index(dim)][no-1][dotNo-1][variant];
	}
	public static SgType getSg(int no) {
		return sgs[SgSystem.dim2index(3)][no-1][0][0];
	}
	public static SgType getSg(int no, int variant) {
		return sgs[SgSystem.dim2index(3)][no-1][0][variant];
	}
	public static SgType getSg(String name) {
		if (name.length()<2) return null;
		name = name.replaceAll(" ", "").toLowerCase();
		name = Character.toUpperCase(name.charAt(0))+name.substring(1);
		char c = name.charAt(name.length()-1);
		int v = 0;
		if (c=='s' || c=='z' || c=='h' || c=='r') {
			name = name.substring(0, name.length()-1);
			if (c=='z' || c=='r') v = 1;
		}
		int d = SgSystem.dim2index(3);
		for (int j=0; j<sgs[d].length; j++) {
			if (sgs[d][j][0].length>v) {
				if (sgs[d][j][0][v].name.equals(name)) return sgs[d][j][0][v];
				if (sgs[d][j][0][v].fullName.equals(name)) return sgs[d][j][0][v];
			}
			for (int i=0; i<sgs[d][j][0].length; i++) {
				if (sgs[d][j][0][i].fullName.equals(name)) return sgs[d][j][0][i];
			}
		}
		return null;
	}

	public SgSystem getSystem() {
		return SgSystem.getSystem(dim, systemIndex, systemVariantIndex);
	}
	
	public String toString() {
		//return dim+"D "+no+(dim<=3?"":"."+dotNo)+" ("+variant+") "+name+" "+(fullName.equals(name)?"":fullName)+" "+suffix;
		return no+" ("+name+")";
	}
/*
	static {
		try {
			URL u = SgType.class.getResource("/spacegroups.obj");
			ObjectInputStream in = new ObjectInputStream(u.openStream());
			//ObjectInputStream in = new ObjectInputStream(new FileInputStream("spacegroups.obj"));
			sgs = (SgType[][][][]) in.readObject();
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
*/
	
	public static void staticInit() {
		if (sgs!=null) return;
		try {
			URL u = SgSystem.class.getResource("/spacegroups.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
			
			HashMap[] mm = new HashMap[SgSystem.dimensions.length];
			for (int i=0; i<SgSystem.dimensions.length; i++) {
				mm[i] = new HashMap();
			}
			while (true) {
				String s = in.readLine();
				if (s==null) break;
				String[] ss = s.split("\\|");
				
				int dim = Integer.parseInt(ss[0]);
				int var = Integer.parseInt(ss[2]);
				int no, dotNo; 
				int p = ss[1].indexOf('.');
				if (p==-1) {
					no = Integer.parseInt(ss[1]);
					dotNo = 1;
				}
				else {
					no = Integer.parseInt(ss[1].substring(0, p));
					dotNo = Integer.parseInt(ss[1].substring(p+1));
				}
				int sysi=-1, sysv=-1;
				firstfor: for (int i=0; i<SgSystem.nbSystems(dim); i++) {
					if (SgSystem.getSystem(dim, i, 0).name.equals(ss[7])) {
						if (ss[8].length()==0) {
							sysi = i;
							sysv = 0;
							break;
						}
						else {
							for (int j=0; j<SgSystem.nbVariants(dim, i); j++) {
								if (SgSystem.getSystem(dim, i, j).variant.equals(ss[8])) {
									sysi = i;
									sysv = j;
									break firstfor;
								}
							}
						}
					}
				}
				if (sysi==-1 || sysv==-1) throw new RuntimeException("System not found "+ss[7]+" "+ss[8]);
				MatVect[] pp = MatVect.multiParse(ss[10]); 
				String[] otherNames = ss[5].length()==0?null:ss[5].split(",");
				SgType sg = new SgType(dim, no, dotNo, var, ss[3], ss[4], otherNames, ss[6], ss[9], sysi, sysv, pp);
				
				int i = SgSystem.dim2index(dim);
				HashMap m1, m2;
				if (mm[i].containsKey(""+no)) m1 = (HashMap) mm[i].get(""+no);
				else mm[i].put(""+no, m1=new HashMap());

				if (m1.containsKey(""+dotNo)) m2 = (HashMap) m1.get(""+dotNo);
				else m1.put(""+dotNo, m2=new HashMap());
				
				m2.put(""+var, sg);
			}
			sgs = new SgType[SgSystem.dimensions.length][][][];
			for (int i=0; i<SgSystem.dimensions.length; i++) {	// dimension
				sgs[i] = new SgType[mm[i].size()][][];
				for (int j=0; j<sgs[i].length; j++) {							// sg no
					HashMap m1 = (HashMap) mm[i].get(""+(j+1));
					sgs[i][j] = new SgType[m1.size()][];
					for (int k=0; k<sgs[i][j].length; k++) {					// dot no
						HashMap m2 = (HashMap) m1.get(""+(k+1));
						sgs[i][j][k] = new SgType[m2.size()];
						for (int l=0; l<sgs[i][j][k].length; l++) {				// variants
							sgs[i][j][k][l] = (SgType) m2.get(""+l);
						}
					}
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
	// it depends on systems.obj file, so execute SgSystem's main first
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("spacegroups.txt")));
			
			HashMap[] mm = new HashMap[SgSystem.dimensions.length];
			for (int i=0; i<SgSystem.dimensions.length; i++) {
				mm[i] = new HashMap();
			}
			while (true) {
				String s = in.readLine();
				if (s==null) break;
				String[] ss = s.split("\\|");
				
				int dim = Integer.parseInt(ss[0]);
				int var = Integer.parseInt(ss[2]);
				int no, dotNo; 
				int p = ss[1].indexOf('.');
				if (p==-1) {
					no = Integer.parseInt(ss[1]);
					dotNo = 1;
				}
				else {
					no = Integer.parseInt(ss[1].substring(0, p));
					dotNo = Integer.parseInt(ss[1].substring(p+1));
				}
				int sysi=-1, sysv=-1;
				firstfor: for (int i=0; i<SgSystem.nbSystems(dim); i++) {
					if (SgSystem.getSystem(dim, i, 0).name.equals(ss[7])) {
						if (ss[8].length()==0) {
							sysi = i;
							sysv = 0;
							break;
						}
						else {
							for (int j=0; j<SgSystem.nbVariants(dim, i); j++) {
								if (SgSystem.getSystem(dim, i, j).variant.equals(ss[8])) {
									sysi = i;
									sysv = j;
									break firstfor;
								}
							}
						}
					}
				}
				if (sysi==-1 || sysv==-1) throw new RuntimeException("System not found "+ss[7]+" "+ss[8]);
				MatVect[] pp = MatVect.multiParse(ss[10]); 
				String[] otherNames = ss[5].length()==0?null:ss[5].split(",");
				SgType sg = new SgType(dim, no, dotNo, var, ss[3], ss[4], otherNames, ss[6], ss[9], sysi, sysv, pp);
				
				int i = SgSystem.dim2index(dim);
				HashMap m1, m2;
				if (mm[i].containsKey(""+no)) m1 = (HashMap) mm[i].get(""+no);
				else mm[i].put(""+no, m1=new HashMap());

				if (m1.containsKey(""+dotNo)) m2 = (HashMap) m1.get(""+dotNo);
				else m1.put(""+dotNo, m2=new HashMap());
				
				m2.put(""+var, sg);
			}
			SgType[][][][] ss = new SgType[SgSystem.dimensions.length][][][];
			for (int i=0; i<SgSystem.dimensions.length; i++) {	// dimension
				ss[i] = new SgType[mm[i].size()][][];
				for (int j=0; j<ss[i].length; j++) {							// sg no
					HashMap m1 = (HashMap) mm[i].get(""+(j+1));
					ss[i][j] = new SgType[m1.size()][];
					for (int k=0; k<ss[i][j].length; k++) {					// dot no
						HashMap m2 = (HashMap) m1.get(""+(k+1));
						ss[i][j][k] = new SgType[m2.size()];
						for (int l=0; l<ss[i][j][k].length; l++) {				// variants
							ss[i][j][k][l] = (SgType) m2.get(""+l);
						}
					}
				}
			}
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("spacegroups.obj"));
			out.writeObject(ss);
			out.close();
			for (int i=0; i<ss.length; i++) {
				for (int j=0; j<ss[i].length; j++) {
					for (int k=0; k<ss[i][j].length; k++) {
						for (int l=0; l<ss[i][j][k].length; l++) {
							System.out.println(ss[i][j][k][l]);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

