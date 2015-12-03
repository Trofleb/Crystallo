import java.awt.Color;

import javax.swing.JComboBox;

import ColorComboBox.ColorComboBox;
import ColorComboBox.ColorComboBoxModel;
import ColorComboBox.IncompatibleLookAndFeelException;

public class ColorChoice extends ColorComboBox {
	public ColorChoice() throws IncompatibleLookAndFeelException {
		super();
	}
	
	public ColorChoice(Color defColor) throws IncompatibleLookAndFeelException {
		super();
		setSelectedColor(defColor);
	}

	public Color getSelectedColor() {
		return (Color) getSelectedItem();
	}
	public void setSelectedColor(Color c) {
		setSelectedItem(c);
	}
	
	public static String colorToString(Color c) {
		for (int i=0; i<ColorComboBoxModel.DEFAULT_COLORS.length; i++) {
			if (c.equals(ColorComboBoxModel.DEFAULT_COLORS[i])) 
				return ColorComboBoxModel.DEFAULT_COLOR_NAMES[i];
		}
		return "";
		
		//return "#"+Integer.toHexString(c.getRGB());
	}
}
