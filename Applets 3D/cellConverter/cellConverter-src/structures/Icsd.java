package structures;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import sg.SgType;
import utils.MultiLineToolTip;
import utils.TableSorter;

public class Icsd implements ActionListener {
	private ActionListener actionListener;
	JFrame frame;
	JButton searchButton;
	JButton importButton;
	JButton cancelButton;
	JTextField edit1, edit2;
	JTable table;
	DefaultTableModel tableModel;
	JLabel status;
	String[] data;
	String[][] list;
	LogPane logPane;
	boolean finished = false;
	String lastError;
	String username;
	private TableSorter sorter;
	Thread thread;
	URL codeBase;

	public Icsd(URL codeBase) {
		this.codeBase = codeBase;
		this.frame = new JFrame("Search ICSD database");

		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		JPanel pp = new JPanel();
		pp.setLayout(new GridBagLayout());
		GridBagConstraints cc = new GridBagConstraints();
		cc.fill = GridBagConstraints.HORIZONTAL;

		cc.gridx = 0;
		cc.gridy = 0;
		cc.weightx = 1.0;
		cc.insets = new Insets(0, 5, 0, 0);
		pp.add(new JLabel("Elements"), cc);
		if (this.edit1 == null) {
			this.edit1 = new JTextField();
			this.edit1.addActionListener(this);
		}
		this.edit1.setColumns(5);
		cc.gridx = 1;
		cc.gridy = 0;
		pp.add(this.edit1, cc);

		cc.insets = new Insets(0, 10, 0, 0);
		cc.gridx = 2;
		cc.gridy = 0;
		pp.add(new JLabel("Element count"), cc);
		if (this.edit2 == null) {
			this.edit2 = new JTextField();
			this.edit2.addActionListener(this);
		}
		this.edit2.setColumns(5);
		cc.insets = new Insets(0, 5, 0, 0);
		cc.gridx = 3;
		cc.gridy = 0;
		pp.add(this.edit2, cc);

		this.edit1.setMinimumSize(new Dimension(30, 20));
		this.edit2.setMinimumSize(new Dimension(30, 20));

		this.searchButton = new JButton("Search");
		this.searchButton.addActionListener(this);
		cc.insets = new Insets(0, 20, 0, 5);
		cc.anchor = GridBagConstraints.LINE_END;
		// cc.fill = GridBagConstraints.BOTH;
		cc.weightx = 1.0;
		cc.gridwidth = GridBagConstraints.REMAINDER;
		cc.gridx = 4;
		cc.gridy = 0;
		pp.add(this.searchButton, cc);

		c.gridx = 0;
		c.gridy = 0;
		p.add(pp, c);

		this.tableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.tableModel.addColumn("Year");
		this.tableModel.addColumn("Authors");
		this.tableModel.addColumn("Formula");
		this.tableModel.addColumn("SG");
		this.tableModel.addColumn("Name");

		this.sorter = new TableSorter(this.tableModel);
		// table = new JTable(tableModel);

		this.table = new JTable(this.sorter) {
			public JToolTip createToolTip() {
				return new MultiLineToolTip();
			}

			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int rowIndex = this.rowAtPoint(p);
				int colIndex = this.columnAtPoint(p);
				int realColumnIndex = this.convertColumnIndexToModel(colIndex);

				if (realColumnIndex == 1 || realColumnIndex == 2 || realColumnIndex == 4) {
					String s = (String) this.getModel().getValueAt(rowIndex, realColumnIndex);
					if (s.trim().length() == 0)
						return null;
					else
						return s;
				} else if (realColumnIndex == 3) {
					String sgName = (String) this.getModel().getValueAt(rowIndex, 3);
					sgName = Icsd.this.revertMinusInSgSymbol(sgName);
					SgType sg = SgType.getSg(sgName);
					if (sg == null)
						return "Unknown space group !";
					return "Space group N° " + sg.no + "\n'" + sg.fullName + "' " + sg.variantDesc + "\n"
							+ sg.getSystem().name;
				} else
					return super.getToolTipText(e);
			}
		};

