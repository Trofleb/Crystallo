import java.awt.Color;

import javax.swing.JComboBox;

import ColorComboBox.ColorComboBox;
import ColorComboBox.IncompatibleLookAndFeelException;

public class ColorChoiceOld extends MultiLineToolTip.JComboBox {
	static String[] names = {"black", "blue", "cyan", "darkGray", "gray", "green", "lightGray", 
									 "magenta", "orange", "pink", "red", "white", "yellow"};
	static Color[] colors = {Color.black, Color.blue, Color.cyan, Color.darkGray, 
									 Color.gray, Color.green, Color.lightGray, Color.magenta, 
									 Color.orange, Color.pink, Color.red, Color.white, Color.yellow};
	
	public ColorChoiceOld() {
		for (int i=0; i<names.length; i++) 
			addItem(names[i]);
	}

	public ColorChoiceOld(String def) {
		this();
		select(def);
	}
	

	public void select(Object o) {
		setSelectedItem(o);
	}

	
	public ColorChoiceOld(Color def) {
		this(colorToString(def));
	}

	public static Color stringToColor(String s) {
		for (int i=0; i<names.length; i++) 
			if (s.equals(names[i]))
				return colors[i];
		return null;	
	}

	public static String colorToString(Color c) {
		for (int i=0; i<colors.length; i++) 
			if (c.equals(colors[i]))
				return names[i];
		return null;	
	}
}
