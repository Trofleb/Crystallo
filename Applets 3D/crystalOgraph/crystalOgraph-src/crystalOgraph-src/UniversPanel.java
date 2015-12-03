import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Color3f;

import ColorComboBox.IncompatibleLookAndFeelException;

public class UniversPanel extends JPanel implements ActionListener, AdjustmentListener {
	ColorChoice choice;
	JScrollBar lightBar;
	Color baseColor;
	Color color;
	int penLum;
	Univers univers;
	Grid grid;
	JCheckBox showCell;
	JRadioButton parallel;
	JRadioButton perspective;
	JButton snap;
	crystalOgraph applet;

	public UniversPanel(crystalOgraph applet, Univers univers, Grid grid) {
		this.applet = applet;
		this.univers = univers;
		this.grid = grid;
		
		setBorder(new TitledBorder("Univers"));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridwidth=2;
		c.gridx = 0; c.gridy = 0;
		add(showCell=new MultiLineToolTip.JCheckBox("Show cell"), c);
		c.gridx = 0; c.gridy = 1;
		add(parallel=new MultiLineToolTip.JRadioButton("Parallel projection"), c);
		c.gridx = 0; c.gridy = 2;
		add(perspective=new MultiLineToolTip.JRadioButton("Perspective"), c);

		showCell.addActionListener(this);
		parallel.addActionListener(this);
		perspective.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(perspective);
		bg.add(parallel);
		
		reset();

		c.gridwidth=1;
		c.gridx = 0; c.gridy = 3;
		add(new JLabel("Color"), c);
		try {
			choice = new ColorChoice(color);
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		choice.setPreferredSize(new Dimension(50, 20));
	  choice.addActionListener(this);
		c.insets = new Insets(0,5,0,0);  
		c.gridx = 1; c.gridy = 3;
		add(choice, c);
		
		c.gridx = 0; c.gridy = 4;
//		add(new JLabel("Light"), c);
		lightBar = new JScrollBar(JScrollBar.HORIZONTAL, penLum, 1, 0, 100);
		lightBar.addAdjustmentListener(this);
		c.gridx = 1; c.gridy = 4;
//	  add(lightBar, c);
	  
		c.gridwidth=2;
		c.gridx = 0; c.gridy = 5;
		c.insets = new Insets(5,0,0,0);  
		snap=new JButton("Take a snapshot");
		snap.setMargin(new Insets(0,0,0,0));
		add(snap, c);
		snap.addActionListener(this);
	}
	
	public void reset() {
		color=Color.white;
		baseColor=color;
		penLum = defaultPenLum(color);
		univers.setBackgroundColor(new Color3f(color));
		if (choice!=null) choice.setSelectedColor(color);
		
		showCell.setSelected(true);
		grid.show();
		
		perspective.setSelected(true);
		univers.parallelUnivers(parallel.isSelected());			
	}

	public void set(BrightedColor bc) {
		this.baseColor=bc.baseColor;
		this.penLum=bc.penLum;
		choice.setSelectedColor(baseColor);
		lightBar.setValue(penLum);
		calculateColor();
	}

	public BrightedColor get() {
		return new BrightedColor(baseColor, penLum);
	}


	public static Color colorBright(Color c, double x) {
		 if (x==0.0) return c;
		 else if (x>0.0) {
			 x=1.0-x;
			 int r=(int)Math.round(c.getRed()*x);
			 int g=(int)Math.round(c.getGreen()*x);
			 int b=(int)Math.round(c.getBlue()*x);
			 return new Color(r, g, b);
		 }
		 else {
			 x=1.0+x;
			 int r=(int)Math.round(255-((255-c.getRed())*x));
			 int g=(int)Math.round(255-((255-c.getGreen())*x));
			 int b=(int)Math.round(255-((255-c.getBlue())*x));
			 return new Color(r, g, b);
		 }
	}

	private int defaultPenLum(Color c) {
		if (color.equals(Color.black)) return 99;
		else if (color.equals(Color.white)) return 0;
		else return 49;
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource()==showCell) {
			if (showCell.isSelected()) grid.show();
			else grid.hide();
		}
		else if (e.getSource()==parallel || e.getSource()==perspective) {
			univers.parallelUnivers(parallel.isSelected());			
		}
		else if (e.getSource()==snap) {
			JFileChooser chooser = new JFileChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addChoosableFileFilter(new JpegFileFilter());
			chooser.setFileFilter(new PngFileFilter());
	    if(chooser.showSaveDialog(applet.frame)==JFileChooser.APPROVE_OPTION) {
	    	FileFilter ff = chooser.getFileFilter();
	    	File f = chooser.getSelectedFile();
	    	if (ff instanceof JpegFileFilter) {
	    		if (f.getName().toLowerCase().indexOf(".jpeg")==-1 && f.getName().toLowerCase().indexOf(".jpg")==-1) {
	    			f = new File(f.getAbsoluteFile()+".jpeg");
	    		}
	    	}
	    	else {
	    		if (f.getName().toLowerCase().indexOf(".png")==-1) {
	    			f = new File(f.getAbsoluteFile()+".png");
	    		}
	    	}
				if (!f.exists() || JOptionPane.showConfirmDialog(applet.frame, "Overwrite file "+f.getName()+" ?", "File already exists", JOptionPane.YES_NO_OPTION)==0) {
		    	try {
		    		BufferedImage img = univers.getScreenShot();
						ImageIO.write(img, ff instanceof JpegFileFilter?"JPEG":"PNG", f);
					} catch (Exception ex) {
						ex.printStackTrace();
			  		JOptionPane.showMessageDialog(applet.frame, "Can't write file");
					}
				}
	    }
//			try {
//				URL u = new URL("http://escher.epfl.ch/servlet/crystalOgraph/Output.png");
//				URLConnection con = u.openConnection();
//				con.setDoOutput(true);
//				OutputStream out = con.getOutputStream();
//				BufferedImage img = univers.getScreenShot();
//				//out.writeObject(img);
//				ImageIO.write(img, "PNG", out);
//				//ImageIO.write(img, "PNG", System.out);
//				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//				while (true) {
//					String s = in.readLine();
//					if (s==null) break;
//					//System.out.println(s);
//					if (s.equals("ok")) {
//						applet.getAppletContext().showDocument(new URL("http://escher.epfl.ch/servlet/crystalOgraph/Output.png"), "_blank");
//					}
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
		}
		else {
			baseColor=choice.getSelectedColor();
			color=baseColor;
			penLum = defaultPenLum(baseColor);
			lightBar.setValue(penLum);
			doUpdate();
		}
	}
	   
