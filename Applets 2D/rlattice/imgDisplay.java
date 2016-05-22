import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.*;
import java.util.Vector;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//import org.jfree.io.IOUtils;


public
class imgDisplay extends Canvas {

	String desc="";
	JApplet apl;
	Image img = null;
	String imageName="/save_0.gif";
	String dataName=null;
	//Pane pan, pan2, master;
	Pane panel;
	//Label label;
	//Scrollbar scrbar;
	//Button b1, b2;
	//Checkbox ck1, ck2;

	//TODO: transformer �a en enum
	static final int INIT = -1;
	static final int STARTING = 0;
	static final int FINDHZSTRIP = 1;
	static final int FOUNDHZSTRIP = 2;
	static final int FINDVERTSTRIP = 3;
	static final int DOUBLECHECKVERT = 5;
	static final int FOUNDVERTSTRIP = 6;
	static final int FOUNDLATTICECELL = 8;
	static final int FOUNDLATTICE = 10;
	static final int AFTERLATTICE = 12;
	static final int ERASEPATTERN = 13;
	static final int POINTSONLY = 15;
	//static final int PICKSECOND = 17;
	//static final int FOUNDBASE = 19;
	static final int NEXTPOINT = 21;
	static final int MOREPOINT = 22;
	//static final int DONESECOND = 23;
	static final int CONNECTPOINTS = 25;
	static final int ALLLINES = 27;
	static final int MEASUREDIST = 28;
	static final int NEWPOINTS = 30;
	static final int BEFOREREPEAT = 31;
	static final int FINISHED = 256;
	//static final int DISPLAYFULL = 258;
	static final int STOPPED = -4;
	static final int ERROR = -2;
	int state = INIT;
	int next = INIT;
	int previous = INIT;
	String prevLabel;

	boolean haveinfo = false;
	int truecx=0, truecy=0, truexoff;
	int offset;
	int ptx=0, pty=0, ptx2=0, pty2=0, xoff=0, yoff=0, cx=0, cy=1;
	int ltpx=0, ltpy=0, ltpx2=0, ltpy2=0, ltxoff=0, ltyoff=0;
	double m, n;
	double dist;
	int x1, y1;
	int [] pixels;
	Dimension pxsz;
	float hue=0;
	Color color = Color.getHSBColor(hue,(float).5,(float).5);
	boolean addedSecondPanel = false, drawLattice = true, drawRecip = true;
	boolean drawPattern = true;
	//int k;

	int newx[] = new int[5000];
	int newy[] = new int[5000];
	int newpts = 0;
	double newxoff, newyoff;
	int oldnewpts = 0;
	//boolean usedpts[][] = new boolean[500][500];
	Vector usedpts;
	boolean quick;
	int ih, iw;
	int zoney;

	private JFrame frame;

	public imgDisplay(String name, Pane p, JFrame frame, boolean q, int offset, JApplet a) {
		this.offset = offset; 
		this.panel = p;
		this.frame = frame;

		addMouseListener(mouseEvent);
		addMouseMotionListener(mouseMotion);

		p.setListener(this);
		apl = a;
		quick = q;
		if(name != null)
			imageName = name;
		if (imageName.endsWith(".gif")) {
			dataName = imageName.substring(0,imageName.length() - 4) + ".data";
			System.out.println("dataName: " + dataName);
		}
		//panel.setupPanel("<html>Here is the selected periodic pattern. You can scroll it anytime by dragging the mouse.<br><br>Please click anywhere on the pattern in order to find other points which are strictly identical.",null,null);
		repaint();
	}

	public void rotateColor() {
		hue += (state%2==1) ? .0025 : .01; // rotate slower when sleeping.
		if (hue >= 1)
			hue = 0;
		color = Color.getHSBColor(hue,(float)1,(float)1);
	}

	public boolean action(Event evt, Object arg) {
		if ("Go Back".equals(arg) || "Done".equals(arg)) {
			if (state==MEASUREDIST) panel.hideD();
			if (state==ERASEPATTERN) {drawPattern=true; panel.ck1.setSelected(true); panel.ck1.setEnabled(false);}
			if (state==NEWPOINTS) {hideSecondLattice(); panel.hideKChooser();}
			if (state==BEFOREREPEAT) {panel.enableKChooser(); hideSecondLattice();}
			state = previous;
			repaint();
			return true;
		} else if ("Continue".equals(arg)) {
			state = next;
			repaint();
			return true;
		} else if ("Re-Start".equals(arg)){
			state = next;
			panel.hideKChooser();
			drawPattern = true;
			panel.ck1.setSelected(true);
			panel.ck1.setEnabled(false);
			repaint();
			return true;
		} else if ("New Pattern".equals(arg)){
			try {
				rlattice.instance.choose();
				frame.dispose();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Unable to go web page. : ");
			}
			return true;
		} else if ("Finished".equals(arg)){
			state = FINISHED;
			repaint();
			return true;
		} else if ("Stop".equals(arg)){
			next = state;
			state = STOPPED;
			prevLabel=panel.text.getText();
			panel.setupPanel("Stopped at user request","Go Back","Continue");
			repaint();
			return true;
		} 
		else if ("ck3".equals(arg)) {
			drawRecip = !drawRecip;
			repaint();
		} else if ("ck2".equals(arg)) {
			drawLattice = !drawLattice;
			repaint();
		} else if ("ck1".equals(arg)) {
			drawPattern = !drawPattern;
			repaint();
		}

		return false;
	}

