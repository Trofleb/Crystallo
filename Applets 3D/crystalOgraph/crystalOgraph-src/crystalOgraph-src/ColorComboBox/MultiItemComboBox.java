package ColorComboBox;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class MultiItemComboBox extends JComponentComboBox {

  protected static final int DEFAULT_COLUMN_COUNT = 5;
  protected static final int DEFAULT_ROW_COUNT = 5;
  protected boolean selectOnMouseOver;
  protected boolean selectOnKeyPress;
  protected int popupColumnCount;
  protected int popupRowCount;
  protected JPanel popupPanel;

  public MultiItemComboBox() throws IncompatibleLookAndFeelException {
    selectOnMouseOver = false;
    selectOnKeyPress = true;
    popupColumnCount = 5;
    popupRowCount = 5;
    popupPanel = new JPanel();
    popupPanel.setLayout(((LayoutManager) (new GridLayout(popupRowCount, popupColumnCount))));
    setPopupComponent(((JComponent) (popupPanel)));
  }

  public boolean isSelectOnMouseOver() {
    return selectOnMouseOver;
  }

  public void setSelectOnMouseOver(boolean flag) {
    selectOnMouseOver = flag;
  }

  public boolean isSelectOnKeyPress() {
    return selectOnKeyPress;
  }

  public void setSelectOnKeyPress(boolean flag) {
    selectOnKeyPress = flag;
  }

  public int getPopupColumnCount() {
    return popupColumnCount;
  }

  public void setPopupColumnCount(int i) {
    popupColumnCount = i;
    popupPanel.setLayout(((LayoutManager) (new GridLayout(popupRowCount, popupColumnCount))));
  }

  public int getPopupRowCount() {
    return popupRowCount;
  }

  public void setPopupRowCount(int i) {
    popupRowCount = i;
    popupPanel.setLayout(((LayoutManager) (new GridLayout(popupRowCount, popupColumnCount))));
  }

  protected Point convertIndexToXY(int i) {
    int j = i % popupColumnCount;
    int k = i / popupColumnCount;
    return new Point(j, k);
  }

  protected int convertXYToIndex(Point point) {
    return point.x + point.y * popupColumnCount;
  }
}
