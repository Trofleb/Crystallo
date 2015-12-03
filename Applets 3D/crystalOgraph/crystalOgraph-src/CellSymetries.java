import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.net.URL;
import java.util.Vector;

import javax.swing.JApplet;
import javax.vecmath.Matrix3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

public class CellSymetries {

	 public static class CustomCell {
		 public Matrix3f[] eqm;
		 public Vector3f[] eqv, bravais;
	 }
	
	
	public static boolean hasChoice(int n) {
		return howManyChoices(n)>1;
	}

	public static int howManyChoices(int n) {
		for (int i=0; i<doubleChoicesNo.length; i++)
			for (int j=0; j<doubleChoicesNo[i].length; j++)
				if (n==doubleChoicesNo[i][j])
					return 2;
		for (int i=0; i<sixChoicesNo.length; i++)
				if (n==sixChoicesNo[i])
					return 6;
		return 1;
	}
	
	public static String getChoice(int n, int c) {
		for (int i=0; i<sixChoicesNo.length; i++)
			if (n==sixChoicesNo[i])
				return sixChoicesDescr[c];
		for (int i=0; i<doubleChoicesNo.length; i++)
			for (int j=0; j<doubleChoicesNo[i].length; j++)
				if (n==doubleChoicesNo[i][j])
					return doubleChoicesDescr[i][c];
		return "";
	}
	
	public static int getIndex(int n, int c) {
  	if (c==0 || !hasChoice(n)) return n-1;
  	int nbc = howManyChoices(n);
  	if ((nbc==2&&c==1)||(nbc==6&&c==3)) {
    	for (int i=0; i<doubleChoicesOrdered.length; i++)
  			if (n==doubleChoicesOrdered[i]) return 230+i;
  	}
  	if (nbc==6) {
    	for (int i=0; i<sixChoicesNo.length; i++)
  			if (n==sixChoicesNo[i]) return 230+doubleChoicesOrdered.length+i*4+((c%3)-1)+(c/3)*2;
  	}
		return -1;
	}	

	
	public static int getSGno(String s) {
		String ss = s.toUpperCase().replaceAll(" [SZRH]", "");
		if ("SZRH".indexOf(ss.charAt(ss.length()-1))!=-1) ss = ss.substring(0, ss.length()-1);
		for (int i=0; i<categoriesName.length; i++) {
			for (int j=0; j<cellTypes[i].length; j++) {
				if (ss.equalsIgnoreCase(cellTypes[i][j])) {
					return j+categoriesBaseNo[i];
				}
			}
		}
		// Sometime sg symbols are not very unified about where to place the spaces
		// If not found, trying to compare strings after trimming spaces
		ss = ss.replaceAll(" ", "");
		for (int i=0; i<categoriesName.length; i++) {
			for (int j=0; j<cellTypes[i].length; j++) {
				if (ss.equalsIgnoreCase(cellTypes[i][j].replaceAll(" ", ""))) {
					return j+categoriesBaseNo[i];
				}
			}
		}
		// Try with double axis choices
		for (int i=0; i<doubleAxesNames.length; i++) {
			for (int j=0; j<doubleAxesNames[i].length; j++) {
				if (ss.equalsIgnoreCase(doubleAxesNames[i][j].replaceAll(" ", ""))) {
					return doubleAxesNo[i];
				}
			}
		}
		// Try with six choices space groups
		for (int i=0; i<sixChoicesNames.length; i++) {
			for (int j=0; j<sixChoicesNames[i].length; j++) {
				if (ss.equalsIgnoreCase(sixChoicesNames[i][j].replaceAll(" ", ""))) {
					return sixChoicesNo[i];
				}
			}
		}
		// Sometimes the - is placed after
		int m = ss.indexOf('-');
		if (m!=-1 && m>1) {
			char c = ss.charAt(m-1);
			ss = ss.replaceAll(c+"-", "-"+c);
			for (int i=0; i<categoriesName.length; i++) {
				for (int j=0; j<cellTypes[i].length; j++) {
					if (ss.equalsIgnoreCase(cellTypes[i][j].replaceAll(" ", ""))) {
						return j+categoriesBaseNo[i];
					}
				}
			}
		}
		return 0;
	}
		
	
	public static boolean isMonoclinic(int n) {
		return n>2 && n<16;
	}

