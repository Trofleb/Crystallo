/* Symmetry - BarBorder.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 26 oct. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class BarBorder extends TitledBorder {
	Border empty = BorderFactory.createEmptyBorder();
	
	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		super.paintBorder(c, g, x, y, w, h);
		String s = getTitle();
		Border b = getBorder();
		setTitle("_");
		setBorder(empty);
    if (System.getProperty("os.name").toLowerCase().indexOf("mac")!=-1) {
    	x+=3;
    	y++;
    }		
		super.paintBorder(c, g, x, y-g.getFont().getSize(), w, h);
		setTitle(s);
		setBorder(b);
	}

	public BarBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont, Color titleColor) {
		super(border, title, titleJustification, titlePosition, titleFont, titleColor);
	}
	public BarBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont) {
		super(border, title, titleJustification, titlePosition, titleFont);
	}
	public BarBorder(Border border, String title, int titleJustification, int titlePosition) {
		super(border, title, titleJustification, titlePosition);
	}
	public BarBorder(Border border, String title) {
		super(border, title);
	}
	public BarBorder(Border border) {
		super(border);
	}
	public BarBorder(String title) {
		super(title);
	}
}