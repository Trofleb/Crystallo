import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dragNdrop.CifFileDropper;
import dragNdrop.CifFileOpener;

/* EscherFFT - FftCanvas.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 29 mai 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class OutputCanvas extends JPanel implements CifFileOpener {
	public  String name;
	private OutputSystem outputSystem;
	public  ComplexImage complexImage;
	public  float scale = 1;
	private float dx, dy;
	private int cw, ch;
	private int mx, my;
	public  BufferedImage originalCellImage;
	public  int originalCellW, originalCellH, originalCellOffset;
	
	public void openFile(File f) {
		//outputSystem.mainPane.openFile(f, false);
	}

	public OutputCanvas(int size, String name, final OutputSystem outputSystem) {
		this.name=name;
		this.outputSystem=outputSystem;
		complexImage = new ComplexImage(size, outputSystem.complexImageSystem);

		setPreferredSize(new Dimension(outputSystem.size, outputSystem.size));
		new DropTarget(this, new CifFileDropper(this));
		
		setBackground(new JPanel().getBackground());
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				float s = (float)scale+scale*(e.getWheelRotation()/20f);
				int z = (int)Math.round(scale*100);
				if (s*100<outputSystem.tools.zoomSlider.slider.getMinimum()) {
					z=100;
					s=1;
				}
				if (s*100>outputSystem.tools.zoomSlider.slider.getMaximum()) {
					z=outputSystem.tools.zoomSlider.slider.getMaximum();
					s=z/100f;
				}
				if (z>=outputSystem.tools.zoomSlider.slider.getMinimum() && z<=outputSystem.tools.zoomSlider.slider.getMaximum()) {						
					outputSystem.tools.zoomSlider.slider.setValue(z);
					scale = s;
					cw = (int)Math.round(outputSystem.size*scale);
					ch = (int)Math.round(outputSystem.size*scale);

					int dx2 = (outputSystem.size-cw)/2;
					int dy2 = (outputSystem.size-ch)/2;
					int px = (int)Math.round(dx*scale+dx2);
					int py = (int)Math.round(dy*scale+dy2);
					
//					if (px>0) dx = -dx2/scale;
//					if (py>0) dy = -dy2/scale;
//					if (cw+px<outputSystem.size) dx = (outputSystem.size-cw-dx2)/scale;
//					if (ch+py<outputSystem.size) dy = (outputSystem.size-ch-dy2)/scale;
					
					updateImageBuffer();
					repaint();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int x = e.getX()-(getWidth()-outputSystem.size)/2;
				int y = e.getY()-(getHeight()-outputSystem.size)/2;
				
				if (e.isControlDown() || e.isAltDown()||e.isShiftDown()||e.isMetaDown()||e.getButton()==MouseEvent.BUTTON2) {
					int a, b, d;
					a = x-mx;
					b = y-my;
					if (Math.abs(a)>Math.abs(b)) d=a;
					else d=b;
					//scale += scale*(d/40d);
					
					float s = (float)(scale + scale*(d/40d));
					int z = (int)Math.round(scale*100);
					if (s*100<outputSystem.tools.zoomSlider.slider.getMinimum()) {
						z=100;
						s=1;
					}
					if (s*100>outputSystem.tools.zoomSlider.slider.getMaximum()) {
						z=outputSystem.tools.zoomSlider.slider.getMaximum();
						s=z/100f;
					}
					if (z>=outputSystem.tools.zoomSlider.slider.getMinimum() && z<=outputSystem.tools.zoomSlider.slider.getMaximum()) {						
						outputSystem.tools.zoomSlider.slider.setValue(z);
						scale = s;
						cw = (int)Math.round(outputSystem.size*scale);
						ch = (int)Math.round(outputSystem.size*scale);

						int dx2 = (outputSystem.size-cw)/2;
						int dy2 = (outputSystem.size-ch)/2;
						int px = (int)Math.round(dx*scale+dx2);
						int py = (int)Math.round(dy*scale+dy2);
						
//						if (px>0) dx = -dx2/scale;
//						if (py>0) dy = -dy2/scale;
//						if (cw+px<outputSystem.size) dx = (outputSystem.size-cw-dx2)/scale;
//						if (ch+py<outputSystem.size) dy = (outputSystem.size-ch-dy2)/scale;
						
						updateImageBuffer();
						repaint();
					}					
				}
				else {
					dx+=(x-mx)/scale;
					dy+=(y-my)/scale;
					
					int dx2 = (outputSystem.size-cw)/2;
					int dy2 = (outputSystem.size-ch)/2;
					int px = (int)Math.round(dx*scale+dx2);
					int py = (int)Math.round(dy*scale+dy2);
					
//					if (px>0) dx = -dx2/scale;
//					if (py>0) dy = -dy2/scale;
//					if (cw+px<outputSystem.size) dx = (outputSystem.size-cw-dx2)/scale;
//					if (ch+py<outputSystem.size) dy = (outputSystem.size-ch-dy2)/scale;
					
					updateImageBuffer();
					repaint();
				}
				mx = x;
				my = y;
			}
			public void mouseMoved(MouseEvent e) {
				mx = e.getX()-(getWidth()-outputSystem.size)/2;
				my = e.getY()-(getHeight()-outputSystem.size)/2;
				
				if (mx>=0 && my>=0 && mx<outputSystem.size && my<outputSystem.size) {
					int dx2 = (outputSystem.size-cw)/2;
					int dy2 = (outputSystem.size-ch)/2;
					int px = (int)Math.round(dx*scale+dx2);
					int py = (int)Math.round(dy*scale+dy2);
					
					int lx = (int)((mx-px)/scale);
					int ly = (int)((my-py)/scale);
					lx = ((lx%outputSystem.size)+outputSystem.size)%outputSystem.size;
					ly = ((ly%outputSystem.size)+outputSystem.size)%outputSystem.size;

					outputSystem.colorRef.showValue=true;
					complexImage.setLabelValue(lx, ly, outputSystem.mainPane.posLabel, outputSystem.colorRef);
					outputSystem.colorRef.repaint();
				}
				else {
					outputSystem.colorRef.showValue=false;
					outputSystem.colorRef.repaint();
					outputSystem.mainPane.posLabel.setText("");
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			public void mouseReleased(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2) {
					scale = 1;
					cw = (int)Math.round(outputSystem.size*scale);
					ch = (int)Math.round(outputSystem.size*scale);
					dx = dy = 0;
					updateImageBuffer();
					repaint();
					
				}
			}
		});
		cw = (int)Math.round(outputSystem.size*scale);
		ch = (int)Math.round(outputSystem.size*scale);
	}
	
	public void setScale(float s) {
		scale = s;
		cw = (int)Math.round(outputSystem.size*scale);
		ch = (int)Math.round(outputSystem.size*scale);

		int dx2 = (outputSystem.size-cw)/2;
		int dy2 = (outputSystem.size-ch)/2;
		int px = (int)Math.round(dx*scale+dx2);
		int py = (int)Math.round(dy*scale+dy2);
		
		updateImageBuffer();
		repaint();
	}
	
	
	public String toString() {
		return name;
	}

	public void destroy() {
		complexImage.destroy();
		if (originalCellImage!=null) originalCellImage.flush();
		originalCellImage=null;
	}

	public void updateImageBuffer() {
		Graphics g = outputSystem.imageBuffer.getGraphics();
		
//		g.setColor(Color.black);
//		g.fillRect(0, 0, outputSystem.imageBuffer.getWidth(), outputSystem.imageBuffer.getHeight());

		if (outputSystem.outputMask) {
			g.setColor(getBackground());
			g.fillRect(0, 0, outputSystem.size, outputSystem.size);
			int d = (int)Math.round(outputSystem.outputMaskRadius*scale);
			g.setClip(new Ellipse2D.Float((outputSystem.size-d)/2, (outputSystem.size-d)/2, d, d));
		}

		int sz = Math.round(outputSystem.size*scale);
		int szms = sz-outputSystem.size;
		int px = (int)Math.round(dx*scale+(outputSystem.size-cw)/2);
		int py = (int)Math.round(dy*scale+(outputSystem.size-ch)/2);
		px = px%sz;
		py = py%sz;
		
		g.drawImage(outputSystem.currentCanvas.complexImage.image, px, py, cw+px, ch+py, 0, 0, outputSystem.size, outputSystem.size, null);
		
		if (px>0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px-sz, py, cw+px-sz, ch+py, 0, 0, outputSystem.size, outputSystem.size, null);
		if (px+szms<0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px+sz, py, cw+px+sz, ch+py, 0, 0, outputSystem.size, outputSystem.size, null);
		if (py>0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px, py-sz, cw+px, ch+py-sz, 0, 0, outputSystem.size, outputSystem.size, null);
		if (py+szms<0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px, py+sz, cw+px, ch+py+sz, 0, 0, outputSystem.size, outputSystem.size, null);

		if (px>0&&py>0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px-sz, py-sz, cw+px-sz, ch+py-sz, 0, 0, outputSystem.size, outputSystem.size, null);
		if (px>0&&py+szms<0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px-sz, py+sz, cw+px-sz, ch+py+sz, 0, 0, outputSystem.size, outputSystem.size, null);
		if (px+szms<0&&py>0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px+sz, py-sz, cw+px+sz, ch+py-sz, 0, 0, outputSystem.size, outputSystem.size, null);
		if (px+szms<0&&py+szms<0) g.drawImage(outputSystem.currentCanvas.complexImage.image, px+sz, py+sz, cw+px+sz, ch+py+sz, 0, 0, outputSystem.size, outputSystem.size, null);
		
		g.setClip(null);
	}

	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(outputSystem.imageBuffer, (getWidth()-complexImage.size)/2, (getHeight()-complexImage.size)/2, null);
	}

	public BufferedImage getImage() {
		return complexImage.image;
	}

//	public void createCellOverlay(DrawCanvas drawCanvas) {
//		if (originalCellImage!=null) originalCellImage.flush();
//		if (!drawCanvas.periodicity) {
//			originalCellImage=null;
//		}
//		else {
//			originalCellImage = DrawCanvas.createTransparentImage(drawCanvas.cell.a+drawCanvas.cell.offset, drawCanvas.cell.h);
//			drawCanvas.drawCellOverlay(originalCellImage.getGraphics(), 0, 0);
//			originalCellW = drawCanvas.cell.a;
//			originalCellH = drawCanvas.cell.h;
//			originalCellOffset = drawCanvas.cell.offset;
//		}
//	}
	
	
	public void clear() {
		complexImage.reset();
		complexImage.updateReIm2HSB();
		complexImage.updateHSB2image();
		updateImageBuffer();
		repaint();
	}
	
	
	public void loadImage(BufferedImage image) {
		complexImage.load(image);
		complexImage.updateReIm2HSB();
		complexImage.updateHSB2image();
		updateImageBuffer();
		repaint();
	}
}
