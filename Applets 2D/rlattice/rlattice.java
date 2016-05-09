import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.applet.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.misc.IOUtils;

public class rlattice  {

	Image img;
	private static String imageName ="save_0.gif";
	int offset=0;
	Pane pan;
	Canvas canvas;
	boolean quick;

	public static void main(String[] args) {
		choose();
	}
	
	private static void runApp() {
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

			c.add("Center", canvas = new imgDisplay(imageName, pan, frame, false, 46, null));


		} catch (Exception e) {throw new RuntimeException(e);}
		frame.validate();	
	
	}

	public static void choose() {
		JPanel choosePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JFrame chooseFrame = new JFrame("Choose your pattern");
		chooseFrame.add(choosePanel);
		try {
			File htmlFile = new File("intro.html");
			byte[] t = IOUtils.readFully(new FileInputStream(htmlFile), -1, true);

			JLabel text = new JLabel();
			text.setText("<html><body>"+new String(t)+"</body></html>");
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 5;
			c.ipadx = 10;
			choosePanel.add(text, c);

			JLabel im1 = createImage("index_2619.gif");
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			choosePanel.add(im1, c);

			JLabel im2 = createImage("index_2676.gif");
			c.gridx = 1;
			c.gridy = 1;
			choosePanel.add(im2, c);

			JLabel im3 = createImage("index_2680.gif");
			c.gridx = 2;
			c.gridy = 1;
			choosePanel.add(im3, c);

			JLabel im4 = createImage("index_2826.gif");
			c.gridx = 3;
			c.gridy = 1;
			choosePanel.add(im4, c);

			JButton ok = new JButton("ok");
			ok.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					runApp();
					chooseFrame.dispose();
				}
			});
			c.gridx = 2;
			c.gridy = 2;
			choosePanel.add(ok, c);

			chooseFrame.setVisible(true);
			chooseFrame.pack();

		} catch (Exception e) {
			System.err.println("Unable to show description.");
		}

	}
	
	private static JLabel createImage(String name) {
		JLabel im = null;
		try {
			im = new JLabel(new ImageIcon(ImageIO.read(new File(name))));

			im.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					imageName = name;
					System.out.println(name);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}
			});
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return im;
	}
}
