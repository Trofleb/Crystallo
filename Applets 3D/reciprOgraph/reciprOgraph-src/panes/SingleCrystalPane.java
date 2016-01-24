package panes;


import java.awt.Color;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.dnd.DropTarget;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import printf.Format;
import printf.Parameters;

import dragNdrop.CifFileDropper;
import intensity.Intensity;
import utils.HVPanel;
import utils.TableSorter;

public class SingleCrystalPane extends HVPanel.h {
	static Text text; 
	static Table table;
	private static int hlen;
	public static boolean needRecalcTheta=false;
	public static boolean needRePrint=true;
	private MainPane mainPane;
	
	public SingleCrystalPane(MainPane mainPane) {
		this.mainPane = mainPane;
		addSubPane(text=new Text());
		//addSubPane(table=new Table());
	}
	
	public static double round(double d) {
		return Math.round(d*1000)/1000d;
	}
	
	public static void show() {
		if (needRecalcTheta) {
			Intensity.recalculateTheta2Table();
			needRecalcTheta=false;
		}
		if (needRePrint) {
			Intensity e = (Intensity) Intensity.allInstances.get(0);
			e.printList();
			text.edit.setCaretPosition(0);
			needRePrint = false;
		}
	}

	public static void clear() {
		if (text!=null) {
			//text.edit.setText("  h   k   l    d(hkl)   2-Theta        Lp     F(re)     F(Im)  Intensity\n");
			text.edit.setText("");
			hlen=text.edit.getText().length();
		}
	}
	
//	public static void addBefore(int h, int k, int l, double d, double theta2, double Fre, double Fim, double I) {
//		if (text!=null) {
//			Parameters params = new Parameters();
//			params.add(h).add(k).add(l).add(d).add(theta2).add(Fre).add(Fim).add(I);
//			String s = Format.sprintf("%3d %3d %3d %9.4f %11.3f %11.2f %11.2f  %11.2f\n", params);
//			text.edit.insert(s, hlen);
//		}
//	}
//	public static void addAfter(int h, int k, int l, double d, double theta2, double Fre, double Fim, double I) {
//		if (text!=null) {
//			Parameters params = new Parameters();
//			params.add(h).add(k).add(l).add(d).add(theta2).add(Fre).add(Fim).add(I);
//			String s = Format.sprintf("%3d %3d %3d %9.4f %11.3f %11.2f %11.2f  %11.2f\n", params);
//			text.edit.append(s);
//		}
//	}
	public static void add(int h, int k, int l, double d, double theta2, double Fre, double Fim, double I) {
		if (text!=null) {
			if (Fre<0&&Fre>-0.001) Fre=0;
			if (Fim<0&&Fim>-0.001) Fim=0;
			if (I<0&&I>-0.001) I=0;
			Parameters params = new Parameters();
			params.add(h).add(k).add(l).add(d).add(theta2).add(Fre).add(Fim).add(I);
			String s = Format.sprintf("%3d %3d %3d %9.4f %11.3f %11.2f %11.2f  %11.2f\n", params);
			text.edit.append(s);
		}
	}
	
	class RightPane extends HVPanel.v {
	}
	
	class Text extends HVPanel.v {
		JTextArea edit; 
		JLabel legend;
		JScrollPane scrollPane;
		public Text() {
			edit = new JTextArea();
			edit.setEditable(false);
			edit.setFont(new Font("Monospaced", Font.PLAIN, 12));
	    scrollPane = new JScrollPane(edit);
	    legend = new JLabel("  h   k   l    d(hkl)     2-Theta       F(re)       F(Im)         |F|\u00b2");
	    legend.setFont(new Font("Monospaced", Font.PLAIN, 12));
	    expand(false);
	    addComp(legend);
	    expand(true);
	    addComp(scrollPane);
			new DropTarget(edit, new CifFileDropper(mainPane)); 
		}
	}
	
	class Table extends HVPanel.h {
		JTable table;
		TableSorter sorter;
		DefaultTableModel tableModel;
		JScrollPane scrollPane;
		
		public Table() {
	    table = new JTable(sorter=new TableSorter(tableModel=new DefaultTableModel() {
	      public boolean isCellEditable(int row, int col) {
	        return false;
	      }
	      public Class getColumnClass(int c) {
	        return getValueAt(0, c).getClass();
	      }
	    })) {
	    };
	    scrollPane = new JScrollPane(table);
	    sorter.setTableHeader(table.getTableHeader());
	    
	    String[] header = {"h", "k", "l", "d(hkl)", "2-Theta", "Lp", "F(Re)", "F(Im)", "Intensity"};
	    int[] width = null;

	    for (int i=0; i<header.length; i++) {
				tableModel.addColumn(header[i]);
			}
			if (width!=null) {
				for (int i=0; i<header.length; i++) {
				  TableColumn column = table.getColumnModel().getColumn(i);
				  column.setPreferredWidth(width[i]);
				}
			}
	    table.setColumnSelectionAllowed(true);
	    table.setRowSelectionAllowed(true);
	    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    addComp(scrollPane);
		}
	}
}
