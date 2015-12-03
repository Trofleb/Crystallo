/* Poki - LogPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 25 août 2005
 * 
 * nicolas.schoeni@epfl.ch
 */

package utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.HVPanel;

public class LogPane extends HVPanel.v {
  public JTextArea text;
  public JCheckBox slock;
  HVPanel.h p;
  public LogPane(boolean scrollLocker) {
    expand(true);
    text = new JTextArea();
    text.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(text);
    addComp(scrollPane);
    expand(false);
    bottom();
    HVPanel.h p1 = new HVPanel.h();
    p1.addComp(slock=new JCheckBox("Scroll"));
    slock.setSelected(scrollLocker);
    p1.expand(false);
    p1.left();
    p = new HVPanel.h();
    p1.right();
    p1.addSubPane(p);
    addSubPane(p1);
  }
  public LogPane() {
    expand(true);
    text = new JTextArea();
    text.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(text);
    addComp(scrollPane);
    slock=new JCheckBox("Scroll");
    slock.setSelected(false);
  }
  public void println(String s) {
    text.append(s+"\n");
    if (slock.isSelected()) {
      text.setCaretPosition(text.getText().length());
    }
  }
  public class LogOutputStream extends OutputStream {
    public void write(int i) throws IOException {
      text.append(""+(char)i);
      if (slock.isSelected()) {
        text.setCaretPosition(text.getText().length());
      }
    }
  }
}