import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Pane {

	private HVPanel.v p;
	public JCheckBox ck1, ck2, ck3;
	private JButton next, back, restart, finish, dummy;
	public JLabel text, kLabel, kVal;
	public JSlider scrbar;
	public int k=5000;
	public int d;
	private Color saveColor=null;

	public Pane() {
		p = new HVPanel.v();

		p.addButton(ck1 = new JCheckBox("Show pattern"));
		p.addButton(ck2 = new JCheckBox("Show Original Lattice"));
		p.addButton(ck3 = new JCheckBox("Show Reciprocal Lattice"));
		ck1.setSelected(true);
		ck2.setSelected(true);
		ck3.setSelected(true);
		setCheckBoxStates(false, false, false);

		ck1.addActionListener(listener);
		ck2.addActionListener(listener);
		ck3.addActionListener(listener);

		p.putExtraSpace();

		p.addComp(text = new JLabel());
		p.putExtraSpace();


		p.addComp(kLabel=new JLabel("Set K:"));
		kLabel.setVisible(false);

		scrbar = new JSlider(JSlider.HORIZONTAL, 1500, 20000, 1500);
		scrbar.setEnabled(false);
		kLabel.setEnabled(false);
		scrbar.addChangeListener(listener2);
		scrbar.setVisible(false);
		p.addComp(scrbar);
		p.addComp(kVal=new JLabel(" "));

		p.putExtraSpace();


		JPanel p3 = new JPanel();
		p3.setLayout(new GridLayout(2, 2)); 
		p3.add(dummy = new JButton(" "));
		p3.add(finish = new JButton("Finished"));
		p3.add(back = new JButton("Back"));
		p3.add(next = new JButton("Next"));
		p.addComp(p3);

		dummy.setVisible(false);
		finish.setVisible(false);
		back.setVisible(false);
		next.setVisible(false);

		finish.addActionListener(listener);
		back.addActionListener(listener);
		next.addActionListener(listener);

		p.addButton(restart = new JButton("Restart or quit"));
		restart.addActionListener(listener);

		// Adjust text pane size
		p.toJPanel().setMinimumSize(new Dimension(350, 0));
		p.toJPanel().setMaximumSize(new Dimension(350, 800));
		p.toJPanel().setPreferredSize(new Dimension(350, 600));
	}

	public imgDisplay l;
	public ActionListener al;
	public void setListener(imgDisplay l) {
		this.l = l;
	}
	public void setListener(ActionListener al) {
		this.al = al;
	}

	private ActionListener listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==restart)
				if (l!=null) l.action(null, "New Pattern");
			if (al!=null) al.actionPerformed(e);
			else if (e.getSource()==ck1)
				if (l!=null) l.action(null, "ck1");
			if (al!=null) al.actionPerformed(e);
			else if (e.getSource()==ck2)
				if (l!=null) l.action(null, "ck2");
			if (al!=null) al.actionPerformed(e);
			else if (e.getSource()==ck3)
				if (l!=null) l.action(null, "ck3");
			if (al!=null) al.actionPerformed(e);
			else
				if (l!=null && e.getSource() instanceof JButton) l.action(null, ((JButton)e.getSource()).getText());
			if (al!=null) al.actionPerformed(e);
		}
	};
	private ChangeListener listener2 = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			k = scrbar.getValue(); 
			kVal.setText("<html>D="+d+"<p>K="+k+"<p>K/D="+((int)(k/d)));
			//System.out.println(k);
			l.handleMyEvent(null);
		}
	};

	public JPanel toJPanel() {
		return p.toJPanel();
	}

	public void setCheckBoxStates(boolean b1, boolean b2, boolean b3) {
		ck1.setEnabled(b1);
		ck2.setEnabled(b2);
		ck3.setEnabled(b3);
	}

	public void setupPanel(String s, String bt1, String bt2) {
		text.setText("<html><body>"+s+"</body></html>");

		back.setText(bt1==null?" ":bt1);
		next.setText(bt2==null?" ":bt2);
		//back.setEnabled(bt1!=null);
		//next.setEnabled(bt2!=null);
		back.setVisible(bt1!=null);
		next.setVisible(bt2!=null);

		back.setFocusable(bt1!=null);
		next.setFocusable(bt2!=null);

		if (saveColor!=null) {
			back.setBackground(saveColor);
			saveColor = null;
		}
		if (bt1!=null && bt1.equals("Finished")) {
			saveColor = back.getBackground();
			back.setBackground(Color.yellow);
		}
		//System.out.println(s+","+bt1+","+bt2);
	}

	public void clear() {
	}



	public void putKChooser() {
		kVal.setText("<html>D="+d+"<p>K="+k+"<p>K/D="+((int)(k/d)));
		scrbar.setMaximum(200*d);
		scrbar.setValue(k=50*d);
		scrbar.setMinimum(10*d);

		enableKChooser();
	}
	public void disableKChooser() {
		//scrbar.setEnabled(false);
		hideKChooser();
	}
	public void enableKChooser() {
		scrbar.setVisible(true);
		kLabel.setVisible(true);
		scrbar.setEnabled(true);
		kLabel.setEnabled(true);
	}
	public void setK(int k) {
		if (this.k==k) return;
		this.k=k;
		scrbar.setValue(k);
	}

	public void setD(int d) {
		this.d=d;
		kVal.setText("D="+d);
	}
	public void hideD() {
		kVal.setText("");
	}

	public void hideKChooser() {
		kVal.setText("");
		scrbar.setVisible(false);
		kLabel.setVisible(false);
	}
	public void showFinish(boolean b) {
		finish.setVisible(b);
		finish.setFocusable(b);
	}


}




/*
JLabel longLabel = new JLabel();
longLabel.setText("<html><body>This is a <p><b>" +
                  "<font size=\"+2\">a label on</font>" +
                  "</b><p>three lines.</body></html>");
 */

/*
master = p;
master.setBackground(Color.white);
master.setLayout(new BorderLayout());
pan = new JPanel();
panel.setupPanel("Please click on a point in the image above.",null,null);
p.add("North",pan);
pan2 = new JPanel();
pan2.setBackground(Color.white);
p.add("South",pan2);
 */