	public static boolean canBeRhombohedral(int n) {
		for (int i=0; i<doubleChoicesNo[2].length; i++)
			if (n==doubleChoicesNo[2][i]) return true;
		return false;
	}
	
	public static boolean hasTwoOriginChoices(int n) {
		for (int i=0; i<doubleChoicesNo[1].length; i++)
			if (n==doubleChoicesNo[1][i]) return true;
		return false;
	}

	public static boolean isRhombohedral(int n, int c) {
		if (c==0) return false;
		return canBeRhombohedral(n);
	}

	public static int parseChoice(String sgName) {
		int no = getSGno(sgName);
		int nbc = howManyChoices(no);
		
		
		//System.out.println(sgName+" "+no+" "+hasTwoOriginChoices(no)+" "+sgName.toUpperCase().charAt(sgName.length()-1));
		
		
		if (nbc == 2) {
			if (canBeRhombohedral(no)) {
				if (sgName.toUpperCase().charAt(sgName.length()-1)=='H') return 0;
				else return 1;
			}
			if (hasTwoOriginChoices(no)) {
				if (sgName.toUpperCase().charAt(sgName.length()-1)=='Z') return 1;
				else return 0;
			}
			if (isMonoclinic(no)){
				String[] ss = sgName.split(" ");
				if (ss.length==4 && ss[1].equals("1") && ss[2].equals("1")) return 1;
				else return 0;
			}
			return 0;
		} else if (nbc == 6) {
			String ss = sgName.replaceAll(" ", "");
			for (int i=0; i<sixChoicesNames.length; i++) {
				for (int j=0; j<sixChoicesNames[i].length; j++) {
					if (ss.equalsIgnoreCase(sixChoicesNames[i][j].replaceAll(" ", ""))) {
						return j;
					}
				}
			}
			return 0;
		}
		else return 0;
	}
	
	public static String buildSGname(int no, int choice) {
		for (int i=0; i<sixChoicesNo.length; i++) {
			if (no == sixChoicesNo[i]) return sixChoicesNames[i][choice];
		}
		for (int i=0; i<doubleAxesNo.length; i++) {
			if (no == doubleAxesNo[i]) return doubleAxesNames[i][choice];
		}
		String name = buildSGname(no);
		if (canBeRhombohedral(no)) {
			name += choice==1?" R":" H";
		}
		if (hasTwoOriginChoices(no)) {
			name += choice==1?" Z":" S";
		}
		return name;
	}
	
	public static String buildSGname(int no) {
		for (int i=0; i<categoriesBaseNo.length; i++) {
			if (no>=categoriesBaseNo[i]&&no<categoriesBaseNo[i+1]) {
				return cellTypes[i][no-categoriesBaseNo[i]];
			}
		}
		return null;
	}
	
	public static int[] getConstraints(int no, int c) {
		switch (getCategory(no)) {
			case 0:
				return new int[] {1, 1, 1, 1, 1, 1};
			case 1:
				if (c>=howManyChoices(no)/2)
					return new int[] {1, 1, 1, 90, 90, 1};
				else
					return new int[] {1, 1, 1, 90, 1, 90};
			case 2:
				return new int[] {1, 1, 1, 90, 90, 90};
			case 3:
				return new int[] {1, 0, 1, 90, 90, 90};
			case 4:
				if (isRhombohedral(no, c))
					return new int[] {1, 0, 0, 1, 0, 0};
				else
					return new int[] {1, 0, 1, 90, 90, 120};
			case 5:
				return new int[] {1, 0, 1, 90, 90, 120};
			case 6:
				return new int[] {1, 0, 0, 90, 90, 90};
			default:
				return new int[] {1, 1, 1, 1, 1, 1};
		}
	}
	
	public static int getCategory(int no) {
		for (int i=0; i<cellTypes.length; i++) {
			no -= cellTypes[i].length;
			if (no<=0) return i;
		}
		return -1;
	}

