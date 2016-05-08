import java.awt.*;
import java.net.*;
import java.applet.*;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public
class rlattice extends JApplet {

	Image img;
	String imageName="save_0.gif";
	int offset=0;
	Pane pan;
	Canvas canvas;
	boolean quick;

	public void init() {
		imageName = getParameter("patternChoice");
		offset = Integer.parseInt(getParameter("offset"));
		quick = (getParameter("quick") == null) ? false : true;
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add("East", (pan = new Pane()).toJPanel());
		this.getContentPane().add("Center", canvas = new imgDisplay(imageName, pan, getCodeBase(), quick, offset, this));
	}
	/*
   public boolean action(Event evt, Object arg) {
     return canvas.action(evt,arg);
   }

   private Image offScreenImage;
   private Dimension offScreenSize;
   private Graphics offScreenGraphics;

   public final synchronized void update (Graphics g) {
     Dimension d = size();
     if ((offScreenImage == null) || (d.width != offScreenSize.width) ||  (d.height != offScreenSize.height)) {
       offScreenImage = createImage(d.width, d.height);
       offScreenSize = d;
       offScreenGraphics = offScreenImage.getGraphics();
     }
     paint(offScreenGraphics);
     g.drawImage(offScreenImage, 0, 0, null);
   }
	 */   
	public static void main(String[] args) {
		JFrame frame = new JFrame("Reciprocal Lattice Calculator");
		frame.setSize(600, 360);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		Pane pan;
		Canvas canvas;
		c.add("East", (pan = new Pane()).toJPanel());
		try {
			//c.add("Center", id = new imgDisplay("save_0.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 0, null));
			//c.add("Center", canvas = new imgDisplay("index_2619.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 46, null));
			//c.add("Center", canvas = new ImgDisplay2("index_2619.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 46, null, false));

			c.add("Center", canvas = new imgDisplay("index_2619.gif", pan, new URL("http://lcr1pc22/rlattice/"), false, 46, null));


		} catch (Exception e) {throw new RuntimeException(e);}
		frame.validate();
	}
}
