
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;

public class ImgDisplay2 extends Canvas implements ActionListener {
	int offset;
	Image pattern, imgAll, offScreenImage;
	Dimension offScreenSize;
	StateMachine stateMachine;
	String desc;
	JApplet applet;
	URL baseUrl;
	Pane panel;
	String imageName;
	boolean drawPattern;
	Graphics offScreenGraphics;
	int truecx, truecy, truexoff;
	int cx=0, cy=1;
	int newpts;
	boolean quick;

	int ptx=0, pty=0, ptx2=0, pty2=0, xoff=0, yoff=0;
  int ltpx=0, ltpy=0, ltpx2=0, ltpy2=0, ltxoff=0, ltyoff=0;
  //double m, n;
  //double dist;
  //int x1, y1;
  int [] pixels;
  Dimension pxsz;
  float hue=0;
  Color color = Color.getHSBColor(hue,(float).5,(float).5);

	public ImgDisplay2(String imgName, Pane p, URL u, boolean q, int offset, JApplet a, boolean quick) {
	this.offset = offset;
	this.applet = a;
	this.baseUrl = u;
	this.panel = p;
	this.imageName = imgName;
	this.quick = quick;
	
	this.drawPattern = true;

  addMouseListener(mouseEvent);
  addMouseMotionListener(mouseMotion);
  p.setListener(this);

	try {
  	pattern = ImageIO.read(new URL(baseUrl, imageName).openStream());
	} catch (Exception e) {throw new RuntimeException(e);}
  truecx = pattern.getWidth(null);
  truecy = pattern.getHeight(null);
  truexoff = offset;
  
	stateMachine = new StateMachine(this);
	
	}

	
  private void setUpCache() {
   	Dimension d = size();

   	if ((offScreenImage == null) || (d.width != offScreenSize.width) ||  (d.height != offScreenSize.height)) {
      offScreenImage = createImage(d.width, d.height);
      offScreenSize = d;
      offScreenGraphics = offScreenImage.getGraphics();
//      System.out.println("offscreensize:  " + offScreenSize);

      int ih = pattern.getHeight(null);
      int iw = pattern.getWidth(null);
         
      imgAll = createImage(d.width*3, d.height*3);
      Graphics grAll = imgAll.getGraphics();
      
      if (grAll != null && pattern != null) {
        for(int j=0; j<d.height*3; j+=ih) {
          for(int i=-iw+((j*offset/ih)%(iw)); i<d.width*3; i+=iw) {
            grAll.drawImage(pattern, i, j, null);
          }
        }
      }
   	}
  }

  public MouseAdapter mouseEvent = new MouseAdapter() {
   	public void mouseClicked(MouseEvent e) {
   		stateMachine.currentState.onMouseClick(e.getX(), e.getY());
   	}
  };
   
  class MouseMotion extends MouseMotionAdapter {
    private int dragX, dragY;
    public int originX, originY;
    public int newxdx, newydy;
    
   	public void mouseMoved(MouseEvent e) {
   		dragX=e.getX();
   		dragY=e.getY();
   	}
   	public void mouseDragged(MouseEvent e) {
   		originX+=e.getX()-dragX;
   		originY+=e.getY()-dragY;
   		//ltxoff+=e.getX()-dragX;  //TODO
   		//ltyoff+=e.getY()-dragY;
      //x1+=e.getX()-dragX;
      //y1+=e.getY()-dragY;
      newxdx+=e.getX()-dragX;
      newydy+=e.getY()-dragY;
   		
      //System.out.println("drag "+newxdx+" "+newydy);
   		
      dragX=e.getX();
   		dragY=e.getY();
   		repaint();
   	}
  }
  public MouseMotion mouseMotion = new MouseMotion();
  
  public void actionPerformed(ActionEvent e) {
  	stateMachine.currentState.onButtonClick(((JButton)e.getSource()).getText());
  }
  
  public void showDescription(String s) {
    if (desc != s) {
      desc = s;
      try {
      	applet.getAppletContext().showDocument(new URL(baseUrl, s),"descFrame");
      } catch (Exception e) {
      	System.err.println("Unable to show web description.");
      }
    }
  }

  public final synchronized void update (Graphics g) {
    setUpCache();
    paint(offScreenGraphics);
    g.drawImage(offScreenImage, 0, 0, null);
  }
  
  public void rotateColor() {
    hue += .01; //(state%2==1) ? .0025 : .01; // rotate slower when sleeping.
    if (hue >= 1)
      hue = 0;
    color = Color.getHSBColor(hue,(float)1,(float)1);
  }
  
  public void paint(Graphics g) {
  	rotateColor();
		setUpCache();
  	stateMachine.currentState.paint(g);
  }
	
}
