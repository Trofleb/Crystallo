import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * Created on 24 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DataBaseCreator {

	public final static String[][] doubleChoicesParam = { { "&gua=b", "&gua=c" }, { "&gor=1", "&gor=2" },
			{ "&grha=hexagonal", "&grha=rhombohedral" } };

	public static String getChoice(int n, int c) {
		for (int i = 0; i < CellSymetries.doubleChoicesNo.length; i++)
			for (int j = 0; j < CellSymetries.doubleChoicesNo[i].length; j++)
				if (n == CellSymetries.doubleChoicesNo[i][j])
					return doubleChoicesParam[i][c];
		return "";
	}

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		int n0 = 1, n1 = 230;

		if (1 == 1)
			throw new RuntimeException("Really !?");

		PrintStream out = new PrintStream(new FileOutputStream("CellGen.java"));

		out.println("import java.lang.reflect.Method;");
		out.println("public class CellGen {");

		for (int n = n0; n <= n1; n++)
			for (int c = 0; c <= (CellSymetries.hasChoice(n) ? 1 : 0); c++) {

				out.println(" public static float[][] pos" + n + (c == 1 ? "alt" : "")
						+ "(float x, float y, float z) {return new float[][] {");

				String param;
				if (CellSymetries.hasChoice(n))
					param = getChoice(n, c);
				else
					param = "";

				BufferedReader stream;
				try {
					URL url = new URL(
							"http://www.cryst.ehu.es/cgi-bin/cryst/programs/nph-getgen?list=show&what=gp&gnum=" + n
									+ param);
					stream = new BufferedReader(new InputStreamReader(url.openStream()));
				} catch (Exception e) {
					try {
						System.out.println("error!");
						Thread.sleep(1000);
						System.out.println("retrying");
						URL url = new URL(
								"http://www.cryst.ehu.es/cgi-bin/cryst/programs/nph-getgen?list=show&what=gp&gnum=" + n
										+ param);
						stream = new BufferedReader(new InputStreamReader(url.openStream()));
					} catch (Exception ee) {
						System.out.println("error!");
						Thread.sleep(10000);
						System.out.println("retrying");
						URL url = new URL(
								"http://www.cryst.ehu.es/cgi-bin/cryst/programs/nph-getgen?list=show&what=gp&gnum=" + n
										+ param);
						stream = new BufferedReader(new InputStreamReader(url.openStream()));
					}
				}

				boolean endOk = false, startOk = false;
				while (true) {
					String s = stream.readLine();
					if (s == null)
						break;
					int i0 = s.indexOf("General positions of the Group " + n);
					if (i0 != -1)
						startOk = true;
					int i1 = s.indexOf("</table></center><p><p>");
					if (i1 != -1)
						endOk = true;
					int i2 = s.indexOf("<td align=\"center\">");
					if (i2 != -1) {
						int i3 = s.indexOf("</td>", i2);
						String eq = s.substring(i2 + 19, i3);
						// System.out.println(eq);
						StringBuffer buf = new StringBuffer();
						for (int i = 0; i < eq.length(); i++) {
							char ch = eq.charAt(i);
							buf.append(ch);
							if (Character.isDigit(ch))
								buf.append('f');
						}
						eq = buf.toString();
						out.println("  {" + eq + "},");
					}
					// System.out.println(s);
				}
				if (!endOk)
					throw new RuntimeException("End pattern not found");
				if (!startOk)
					throw new RuntimeException("Start pattern not found");

				stream.close();

				out.println(" };}");

				System.out.print(c == 0 ? "." : ":");
				if (n % 20 == 0)
					System.out.println(n);

				Thread.sleep(500);
			}

		out.println();
		out.println(" static Method[] methods;");
		out.println(" static {");
		out.println("  try {");
		out.println("   methods = new Method[] {");

		for (int n = n0; n <= n1; n++)
			out.println("    CellGen.class.getMethod(\"pos" + n
					+ "\", new Class[]{float.class, float.class, float.class}),");

		for (int n = n0; n <= n1; n++)
			if (CellSymetries.hasChoice(n))
				out.println("    CellGen.class.getMethod(\"pos" + n
						+ "alt\", new Class[]{float.class, float.class, float.class}),");

		out.println();
		out.println("   };");
		out.println("  } catch (NoSuchMethodException e) {throw new RuntimeException(e);}");
		out.println(" }");
		out.println();
		out.println(" public static float[][] pos(int n, int c, float x, float y, float z) {");
		out.println("  try {");
		out.println(
				"   return (float[][]) methods[CellSymetries.getIndex(n, c)].invoke(null, new Object[]{new Float(x), new Float(y), new Float(z)});");
		out.println("  } catch (Exception e) {throw new RuntimeException(e);}");
		out.println(" }");
		out.println();
		out.println(" public static void main(String[] args) {");
		out.println("  float[][] m = pos(1, 0, 1.5f, 2.3f, 4.6f);");
		out.println("  for (int i=0; i<m.length; i++)");
		out.println("   System.out.println(m[i][0]+\",\"+m[i][1]+\",\"+m[i][2]);");
		out.println(" }");

		out.println("}");

		out.close();

		System.out.println();
		System.out.println("Finished");
	}
}