	public void stop() {
		previous = state;
		next = state;
		state = STOPPED;
		panel.setupPanel("Stopped by the web browser","Go Back","Continue");
		prevLabel=panel.text.getText();
		repaint();
	}


	public void init() {

		try {
			img = ImageIO.read(rlattice.instance.open(imageName));
		} catch (Exception e) {throw new RuntimeException(e);}

		try {
			StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader(new File(dataName))));
			if (st != null) {
				while(st.nextToken() == StreamTokenizer.TT_WORD) {
					System.out.println("sval: " + st.sval);
					if (st.sval.equals("x") && 
							st.nextToken() == StreamTokenizer.TT_NUMBER)
						truecx=(int) st.nval;
					else if (st.sval.equals("y") && 
							st.nextToken() == StreamTokenizer.TT_NUMBER)
						truecy=(int) st.nval;
					else if (st.sval.equals("xoff") && 
							st.nextToken() == StreamTokenizer.TT_NUMBER)
						truexoff=(int) st.nval;
				}
			}
		} catch (java.io.IOException e) { 
		}

		truecx = img.getWidth(null);
		truecy = img.getHeight(null);
		truexoff = offset;

		if (truecx != 0 && truecy != 0) {
			haveinfo = true;
			System.out.println("have info:  " + truecx + "," + truecy + "," + 
					truexoff);
			double kk = 3626;
			double axs = (double)(kk*truecy)/((double)(-truexoff + truecx*truecy));
			double ays = (double)(kk*truexoff)/((double)(truexoff - truecx*truecy));
			double bxs = kk/((double)truexoff);
			double bys = -(((double)(truecx))/((double)truexoff));
			//        System.out.println("bxs=" + bxs + ", bys=" + bys +
			//                           ", axs=" + axs + ", ays=" + ays);
		}
	}

	public Image offScreenImage;
	public Dimension offScreenSize;
	public Graphics offScreenGraphics;

	public Image imgAll;

	public void setUpCache() {
		Graphics grAll;
		Dimension d = size();
		if ((offScreenImage == null) || (d.width != offScreenSize.width) ||  (d.height != offScreenSize.height)) {
			offScreenImage = createImage(d.width, d.height);
			offScreenSize = d;
			offScreenGraphics = offScreenImage.getGraphics();
			//       System.out.println("offscreensize:  " + offScreenSize);

			ih = img.getHeight(null);
			iw = img.getWidth(null);
			imgAll = createImage(d.width*3, d.height*3);
			grAll = imgAll.getGraphics();

			if (grAll != null && img != null) {
				for(int j=0; j < d.height*3; j += ih) {
					for(int i=-iw+((j*offset/ih)%(iw)); i < d.width*3; i += iw) {
						grAll.drawImage(img,i,j,null);
					}
				}
			}
		}
	}

	public final synchronized void update (Graphics g) {
		if (img == null)
			init();
		setUpCache();
		paint(offScreenGraphics);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	public void paint(Graphics g) {
		if (img == null)
			init();
		setUpCache();
		Dimension d = size();

		if (state == INIT) {
			showDescription("begin.html");
			frame.pack();
			next = STARTING;
		}
		if (state == STARTING) {
			//showDescription("begin.html");
			newpts = 0;
			panel.clear();
			panel.setupPanel("<html>Here is the selected periodic pattern. You can scroll it anytime by dragging the mouse.<br><br>Please click anywhere on the pattern in order to find other points which are strictly identical.",null,null);
			if (quick) {
				cx = truecx-1;
			} else {
				cx = 0;
			}
		}
		if (state == FINDHZSTRIP) {
			cx++;
			//if (ptx+cx > mouseMotion.originX+pxsz.width*2) {
			if (cx>1000) {
				//System.out.println("ptx:"+ptx+" cx:"+cx+" mouseMotion.originX:"+mouseMotion.originX+" pxsz.width:"+pxsz.width);
				state = ERROR;
				previous = STARTING;
				panel.setupPanel("Failed to find a repeating horizontal strip!","Go Back",
						null);
			} else {
				boolean failed = false;
				if (!haveinfo) {
					// check all lengths to the right
					for(int i=cx; i+ptx < pxsz.width && !failed; i++)
						if (pixels[pty*pxsz.width + i+ptx] != 
						pixels[pty*pxsz.width + ptx+i%cx])
							failed = true;
					// check all lengths to the left.
					for(int i=0; i+ptx >= 0 && !failed; i--)
						if (pixels[pty*pxsz.width + i+ptx] != 
						pixels[pty*pxsz.width + ptx+(i%cx)])
							failed = true;
				}
				if ((failed == false && !haveinfo) ||
						(haveinfo && cx == truecx)) {
					System.out.println("strip len:  " + cx + " ptx: " + ptx + " w: " + pxsz.width);
					//panel.setupPanel("Finding the same strip somewhere on the pattern.",
					//           null,"Stop");

					panel.setupPanel("Let us try to find an identical strip on the pattern.", 
							"Go Back","Continue");

					previous = STARTING;
					state = FOUNDHZSTRIP;
				}
			}
		}
		if (state == FOUNDHZSTRIP) {
			if (next != FINDVERTSTRIP) {
				previous = STARTING;
				next = FINDVERTSTRIP;
				panel.setupPanel("Let us try to find an identical strip on the pattern.", 
						"Go Back","Continue");
			}
		}
		if (state == FINDVERTSTRIP) {
			if (next != FOUNDVERTSTRIP) {
				next = FOUNDVERTSTRIP;
				//previous = FOUNDHZSTRIP;
				previous = STARTING;
				panel.setupPanel("Finding this same strip somewhere below...", 
						null,"Stop");
				if (quick) {
					cy = truecy-1;
					xoff = 0;
				} else {
					cy = 1;
					xoff = 0;
				}
			}
			//if (ptx+xoff >= pxsz.width) {
			if (xoff >pxsz.width) {
				cy++;
				xoff = 0;
			}
			//if (pty+cy >= pxsz.height) {
			if (cy>1000) {
				previous=STARTING;
				state=ERROR;
				panel.setupPanel("Failed to find the horizontal strip down and/or right in the image!","Go Back", null);
			} else {
				boolean failed = false;
				// check one length to the right.
				if (!haveinfo) {
					for(int b=xoff; (b == xoff) || 
							(ptx+xoff < pxsz.width && pty+cy < pxsz.height && 
									(xoff+1)%30 != 0 && failed == true); xoff++) {
						if (failed == true)
							failed = false;
						for(int i=0; i+ptx+xoff < pxsz.width && i < cx && !failed; i++)
							if (pixels[(pty+cy)*pxsz.width + i+ptx+xoff] != 
							pixels[pty*pxsz.width + ptx+i%cx])
								failed = true;
						// check one length to the left.
						for(int i=0; i+ptx+xoff >= 0 && i > -cx && !failed; i--)
							if (pixels[(pty+cy)*pxsz.width + i+ptx+xoff] != 
							pixels[pty*pxsz.width + ptx+(i%cx)])
								failed = true;
					}
				} else {
					xoff+= 40;
				}
				if ((failed == false && !haveinfo) ||
						(haveinfo && cy == truecy)) {
					if (haveinfo) {
						xoff = truexoff;
						state = FOUNDVERTSTRIP;
					} else {
						xoff--;
						state = DOUBLECHECKVERT;
					}
				}
			}
		}
		if (state == DOUBLECHECKVERT) {
			for(int i=2; pty+i*cy < pxsz.height && state==DOUBLECHECKVERT; i++)
				for(int j=0; j < cx && 
						state==DOUBLECHECKVERT; j++)
					if (pixels[(pty+i*cy)*pxsz.width + ptx+(j+(xoff*i))%cx] != 
					pixels[pty*pxsz.width + ptx+j%cx]) {
						xoff++;
						state = FINDVERTSTRIP;
					}
			for(int i=-1; pty+i*cy > 0 && state==DOUBLECHECKVERT; i--)
				for(int j=0; j < cx && 
						state==DOUBLECHECKVERT; j++)
					if (pixels[(pty+i*cy)*pxsz.width + ptx+(j+(xoff*i))%cx] != 
					pixels[pty*pxsz.width + ptx+j%cx]) {
						xoff++;
						state = FINDVERTSTRIP;
					}
			if (state == DOUBLECHECKVERT) {
				System.out.println("strip len:  " + cx + " x,y: " + ptx + "," + pty +
						" xoff: " + xoff + " cy: " + cy);
				state=FOUNDVERTSTRIP;
			}
		}
		if (state == FOUNDVERTSTRIP) {
			if (next != FOUNDLATTICECELL) {
				next = FOUNDLATTICECELL;
				previous = FOUNDHZSTRIP;
				panel.setupPanel("The two strips relates two pairs of identical points which allows us to find a unit cell of the pattern.",
						"Go Back", "Continue");
			}
		}
		if (state == FOUNDLATTICECELL) {
			if (next != FOUNDLATTICE) {
				next = FOUNDLATTICE;
				previous = FOUNDVERTSTRIP;
				panel.setupPanel("This is a possible unit cell.",
						"Go Back", "Continue");
			}
		}
		if (state == FOUNDLATTICE) {
			if (next != AFTERLATTICE) {
				next = AFTERLATTICE;
				previous = FOUNDLATTICECELL;
				panel.setupPanel("And all the equivalent unit cells generated by translation.",
						"Go Back", "Continue");
			}
		}
		if (state == AFTERLATTICE) {
			if (next != ERASEPATTERN) {
				next = ERASEPATTERN;
				previous = FOUNDLATTICE;
				panel.setupPanel("Let us remove the background pattern and keep only the unit cells.",
						"Go Back", "Continue");
			}
		}
		if (state == ERASEPATTERN) {
			if (next != POINTSONLY) {

				drawPattern=false;
				panel.setCheckBoxStates(true, false, false);
				panel.ck1.setSelected(false);

				ltyoff = (pty+mouseMotion.originY)%cy;
				ltxoff = (ptx+mouseMotion.originX)%cx-zoney*xoff;  //TODO spacetrick 
				// these are adjusted when scrolling inside MouseMotionListener


				//         System.out.println("ltxoff, ltyoff: " + ltxoff + "," + ltyoff);
				next = POINTSONLY;
				previous = AFTERLATTICE;
				panel.setupPanel("In a further abstraction, let us keep only the nodes of the lattice.",
						"Go Back", "Continue");
			}
		}
		if (state == POINTSONLY) {
			if (next != NEXTPOINT) {
				showDescription("calculate.html");
				if (addedSecondPanel == true) {
					showDescription("calculate.html");
					panel.clear();
				}
				addedSecondPanel=false;
				newpts = 0;
				usedpts = new Vector(5, 5);
				//for(int i=0;i<500;i++)
				//  for(int j=0;j<500;j++)
				//    usedpts[i][j] = false;
				oldnewpts = 0;
				//next = PICKSECOND;
				next = NEXTPOINT;
				previous = ERASEPATTERN;
				panel.setupPanel("Select one of the lattice points by clicking on it.",
						"Go Back", null);
			}
		}
		if (state == NEXTPOINT) {
			if (next != CONNECTPOINTS) {
				if (!addedSecondPanel && newpts > 0) {
					panel.setCheckBoxStates(true, true, true);
					addedSecondPanel=true;
				}
				next = CONNECTPOINTS;
				previous = POINTSONLY;
				panel.setupPanel("<html>This will be the origin of our reference system.<br><br>Select a second lattice point in order to define a new line.", "Go Back", null);
			}
		}
		if (state == MOREPOINT) {
			if (next != CONNECTPOINTS) {
				if (!addedSecondPanel && newpts > 0) {
					panel.setCheckBoxStates(true, true, true);
					addedSecondPanel=true;
				}
				next = CONNECTPOINTS;
				previous = BEFOREREPEAT;
				panel.setupPanel("Select another lattice point in order to define a new line.", "Go Back", null);
			}
		}
		if (state == CONNECTPOINTS) {
			if (next != ALLLINES) {
				next = ALLLINES;
				if (newpts==0)
					previous = NEXTPOINT;
				else
					previous = MOREPOINT;
				panel.setupPanel("<html>Here is the lattice line defined by the point and the origin. <br><br> Now let us draw all the possible parallel lines so that all the lattice points are included.",
						"Go Back", "Continue");
			}
		}
		if (state == ALLLINES) {
			if (next != MEASUREDIST) {
				next = MEASUREDIST;
				previous = CONNECTPOINTS;
				panel.setupPanel("And measure the distance between them...",
						"Go Back", "Continue");
			}
		}
		if (state == MEASUREDIST) {
			if (next != NEWPOINTS) {
				next = NEWPOINTS;
				previous = ALLLINES;
				panel.setupPanel("The distance D between two consecutive lines is " + ((int) dist) + 
						" pixels.",
						"Go Back", "Continue");
			}
		}
		if (state == NEWPOINTS) {
			if (next != BEFOREREPEAT) {
				oldnewpts = newpts;
				setSecondLattice(newpts, d);
				next = BEFOREREPEAT;
				previous = MEASUREDIST;
				//previous = POINTSONLY;
				if (oldnewpts == 0) {
					panel.putKChooser();
					showDescription("choose-K.html");
				}
				panel.setupPanel("From our origin, let us draw a series of points equally spaced by K/D.",
						"Go Back", "Continue");
			}
		}
		if (state < BEFOREREPEAT) {
			panel.ck2.setSelected(true);
			panel.ck3.setSelected(true);
			panel.ck2.setEnabled(false);
			panel.ck3.setEnabled(false);
			drawRecip = true;
			drawLattice = true;
		}

		if (state == BEFOREREPEAT) {
			//if (!addedSecondPanel) {
			panel.disableKChooser();
			panel.setCheckBoxStates(true, true, true);
			addedSecondPanel=true;
			//}
			if (next != MOREPOINT) {
				next = MOREPOINT;
				//previous = POINTSONLY;
				previous = NEWPOINTS;
				panel.setupPanel("Let us repeat this process for other series of parallel lines.",
						"Go Back", "Continue");
				panel.showFinish(true);
				showDescription("continuing.html");
			}
		}
		if (state != BEFOREREPEAT) panel.showFinish(false);
		/*
     if (state == FINISHED) {
       if (next != DISPLAYFULL) {
         next = DISPLAYFULL;
         previous = NEXTPOINT;
         panel.setupPanel("This is the series of lattice points that you have created.",
                    "Go Back", "Continue");
       }
     }*/
		if (state == FINISHED) {
			if (next != STARTING) {
				next = STARTING;
				previous = BEFOREREPEAT;
				panel.setupPanel("Here is the complete reciprocal lattice.",
						"Go Back", "Re-Start");
				showDescription("final.html");
			}
		}
		// Draw as much of the lattice as we know about it.
		g.setColor(Color.white);
		g.fillRect(0,0,d.width,d.height);

		if (drawPattern) {
			//if (state < ERASEPATTERN) { 
			//System.out.println(mouseMotion.originX+" "+((mouseMotion.originX%d.width))+" "+mouseMotion.originY+" "+((mouseMotion.originY%d.height)));
			g.drawImage(imgAll, ((mouseMotion.originX)-offset*(mouseMotion.originY/ih))%iw-iw,((mouseMotion.originY)%ih-ih),null);
		}
		if (state >= FOUNDLATTICE && state <= ERASEPATTERN) {
			g.setColor(Color.black);
			//for(int j=pty+(pxsz.height/cy)*cy+cy; j>0; j -= cy) {
			// for(int i=ptx+(pxsz.width/cx)*cx+xoff*(j-pty)/cy+cx; i>0; i -= cx) {
			for(int j=(pty+mouseMotion.originY)%cy; j<(pty+mouseMotion.originY)%cy+d.height+2*cy; j+=cy) {
				for(int i=(ptx+mouseMotion.originX)%cx+((xoff*(j-(pty+mouseMotion.originY))/cy)%cx)-cx; i<(ptx+mouseMotion.originX)%cx+d.width+2*cx; i+=cx) {

					//g.fillOval(i-4,j-4,8,8);


					g.drawLine(i,j,i-cx-xoff,j);
					g.drawLine(i,j,i-xoff,j-cy);
					g.drawLine(i-cx-xoff,j-cy,i-cx,j);
					g.drawLine(i-cx-xoff,j-cy,i-xoff,j-cy);
				}
			}
			g.setColor(color);
		}

		/*     
     (pty+mouseMotion.originY)%cy+j*cy
     ((ptx+mouseMotion.originX)%cx+((xoff*(j-(pty+mouseMotion.originY))/cy)%cx)-cx)+i*cx

     (ltyoff%cy)+j*cy
     -cx((ltxoff+off)%cx)+i*cx

     int off = ((xoff*(int)Math.ceil(((double)j-ltyoff)/cy)));
     if (ltyoff<0 &&ltyoff%cy!=0) off-=xoff;

		 */     

		if (state > ERASEPATTERN && drawLattice) {


			if (state<FINISHED) {
				g.setColor(Color.blue);
				for (int i=0; i<usedpts.size(); i++) {
					Point p = ((Point)usedpts.get(i));
					if (p.x==ltpx2 && p.y==ltpy2 && state>=CONNECTPOINTS) continue;
					g.drawLine(ltxoff+cx*(p.x)+p.y*xoff-3, ltyoff+cy*p.y-3, ltxoff+cx*(p.x)+p.y*xoff+3, ltyoff+cy*p.y+3);
					g.drawLine(ltxoff+cx*(p.x)+p.y*xoff-3, ltyoff+cy*p.y+3, ltxoff+cx*(p.x)+p.y*xoff+3, ltyoff+cy*p.y-3);
					//g.drawOval(ltxoff+cx*(p.x)+p.y*xoff-4, ltyoff+cy*p.y-4,4,4);
				}
			}

			//repaint();
			//g.fillOval(ltxoff-3+cx*(ltpx2)+ltpy2*xoff, ltyoff+cy*ltpy2-3,6,6);


			g.setColor(Color.black);


			for(int j=(pty+mouseMotion.originY)%cy; j<(pty+mouseMotion.originY)%cy+d.height+2*cy; j+=cy) {
				for(int i=(ptx+mouseMotion.originX)%cx+((xoff*(j-(pty+mouseMotion.originY))/cy)%cx)-cx; i<(ptx+mouseMotion.originX)%cx+d.width+2*cx; i+=cx) {
					g.fillOval(i-2,j-2,4,4);



					//ltxoff-3+cx*(ltpx)+(ltpy)*xoff
					//ltyoff+cy*ltpy


					//g.drawString((int)Math.floor((double)(i-ltxoff-((int)Math.floor((double)(j-ltyoff)/cy))*xoff)/cx)+" "+(int)Math.floor((double)(j-ltyoff)/cy), i+4, j+4);
					//g.drawString((((i-(ptx+mouseMotion.originX))/cx))+" "+((j-(pty+mouseMotion.originY))/cy), i+4, j+4);
				}
			}

			/*      
       //System.out.println((int)((-(pty+mouseMotion.originY-cy))/cy));
       //System.out.println(offset+" "+xoff);
       for(int j=0; j < pxsz.height+cy; j+=cy) {
         for(int i=-cx; i < pxsz.width+2*cx; i+=cx) {
           //if (usedpts[i/cx+10][j/cy] == true) {

//         	if (usedpts.contains(new Point(i/cx, j/cy))) {
//           	 g.setColor(Color.blue);
           	 //g.fillOval(ltxoff-3+cx*(ltpx2)+ltpy2*xoff, ltyoff+cy*ltpy2-3,6,6);
//             g.drawOval(i+(ltxoff%cx)+xoff*(j/cy)-2,j+(ltyoff%cy)-2,4,4);
//           } else {
             g.setColor(Color.black);
//             g.fillOval(i+(ltxoff%cx)+((xoff*(j/cy)))-2,j+(ltyoff%cy)-2,4,4);

             //if (i==0) System.out.println(ltxoff+" "+xoff+" "+j+" "+ltyoff+" "+cy+" "+(ltxoff+((xoff*((j-ltyoff)/cy)))));

            	//System.out.println(i+" "+j);

             int off = ((xoff*(int)Math.ceil(((double)j-ltyoff)/cy)));

             if (ltyoff<0 &&ltyoff%cy!=0) off-=xoff;
             //off-=xoff;

             //if (j==0 && i==-cx) System.out.println(off+" "+ltyoff);

             //if (ltyoff%cy==0) off+=xoff;

             //g.fillOval(i+((ltxoff+off)%cx)-2,j+(ltyoff%cy)-2,4,4);


             int n =(int)((-(pty+mouseMotion.originY-cy))/cy);
             int off2=(xoff*(((j/cy)-1)+((n))))%cx;
             //int off2=xoff*(((j/cy)-1)+(0));

             g.fillOval(i+(ptx+mouseMotion.originX)%cx+off2-2,j+(ltyoff%cy)-2,4,4);

//           }
         }
       }
      	//System.out.println();
			 */       


		}

		if (state == FINISHED) {
			// ax = cx; ay=0; bx=xoff; by=cy;
			double axs = (double)(panel.k*cy)/((double)(-xoff + cx*cy));
			double ays = (double)(panel.k*xoff)/((double)(xoff - cx*cy));

			int basex = ltxoff+cx*(ltpx)+ltpy*xoff;
			int basey = ltyoff+cy*ltpy;

			if (drawRecip) {
				g.setColor(Color.green.darker());


				System.out.println(((int)(-basex/axs)-1)+" "+(((int)(pxsz.width/axs+1))));

				//for(int i=(int)(-basex/axs)-1; i < ((int)(pxsz.width/axs+1)); i++) {
				for(int i=(int)(-basex/axs)-1; i < (int)(-basex/axs)-1+50; i++) {
					for(int j=(int)(-basey*cy/panel.k-1-i*ays*cy/panel.k); 
							j < (pxsz.height-basey)*cy/panel.k+1-i*ays*cy/panel.k;
							j++) {
						g.fillOval((int)(basex+i*axs-3),(int)(basey+j*panel.k/cy+i*ays-3),6,6);
					}
				}

				g.setColor(Color.blue);
				drawArrow(g,basex, basey, basex+((int)(axs)), basey+((int)(ays)));
				g.drawString("a", basex+((int)axs)+6, basey+((int)(ays)));
				g.drawString("*", basex+((int)axs)+12, basey+((int)(ays))-6);
				drawArrow(g,basex, basey, basex, basey-((int) (panel.k/cy)));
				g.drawString("b", basex+6, basey-((int) (panel.k/cy)));
				g.drawString("*", basex+12, basey-((int) (panel.k/cy))-6);
			}
			if (drawLattice) {
				g.setColor(Color.blue);
				drawArrow(g,basex, basey, basex+cx, basey);
				g.drawString("a", basex+cx+6, basey);
				drawArrow(g,basex, basey, basex-xoff, basey-cy);
				g.drawString("b", basex-xoff+6, basey-cy);
			}
		}


		if (state > POINTSONLY) {
			g.setColor(Color.red);					
			g.fillOval(ltxoff-3+cx*(ltpx)+(ltpy)*xoff, ltyoff+cy*ltpy-3,6,6);
			//g.drawString("o:"+ltpx+" "+ltpy, ltxoff+5+cx*(ltpx)+(ltpy)*xoff, ltyoff+cy*ltpy+14);
		}
		if (state >= CONNECTPOINTS && state < FINISHED && drawLattice) {
			g.setColor(Color.blue);
			g.fillOval(ltxoff-3+cx*(ltpx2)+ltpy2*xoff, ltyoff+cy*ltpy2-3,6,6);
		}
		if (state >= CONNECTPOINTS && state < BEFOREREPEAT) {
			// conect the two user chosen points with a line.
			g.setColor(Color.blue);
			x1=ltxoff+cx*(ltpx)+ltpy*xoff;
			int x2=ltxoff+cx*(ltpx2)+ltpy2*xoff;
			y1=ltyoff+cy*ltpy;
			int y2=ltyoff+cy*ltpy2;
			if (x1 != x2) {
				m = ((double)(y1-y2))/((double)(x1-x2));
				g.drawLine(0,(int) (m*(0-x1)+y1), d.width, (int)(m*(d.width-x1)+y1));
			} else {
				m=Float.POSITIVE_INFINITY;
				g.drawLine(x1, 0, x1, d.height);
			}
			//usedpts[ltpx2+10][ltpy2] = true;
			usedpts.add(new Point(ltpx2, ltpy2));
		}
		if (state >= ALLLINES && state < NEWPOINTS) {
			// draw a series of parallel lines.
			dist = d.width;
			g.setColor(Color.blue);
			//for(int j=-5; j<d.height/cy+1; j++)
			// for(int i=-10; i<d.width/cx+1; i++) {
			for(int j=-1; j<d.height/cy+2; j++)
				for(int i=-1; i<d.width/cx+2; i++) {
					int x3=((ltxoff)+j*xoff-(ltyoff/cy)*xoff)%cx+cx*i;
					int y3=(ltyoff%cy)+cy*j;
					double newdist;
					if (m == Float.POSITIVE_INFINITY) {
						g.drawLine(x3, 0, x3, d.height);
						newdist = Math.abs(x3-x1);
					} else {
						g.drawLine(0,(int) (m*(0-x3)+y3), d.width, 
								(int)(m*(d.width-x3)+y3));
						newdist = (((double)(-m*(double)(x3)+y3+m*(double)x1-y1))/
								(Math.sqrt(1+m*m)));
					}
					if (Math.abs(dist) > Math.abs(newdist) && newdist > 0.000001)
						dist = newdist;
				}
		}


		if (state == MEASUREDIST)
			panel.setD((int)dist);


		if (state >= MEASUREDIST && state < BEFOREREPEAT) {
			// draw a changing-colored line between parallel lines.
			if (m != 0)
				n=-1/m;
			else
				n=Double.POSITIVE_INFINITY;
			rotateColor();
			g.setColor(color);
			newxoff = (int) (dist*Math.cos(Math.atan(n)));
			newyoff = (int) (dist*Math.sin(Math.atan(n)));
			g.drawLine(x1, y1, (int)Math.round(newxoff+x1), (int)Math.round(newyoff+y1));
			// real new offsets = k/dist, k = cy*cx here;
			if (state > MEASUREDIST && oldnewpts == 0 && newpts == 0) {
				panel.setK(cx*cy*2/3);
			}
			newxoff = (int)(Math.cos(Math.atan(n))*((double)(panel.k)/dist));
			newyoff = (int)(Math.sin(Math.atan(n))*((double)(panel.k)/dist));
		}
		if (newpts > 0  && drawRecip && state < FINISHED) {
			g.setColor(Color.green.darker());
			for(int i=0; i<oldnewpts; i++) {
				g.fillOval(newx[i]+mouseMotion.originX-3,newy[i]+mouseMotion.originY-3,6,6);
			}
			if (state == NEWPOINTS)
				g.setColor(color);
			for(int i=oldnewpts; i<newpts; i++) {
				g.fillOval(newx[i]+mouseMotion.newxdx-3,newy[i]+mouseMotion.newydy-3,6,6);
			}
		}
		if (state < ERASEPATTERN) {
			rotateColor();
			g.setColor(color);
			g.setXORMode(Color.white);
			// g.setPaintMode();
			if (state >= FINDHZSTRIP) {
				g.fillRect(ptx+mouseMotion.originX,pty-1+mouseMotion.originY,cx,3); //TODO
			}
			if (state >= FINDVERTSTRIP)
				g.fillRect(ptx+mouseMotion.originX+xoff,pty+mouseMotion.originY+cy-1,cx,3);
			if (state >= FOUNDLATTICECELL) {
				int ptxs[] = new int[4], ptys[] = new int [4];
				ptxs[0] = ptx-1+mouseMotion.originX;
				ptys[0] = pty-1+mouseMotion.originY;
				ptxs[1] = ptx+1+mouseMotion.originX;
				ptys[1] = pty-1+mouseMotion.originY;
				ptxs[2] = ptx+xoff+1+mouseMotion.originX;
				ptys[2] = pty+cy+1+mouseMotion.originY;
				ptxs[3] = ptx+xoff-1+mouseMotion.originX;
				ptys[3] = pty+cy+1+mouseMotion.originY;
				g.fillPolygon(ptxs, ptys, 4);
				g.translate(cx,0);
				g.fillPolygon(ptxs, ptys, 4);
				g.translate(-cx,0);
			}
			g.setPaintMode();
		}
		if ((state < ERASEPATTERN && state > 0) || 
				(state >= MEASUREDIST && state < BEFOREREPEAT)) {
			// animate by re-painting.
			if (state%2 == 0)
				try {Thread.sleep(50);} catch (InterruptedException e){}
			repaint();
		}
	}

	public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1,y1,x2,y2);
		int px[] = new int[6], py[] = new int[6];
		px[0] = x2;
		py[0] = y2;
		int d = dist(x1,y1,x2,y2);
		double t = Math.atan2(y1-y2,x1-x2);
		double td = Math.PI*((double)(20.0/180.0));
		int len=10;
		px[1] = (int)(x2+Math.cos(t+td)*len);
		py[1] = (int) (y2+Math.sin(t+td)*len);
		px[2] = (int)(x2+Math.cos(t-td)*len);
		py[2] = (int) (y2+Math.sin(t-td)*len);
		g.fillPolygon(px,py,3);
		//      g.drawLine(x2,y2,
		//                 (int)(x2+Math.cos(t+td)*len),(int) (y2+Math.sin(t+td)*len));
		//      g.drawLine(x2,y2,
		//                 (int)(x2+Math.cos(t-td)*len),(int) (y2+Math.sin(t-td)*len));
	}

	public int dist(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}


	private MouseAdapter mouseEvent = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			mouseDownEvent(null, e.getX(), e.getY());
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
			ltxoff+=e.getX()-dragX;
			ltyoff+=e.getY()-dragY;
			x1+=e.getX()-dragX;
			y1+=e.getY()-dragY;
			newxdx+=e.getX()-dragX;
			newydy+=e.getY()-dragY;

			//System.out.println("drag "+newxdx+" "+newydy);

			dragX=e.getX();
			dragY=e.getY();
			repaint();
		}
	}
	private MouseMotion mouseMotion = new MouseMotion();

	public boolean mouseDownEvent(Event evt, int x, int y) {
		if (state == STARTING) {
			ptx = x-mouseMotion.originX;
			pty = y-mouseMotion.originY;
			pxsz = new Dimension(offScreenSize);
			zoney = y/ih;
			System.out.println("click:"+ptx+" "+pty+" ("+(y/ih)+")");
			if (!haveinfo) {
				panel.setupPanel("Please wait, getting image information...",null,null);
				pixels = new int [pxsz.height*pxsz.width];
				PixelGrabber pg = new PixelGrabber(imgAll, 0, 0, pxsz.width, 
						pxsz.height, pixels, 0, pxsz.width);
				try {
					pg.grabPixels();
				} catch (InterruptedException e) {
					panel.setupPanel("Getting image information...",null,null);
					state = ERROR;
					return true;
				}
				if ((pg.status() & ImageObserver.ABORT) != 0) {
					panel.setupPanel("Getting image information...",null,null);
					state = ERROR;
					return true;
				}
			}
			panel.setupPanel("Finding an identical point with the same environment...",null,"Stop");
			state = FINDHZSTRIP;
			repaint();
			return true;
		}
		if (state == POINTSONLY) {
			//       System.out.println("pty: " + pty + ", cy: " + cy);
			ltpy=findYnum(y);
			ltpx=findXnum(x,ltpy);
			System.out.println("origin: click:"+(x-ltxoff)+" "+(y-ltyoff)+" point:"+ltpx+" "+ltpy);
			//System.out.println("ltxoff: " + ltxoff + ", ltpx: " + ltpx);
			//state=FOUNDBASE;
			state=NEXTPOINT;
			next=NEXTPOINT;
			previous=POINTSONLY;
			//panel.setupPanel("This will be the origin of our reference system.","Go Back",
			//           "Continue");
			repaint();
			return true;
		}
		if (state == NEXTPOINT || state == MOREPOINT || state == BEFOREREPEAT) {
			//       System.out.println("pty: " + pty + ", cy: " + cy);
			ltpy2=findYnum(y);
			ltpx2=findXnum(x,ltpy2);
			//        System.out.println("ltyoff: " + ltyoff + ", ltpy2: " + ltpy2);
			//        System.out.println("ltxoff: " + ltxoff + ", ltpx2: " + ltpx2);
			state=CONNECTPOINTS;
			next=CONNECTPOINTS;
			previous=POINTSONLY;
			//panel.setupPanel("Here is the lattice line defined by the two points.","Go Back",
			//           "Continue");
			repaint();
			return true;
		}
		return false;
	}


	//g.drawString((int)Math.floor((double)(i-ltxoff-((int)Math.floor((double)(j-ltyoff)/cy))*xoff)/cx)+" "+(int)Math.floor((double)(j-ltyoff)/cy), i+4, j+4);


	public int findYnum(int y) {
		return (int)Math.round((double)(y-ltyoff)/cy);


		//int yy = y-ltyoff;
		//return ((yy+((yy<0?-1:1)*cy/2))/cy);
	}

	public int findXnum(int x, int ltpy) {
		return (int)Math.round((double)(x-ltxoff-(ltpy)*xoff)/cx);



		//int xx = x-ltxoff;                      
		//return (xx+((xx<0?-1:1)*cx/2)-(xoff*(ltpy/*-(pty/cy)*/)))/cx;

		/*   	
   	int n; 
   	if (x < xoff*ltpy+cx/2) n = ((x-cx/2) - ltxoff - (xoff*ltpy))/cx;
    else n = ((x+cx/2) - ltxoff - (xoff*ltpy))/cx;
   	if (n<0)n--;
    return n;
		 */    
	}

	public boolean handleMyEvent(Event event) {
		setSecondLattice(0, size());
		repaint();
		return false;
	}

	public void setSecondLattice(int start, Dimension d) {
		//     System.out.println("k: " + k);

		//mouseMotion.newxdx=mouseMotion.newxdx=0;

		newpts = start;
		newxoff = Math.cos(Math.atan(n))*((double)(panel.k)/dist);
		newyoff = Math.sin(Math.atan(n))*((double)(panel.k)/dist);
		//     System.out.println("newxoff: " + newxoff + ", newyoff: " + newyoff);
		for(double kk=0,i=x1-mouseMotion.newxdx+newxoff, j=y1-mouseMotion.newydy+newyoff; 
				//i < d.width && j < d.height && newpts < 500;
				kk<50;
				i += newxoff, j += newyoff, kk++) {
			newx[newpts]=(int)Math.round(i);
			newy[newpts++]=(int)Math.round(j);
		}
		for(double kk=0,i=x1-newxoff-mouseMotion.newxdx, j=y1-newyoff-mouseMotion.newydy; 
				//i > 0 && j > 0 && newpts < 500;
				kk<50;
				i -= newxoff, j -= newyoff, kk++) {
			newx[newpts]=(int)Math.round(i);
			newy[newpts++]=(int)Math.round(j);
		}
	}

	public void hideSecondLattice() {
		newpts = oldnewpts;
	}

	public void showDescription(String s) {
		if (desc != s) {
			desc = s;
			try {
				InputStream in = rlattice.instance.open(desc);
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;

				while((line =reader.readLine())!=null){
					stringBuffer.append(line).append("\n");
				}

				String t = stringBuffer.toString();

				panel.clear();
				panel.setupPanel(t, null, "Continue");

			} catch (Exception e) {
				System.err.println("Unable to show web description.");
			}
		}
	}
}
