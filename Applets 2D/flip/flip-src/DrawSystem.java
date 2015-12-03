import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;



/* EscherFFT - DrawSystem.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 22 mai 2006
 * 
 * nicolas.schoeni@epfl.ch
 */

public class DrawSystem {
	public  final static int defaultToolSize = 10;
	public  final static int defaultToolDark = 255;
	public  final static boolean defaultMaskRectangular = false;
	public  final static boolean defaultMaskCircular = false;
	public  final static int defaultMaskWidth = 350;
	public  final static int defaultMaskHeight = 350;
	public  final static int defaultMaskAngle = 90;
	public  final static int defaultCellA = 80;
	public  final static int defaultCellH = 60;
	public  final static int defaultCellOffset = 0;
	public  final static Color gridColor = new Color(0, 0, 0, 30);
	public  static int   gridSize;
	
	public  final static int TOOL_PEN=0; 
	public  final static int TOOL_LINE=1; 
	public  final static int TOOL_BOX=2; 
	public  final static int TOOL_FILL=3;
	
	public  int toolSize = defaultToolSize;
	public  int tool = TOOL_PEN;
	public  boolean gaussianPen=true; 
	public  int toolDark = defaultToolDark;

	public  DrawCanvas currentCanvas, lastDrawCanvas;
	public  BufferedImage upLayerImage, bufferImage, bufferImage2;
	public  Graphics2D upLayerGraphics, bufferGraphics, bufferGraphics2;

	public  DrawTools tools;
	public  Cursor penCursor;
	
	public  Vector drawCanvas;
	public  JTabbedPane tabbedPane;
	public  MainPane mainPane;
	public  int size; 
	
