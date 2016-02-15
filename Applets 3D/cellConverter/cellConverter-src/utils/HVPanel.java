package utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.AbstractSpinnerModel;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;

public abstract class HVPanel implements ActionListener {
	public static boolean quiet = false;
	private ButtonGroup group;
	private Vector listeners;
	public JPanel jPanel;

	public HVPanel() {
		this.jPanel = new JPanel();
		this.jPanel.setLayout(new GridBagLayout());
		this.listeners = new Vector(5, 5);
	}

	public HVPanel(String name) {
		this();
		this.setBorder(new TitledBorder(name));
	}

	public void setBorder(Border border) {
		this.jPanel.setBorder(border);
	}

	public static class h extends HVPanel {
		private GridBagConstraints c;

		public h() {
			this.c = new GridBagConstraints();
			this.c.fill = GridBagConstraints.BOTH;
			this.c.gridx = this.c.gridy = 0;
			this.c.weightx = this.c.weighty = 0.01;
		}

		public h(String name) {
			this();
			this.setBorder(new TitledBorder(name));
		}

		protected void increment() {
			this.c.gridx++;
			this.c.gridy = 0;
		}

		protected void subIncrement() {
			this.c.gridy++;
		}

		public GridBagConstraints c() {
			return this.c;
		}

		public void left() {
			this.c.anchor = GridBagConstraints.WEST;
			this.c.weightx = 0.001;
		}

		public void right() {
			this.c.anchor = GridBagConstraints.EAST;
			this.c.weightx = 0.001;
		}

		public void center() {
			this.c.weightx = 0;
		}

		public void putExtraSpace() {
			this.putExtraSpace(10);
		}

		public void putExtraSpace(int pixels) {
			if (this.c.fill == GridBagConstraints.BOTH || this.c.fill == GridBagConstraints.HORIZONTAL) {
				double wx = this.c.weightx;
				this.c.weightx = 1;
				this.addComp(new JPanel());
				this.c.weightx = wx;
			} else {
				int l = this.c.insets.left;
				this.c.insets.left = pixels;
				this.addComp(new JPanel());
				this.c.insets.left = l;
			}
		}

		public void expand(boolean b) {
			if (this.c.fill == GridBagConstraints.BOTH || this.c.fill == GridBagConstraints.VERTICAL)
				this.c.fill = b ? GridBagConstraints.BOTH : GridBagConstraints.VERTICAL;
			else
				this.c.fill = b ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
			this.c.weightx = b ? 1 : 0.001;
		}
	}

	public static class v extends HVPanel {
		private GridBagConstraints c;

		public v() {
			this.c = new GridBagConstraints();
			this.c.fill = GridBagConstraints.BOTH;
			this.c.insets = new Insets(0, 2, 0, 2);
			this.c.gridx = this.c.gridy = 0;
			this.c.weightx = this.c.weighty = 0.01;
		}

		public v(String name) {
			this();
			this.setBorder(new TitledBorder(name));
		}

		protected void increment() {
			this.c.gridy++;
			this.c.gridx = 0;
		}

		protected void subIncrement() {
			this.c.gridx++;
		}

		public GridBagConstraints c() {
			return this.c;
		}

		public void top() {
			this.c.anchor = GridBagConstraints.NORTH;
			this.c.weighty = 0.001;
		}

		public void bottom() {
			this.c.anchor = GridBagConstraints.SOUTH;
			this.c.weighty = 0.001;
		}

		public void center() {
			this.c.weighty = 0;
		}

		public void putExtraSpace() {
			this.putExtraSpace(10);
		}

		public void putExtraSpace(int pixels) {
			if (this.c.fill == GridBagConstraints.BOTH || this.c.fill == GridBagConstraints.VERTICAL) {
				double wy = this.c.weighty;
				this.c.weighty = 1;
				this.addComp(new JPanel());
				this.c.weighty = wy;
			} else {
				int t = this.c.insets.top;
				this.c.insets.top = pixels;
				this.addComp(new JPanel());
				this.c.insets.top = t;
			}
		}

