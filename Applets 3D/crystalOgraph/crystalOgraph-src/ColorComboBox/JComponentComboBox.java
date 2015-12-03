package ColorComboBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

public class JComponentComboBox extends javax.swing.JComboBox {

  javax.swing.JComponent component;

  public JComponentComboBox() {
  }

  private javax.swing.plaf.basic.BasicComboPopup getComboPopup() {
    int i = 0;
    for(int j = getUI().getAccessibleChildrenCount(((javax.swing.JComponent) (this))); i < j; i++) {
      javax.accessibility.Accessible accessible = getUI().getAccessibleChild(((javax.swing.JComponent) (this)), i);
      if(accessible instanceof javax.swing.plaf.basic.BasicComboPopup)
        return (javax.swing.plaf.basic.BasicComboPopup)accessible;
    }

    return null;
  }

  public javax.swing.JComponent getPopupComponent() {
    return component;
  }

  public void setPopupComponent(javax.swing.JComponent jcomponent) throws IncompatibleLookAndFeelException {
    javax.swing.plaf.basic.BasicComboPopup basiccombopopup = getComboPopup();
    if(basiccombopopup != null) {
      basiccombopopup.removeAll();
      basiccombopopup.add(((java.awt.Component) (jcomponent)));
      component = jcomponent;
    } else {
      throw new IncompatibleLookAndFeelException("Could not modify the combo box' popup menu: Try a different look&feel.");
    }
  }

  public void updateUI() {
    super.updateUI();
    try {
      if(component != null) {
        javax.swing.SwingUtilities.updateComponentTreeUI(((java.awt.Component) (component)));
        setPopupComponent(component);
      }
    }
    catch(IncompatibleLookAndFeelException incompatiblelookandfeelexception) {
      throw new RuntimeException(((java.lang.Throwable) (incompatiblelookandfeelexception)));
    }
  }
}