	public static String[] getEquations(int no, int c) {
		Vector v = new Vector(8, 16);
		String name = CellGen.methods[CellSymetries.getIndex(no, c)].getName();
		URL u = CellSymetries.class.getResource("/CellGen.java");
    if (u!=null)
  		try {
  	    DataInputStream is = new DataInputStream(u.openStream());
  	    while (true) {
  		    String line = is.readLine();
  	    	if (line==null) break;
  	    	if (line.indexOf(name+"(")!=-1) {
  			    while (true) {
  		    		line = is.readLine();
  				    if (line.indexOf("};}")!=-1) break;
  				    String s = line.substring(line.indexOf('{')+1, line.indexOf('}')).replaceAll("f", "").replaceAll(",", ", "); 
  				    v.add(s);
  			    }
  			    if (line.indexOf("};}")!=-1) break;
  	    	}
  	    }
  		} catch (Exception e) {}
		return (String[]) v.toArray(new String[0]);
	}
	

	 
	 
	 private final static Vector3f ooo = new Vector3f(0f, 0f, 0f);
	 private final static Vector3f fff = new Vector3f(1/2f, 1/2f, 1/2f);
	 private final static Vector3f ffo = new Vector3f(1/2f, 1/2f, 0f);
	 private final static Vector3f fof = new Vector3f(1/2f, 0f, 1/2f);
	 private final static Vector3f off = new Vector3f(0f, 1/2f, 1/2f);
	 private final static Vector3f r1 = new Vector3f(2/3f, 1/3f, 1/3f);
	 private final static Vector3f r2 = new Vector3f(1/3f, 2/3f, 2/3f);
	 
	 private static Vector3f[] createBravaisVectors(String name) {
	 	if (name==null || name.length()==0) return new Vector3f[] {ooo}; 
	 	switch (name.toUpperCase().charAt(0)) {
	 		case 'P': return new Vector3f[] {ooo};
	 		case 'I': return new Vector3f[] {ooo, fff};
	 		case 'F': return new Vector3f[] {ooo, ffo, fof, off};
	 		case 'A': return new Vector3f[] {ooo, off};
	 		case 'B': return new Vector3f[] {ooo, fof};
	 		case 'C': return new Vector3f[] {ooo, ffo};
	 		case 'R': return new Vector3f[] {ooo, r1, r2};
	 		default : return new Vector3f[] {ooo};
	 	}
	 }
	 private static void createCustomEqMV(String[] eq, CustomCell ccell) throws ParseException {
		ccell.eqm = new Matrix3f[eq.length];
		ccell.eqv = new Vector3f[eq.length];
	 	
	 	for (int i=0; i<eq.length; i++) {
	 		ccell.eqm[i] = new Matrix3f();
	 		ccell.eqv[i] = new Vector3f();
	 		String ss[] = eq[i].split(",");
	 		if (ss.length!=3) throw new ParseException("Bad number of coordinates ("+eq[i]+")");
	 		for (int j=0; j<3; j++) {
	 			String[] ff = splitEq(ss[j].trim());
	 			for (int k=0; k<ff.length; k++) {
 					float val;
 					ff[k] = ff[k].replaceAll(" ", "");
 					switch(ff[k].charAt(0)) {
 						case '+': val=1; break;
 						case '-': val=-1; break;
 						default : throw new ParseException("\""+ss[j]+"\"");
 					}
	 				if (ff[k].length()==2) {
	 					switch(ff[k].toLowerCase().charAt(1)) {
	 						case 'x': ccell.eqm[i].setElement(j, 0, val); break;
	 						case 'y': ccell.eqm[i].setElement(j, 1, val); break;
	 						case 'z': ccell.eqm[i].setElement(j, 2, val); break;
	 						default : throw new ParseException("\""+ss[j]+"\"");
	 					}
	 				}
	 				else {
	 					try {
	 						val *= parseFrac(ff[k].substring(1));
	 						if (j==0) ccell.eqv[i].x = val;
	 						else if (j==1) ccell.eqv[i].y = val;
	 						else ccell.eqv[i].z = val;
	 					} catch (Exception e) {throw new ParseException("\""+ss[j]+"\"");}
	 				}
	 			}
	 		}
	 	}
	 }	 
	 
	 public static class ParseException extends Exception {
	 	ParseException(String s) {super(s);}
	 	ParseException() {super();}
	 }
	 
