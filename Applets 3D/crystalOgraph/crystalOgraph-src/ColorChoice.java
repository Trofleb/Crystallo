import java.awt.Color;

import ColorComboBox.ColorComboBox;
import ColorComboBox.ColorComboBoxModel;
import ColorComboBox.IncompatibleLookAndFeelException;

public class ColorChoice extends ColorComboBox {
	public ColorChoice() throws IncompatibleLookAndFeelException {
		super();
	}

	public ColorChoice(Color defColor) throws IncompatibleLookAndFeelException {
		super();
		this.setSelectedColor(defColor);
	}

	public Color getSelectedColor() {
		return (Color) this.getSelectedItem();
	}

	public void setSelectedColor(Color c) {
		this.setSelectedItem(c);
	}

	public static String colorToString(Color c) {
		for (int i = 0; i < ColorComboBoxModel.DEFAULT_COLORS.length; i++)
			if (c.equals(ColorComboBoxModel.DEFAULT_COLORS[i]))
				return ColorComboBoxModel.DEFAULT_COLOR_NAMES[i];
		return "";

		// return "#"+Integer.toHexString(c.getRGB());
	}
}
