package structures;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class CifFileParser {
	private Loop currentLoop;
	private String currentLabel;
	private Vector currentLoopLine;
	private boolean loopInHeader;
	public Vector cifHeader;
	public HashMap infos;
	public HashMap infosToDataLine;
	public Vector loops;
	public Vector data;
	private int currentLine;
	
	
	public boolean isFieldPresent(String key) {
		return infos.get(key) != null;
	}
	
	public void setDoubleField(String key, double d) {
		String s = (String)infos.get(key);
		if (s==null) throw new RuntimeException("Field "+key+" not found");
		String newVal = ""+Math.round(d*100000)/100000d;
		infos.put(key, newVal);
		int l = ((Integer)infosToDataLine.get(key)).intValue();
		String t = (String)data.get(l);
		int k = getValueLocation(t);
		data.set(l, t.substring(0, k)+newVal); 
	}
	public void setIntField(String key, int d) {
		String s = (String)infos.get(key);
		if (s==null) throw new RuntimeException("Field "+key+" not found");
		String newVal = ""+d;
		infos.put(key, newVal);
		int l = ((Integer)infosToDataLine.get(key)).intValue();
		String t = (String)data.get(l);
		int k = getValueLocation(t);
		data.set(l, t.substring(0, k)+newVal); 
	}
	public void setStringField(String key, String val) {
		String s = (String)infos.get(key);
		if (s==null) throw new RuntimeException("Field "+key+" not found");
		String newVal = ""+val;
		infos.put(key, newVal);
		int l = ((Integer)infosToDataLine.get(key)).intValue();
		String t = (String)data.get(l);
		int k = getValueLocation(t);
		data.set(l, t.substring(0, k)+newVal); 
	}

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
		return Double.parseDouble(s);	
	}
	
	private int getValueLocation(String s) {
		int i = s.indexOf(' ')+1;
		if (i==-1) return -1;
		for (; true; i++) {
			if (i>=s.length()) return -1;
			if (s.charAt(i)!=' ') return i;
		}
	}
	
	public Loop getLoopContainingHeader(String header) {
		for (int i=0; i<loops.size(); i++) {
			CifFileParser.Loop loop = (CifFileParser.Loop) loops.get(i);
			if (loop.header.contains(header)) return loop;
		}		
		return null;
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
	
	public CifFileParser(Reader in) {
		try {
			BufferedReader bufIn = new BufferedReader(in);
			currentLoop = null;
			currentLabel = null;
			cifHeader = new Vector(2, 3);
			infos = new HashMap();
			infosToDataLine = new HashMap();
			loops = new Vector(5, 5);
			currentLoopLine = null;
			data = new Vector(100, 100);
			
			for (currentLine=0; true; currentLine++) {
				String s = bufIn.readLine();
				if (s==null) break;
				data.add(s);
				s = s.trim();
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public CifFileParser(String[] file) {
		data = new Vector(100, 100);
		for (int i=0; i<file.length; i++) data.add(file[i]);
		currentLoop = null;
		currentLabel = null;
		cifHeader = new Vector(2, 3);
		infos = new HashMap();
		infosToDataLine = new HashMap();
		loops = new Vector(5, 5);
		currentLoopLine = null;
		
		for (currentLine=0; currentLine<file.length; currentLine++) {
			String s = file[currentLine];
			if (s==null) break;
			s = s.trim();
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
				infosToDataLine.put(currentLabel, null);
				currentLabel = null;
				token(s);
			}
			else {
				infos.put(currentLabel, s);
				infosToDataLine.put(currentLabel, new Integer(currentLine));
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
	
	private void newLine() {
		if (currentLoopLine!=null) {
			currentLoop.lines.add(currentLoopLine);
			currentLoop.linesToDataLine.add(new Integer(currentLine));
			currentLoopLine = new Vector(5, 5);
		}
	}

	private void endOfFile() {
		if (currentLoop!=null) {
			loops.add(currentLoop);
		}
	}
	
	public static class Loop {
		public Vector header = new Vector(10, 10);
		public Vector lines = new Vector(20, 20);
		public Vector linesToDataLine = new Vector(20, 20);
	}
	
	
	public static void main(String[] args) {
		try {
			CifFileParser cif = new CifFileParser(new FileReader("c:\\test.cif"));
			System.out.println(cif);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}

