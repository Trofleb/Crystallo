import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
	public Vector loops;

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
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	@Override
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

	private int line;

	public CifFileParser(String[] data) {
		this.currentLoop = null;
		this.currentLabel = null;
		this.cifHeader = new Vector(2, 3);
		this.infos = new HashMap();
		this.loops = new Vector(5, 5);
		this.currentLoopLine = null;

		for (this.line = 0; this.line < data.length; this.line++) {
			String s = data[this.line].trim();
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
				this.currentLabel = null;
				this.token(s);
			} else {
				this.infos.put(this.currentLabel, s);
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

	public void newLine() {
		if (this.currentLoopLine != null) {
			this.currentLoop.lines.add(this.currentLoopLine);
			this.currentLoopLine = new Vector(5, 5);
		}
	}

	public void endOfFile() {
		if (this.currentLoop != null)
			this.loops.add(this.currentLoop);
	}

	public static class Loop {
		public Vector header = new Vector(10, 10);
		public Vector lines = new Vector(10, 10);
	}

	public static String ArrayToString(String[] ss) {
		String s = "";
		for (int i = 0; i < ss.length; i++)
			s += ((i > 0 ? " " : "") + ss[i]);
		return s;
	}

	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("c:\\test.cif")));
			Vector v = new Vector(100, 100);
			for (String s; (s = in.readLine()) != null; v.add(s))
				;
			CifFileParser cif = new CifFileParser((String[]) v.toArray(new String[0]));

			System.out.println(cif);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
