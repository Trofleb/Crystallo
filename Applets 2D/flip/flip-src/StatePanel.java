import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/* Charge Flip - StatePanel.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 11 janv. 07
 * 
 * nicolas.schoeni@epfl.ch
 */

public class StatePanel extends JPanel {
	private BufferedImage image, states[][];
	private static final int[] sx = {1, 23, 140, 140, 289, 289};
	private static final int[] sy = {11, 114, 114, 14, 14, 114};
	private int state = 0;
	
	public StatePanel() {
		try {
			InputStream in = getClass().getResourceAsStream("/states.png");
			image = ImageIO.read(in);
			states = new BufferedImage[2][2];
			in = getClass().getResourceAsStream("/activeState1.png");
			states[0][0] = ImageIO.read(in);
			in = getClass().getResourceAsStream("/activeState2.png");
			states[0][1] = ImageIO.read(in);
			in = getClass().getResourceAsStream("/unactiveState1.png");
			states[1][0] = ImageIO.read(in);
			in = getClass().getResourceAsStream("/unactiveState2.png");
			states[1][1] = ImageIO.read(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}
	
	public void setActiveState(int state) {
		this.state = state;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int y = (getHeight()-image.getHeight())/2;
		for (int i=0; i<sx.length; i++) {
			g.drawImage(states[state==i||(state==-1&&i>1)?0:1][i==0?1:0], 0+sx[i], y+sy[i], null);
		}
		g.drawImage(image, 0, y, null);
	}
}
