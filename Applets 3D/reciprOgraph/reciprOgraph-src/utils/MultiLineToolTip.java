package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.metal.MetalToolTipUI;

public class MultiLineToolTip extends JToolTip {
  public MultiLineToolTip() {  
    setUI(new MultiLineToolTipUI());
  }

  
  public static class JButton extends javax.swing.JButton {
  	public JButton(String s) {super(s);}
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JTextField extends javax.swing.JTextField {
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JToggleButton extends javax.swing.JToggleButton {
  	public JToggleButton(String s) {super(s);}
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JCheckBox extends javax.swing.JCheckBox {
  	public JCheckBox(String s) {super(s);}
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JRadioButton extends javax.swing.JRadioButton {
  	public JRadioButton(String s) {super(s);}
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JComboBox extends javax.swing.JComboBox {
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JLabel extends javax.swing.JLabel {
  	public JLabel(String s) {super(s);}
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
  public static class JFormattedTextField extends javax.swing.JFormattedTextField {
		public JToolTip createToolTip() {return new MultiLineToolTip();}
  }
}



class MultiLineToolTipUI extends MetalToolTipUI {
  private String[] strs;
  private int maxWidth = 0;

  public void paint(Graphics g, JComponent c) {
  	FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
    Dimension size = c.getSize();
    g.setColor(c.getBackground());
    g.fillRect(0, 0, size.width, size.height);
    g.setColor(c.getForeground());
    if (strs != null) {
      for (int i=0;i<strs.length;i++) {
        g.drawString(strs[i], 3, (metrics.getHeight()) * (i+1));
      }
    }
    
  }

  public Dimension getPreferredSize(JComponent c) {
    FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(c.getFont());
    String tipText = ((MultiLineToolTip)c).getTipText();
    if (tipText == null) {
      tipText = "";
    }
    BufferedReader br = new BufferedReader(new StringReader(tipText));
    String line;
    int maxWidth = 0;
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
        int width = SwingUtilities.computeStringWidth(metrics,line);
        maxWidth = (maxWidth < width) ? width : maxWidth;
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }   
    int lines = v.size();
    if (lines < 1) {
      strs = null;
      lines = 1;
    } else {
      strs = new String[lines];
      int i=0;
      for (Enumeration e = v.elements(); e.hasMoreElements() ;i++) {
        strs[i] = (String)e.nextElement();
      }
    }
    int height = metrics.getHeight() * lines;
    this.maxWidth = maxWidth;
    return new Dimension(maxWidth + 6, height + 4);
  }
}
