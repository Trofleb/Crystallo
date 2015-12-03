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
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

public abstract class HVPanel extends JPanel implements ActionListener {
	private ButtonGroup group;
	private ActionListener listener;
	
	public HVPanel() {
		super();
		setLayout(new GridBagLayout());
	}
	
	public static class h extends HVPanel {
		private GridBagConstraints c;
		public h() {
			super();
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridx=c.gridy=0; 
			c.weightx=c.weighty=1;
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
		public void top() {
			c().anchor = GridBagConstraints.NORTH;
		}
		public void bottom() {
			c().anchor = GridBagConstraints.SOUTH;
		}
	}	
	
	
	public static class v extends HVPanel {
		private GridBagConstraints c;
		public v() {
			super();
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets = new Insets(0,2,0,2);  
			c.gridx=c.gridy=0; 
			c.weightx=c.weighty=1;
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
		public void left() {
			c().anchor = GridBagConstraints.WEST;
		}
		public void right() {
			c().anchor = GridBagConstraints.EAST;
		}
	}	

	protected abstract void increment();
	protected abstract void subIncrement();
	public abstract GridBagConstraints c();

	public void expandH(boolean b) {
		if (c().fill==GridBagConstraints.BOTH || c().fill==GridBagConstraints.VERTICAL) {
			c().fill = b?GridBagConstraints.BOTH:GridBagConstraints.VERTICAL;
		} else {
			c().fill = b?GridBagConstraints.HORIZONTAL:GridBagConstraints.NONE;
		}
		c().weightx = b?1:0;
	}
	public void expandV(boolean b) {
		if (c().fill==GridBagConstraints.BOTH || c().fill==GridBagConstraints.HORIZONTAL) {
			c().fill = b?GridBagConstraints.BOTH:GridBagConstraints.HORIZONTAL;
		} else {
			c().fill = b?GridBagConstraints.VERTICAL:GridBagConstraints.NONE;
		}
		c().weighty = b?1:0;
	}
	
	
	public void addComp(Component comp) {
		add(comp, c());
		increment();
	}
	
	public void addSubPane(HVPanel panel, boolean h, boolean v) {
		int f = c().fill;
		double wx = c().weightx;
		double wy = c().weighty;
		expandV(v);
		expandH(h);
		addSubPane(panel);
		c().fill = f;
		c().weightx = wx;
		c().weighty = wy;
	}
	
	
	public void addSubPane(HVPanel panel) {
		addComp(panel);
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
	

	public EditField addIntField(String name, String unit, int nbcol, int defValue) {
		return new EditField(name, unit, nbcol, defValue, this);
	}

	public EditField addFloatField(String name, String unit, int nbcol, float defValue, String format) {
		return new EditField(name, unit, nbcol, defValue, format, this);
	}

	public SliderAndValue addSliderAndValue(String name, String unit, double min, double max, double def, int nbDecimals, boolean orientation, int size) {
		return new SliderAndValue(name, unit, min, max, def, nbDecimals, orientation, size, this);
	}
	
	public void addActionListener(ActionListener listener) {
		this.listener = listener;
	}

	public void actionPerformed(ActionEvent e) {
		if (listener!=null) {
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
			createField(name, unit, nbcol, new Integer(defValue), f, listener);
		}
		
		public EditField(String name, String unit, int nbcol, float defValue, String format, ActionListener listener) {
			DecimalFormat df = new DecimalFormat(format);
			DecimalFormatSymbols s = df.getDecimalFormatSymbols();
			s.setDecimalSeparator('.');
			df.setDecimalFormatSymbols(s);
			NumberFormatter f = new NumberFormatter(df);
			createField(name, unit, nbcol, new Float(defValue), f, listener);
		}

		private void createField(String name, String unit, int nbcol, Object defValue, NumberFormatter format, ActionListener listener) {
			this.listener = listener;
			edit = new JFormattedTextField(format);
			edit.setValue(defValue);
			edit.setColumns(nbcol);
			edit.addActionListener(this);
			edit.setMinimumSize(new Dimension(30, 0));
			this.name = new JLabel(name);
			add(this.name, c());
			subIncrement();
			add(edit, c());
			subIncrement();
			if (unit!=null) {
				this.unit = new JLabel(unit);
				add(this.unit, c());
				subIncrement();
			}
			increment();
		}

		public void actionPerformed(ActionEvent e) {
			listener.actionPerformed(new ActionEvent(this, 0, name.getText()));
		}
		
		public float getValue() {
			return Float.parseFloat(""+edit.getValue());
		}
		
		public void setEnable(boolean b) {
			edit.setEditable(b);
			edit.setEnabled(b);
			name.setEnabled(b);
			unit.setEnabled(b);
		}
	}	

	
	
	public class SliderAndValue implements ChangeListener, MouseWheelListener, ActionListener {
		private JSlider slider;
		private JFormattedTextField edit;
		private JLabel nameLabel, unitLabel;
		private ActionListener listener;
		private double mult;
		
		public SliderAndValue(String name, String unit, double min, double max, double def, int nbDecimals, boolean orientation, int size, ActionListener listener) {
			this.listener=listener;
			mult = Math.pow(10, nbDecimals);
			slider = new JSlider(orientation?JSlider.HORIZONTAL:JSlider.VERTICAL, (int)Math.round(min*mult), (int)Math.round(max*mult), (int)Math.round(def*mult));
			slider.setMinimumSize(new Dimension(orientation?size:0, orientation?0:size));
						
			if (name!=null && name.length()!=0) {
				add(nameLabel = new JLabel(name), c());
				nameLabel.addMouseWheelListener(this);
				subIncrement();
			}
			
			add(slider, c());
			slider.addChangeListener(this);
			slider.addMouseWheelListener(this);
			subIncrement();

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
				edit.setMinimumSize(new Dimension(30, 0));
				add(edit, c());
				edit.addActionListener(this);
				edit.addMouseWheelListener(this);
				subIncrement();
				if (unit.length()!=0) {
					add(unitLabel = new JLabel(unit), c());
					unitLabel.addMouseWheelListener(this);
				}
			}
			increment();
		}

		public void setValue(double v) {
			slider.setValue((int)Math.round(v*mult));
		}
		public double getValue() {
			return slider.getValue()/mult;
		}

		public void stateChanged(ChangeEvent e) { 
	  	double v = getValue();
			if (edit!=null)  {
				edit.setValue(new Double(v));
				try {
					edit.commitEdit();
				} catch (Exception ex) {}
			}
			if (listener!=null) listener.actionPerformed(new ActionEvent(this, 0, nameLabel==null?null:(nameLabel.getText())));
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			setValue(getValue()+e.getWheelRotation()/mult);
		}

		public void actionPerformed(ActionEvent e) { 
			setValue(((Double)edit.getValue()).doubleValue());
		}
	}
}