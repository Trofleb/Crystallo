package ColorComboBox;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ColorComboBox extends MultiItemComboBox {

  public static final int DEFAULT_COLUMN_COUNT = 8;
  public static final int DEFAULT_ROW_COUNT = 5;
  protected ColorItem overItem;
  protected boolean showCustomColorButton;

  public void setAdditionalCustomButton(JButton jButton) {
    javax.swing.JComponent jcomponent = getPopupComponent();
    jButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent actionevent) {
      	hidePopup();
      }
    });
    jcomponent.add(((java.awt.Component) (jButton)), "South");
  }
  
  public ColorComboBox() throws IncompatibleLookAndFeelException {
    overItem = null;
    showCustomColorButton = false;
    setPopupColumnCount(8);
    setPopupRowCount(5);
    
    popupPanel.addMouseListener(((java.awt.event.MouseListener) (new java.awt.event.MouseAdapter() {
      public void mousePressed(java.awt.event.MouseEvent mouseevent) {
      	
      	//java.awt.Component component = ((java.awt.event.MouseAdapter) (_fld0)).MouseAdapter.getComponentAt(mouseevent.getPoint());
      	java.awt.Component component = popupPanel.getComponentAt(mouseevent.getPoint());
        if(component != null && (component instanceof ColorItem)) {
          overItem = (ColorItem)component;
          setSelectedItem(((java.lang.Object) (overItem.getColor())));
          hidePopup();
        }
      }
    })));
    popupPanel.addMouseMotionListener(((java.awt.event.MouseMotionListener) (new java.awt.event.MouseMotionAdapter() {
      public void mouseMoved(java.awt.event.MouseEvent mouseevent) {
        //java.awt.Component component = ((java.awt.event.MouseMotionAdapter) (_fld0)).MouseMotionAdapter.getComponentAt(mouseevent.getPoint());
        java.awt.Component component = popupPanel.getComponentAt(mouseevent.getPoint());
        if(component != null && (component instanceof ColorItem)) {
          if(overItem != null)
            overItem.setOver(false);
          overItem = (ColorItem)component;
          overItem.setOver(true);
          //if(_fld0)
          if(false)
            setSelectedItem(((java.lang.Object) (overItem.getColor())));
        }
      }
    })));
    setPreferredSize(new Dimension(50, 30));
    javax.swing.JPanel jpanel = new JPanel();
    jpanel.setLayout(((java.awt.LayoutManager) (new BorderLayout())));
    jpanel.add(((java.awt.Component) (popupPanel)), "Center");
    setPopupComponent(((javax.swing.JComponent) (jpanel)));
    setRenderer(((javax.swing.ListCellRenderer) (new ColorComboBoxRenderer())));
    addKeyListener(((java.awt.event.KeyListener) (new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent keyevent) {
        processKeyOnPopup(keyevent);
        keyevent.consume();
      }
    })));
    populatePopup();
  }

  public boolean getShowCustomColorButton() {
    return showCustomColorButton;
  }

  public void setShowCustomColorButton(boolean flag) {
    showCustomColorButton = flag;
  }

  protected void populatePopup() {
    final ColorComboBoxModel model = new ColorComboBoxModel();
    int i = model.getSize();
    final int extraItemIndex = i;
    for(int j = 0; j < i; j++)
      popupPanel.add(((java.awt.Component) (new ColorItem((java.awt.Color)model.getElementAt(j)))));

    if(showCustomColorButton) {
      javax.swing.JComponent jcomponent = getPopupComponent();
      javax.swing.JButton jbutton = new JButton("Choose ...");
      jbutton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent actionevent) {
          java.awt.Color color = (java.awt.Color)getSelectedItem();
          java.awt.Color color1 = javax.swing.JColorChooser.showDialog(((java.awt.Component) (null)), "Choose Color...", color);
          if(color1 != null) {
            if(model.getElementAt(extraItemIndex) != null)
              model.removeElementAt(extraItemIndex);
            model.insertElementAt(((java.lang.Object) (color1)), extraItemIndex);
            setSelectedItem(((java.lang.Object) (color1)));
            hidePopup();
          }
        }
      });
      jcomponent.add(((java.awt.Component) (jbutton)), "South");
    }
    setModel(((javax.swing.ComboBoxModel) (model)));
  }

  protected synchronized void processKeyOnPopup(java.awt.event.KeyEvent keyevent) {
    int i = keyevent.getKeyCode();
    ColorComboBoxModel colorcomboboxmodel = (ColorComboBoxModel)getModel();
    int j;
    if(overItem != null)
      j = colorcomboboxmodel.getIndexOf(((java.lang.Object) (overItem.getColor())));
    else
      j = getSelectedIndex();
    java.awt.Point point = convertIndexToXY(j);
    switch(i) {
    default:
      break;

    case 38: // '&'
      if(point.y > 0)
        point.y--;
      else
        return;
      break;

    case 40: // '('
      if(point.y < 4)
        point.y++;
      else
        return;
      break;

    case 37: // '%'
      if(point.x > 0)
        point.x--;
      else
        return;
      break;

    case 39: // '\''
      if(point.x < 7)
        point.x++;
      else
        return;
      break;

    case 10: // '\n'
      if(overItem != null)
        setSelectedItem(((java.lang.Object) (overItem.getColor())));
      hidePopup();
      break;

    case 27: // '\033'
      hidePopup();
      break;
    }
    int k = convertXYToIndex(point);
    if(k != j && k < getItemCount()) {
      if(overItem != null)
        overItem.setOver(false);
      overItem = (ColorItem) popupPanel.getComponent(k);
      overItem.setOver(true);
      if(selectOnKeyPress)
        setSelectedIndex(k);
    }
  }
}