	public DrawSystem(int size, MainPane mainPane) {
		this.size=size;
		this.mainPane=mainPane;
		gridSize = size/8;
		
		try {
			Image penCursorImage = ImageIO.read(getClass().getResource("/penCursor.png"));
			
			penCursor = Toolkit.getDefaultToolkit().createCustomCursor(penCursorImage, new Point(9, 23), "pen.cur");
		} catch (IOException e) {
			System.err.println("Pen cursor not found !");
		}

		bufferImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		bufferGraphics = bufferImage.createGraphics(); 
		bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		bufferImage2 = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		bufferGraphics2 = bufferImage2.createGraphics();
		
		tools = new DrawTools();
		
		drawCanvas = new Vector();
		tabbedPane = new JTabbedPane();
		//tabbedPane.setPreferredSize(new Dimension(size, size));
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Component c = tabbedPane.getSelectedComponent();
				if (c!=null) {
					if (c instanceof DrawCanvas) {
						setCurrentCanvas((DrawCanvas)c);
					}
				}
			}
		});
	}
	
	public void destroy() {
		for (int i=0; i<drawCanvas.size(); i++) {
			((DrawCanvas)drawCanvas.get(i)).destroy();
		}
		drawCanvas.clear();
		upLayerImage.flush();
		bufferImage.flush();
		bufferImage2.flush();
		currentCanvas = null;
		lastDrawCanvas = null;
	}

	public DrawCanvas addCanvas(String name) {
		DrawCanvas c = new DrawCanvas(name, size, this);
		drawCanvas.add(c);
		
		tabbedPane.addTab(name, c);		
		//mainPane.printUsedMem();
		return c;
	}
	public void closeCanvas(String name) {
		DrawCanvas c = getCanvas(name);
		drawCanvas.remove(c);
		tabbedPane.remove(c);
		c.destroy();
	}
	public void showCanvas(String name) {
		int i = tabbedPane.indexOfTab(name);
		if (i==-1) return;
		tabbedPane.setSelectedIndex(i);
	}
	public DrawCanvas getCanvas(String name) {
		int i = tabbedPane.indexOfTab(name);
		if (i==-1) return null;
		return (DrawCanvas) tabbedPane.getComponentAt(i);
	}
	public void renameCanvas(String oldName, String newName) {
		DrawCanvas c = getCanvas(oldName);
		c.name = newName;
		tabbedPane.setTitleAt(tabbedPane.indexOfTab(oldName), newName);
	}
	
	public void setCurrentCanvas(DrawCanvas drawCanvas) {
		if (currentCanvas!=null) {
			if (currentCanvas.placingCell!=0) currentCanvas.stopPlaceCell(); 
			if (currentCanvas.placingMask!=0) currentCanvas.stopPlaceMask(); 
		}
		currentCanvas = drawCanvas;
		if (!tools.quiet) {
			tools.quiet=true;
			tools.circMaskButton.setSelected(drawCanvas.mask.circular);
			tools.rectMaskButton.setSelected(drawCanvas.mask.rectangular);
			tools.periodicButton.setSelected(currentCanvas.periodicity);
			int angle = (180-(int)Math.round(Math.atan((double)drawCanvas.mask.h/drawCanvas.mask.offset)*180d/Math.PI));
			tools.angleMaskEdit.setValue(angle);
			tools.wMaskEdit.setValue(drawCanvas.mask.w);
			tools.hMaskEdit.setValue(drawCanvas.mask.h);
			tools.cellAEdit.setValue(drawCanvas.cell.a);
			int alpha = (int)Math.round(Math.atan((double)drawCanvas.cell.h/drawCanvas.cell.offset)*180d/Math.PI);
			int b = (int)Math.round(Math.sqrt(drawCanvas.cell.offset*drawCanvas.cell.offset+drawCanvas.cell.h*drawCanvas.cell.h));
			tools.cellBEdit.setValue(b);
			tools.cellAlphaEdit.setValue(alpha);
			tools.quiet=false;
		}
		createUpLayerImage(true, currentCanvas.periodicity);
	}

	public void createUpLayerImage(boolean hasMask, boolean cellVisible) {
		if (currentCanvas==null) return;
		if (upLayerImage==null) {
			upLayerImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			upLayerGraphics = upLayerImage.createGraphics(); 
		}
		currentCanvas.createUpLayerImage(upLayerGraphics, hasMask, cellVisible);
	}
	
	public class DrawTools extends HVPanel.h {
		IntEditField toolSizeEdit, darkSizeEdit, cellAEdit, cellBEdit, cellAlphaEdit;
		IntEditField wMaskEdit, hMaskEdit, angleMaskEdit;
		JCheckBox rectMaskButton, circMaskButton, periodicButton;
		JToggleButton placeCellButton, placeMaskButton;
		JLabel posLabel;
		JButton clearButton;
		boolean quiet=false;
		JComboBox toolComboBox;
		JToggleButton flatPenButton, gaussianPenButton;
		
		public DrawTools() {
			int alpha = (int)Math.round(Math.atan((double)defaultCellH/defaultCellOffset)*180d/Math.PI);
			int b = (int)Math.round(Math.sqrt(defaultCellOffset*defaultCellOffset+defaultCellH*defaultCellH));
			cellAEdit = new HVPanel.IntSpinnerEditField("a ", null, 3, defaultCellA, 10);
			cellBEdit = new HVPanel.IntSpinnerEditField("b ", null, 3, b, 10);
			cellAlphaEdit = new HVPanel.IntSpinnerEditField("alpha ", null, 3, alpha, 1);
			circMaskButton=new JCheckBox("Circular");
			rectMaskButton=new JCheckBox("Rectangular");
			circMaskButton.setSelected(defaultMaskCircular);
			rectMaskButton.setSelected(defaultMaskRectangular);
			wMaskEdit=new HVPanel.IntSpinnerEditField("Width ", null, 3, defaultMaskWidth, 10);
			hMaskEdit=new HVPanel.IntSpinnerEditField("Height ", null, 3, defaultMaskHeight, 10);
			angleMaskEdit=new HVPanel.IntSpinnerEditField("Angle ", null, 3, defaultMaskAngle, 1);
			placeMaskButton=new JToggleButton("Place mask");
			
			HVPanel.v p3 = new HVPanel.v("Drawing");
			p3.top();
			
			HVPanel.h p4 = new HVPanel.h();
			HVPanel.v p41 = new HVPanel.v();
			p41.expand(false);
			HVPanel.h p411 = new HVPanel.h();
			p411.expand(true);
			ImageIcon im1 = new ImageIcon(getClass().getResource("/flat_pen.png"));			
			ImageIcon im2 = new ImageIcon(getClass().getResource("/gaussian_pen.png"));			


						

			flatPenButton=new SpecialButton(im1);
			gaussianPenButton=new SpecialButton(im2);
			p411.addButtonGroupped(gaussianPenButton);
			p411.addButtonGroupped(flatPenButton);
			
			flatPenButton.setSelected(!gaussianPen);
			gaussianPenButton.setSelected(gaussianPen);
			p41.addSubPane(p411);
			//p41.putExtraSpace();
			HVPanel.v p412 = new HVPanel.v();
			toolSizeEdit = new HVPanel.IntSpinnerEditField("Pen size ", null, 2, defaultToolSize, 1).to(p412);
			toolSizeEdit.setMinimum(1);
			darkSizeEdit = new HVPanel.IntSpinnerEditField("Darkness ", null, 2, defaultToolDark*100/255, 10).to(p412);
			darkSizeEdit.setMinimum(0);
			darkSizeEdit.setMaximum(100);
			p41.addSubPane(p412);
			p4.addSubPane(p41);
			p3.addSubPane(p4);

			//p3.putExtraSpace();

			Object[] oo = new Object[] {new Integer(TOOL_PEN), new Integer(TOOL_LINE), new Integer(TOOL_BOX), new Integer(TOOL_FILL)};
			toolComboBox = new JComboBox(oo);
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			renderer.setPreferredSize(new Dimension(60, 60));
			toolComboBox.setRenderer(renderer);
			toolComboBox.setPreferredSize(new Dimension(60, 60));
			
			toolComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tool = ((Integer)toolComboBox.getSelectedItem()).intValue();
				}
			});

			posLabel = new JLabel(" ");
			posLabel.setPreferredSize(new Dimension(120, 18));
			p41.addComp(posLabel);

			//p3.putExtraSpace(1);
			
			
			HVPanel.h p5 = new HVPanel.h();
			p5.expand(false);
			p3.addSubPane(p5);
			//p3.putExtraSpace(2);
			HVPanel.h p51 = new HVPanel.h();
			p51.addButton(clearButton=new JButton("Clear"));
			p3.addSubPane(p51);

			periodicButton=new JCheckBox("Periodicity");
			placeCellButton=new JToggleButton("Place cell");
			
