package panes;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Menu;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import panes.MainPane.MainTab;



import sg.SgSystem;
import sg.SgType;

import dragNdrop.CifFileDropper;

public class MainApp extends JApplet implements Runnable {
	public  static final String title = "reciprOgraph";
	private static final String titleInit = title+" is starting up. Please wait...";
	private static final int width=1100, height=700;
	private static final String defCodeBase = "http://escher.epfl.ch/crystalOgraph/";
	public static boolean isApplet = true;
	public JFrame frame;
	public MainPane mainPane;
	public boolean started;

	public MainApp() {
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
	
	// initialisation in GUI thread
	public void run() {
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
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
			SgSystem.static_init();
			SgType.staticInit();
			mainPane = new MainPane(this);
		} catch (Error e) {
			showException(e);
			throw e;
		}
	}
	
	private void showMainPane() {
		frame.getContentPane().add(mainPane.new MainTab(mainPane.jPanel).jPanel);
		frame.validate();
	  frame.setVisible(true);
	  frame.toFront();
	}
	
	public boolean getBoolParameter(String s) {
		try {
			return "true".equalsIgnoreCase(getParameter(s));
		} catch (Exception e) {
			return false;
		}
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
		
	public void setDndDropListener(DropTargetListener listener) {
		new DropTarget(frame, listener);
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
