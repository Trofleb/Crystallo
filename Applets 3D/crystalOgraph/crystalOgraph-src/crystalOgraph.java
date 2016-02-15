import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class crystalOgraph extends JApplet implements Runnable {
	static final String title = "crystalOgraph";
	static final String titleInit = title + " is starting up. Please wait...";
	static final int width = 1000, height = 820;
	private static final String defCodeBase = "http://escher.epfl.ch/crystalOgraph/";

	public static boolean isApplet = true;
	public Frame frame;
	public MainPane mainPane;
	public boolean started;

	public crystalOgraph() {
	}

	@Override
	public void init() {
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void start() {
		this.started = true;
	}

	@Override
	public void stop() {
		this.started = false;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				crystalOgraph.this.mainPane.stop();
			}
		});
	}

	@Override
	public void destroy() {
		this.mainPane.destroy();
	}

	public static void main(String[] args) {
		isApplet = false;
		crystalOgraph mainApp = new crystalOgraph();
		mainApp.init();
		mainApp.start();
	}

	// initialisation in GUI thread
	@Override
	public void run() {
		this.createMainFrame();
		if (isApplet)
			this.createWebPane();
		this.createMainPane();
		this.showMainPane();
		new DropTarget(this.frame, new CifFileDropper(this.mainPane));
	}

	private void createMainFrame() {
		this.frame = new Frame(titleInit);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (isApplet) {
					crystalOgraph.this.stop();
					crystalOgraph.this.frame.setVisible(false);
				} else
					System.exit(0);
			}
		});
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(width, height);
		this.frame.setVisible(true);
	}

	private void createWebPane() {
		if ("true".equals(this.getParameter("mini")))
			this.getContentPane().add(new AppletMiniPane());
		else
			this.getContentPane()
					.add(new JLabel("Applet launched. Refresh page to load again...", SwingConstants.CENTER));
	}

	private void createMainPane() {
		try {
			this.mainPane = new MainPane(this);
		} catch (Error e) {
			this.showException(e);
			throw e;
		}
	}

	private void showMainPane() {
		// frame.getContentPane().add(mainPane);
		this.frame.add(this.mainPane);
		this.frame.validate();
		this.frame.setTitle(title);
		this.frame.setVisible(true);
		this.frame.toFront();
	}

	@Override
	public URL getCodeBase() {
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
			@Override
			public void run() {
				if (!crystalOgraph.this.started)
					crystalOgraph.this.start();
				crystalOgraph.this.frame.setVisible(true);
				crystalOgraph.this.frame.toFront();
			}
		});
	}

	public void setDndDropListener(DropTargetListener listener) {
		new DropTarget(this.frame, listener);
	}

	public void showException(Throwable error) {
		ErrorPane errorPane = new ErrorPane();
		JFrame errorFrame = new JFrame("There was a problem");
		errorFrame.getContentPane().add(errorPane);
		errorFrame.setSize(500, 400);
		errorFrame.setVisible(true);
		if (error instanceof NoClassDefFoundError && error.getMessage().indexOf("javax/media/j3d") != -1) {
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
			this.textArea = new JTextArea();
			this.textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(this.textArea);
			this.setLayout(new BorderLayout());
			this.add(scrollPane);

			this.out = new PrintStream(new OutputStream() {
				@Override
				public void write(byte[] bb) throws IOException {
					this.write(bb, 0, bb.length);
				}

				@Override
				public void write(byte[] bb, int off, int len) throws IOException {
					ErrorPane.this.textArea.setText(ErrorPane.this.textArea.getText() + new String(bb, off, len));
				}

				@Override
				public void write(int b) throws IOException {
					ErrorPane.this.textArea.setText(ErrorPane.this.textArea.getText() + (char) b);
				}
			});
		}
	}

	class AppletMiniPane extends JPanel {
		public AppletMiniPane() {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!crystalOgraph.this.started)
						crystalOgraph.this.start();
					if (crystalOgraph.this.frame != null) {
						crystalOgraph.this.frame.setVisible(true);
						crystalOgraph.this.frame.toFront();
					}
				}
			});
		}

		@Override
		public void paint(Graphics g) {
			new ImageIcon(this.getClass().getResource("/applet-mini.png")).paintIcon(this, g, 0, 0);
		}
	}
}
