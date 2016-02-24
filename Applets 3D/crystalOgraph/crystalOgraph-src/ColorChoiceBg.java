import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import ColorComboBox.IncompatibleLookAndFeelException;

/*
 * Created on 18 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// setLayout(new FlowLayout());
		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		this.color = color;
		this.baseColor = color;
		this.penLum = this.defaultPenLum(color);

		this.add(new JLabel("Color"));
		try {
			this.choice = new ColorChoice(color);
		} catch (IncompatibleLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		this.choice.addActionListener(this);
		this.choice.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.choice);

		this.add(new JLabel("Brightness"));
		this.lightBar = new JScrollBar(Adjustable.HORIZONTAL, this.penLum, 1, 0, 100);
		this.lightBar.addAdjustmentListener(this);
		this.lightBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(this.lightBar);

		// add(javax.swing.Box.createVerticalGlue());
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void set(BrightedColor bc) {
		this.baseColor = bc.baseColor;
		this.penLum = bc.penLum;
		this.choice.setSelectedColor(this.baseColor);
		this.lightBar.setValue(this.penLum);
		this.calculateColor();
	}

	public BrightedColor get() {
		return new BrightedColor(this.baseColor, this.penLum);
	}

	public static Color colorBright(Color c, double x) {
		if (x == 0.0)
			return c;
		else if (x > 0.0) {
			x = 1.0 - x;
			int r = (int) Math.round(c.getRed() * x);
			int g = (int) Math.round(c.getGreen() * x);
			int b = (int) Math.round(c.getBlue() * x);
			return new Color(r, g, b);
		} else {
			x = 1.0 + x;
			int r = (int) Math.round(255 - ((255 - c.getRed()) * x));
			int g = (int) Math.round(255 - ((255 - c.getGreen()) * x));
			int b = (int) Math.round(255 - ((255 - c.getBlue()) * x));
			return new Color(r, g, b);
		}
	}

	private int defaultPenLum(Color c) {
		if (this.color.equals(Color.black))
			return 99;
		else if (this.color.equals(Color.white))
			return 0;
		else
			return 49;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.baseColor = this.choice.getSelectedColor();
		this.color = this.baseColor;
		this.penLum = this.defaultPenLum(this.baseColor);
		this.lightBar.setValue(this.penLum);
		this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		this.penLum = this.lightBar.getValue();
		this.calculateColor();
		this.actionListener.actionPerformed(new ActionEvent(this, 1, ""));
	}

	private void calculateColor() {
		if (this.baseColor.equals(Color.black))
			this.color = colorBright(this.baseColor, (this.penLum - 100) / 160.0);
		else if (this.baseColor.equals(Color.white))
			this.color = colorBright(this.baseColor, (this.penLum) / 160.0);
		else
			this.color = colorBright(this.baseColor, (this.penLum - 50) / 80.0);
	}

	// static String colorToString(Color c) {
	// return ColorChoice.colorToString(c);
	// }
}

class BrightedColor {
	public Color baseColor;
	public int penLum;

	BrightedColor(Color baseColor, int penLum) {
		this.baseColor = baseColor;
		this.penLum = penLum;
	}
}
