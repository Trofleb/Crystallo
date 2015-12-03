import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;

/*
 * Created on 18 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author nschoeni
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */




public class TextFieldCoord extends JPanel implements ActionListener {
	public JTextField x, y, z;
	private ActionListener actionListener;
	GridBagConstraints c;

	public static class v extends TextFieldCoord {
		public v(String[] s, String u) {
			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0,10,0,0);  
			add(new MultiLineToolTip.JLabel(s[0]), c);
			c.insets = new Insets(0,10,0,0);  
			x = new MultiLineToolTip.JTextField();
			x.setColumns(4);
			x.addActionListener(this);
			c.weightx = 1.0; 
			c.gridx = 1;
			c.gridy = 0;
			add(x, c);

			c.insets = new Insets(0,0,0,10);  
			c.weightx = 0.0; 
			c.gridx = 2;
			c.gridy = 0;
			add(new MultiLineToolTip.JLabel(u), c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0,10,0,0);  
			add(new MultiLineToolTip.JLabel(s[1]), c);
			c.insets = new Insets(0,10,0,0);  
			y = new MultiLineToolTip.JTextField();
			y.setColumns(4);
			y.addActionListener(this);
			c.gridx = 1;
			c.gridy = 1;
			add(y, c);
			c.gridx = 2;
			c.gridy = 1;
			c.insets = new Insets(0,0,0,10);  
			add(new MultiLineToolTip.JLabel(u), c);

