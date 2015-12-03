import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import ColorComboBox.IncompatibleLookAndFeelException;

/*
 * Created on 18 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ColorChoiceBg extends JPanel implements ActionListener, AdjustmentListener {
	ColorChoice choice;
	JScrollBar lightBar;
	Color baseColor;
	Color color;
	int penLum;
	ActionListener actionListener;
	
	public ColorChoiceBg(Color color) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//setLayout(new FlowLayout());
		setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		this.color=color;
		this.baseColor=color;
		penLum = defaultPenLum(color);

		add(new JLabel("Color"));
		try {
			choice = new ColorChoice(color);
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
	  choice.addActionListener(this);
	  choice.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(choice);
		
		add(new JLabel("Brightness"));
		lightBar = new JScrollBar(JScrollBar.HORIZONTAL, penLum, 1, 0, 100);
		lightBar.addAdjustmentListener(this);
		lightBar.setAlignmentX(JPanel.LEFT_ALIGNMENT);
	  add(lightBar);
	  
	  //add(javax.swing.Box.createVerticalGlue());
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
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

	public void actionPerformed(ActionEvent event){
		baseColor=choice.getSelectedColor();
		color=baseColor;
		penLum = defaultPenLum(baseColor);
		lightBar.setValue(penLum);
		actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}
	   
	public void adjustmentValueChanged(AdjustmentEvent e) {
		penLum = lightBar.getValue();
		calculateColor();
		actionListener.actionPerformed(new ActionEvent(this, 1, ""));
	}
	
	private void calculateColor() {
		if (baseColor.equals(Color.black))
			color = colorBright(baseColor, (penLum-100)/160.0);
		else if (baseColor.equals(Color.white))
			color = colorBright(baseColor, (penLum)/160.0);
		else color = colorBright(baseColor, (penLum-50)/80.0);
	}

//	static String colorToString(Color c) {
//		return ColorChoice.colorToString(c);
//	}
}



class BrightedColor {
	public Color baseColor;
	public int penLum;
	
	BrightedColor(Color baseColor, int penLum) {
		this.baseColor=baseColor;
		this.penLum=penLum;
	}
}

