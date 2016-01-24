package utils;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.AbstractSpinnerModel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JSpinner.DefaultEditor;
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
		jPanel = new JPanel() {
			public void setVisible(boolean v){
				super.setVisible(v);
				HVPanel.this.setVisible(v);
			}
		};
		jPanel.setLayout(new GridBagLayout());
		listeners = new Vector(5, 5);
	}
	
	public HVPanel(String name) {
		this();
		setBorder(new TitledBorder(name));
	}
	
	public void setBorder(Border border) {
		jPanel.setBorder(border);
	}

	public void setVisible(boolean v){
	}
	
	public static class h extends HVPanel {
		private GridBagConstraints c;
		public h() {
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridx=c.gridy=0; 
			c.weightx=c.weighty=0.01;
		}
		public h(String name) {
			this();
			setBorder(new TitledBorder(name));
		}
		protected void increment() {
			c.gridx++;
			c.gridy=0; 
		}
		protected void subIncrement() {
			c.gridy++;
		}
		public GridBagConstraints c() {
			return c;
		}
		public void left() {
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 0.001;
		}
		public void right() {
			c.anchor = GridBagConstraints.EAST;
			c.weightx = 0.001;
		}
		public void center() {
			c.weightx=0;
		}
		public void putExtraSpace() {
			putExtraSpace(10);
		}
		public void putExtraSpace(int pixels) {
			if (c.fill==GridBagConstraints.BOTH || c.fill==GridBagConstraints.HORIZONTAL) {
				double wx = c.weightx;
				c.weightx = 1;
				addComp(new JPanel());
				c.weightx = wx;
			}
			else {
				int l = c.insets.left;
				c.insets.left = pixels;
				addComp(new JPanel());
				c.insets.left = l;
			}
		}
		public void expand(boolean b) {
			if (c.fill==GridBagConstraints.BOTH || c.fill==GridBagConstraints.VERTICAL) {
				c.fill = b?GridBagConstraints.BOTH:GridBagConstraints.VERTICAL;
			} else {
				c.fill = b?GridBagConstraints.HORIZONTAL:GridBagConstraints.NONE;
			}
			c.weightx = b?1:0.001;
		}
	}	
		
	public static class v extends HVPanel {
		private GridBagConstraints c;
		public v() {
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0,2,0,2);  
			c.gridx=c.gridy=0; 
			c.weightx=c.weighty=0.01;
		}
		public v(String name) {
			this();
			setBorder(new TitledBorder(name));
		}
		protected void increment() {
			c.gridy++;
			c.gridx=0; 
		}
		protected void subIncrement() {
			c.gridx++;
		}
		public GridBagConstraints c() {
			return c;
		}
		public void top() {
			c.anchor = GridBagConstraints.NORTH;
			c.weighty = 0.001;
		}
		public void bottom() {
			c.anchor = GridBagConstraints.SOUTH;
			c.weighty = 0.001;
		}
		public void center() {
			c.weighty=0;
		}
		
		public void putExtraSpace() {
			putExtraSpace(10);
		}
		public void putExtraSpace(int pixels) {
			if (c.fill==GridBagConstraints.BOTH || c.fill==GridBagConstraints.VERTICAL) {
				double wy = c.weighty;
				c.weighty = 1;
				addComp(new JPanel());
				c.weighty = wy;
			}
			else {
				int t = c.insets.top;
				c.insets.top = pixels;
				addComp(new JPanel());
				c.insets.top = t;
			}
		}
		public void expand(boolean b) {
			if (c.fill==GridBagConstraints.BOTH || c.fill==GridBagConstraints.HORIZONTAL) {
				c.fill = b?GridBagConstraints.BOTH:GridBagConstraints.HORIZONTAL;
			} else {
				c.fill = b?GridBagConstraints.VERTICAL:GridBagConstraints.NONE;
			}
			c.weighty = b?1:0.001;
		}
	}	

	public void fillSpace() {
		expand(true);
		addComp(new JPanel());
		expand(false);
	}
	
	protected abstract void increment();
	protected abstract void subIncrement();
	public abstract GridBagConstraints c();
	public abstract void expand(boolean b);
	public abstract void center();

	
	
	public void addComp(Component comp) {
		jPanel.add(comp, c());
		increment();
	}
	
	public void addSubPane(HVPanel panel) {
		addComp(panel.jPanel);
		panel.addActionListener(this);
	}

	public void addButton(AbstractButton button) {
		addComp(button);
		button.addActionListener(this);
	}
	public void addButtonGroupped(AbstractButton button) {
		if (group==null) {
			group = new ButtonGroup();
			button.setSelected(true);
		}
		group.add(button);
		addButton(button);
	}
	
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}

	public void actionPerformed(ActionEvent e) {
		for (int i=0; i<listeners.size(); i++) {
			ActionListener listener = (ActionListener)listeners.get(i);
			if (e.getSource() instanceof SliderAndValue)
				listener.actionPerformed(e);
			else if (e.getSource() instanceof HVPanel)
				listener.actionPerformed(e);
			else if (e.getSource() instanceof AbstractButton)
				listener.actionPerformed(new ActionEvent(e.getSource(), ((AbstractButton)e.getSource()).isSelected()?1:0, ((AbstractButton)e.getSource()).getText()));
			else if (e.getSource() instanceof EditField)
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
			edit = new JFormattedTextField(format);
			edit.setValue(defValue);
			edit.addPropertyChangeListener(this);
			edit.setColumns(nbcol);
			//edit.setPreferredSize(new Dimension(20, 0));
			//edit.setMinimumSize(new Dimension(20, 0));
			if (name!=null) this.name = new JLabel(name);
			if (unit!=null) this.unit = new JLabel(unit);
			component = edit;
		}
		
		public void putTo(HVPanel p) {
			if (name!=null) {
				p.jPanel.add(name, p.c());
			}
			p.c().gridx++;
			p.jPanel.add(component, p.c());
			p.c().gridx++;
			if (unit!=null) {
				p.jPanel.add(unit, p.c());
			}
			p.c().gridx++;
			p.increment();
		}

		public void propertyChange(PropertyChangeEvent e) {
			if (!quiet && e.getPropertyName().equals("value") && !edit.getText().equals(e.getNewValue())) {
				if (listener!=null) listener.actionPerformed(new ActionEvent(this, 0, name==null?"":name.getText()));
			}
		}
		
		protected void setListener(ActionListener listener) {
			this.listener = listener;
		}
		public Object getValue() {
			return edit.getValue();
		}
		public void setValue(Object v) {
			edit.setValue(v);
		}
		public void setMaximum(Object max) {
			NumberFormatter f = (NumberFormatter) edit.getFormatter();
			f.setMaximum((Comparable)max);
		}
		public void setMinimum(Object min) {
			NumberFormatter f = (NumberFormatter) edit.getFormatter();
			f.setMinimum((Comparable)min);
		}
		public void setEnable(boolean b) {
			edit.setEditable(b);
			edit.setEnabled(b);
			if (name!=null) name.setEnabled(b);
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
			return (String)super.getValue();
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
			super(name, unit, nbcol, new Integer(defValue), new NumberFormatter(java.text.NumberFormat.getIntegerInstance()));
		}
		public int getIntValue() {
			return Integer.parseInt(""+super.getValue());
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
			DecimalFormat df = new DecimalFormat(format);
			DecimalFormatSymbols s = df.getDecimalFormatSymbols();
			s.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(s);
			return new NumberFormatter(df);
		}
		public float getFloatValue() {
			Object o = super.getValue();
			if (o instanceof Double) return ((Double)super.getValue()).floatValue();
			if (o instanceof Float) return ((Float)super.getValue()).floatValue();
			return Float.parseFloat(""+o);
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
			this.step=step;
			this.edit=edit;
		}
		public void setValue(Object value) {
			edit.setValue(value);
		}
		public Object getValue() {
			return edit.getValue();
		}
		public Object getNextValue() {
			Object o = getValue();
			Object r = o;
			if (o instanceof Long) r = new Long(((Long)o).longValue()+(long)step);
			if (o instanceof Integer) r = new Integer(((Integer)o).intValue()+(int)step);
			if (o instanceof Float) r = new Float(((Float)o).floatValue()+(float)step);
			if (o instanceof Double) r = new Double(((Double)o).doubleValue()+(long)step);
			Object max = ((NumberFormatter)edit.getFormatter()).getMaximum();
			if (max!=null && ((Comparable)r).compareTo(max)>0) r = max;
			return r;
		}
		public Object getPreviousValue() {
			Object o = getValue();
			Object r = o;
			if (o instanceof Long) r = new Long(((Long)o).longValue()-(long)step);
			if (o instanceof Integer) r = new Integer(((Integer)o).intValue()-(int)step);
			if (o instanceof Float) r = new Float(((Float)o).floatValue()-(float)step);
			if (o instanceof Double) r = new Double(((Double)o).doubleValue()-(long)step);
			Object min = ((NumberFormatter)edit.getFormatter()).getMinimum();
			if (min!=null && ((Comparable)r).compareTo(min)<0) r = min;
			return r;
		}
	}
	
	public static class IntSpinnerEditField extends IntEditField {
		public SteppedSpinnerModel model;
		public IntSpinnerEditField(String name, String unit, int nbcol, int defValue) {
			super(name, unit, nbcol, defValue);
			model = new SteppedSpinnerModel(edit, 1);
			component = new JSpinner(model);
			((JSpinner)component).setEditor(edit);
		}
		public void setEnable(boolean b) {
			super.setEnable(b);
			((JSpinner)component).setEnabled(b);
		}
	}
	public static class FloatSpinnerEditField extends FloatEditField {
		public SteppedSpinnerModel model;
		public FloatSpinnerEditField(String name, String unit, int nbcol, float defValue, String format, double step) {
			super(name, unit, nbcol, defValue, format);
			model = new SteppedSpinnerModel(edit, step);
			component = new JSpinner(model);
			((JSpinner)component).setEditor(edit);
		}
		public void setEnable(boolean b) {
			super.setEnable(b);
			((JSpinner)component).setEnabled(b);
		}
	}
	
	
	
	
	
	
	// *** Slider and value ***
	
	public static class SliderAndValueH extends SliderAndValue {
		public SliderAndValueH(String name, String unit, double min, double max, double def, int nbDecimals, int size) {
			super(name, unit, min, max, def, nbDecimals, true, size);	
		}
		public SliderAndValueH to(HVPanel p) {
			super.putTo(p);
			listener = p;
			return this;
		}
		protected void increment(GridBagConstraints c) {
			c.gridy++;
			c.gridx=0; 
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
			listener = p;
			return this;
		}
		protected void increment(GridBagConstraints c) {
			c.gridx++;
			c.gridy=0; 
		}
		protected void subIncrement(GridBagConstraints c) {
			c.gridy++;
		}
	}

