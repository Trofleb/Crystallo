package ColorComboBox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ColorComboBoxRenderer extends javax.swing.DefaultListCellRenderer {
  class Renderer extends java.awt.Component {

    private java.awt.Color color;

    public void setColor(java.awt.Color color1) {
      color = color1;
    }

    public java.awt.Color getColor() {
      return color;
    }

    public void paint(java.awt.Graphics g) {
      g.setColor(color);
      //g.fillRect(1, 1, getWidth()-2, getHeight()-2);
      g.fillRect(2, 2, getWidth()-5, getHeight()-5);
      
      g.setColor(java.awt.Color.darkGray);
      //g.draw3DRect(1, 1, getWidth()-2, getHeight()-2, true);
      g.draw3DRect(2, 2, getWidth()-5, getHeight()-5, true);
    }

    Renderer() {
      super();
    }
  }


  protected Renderer renderer;

  public ColorComboBoxRenderer() {
    renderer = new Renderer();
  }

  public java.awt.Component getListCellRendererComponent(javax.swing.JList jlist, java.lang.Object obj, int i, boolean flag, boolean flag1) {
  	renderer.setColor((java.awt.Color)obj);
    return ((java.awt.Component) (renderer));
  }
}