//			p3.putExtraSpace(1);
//			p3.addButton(periodicButton);
//			p3.addButton(placeCellButton);

			addSubPane(p3);
		}
		
		class SpecialButton extends JToggleButton {
			public SpecialButton(ImageIcon image) {
				super(image);
				//setBorder(new TitledBorder(""));
			}
			public void paint(Graphics g) {
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
				Icon icon = getIcon();
				icon.paintIcon(this, g, (getWidth()-icon.getIconWidth())/2, (getHeight()-icon.getIconHeight())/2);
				if (isSelected()) {
					//getBorder().paintBorder(this, g, 2, 2, getWidth()-5, getHeight()-5);
					g.setColor(Color.black);
					g.drawRect(2, 2, getWidth()-5, getHeight()-5);
				}
			}
		}
		
		class ComboBoxRenderer extends JPanel implements ListCellRenderer {
			int index;
			boolean selected;

			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				if (index==-1) {
					g.setColor(Color.white);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.black);
					Stroke s = g2d.getStroke();
					g2d.setStroke(new BasicStroke(toolSize));
					switch (toolComboBox.getSelectedIndex()) {
					case 0:
						g.fillOval(getWidth()/2-toolSize/2, getHeight()/2-toolSize/2, toolSize, toolSize);				
						break;
					case 1:
						g.drawLine(getWidth()/2-15, getHeight()/2+5, getWidth()/2+15, getHeight()/2-5);
						break;
					case 2:
						g.drawRect(getWidth()/2-15, getHeight()/2-10, 30, 20);
						break;
					case 3:
						g.fillRect(getWidth()/2-15, getHeight()/2-10, 30, 20);
						break;
					}
					g2d.setStroke(s);
				}
				else {
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.black);
					switch (index) {
					case 0:
						g.fillOval(getWidth()/2-4, getHeight()/2-4, 8, 8);
						break;
					case 1:
						g.drawLine(getWidth()/2-15, getHeight()/2+5, getWidth()/2+15, getHeight()/2-5);
						break;
					case 2:
						g.drawRect(getWidth()/2-15, getHeight()/2-10, 30, 20);
						break;
					case 3:
						g.fillRect(getWidth()/2-15, getHeight()/2-10, 30, 20);
						break;
					}
				}
			}
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				this.index=index;
				this.selected = isSelected;
				if (isSelected) {
					setBackground(list.getSelectionBackground());
				} else {
					setBackground(list.getBackground());
				}
				return this;
			}
		}
		
		public void setCellParams(DrawCanvas.Cell cell, boolean updateOffset) {
			int alpha = (180-(int)Math.round(Math.atan((double)cell.h/cell.offset)*180d/Math.PI));
			int b = (int)Math.round(Math.sqrt(cell.offset*cell.offset+cell.h*cell.h));
			quiet=true;
			cellAEdit.setValue(cell.a);
			cellBEdit.setValue(b);
			if (updateOffset) cellAlphaEdit.setValue(alpha);
			quiet=false;
		}
		
		public void setMaskParams(DrawCanvas.Mask mask, boolean updateOffset) {
			int alpha = (180-(int)Math.round(Math.atan((double)mask.h/mask.offset)*180d/Math.PI));
			int h = (int)Math.round(Math.sqrt(mask.offset*mask.offset+mask.h*mask.h));
			quiet=true;
			wMaskEdit.setValue(mask.w);
			hMaskEdit.setValue(h);
			if (updateOffset) angleMaskEdit.setValue(alpha);
			quiet=false;
		}
		
		public void actionPerformed(ActionEvent e) {
			if (quiet) return;
			if (currentCanvas==null) {
				if (lastDrawCanvas!=null) {
					quiet=true;
					showCanvas(lastDrawCanvas.name);
					quiet=false;
				}
				else {
					if (e.getSource()==placeCellButton) placeCellButton.setSelected(false); 
					if (e.getSource()==placeMaskButton) placeMaskButton.setSelected(false); 
					return;
				}
			}
			super.actionPerformed(new ActionEvent(currentCanvas, 0, null));
			
			if (e.getSource()==toolSizeEdit) {
				toolSize = toolSizeEdit.getIntValue();
			}
			if (e.getSource()==flatPenButton) {
				gaussianPen = !flatPenButton.isSelected();
			}
			if (e.getSource()==gaussianPenButton) {
				gaussianPen = gaussianPenButton.isSelected();
			}
			if (e.getSource()==darkSizeEdit) {
				toolDark = darkSizeEdit.getIntValue()*255/100;
			}
			if (e.getSource()==cellAEdit) {
				currentCanvas.erase();
				currentCanvas.cell.a = cellAEdit.getIntValue();
				currentCanvas.periodicity=true;
				periodicButton.setSelected(true);
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==cellBEdit) {
				currentCanvas.erase();
				double a = (180-cellAlphaEdit.getIntValue())*Math.PI/180d;
				currentCanvas.cell.h = (int)Math.round(cellBEdit.getIntValue()*Math.sin(a));
				currentCanvas.cell.offset = (int)Math.round(cellBEdit.getIntValue()*Math.cos(a));
				currentCanvas.periodicity=true;
				periodicButton.setSelected(true);
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==cellAlphaEdit) {
				currentCanvas.erase();
				cellAlphaEdit.setValue(((cellAlphaEdit.getIntValue()%360)+360)%360);
				double a = (180-cellAlphaEdit.getIntValue())*Math.PI/180d;
				currentCanvas.cell.h = (int)Math.round(cellBEdit.getIntValue()*Math.sin(a));
				currentCanvas.cell.offset = (int)Math.round(cellBEdit.getIntValue()*Math.cos(a));
				currentCanvas.periodicity=true;
				periodicButton.setSelected(true);
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==placeCellButton) {
				if (placeCellButton.isSelected()) {
					currentCanvas.periodicity=true;
					periodicButton.setSelected(true);
					currentCanvas.beginPlaceCell();
				}
				else currentCanvas.stopPlaceCell();
			}
			if (e.getSource()==periodicButton) {
				currentCanvas.periodicity=periodicButton.isSelected();
				if (currentCanvas.periodicity) currentCanvas.restorePeriodicity();
				else currentCanvas.removePeriodicity();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==clearButton) {
				currentCanvas.erase();
				currentCanvas.repaint();
			}
			if (e.getSource()==circMaskButton) {
				currentCanvas.mask.circular = circMaskButton.isSelected();
				if (currentCanvas.mask.circular) {
					rectMaskButton.setSelected(false);
					currentCanvas.mask.rectangular=false;
				}
				currentCanvas.mask.updateShape();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==rectMaskButton) {
				currentCanvas.mask.rectangular = rectMaskButton.isSelected();
				if (currentCanvas.mask.rectangular) {
					circMaskButton.setSelected(false);
					currentCanvas.mask.circular=false;
				}
				currentCanvas.mask.updateShape();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==wMaskEdit) {
				currentCanvas.mask.w = wMaskEdit.getIntValue();
				currentCanvas.mask.updateShape();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==hMaskEdit) {
				currentCanvas.mask.h = hMaskEdit.getIntValue();
				double a = (180-angleMaskEdit.getIntValue())*Math.PI/180d;
				currentCanvas.mask.offset = (int)Math.round(currentCanvas.mask.h/Math.tan(a));
				currentCanvas.mask.updateShape();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==angleMaskEdit) {
				double a = (180-angleMaskEdit.getIntValue())*Math.PI/180d;
				currentCanvas.mask.offset = (int)Math.round(currentCanvas.mask.h/Math.tan(a));
				circMaskButton.setSelected(false);
				currentCanvas.mask.circular=false;
				rectMaskButton.setSelected(true);
				currentCanvas.mask.rectangular=true;
				currentCanvas.mask.updateShape();
				createUpLayerImage(true, currentCanvas.periodicity);
				currentCanvas.repaint();
			}
			if (e.getSource()==placeMaskButton) {
				if (placeMaskButton.isSelected()) {
					if (!currentCanvas.mask.circular && !currentCanvas.mask.rectangular) {
						currentCanvas.mask.circular=true;
						circMaskButton.setSelected(true);
						currentCanvas.mask.updateShape();
					}
					currentCanvas.beginPlaceMask();
				}
				else currentCanvas.stopPlaceMask();
			}
		}
	}
}