//	public static class SliderAndValueSpinner extends SliderAndValue {
//		private JSpinner spinner;
//		public SliderAndValueSpinner(String name, String unit, double min, double max, double def, int nbDecimals, int size) {
//			super(name, unit, min, max, def, nbDecimals, true, size);	
//			spinner=IntSpinnerEditField.createSpinner(edit, .1);
//			slider.setMinimumSize(new Dimension(size, 60));
//			slider.setPreferredSize(new Dimension(size, 60));
//			slider.setSnapToTicks(true);
//			Hashtable labelTable = new Hashtable();
//			for (int i=-5; i<=5; i++) {
//				labelTable.put(new Integer(i*2), new JLabel(""+i) );
//			}
//			slider.setLabelTable(labelTable);
//			
//			
//			slider.setMajorTickSpacing(2);
//			slider.setMinorTickSpacing(1);
//			slider.setPaintTicks(true);
//			slider.setPaintLabels(true);
//			
//		}
//		public SliderAndValueSpinner to(HVPanel p) {
//			putTo(p);
//			listener = p;
//			return this;
//		}
//		protected void increment(GridBagConstraints c) {
//			c.gridx++;
//			c.gridy=0; 
//		}
//		protected void subIncrement(GridBagConstraints c) {
//			c.gridy++;
//		}
//		public void putTo(HVPanel p) {
//			if (nameLabel!=null) {
//				p.jPanel.add(nameLabel, p.c());
//				subIncrement(p.c());
//			}
//			p.jPanel.add(slider, p.c());
//			subIncrement(p.c());
//			
//			if (edit!=null) {
//				p.jPanel.add(spinner, p.c());
//				subIncrement(p.c());
//				if (unitLabel!=null) {
//					p.jPanel.add(unitLabel, p.c());
//				}
//			}
//			increment(p.c());
//		}
//	}
	
	public static abstract class SliderAndValue implements ChangeListener, MouseWheelListener, ActionListener, PropertyChangeListener {
		public JSlider slider;
		public JFormattedTextField edit;
		protected JLabel nameLabel, unitLabel;
		protected ActionListener listener;
		private double mult;
		
		public SliderAndValue(String name, String unit, double min, double max, double def, int nbDecimals, boolean orientation, int size) {
			mult = Math.pow(10, nbDecimals);
			slider = new JSlider(orientation?JSlider.HORIZONTAL:JSlider.VERTICAL, (int)Math.round(min*mult), (int)Math.round(max*mult), (int)Math.round(def*mult));
			slider.setMinimumSize(new Dimension(orientation?size:18, orientation?18:size));
			slider.setPreferredSize(new Dimension(orientation?size:18, orientation?18:size));
						
			if (name!=null && name.length()!=0) {
				nameLabel = new JLabel(name);
				nameLabel.addMouseWheelListener(this);
			}
			
			slider.addChangeListener(this);
			slider.addMouseWheelListener(this);

			if (unit!=null) {
				StringBuffer sf = new StringBuffer("0");
				if (nbDecimals>0) sf.append('.');
				for(int i=0; i<nbDecimals; i++) sf.append('0');
				DecimalFormat df = new DecimalFormat(sf.toString());
				DecimalFormatSymbols s = df.getDecimalFormatSymbols();
				s.setDecimalSeparator('.');
				df.setDecimalFormatSymbols(s);
				NumberFormatter formatter = new NumberFormatter(df);
				formatter.setMinimum(new Double(min));
				formatter.setMaximum(new Double(max));
				
				edit = new JFormattedTextField(formatter);
				edit.setValue(new Double(def));
				edit.setColumns(3);
				edit.setMinimumSize(new Dimension(40, 0));
				edit.addPropertyChangeListener(this);
				edit.addActionListener(this);
				edit.addMouseWheelListener(this);
				if (unit.length()!=0) {
					unitLabel = new JLabel(unit);
					unitLabel.addMouseWheelListener(this);
				}
			}
		}

		public void putTo(HVPanel p) {
			if (nameLabel!=null) {
				p.jPanel.add(nameLabel, p.c());
				subIncrement(p.c());
			}
			p.jPanel.add(slider, p.c());
			subIncrement(p.c());

			if (edit!=null) {
				p.jPanel.add(edit, p.c());
				subIncrement(p.c());
				if (unitLabel!=null) {
					p.jPanel.add(unitLabel, p.c());
				}
			}
			increment(p.c());
		}
		
		protected abstract void subIncrement(GridBagConstraints c);
		protected abstract void increment(GridBagConstraints c);
		
		public void setValue(double v) {
			slider.setValue((int)Math.round(v*mult));
		}
		public double getValue() {
			return slider.getValue()/mult;
		}
		public double getMin() {
			return ((Double)((NumberFormatter)edit.getFormatter()).getMinimum()).doubleValue();
		}
		public double getMax() {
			return ((Double)((NumberFormatter)edit.getFormatter()).getMaximum()).doubleValue();
		}
		
		public void setEnabled(boolean enabled) {
			slider.setEnabled(enabled);
			if (edit!=null) edit.setEnabled(enabled);
		}
		
		public void stateChanged(ChangeEvent e) { 
			//if (((JSlider )e.getSource()).getValueIsAdjusting()) return;
			
	  	double v = getValue();
			if (edit!=null)  {
				edit.setValue(new Double(v));
				try {
					edit.commitEdit();
				} catch (Exception ex) {}
			}
			if (!quiet && listener!=null) listener.actionPerformed(new ActionEvent(this, 0, nameLabel==null?null:(nameLabel.getText())));
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			setValue(getValue()+e.getWheelRotation()/mult);
		}

		public void actionPerformed(ActionEvent e) { 
			System.out.println("actionPerformed");
			setValue(((Double)edit.getValue()).doubleValue());
		}
		public void propertyChange(PropertyChangeEvent evt) {
			System.out.println("propertyChange");
			if (!quiet && evt.getPropertyName().equals("value") && !edit.getText().equals(evt.getNewValue())) {
				setValue(((Double)edit.getValue()).doubleValue());
			}
		}
	}
}