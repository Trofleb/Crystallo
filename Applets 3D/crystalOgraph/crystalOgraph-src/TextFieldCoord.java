import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;

/*
 * Created on 18 juin 2004
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class TextFieldCoord extends JPanel implements ActionListener {
	public JTextField x, y, z;
	private ActionListener actionListener;
	GridBagConstraints c;

	public static class v extends TextFieldCoord {
		public v(String[] s, String u) {
			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 10, 0, 0);
			this.add(new MultiLineToolTip.JLabel(s[0]), c);
			c.insets = new Insets(0, 10, 0, 0);
			this.x = new MultiLineToolTip.JTextField();
			this.x.setColumns(4);
			this.x.addActionListener(this);
			c.weightx = 1.0;
			c.gridx = 1;
			c.gridy = 0;
			this.add(this.x, c);

			c.insets = new Insets(0, 0, 0, 10);
			c.weightx = 0.0;
			c.gridx = 2;
			c.gridy = 0;
			this.add(new MultiLineToolTip.JLabel(u), c);

			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0, 10, 0, 0);
			this.add(new MultiLineToolTip.JLabel(s[1]), c);
			c.insets = new Insets(0, 10, 0, 0);
			this.y = new MultiLineToolTip.JTextField();
			this.y.setColumns(4);
			this.y.addActionListener(this);
			c.gridx = 1;
			c.gridy = 1;
			this.add(this.y, c);
			c.gridx = 2;
			c.gridy = 1;
			c.insets = new Insets(0, 0, 0, 10);
			this.add(new MultiLineToolTip.JLabel(u), c);

			c.gridx = 0;
			c.gridy = 2;
			c.insets = new Insets(0, 10, 0, 0);
			this.add(new MultiLineToolTip.JLabel(s[2]), c);
			c.insets = new Insets(0, 10, 0, 0);
			this.z = new MultiLineToolTip.JTextField();
			this.z.setColumns(4);
			this.z.addActionListener(this);
			c.gridx = 1;
			c.gridy = 2;
			this.add(this.z, c);
			c.gridx = 2;
			c.gridy = 2;
			c.insets = new Insets(0, 0, 0, 10);
			this.add(new MultiLineToolTip.JLabel(u), c);
		}
	}

	public static class h extends TextFieldCoord {
		public h(String[] s, int w) {
			this.setLayout(new GridBagLayout());
			this.c = new GridBagConstraints();
			this.c.fill = GridBagConstraints.HORIZONTAL;

			this.c.weightx = 0.0;
			this.c.gridy = 0;
			this.c.gridx = 0;
			this.add(new MultiLineToolTip.JLabel(s[0]), this.c);
			this.c.gridx = 2;
			this.add(new MultiLineToolTip.JLabel(s[1]), this.c);
			this.c.gridx = 4;
			this.add(new MultiLineToolTip.JLabel(s[2]), this.c);

			this.c.weightx = 1.0;
			this.x = new MultiLineToolTip.JTextField();
			this.x.setColumns(w);
			this.x.addActionListener(this);
			this.c.gridx = 1;
			this.add(this.x, this.c);

			this.y = new MultiLineToolTip.JTextField();
			this.y.setColumns(w);
			this.y.addActionListener(this);
			this.c.gridx = 3;
			this.add(this.y, this.c);

			this.z = new MultiLineToolTip.JTextField();
			this.z.setColumns(w);
			this.z.addActionListener(this);
			this.c.gridx = 5;
			this.add(this.z, this.c);
		}
	}

	public void setEditable(boolean bx, boolean by, boolean bz) {
		this.x.setEditable(bx);
		this.y.setEditable(by);
		this.z.setEditable(bz);
		this.x.setEnabled(bx);
		this.y.setEnabled(by);
		this.z.setEnabled(bz);
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (this.actionListener != null)
			this.actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}

	public Point3i getIntValue() throws BadEntry {
		try {
			int vx = Integer.parseInt(this.x.getText());
			int vy = Integer.parseInt(this.y.getText());
			int vz = Integer.parseInt(this.z.getText());
			return new Point3i(vx, vy, vz);
		} catch (NumberFormatException e) {
			throw new BadEntry();
		}
	}

	public Point3f getFloatValue() throws BadEntry {
		try {
			float vx = Float.parseFloat(this.x.getText());
			float vy = Float.parseFloat(this.y.getText());
			float vz = Float.parseFloat(this.z.getText());
			return new Point3f(vx, vy, vz);
		} catch (NumberFormatException e) {
			throw new BadEntry();
		}
	}

	public static float parseFraqValue(String s) throws BadEntry {
		try {
			int f = s.indexOf("/");
			if (f == -1)
				return Float.parseFloat(s);
			else
				return Float.parseFloat(s.substring(0, f)) / Float.parseFloat(s.substring(f + 1, s.length()));
		} catch (NumberFormatException e) {
			throw new BadEntry();
		}
	}

	public static Point3f parseFraqValue(String sx, String sy, String sz) throws BadEntry {
		return new Point3f(parseFraqValue(sx), parseFraqValue(sy), parseFraqValue(sz));
	}

	public Point3f parseFraqValue() throws BadEntry {
		return parseFraqValue(this.x.getText(), this.y.getText(), this.z.getText());
	}

	private float parse(String s) throws BadEntry {
		try {
			float val = 0;
			int p;
			if (s.length() == 0)
				return 0f;
			p = s.indexOf("pi");
			if (p == -1)
				p = s.indexOf("Pi");
			if (p == -1)
				p = s.indexOf("PI");
			if (p != -1) {
				val = 180f;
				if (p != 0)
					val *= this.parse(s.substring(0, p));
				if (p != s.length() - 2)
					val *= this.parse(s.substring(p + 2, s.length()));
				// System.out.println("pi "+parse(s.substring(0, p))+"
				// "+parse(s.substring(p+2, s.length())));
				return val;
			}
			p = s.indexOf("*");
			if (p != -1) {
				if (p == 0)
					// System.out.println("* r:"+parse(s.substring(p+1,
					// s.length())));
					return this.parse(s.substring(p + 1, s.length()));
				if (p == s.length() - 1)
					// System.out.println("* l:"+s.substring(0, p));
					return this.parse(s.substring(0, p));
				// System.out.println("* "+parse(s.substring(0, p)) +" "+
				// parse(s.substring(p+1, s.length())));
				return this.parse(s.substring(0, p)) * this.parse(s.substring(p + 1, s.length()));
			}

			p = s.indexOf("/");
			if (p != -1) {
				if (p == 0)
					// System.out.println("/ r:"+parse(s.substring(p+1,
					// s.length())));
					return 1f / this.parse(s.substring(p + 1, s.length()));
				if (p == s.length() - 1)
					throw new BadEntry();
				// System.out.println("/ "+parse(s.substring(0, p)) +" "+
				// parse(s.substring(p+1, s.length())));
				return this.parse(s.substring(0, p)) / this.parse(s.substring(p + 1, s.length()));
			}

			// System.out.println(Float.parseFloat(s));
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			throw new BadEntry();
		}
	}

	public Point3f parseAngleValue() throws BadEntry {
		// System.out.println("alpha:"+parse(x.getText()));
		return new Point3f(this.parse(this.x.getText()), this.parse(this.y.getText()), this.parse(this.z.getText()));
	}

	public void setValue(int ix, int iy, int iz) {
		this.setValue("" + ix, "" + iy, "" + iz);
	}

	public void setValue(float ix, float iy, float iz) {
		this.setValue("" + ix, "" + iy, "" + iz);
	}

	public void setValue(Point3d p) {
		this.setValue("" + p.x, "" + p.y, "" + p.z);
	}

	public void setValue(Point3f p) {
		this.setValue("" + p.x, "" + p.y, "" + p.z);
	}

	public void setValue(Point3i p) {
		this.setValue("" + p.x, "" + p.y, "" + p.z);
	}

	public void setValueX(int ix) {
		this.x.setText("" + ix);
	}

	public void setValueY(int iy) {
		this.y.setText("" + iy);
	}

	public void setValueZ(int iz) {
		this.z.setText("" + iz);
	}

	public void setValueX(float ix) {
		this.x.setText("" + ix);
	}

	public void setValueY(float iy) {
		this.y.setText("" + iy);
	}

	public void setValueZ(float iz) {
		this.z.setText("" + iz);
	}

	public void setValue(String sx, String sy, String sz) {
		this.x.setText(sx);
		this.y.setText(sy);
		this.z.setText(sz);
	}
}