	public void adjustmentValueChanged(AdjustmentEvent e) {
		penLum = lightBar.getValue();
		calculateColor();
		doUpdate();
	}
	
	private void calculateColor() {
		if (baseColor.equals(Color.black))
			color = colorBright(baseColor, (penLum-100)/160.0);
		else if (baseColor.equals(Color.white))
			color = colorBright(baseColor, (penLum)/160.0);
		else color = colorBright(baseColor, (penLum-50)/80.0);
	}

	private void doUpdate() {
		univers.setBackgroundColor(new Color3f(color));
	}

	class BrightedColor {
		public Color baseColor;
		public int penLum;
		
		BrightedColor(Color baseColor, int penLum) {
			this.baseColor=baseColor;
			this.penLum=penLum;
		}
	}
}

class JpegFileFilter extends FileFilter {
	public String[] ext = {"jpeg", "jpg"};

	public String getDescription() {
		return "JPEG files";
	}
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
    String extension = getExtension(f);
    if (extension==null) return false;
    for (int i=0; i<ext.length; i++) {
      if (extension.toLowerCase().equals(ext[i])) return true;
		}
    return false;
	}

	public String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 &&  i < s.length() - 1) {
        ext = s.substring(i+1).toLowerCase();
    }
    return ext;
	}
}

class PngFileFilter extends FileFilter {
	public String[] ext = {"png"};

	public String getDescription() {
		return "PNG files";
	}
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
    String extension = getExtension(f);
    if (extension==null) return false;
    for (int i=0; i<ext.length; i++) {
      if (extension.toLowerCase().equals(ext[i])) return true;
		}
    return false;
	}

	public String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 &&  i < s.length() - 1) {
        ext = s.substring(i+1).toLowerCase();
    }
    return ext;
	}
}


