package ColorComboBox;

public class JComponentComboBox extends javax.swing.JComboBox {

	javax.swing.JComponent component;

	public JComponentComboBox() {
	}

	private javax.swing.plaf.basic.BasicComboPopup getComboPopup() {
		int i = 0;
		for (int j = this.getUI().getAccessibleChildrenCount(((this))); i < j; i++) {
			javax.accessibility.Accessible accessible = this.getUI().getAccessibleChild(((this)), i);
			if (accessible instanceof javax.swing.plaf.basic.BasicComboPopup)
				return (javax.swing.plaf.basic.BasicComboPopup) accessible;
		}

		return null;
	}

	public javax.swing.JComponent getPopupComponent() {
		return this.component;
	}

	public void setPopupComponent(javax.swing.JComponent jcomponent) throws IncompatibleLookAndFeelException {
		javax.swing.plaf.basic.BasicComboPopup basiccombopopup = this.getComboPopup();
		if (basiccombopopup != null) {
			basiccombopopup.removeAll();
			basiccombopopup.add(((jcomponent)));
			this.component = jcomponent;
		} else
			throw new IncompatibleLookAndFeelException(
					"Could not modify the combo box' popup menu: Try a different look&feel.");
	}

	@Override
	public void updateUI() {
		super.updateUI();
		try {
			if (this.component != null) {
				javax.swing.SwingUtilities.updateComponentTreeUI(((this.component)));
				this.setPopupComponent(this.component);
			}
		} catch (IncompatibleLookAndFeelException incompatiblelookandfeelexception) {
			throw new RuntimeException(((incompatiblelookandfeelexception)));
		}
	}
}
