package ColorComboBox;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

public class IncompatibleLookAndFeelException extends java.lang.Exception {

  public IncompatibleLookAndFeelException(java.lang.String s) {
    super(s);
  }

  public java.lang.String getLookAndFeelName() {
    return javax.swing.UIManager.getLookAndFeel().getName();
  }
}
