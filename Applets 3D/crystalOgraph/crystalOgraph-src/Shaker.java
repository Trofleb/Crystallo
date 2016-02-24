import java.awt.Frame;

/*
 * Created on 14 juil. 2004
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author nschoeni
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Shaker {
	static Frame frame;
	static public boolean quiet = false;

	public static void shake(String txt) {
		// Exception e = new Exception("Shake");
		// e.fillInStackTrace();
		// e.printStackTrace(System.err);
		System.err.println(txt);

		// if (frame==null || quiet) return;
		// Point p = frame.getLocation();
		// for (int i=6; i>=0; i-=1) {
		// frame.setLocation(p.x-i, p.y);
		// //try {Thread.sleep(80);}catch(Exception ee){}
		// frame.setLocation(p.x+i, p.y);
		// }
	}
}