	 public static CustomCell setCustomSG(String[] eq, String name) throws ParseException {
		 CustomCell ccell = new CustomCell();
		 ccell.bravais = createBravaisVectors(name);
		 createCustomEqMV(eq, ccell);
		 return ccell;
	 }
	 
	 public static float[][] posCustom(CustomCell ccell, float x, float y, float z) {
	 	float[][] r = new float[ccell.eqm.length*ccell.bravais.length][3];
	 	Vector3f v1, v2, input = new Vector3f(x, y, z);
	 	for (int i=0; i<ccell.eqm.length; i++) {
		 	v1 = new Vector3f(input);
		 	ccell.eqm[i].transform(v1);
		 	v1.add(ccell.eqv[i]);
		 	for (int j=0; j<ccell.bravais.length; j++) {
			 	v2 = new Vector3f(v1);
			 	v2.add(ccell.bravais[j]);
			 	int t = j*ccell.eqm.length+i;
			 	r[t][0] = v2.x;
			 	r[t][1] = v2.y;
			 	r[t][2] = v2.z;
		 	}
	 	}
		return r;
	 }
	
	 private static float parseFrac(String s) {
	 	int k = s.indexOf('/');
	 	if (k!=-1) return parseFrac(s.substring(0, k))/parseFrac(s.substring(k+1)); 
	 	else return Float.parseFloat(s);
	 }
	 
	 private static String[] splitEq(String s) {
	 	Vector v = new Vector(3, 3);
	 	if ("+-".indexOf(s.charAt(0))==-1) s = "+"+s;
	 	for (int last=0, i=0; i<=s.length(); i++) {
	 		if (i==s.length() || ("+-".indexOf(s.charAt(i))!=-1 && i>0)) {
	 			v.add(s.substring(last, i));
	 			last=i;
	 		}
	 	}
		return (String[]) v.toArray(new String[0]);
	 }
	 
		public static void main(String[] a) {

//			System.out.println("*"+CifViewer.getCol("  1    \t   'x-y, z' x-y +z", 0)+"*");
			
/*			
			bravais = createBravaisVectors("I");
			
			for (int i=0; i<bravais.length; i++)
				System.out.println(bravais[i]);
			
			String[] eq = {
			"x, y , z",
			"-y, x-y,z+2/3",
			"-x, -y,z+1/2",
			"y,x ,-z+2/3",
			};
			try {
				createCustomEqMV(eq);
				for (int i=0; i<eq.length; i++) {
					System.out.println(eqm[i]);
					System.out.println(eqv[i]);
				}
					
				float[][] ff = posCustom(10, 20, 30);
				for (int i=0; i<ff.length; i++) {
					for (int j=0; j<ff[i].length; j++) {
						System.out.println("i:"+i+" j:"+j+" "+ff[i][j]);
					}
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
*/			
			
			
			
//			String ss[] = splitEq("y-z+1/2+z");
//			for (int i=0; i<ss.length; i++)
//				System.out.println(ss[i]);
			
			
//			System.out.println(parseFrac("24/5"));
//			System.out.println(new Vector3f().x);
		}
	
		public final static String[] categoriesName = {
			"Triclinic",
			"Monoclinic",
			"Orthorhombic",
			"Tetragonal",
			"Trigonal",
			"Hexagonal",
			"Cubic"
		};
		
		public final static int[] categoriesBaseNo = {
			1, 3, 16, 75, 143, 168, 195, 231
		};
			
		public final static int[][] doubleChoicesNo = {
			{3, 4, 6, 10, 11},
			{48, 50, 59, 68, 70, 85, 86, 88, 
				125, 126, 129, 130, 133, 134, 137, 138, 141, 142,
				201, 203, 222, 224, 227, 228},
			{146, 148, 155, 160, 161, 166, 167},
		};		
		
		public final static int[] doubleChoicesOrdered = {
			3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			48, 50, 59, 68, 70, 85, 86, 88, 
			125, 126, 129, 130, 133, 134, 137, 138, 141, 142,
			146, 148, 155, 160, 161, 166, 167,
			201, 203, 222, 224, 227, 228,
		};

