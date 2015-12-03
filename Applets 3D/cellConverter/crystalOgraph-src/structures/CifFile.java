/* reciprOgraph - CifFile.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 10 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package structures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Vector;

import printf.Format;
import printf.Parameters;
import sg.Lattice;
import sg.SgType;
import sg.SpaceGroup;
import utils.MatVect;


public class CifFile {
	private CifFileParser parser;
	private Vector extraAtoms=new Vector(30, 30);
	private Vector extraAtomsLine=new Vector(30, 30);
	
	public CifFile(File f) {
		try {
			parser = new CifFileParser(new FileReader(f));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public CifFile(Reader in) {
		parser = new CifFileParser(in);
	}
	public CifFile(String[] file) {
		parser = new CifFileParser(file);
	}
	
	public String getFormula() {
		String s = parser.getStringField("_chemical_formula_structural", "");
		if (s.trim().length()==0)
			s = parser.getStringField("_chemical_formula_sum", "");
		return s;
	}
	public int getUnitZ() {
		return parser.getIntField("_cell_formula_units_Z", 1);
	}
	
	public String[] getData() {
		Vector v = new Vector(100, 100);
		v.addAll(parser.data);
		for (int i=0; i<extraAtoms.size(); i++) {
			int l = ((Integer)extraAtomsLine.get(i)).intValue();
			v.add(l+1, extraAtoms.get(i));
			//v.add(extraAtoms.get(i));
		}
		return (String[]) v.toArray(new String[0]);
	}
	
	public String toString() {
		String[] ss = getData();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<ss.length; i++) {
			sb.append(ss[i]+"\n");
		}
		return sb.toString();
	}
	
	public SpaceGroup getSg() {
		String sgSymbol = parser.getStringField("_symmetry_space_group_name_H-M", "");
		int sgNo = parser.getIntField("_symmetry_Int_Tables_number", 0);

		//System.out.println("no:"+sgNo+" symbol:'"+sgSymbol+"'");

		double a = parser.getDoubleField("_cell_length_a", 5);
		double b = parser.getDoubleField("_cell_length_b", 5);
		double c = parser.getDoubleField("_cell_length_c", 5);
		double alpha = parser.getDoubleField("_cell_angle_alpha", 90);
		double beta = parser.getDoubleField("_cell_angle_beta", 90);
		double gamma = parser.getDoubleField("_cell_angle_gamma", 90);
		
		Lattice lattice = new Lattice(a, b, c, alpha, beta, gamma);
		if (sgSymbol.length()==0) {
			if (sgNo==0) {
				CifFileParser.Loop loop = parser.getLoopContainingHeader("_symmetry_equiv_pos_as_xyz");
				int indexXyz = loop==null?-1:loop.header.indexOf("_symmetry_equiv_pos_as_xyz");
				if (indexXyz==-1) {
					//throw new RuntimeException("No space group information found");
					System.out.println("No space group information found. Using P1.");
					return new SpaceGroup(SgType.getSg(1), lattice); 
				}
				MatVect[] mm = new MatVect[loop.lines.size()];
				for (int i=0; i<loop.lines.size(); i++) {
					//double x1 = CifFileParser.parseCoord((String)lin.get(indexXin));
					mm[i] = new MatVect((String)((Vector)loop.lines.get(i)).get(indexXyz));
				}
				SgType type = new SgType(3, 0, 0, 0, "", "", null, "", "", 0, 0, mm);
				return new SpaceGroup(type, lattice); 
				//throw new RuntimeException("Invalid spacegoup : "+sgNo+" '"+sgSymbol+"'");
			}
			return new SpaceGroup(SgType.getSg(sgNo), lattice); 
		}
		else {
			SgType sg = SgType.getSg(sgSymbol);
			if (sg==null) {
				if (sgNo==0) throw new RuntimeException("Invalid spacegoup : "+sgNo+" '"+sgSymbol+"'");
				else {
					System.err.println("Spacegroup symbol not found. Using default setting for "+sgNo);
					sg = SgType.getSg(sgNo);
				}				
			}
			return new SpaceGroup(sg, lattice); 
		}
	}
	
	public void updateLattice(Lattice cell, int uzFactor) {
		if (parser.isFieldPresent("_cell_formula_units_Z")) {
			parser.setIntField("_cell_formula_units_Z", uzFactor);
		}
		if (parser.isFieldPresent("_cell_volume")) {
			parser.setDoubleField("_cell_volume", cell.volume());
		}
		parser.setDoubleField("_cell_length_a", cell.a);
		parser.setDoubleField("_cell_length_b", cell.b);
		parser.setDoubleField("_cell_length_c", cell.c);
		parser.setDoubleField("_cell_angle_alpha", cell.alpha);
		parser.setDoubleField("_cell_angle_beta", cell.beta);
		parser.setDoubleField("_cell_angle_gamma", cell.gamma);
	}
	
	public void setSgP1() {
		if (parser.isFieldPresent("_symmetry_Int_Tables_number")) {
			parser.setIntField("_symmetry_Int_Tables_number", 1);
		}
		if (parser.isFieldPresent("_symmetry_space_group_name_H-M")) {
			parser.setStringField("_symmetry_space_group_name_H-M", "'P 1'");
		}
		CifFileParser.Loop loopEq = parser.getLoopContainingHeader("_symmetry_equiv_pos_as_xyz");
		if (loopEq!=null) {
			int indexXyz = loopEq.header.indexOf("_symmetry_equiv_pos_as_xyz");
			int indexId = loopEq.header.indexOf("_symmetry_equiv_pos_site_id");
			for (int i=0; i<loopEq.lines.size(); i++) {
				int dl = ((Integer)loopEq.linesToDataLine.get(i)).intValue();
				if (i==0) {
					if (indexId==-1) {
						parser.data.set(dl, "  'x, y, z'");
					}
					else {
						parser.data.set(dl, "  1      'x, y, z'");
					}
				}
				else parser.data.set(dl, "");
			}
		}
	}
	
	public void updateAtoms(AtomSite[] atoms) {
		CifFileParser.Loop loopPos = parser.getLoopContainingHeader("_atom_site_fract_x");
		int indexSymb = loopPos.header.indexOf("_atom_site_type_symbol");
		int indexX = loopPos.header.indexOf("_atom_site_fract_x");
		int indexY = loopPos.header.indexOf("_atom_site_fract_y");
		int indexZ = loopPos.header.indexOf("_atom_site_fract_z");
		int indexOcc = loopPos.header.indexOf("_atom_site_occupancy");
		int indexLabel = loopPos.header.indexOf("_atom_site_label");
		int indexMult = loopPos.header.indexOf("_atom_site_symmetry_multiplicity");
		int indexWyck = loopPos.header.indexOf("_atom_site_Wyckoff_symbol");
		int indexIsoB = loopPos.header.indexOf("_atom_site_B_iso_or_equiv");
		int last = 0;
		
		//TODO utiliser printf !!!!
		extraAtoms.clear();
		extraAtomsLine.clear();
		
		for (int i=0; i<atoms.length; i++) {
			StringBuffer sb = new StringBuffer();
			for (int j=0; j<loopPos.header.size(); j++) {
				if (j==indexLabel) sb.append(atoms[i].label+"    ");
				else if (j==indexSymb) sb.append(atoms[i].atom+"    ");
				else if (j==indexX) sb.append(round(atoms[i].x)+"    ");
				else if (j==indexY) sb.append(round(atoms[i].y)+"    ");
				else if (j==indexZ) sb.append(round(atoms[i].z)+"    ");
				else if (j==indexOcc) sb.append(round(atoms[i].occupancy)+"    ");
				else if (j==indexMult) sb.append(atoms[i].multiplicity+"    ");
				else if (j==indexWyck) sb.append(atoms[i].wyckoff+"    ");
				else if (j==indexIsoB) sb.append(round(atoms[i].isoB)+"    ");
			}
			if (i<loopPos.lines.size()) {
				Vector lout = (Vector) loopPos.lines.get(i);
				int dl = ((Integer)loopPos.linesToDataLine.get(i)).intValue();
				parser.data.set(dl, sb.toString());
				last = Math.max(last, dl);
			}
			else {
				extraAtoms.add(sb.toString());
				extraAtomsLine.add(new Integer(last++));
			}
		}
	}
	
	
	
	
	
//	public void transformAtoms(CifFile cifIn, Matrix3d M, Vector3d v, Vector translations) {
//		// (x',y',z') = M(x,y,z) + v
//		CifFileParser.Loop loopIn = cifIn.parser.getLoopContainingHeader("_atom_site_fract_x");
//		int indexXin = loopIn.header.indexOf("_atom_site_fract_x");
//		int indexYin = loopIn.header.indexOf("_atom_site_fract_y");
//		int indexZin = loopIn.header.indexOf("_atom_site_fract_z");
//		int indexLin = loopIn.header.indexOf("_atom_site_label");
//
//		CifFileParser.Loop loopOut = parser.getLoopContainingHeader("_atom_site_fract_x");
//		int indexXout = loopOut.header.indexOf("_atom_site_fract_x");
//		int indexYout = loopOut.header.indexOf("_atom_site_fract_y");
//		int indexZout = loopOut.header.indexOf("_atom_site_fract_z");
//		int indexLout = loopOut.header.indexOf("_atom_site_label");
//		
//		extraAtoms.clear();
//		extraAtomsLine.clear();
//		
//		for (int i=0; i<loopIn.lines.size(); i++) {
//			Vector lin = (Vector) loopIn.lines.get(i);
//			Vector lout = (Vector) loopOut.lines.get(i);
//			int dl = ((Integer)loopOut.linesToDataLine.get(i)).intValue();
//			try {
//				double x1 = CifFileParser.parseCoord((String)lin.get(indexXin));
//				double y1 = CifFileParser.parseCoord((String)lin.get(indexYin));
//				double z1 = CifFileParser.parseCoord((String)lin.get(indexZin));
//
//			boolean first=true;
//			Vector3d[] vv = cifIn.getSg().getSymPos(new Vector3d(x1, y1, z1));
//			for (int l=0, l2=0; l<vv.length; l++) {
//				if (vv[l]==null) continue;
//				double x2 = M.m00*vv[l].x+M.m01*vv[l].y+M.m02*vv[l].z+v.x;
//				double y2 = M.m10*vv[l].x+M.m11*vv[l].y+M.m12*vv[l].z+v.y;
//				double z2 = M.m20*vv[l].x+M.m21*vv[l].y+M.m22*vv[l].z+v.z;
//				Vector3d p = new Vector3d(x2, y2, z2);
//				
//				for (int k=0; k<=translations.size(); k++) {	// first one is 'no translation'
//					StringBuffer sb = new StringBuffer();
//					Vector3d q = new Vector3d(p);
//					if (k>0) {
//						Vector3d t = (Vector3d)translations.get(k-1);
//						q.add(t);
//					}
//					if (q.x>=1) q.x = q.x%1;
//					if (q.y>=1) q.y = q.y%1;
//					if (q.z>=1) q.z = q.z%1;
//					if (q.x>.9999) q.x = 0;
//					if (q.y>.9999) q.y = 0;
//					if (q.z>.9999) q.z = 0;
//					String label = indexLin==-1?"":((String)lin.get(indexLin));
//					
//					for (int j=0; j<lout.size(); j++) {
//						if (j==indexXout) sb.append(round(q.x)+"    ");
//						else if (j==indexYout) sb.append(round(q.y)+"    ");
//						else if (j==indexZout) sb.append(round(q.z)+"    ");
//						else if (!first&&j==indexLout) sb.append(label+(k==0?"":("t"+k))+(l2==0?"":("s"+l2))+"    ");
//						else sb.append(lout.get(j)+"    ");
//					}
//					if (first) {
//						first=false;
//						parser.data.set(dl, sb.toString());
//					}
//					else {
//						extraAtoms.add(sb.toString());
//						extraAtomsLine.add(new Integer(dl+1));
//					}
//				}
//				l2++;
//			}
//			} catch (Exception e) {
//				e.printStackTrace(System.err);
//				extraAtoms.add("# !!Bad coords!! "+parser.data.get(dl));
//				System.err.println("bad atom : "+parser.data.get(dl));
//			}
//		
//		
//		
////		Vector3d v = new Vector3d(atomsIn[i].x, atomsIn[i].y, atomsIn[i].z);
////		Vector3d[] vv = sgIn.getSymPos(v);
////		for (int l=0; l<vv.length; l++) {
////			if (vv[l]==null) continue;
//			
//			
//			
//		}
//	}
	
	
	public AtomSite[] getAtoms() {
		Vector v = new Vector(100, 100);
		
		CifFileParser.Loop loopCoord=null;
		HashMap oxidations=null;
		
		for (int i=0; i<parser.loops.size(); i++) {
			CifFileParser.Loop loop = (CifFileParser.Loop) parser.loops.get(i);
			if (loop.header.contains("_atom_site_fract_x")) {
				loopCoord = loop;
			}
			if (loop.header.contains("_atom_type_oxidation_number")) {
				oxidations = new HashMap();
				for (int j=0; j<loop.lines.size(); j++) {
					Vector l = (Vector) loop.lines.get(j);
					oxidations.put(l.get(0), l.get(1));
				}
			}
		}
		
		int indexSymb = loopCoord.header.indexOf("_atom_site_type_symbol");
		int indexX = loopCoord.header.indexOf("_atom_site_fract_x");
		int indexY = loopCoord.header.indexOf("_atom_site_fract_y");
		int indexZ = loopCoord.header.indexOf("_atom_site_fract_z");
		int indexOcc = loopCoord.header.indexOf("_atom_site_occupancy");
		int indexLabel = loopCoord.header.indexOf("_atom_site_label");
		int indexMult = loopCoord.header.indexOf("_atom_site_symmetry_multiplicity");
		int indexWyck = loopCoord.header.indexOf("_atom_site_Wyckoff_symbol");
		int indexIsoB = loopCoord.header.indexOf("_atom_site_B_iso_or_equiv");
		
		for (int i=0; i<loopCoord.lines.size(); i++) {
			Vector l = (Vector) loopCoord.lines.get(i);

			try {
				double x = CifFileParser.parseCoord((String)l.get(indexX));
				double y = CifFileParser.parseCoord((String)l.get(indexY));
				double z = CifFileParser.parseCoord((String)l.get(indexZ));
				
				String label="";
				if (indexLabel!=-1) label = (String) l.get(indexLabel);
				
				String atom = "O";
				if (indexSymb!=-1) atom = (String) l.get(indexSymb);
				
				String symbol = "O";
				if (indexSymb!=-1) {
					symbol = getSymbol(atom);
				}
				else if (indexLabel!=-1) {
					symbol = getSymbol(label);
				}

				double occ = 1;
				if (indexOcc!=-1) occ=CifFileParser.parseCoord((String)l.get(indexOcc));
				
				double oxid = 0;
				if (oxidations==null) {
					if (indexSymb!=-1) {
						oxid = getOxidation((String)l.get(indexLabel));
					}
				}
				else {
					String s = (String)oxidations.get(atom);
					if (s!=null) oxid = CifFileParser.parseCoord(s);
				}
				
				double isoB = 0; 
				if (indexIsoB!=-1) isoB=CifFileParser.parseCoord((String)l.get(indexIsoB));

				int mult = 1; 
				if (indexMult!=-1) mult=Integer.parseInt((String)l.get(indexMult));
				
				char wyck = 'a';
				if (indexWyck!=-1) wyck=((String)l.get(indexWyck)).charAt(0);
				
				AtomSite a = new AtomSite(atom, symbol, label, x, y, z, occ, oxid, mult, wyck, isoB);
				v.add(a);
			} catch (RuntimeException e) {
				System.err.println("bad atom : "+l);
			}
		}
		return (AtomSite[]) v.toArray(new AtomSite[0]);
	}
	
	private double getOxidation(String s) {
		int sign = 1;
		s = s.replaceAll("[a-zA-Z\\ \\+]", "");
		if (s.length()==0) return 0;
		if (s.indexOf("-")!=-1) {
			sign=-1;
			s = s.replaceAll("\\-", "");
		}
		try {
			return Double.parseDouble(s)*sign;
		} catch (Exception e) {
			return 0;
		}
	}
	private String getSymbol(String s) {
		return s.replaceAll("[^a-zA-Z]", "");
	}
	
	public static String[] generateCif(SpaceGroup sg, AtomSite[] atoms) {
		Vector v = new Vector(10, 10);
		v.add("data_cif");
		v.add("_cell_length_a                  "+round(sg.cell.a));
		v.add("_cell_length_b                  "+round(sg.cell.b));
		v.add("_cell_length_c                  "+round(sg.cell.c));
		v.add("_cell_angle_alpha               "+round(sg.cell.alpha));
		v.add("_cell_angle_beta                "+round(sg.cell.beta));
		v.add("_cell_angle_gamma               "+round(sg.cell.gamma));
		v.add("_symmetry_space_group_name_H-M '"+sg.sg.fullName+sg.sg.suffix+"'");
		v.add("_symmetry_Int_Tables_number    '"+sg.sg.no+"'");
		v.add("");
		MatVect[] eq = sg.sg.symPos;
		if (eq!=null && eq.length>0) {
			v.add("loop_");
			v.add("_symmetry_equiv_pos_site_id");
			v.add("_symmetry_equiv_pos_as_xyz");
			for (int i=0; i<eq.length; i++) v.add("  "+(i+1)+"     '"+eq[i].toEq()+"'");
			v.add("");
		}
		v.add("loop_");
		v.add("_atom_site_label");
		v.add("_atom_site_type_symbol");
		v.add("_atom_site_fract_x");
		v.add("_atom_site_fract_y");
		v.add("_atom_site_fract_z");
		v.add("_atom_site_occupancy");
		for (int i=0; i<atoms.length; i++) {
			Parameters p = new Parameters();
			p.add(atoms[i].label);
			p.add(atoms[i].symbol);
			p.add(atoms[i].x);
			p.add(atoms[i].y);
			p.add(atoms[i].z);
			p.add(atoms[i].occupancy);
			v.add(Format.sprintf("%-8s %-8s %8.5f %8.5f %8.5f %4.2f", p));
		}
		return (String[]) v.toArray(new String[0]);
	}

	public static double round(double d) {
		return Math.round(10000*d)/10000d;
	}
}
