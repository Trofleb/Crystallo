package panes;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Vector;

import javax.swing.JPanel;


public class ProjScreen {
	public static Vector allInstances;
	
	public static boolean drawIndex=true, drawI=false;
	private static double dx, dy;
	private static int mx, my;
	public static double scale2D = 7;
	public static double scaleZoom = 16;
	public static double scaleUser = 1;
	public static ProjPanel p;
	public static Color colorIndex, colorIntensity;

	private Vector points;
	public double scaleStruct = 1;
	public double ldx;
	public double iMin;
	private Color color, colorText;
	public boolean visible=true;
	
	public static class Point {
		public double x, y, r;
		public String index;
		public double intensity;
		public Point(double x, double y, double r, String index, double intensity){
			this.x=x; this.y=y; this.r=r; this.index=index; this.intensity=intensity;
		}
	}
	
	public static JPanel createPanel() {
		allInstances = new Vector(20, 20);
		mx=my=0;
		dx=dy=0;
		p = new ProjPanel();
		p.setCursor(new Cursor(Cursor.HAND_CURSOR));
		p.addMouseMotionListener(p);
		p.addMouseWheelListener(p);
		p.addComponentListener(sizeListener);
		return p;
	}

	public ProjScreen(Color color) {
		allInstances.add(this);
		points = new Vector(100, 200);
		this.color=color;
		this.colorText = new Color(color.getRGB());
	}
	
	public synchronized void addPoint(double x, double y, double r, String index, double intensity) {
		points.add(new Point(x, y, r, index, intensity));
	}
	
	public synchronized void clearImage() {
		points.clear();
		p.repaint();
	}
		
	public static synchronized void refresh() {
		p.repaint();
	}
	
	public void setColor(Color color) {
		this.color=color;
		this.colorText = new Color(color.getRGB());
		p.repaint();
	}

	public void remove() {
		points.clear();
		allInstances.remove(this);
		p.repaint();
	}

	private static int min(int a, int b) {
		return a<b?a:b;
	}
	
	
	static ComponentListener sizeListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
		}
	};
	
	public static class ProjPanel extends JPanel implements MouseMotionListener, MouseWheelListener {
		public String message;
		public synchronized void paint(Graphics g) {
			super.paint(g);

			int w = getWidth();
			int h = getHeight();
			g.setColor(new Color(255, 255, 255, 200));
			g.fillRect(0, 0, w, h);
			
			w/=2; h/=2;
			int wh = min(w, h);

			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			if (message!=null) {
				g.setFont(new Font("SansSerif", Font.PLAIN, 14));
				g.setColor(Color.black);
				g.drawString(message, w-g.getFontMetrics().stringWidth(message)/2, h-14);
			}
			
			g.setFont(new Font("SansSerif", Font.PLAIN, 11));
			for (int j=0; j<allInstances.size(); j++) {
				ProjScreen e = (ProjScreen) allInstances.get(j);
				if (!e.visible) continue;
				for (int i=0; i<e.points.size(); i++) {
					Point p = (Point) e.points.get(i);
					int r = (int)Math.round(p.r*scaleZoom*scaleUser*scale2D);
					if (r>200) continue;
					g.setColor(e.color);
					int x = (int)Math.round(p.x*wh*scaleZoom+dx*scaleZoom+e.ldx*scaleZoom)+w-r;
					int y = (int)Math.round(p.y*wh*scaleZoom+dy*scaleZoom)+h-r;
					//if (r>100) g.drawOval(x, y, r*2, r*2); else 
					g.fillOval(x, y, r*2, r*2);
					if (r>30) {y+=r;x+=r/2;}
					if (drawI&&drawIndex && p.intensity>=e.iMin) {
						g.setColor(colorIndex!=null?colorIndex:e.colorText);
						g.drawString(p.index, x, y-11);
						g.setColor(colorIntensity!=null?colorIntensity:e.colorText);
						g.drawString(""+((int)p.intensity), x, y);
					}
					else if (drawI && p.intensity>=e.iMin) {
						g.setColor(colorIntensity!=null?colorIntensity:e.colorText);
						g.drawString(""+((int)p.intensity), x, y);
					}
					else if (drawIndex && p.intensity>=e.iMin) {
						g.setColor(colorIndex!=null?colorIndex:e.colorText);
						g.drawString(p.index, x, y);
					}
				}
			}
		}
		public synchronized void mouseDragged(MouseEvent e) {
			if (e.isControlDown() || e.isAltDown()||e.isShiftDown()||e.isMetaDown()||e.getButton()==MouseEvent.BUTTON2) {
				int a, b, d;
				a = e.getX()-mx;
				b = e.getY()-my;
				if (Math.abs(a)>Math.abs(b)) d=a;
				else d=b;
				scaleZoom += scaleZoom*(d/40d);
				repaint();
			}
			else {
				dx+=(e.getX()-mx)/scaleZoom;
				dy+=(e.getY()-my)/scaleZoom;
				repaint();
			}
			mx = e.getX();
			my = e.getY();
		}
		public synchronized void mouseMoved(MouseEvent e) {
			mx = e.getX();
			my = e.getY();
		}
		
		public synchronized void mouseWheelMoved(MouseWheelEvent e) {
			scaleZoom += scaleZoom*(e.getWheelRotation()/20d);
			repaint();
		}
	}
}


