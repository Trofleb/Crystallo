package ColorComboBox;

import java.awt.GridLayout;
import java.awt.Point;

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
		this.selectOnMouseOver = false;
		this.selectOnKeyPress = true;
		this.popupColumnCount = 5;
		this.popupRowCount = 5;
		this.popupPanel = new JPanel();
		this.popupPanel.setLayout(((new GridLayout(this.popupRowCount, this.popupColumnCount))));
		this.setPopupComponent(((this.popupPanel)));
	}

	public boolean isSelectOnMouseOver() {
		return this.selectOnMouseOver;
	}

	public void setSelectOnMouseOver(boolean flag) {
		this.selectOnMouseOver = flag;
	}

	public boolean isSelectOnKeyPress() {
		return this.selectOnKeyPress;
	}

	public void setSelectOnKeyPress(boolean flag) {
		this.selectOnKeyPress = flag;
	}

	public int getPopupColumnCount() {
		return this.popupColumnCount;
	}

	public void setPopupColumnCount(int i) {
		this.popupColumnCount = i;
		this.popupPanel.setLayout(((new GridLayout(this.popupRowCount, this.popupColumnCount))));
	}

	public int getPopupRowCount() {
		return this.popupRowCount;
	}

	public void setPopupRowCount(int i) {
		this.popupRowCount = i;
		this.popupPanel.setLayout(((new GridLayout(this.popupRowCount, this.popupColumnCount))));
	}

	protected Point convertIndexToXY(int i) {
		int j = i % this.popupColumnCount;
		int k = i / this.popupColumnCount;
		return new Point(j, k);
	}

	protected int convertXYToIndex(Point point) {
		return point.x + point.y * this.popupColumnCount;
	}
}
