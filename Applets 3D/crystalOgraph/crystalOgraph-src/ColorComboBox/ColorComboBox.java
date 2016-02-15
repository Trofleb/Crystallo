package ColorComboBox;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ColorComboBox extends MultiItemComboBox {

	public static final int DEFAULT_COLUMN_COUNT = 8;
	public static final int DEFAULT_ROW_COUNT = 5;
	protected ColorItem overItem;
	protected boolean showCustomColorButton;

	public void setAdditionalCustomButton(JButton jButton) {
		javax.swing.JComponent jcomponent = this.getPopupComponent();
		jButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent actionevent) {
				ColorComboBox.this.hidePopup();
			}
		});
		jcomponent.add(((jButton)), "South");
	}

	public ColorComboBox() throws IncompatibleLookAndFeelException {
		this.overItem = null;
		this.showCustomColorButton = false;
		this.setPopupColumnCount(8);
		this.setPopupRowCount(5);

		this.popupPanel.addMouseListener(((new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent mouseevent) {

				// java.awt.Component component = ((java.awt.event.MouseAdapter)
				// (_fld0)).MouseAdapter.getComponentAt(mouseevent.getPoint());
				java.awt.Component component = ColorComboBox.this.popupPanel.getComponentAt(mouseevent.getPoint());
				if (component != null && (component instanceof ColorItem)) {
					ColorComboBox.this.overItem = (ColorItem) component;
					ColorComboBox.this.setSelectedItem(((ColorComboBox.this.overItem.getColor())));
					ColorComboBox.this.hidePopup();
				}
			}
		})));
		this.popupPanel.addMouseMotionListener(((new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseMoved(java.awt.event.MouseEvent mouseevent) {
				// java.awt.Component component =
				// ((java.awt.event.MouseMotionAdapter)
				// (_fld0)).MouseMotionAdapter.getComponentAt(mouseevent.getPoint());
				java.awt.Component component = ColorComboBox.this.popupPanel.getComponentAt(mouseevent.getPoint());
				if (component != null && (component instanceof ColorItem)) {
					if (ColorComboBox.this.overItem != null)
						ColorComboBox.this.overItem.setOver(false);
					ColorComboBox.this.overItem = (ColorItem) component;
					ColorComboBox.this.overItem.setOver(true);
					// if(_fld0)
					if (false)
						ColorComboBox.this.setSelectedItem(((ColorComboBox.this.overItem.getColor())));
				}
			}
		})));
		this.setPreferredSize(new Dimension(50, 30));
		javax.swing.JPanel jpanel = new JPanel();
		jpanel.setLayout(((new BorderLayout())));
		jpanel.add(((this.popupPanel)), "Center");
		this.setPopupComponent(((jpanel)));
		this.setRenderer(((new ColorComboBoxRenderer())));
		this.addKeyListener(((new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent keyevent) {
				ColorComboBox.this.processKeyOnPopup(keyevent);
				keyevent.consume();
			}
		})));
		this.populatePopup();
	}

	public boolean getShowCustomColorButton() {
		return this.showCustomColorButton;
	}

	public void setShowCustomColorButton(boolean flag) {
		this.showCustomColorButton = flag;
	}

	protected void populatePopup() {
		final ColorComboBoxModel model = new ColorComboBoxModel();
		int i = model.getSize();
		final int extraItemIndex = i;
		for (int j = 0; j < i; j++)
			this.popupPanel.add(((new ColorItem((java.awt.Color) model.getElementAt(j)))));

		if (this.showCustomColorButton) {
			javax.swing.JComponent jcomponent = this.getPopupComponent();
			javax.swing.JButton jbutton = new JButton("Choose ...");
			jbutton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent actionevent) {
					java.awt.Color color = (java.awt.Color) ColorComboBox.this.getSelectedItem();
					java.awt.Color color1 = javax.swing.JColorChooser.showDialog(((java.awt.Component) (null)),
							"Choose Color...", color);
					if (color1 != null) {
						if (model.getElementAt(extraItemIndex) != null)
							model.removeElementAt(extraItemIndex);
						model.insertElementAt(((color1)), extraItemIndex);
						ColorComboBox.this.setSelectedItem(((color1)));
						ColorComboBox.this.hidePopup();
					}
				}
			});
			jcomponent.add(((jbutton)), "South");
		}
		this.setModel(((model)));
	}

	protected synchronized void processKeyOnPopup(java.awt.event.KeyEvent keyevent) {
		int i = keyevent.getKeyCode();
		ColorComboBoxModel colorcomboboxmodel = (ColorComboBoxModel) this.getModel();
		int j;
		if (this.overItem != null)
			j = colorcomboboxmodel.getIndexOf(((this.overItem.getColor())));
		else
			j = this.getSelectedIndex();
		java.awt.Point point = this.convertIndexToXY(j);
		switch (i) {
			default:
				break;

			case 38: // '&'
				if (point.y > 0)
					point.y--;
				else
					return;
				break;

			case 40: // '('
				if (point.y < 4)
					point.y++;
				else
					return;
				break;

			case 37: // '%'
				if (point.x > 0)
					point.x--;
				else
					return;
				break;

			case 39: // '\''
				if (point.x < 7)
					point.x++;
				else
					return;
				break;

			case 10: // '\n'
				if (this.overItem != null)
					this.setSelectedItem(((this.overItem.getColor())));
				this.hidePopup();
				break;

			case 27: // '\033'
				this.hidePopup();
				break;
		}
		int k = this.convertXYToIndex(point);
		if (k != j && k < this.getItemCount()) {
			if (this.overItem != null)
				this.overItem.setOver(false);
			this.overItem = (ColorItem) this.popupPanel.getComponent(k);
			this.overItem.setOver(true);
			if (this.selectOnKeyPress)
				this.setSelectedIndex(k);
		}
	}
}
