import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

public abstract class HVPanel extends JPanel implements ActionListener {
	private ButtonGroup group;
	private ActionListener listener;

	public HVPanel() {
		super();
		this.setLayout(new GridBagLayout());
	}

	public static class h extends HVPanel {
		private GridBagConstraints c;

		public h() {
			super();
			this.c = new GridBagConstraints();
			this.c.fill = GridBagConstraints.BOTH;
			this.c.gridx = this.c.gridy = 0;
			this.c.weightx = this.c.weighty = 1;
		}

		@Override
		protected void increment() {
			this.c.gridx++;
			this.c.gridy = 0;
		}

		@Override
		protected void subIncrement() {
			this.c.gridy++;
		}

		@Override
		public GridBagConstraints c() {
			return this.c;
		}

		public void top() {
			this.c().anchor = GridBagConstraints.NORTH;
		}

		public void bottom() {
			this.c().anchor = GridBagConstraints.SOUTH;
		}
	}

	public static class v extends HVPanel {
		private GridBagConstraints c;

		public v() {
			super();
			this.c = new GridBagConstraints();
			this.c.fill = GridBagConstraints.BOTH;
			this.c.insets = new Insets(0, 2, 0, 2);
			this.c.gridx = this.c.gridy = 0;
			this.c.weightx = this.c.weighty = 1;
		}

		@Override
		protected void increment() {
			this.c.gridy++;
			this.c.gridx = 0;
		}

		@Override
		protected void subIncrement() {
			this.c.gridx++;
		}

		@Override
		public GridBagConstraints c() {
			return this.c;
		}

		public void left() {
			this.c().anchor = GridBagConstraints.WEST;
		}

		public void right() {
			this.c().anchor = GridBagConstraints.EAST;
		}
	}

	protected abstract void increment();

	protected abstract void subIncrement();

	public abstract GridBagConstraints c();

	public void expandH(boolean b) {
		if (this.c().fill == GridBagConstraints.BOTH || this.c().fill == GridBagConstraints.VERTICAL)
			this.c().fill = b ? GridBagConstraints.BOTH : GridBagConstraints.VERTICAL;
		else
			this.c().fill = b ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
		this.c().weightx = b ? 1 : 0;
	}

	public void expandV(boolean b) {
		if (this.c().fill == GridBagConstraints.BOTH || this.c().fill == GridBagConstraints.HORIZONTAL)
			this.c().fill = b ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
		else
			this.c().fill = b ? GridBagConstraints.VERTICAL : GridBagConstraints.NONE;
		this.c().weighty = b ? 1 : 0;
	}

	public void addComp(Component comp) {
		this.add(comp, this.c());
		this.increment();
	}

	public void addSubPane(HVPanel panel, boolean h, boolean v) {
		int f = this.c().fill;
		double wx = this.c().weightx;
		double wy = this.c().weighty;
		this.expandV(v);
		this.expandH(h);
		this.addSubPane(panel);
		this.c().fill = f;
		this.c().weightx = wx;
		this.c().weighty = wy;
	}

	public void addSubPane(HVPanel panel) {
		this.addComp(panel);
		panel.addActionListener(this);
	}

	public void addButton(AbstractButton button) {
		this.addComp(button);
		button.addActionListener(this);
	}

	public void addButtonGroupped(AbstractButton button) {
		if (this.group == null) {
			this.group = new ButtonGroup();
			button.setSelected(true);
		}
		this.group.add(button);
		this.addButton(button);
	}

	public EditField addIntField(String name, String unit, int nbcol, int defValue) {
		return new EditField(name, unit, nbcol, defValue, this);
	}

	public EditField addFloatField(String name, String unit, int nbcol, float defValue, String format) {
		return new EditField(name, unit, nbcol, defValue, format, this);
	}

	public SliderAndValue addSliderAndValue(String name, String unit, double min, double max, double def,
			int nbDecimals, boolean orientation, int size) {
		return new SliderAndValue(name, unit, min, max, def, nbDecimals, orientation, size, this);
	}

