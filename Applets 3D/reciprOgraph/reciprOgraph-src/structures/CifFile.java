/* reciprOgraph - CifFile.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 10 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package structures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import printf.Format;
import printf.Parameters;

import sg.Lattice;
import sg.SgType;
import sg.SpaceGroup;
import utils.MatVect;
import utils.Utils3d;


public class CifFile {
	public String[] data;
	private CifFileParser parser;
	
	public CifFile(File f) {
		try {
			parser = new CifFileParser(new FileInputStream(f));
			data = parser.data;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public CifFile(InputStream in) {
		parser = new CifFileParser(in);
		data = parser.data;
	}
	public CifFile(String[] file) {
		data = file;
		parser = new CifFileParser(file);
	}
	
	public String getFormula() {
		String s = parser.getStringField("_chemical_formula_structural", "");
		if (s.trim().length()==0)
			s = parser.getStringField("_chemical_formula_sum", "");
		return s;
	}
	
	
	public SpaceGroup getSg() {
		String sgSymbol = parser.getStringField("_symmetry_space_group_name_H-M", "");
		int sgNo = parser.getIntField("_symmetry_Int_Tables_number", 0);

		System.out.println("no:"+sgNo+" symbol:'"+sgSymbol+"'");

		double a = parser.getDoubleField("_cell_length_a", 5);
		double b = parser.getDoubleField("_cell_length_b", 5);
		double c = parser.getDoubleField("_cell_length_c", 5);
		double alpha = parser.getDoubleField("_cell_angle_alpha", 90);
		double beta = parser.getDoubleField("_cell_angle_beta", 90);
		double gamma = parser.getDoubleField("_cell_angle_gamma", 90);

		Lattice lattice = new Lattice(a, b, c, alpha, beta, gamma);
		if (sgSymbol.length()==0) {
			if (sgNo==0) throw new RuntimeException("Invalid spacegoup : "+sgNo+" '"+sgSymbol+"'");
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
				
				AtomSite a = new AtomSite(atom, symbol, label, x, y, z, occ, oxid);
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
		v.add("_cell_length_a                  "+Math.round(sg.cell.a*10000d)/10000d);
		v.add("_cell_length_b                  "+Math.round(sg.cell.b*10000d)/10000d);
		v.add("_cell_length_c                  "+Math.round(sg.cell.c*10000d)/10000d);
		v.add("_cell_angle_alpha               "+Math.round(sg.cell.alpha*100d)/100d);
		v.add("_cell_angle_beta                "+Math.round(sg.cell.beta*100d)/100d);
		v.add("_cell_angle_gamma               "+Math.round(sg.cell.gamma*100d)/100d);
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
}
