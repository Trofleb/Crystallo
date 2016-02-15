import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;

import javax.media.j3d.BranchGroup;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;

public class Help3d extends BranchGroup implements ColorConstants, ActionListener {
	public JFrame frame;
	private JButton close;
	private JTextPane txt;
	private crystalOgraph applet;

	public Help3d(crystalOgraph applet, RightPanel rightPanel) {
		this.applet = applet;
		this.frame = new JFrame("Some help...");
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.frame.setContentPane(p);

		this.txt = new JTextPane();
		this.txt.setMargin(new Insets(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(this.txt);
		scrollPane.setPreferredSize(new Dimension(500, 400));

		this.txt.setEditable(false);
		URL u = this.getClass().getResource("/help.html");
		try {
			this.txt.setPage(u);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		this.close = new JButton("Close");
		this.close.addActionListener(this);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		p.add(scrollPane, c);
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth = 1;
		c.insets = new Insets(10, 0, 0, 0);
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 2;
		c.gridy = 1;
		p.add(this.close, c);

		this.txt.addMouseMotionListener(this.mmListener);
		this.txt.addMouseListener(this.mListener);
	}

	public void show() {
		this.frame.pack();
		this.frame.setVisible(true);
	}

	MouseMotionListener mmListener = new MouseMotionAdapter() {
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
		Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);

		@Override
		public void mouseMoved(MouseEvent me) {
			JTextComponent jt = (JTextComponent) me.getComponent();
			int pos = jt.viewToModel(me.getPoint());
			if (pos >= 0) {
				StyledDocument d = (StyledDocument) jt.getDocument();
				Element el = d.getCharacterElement(pos);
				AttributeSet as = el.getAttributes();
				/*
				 * for (Enumeration e = as.getAttributeNames();
				 * e.hasMoreElements();) {
				 * Object o = e.nextElement();
				 * System.out.println(o+" "+as.getAttribute(o));
				 * }
				 */
				if (as.isDefined(javax.swing.text.html.HTML.Tag.A))
					jt.setCursor(this.handCursor);
				else
					jt.setCursor(this.defCursor);
			}
		}
	};

	MouseListener mListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent ev) {
			JTextComponent jt = (JTextComponent) ev.getComponent();
			int pos = jt.viewToModel(ev.getPoint());
			if (pos >= 0) {
				StyledDocument d = (StyledDocument) jt.getDocument();
				Element el = d.getCharacterElement(pos);
				AttributeSet as = el.getAttributes();
				if (as.isDefined(javax.swing.text.html.HTML.Tag.A)) {
					String s = as.getAttribute(javax.swing.text.html.HTML.Tag.A).toString();
					String a = Icsd.right(s, "href=");
					if (a != null)
						if (a.charAt(0) == '_') {
							a = a.substring(1, a.length()).trim();
							Component c = findComponent(Help3d.this.applet.mainPane, a);
							if (c != null)
								blink(c);
						} else
							try {
								Help3d.this.txt.setPage(a);
							} catch (IOException io) {
								System.out.println("invalid link");
							}
				}
			}
		}
	};

	public static Component findComponent(Container root, String name) {
		for (int i = 0; i < root.getComponentCount(); i++) {
			Component c = root.getComponent(i);
			if (c instanceof JPanel && ((JPanel) c).getBorder() != null
					&& (((JPanel) c).getBorder() instanceof TitledBorder)) {
				String s = ((TitledBorder) ((JPanel) c).getBorder()).getTitle();
				if (s.equals(name))
					return c;
				else {
					int p = name.indexOf(s + "/");
					if (p != -1)
						return findComponent((Container) c, name.substring(p + s.length() + 1));
				}
			}
			if (c instanceof AbstractButton && ((AbstractButton) c).getText().equals(name))
				return c;
			else if (c instanceof Container) {
				c = findComponent((Container) c, name);
				if (c != null)
					return c;
			}
		}
		return null;
	}

	public static void blink(final Component cp) {
		new Thread() {
			@Override
			public void run() {
				Color c = cp.getBackground();
				for (int i = 0; i < 3; i++) {
					cp.setBackground(Color.yellow);
					try {
						sleep(300);
					} catch (Exception e) {
					}
					cp.setBackground(c);
					try {
						sleep(200);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.close)
			this.frame.dispose();
	}
}

// Appearance app = new Appearance();
// app.setMaterial(new Material(green, black, green, white, 120.0f));

// this.addChild(Atom.createLegend("Help", new Point3d(0,0,0), new
// Point3d(0,0,0), 2, app));
