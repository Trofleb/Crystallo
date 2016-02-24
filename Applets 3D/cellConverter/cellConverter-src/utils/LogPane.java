/*
 * Poki - LogPane.java
 * Author : Nicolas Schoeni
 * Creation : 25 août 2005
 * nicolas.schoeni@epfl.ch
 */

package utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPane extends HVPanel.v {
	public JTextArea text;
	public JCheckBox slock;
	HVPanel.h p;

	public LogPane(boolean scrollLocker) {
		this.expand(true);
		this.text = new JTextArea();
		this.text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.text);
		this.addComp(scrollPane);
		this.expand(false);
		this.bottom();
		HVPanel.h p1 = new HVPanel.h();
		p1.addComp(this.slock = new JCheckBox("Scroll"));
		this.slock.setSelected(scrollLocker);
		p1.expand(false);
		p1.left();
		this.p = new HVPanel.h();
		p1.right();
		p1.addSubPane(this.p);
		this.addSubPane(p1);
	}

	public LogPane() {
		this.expand(true);
		this.text = new JTextArea();
		this.text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.text);
		this.addComp(scrollPane);
		this.slock = new JCheckBox("Scroll");
		this.slock.setSelected(false);
	}

	public void println(String s) {
		this.text.append(s + "\n");
		if (this.slock.isSelected())
			this.text.setCaretPosition(this.text.getText().length());
	}

	public class LogOutputStream extends OutputStream {
		public void write(int i) throws IOException {
			LogPane.this.text.append("" + (char) i);
			if (LogPane.this.slock.isSelected())
				LogPane.this.text.setCaretPosition(LogPane.this.text.getText().length());
		}
	}
}