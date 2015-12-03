import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Pattern;

public class CifFileParser {

	private Loop currentLoop;
	private String currentLabel;
	private Vector currentLoopLine;
	private boolean loopInHeader;
	public Vector cifHeader;
	public HashMap infos;
	public Vector loops;
	
	
	public String getStringField(String key, String defVal) {
		String s = (String)infos.get(key);
		return s==null?defVal:s;
	}
	public double getDoubleField(String key, double defVal) {
		String s = (String)infos.get(key);
		if (s==null) return defVal;
		else return parseCoord(s);
	}
	public int getIntField(String key, int defVal) {
		String s = (String)infos.get(key);
		return s==null?defVal:Integer.parseInt(s);
	}
	
	public static double parseCoord(String s) {
		int p = s.indexOf('(');
		if (p!=-1) s = s.substring(0, p);
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}	
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("-- Header --\n");
		for (int i=0; i<cifHeader.size(); i++) sb.append(cifHeader.get(i)+"\n");

		sb.append("-- Infos --\n");
		Iterator iterator = infos.entrySet().iterator();
		for (java.util.Map.Entry e; iterator.hasNext();) {
			e = (java.util.Map.Entry)iterator.next();
			sb.append(e.getKey()+" = "+e.getValue()+"\n");
		}
		
		for (int i=0; i<loops.size(); i++) {
			sb.append("-- Block --\n");
			Loop l = ((Loop)loops.get(i));
			Vector v = l.header;
			for (int j=0; j<v.size(); j++) sb.append(v.get(j)+" ");
			sb.append("\n");
			for (int j=0; j<l.lines.size(); j++) {
				Vector u = (Vector) l.lines.get(j);
				for (int k=0; k<u.size(); k++) sb.append(u.get(k)+" ");
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private String[] data;
	private int line;
	
	public CifFileParser(String[] data) {
		this.data = data;
		currentLoop = null;
		currentLabel = null;
		cifHeader = new Vector(2, 3);
		infos = new HashMap();
		loops = new Vector(5, 5);
		currentLoopLine = null;
		
		for (line=0; line<data.length; line++) {
			String s = data[line].trim();
			if (s.length()==0 || s.charAt(0)=='#') continue;
			
			String[] ss = s.split("'");
			for (int j=0; j<ss.length; j++) {
				if (j%2==0) {
					String[] sss = ss[j].trim().split("\\s+");
					for (int k=0; k<sss.length; k++) {
						if (sss[k].trim().length()==0) continue;
						token(sss[k]);
					}
				}
				else {
					token(ss[j]);
				}
			}
			newLine();
		}
		endOfFile();
	}
	
	private void token(String s) {
		if (currentLoop!=null) {
			if (loopInHeader) {
				if (s.charAt(0)=='_') {
					currentLoop.header.add(s);
				}
				else {
					loopInHeader = false;
					currentLoopLine = new Vector(5, 5);
					currentLoopLine.add(s);
				}
			}
			else {
				if (s.charAt(0)=='_' || s.equalsIgnoreCase("loop_")) {
					// end loop
					loops.add(currentLoop);
					currentLoop = null;
					currentLoopLine = null;
					token(s);
				}
				else {
					// new entry in current loop line
					currentLoopLine.add(s);
				}
			}
		}
		else if (currentLabel!=null) {
			if (s.equalsIgnoreCase("loop_") || s.charAt(0)=='_') {
				infos.put(currentLabel, null);
				currentLabel = null;
				token(s);
			}
			else {
				infos.put(currentLabel, s);
				currentLabel = null;
			}
		}
		else {
			if (s.equalsIgnoreCase("loop_")) {
				currentLoop = new Loop();
				loopInHeader = true;
			}
			else if (s.charAt(0)=='_') {
				currentLabel = s;
			}
			else {
				cifHeader.add(s);
			}
		}
	}
	
	public void newLine() {
		if (currentLoopLine!=null) {
			currentLoop.lines.add(currentLoopLine);
			currentLoopLine = new Vector(5, 5);
		}
	}

	public void endOfFile() {
		if (currentLoop!=null) {
			loops.add(currentLoop);
		}
	}
	
	public static class Loop {
		public Vector header = new Vector(10, 10);
		public Vector lines = new Vector(10, 10);
	}
	
	
	public static String ArrayToString(String[] ss) {
		String s = "";
		for (int i=0; i<ss.length; i++) s+=((i>0?" ":"")+ss[i]);
		return s;
	}
	
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:\\test.cif")));
			Vector v = new Vector(100, 100);
			for (String s; (s=in.readLine())!=null; v.add(s));
			CifFileParser cif = new CifFileParser((String[])v.toArray(new String[0]));
			
			
			System.out.println(cif);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

