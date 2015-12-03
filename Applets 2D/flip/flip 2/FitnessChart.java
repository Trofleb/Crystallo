import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

/* Charge Flip - FitnessChart.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 13 déc. 06
 * 
 * nicolas.schoeni@epfl.ch
 */

public class FitnessChart extends JPanel {
	private FloatVector[] data;
	private BufferedImage buffer;
	private int x1, x2, y1, y2, dy, sd, fh;
	private float dw = 2f;
	private int dws = 20;
	private Color c1 = new Color(100, 100, 100);
	private Color c2 = new Color(0x6337F3);
	private Color c3 = new Color(0xF35237);

	
	private class FloatVector {
		private static final int SUBSIZE = 500;
		private Vector v;
		private int index;
		
		public FloatVector() {
			v = new Vector();
			index = 0;
		}
		public void add(float f) {
			if (index%SUBSIZE==0) v.add(new float[SUBSIZE]);
			float[] ff = (float[]) v.get(index/SUBSIZE);
			ff[index%SUBSIZE]=f;
			index++;
		}
		public float get(int i) {
			float[] ff = (float[]) v.get(i/SUBSIZE);
			return ff[i%SUBSIZE];
		}
		public int size() {
			return index;
		}
		public void clear() {
			v.clear();
			index=0;
		}
	}
	
	public FitnessChart() {
		data = new FloatVector[2];
		data[0] = new FloatVector();
		data[1] = new FloatVector();
	}
	
	public void addValue(float v1, float v2) {
		data[0].add(v1);
		data[1].add(v2);
		if (data[0].size()*dw+x1>getWidth()*.95f) {
			dw/=2f;
			if (dws==20) dws=50; else if (dws==200) dws=500; else dws*=2;
			createBuffer();
		}
		else {
			Graphics2D g = (Graphics2D) buffer.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			drawSpot(g, data[0].size()-1, v1, v2);
		}
		repaint();
	}
	
	public void reset() {
		data[0].clear();
		data[1].clear();
		dw = 2f;
		dws = 20;
		createBuffer();
		repaint();
	}
	
	private void drawSpot(Graphics g, int i, float v1, float v2) {
		int r = 1;
		int x = Math.round(i*dw+x1);
		int y = y1-(int)Math.ceil(v1*10f*dy)-r;
		g.setColor(c2);
		g.fillOval(x-r, y+r, 2*r, 2*r);
		y = y1-(int)Math.ceil(v2*10f*dy)-r;
		g.setColor(c3);
		g.fillOval(x-r, y+r, 2*r, 2*r);
		if (i%dws==0) {
			Font f = new Font("SansSerif", Font.PLAIN, 10);
			g.setFont(f);
			g.setColor(c1);
			g.drawLine(x, y1-sd, x, y1+sd);
			String s = String.valueOf(i);
			g.drawString(s, x-g.getFontMetrics().stringWidth(s)/2, y1+sd+12);
		}
	}
	
	private void createBuffer() {
		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g.setColor(getBackground());
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

//		Composite c = g.getComposite();
//		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
//		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
//		g.setComposite(c);
//		g.setColor(new Color(255, 255, 255, 200));
//		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		
		Font f = new Font("SansSerif", Font.PLAIN, 10);
		g.setFont(f);
		x1 = Math.round(getWidth()*.04f); 
		x2 = Math.round(getWidth()*.98f); 
		y1 = Math.round(getHeight()*.90f); 
		y2 = Math.round(getHeight()*.04f); 
		dy = (y1-y2)/10; 
		sd = 3; 
		fh = g.getFontMetrics().getHeight();
		
		g.setColor(c1);
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x1, y1, x1, y2);
		
		int lw = g.getFontMetrics().stringWidth("Total Charge G(0)");
		g.setColor(c2);
		g.drawLine(x2-lw-14, y2+2*fh-4, x2-lw-4, y2+2*fh-4);
		g.drawLine(x2-lw-14, y2+2*fh-3, x2-lw-4, y2+2*fh-3);
		g.setColor(c3);
		g.drawLine(x2-lw-14, y2+fh-4, x2-lw-4, y2+fh-4);
		g.drawLine(x2-lw-14, y2+fh-3, x2-lw-4, y2+fh-3);
		g.setColor(c1);
		g.drawString("Total Charge G(0)", x2-lw, y2+fh);
		g.drawString("R factor", x2-lw, y2+2*fh);
		
		for (int i=2; i<=10; i+=2) {
			g.drawLine(x1-sd, y1-dy*i, x1+sd, y1-dy*i);
			String s = i*10+"%";
			int fwx = g.getFontMetrics().stringWidth(s)+5;
			g.drawString(s, x1-sd-fwx, y1-dy*i+fh/4);
		}
		for (int i=0; i<data[0].size(); i++) {
			drawSpot(g, i, data[0].get(i), data[1].get(i));
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if (buffer==null || buffer.getWidth()!=getWidth() || buffer.getHeight()!=getHeight()) {
			createBuffer();
		}
		g.drawImage(buffer, 0, 0, null);
	}
}
