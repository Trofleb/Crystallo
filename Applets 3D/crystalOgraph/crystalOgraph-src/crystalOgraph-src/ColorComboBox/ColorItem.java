package ColorComboBox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class ColorItem extends javax.swing.JComponent {

  protected static final int DEFAULT_SIZE = 12;
  protected static final int BORDER_SIZE = 3;
  protected static final java.awt.Color BORDER_COLOR = new Color(128, 128, 128);
  protected static final java.awt.Color OVER_COLOR = new Color(182, 189, 210, 100);
  protected static final java.awt.Color OVER_BORDER_COLOR = new Color(10, 30, 106);
  protected boolean over;

  public ColorItem(java.awt.Color color) {
    setColor(color);
    setPreferredSize(new Dimension(19, 19));
    over = false;
  }

  public void setColor(java.awt.Color color) {
    setBackground(color);
  }

  public java.awt.Color getColor() {
    return getBackground();
  }

  public void setOver(boolean flag) {
    over = flag;
    repaint();
  }

  public boolean isOver() {
    return over;
  }

  public void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    g.setColor(getColor());
    g.fillRect(3, 3, 11, 11);
    g.setColor(BORDER_COLOR);
    g.drawRect(3, 3, 11, 11);
    if(over) {
      g.setColor(OVER_COLOR);
      g.fillRect(0, 0, 17, 17);
      g.setColor(OVER_BORDER_COLOR);
      g.drawRect(0, 0, 17, 17);
    }
  }

}
