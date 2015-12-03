

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class Help {
	private JFrame frame;
	private JApplet applet;
	
	public Help(JApplet applet) {
		this.applet = applet;
	  frame = new JFrame("Help");
	  frame.setSize(800, 480);
	  frame.setVisible(false);
	  frame.setContentPane(new HelpPanel().jPanel);
	  frame.validate();
	}
	
	public void show(boolean show) {
	  frame.setVisible(show);
	  if (show) frame.toFront();
	}
	
	private class HelpPanel extends HVPanel.v implements HyperlinkListener {
		JTextPane textPane;
		public HelpPanel() {
			textPane = new JTextPane() {
				public synchronized void paint(Graphics g) {
					Graphics2D g2d = (Graphics2D)g;
					g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					super.paint(g);
				}
			};
			textPane.setEditable(false);
			textPane.addHyperlinkListener(this);
			
			addComp(new JScrollPane(textPane));
			try {
				URL helpURL = Help.class.getResource("/help.html");
				textPane.setPage(helpURL);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void hyperlinkUpdate(HyperlinkEvent event) {
			if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				if (applet!=null) {
					applet.getAppletContext().showDocument(event.getURL(), "_blank");
					//applet.getAppletContext().showDocument(event.getURL(), "e-Crystallography");
				}
				else {
					System.out.println(event.getURL());
				}
			}
		}

	}
}
