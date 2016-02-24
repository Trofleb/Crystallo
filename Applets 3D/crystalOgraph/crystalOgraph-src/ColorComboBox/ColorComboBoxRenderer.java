package ColorComboBox;

public class ColorComboBoxRenderer extends javax.swing.DefaultListCellRenderer {
	class Renderer extends java.awt.Component {

		private java.awt.Color color;

		public void setColor(java.awt.Color color1) {
			this.color = color1;
		}

		public java.awt.Color getColor() {
			return this.color;
		}

		@Override
		public void paint(java.awt.Graphics g) {
			g.setColor(this.color);
			// g.fillRect(1, 1, getWidth()-2, getHeight()-2);
			g.fillRect(2, 2, this.getWidth() - 5, this.getHeight() - 5);

			g.setColor(java.awt.Color.darkGray);
			// g.draw3DRect(1, 1, getWidth()-2, getHeight()-2, true);
			g.draw3DRect(2, 2, this.getWidth() - 5, this.getHeight() - 5, true);
		}

		Renderer() {
			super();
		}
	}

	protected Renderer renderer;

	public ColorComboBoxRenderer() {
		this.renderer = new Renderer();
	}

	@Override
	public java.awt.Component getListCellRendererComponent(javax.swing.JList jlist, java.lang.Object obj, int i,
			boolean flag, boolean flag1) {
		this.renderer.setColor((java.awt.Color) obj);
		return ((this.renderer));
	}
}
