/* ReciprOgraph - LayerPane.java
 * 
 * Author   : Nicolas Schoeni
 * Creation : 8 nov. 2005
 * 
 * nicolas.schoeni@epfl.ch
 */
package panes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import panes.LogPane.LogOutputStream;
import utils.HVPanel;
import utils.MultiLineToolTip;


public class LayerPane {
	private DefaultTableModel tableModel;
	public JTable table;
	public JScrollPane scrollPane;
	private LayerPaneListener listener;
	private JFrame frame;
	
	public LayerPane() {
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		ToolTipManager.sharedInstance().setDismissDelay(20000);

		String[] header = {"File", "Formula", "Sg", "Lattice", "Color", "Show"};
		int[] width = {200, 200, 100, 200, 80, 60};
		
		table = new JTable(tableModel=new DefaultTableModel() {
	    public boolean isCellEditable(int row, int col) {
	    	return col==4||col==5;
	    }
      public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
      }
		}) {
			public JToolTip createToolTip() {
				return new MultiLineToolTip();
			}
	    public String getToolTipText(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        colIndex = convertColumnIndexToModel(colIndex);

        if (colIndex == 4) {
        	return null;
        }
        else if (colIndex == 5) {
        	return "The square doesn't modify others item's state.\nThe circle makes this selection exclusive.\nTo do a 'select all' unselect a selected circle.";
        }
        if ((colIndex != -1) && (rowIndex != -1)) { 
            TableCellRenderer renderer = getCellRenderer(rowIndex, colIndex); 
            Component component = prepareRenderer(renderer, rowIndex, colIndex); 
 
            if (component instanceof JComponent) { 
                Rectangle cellRect = getCellRect(rowIndex, colIndex, false); 
                if(cellRect.width>=component.getPreferredSize().width) 
                    return null; 
                else
                	return table.getValueAt(rowIndex, colIndex).toString();
            } 
        } 
        return super.getToolTipText(e);
	    }
		};
		table.setRowHeight(20);
		scrollPane = new JScrollPane(table);
		
		for (int i=0; i<header.length; i++) {
			tableModel.addColumn(header[i]);
		}
		
		for (int i=0; i<header.length; i++) {
		  TableColumn column = table.getColumnModel().getColumn(i);
		  column.setPreferredWidth(width[i]);
		}
		