	public void addActionListener(ActionListener listener) {
		this.listener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.listener != null)
			if (e.getSource() instanceof SliderAndValue)
				this.listener.actionPerformed(e);
			else if (e.getSource() instanceof HVPanel)
				this.listener.actionPerformed(e);
			else if (e.getSource() instanceof AbstractButton)
				this.listener.actionPerformed(
						new ActionEvent(e.getSource(), ((AbstractButton) e.getSource()).isSelected() ? 1 : 0,
								((AbstractButton) e.getSource()).getText()));
			else if (e.getSource() instanceof EditField)
				this.listener.actionPerformed(e);
	}

	public static void main(String[] args) {
		HVPanel p1 = new HVPanel.v();
		p1.addIntField("alpha", "°", 3, 90);
		p1.addIntField("beta", "°", 3, 90);
		p1.addIntField("gamma", "°", 3, 90);
		p1.addFloatField("lala", "m", 3, 4f, "0.00");

		HVPanel p2 = new HVPanel.h();
		p2.addButtonGroupped(new JRadioButton("1"));
		p2.addButtonGroupped(new JRadioButton("2"));
		p2.addButtonGroupped(new JRadioButton("3"));
		p2.addButtonGroupped(new JRadioButton("4"));

		HVPanel p = new HVPanel.v();
		p.addComp(p1);
		p.addComp(p2);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(p);
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.pack();
		frame.show();
	}

	public class EditField implements ActionListener {
		private ActionListener listener;
		public JFormattedTextField edit;
		public JLabel name, unit;

		public EditField(String name, String unit, int nbcol, int defValue, ActionListener listener) {
			NumberFormatter f = new NumberFormatter(java.text.NumberFormat.getIntegerInstance());
			this.createField(name, unit, nbcol, new Integer(defValue), f, listener);
		}

		public EditField(String name, String unit, int nbcol, float defValue, String format, ActionListener listener) {
			DecimalFormat df = new DecimalFormat(format);
			DecimalFormatSymbols s = df.getDecimalFormatSymbols();
			s.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(s);
			NumberFormatter f = new NumberFormatter(df);
			this.createField(name, unit, nbcol, new Float(defValue), f, listener);
		}

		private void createField(String name, String unit, int nbcol, Object defValue, NumberFormatter format,
				ActionListener listener) {
			this.listener = listener;
			this.edit = new JFormattedTextField(format);
			this.edit.setValue(defValue);
			this.edit.setColumns(nbcol);
			this.edit.addActionListener(this);
			this.edit.setMinimumSize(new Dimension(30, 0));
			this.name = new JLabel(name);
			HVPanel.this.add(this.name, HVPanel.this.c());
			HVPanel.this.subIncrement();
			HVPanel.this.add(this.edit, HVPanel.this.c());
			HVPanel.this.subIncrement();
			if (unit != null) {
				this.unit = new JLabel(unit);
				HVPanel.this.add(this.unit, HVPanel.this.c());
				HVPanel.this.subIncrement();
			}
			HVPanel.this.increment();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.listener.actionPerformed(new ActionEvent(this, 0, this.name.getText()));
		}

		public float getValue() {
			return Float.parseFloat("" + this.edit.getValue());
		}

		public void setEnable(boolean b) {
			this.edit.setEditable(b);
			this.edit.setEnabled(b);
			this.name.setEnabled(b);
			this.unit.setEnabled(b);
		}
	}

	public class SliderAndValue implements ChangeListener, MouseWheelListener, ActionListener {
		private JSlider slider;
		private JFormattedTextField edit;
		private JLabel nameLabel, unitLabel;
		private ActionListener listener;
		private double mult;

		public SliderAndValue(String name, String unit, double min, double max, double def, int nbDecimals,
				boolean orientation, int size, ActionListener listener) {
			this.listener = listener;
			this.mult = Math.pow(10, nbDecimals);
			this.slider = new JSlider(orientation ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL,
					(int) Math.round(min * this.mult), (int) Math.round(max * this.mult),
					(int) Math.round(def * this.mult));
			this.slider.setMinimumSize(new Dimension(orientation ? size : 0, orientation ? 0 : size));

			if (name != null && name.length() != 0) {
				HVPanel.this.add(this.nameLabel = new JLabel(name), HVPanel.this.c());
				this.nameLabel.addMouseWheelListener(this);
				HVPanel.this.subIncrement();
			}

			HVPanel.this.add(this.slider, HVPanel.this.c());
			this.slider.addChangeListener(this);
			this.slider.addMouseWheelListener(this);
			HVPanel.this.subIncrement();

			if (unit != null) {
				StringBuffer sf = new StringBuffer("0");
				if (nbDecimals > 0)
					sf.append('.');
				for (int i = 0; i < nbDecimals; i++)
					sf.append('0');
				DecimalFormat df = new DecimalFormat(sf.toString());
				DecimalFormatSymbols s = df.getDecimalFormatSymbols();
				s.setDecimalSeparator('.');
				df.setDecimalFormatSymbols(s);
				NumberFormatter formatter = new NumberFormatter(df);
				formatter.setMinimum(new Double(min));
				formatter.setMaximum(new Double(max));
				this.edit = new JFormattedTextField(formatter);
				this.edit.setValue(new Double(def));
				this.edit.setColumns(3);
				this.edit.setMinimumSize(new Dimension(30, 0));
				HVPanel.this.add(this.edit, HVPanel.this.c());
				this.edit.addActionListener(this);
				this.edit.addMouseWheelListener(this);
				HVPanel.this.subIncrement();
				if (unit.length() != 0) {
					HVPanel.this.add(this.unitLabel = new JLabel(unit), HVPanel.this.c());
					this.unitLabel.addMouseWheelListener(this);
				}
			}
			HVPanel.this.increment();
		}

		public void setValue(double v) {
			this.slider.setValue((int) Math.round(v * this.mult));
		}

		public double getValue() {
			return this.slider.getValue() / this.mult;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			double v = this.getValue();
			if (this.edit != null) {
				this.edit.setValue(new Double(v));
				try {
					this.edit.commitEdit();
				} catch (Exception ex) {
				}
			}
			if (this.listener != null)
				this.listener.actionPerformed(
						new ActionEvent(this, 0, this.nameLabel == null ? null : (this.nameLabel.getText())));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			this.setValue(this.getValue() + e.getWheelRotation() / this.mult);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.setValue(((Double) this.edit.getValue()).doubleValue());
		}
	}
}