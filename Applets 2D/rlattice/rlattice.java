import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class rlattice  {

	Image img;
	private static String imageName ="save_0.gif";
	private static HashMap<String, JLabel> images;
	int offset=0;
	Pane pan;
	Canvas canvas;
	boolean quick;
	static public rlattice instance;

	private rlattice() {}

	public static void main(String[] args) {
		images = new HashMap<>();
		rlattice.instance = new rlattice();
		rlattice.instance.choose();
	}
	
	private static void runApp() {
		JFrame frame = new JFrame("Reciprocal Lattice Calculator");
		frame.setSize(1000, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		Pane pan;
		c.add("East", (pan = new Pane()).toJPanel());
		try {
			//c.add("Center", id = new imgDisplay("save_0.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 0, null));
			//c.add("Center", canvas = new imgDisplay("index_2619.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 46, null));
			//c.add("Center", canvas = new ImgDisplay2("index_2619.gif", pan, new URL("http://marie.epfl.ch/crystallo/reciprocal/"), false, 46, null, false));

			c.add("Center", new imgDisplay(imageName, pan, frame, false, 46, null));


		} catch (Exception e) {throw new RuntimeException(e);}
		frame.validate();	
	}

	public InputStream open(String name) {
		return getClass().getResourceAsStream(name);
	}

	public void choose() {
		JPanel choosePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		final JFrame chooseFrame = new JFrame("Choose your pattern");
		chooseFrame.add(choosePanel);
		try {
			InputStream in = open("/intro.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;

			while((line =reader.readLine())!=null){
				stringBuffer.append(line).append("\n");
			}

			String t = stringBuffer.toString();

			JLabel text = new JLabel();
			text.setText("<html><body>"+ t +"</body></html>");
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 5;
			c.insets = new Insets(10, 10, 10, 10);
			choosePanel.add(text, c);

			JLabel im1 = createImage("index_2619.gif");
			images.put("index_2619.gif", im1);
			im1.setBorder(new LineBorder(Color.black, 2));
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			choosePanel.add(im1, c);

			JLabel im2 = createImage("index_2676.gif");
			images.put("index_2676.gif", im2);
			im2.setBorder(new LineBorder(Color.black, 2));
			c.gridx = 1;
			c.gridy = 1;
			choosePanel.add(im2, c);

			JLabel im3 = createImage("index_2680.gif");
			im3.setBorder(new LineBorder(Color.black, 2));
			images.put("index_2680.gif", im3);
			c.gridx = 2;
			c.gridy = 1;
			choosePanel.add(im3, c);

			JLabel im4 = createImage("index_2826.gif");
			im4.setBorder(new LineBorder(Color.black, 2));
			images.put("index_2826.gif", im4);
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
			chooseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		} catch (Exception e) {
			System.err.println("Unable to show description.");
			e.printStackTrace();
		}

	}
	
	
	private static void selectImage(String name) {
		JLabel im = images.get(name);
		im.setBorder(new LineBorder(Color.red, 2));
		
		for(String n : images.keySet()) {
			if (!n.equals(name)) {
				JLabel image = images.get(n);
				image.setBorder(new LineBorder(Color.black, 2));
			}
		}
	}
	
	private JLabel createImage(final String name) {
		JLabel im = null;
		try {
			im = new JLabel(new ImageIcon(ImageIO.read(open(name))));

			im.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					imageName = name;
					selectImage(name);
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
