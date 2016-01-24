/* crystalOgraph2 - AtomsPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 9 juin 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package panes;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.vecmath.Point3d;

import utils.ColorConstants;
import utils.HVPanel;


public class AtomsPane {
	public final static int DEFAULT = 0;
	public final static int CENTER_TEXT = 1;
	public final static int RADIOCHECK = 2;
	public final static int COLOR = 3;
	
	private DefaultTableModel tableModel;
	public JTable table;
	public JScrollPane scrollPane;
	private AtomsPaneListener listener;
	
	public AtomsPane(String[] header, int[] width, final boolean[] editable, int[] type) {
		table = new JTable(tableModel=new DefaultTableModel() {
	    public boolean isCellEditable(int row, int col) {
	    	return editable==null?false:editable[col];
	    }
      public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
      }
		});
		table.setRowHeight(20);
		scrollPane = new JScrollPane(table);
		
		for (int i=0; i<header.length; i++) {
			tableModel.addColumn(header[i]);
		}
		for (int i=0; i<header.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			if (width!=null && width[i]!=-1) {
				col.setMinWidth(width[i]); 
				col.setMaxWidth(width[i]); 
				col.setPreferredWidth(width[i]); 
			}
			if (type!=null) {
				switch (type[i]) {
				case CENTER_TEXT:
					col.setCellRenderer(new centeredTextRenderer());
					break;
				case RADIOCHECK:
					col.setCellRenderer(new RadioCheckRenderer());
					col.setCellEditor(new RadioCheckEditor());
					break;
				case COLOR:
					col.setCellRenderer(new ColorRenderer());
					break;
				}
			}
		}
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new SelectionListener());
	}

	public void clear() {
		while (tableModel.getRowCount()>0) {
			tableModel.removeRow(0);
		}
	}
	
	public void addRow(Object[] data) {
		tableModel.addRow(data);
	}
	
	public void setSelectedRow(int row) {
		table.setRowSelectionInterval(row, row);
	}
	
	public void setListener(AtomsPaneListener l) {
		listener=l;
	}
	
	private class SelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			if (listener!=null) listener.selectionChanged(table.getSelectedRow());
		}
	}
	
	public static interface AtomsPaneListener {
		public void valueChanged(Object newValue, int col, int row);
		public void selectionChanged(int row);
	}

	private class centeredTextRenderer extends DefaultTableCellRenderer {
		public centeredTextRenderer() {
			setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		}				
	}

	private class RadioCheckButton extends JPanel {
		public JCheckBox checkBox;
		public JRadioButton radioButton;
		public RadioCheckButton() {
			checkBox = new JCheckBox();
			radioButton = new JRadioButton();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER); 
			layout.setVgap(0);
			setLayout(layout);
			add(checkBox);
			add(radioButton);
		}				
		public void setBackground(Color c) {
			if (checkBox!=null) checkBox.setBackground(c);
			if (radioButton!=null) radioButton.setBackground(c);
			super.setBackground(c);
		}
		public void setSelected(int b) {
			checkBox.setSelected(b==1 || b==3);
			radioButton.setSelected(b==2 || b==3);
		}
		public int isSelected() {
			return (radioButton.isSelected()?2:0)+(checkBox.isSelected()?1:0);
		}
	}
	private class RadioCheckRenderer extends RadioCheckButton implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			if (value instanceof Boolean) {
				setSelected(((Boolean)value).booleanValue()?1:0);
				table.setValueAt(new Integer(isSelected()), row, column);
			}
			else
				setSelected(((Integer)value).intValue());
			setBackground(selected?table.getSelectionBackground():table.getBackground()); 
			return this;
		}
	}
	private class RadioCheckEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		private RadioCheckButton editor;
		private JTable table;
		private int row;
		private int column;
		public RadioCheckEditor() {
			editor = new RadioCheckButton();
			editor.checkBox.addActionListener(this);
			editor.radioButton.addActionListener(this);
		}
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
			this.table = table;
			this.row = row;
			this.column = column;
			if (value instanceof Boolean) {
				editor.setSelected(((Boolean)value).booleanValue()?1:0);
				table.setValueAt(new Integer(editor.isSelected()), row, column);
			}
			else
				editor.setSelected(((Integer)value).intValue());
			editor.setBackground(table.getSelectionBackground());
			return editor;
		}
		public Object getCellEditorValue() {
			return new Integer(editor.isSelected());
		}
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==editor.checkBox) {
				boolean b = editor.radioButton.isSelected();
				editor.radioButton.setSelected(false);
				if (editor.checkBox.isSelected()) {
					if (!b&&listener!=null) listener.valueChanged(new Boolean(true), column, row);
					for (int i=0; i<table.getRowCount(); i++) {
						if (i!=row && ((Integer)table.getValueAt(i, column)).intValue()==2) {
							table.setValueAt(new Integer(1), i, column);
						}
					}
				}
				else {
					if (listener!=null) listener.valueChanged(new Boolean(false), column, row);
				}
			}
			else if (e.getSource()==editor.radioButton) {
				boolean b = editor.checkBox.isSelected();
				
				if (!editor.radioButton.isSelected()) {
					for (int i=0; i<table.getRowCount(); i++) {
								table.setValueAt(new Integer(1), i, column);
								if (i!=row&&listener!=null) listener.valueChanged(new Boolean(true), column, i);
					}
					editor.setSelected(1);
				}
				else {
					editor.checkBox.setSelected(false);
					if (editor.radioButton.isSelected()&&!b) {
						if (listener!=null) listener.valueChanged(new Boolean(true), column, row);
					}
					else editor.radioButton.setSelected(true);
					for (int i=0; i<table.getRowCount(); i++) {
						if (i!=row) {
							if (((Integer)table.getValueAt(i, column)).intValue()!=0) {
								table.setValueAt(new Integer(0), i, column);
								if (listener!=null) listener.valueChanged(new Boolean(false), column, i);
							}
						}
					}
				}
			}
		}
	}

	private class ColorRenderer extends JLabel implements TableCellRenderer {
		Border unselectedBorder = null;
		Border selectedBorder = null;
		public ColorRenderer() {
			setOpaque(true); //MUST do this for background to show up.
		}
		public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {
			Color newColor = (Color)color;
			setBackground(newColor);
			if (isSelected) {
				if (selectedBorder == null) {
					selectedBorder = BorderFactory.createMatteBorder(3,5,3,5, table.getSelectionBackground());
				}
				setBorder(selectedBorder);
			} else {
				if (unselectedBorder == null) {
					unselectedBorder = BorderFactory.createMatteBorder(3,5,3,5, table.getBackground());
				}
				setBorder(unselectedBorder);
			}
			return this;
		}
	}
}

