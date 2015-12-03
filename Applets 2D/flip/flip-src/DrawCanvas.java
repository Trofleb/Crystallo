import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;

/* EscherFFT - DrawCanvas.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 23 mars 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

class DrawCanvas extends JPanel {
	private DrawSystem drawSystem;
	public  final int size;
	public String name;

	public  int placingCell=0, placingMask=0;
	private int drawingTool=0;
	private int toolFirstPointX, toolFirstPointY, toolSecondPointX, toolSecondPointY; 
	public  Cell cell;
	public  Mask mask;
	public  BufferedImage patternImage;
	private Graphics2D patternGraphics;
	private boolean eraser;
	public  boolean periodicity=false;
	
	public void destroy() {
		patternImage.flush();
		cell=null;
		mask=null;
	}
	
	public class Cell {
		int a=DrawSystem.defaultCellA, h=DrawSystem.defaultCellH, offset=DrawSystem.defaultCellOffset;
		int x=size/2-(a+offset)/2, y=size/2-h/2;
		
		public void applyClip(Graphics g, int dx, int dy) {
			//if (offset<0) dx+= offset;
			int[] xx = {dx, dx+a, dx+a+offset, dx+offset};
			int[] yy = {dy, dy, dy+h, dy+h};
			g.setClip(new Polygon(xx, yy, 4));
		}
		public void applyAntiClip(Graphics g, int dx, int dy) {
			//if (offset<0) dx+= offset;
			int[] xx = {dx, dx+a, dx+a+offset, dx+offset};
			int[] yy = {dy, dy, dy+h, dy+h};
			Area area = new Area(new Rectangle(0, 0, size, size));
			area.subtract(new Area(new Polygon(xx, yy, 4)));
			g.setClip(area);
		}
	}
	public class Mask {
		int w=DrawSystem.defaultMaskWidth, h=DrawSystem.defaultMaskHeight;
		int offset=0;
		int x=size/2-w/2, y=size/2-h/2;
		boolean circular=DrawSystem.defaultMaskCircular, rectangular=DrawSystem.defaultMaskRectangular;		
		Shape shape;
		
		public Mask() {
			updateShape();
		}
		
		public void updateShape() {
			if (circular) {
				shape=new Ellipse2D.Float(x, y, w, h);
			}
			else if (rectangular){
				int[] xx = {x, x+w, x+w+offset, x+offset};
				int[] yy = {y, y, y+h, y+h};
				shape=new Polygon(xx, yy, 4);
			}
			else {
				shape=null;
			}
		}
	}
	
	public String toString() {
		return name;
	}
	
	public DrawCanvas(String name, final int size, final DrawSystem drawSystem) {
		this.size=size;
		this.drawSystem = drawSystem;
		this.name=name;
		
		setBackground(new JPanel().getBackground());
		setPreferredSize(new Dimension(size, size));
		if (drawSystem.penCursor!=null) setCursor(drawSystem.penCursor);
		
		cell = new Cell();
		mask = new Mask();
		createPatternImage();
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getX()-((getWidth()-size)/2);
				int y = e.getY()-((getHeight()-size)/2);
				
				if (placingCell==0&&placingMask==0) {
					//eraser = e.isShiftDown()||e.getButton()!=MouseEvent.BUTTON1;
					if (drawSystem.tool==DrawSystem.TOOL_PEN) drawPoint(x, y);
					else {
						toolSecondPointX = x;
						toolSecondPointY = y;
					}
					repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {
				int x = e.getX()-((getWidth()-size)/2);
				int y = e.getY()-((getHeight()-size)/2);

				if (x>=0 && x<size && y>=0 && y<size) {
					drawSystem.tools.posLabel.setText("Position: "+(x-size/2)+" "+(-y+size/2));
				}
				else {
					drawSystem.tools.posLabel.setText("Position: ");
				}
				drawSystem.tools.posLabel.setBackground(Color.black);
				
				if (placingCell==1) {
					cell.x = x;
					cell.y = y;
					repaint();
				}
				else if (placingCell==2) {
					cell.h = y-cell.y;
					if (cell.h<15) cell.h=15;
					if (cell.h+cell.y>=size) cell.h=size-cell.y-1;
					if (e.isShiftDown()||e.isControlDown()) {
						cell.offset=0;
					}
					else {
						double a = (180-drawSystem.tools.cellAlphaEdit.getIntValue())*Math.PI/180d;
						cell.offset = (int)Math.round(cell.h/Math.tan(a));
					}
					cell.a = x-cell.x-cell.offset;
					if (cell.a<20) cell.a=20;
					if (cell.a+cell.x>=size) cell.a=size-cell.x-1;
					if (e.isShiftDown()||e.isControlDown()) {
						cell.h = cell.a = Math.max(cell.h, cell.a);
					}
					drawSystem.tools.setCellParams(cell, false);
					repaint();
				}
				else if (placingMask==1) {
					if (mask.circular) {
						mask.x = x-mask.w/2;
						mask.y = y-mask.h/2;
					}
					if (mask.rectangular) {
						mask.x = x;
						mask.y = y;
					}
					mask.updateShape();
					repaint();
				}
				else if (placingMask==2) {
					if (mask.circular) {
						int cx = mask.x+mask.w/2;
						int cy = mask.y+mask.h/2;
						mask.w = (x-cx)*2;
						mask.h = (y-cy)*2;
						if (mask.w<20) mask.w=20;
						if (mask.h<15) mask.h=15;
						if (e.isShiftDown()||e.isControlDown()) {
							mask.h = mask.w = Math.max(mask.h, mask.w);
						}
						mask.x = cx-mask.w/2;
						mask.y = cy-mask.h/2;
					}
					if (mask.rectangular) {
						mask.h = y-mask.y;
						if (mask.h<15) mask.h=15;
						if (e.isShiftDown()||e.isControlDown()) {
							mask.offset=0;
						}
						else {
							double a = (180-drawSystem.tools.angleMaskEdit.getIntValue())*Math.PI/180d;
							mask.offset = (int)Math.round(mask.h/Math.tan(a));
						}
						mask.w = x-mask.x-mask.offset;
						if (mask.w<20) mask.w=20;
						if (e.isShiftDown()||e.isControlDown()) {
							mask.h = mask.w = Math.max(mask.h, mask.w);
						}
					}
					mask.updateShape();
					drawSystem.tools.setMaskParams(mask, false);
					repaint();
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX()-((getWidth()-size)/2);
				int y = e.getY()-((getHeight()-size)/2);
				
				if (placingCell==0&&placingMask==0) {
					eraser = e.isShiftDown()||e.getButton()!=MouseEvent.BUTTON1;
					if (drawSystem.tool==DrawSystem.TOOL_PEN) drawPoint(x, y);
					else {
						toolFirstPointX = toolSecondPointX = x;
						toolFirstPointY = toolSecondPointY = y;
						drawingTool = 1;
					}
					repaint();
				}
				else if (placingCell==1) placingCell=2;
				else if (placingCell==2) stopPlaceCell();
				else if (placingMask==1) placingMask=2;
				else if (placingMask==2) stopPlaceMask();
			}
			public void mouseReleased(MouseEvent e) {
				if (drawingTool!=0) drawTool(patternGraphics, toolFirstPointX, toolFirstPointY, toolSecondPointX, toolSecondPointY); 
				drawingTool = 0;
			}

		});
	}

	public void beginPlaceCell() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		drawSystem.tools.placeMaskButton.setSelected(false);
		placingMask=0;
		placingCell=1;
		erase();
		drawSystem.createUpLayerImage(true, false);
		repaint();
	}
	public void stopPlaceCell() {
		if (drawSystem.penCursor!=null) setCursor(drawSystem.penCursor);
		placingCell=0;
		drawSystem.tools.setCellParams(cell, true);
		drawSystem.tools.placeCellButton.setSelected(false);
		drawSystem.createUpLayerImage(true, periodicity);
	}
	
	public void beginPlaceMask() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		drawSystem.tools.placeCellButton.setSelected(false);
		placingCell=0;
		placingMask=1;
		drawSystem.createUpLayerImage(false, false);
		repaint();
	}
	public void stopPlaceMask() {
		if (drawSystem.penCursor!=null) setCursor(drawSystem.penCursor);
		placingMask=0;
		drawSystem.tools.setMaskParams(mask, true);
		drawSystem.tools.placeMaskButton.setSelected(false);
		drawSystem.createUpLayerImage(true, periodicity);
	}

	private void fill(Graphics2D g, Color c) {
		g.setColor(c);
		g.fillRect(0, 0, size, size);
	}
	
	private void fillWithTransparency(Graphics2D g) {
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		g.fillRect(0, 0, size, size);
		g.setComposite(c);
	}	

	public static BufferedImage createTransparentImage(int w, int h) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		Composite c = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		g.fillRect(0, 0, w, h);
		g.setComposite(c);
		return image;
	}	
	
	public void createUpLayerImage(Graphics2D upLayerGraphics, boolean hasMask, boolean cellVisible) {
		if (hasMask) {
			fill(upLayerGraphics, getBackground());
			upLayerGraphics.setClip(mask.shape);
		}
		fillWithTransparency(upLayerGraphics);
		drawGrid(upLayerGraphics);
		upLayerGraphics.setClip(null);
		if (cellVisible) {
			drawCell(upLayerGraphics);
		}
	}
	
	
	private void createPatternImage() {
		patternImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		patternGraphics = patternImage.createGraphics(); 
		patternGraphics.setColor(Color.white);
		patternGraphics.fillRect(0, 0, size, size);
		patternGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	
	private void drawPoint(int x, int y) {
		if (drawSystem.gaussianPen) {
			int s = drawSystem.toolSize/2;
			int w = drawSystem.toolSize*5;
			GaussianPaint gaussianPaint = new GaussianPaint(drawSystem.toolDark, s, eraser);
			patternGraphics.setPaint(gaussianPaint);
			patternGraphics.fillOval(x-w/2, y-w/2, w, w);
		}
		else {
			int w = drawSystem.toolSize;
			int c = eraser?255:(255-drawSystem.toolDark);
			patternGraphics.setColor(new Color(c, c, c));
			patternGraphics.fillOval(x-w/2, y-w/2, w, w);
		}
	}
	
	public void drawTool(Graphics2D g, int x1, int y1, int x2, int y2) {
		int w = x2-x1, h = y2-y1;
		drawSingleTool(g, x1, y1, w, h);
		if (periodicity) {
			for (int j=y1,i0=x1; (h>0?j:j+h)<size+drawSystem.toolSize; j+=cell.h,i0+=cell.offset) {
				for (int i=i0; (w>0?i:i+w)<size+drawSystem.toolSize; i+=cell.a) {
					if (i==i0&&j==y1) continue;
					drawSingleTool(g, i, j, w, h);
				}
				for (int i=i0-cell.a; (w>0?i+w:i)>-drawSystem.toolSize; i-=cell.a) {
					drawSingleTool(g, i, j, w, h);
				}
			}
			for (int j=y1-cell.h,i0=x1-cell.offset; (h>0?j+h:j)>-drawSystem.toolSize; j-=cell.h,i0-=cell.offset) {
				for (int i=i0; (w>0?i:i+w)<size+drawSystem.toolSize; i+=cell.a) {
					drawSingleTool(g, i, j, w, h);
				}
				for (int i=i0-cell.a; (w>0?i+w:i)>-drawSystem.toolSize; i-=cell.a) {
					drawSingleTool(g, i, j, w, h);
				}
			}
		}
	}
	
	public void drawSingleTool(Graphics2D g, int x, int y, int w, int h) {
		g.setColor(eraser?Color.white:Color.black);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(drawSystem.toolSize));
		switch (drawSystem.tool) {
			case 1:
				g.drawLine(x, y, x+w, y+h);
				break;
			case 2: {
				if (w<0) {w=-w; x-=w;}
				if (h<0) {h=-h; y-=h;}
				g.drawRect(x, y, w, h);
				break;
			}
			case 3: {
				if (w<0) {w=-w; x-=w;}
				if (h<0) {h=-h; y-=h;}
				g.fillRect(x, y, w, h);
				break;
			}
		}
		g.setStroke(s);
	}

	
	public void erase() {
		fill(patternGraphics, Color.white);
	}
	
	public void loadImage(BufferedImage image) {
		periodicity=false;
		drawSystem.tools.periodicButton.setSelected(false);
		erase();
//		if (ImageAnalyser.hasBlackBorder(image)) {
//			int x = (size-image.getWidth())/2;
//			int y = (size-image.getHeight())/2;
//			int w = image.getWidth();
//			int h = image.getHeight();
//			patternGraphics.drawImage(image, x+1, y+1, x+w-1, y+h-1, 1, 1, w-1, h-1, null);
//		}
//		else {
//			patternGraphics.drawImage(image, (size-image.getWidth())/2, (size-image.getHeight())/2, null);
//		}
		patternGraphics.drawImage(image, (size-image.getWidth())/2, (size-image.getHeight())/2, null);		
		repaint();
	}
	
	public void drawCell(Graphics g) {
		g.setColor(Color.red);
		g.drawLine(cell.x, cell.y, cell.x+cell.a, cell.y);
		g.drawLine(cell.x, cell.y, cell.x+cell.offset, cell.y+cell.h);
		g.drawLine(cell.x+cell.offset, cell.y+cell.h, cell.x+cell.offset+cell.a, cell.y+cell.h);
		g.drawLine(cell.x+cell.a, cell.y, cell.x+cell.offset+cell.a, cell.y+cell.h);
	}
	public static void drawCell(Graphics g, Color color, int x, int y, int a, int h, int offset) {
		g.setColor(color);
		g.drawLine(x, y, x+a, y);
		g.drawLine(x, y, x+offset, y+h);
		g.drawLine(x+offset, y+h, x+offset+a, y+h);
		g.drawLine(x+a, y, x+offset+a, y+h);
	}

	public void drawGrid(Graphics2D g) {
		g.setColor(DrawSystem.gridColor);
		g.drawLine(0, size/2, size, size/2);
		g.drawLine(size/2, 0, size/2, size);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[] {2f}, 0f));
		for (int i=DrawSystem.gridSize; i<size; i+=DrawSystem.gridSize) {
			if (i==size/2) continue;
			g.drawLine(0, i, size, i);
			g.drawLine(i, 0, i, size);
		}
		g.setStroke(s);
	}

	public void drawCellOverlay(Graphics g, int x, int y) {
		if (cell.offset<0) x-=cell.offset;
		cell.applyClip(g, x, y);
		g.drawImage(patternImage, x, y, x+cell.a, y+cell.h, cell.x, cell.y, cell.x+cell.a, cell.y+cell.h, null);
		if (cell.offset>0) {
			g.drawImage(patternImage, x+cell.a, y, x+cell.a+cell.offset, y+cell.h, cell.x+cell.a, cell.y, cell.x+cell.a+cell.offset, cell.y+cell.h, null);
		}
		if (cell.offset<0) {
			g.drawImage(patternImage, x+cell.offset, y, x, y+cell.h, cell.x+cell.offset, cell.y, cell.x, cell.y+cell.h, null);
		}
		g.setClip(null);
		drawCell(g, Color.black, x, y, cell.a-1, cell.h-1, cell.offset);
	}
	
	public void removePeriodicity() {
		cell.applyAntiClip(patternGraphics, cell.x, cell.y);
		fill(patternGraphics, Color.white);
		patternGraphics.setClip(null);
	}
	public void restorePeriodicity() {
		if (cell.offset>0) {
			cell.applyAntiClip(patternGraphics, cell.x, cell.y);
			patternGraphics.drawImage(patternImage, cell.x, cell.y, cell.x+cell.offset, cell.y+cell.h, cell.x+cell.a, cell.y, cell.x+cell.a+cell.offset, cell.y+cell.h, null);
			patternGraphics.setClip(null);
		}
		if (cell.offset<0) {
			cell.applyAntiClip(patternGraphics, cell.x, cell.y);
			patternGraphics.drawImage(patternImage, cell.x+cell.a+cell.offset, cell.y, cell.x+cell.a, cell.y+cell.h, cell.x+cell.offset, cell.y, cell.x, cell.y+cell.h, null);
			patternGraphics.setClip(null);
		}
		
		for (int j=cell.y,i0=cell.x; j<size; j+=cell.h,i0+=cell.offset) {
			for (int i=i0; i<size; i+=cell.a) {
				if (i==i0&&j==cell.y) continue;
				patternGraphics.drawImage(patternImage, i, j, i+cell.a, j+cell.h, cell.x, cell.y, cell.x+cell.a, cell.y+cell.h, null);
			}
			for (int i=i0-cell.a; i>-cell.a; i-=cell.a) {
				patternGraphics.drawImage(patternImage, i, j, i+cell.a, j+cell.h, cell.x, cell.y, cell.x+cell.a, cell.y+cell.h, null);
			}
		}
		for (int j=cell.y-cell.h,i0=cell.x-cell.offset; j>-cell.h; j-=cell.h,i0-=cell.offset) {
			for (int i=i0; i<size; i+=cell.a) {
				patternGraphics.drawImage(patternImage, i, j, i+cell.a, j+cell.h, cell.x, cell.y, cell.x+cell.a, cell.y+cell.h, null);
			}
			for (int i=i0-cell.a; i>-cell.a; i-=cell.a) {
				patternGraphics.drawImage(patternImage, i, j, i+cell.a, j+cell.h, cell.x, cell.y, cell.x+cell.a, cell.y+cell.h, null);
			}
		}
	}
	
	public void paint(Graphics g) {
		if (drawSystem.upLayerImage==null) drawSystem.createUpLayerImage(true, periodicity);
		if (placingMask!=0) {
			fill(drawSystem.bufferGraphics, getBackground());
			drawSystem.bufferGraphics.setClip(mask.shape);
			drawSystem.bufferGraphics.drawImage(patternImage, 0, 0, null);
			drawSystem.bufferGraphics.drawImage(drawSystem.upLayerImage, 0, 0, null);
			drawSystem.bufferGraphics.setClip(null);
			if (periodicity) drawCell(drawSystem.bufferGraphics);
		}
		else {
			drawSystem.bufferGraphics.setClip(mask.shape);
			drawSystem.bufferGraphics.drawImage(patternImage, 0, 0, null);
			if (drawingTool!=0) drawTool(drawSystem.bufferGraphics, toolFirstPointX, toolFirstPointY, toolSecondPointX, toolSecondPointY); 
			drawSystem.bufferGraphics.setClip(null);
			drawSystem.bufferGraphics.drawImage(drawSystem.upLayerImage, 0, 0, null);
			if (placingCell!=0) drawCell(drawSystem.bufferGraphics);
		}
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(drawSystem.bufferImage, (getWidth()-size)/2, (getHeight()-size)/2, null);
	}
	
	public BufferedImage getImage() {
		fill(drawSystem.bufferGraphics2, Color.white);
		drawSystem.bufferGraphics2.setClip(mask.shape);
		drawSystem.bufferGraphics2.drawImage(patternImage, 0, 0, null);
		drawSystem.bufferGraphics2.setClip(null);
		return drawSystem.bufferImage2;
	}
	

}