		public final static String[][] doubleChoicesDescr = {
			{"Unique axis b", "Unique axis c"},
			{"Origin choice 1", "Origin choice 2"},
			{"Hexagonal", "Rhombohedral"}
		};
		
		public final static int[] sixChoicesNo = {
				5, 7, 8, 9, 12, 13, 14, 15
		};
		
		public final static String[] sixChoicesDescr = {
				"Axis b, Choice 1", "Axis b, Choice 2", "Axis b, Choice 3", "Axis c, Choice 1", "Axis c, Choice 2", "Axis c, Choice 3" 
		};

		public final static String[][] sixChoicesNames = {
				{"C 1 2 1", "A 1 2 1", "I 1 2 1", "A 1 1 2", "B 1 1 2", "I 1 1 2"},
				{"P 1 c 1", "P 1 n 1", "P 1 a 1", "P 1 1 a", "P 1 1 n", "P 1 1 b"},
				{"C 1 m 1", "A 1 m 1", "I 1 m 1", "A 1 1 m", "B 1 1 m", "I 1 1 m"},
				{"C 1 c 1", "A 1 n 1", "I 1 a 1", "A 1 1 a", "B 1 1 n", "I 1 1 b"},
				{"C 1 2/m 1", "A 1 2/m 1", "I 1 2/m 1", "A 1 1 2/m", "B 1 1 2/m", "I 1 1 2/m"},
				{"P 1 2/c 1", "P 1 2/n 1", "P 1 2/a 1", "P 1 1 2/a", "P 1 1 2/n", "P 1 1 2/b"},
				{"P 1 21/c 1", "P 1 21/n 1", "P 1 21/a 1", "P 1 1 21/a", "P 1 1 21/n", "P 1 1 21/b"},
				{"C 1 2/c 1", "A 1 2/n 1", "I 1 2/a 1", "A 1 1 2/a", "B 1 1 2/n", "I 1 1 2/b"},
		};
		
		public final static int[] doubleAxesNo = {
				3, 4, 6, 10, 11
		};

		public final static String[][] doubleAxesNames = {
				{"P 1 2 1", "P 1 1 2"}, 	
				{"P 1 21 1", "P 1 1 21"}, 	
				{"P 1 m 1", "P 1 1 m"}, 	
				{"P 1 2/m 1", "P 1 1 2/m"}, 	
				{"P 1 21/m 1", "P 1 1 21/m"}, 	
		};

