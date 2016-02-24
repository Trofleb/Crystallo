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
		return this.infos.get(key) != null;
	}

	public void setDoubleField(String key, double d) {
		String s = (String) this.infos.get(key);
		if (s == null)
			throw new RuntimeException("Field " + key + " not found");
		String newVal = "" + Math.round(d * 100000) / 100000d;
		this.infos.put(key, newVal);
		int l = ((Integer) this.infosToDataLine.get(key)).intValue();
		String t = (String) this.data.get(l);
		int k = this.getValueLocation(t);
		this.data.set(l, t.substring(0, k) + newVal);
	}

	public void setIntField(String key, int d) {
		String s = (String) this.infos.get(key);
		if (s == null)
			throw new RuntimeException("Field " + key + " not found");
		String newVal = "" + d;
		this.infos.put(key, newVal);
		int l = ((Integer) this.infosToDataLine.get(key)).intValue();
		String t = (String) this.data.get(l);
		int k = this.getValueLocation(t);
		this.data.set(l, t.substring(0, k) + newVal);
	}

	public void setStringField(String key, String val) {
		String s = (String) this.infos.get(key);
		if (s == null)
			throw new RuntimeException("Field " + key + " not found");
		String newVal = "" + val;
		this.infos.put(key, newVal);
		int l = ((Integer) this.infosToDataLine.get(key)).intValue();
		String t = (String) this.data.get(l);
		int k = this.getValueLocation(t);
		this.data.set(l, t.substring(0, k) + newVal);
	}

	public String getStringField(String key, String defVal) {
		String s = (String) this.infos.get(key);
		return s == null ? defVal : s;
	}

	public double getDoubleField(String key, double defVal) {
		String s = (String) this.infos.get(key);
		if (s == null)
			return defVal;
		else
			return parseCoord(s);
	}

	public int getIntField(String key, int defVal) {
		String s = (String) this.infos.get(key);
		return s == null ? defVal : Integer.parseInt(s);
	}

	public static double parseCoord(String s) {
		int p = s.indexOf('(');
		if (p != -1)
			s = s.substring(0, p);
		return Double.parseDouble(s);
	}

	private int getValueLocation(String s) {
		int i = s.indexOf(' ') + 1;
		if (i == -1)
			return -1;
		for (; true; i++) {
			if (i >= s.length())
				return -1;
			if (s.charAt(i) != ' ')
				return i;
		}
	}

	public Loop getLoopContainingHeader(String header) {
		for (int i = 0; i < this.loops.size(); i++) {
			CifFileParser.Loop loop = (CifFileParser.Loop) this.loops.get(i);
			if (loop.header.contains(header))
				return loop;
		}
		return null;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("-- Header --\n");
		for (int i = 0; i < this.cifHeader.size(); i++)
			sb.append(this.cifHeader.get(i) + "\n");

		sb.append("-- Infos --\n");
		Iterator iterator = this.infos.entrySet().iterator();
		for (java.util.Map.Entry e; iterator.hasNext();) {
			e = (java.util.Map.Entry) iterator.next();
			sb.append(e.getKey() + " = " + e.getValue() + "\n");
		}

		for (int i = 0; i < this.loops.size(); i++) {
			sb.append("-- Block --\n");
			Loop l = ((Loop) this.loops.get(i));
			Vector v = l.header;
			for (int j = 0; j < v.size(); j++)
				sb.append(v.get(j) + " ");
			sb.append("\n");
			for (int j = 0; j < l.lines.size(); j++) {
				Vector u = (Vector) l.lines.get(j);
				for (int k = 0; k < u.size(); k++)
					sb.append(u.get(k) + " ");
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public CifFileParser(Reader in) {
		try {
			BufferedReader bufIn = new BufferedReader(in);
			this.currentLoop = null;
			this.currentLabel = null;
			this.cifHeader = new Vector(2, 3);
			this.infos = new HashMap();
			this.infosToDataLine = new HashMap();
			this.loops = new Vector(5, 5);
			this.currentLoopLine = null;
			this.data = new Vector(100, 100);

			for (this.currentLine = 0; true; this.currentLine++) {
				String s = bufIn.readLine();
				if (s == null)
					break;
				this.data.add(s);
				s = s.trim();
				if (s.length() == 0 || s.charAt(0) == '#')
					continue;

				String[] ss = s.split("'");
				for (int j = 0; j < ss.length; j++)
					if (j % 2 == 0) {
						String[] sss = ss[j].trim().split("\\s+");
						for (int k = 0; k < sss.length; k++) {
							if (sss[k].trim().length() == 0)
								continue;
							this.token(sss[k]);
						}
					} else
						this.token(ss[j]);
				this.newLine();
			}
			this.endOfFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public CifFileParser(String[] file) {
		this.data = new Vector(100, 100);
		for (int i = 0; i < file.length; i++)
			this.data.add(file[i]);
		this.currentLoop = null;
		this.currentLabel = null;
		this.cifHeader = new Vector(2, 3);
		this.infos = new HashMap();
		this.infosToDataLine = new HashMap();
		this.loops = new Vector(5, 5);
		this.currentLoopLine = null;

		for (this.currentLine = 0; this.currentLine < file.length; this.currentLine++) {
			String s = file[this.currentLine];
			if (s == null)
				break;
			s = s.trim();
			if (s.length() == 0 || s.charAt(0) == '#')
				continue;

			String[] ss = s.split("'");
			for (int j = 0; j < ss.length; j++)
				if (j % 2 == 0) {
					String[] sss = ss[j].trim().split("\\s+");
					for (int k = 0; k < sss.length; k++) {
						if (sss[k].trim().length() == 0)
							continue;
						this.token(sss[k]);
					}
				} else
					this.token(ss[j]);
			this.newLine();
		}
		this.endOfFile();
	}

	private void token(String s) {
		if (this.currentLoop != null) {
			if (this.loopInHeader) {
				if (s.charAt(0) == '_')
					this.currentLoop.header.add(s);
				else {
					this.loopInHeader = false;
					this.currentLoopLine = new Vector(5, 5);
					this.currentLoopLine.add(s);
				}
			} else if (s.charAt(0) == '_' || s.equalsIgnoreCase("loop_")) {
				// end loop
				this.loops.add(this.currentLoop);
				this.currentLoop = null;
				this.currentLoopLine = null;
				this.token(s);
			} else
				// new entry in current loop line
				this.currentLoopLine.add(s);
		} else if (this.currentLabel != null) {
			if (s.equalsIgnoreCase("loop_") || s.charAt(0) == '_') {
				this.infos.put(this.currentLabel, null);
				this.infosToDataLine.put(this.currentLabel, null);
				this.currentLabel = null;
				this.token(s);
			} else {
				this.infos.put(this.currentLabel, s);
				this.infosToDataLine.put(this.currentLabel, new Integer(this.currentLine));
				this.currentLabel = null;
			}
		} else if (s.equalsIgnoreCase("loop_")) {
			this.currentLoop = new Loop();
			this.loopInHeader = true;
		} else if (s.charAt(0) == '_')
			this.currentLabel = s;
		else
			this.cifHeader.add(s);
	}

	private void newLine() {
		if (this.currentLoopLine != null) {
			this.currentLoop.lines.add(this.currentLoopLine);
			this.currentLoop.linesToDataLine.add(new Integer(this.currentLine));
			this.currentLoopLine = new Vector(5, 5);
		}
	}

	private void endOfFile() {
		if (this.currentLoop != null)
			this.loops.add(this.currentLoop);
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