			c.gridx = 0;
			c.gridy = 2;
			c.insets = new Insets(0,10,0,0);  
			add(new MultiLineToolTip.JLabel(s[2]), c);
			c.insets = new Insets(0,10,0,0);  
			z = new MultiLineToolTip.JTextField();
			z.setColumns(4);
			z.addActionListener(this);
			c.gridx = 1;
			c.gridy = 2;
			add(z, c);
			c.gridx = 2;
			c.gridy = 2;
			c.insets = new Insets(0,0,0,10);  
			add(new MultiLineToolTip.JLabel(u), c);
		}
	}
	
	public static class h extends TextFieldCoord {
		public h(String[] s, int w) {
			setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;

			c.weightx=0.0;
			c.gridy = 0;
			c.gridx = 0;
			add(new MultiLineToolTip.JLabel(s[0]), c);
			c.gridx = 2;
			add(new MultiLineToolTip.JLabel(s[1]), c);
			c.gridx = 4;
			add(new MultiLineToolTip.JLabel(s[2]), c);

			c.weightx=1.0;
			x = new MultiLineToolTip.JTextField();
			x.setColumns(w);
			x.addActionListener(this);
			c.gridx = 1;
			add(x, c);
			
			y = new MultiLineToolTip.JTextField();
			y.setColumns(w);
			y.addActionListener(this);
			c.gridx = 3;
			add(y, c);

			z = new MultiLineToolTip.JTextField();
			z.setColumns(w);
			z.addActionListener(this);
			c.gridx = 5;
			add(z, c);
		}
	}
	
	
	
	
	
	public void setEditable(boolean bx, boolean by, boolean bz) {
		x.setEditable(bx);
		y.setEditable(by);
		z.setEditable(bz);
		x.setEnabled(bx);
		y.setEnabled(by);
		z.setEnabled(bz);
	}

	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	public void actionPerformed(ActionEvent event){
		if (actionListener!=null)
			actionListener.actionPerformed(new ActionEvent(this, 0, ""));
	}

	public Point3i getIntValue() throws BadEntry {		
		try {
			int vx = Integer.parseInt(x.getText()); 
			int vy = Integer.parseInt(y.getText()); 
			int vz = Integer.parseInt(z.getText()); 
			return new Point3i(vx, vy, vz);
		}
		catch(NumberFormatException e) {
			throw new BadEntry();
		}
	}

	public Point3f getFloatValue() throws BadEntry {		
		try {
			float vx = Float.parseFloat(x.getText()); 
			float vy = Float.parseFloat(y.getText()); 
			float vz = Float.parseFloat(z.getText()); 
			return new Point3f(vx, vy, vz);
		}
		catch(NumberFormatException e) {
			throw new BadEntry();
		}
	}

	
	public static float parseFraqValue(String s) throws BadEntry {
		try {
			int f = s.indexOf("/");
			if (f==-1) return Float.parseFloat(s);
			else return Float.parseFloat(s.substring(0, f))/Float.parseFloat(s.substring(f+1, s.length()));
		}
		catch(NumberFormatException e) {
			throw new BadEntry();
		}
	}
	
	
	public static Point3f parseFraqValue(String sx, String sy, String sz) throws BadEntry {
			return new Point3f(parseFraqValue(sx), parseFraqValue(sy), parseFraqValue(sz));
	}

	public Point3f parseFraqValue() throws BadEntry {
		return parseFraqValue(x.getText(), y.getText(), z.getText());
	}
	
	
	private float parse(String s) throws BadEntry {
		try {
			float val=0;
			int p;
			if (s.length()==0) return 0f;
			p = s.indexOf("pi");
			if (p==-1) p = s.indexOf("Pi");
			if (p==-1) p = s.indexOf("PI");
			if (p!=-1) {
				val=180f;
				if (p!=0) val*=parse(s.substring(0, p));
				if (p!=s.length()-2) val*=parse(s.substring(p+2, s.length()));
				//System.out.println("pi "+parse(s.substring(0, p))+" "+parse(s.substring(p+2, s.length())));
				return val;
			}
			p = s.indexOf("*");
			if (p!=-1) {
				if (p==0) {
					//System.out.println("* r:"+parse(s.substring(p+1, s.length())));
					return parse(s.substring(p+1, s.length()));
				}
				if (p==s.length()-1) {
					//System.out.println("* l:"+s.substring(0, p));
					return parse(s.substring(0, p));
				}
				//System.out.println("* "+parse(s.substring(0, p)) +" "+ parse(s.substring(p+1, s.length())));
				return parse(s.substring(0, p)) * parse(s.substring(p+1, s.length()));
			}
	
			p = s.indexOf("/");
			if (p!=-1) {
				if (p==0) {
					//System.out.println("/ r:"+parse(s.substring(p+1, s.length())));
					return 1f / parse(s.substring(p+1, s.length()));
				}
				if (p==s.length()-1) {
					throw new BadEntry();
				}
				//System.out.println("/ "+parse(s.substring(0, p)) +" "+ parse(s.substring(p+1, s.length())));
				return parse(s.substring(0, p)) / parse(s.substring(p+1, s.length()));
			}
			
			//System.out.println(Float.parseFloat(s));	
			return Float.parseFloat(s);
		}
		catch(NumberFormatException e) {
			throw new BadEntry();
		}
	}
	
	public Point3f parseAngleValue() throws BadEntry {
		//System.out.println("alpha:"+parse(x.getText()));
		return new Point3f(parse(x.getText()), parse(y.getText()), parse(z.getText()));
	}	
	
	
	public void setValue(int ix, int iy, int iz) {
		setValue(""+ix, ""+iy, ""+iz);
	}

	public void setValue(float ix, float iy, float iz) {
		setValue(""+ix, ""+iy, ""+iz);
	}

	public void setValue(Point3d p) {
		setValue(""+p.x, ""+p.y, ""+p.z);
	}

	public void setValue(Point3f p) {
		setValue(""+p.x, ""+p.y, ""+p.z);
	}
	
	public void setValue(Point3i p) {
		setValue(""+p.x, ""+p.y, ""+p.z);
	}

	public void setValueX(int ix) {
		x.setText(""+ix);
	}
	public void setValueY(int iy) {
		y.setText(""+iy);
	}
	public void setValueZ(int iz) {
		z.setText(""+iz);
	}

	public void setValueX(float ix) {
		x.setText(""+ix);
	}
	public void setValueY(float iy) {
		y.setText(""+iy);
	}
	public void setValueZ(float iz) {
		z.setText(""+iz);
	}

	public void setValue(String sx, String sy, String sz) {
		x.setText(sx);
		y.setText(sy);
		z.setText(sz);
	}
}