		public final static String[] Triclinic = {
			"P 1",
			"P -1"
		};
		public final static String[] Monoclinic = {
			"P 2",
			"P 21",
			"C 2",
			"P m",
			"P c",
			"C m",
			"C c",
			"P 2/m",
			"P 21/m",
			"C 2/m",
			"P 2/c",
			"P 21/c",
			"C 2/c",
		};
		public final static String[] Orthorhombic = {
			"P 2 2 2",
			"P 2 2 21",
			"P 21 21 2",
			"P 21 21 21",
			"C 2 2 21",
			"C 2 2 2",
			"F 2 2 2",
			"I 2 2 2",
			"I 21 21 21",
			"P m m 2",
			"P m c 21",
			"P c c 2",
			"P m a 2",
			"P c a 21",
			"P n c 2",
			"P m n 21",
			"P b a 2",
			"P n a 21",
			"P n n 2",
			"C m m 2",
			"C m c 21",
			"C c c 2",
			"A m m 2",
			"A b m 2",
			"A m a 2",
			"A b a 2",
			"F m m 2",
			"F d d 2",
			"I m m 2",
			"I b a 2",
			"I m a 2",
			"P m m m",
			"P n n n",
			"P c c m",
			"P b a n",
			"P m m a",
			"P n n a",
			"P m n a",
			"P c c a",
			"P b a m",
			"P c c n",
			"P b c m",
			"P n n m",
			"P m m n",
			"P b c n",
			"P b c a",
			"P n m a",
			"C m c m",
			"C m c a",
			"C m m m",
			"C c c m",
			"C m m a",
			"C c c a",
			"F m m m",
			"F d d d",
			"I m m m",
			"I b a m",
			"I b c a",
			"I m m a"
		};
		public final static String[] Tetragonal = {
			"P 4",
			"P 41",
			"P 42",
			"P 43",
			"I 4",
			"I 41",
			"P -4",
			"I -4",
			"P 4/m",
			"P 42/m",
			"P 4/n",
			"P 42/n",
			"I 4/m",
			"I 41/a",
			"P 4 2 2",
			"P 4 21 2",
			"P 41 2 2",
			"P 41 21 2",
			"P 42 2 2",
			"P 42 21 2",
			"P 43 2 2",
			"P 43 21 2",
			"I 4 2 2",
			"I 41 2 2",
			"P 4 m m",
			"P 4 b m",
			"P 42 c m",
			"P 42 n m",
			"P 4 c c",
			"P 4 n c",
			"P 42 m c",
			"P 42 b c",
			"I 4 m m",
			"I 4 c m",
			"I 41 m d",
			"I 41 c d",
			"P -4 2 m",
			"P -4 2 c",
			"P -4 21 m",
			"P -4 21 c",
			"P -4 m 2",
			"P -4 c 2",
			"P -4 b 2",
			"P -4 n 2",
			"I -4 m 2",
			"I -4 c 2",
			"I -4 2 m",
			"I -4 2 d",
			"P 4/m m m",
			"P 4/m c c",
			"P 4/n b m",
			"P 4/n n c",
			"P 4/m b m",
			"P 4/m n c",
			"P 4/n m m",
			"P 4/n c c",
			"P 42/m m c",
			"P 42/m c m",
			"P 42/n b c",
			"P 42/n n m",
			"P 42/m b c",
			"P 42/m n m",
			"P 42/n m c",
			"P 42/n c m",
			"I 4/m m m",
			"I 4/m c m",
			"I 41/a m d",
			"I 41/a c d",
		};
		public final static String[] Trigonal = {
			"P 3",
			"P 31",
			"P 32",
			"R 3",
			"P -3",
			"R -3",
			"P 3 1 2",
			"P 3 2 1",
			"P 31 1 2",
			"P 31 2 1",
			"P 32 1 2",
			"P 32 2 1",
			"R 3 2",
			"P 3 m 1",
			"P 3 1 m",
			"P 3 c 1",
			"P 3 1 c",
			"R 3 m",
			"R 3 c",
			"P -3 1 m",
			"P -3 1 c",
			"P -3 m 1",
			"P -3 c 1",
			"R -3 m",
			"R -3 c",
		};
		public final static String[] Hexagonal = {
			"P 6",
			"P 61",
			"P 65",
			"P 62",
			"P 64",
			"P 63",
			"P -6",
			"P 6/m",
			"P 63/m",
			"P 6 2 2",
			"P 61 2 2",
			"P 65 2 2",
			"P 62 2 2",
			"P 64 2 2",
			"P 63 2 2",
			"P 6 m m",
			"P 6 c c",
			"P 63 c m",
			"P 63 m c",
			"P -6 m 2",
			"P -6 c 2",
			"P -6 2 m",
			"P -6 2 c",
			"P 6/m m m",
			"P 6/m c c",
			"P 63/m c m",
			"P 63/m m c",
		};
		public final static String[] Cubic = {
			"P 2 3",
			"F 2 3",
			"I 2 3",
			"P 21 3",
			"I 21 3",
			"P m -3",
			"P n -3",
			"F m -3",
			"F d -3",
			"I m -3",
			"P a -3",
			"I a -3",
			"P 4 3 2",
			"P 42 3 2",
			"F 4 3 2",
			"F 41 3 2",
			"I 4 3 2",
			"P 43 3 2",
			"P 41 3 2",
			"I 41 3 2",
			"P -4 3 m",
			"F -4 3 m",
			"I -4 3 m",
			"P -4 3 n",
			"F -4 3 c",
			"I -4 3 d",
			"P m -3 m",
			"P n -3 n",
			"P m -3 n",
			"P n -3 m",
			"F m -3 m",
			"F m -3 c",
			"F d -3 m",
			"F d -3 c",
			"I m -3 m",
			"I a -3 d",
		};
		
		public final static String[][] cellTypes = {
			Triclinic,
			Monoclinic,
			Orthorhombic,
			Tetragonal,
			Trigonal,
			Hexagonal,
			Cubic
		};

}
