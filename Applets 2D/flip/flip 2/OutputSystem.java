import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/* EscherFFT - FftSystem.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 29 mai 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class OutputSystem {
	public  Vector outputCanvas;
	public  OutputCanvas currentCanvas;
	public  OutputTools tools;
	public  int outputMaskRadius;
	public  boolean outputMask;
	public  BufferedImage imageBuffer;
	public  final ComplexImageSystem complexImageSystem;
	public  JTabbedPane tabbedPane;
	public  MainPane mainPane;
	public  int size;
	public ColorRef colorRef;
	
	public OutputSystem(int size, MainPane mainPane) {
		this.size=size;
		this.mainPane=mainPane;
		outputMaskRadius=size/2;
		outputMask=false;

		imageBuffer = createImage(size, Color.white);
		complexImageSystem = new ComplexImageSystem(size);
		tools = new OutputTools();
		
		outputCanvas = new Vector();

		tabbedPane = new JTabbedPane();
		//tabbedPane.setPreferredSize(new Dimension(size, size));
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Component c = tabbedPane.getSelectedComponent();
				if (c!=null) {
					if (c instanceof OutputCanvas) {
						setCurrentCanvas((OutputCanvas)c);
					}
				}
			}
		});
	}
	
	public OutputCanvas addCanvas(String name) {
		OutputCanvas c = new OutputCanvas(size, name, this);
		outputCanvas.add(c);
		tabbedPane.addTab(name, c);	
		//mainPane.printUsedMem();
		return c;
	}
	public void closeCanvas(String name) {
		OutputCanvas c = getCanvas(name);
		outputCanvas.remove(c);
		tabbedPane.remove(c);
		c.destroy();
	}
	public void showCanvas(String name) {
		int i = tabbedPane.indexOfTab(name);
		if (i==-1) return;
		tabbedPane.setSelectedIndex(i);
	}
	public OutputCanvas getCanvas(String name) {
		int i = tabbedPane.indexOfTab(name);
		if (i==-1) return null;
		return (OutputCanvas) tabbedPane.getComponentAt(i);
	}
	public void renameCanvas(String oldName, String newName) {
		OutputCanvas c = getCanvas(oldName);
		c.name = newName;
		tabbedPane.setTitleAt(tabbedPane.indexOfTab(oldName), newName);
	}

	public void setCurrentCanvas(OutputCanvas c) {
		currentCanvas = c;
		// if mode has changed, the stored image is invalid
		if (complexImageSystem.mode!=currentCanvas.complexImage.imageInMode) {
			complexImageSystem.updateOutput(currentCanvas.complexImage);
		}
		currentCanvas.updateImageBuffer();
		currentCanvas.repaint();

		tools.quiet=true;
		int z = (int)Math.round(currentCanvas.scale*100);
		tools.zoomSlider.slider.setValue(z);
		tools.contrastSlider.setValue(currentCanvas.complexImage.contrast*100f);
		tools.quiet=false;
	}
	
	public static BufferedImage createImage(int size, Color c) {
		BufferedImage i = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics g = i.createGraphics(); 
		g.setColor(c);
		g.fillRect(0, 0, size, size);
		return i;
	}
	
	public void destroy() {
		imageBuffer.flush();
		imageBuffer = null;
		for (int i=0; i<outputCanvas.size(); i++) {
			((OutputCanvas)outputCanvas.get(i)).destroy();
		}
		outputCanvas.clear();
		complexImageSystem.destroy();
  }
	
	public void showMask(int radius) {
		outputMask=true;
		outputMaskRadius=radius;
		currentCanvas.updateImageBuffer();
		currentCanvas.repaint();
	}
	public void hideMask() {
		outputMask=false;
		currentCanvas.updateImageBuffer();
		currentCanvas.repaint();
	}
	
	public class OutputTools extends HVPanel.h {
		JRadioButton reImButton, reButton, imButton, magButton, phaseButton;
		SliderAndValueH zoomSlider, contrastSlider;
		JToggleButton rot180Button;
		boolean quiet = false;
		
		public OutputTools() {
			HVPanel.v p10 = new HVPanel.v("Image");
			p10.addComp(new JLabel("Zoom"));
			zoomSlider = new HVPanel.SliderAndValueH(null, null, 100, 1000, 100, 0, 100) {
				public void mouseWheelMoved(MouseWheelEvent e) {
					setValue(getValue()+e.getWheelRotation()*10);
				}
			}.to(p10);
			p10.addComp(new JLabel("Contrast"));
			contrastSlider = new HVPanel.SliderAndValueH(null, null, 100, 1000, 100, 0, 100) {
				public void mouseWheelMoved(MouseWheelEvent e) {
					setValue(getValue()+e.getWheelRotation()*10);
				}
			}.to(p10);
			p10.expand(false);
			p10.addButton(rot180Button=new JToggleButton("Rotate 180°"));
			addSubPane(p10);
			
			HVPanel.v p11 = new HVPanel.v("Show");
			reImButton = new JRadioButton("Complex");
			reButton = new JRadioButton("Real part");
			imButton = new JRadioButton("Imaginary part");
			magButton = new JRadioButton("Magnitude\u00b2");
			phaseButton = new JRadioButton("Phase");
			ButtonGroup bg = new ButtonGroup();
			bg.add(reImButton);
			bg.add(reButton);
			bg.add(imButton);
			bg.add(magButton);
			bg.add(phaseButton);
			reImButton.setSelected(true);
			p11.addButton(reImButton);
			p11.addButton(reButton);
			p11.addButton(imButton);
			p11.addButton(magButton);
			p11.addButton(phaseButton);
			addSubPane(p11);

			HVPanel.v p12 = new HVPanel.v("Color ref");
			p12.top();
			p12.addComp(colorRef=new ColorRef(mainPane.posLabel));

			addSubPane(p12);
		}
		public void actionPerformed(ActionEvent e) {
			if (quiet) return;
			if (e.getSource()==reImButton) {
				complexImageSystem.setOutputMode(currentCanvas.complexImage, ComplexColor.MODE_COMPLEX);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==reButton) {
				complexImageSystem.setOutputMode(currentCanvas.complexImage, ComplexColor.MODE_REAL_ONLY);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==imButton) {
				complexImageSystem.setOutputMode(currentCanvas.complexImage, ComplexColor.MODE_IM_ONLY);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==magButton) {
				complexImageSystem.setOutputMode(currentCanvas.complexImage, ComplexColor.MODE_MAGNITUDE);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==phaseButton) {
				complexImageSystem.setOutputMode(currentCanvas.complexImage, ComplexColor.MODE_PHASE);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==zoomSlider) {
				currentCanvas.setScale(zoomSlider.slider.getValue()/100f);
			}
			else if (e.getSource()==contrastSlider) {
				currentCanvas.complexImage.setContrast(contrastSlider.slider.getValue()/100f);
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
			else if (e.getSource()==rot180Button) {
				currentCanvas.complexImage.rot180();
				currentCanvas.updateImageBuffer();
				currentCanvas.repaint();
			}
		}
	}
	
	public static class ColorRef extends JPanel {
		final static int size = 90;
		BufferedImage colorRefImage, refImage;
		private Shape mask;
		public boolean showValue;
		public double re, im;
		
		public ColorRef(final JLabel posValueLabel) {
			createImage();
			mask = new Ellipse2D.Float(0, 0, size, size);
				
			try {
				InputStream in = getClass().getResourceAsStream("/reIm.png");
				refImage = ImageIO.read(in);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			setPreferredSize(new Dimension(size, size));
			setMaximumSize(new Dimension(size, size));
			setMinimumSize(new Dimension(0, 0));
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent e) {
					setLabelValue(e.getX(), e.getY(), posValueLabel);
				}
			});
		}
		private void createImage() {
			colorRefImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
			float[] hsb = new float[3];
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					float re = (float)i*2f/(size-1)-1f;
					float im = 1f-(float)j*2f/(size-1);
					ComplexColor.complexToHSB_correctBanding(re, im, hsb, ComplexColor.MODE_COMPLEX);
					int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
					colorRefImage.setRGB(i, j, rgb);
				}
			}
		}
		private void setLabelValue(int x, int y, JLabel label) {
			if (x<0||y<0||x>=size||y>=size) {
				label.setText(" ");
			}
			else {
				float re = (float)x*2f/(size-1)-1f;
				float im = 1f-(float)y*2f/(size-1);
				
				float a = (float)Math.sqrt(re*re+im*im);
				int phi = (int)Math.round(Math.atan2(im, re)*180f/Math.PI);
				re = Math.round(re*100f)/100f;
				im =  Math.round(im*100f)/100f;
				a =  Math.round(a*100f)/100f;
				label.setText("Re="+re+" Im="+im+" A="+a+" \u03c6="+phi+"°");
			}
		}
		public void paint(Graphics g) {
			super.paint(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setClip(mask);
			g.drawImage(colorRefImage, 0, 0, null);
			g.setClip(null);
			if (showValue) {
				double phi = Math.atan2(im, re);
				int x = (int)Math.round(Math.cos(phi)*size/2);
				int y = (int)Math.round(Math.sin(phi)*size/2);
				g.setColor(Color.black);
				g.drawLine(size/2, size/2, size/2+x, size/2-y);
			}
			else {
				g.drawImage(refImage, 0, 0, null);
			}
		}
	}
}
