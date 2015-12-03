import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import dragNdrop.CifFileDropper;

public class ChargeFlip extends JApplet implements Runnable {
	private static final String title = "Charge Flipping";
	static final String titleInit = "Applet is starting up. Please wait...";
	private static final int width=1050, height=700;
	private static final String defCodeBase = "http://escher.epfl.ch/crystalOgraph/";
	
	public static boolean isApplet = true;
	public JFrame frame;
	public MainPane mainPane;
	public boolean started;

	public ChargeFlip() {
	}
	
	public void init() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void start() {
		started=true;
	}
	public void stop() {
		started=false;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainPane.stop();
			}
		});
	}
	public void destroy() {
		mainPane.destroy();
	}
	
	public static void main(String[] args) {
		isApplet = false;
		ChargeFlip mainApp = new ChargeFlip();
		mainApp.init();
		mainApp.start();
	}

	// initialisation in GUI thread
	public void run() {
		createMainFrame();
		if (isApplet) createWebPane();
		createMainPane();
		showMainPane();
		new DropTarget(frame, new CifFileDropper(mainPane)); 
	}

	private void createMainFrame() {
		frame = new JFrame(titleInit);
	  frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
	  		if (isApplet) {
		    	stop();
		    	frame.setVisible(false);
	  		}
	  		else {
	  			System.exit(0);
	  		}
	    }
	  });
	  //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
	  frame.setVisible(true);
	}
	
	private void createWebPane() {
		if ("true".equals(getParameter("mini"))) {
			getContentPane().add(new AppletMiniPane());
		}
		else {
			getContentPane().add(new JLabel("Applet launched. Refresh page to load again...", JLabel.CENTER));
		}
	}
	
	private void createMainPane() {
		try {
			mainPane = new MainPane(this);
		} catch (Error e) {
			showException(e);
			throw e;
		} catch (Exception e) {
			showException(e);
			e.printStackTrace();
			System.exit(0);
			//throw e;
		}
	}
	
	private void showMainPane() {
		frame.getContentPane().add(mainPane.jPanel);
		frame.validate();
		frame.setTitle(title);
	  frame.setVisible(true);
	  frame.toFront();
	}
	
	public URL getCodeBase() {
		URL codeBase=null;
		try {
			return super.getCodeBase();
		} catch (Exception e) {
			try {
				return new URL(defCodeBase);
			} catch (MalformedURLException e1) {
				throw new RuntimeException(e1);
			}
		}
	}
	
	public void showUp() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		  	if (!started) start();
				frame.setVisible(true);
				frame.toFront();
			}
		});
	}
		
	public void showException(Throwable error) {
		ErrorPane errorPane = new ErrorPane();
		JFrame errorFrame = new JFrame("There was a problem");
		errorFrame.getContentPane().add(errorPane);
		errorFrame.setSize(500, 400);
		errorFrame.setVisible(true);
		if (error instanceof NoClassDefFoundError && error.getMessage().indexOf("javax/media/j3d")!=-1) {
			errorPane.out.println("Java3D is not installed on your computer.");
			errorPane.out.println("Please visit http://escher.epfl.ch/java3d to learn how to install it.");
			errorPane.out.println("");
		}
		error.printStackTrace(errorPane.out);
	}
	
	class ErrorPane extends JPanel {
		public PrintStream out;
		private JTextArea textArea;
		
		public ErrorPane() {
			textArea = new JTextArea();
			textArea.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(textArea);
			setLayout(new BorderLayout());
			add(scrollPane);
			
			out = new PrintStream(new OutputStream(){
				public void write(byte[] bb) throws IOException {
					write(bb, 0, bb.length);
				}
				public void write(byte[] bb, int off, int len) throws IOException {
					textArea.setText(textArea.getText()+new String(bb, off, len));
				}
				public void write(int b) throws IOException {
					textArea.setText(textArea.getText()+(char)b);
				}
			});
		}
	}

	class AppletMiniPane extends JPanel {
		public AppletMiniPane() {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
			  	if (!started) start();
			  	if (frame!=null) {
						frame.setVisible(true);
						frame.toFront();
			  	}
				}
			});
		}
		public void paint(Graphics g) {
			new ImageIcon(getClass().getResource("/applet-mini.png")).paintIcon(this, g, 0, 0);
		}
	}
}
