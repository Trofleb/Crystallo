

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class PositionsList {
	private JFrame frame;
	private HelpPanel p;
	
	public PositionsList() {
	  frame = new JFrame("Atom positions");
	  frame.setSize(300, 300);
	  frame.setVisible(false);
	  frame.setContentPane(p=new HelpPanel());
	  frame.validate();
	}
	
	public void show() {
	  frame.setVisible(true);
	}
	public void showOnOff(Model model) {
		if (!frame.isVisible()) {
			rebuild(model);
		}
		frame.setVisible(!frame.isVisible());
	}
	
	public void rebuild(Model model) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		for (int i=0; i<model.atoms.size(); i++) {
			Atom ai = (Atom) model.atoms.get(i);
			//sb.append("<b>Atom: "+ai.label+" "+Atom.posToString2(ai.pos)+" "+ColorChoice.colorToString(ai.color.get())+"</b><br>\r\n");
			String colorString = ColorChoice.colorToString(ai.color.get());
			sb.append("<b>Atom: "+ai.label+" "+Atom.posToString2(ai.pos)+" "+colorString+"</b>"+"<br>\r\n");
			sb.append(Atom.posToString2(ai.pos)+"<br>\r\n");
			for (int j=0; j<ai.symDown.size(); j++) {
				Atom aj = (Atom) ai.symDown.get(j);
				sb.append(Atom.posToString2(aj.pos)+"<br>\r\n");
				doAtomVect(sb, aj.down);
			}
			doAtomVect(sb, ai.down);
		}
		p.textPane.setText(sb.toString());
	}
	
	public void doAtomVect(StringBuffer sb, Vector v) {
		for (int i=0; i<v.size(); i++) {
			Atom a = (Atom) v.get(i);
			sb.append(Atom.posToString2(a.pos)+"<br>\r\n");
		}
	}
	
	private class HelpPanel extends HVPanel.v {
		JEditorPane textPane;
		public HelpPanel() {
			textPane = new JTextPane() {
//				public synchronized void paint(Graphics g) {
//					Graphics2D g2d = (Graphics2D)g;
//					g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//					super.paint(g);
//				}
			};
			textPane.setEditable(false);
			textPane.setContentType("text/html");
			addComp(new JScrollPane(textPane));
		}
	}
}