		for (int i=0; i<header.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			if (i==4) {
				col.setCellRenderer(new ColorRenderer());
				col.setCellEditor(new ColorEditor());
			}
			else if (i==5) {
				col.setCellRenderer(new RadioCheckRenderer());
				col.setCellEditor(new RadioCheckEditor());
			}
			else {
				col.setCellRenderer(new centeredTextRenderer());
			}
		}

		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new SelectionListener());

    table.addMouseListener(new MouseAdapter(){
      public void mouseClicked(MouseEvent e){
        if (e.getClickCount() == 2){
          int row = table.getSelectedRow();
          int col = table.getSelectedColumn();
          if (col<4) {
    				if (listener!=null) listener.valueChanged("Show CIF", -1, table.getSelectedRow());
          }
        }
      }
    });
		
		HVPanel.v p = new HVPanel.v();
		p.expand(true);
		p.addComp(scrollPane);

		HVPanel.h p2 = new HVPanel.h() {
			public void actionPerformed(ActionEvent e) {
				if (listener!=null) listener.valueChanged(e.getActionCommand(), -1, table.getSelectedRow());
			}
		};
		p2.expand(false);
		p2.addButton(new JButton("Add file"));
		p2.expand(true);
		p2.putExtraSpace();
		p2.expand(false);
		p2.addButton(new JButton("Move up"));
		p2.addButton(new JButton("Move down"));
		p2.putExtraSpace(5);
		p2.addButton(new JButton("Show CIF"));
		p2.putExtraSpace(5);
		p2.addButton(new JButton("Remove file"));
		p.expand(false);
		p.putExtraSpace(10);
		p.bottom();
		p.addSubPane(p2);
		createFrame(p.jPanel);
	}

	public void show() {
		frame.setVisible(true);
		frame.toFront();
	}
	
	private void createFrame(Container mainPane) {
		frame = new JFrame("Layers");
	  frame.setSize(800, 200);
		frame.setContentPane(mainPane);
	  frame.validate();
	}
	
	
	public void addRow(Object[] data) {
		tableModel.addRow(data);
	}
	public void removeRow(int i) {
		tableModel.removeRow(i);
	}
	public void moveUpRow(int i) {
		if (i==0) return;
		tableModel.moveRow(i, i, i-1);
		table.setRowSelectionInterval(i-1, i-1);
	}
	public void moveDownRow(int i) {
		if (i==table.getRowCount()-1) return;
		tableModel.moveRow(i, i, i+1);
		table.setRowSelectionInterval(i+1, i+1);
	}
	
	public void setSelectedRow(int row) {
		table.setRowSelectionInterval(row, row);
	}
	
	public void setListener(LayerPaneListener l) {
		listener=l;
	}
	
	private class SelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			if (listener!=null) listener.selectionChanged(table.getSelectedRow());
		}
	}
	
	public static interface LayerPaneListener {
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

	public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
		Color currentColor;
		JButton button;
		JColorChooser colorChooser;
		JDialog dialog;
		private JTable table;
		private int row;
		private int column;
		protected static final String EDIT = "edit";
		
		public ColorEditor() {
			button = new JButton();
			button.setActionCommand(EDIT);
			button.addActionListener(this);
			button.setBorderPainted(false);
			
//			Set up the dialog that the button brings up.
			colorChooser = new JColorChooser();
			//colorChooser.setPreviewPanel(new MyPreviewPanel(colorChooser));
			colorChooser.setPreviewPanel(new JPanel());
			dialog = JColorChooser.createDialog(button,
					"Pick a Color",
					true,  //modal
					colorChooser,
					this,  //OK button handler
					null); //no CANCEL button handler
		}
		
		public void actionPerformed(ActionEvent e) {
			if (EDIT.equals(e.getActionCommand())) {
//				The user has clicked the cell, so
//				bring up the dialog.
				button.setBackground(currentColor);
				colorChooser.setColor(currentColor);
				dialog.setVisible(true);
				
				fireEditingStopped(); //Make the renderer reappear.
				
			} else { //User pressed dialog's "OK" button.
				currentColor = colorChooser.getColor();
				if (listener!=null) listener.valueChanged(currentColor, column, row);
			}
		}
		
//		Implement the one CellEditor method that AbstractCellEditor doesn't.
		public Object getCellEditorValue() {
			return currentColor;
		}

		
		
//		Implement the one method defined by TableCellEditor.
		public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
			this.table = table;
			this.row = row;
			this.column = column;
			this.currentColor = (Color)value;
			return button;
		}
	}
  
  // This preview panel simply displays the currently selected color.
  public class MyPreviewPanel extends JComponent {
  	// The currently selected color
  	Color curColor;
  	
  	public MyPreviewPanel(JColorChooser chooser) {
  		// Initialize the currently selected color
  		curColor = chooser.getColor();
  		//setBorder(new TitledBorder("Preview"));
  		
  		// Add listener on model to detect changes to selected color
  		ColorSelectionModel model = chooser.getSelectionModel();
  		model.addChangeListener(new ChangeListener() {
  			public void stateChanged(ChangeEvent evt) {
  				ColorSelectionModel model = (ColorSelectionModel)evt.getSource();
  				
  				// Get the new color value
  				curColor = model.getSelectedColor();
  			}
  		}) ;
  		
  		// Set a preferred size
  		//setMinimumSize(new Dimension(100, 100));
  		setPreferredSize(new Dimension(100, 100));
  		
  		//System.out.println("www");
  	}
  	
  	// Paint current color
  	public void paint(Graphics g) {
  		//super.paint(g);
  		//g.setColor(curColor);
  		g.setColor(Color.blue);
  		g.fillRect(0, 0, getWidth()-1, getHeight()-1);
  		//System.out.println(getWidth()-1);
  	}
  }
}