		public void expand(boolean b) {
			if (this.c.fill == GridBagConstraints.BOTH || this.c.fill == GridBagConstraints.HORIZONTAL)
				this.c.fill = b ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
			else
				this.c.fill = b ? GridBagConstraints.VERTICAL : GridBagConstraints.NONE;
			this.c.weighty = b ? 1 : 0.001;
		}
	}

	public void fillSpace() {
		this.expand(true);
		this.addComp(new JPanel());
		this.expand(false);
	}

	protected abstract void increment();

	protected abstract void subIncrement();

	public abstract GridBagConstraints c();

	public abstract void expand(boolean b);

	public abstract void center();

	public void addComp(Component comp) {
		this.jPanel.add(comp, this.c());
		this.increment();
	}

	public void addSubPane(HVPanel panel) {
		this.addComp(panel.jPanel);
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

	public void addActionListener(ActionListener listener) {
		this.listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		this.listeners.remove(listener);
	}

	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < this.listeners.size(); i++) {
			ActionListener listener = (ActionListener) this.listeners.get(i);
			if (e.getSource() instanceof AbstractButton)
				listener.actionPerformed(
						new ActionEvent(e.getSource(), ((AbstractButton) e.getSource()).isSelected() ? 1 : 0,
								((AbstractButton) e.getSource()).getText()));
			else
				listener.actionPerformed(e);
		}
	}

	// *** Edit field ***

	public abstract static class EditField implements PropertyChangeListener {
		public JFormattedTextField edit;
		protected JLabel name, unit;
		protected Component component;
		private ActionListener listener;

		public EditField(String name, String unit, int nbcol, Object defValue, DefaultFormatter format) {
			this.edit = new JFormattedTextField(format);
			this.edit.setValue(defValue);
			this.edit.addPropertyChangeListener(this);
			this.edit.setColumns(nbcol);
			// edit.setPreferredSize(new Dimension(20, 0));
			// edit.setMinimumSize(new Dimension(20, 0));
			if (name != null)
				this.name = new JLabel(name);
			if (unit != null)
				this.unit = new JLabel(unit);
			this.component = this.edit;
		}

		public void putTo(HVPanel p) {
			if (this.name != null)
				p.jPanel.add(this.name, p.c());
			p.c().gridx++;
			p.jPanel.add(this.component, p.c());
			p.c().gridx++;
			if (this.unit != null)
				p.jPanel.add(this.unit, p.c());
			p.c().gridx++;
			p.increment();
		}

		public void propertyChange(PropertyChangeEvent e) {
			if (!quiet && e.getPropertyName().equals("value") && !this.edit.getText().equals(e.getNewValue()))
				if (this.listener != null)
					this.listener
							.actionPerformed(new ActionEvent(this, 0, this.name == null ? "" : this.name.getText()));
		}

		protected void setListener(ActionListener listener) {
			this.listener = listener;
		}

		public Object getValue() {
			return this.edit.getValue();
		}

		public void setValue(Object v) {
			this.edit.setValue(v);
		}

		public void setMaximum(Object max) {
			NumberFormatter f = (NumberFormatter) this.edit.getFormatter();
			f.setMaximum((Comparable) max);
		}

		public void setMinimum(Object min) {
			NumberFormatter f = (NumberFormatter) this.edit.getFormatter();
			f.setMinimum((Comparable) min);
		}

		public void setEnable(boolean b) {
			this.edit.setEditable(b);
			this.edit.setEnabled(b);
			if (this.name != null)
				this.name.setEnabled(b);
		}
	}

	public static class TextEditField extends EditField {
		public TextEditField(String name, String unit, int nbcol, String defValue) {
			super(name, unit, nbcol, defValue, new DefaultFormatter());
		}

		public TextEditField(String name, String unit, int nbcol, String defValue, DefaultFormatter format) {
			super(name, unit, nbcol, defValue, format);
		}

		public String getStringValue() {
			return (String) super.getValue();
		}

		public void setMaximum(int max) {
			throw new RuntimeException("setMaximum not available in TextEditField");
		}

		public void setMinimum(int min) {
			throw new RuntimeException("setMinimum not available in TextEditField");
		}

		public TextEditField to(HVPanel p) {
			super.putTo(p);
			this.setListener(p);
			return this;
		}
	}

	public static class IntEditField extends EditField {
		public IntEditField(String name, String unit, int nbcol, int defValue) {
			super(name, unit, nbcol, new Integer(defValue),
					new NumberFormatter(java.text.NumberFormat.getIntegerInstance()));
		}

		public int getIntValue() {
			return Integer.parseInt("" + super.getValue());
		}

		public void setValue(int v) {
			super.setValue(new Integer(v));
		}

		public void setMaximum(int max) {
			super.setMaximum(new Integer(max));
		}

		public void setMinimum(int min) {
			super.setMinimum(new Integer(min));
		}

		public IntEditField to(HVPanel p) {
			super.putTo(p);
			this.setListener(p);
			return this;
		}
	}

	public static class FloatEditField extends EditField {
		public FloatEditField(String name, String unit, int nbcol, float defValue, String format) {
			super(name, unit, nbcol, new Float(defValue), createFormatter(format));
		}

		protected static NumberFormatter createFormatter(String format) {
			DecimalFormat df = new DecimalFormat(format) {
				public Number parse(String text, ParsePosition pos) {
					return super.parse(text.trim(), pos);
				}
			};
			DecimalFormatSymbols s = df.getDecimalFormatSymbols();
			s.setDecimalSeparator('.');
			s.setNaN(" ");
			df.setDecimalFormatSymbols(s);
			return new NumberFormatter(df);
		}

		public float getFloatValue() {
			Object o = super.getValue();
			if (o instanceof Double)
				return ((Double) super.getValue()).floatValue();
			if (o instanceof Float)
				return ((Float) super.getValue()).floatValue();
			return Float.parseFloat("" + o);
		}

		public void setValue(float v) {
			super.setValue(new Float(v));
		}

		public void setMaximum(float max) {
			super.setMaximum(new Float(max));
		}

		public void setMinimum(float min) {
			super.setMinimum(new Float(min));
		}

		public FloatEditField to(HVPanel p) {
			super.putTo(p);
			this.setListener(p);
			return this;
		}
	}

	public static class SteppedSpinnerModel extends AbstractSpinnerModel {
		public double step;
		private JFormattedTextField edit;

		public SteppedSpinnerModel(JFormattedTextField edit, double step) {
			this.step = step;
			this.edit = edit;
		}

		public void setValue(Object value) {
			this.edit.setValue(value);
		}

		public Object getValue() {
			return this.edit.getValue();
		}

		public Object getNextValue() {
			Object o = this.getValue();
			Object r = o;
			if (o instanceof Long)
				r = new Long(((Long) o).longValue() + (long) this.step);
			if (o instanceof Integer)
				r = new Integer(((Integer) o).intValue() + (int) this.step);
			if (o instanceof Float)
				r = new Float(((Float) o).floatValue() + (float) this.step);
			if (o instanceof Double)
				r = new Double(((Double) o).doubleValue() + (long) this.step);
			Object max = ((NumberFormatter) this.edit.getFormatter()).getMaximum();
			if (max != null && ((Comparable) r).compareTo(max) > 0)
				r = max;
			return r;
		}

		public Object getPreviousValue() {
			Object o = this.getValue();
			Object r = o;
			if (o instanceof Long)
				r = new Long(((Long) o).longValue() - (long) this.step);
			if (o instanceof Integer)
				r = new Integer(((Integer) o).intValue() - (int) this.step);
			if (o instanceof Float)
				r = new Float(((Float) o).floatValue() - (float) this.step);
			if (o instanceof Double)
				r = new Double(((Double) o).doubleValue() - (long) this.step);
			Object min = ((NumberFormatter) this.edit.getFormatter()).getMinimum();
			if (min != null && ((Comparable) r).compareTo(min) < 0)
				r = min;
			return r;
		}
	}

	public static class IntSpinnerEditField extends IntEditField {
		public SteppedSpinnerModel model;

		public IntSpinnerEditField(String name, String unit, int nbcol, int defValue) {
			super(name, unit, nbcol, defValue);
			this.model = new SteppedSpinnerModel(this.edit, 1);
			this.component = new JSpinner(this.model);
			((JSpinner) this.component).setEditor(this.edit);
		}

		public void setEnable(boolean b) {
			super.setEnable(b);
			((JSpinner) this.component).setEnabled(b);
		}
	}

	public static class FloatSpinnerEditField extends FloatEditField {
		public SteppedSpinnerModel model;

		public FloatSpinnerEditField(String name, String unit, int nbcol, float defValue, String format, double step) {
			super(name, unit, nbcol, defValue, format);
			this.model = new SteppedSpinnerModel(this.edit, step);
			this.component = new JSpinner(this.model);
			((JSpinner) this.component).setEditor(this.edit);
		}

		public void setEnable(boolean b) {
			super.setEnable(b);
			((JSpinner) this.component).setEnabled(b);
		}
	}

	// *** Slider and value ***

	public static class SliderAndValueH extends SliderAndValue {
		public SliderAndValueH(String name, String unit, double min, double max, double def, int nbDecimals, int size) {
			super(name, unit, min, max, def, nbDecimals, true, size);
		}

		public SliderAndValueH to(HVPanel p) {
			super.putTo(p);
			this.listener = p;
			return this;
		}

		protected void increment(GridBagConstraints c) {
			c.gridy++;
			c.gridx = 0;
		}

		protected void subIncrement(GridBagConstraints c) {
			c.gridx++;
		}
	}

	public static class SliderAndValueV extends SliderAndValue {
		public SliderAndValueV(String name, String unit, double min, double max, double def, int nbDecimals, int size) {
			super(name, unit, min, max, def, nbDecimals, false, size);
		}

		public SliderAndValueV to(HVPanel p) {
			super.putTo(p);
			this.listener = p;
			return this;
		}

		protected void increment(GridBagConstraints c) {
			c.gridx++;
			c.gridy = 0;
		}

		protected void subIncrement(GridBagConstraints c) {
			c.gridy++;
		}
	}

	// public static class SliderAndValueSpinner extends SliderAndValue {
	// private JSpinner spinner;
	// public SliderAndValueSpinner(String name, String unit, double min, double
	// max, double def, int nbDecimals, int size) {
	// super(name, unit, min, max, def, nbDecimals, true, size);
	// spinner=IntSpinnerEditField.createSpinner(edit, .1);
	// slider.setMinimumSize(new Dimension(size, 60));
	// slider.setPreferredSize(new Dimension(size, 60));
	// slider.setSnapToTicks(true);
	// Hashtable labelTable = new Hashtable();
	// for (int i=-5; i<=5; i++) {
	// labelTable.put(new Integer(i*2), new JLabel(""+i) );
	// }
	// slider.setLabelTable(labelTable);
	//
	//
	// slider.setMajorTickSpacing(2);
	// slider.setMinorTickSpacing(1);
	// slider.setPaintTicks(true);
	// slider.setPaintLabels(true);
	//
	// }
	// public SliderAndValueSpinner to(HVPanel p) {
	// putTo(p);
	// listener = p;
	// return this;
	// }
	// protected void increment(GridBagConstraints c) {
	// c.gridx++;
	// c.gridy=0;
	// }
	// protected void subIncrement(GridBagConstraints c) {
	// c.gridy++;
	// }
	// public void putTo(HVPanel p) {
	// if (nameLabel!=null) {
	// p.jPanel.add(nameLabel, p.c());
	// subIncrement(p.c());
	// }
	// p.jPanel.add(slider, p.c());
	// subIncrement(p.c());
	//
	// if (edit!=null) {
	// p.jPanel.add(spinner, p.c());
	// subIncrement(p.c());
	// if (unitLabel!=null) {
	// p.jPanel.add(unitLabel, p.c());
	// }
	// }
	// increment(p.c());
	// }
	// }

	public static abstract class SliderAndValue
			implements ChangeListener, MouseWheelListener, ActionListener, PropertyChangeListener {
		public JSlider slider;
		public JFormattedTextField edit;
		protected JLabel nameLabel, unitLabel;
		protected ActionListener listener;
		private double mult;

		public SliderAndValue(String name, String unit, double min, double max, double def, int nbDecimals,
				boolean orientation, int size) {
			this.mult = Math.pow(10, nbDecimals);
			this.slider = new JSlider(orientation ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL,
					(int) Math.round(min * this.mult), (int) Math.round(max * this.mult),
					(int) Math.round(def * this.mult));
			this.slider.setMinimumSize(new Dimension(orientation ? size : 18, orientation ? 18 : size));
			this.slider.setPreferredSize(new Dimension(orientation ? size : 18, orientation ? 18 : size));

			if (name != null && name.length() != 0) {
				this.nameLabel = new JLabel(name);
				this.nameLabel.addMouseWheelListener(this);
			}

			this.slider.addChangeListener(this);
			this.slider.addMouseWheelListener(this);

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
				this.edit.setMinimumSize(new Dimension(40, 0));
				this.edit.addPropertyChangeListener(this);
				this.edit.addActionListener(this);
				this.edit.addMouseWheelListener(this);
				if (unit.length() != 0) {
					this.unitLabel = new JLabel(unit);
					this.unitLabel.addMouseWheelListener(this);
				}
			}
		}

		public void putTo(HVPanel p) {
			if (this.nameLabel != null) {
				p.jPanel.add(this.nameLabel, p.c());
				this.subIncrement(p.c());
			}
			p.jPanel.add(this.slider, p.c());
			this.subIncrement(p.c());

			if (this.edit != null) {
				p.jPanel.add(this.edit, p.c());
				this.subIncrement(p.c());
				if (this.unitLabel != null)
					p.jPanel.add(this.unitLabel, p.c());
			}
			this.increment(p.c());
		}

		protected abstract void subIncrement(GridBagConstraints c);

		protected abstract void increment(GridBagConstraints c);

		public void setValue(double v) {
			this.slider.setValue((int) Math.round(v * this.mult));
		}

		public double getValue() {
			return this.slider.getValue() / this.mult;
		}

		public double getMin() {
			return ((Double) ((NumberFormatter) this.edit.getFormatter()).getMinimum()).doubleValue();
		}

		public double getMax() {
			return ((Double) ((NumberFormatter) this.edit.getFormatter()).getMaximum()).doubleValue();
		}

		public void setEnabled(boolean enabled) {
			this.slider.setEnabled(enabled);
			if (this.edit != null)
				this.edit.setEnabled(enabled);
		}

		public void stateChanged(ChangeEvent e) {
			// if (((JSlider )e.getSource()).getValueIsAdjusting()) return;

			double v = this.getValue();
			if (this.edit != null) {
				this.edit.setValue(new Double(v));
				try {
					this.edit.commitEdit();
				} catch (Exception ex) {
				}
			}
			if (!quiet && this.listener != null)
				this.listener.actionPerformed(
						new ActionEvent(this, 0, this.nameLabel == null ? null : (this.nameLabel.getText())));
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			this.setValue(this.getValue() + e.getWheelRotation() / this.mult);
		}

		public void actionPerformed(ActionEvent e) {
			System.out.println("actionPerformed");
			this.setValue(((Double) this.edit.getValue()).doubleValue());
		}

		public void propertyChange(PropertyChangeEvent evt) {
			System.out.println("propertyChange");
			if (!quiet && evt.getPropertyName().equals("value") && !this.edit.getText().equals(evt.getNewValue()))
				this.setValue(((Double) this.edit.getValue()).doubleValue());
		}
	}
}