		this.sorter.setTableHeader(this.table.getTableHeader());

		this.table.getColumnModel().getColumn(0).setPreferredWidth(40);
		this.table.getColumnModel().getColumn(1).setPreferredWidth(120);
		this.table.getColumnModel().getColumn(2).setPreferredWidth(50);
		this.table.getColumnModel().getColumn(3).setPreferredWidth(70);
		this.table.getColumnModel().getColumn(4).setPreferredWidth(100);
		JScrollPane scrollPane = new JScrollPane(this.table);
		this.table.setPreferredScrollableViewportSize(new Dimension(400, 300));
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					Icsd.this.actionPerformed(new ActionEvent(Icsd.this.importButton, 0, null));
			}
		});
		// pp.add(scrollPane);

		scrollPane.setMinimumSize(new Dimension(200, 100));

		cc.fill = GridBagConstraints.BOTH;

		c.insets = new Insets(5, 0, 0, 0);
		c.weightx = 1;
		c.weighty = .8;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 5;
		p.add(scrollPane, c);
		c.gridwidth = 1;

		this.logPane = new LogPane();

		c.insets = new Insets(5, 0, 0, 0);
		c.gridwidth = 5;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 0.2;
		p.add(this.logPane.scrollPane, c);
		c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1;
		c.weighty = 0;

		if (this.list != null)
			for (int i = 0; i < this.list[0].length; i++)
				this.tableModel.addRow(new Object[] { this.list[0][i], this.list[1][i] });

		JPanel ppp = new JPanel();

		this.importButton = new JButton("Import selected");
		this.cancelButton = new JButton("Cancel");
		this.importButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
		ppp.add(this.importButton);
		ppp.add(this.cancelButton);

		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 5;
		p.add(ppp, c);

		this.frame.getContentPane().add(p);

		this.frame.pack();
		this.frame.validate();
		this.prePopulate();
	}

	private String revertMinusInSgSymbol(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++)
			if (i != 0 && s.charAt(i) == '-')
				sb.insert(sb.length() - 1, '-');
			else
				sb.append(s.charAt(i));
		return sb.toString();
	}

	private static final String[] files = { "1913_Hull, W.H.;Bragg, W.L._C_F d -3 m S_Carbon.cif",
			"1913_Bragg, W.H.;Bragg, W.L._Na Cl_F m -3 m_Sodium chloride.cif",
			"1923_Aminoff, G._Ni As_P 63.m m c_Nickel arsenide.cif",
			"1922_Gerlach, W._Zn S_F -4 3 m_Zinc sulfide - cubic.cif",
			"1961_Ballentyne, D.W.G.;Roy, B._Zn S_P 63 m c_Zinc sulfide - 2H.cif",
			"2002_Rozenberg, G.Kh.;Dubrovinskii, L.S.;Pasternak, M.P._Fe2 O3_P b c n_Iron(III) oxide - HP2.cif",
			"1982_King, H.E.jr.;Prewitt, C.T._Fe S_P n m a_Iron sulfide.cif",
			"1925_Barth, T._Ca (Ti O3)_C m m m_Calcium titanate.cif",
			"2003_Arakcheeva, A.V.;Chapuis, G.;Birkedal, H.;Pattison, P._Ta8_P 4.m b m_Tantalum - beta, LT guest.cif",
			"1928_Harris, P.M.;Mack, E.;Blake, F.C._I2_C m c a_Diiodine.cif", };

	private void prePopulate() {
		this.list = new String[files.length][6];
		for (int i = 0; i < files.length; i++) {
			this.getClass().getResource("/" + files[i]);
			String[] ss = files[i].split("_");
			this.list[files.length - i - 1][0] = files[i];
			this.list[files.length - i - 1][1] = ss[0];
			this.list[files.length - i - 1][2] = ss[1];
			this.list[files.length - i - 1][3] = ss[2];
			this.list[files.length - i - 1][4] = ss[3];
			this.list[files.length - i - 1][5] = ss[4];
			this.tableModel.addRow(new Object[] { ss[0], ss[1], ss[2], ss[3].replaceAll("\\.", "/"),
					ss[4].substring(0, ss[4].length() - 4), files[i] });
		}
	}

	public void show(boolean show) {
		this.frame.setVisible(show);
		if (show)
			this.frame.toFront();
	}

	class LogPane {
		public PrintStream out;
		private JTextArea textArea;
		private JScrollPane scrollPane;
		private StringBuffer sb;

		public LogPane() {
			this.sb = new StringBuffer();
			this.textArea = new JTextArea();
			this.textArea.setEditable(false);
			this.textArea.setOpaque(false);
			this.textArea.setRows(3);

			this.scrollPane = new JScrollPane(this.textArea);
			this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

			this.out = new PrintStream(new OutputStream() {
				public void write(final byte[] bb) throws IOException {
					this.write(bb, 0, bb.length);
				}

				public void write(final byte[] bb, final int off, final int len) throws IOException {
					LogPane.this.sb.append(new String(bb, off, len));
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							LogPane.this.textArea.setText(LogPane.this.sb.toString());
							LogPane.this.textArea.setCaretPosition(LogPane.this.textArea.getText().length() - 1);
						}
					});
				}

				public void write(final int b) throws IOException {
					LogPane.this.sb.append((char) b);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							LogPane.this.textArea.setText(LogPane.this.sb.toString());
							LogPane.this.textArea.setCaretPosition(LogPane.this.textArea.getText().length() - 1);
						}
					});
				}
			});
		}

		public void clear() {
			this.sb.setLength(0);
			this.textArea.setText(this.sb.toString());
		}
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}

	private void setButtonEnableState(final JButton b, final boolean state) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				b.setEnabled(state);
			}
		});
	}

	private void clearTable() {
		for (int i = 0, n = this.tableModel.getRowCount(); i < n; i++)
			this.tableModel.removeRow(0);
	}

	int nbElementsFound;

	private void search(final boolean limited) {
		this.setButtonEnableState(this.searchButton, false);
		this.list = null;
		this.logPane.out.print("Connecting to ICSD database..");
		this.finished = false;
		this.thread = new Thread() {
			public void run() {
				Icsd.this.populateElementList();
				Icsd.this.finished = true;
			}
		};
		this.thread.start();

		while (!this.finished) {
			this.logPane.out.print(".");
			try {
				Thread.sleep(200);
			} catch (Exception ee) {
			}
		}
		this.logPane.out.println();

		String s;
		if (this.elementNumber == 0)
			s = "No element found.";
		else if (this.elementNumber == -1)
			s = this.lastError;
		else {
			s = "Found " + this.elementNumber + " element" + (this.elementNumber > 1 ? "s" : "") + " matching.";
			if (limited && this.elementNumber > 20)
				s += " Limited to first 20 elements.";
		}

		this.logPane.out.println(s);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (Icsd.this.list != null)
					for (int k = 20, i = Icsd.this.list.length - 1; i >= 0 && (!limited || k >= 0); i--, k--)
						Icsd.this.tableModel
								.addRow(new Object[] { Icsd.this.list[i][1], Icsd.this.list[i][2], Icsd.this.list[i][3],
										Icsd.this.list[i][4], Icsd.this.list[i][5], Icsd.this.list[i][0] });
			}
		});
		this.setButtonEnableState(this.searchButton, true);
	}

	private void importElement(final String s) {
		this.setButtonEnableState(this.importButton, false);
		this.logPane.out.print("Retrieving selected element");
		this.finished = false;
		this.thread = new Thread() {
			public void run() {
				if (s.indexOf(".cif") != -1)
					Icsd.this.data = Icsd.this.retrieveCif(s);
				else {
					int e = Integer.parseInt(s);
					Icsd.this.data = Icsd.this.retrieveCif(e);
				}
				Icsd.this.finished = true;
			}
		};
		this.thread.start();

		while (!this.finished) {
			this.logPane.out.print(".");
			try {
				Thread.sleep(200);
			} catch (Exception ee) {
			}
		}
		this.logPane.out.println();

		if (this.data.length == 1)
			this.logPane.out.println(this.data[0]);
		else
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Icsd.this.frame.setVisible(false);
					Icsd.this.actionListener.actionPerformed(new ActionEvent(Icsd.this, 0, ""));
				}
			});
		this.setButtonEnableState(this.importButton, true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.searchButton || e.getSource() == this.edit1 || e.getSource() == this.edit2) {
			final boolean limited = (e.getModifiers() & ActionEvent.CTRL_MASK) == 0;
			this.clearTable();
			new Thread() {
				public void run() {
					Icsd.this.search(limited);
				}
			}.start();
		} else if (e.getSource() == this.importButton) {
			if (this.table.getSelectedRowCount() != 0) {
				final String s = this.list[this.list.length - this.sorter.modelIndex(this.table.getSelectedRow())
						- 1][0];
				new Thread() {
					public void run() {
						Icsd.this.importElement(s);
					}
				}.start();
			}
		} else if (e.getSource() == this.cancelButton) {
			this.finished = true;
			this.importButton.setEnabled(true);
			this.searchButton.setEnabled(true);
			this.frame.setVisible(false);
		}
	}

	public String[] getData() {
		return this.data;
	}

	public static void main(String[] args) throws IOException {
		final Icsd icsd = new Icsd(new URL("http://escher.epfl.ch/crystalOgraph/"));
		icsd.setActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] ss = icsd.getData();

				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileFilter(new CifFileFilter());
				if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					if (f.getName().toLowerCase().indexOf(".cif") == -1)
						f = new File(f.getAbsolutePath() + ".cif");
					try {
						PrintWriter out = new PrintWriter(new FileOutputStream(f));
						for (int i = 0; i < ss.length; i++)
							out.println(ss[i]);
						out.close();
					} catch (FileNotFoundException e1) {
						throw new RuntimeException(e1);
					}
				}
			}
		});
		icsd.show(true);
	}

	private URL urlPage(int page) throws MalformedURLException {
		try {
			String elts = this.edit1.getText();
			elts = elts.replace(',', ' ');
			return new URL(this.codeBase,
					"icsd.cgi?action=Search&page=" + page + "&nb_rows=100&elements=" + URLEncoder.encode(elts, "UTF-8")
							+ "&elementc=" + URLEncoder.encode(this.edit2.getText(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public URL urlElement(int e) throws MalformedURLException {
		return new URL(this.codeBase, "icsd.cgi?format=cif&action=Export&id%5B%5D=" + e);
	}

	BufferedReader stream;
	String currentLine;
	int elementCount, elementNumber;

	private String[] retrieveCif(int e) {
		Vector v = new Vector(100, 50);
		try {
			URL u = this.urlElement(e);
			this.stream = new BufferedReader(new InputStreamReader(u.openStream()));
			while (true) {
				this.currentLine = this.stream.readLine();
				if (this.currentLine == null)
					break;
				v.add(this.currentLine);
			}
			this.stream.close();
			return (String[]) v.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new String[] { ex.toString() };
		}
	}

	private String[] retrieveCif(String fileName) {
		Vector v = new Vector(100, 50);
		try {
			URL u = this.getClass().getResource("/" + fileName);
			this.stream = new BufferedReader(new InputStreamReader(u.openStream()));
			while (true) {
				this.currentLine = this.stream.readLine();
				if (this.currentLine == null)
					break;
				v.add(this.currentLine);
			}
			this.stream.close();
			return (String[]) v.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new String[] { ex.toString() };
		}
	}

	private void parsePage(int page) throws IOException, MalformedURLException, ParseException {
		URL u = this.urlPage(page);
		this.stream = new BufferedReader(new InputStreamReader(u.openStream()));
		while (true) {
			this.currentLine = this.stream.readLine();
			if (this.currentLine == null)
				break;
			if (page == 1)
				this.parseElementNumber();
			this.parseElement();
		}
		this.stream.close();
	}

	private void parseElementNumber() throws ParseException {
		String a = between(this.currentLine, "<i><b>", "</b> Results</i>");
		if (a != null) {
			this.elementNumber = Integer.parseInt(a);
			this.list = new String[this.elementNumber][6];
			return;
		}
		a = between(this.currentLine, "Your search matched ",
				" documents.<br>Maximum allowed : 1000<br>Please try to be more specific.");
		if (a != null)
			throw new ParseException("Too much elements found (" + a + "). Please be more specific.");
		if (this.currentLine.indexOf("Your search did not match any documents.") != -1)
			throw new ParseException("No element found.");
	}

	private void parseElement() throws IOException, ParseException {
		String a = between(this.currentLine, "<td align=\"center\"><input type=\"checkbox\" name=\"id[]\" value=\"",
				"\" id=\"");
		if (a != null) {
			this.list[this.elementCount][0] = a;
			if ((this.currentLine = this.stream.readLine()) == null)
				throw new ParseException("ERROR !! Unexpected end of file");
			this.list[this.elementCount][1] = between(this.currentLine, "<td>", "</td>");
			if ((this.currentLine = this.stream.readLine()) == null)
				throw new ParseException("ERROR !! Unexpected end of file");
			this.list[this.elementCount][2] = between(this.currentLine, "<td>", "</td>").replaceAll("\\<.*?\\>", "");
			;
			if ((this.currentLine = this.stream.readLine()) == null)
				throw new ParseException("ERROR !! Unexpected end of file");
			this.list[this.elementCount][5] = between(this.currentLine, "<td>", "</td>").replaceAll("\\<.*?\\>", "");
			;
			if ((this.currentLine = this.stream.readLine()) == null)
				throw new ParseException("ERROR !! Unexpected end of file");
			this.list[this.elementCount][3] = between(this.currentLine, "<td>", "</td>").replaceAll("\\<.*?\\>", "");
			;
			if ((this.currentLine = this.stream.readLine()) == null)
				throw new ParseException("ERROR !! Unexpected end of file");
			this.list[this.elementCount][4] = between(this.currentLine, "<td nowrap>", "</td>");
			this.elementCount++;
		}
	}

	class ParseException extends Exception {
		public ParseException(String s) {
			super(s);
		}
	}

	private void populateElementList() {
		this.elementCount = 0;
		try {
			this.parsePage(1);
			if (this.elementNumber == 0) {
				dumpStream(this.urlPage(1));
				this.lastError = null;
			} else {
				for (int i = 2; i <= Math.ceil(this.elementNumber / 100f); i++)
					this.parsePage(i);
				this.lastError = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			this.lastError = e.getMessage();
			this.elementNumber = -1;
		} catch (IOException e) {
			e.printStackTrace();
			this.lastError = e.toString() + "\n" + "Can't connect to the server !";
			this.elementNumber = -1;
		} catch (Exception e) {
			e.printStackTrace();
			this.lastError = e.toString();
			this.elementNumber = -1;
		}
	}

	private static void dumpStream(URL url) {
		try {
			BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
			while (true) {
				String s = stream.readLine();
				if (s == null)
					break;
				System.out.println(s);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String between(String s, String a, String b) {
		int p1 = s.indexOf(a);
		if (p1 != -1) {
			int p2 = s.indexOf(b, p1);
			if (p2 != -1)
				return s.substring(p1 + a.length(), p2);
		}
		return null;
	}
}
