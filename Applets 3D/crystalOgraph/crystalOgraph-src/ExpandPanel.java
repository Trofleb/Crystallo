import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.vecmath.Point3f;

public class ExpandPanel extends JPanel implements ActionListener {
	private JButton expMore, expLess;
	private TextFieldCoord expandCoord;
	private Model model;

	public ExpandPanel(Univers univers, Model model) {
		this.model = model;

		this.setBorder(new TitledBorder("Expand"));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;

		this.expandCoord = new TextFieldCoord.h(new String[] { "a", " b", " c" }, 2);
		this.expandCoord.addActionListener(this);
		this.add(this.expandCoord, c);

		this.expandCoord.x.setToolTipText("Extension of the cell contents by +x and -x.");
		this.expandCoord.y.setToolTipText("Extension of the cell contents by +y and -y.");
		this.expandCoord.z.setToolTipText("Extension of the cell contents by +z and -z.");

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		this.expMore = new MultiLineToolTip.JButton("More");
		this.expMore.addActionListener(this);
		p.add(this.expMore);
		this.expLess = new MultiLineToolTip.JButton("Less");
		this.expLess.addActionListener(this);
		p.add(this.expLess);
		this.add(p, c);

		this.expMore.setToolTipText("Expand the model by 10% in all directions.");
		this.expLess.setToolTipText("Reduce the model by 10% in all directions.");

		this.reset();
	}

	public void reset() {
		this.model.setExpand(1f, 1f, 1f);
		this.expandCoord.setValue(1f, 1f, 1f);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.expandCoord)
			try {
				Point3f g = this.expandCoord.parseFraqValue();
				if (g.x <= 0.0 || g.y <= 0.0 || g.z <= 0.0)
					throw new BadEntry();
				this.model.setExpand(g.x, g.y, g.z);
			} catch (BadEntry e) {
				Shaker.shake("Bad entry in expand coordinates");
			}
		else if (event.getSource() == this.expMore)
			try {
				Point3f g = this.expandCoord.parseFraqValue();
				if (g.x <= 0.0 || g.y <= 0.0 || g.z <= 0.0)
					throw new BadEntry();
				if (g.x > .89 && g.x < .91 && g.y > .89 && g.y < .91 && g.z > .89 && g.z < .91)
					g.set(.99f, .99f, .99f);
				else if (g.x > 0.98 && g.x < 1 && g.y > 0.98 && g.y < 1 && g.z > 0.98 && g.z < 1)
					g.set(1f, 1f, 1f);
				else
					g.add(new Point3f(.1f, .1f, .1f));
				this.expandCoord.setValue((Point3f) Atom.round(g));
				this.model.setExpand(g.x, g.y, g.z);
			} catch (BadEntry e) {
				Shaker.shake("Bad entry in expand coordinates");
			}
		else if (event.getSource() == this.expLess)
			try {
				Point3f g = this.expandCoord.parseFraqValue();
				if (g.x >= 0.1f && g.y > 0.1f && g.z > 0.1f) {
					if (g.x <= 0.0 || g.y <= 0.0 || g.z <= 0.0)
						throw new BadEntry();
					if (g.x == 1 && g.y == 1 && g.z == 1)
						g.add(new Point3f(-.01f, -.01f, -.01f));
					else if (g.x > 0.98 && g.x < 1 && g.y > 0.98 && g.y < 1 && g.z > 0.98 && g.z < 1)
						g.set(.9f, .9f, .9f);
					else
						g.add(new Point3f(-.1f, -.1f, -.1f));
					this.expandCoord.setValue((Point3f) Atom.round(g));
				}
				this.model.setExpand(g.x, g.y, g.z);
			} catch (BadEntry e) {
				Shaker.shake("Bad entry in expand coordinates");
			}
	}
}