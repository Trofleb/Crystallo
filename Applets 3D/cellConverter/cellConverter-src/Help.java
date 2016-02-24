
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import utils.HVPanel;

public class Help {
	private JFrame frame;

	public Help() {
		this.frame = new JFrame("Help");
		this.frame.setSize(800, 480);
		this.frame.setVisible(false);
		this.frame.setContentPane(new HelpPanel().jPanel);
		this.frame.validate();
	}

	public void show(boolean show) {
		this.frame.setVisible(show);
		if (show)
			this.frame.toFront();
	}

	private class HelpPanel extends HVPanel.v {
		JTextPane textPane;

		public HelpPanel() {
			this.textPane = new JTextPane() {
				public synchronized void paint(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					super.paint(g);
				}
			};
			this.textPane.setEditable(false);
			this.addComp(new JScrollPane(this.textPane));
			try {
				URL helpURL = Help.class.getResource("/help.html");
				this.textPane.setPage(helpURL);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
