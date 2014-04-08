/**
 * TablePanel
 * Extends a panel containing a JTable inside of a JScrollPane
 * Provides buttons to modify the table
 * @author Adam Heins
 * 2014-04-08
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel implements ActionListener{
	
	// Serial Version UID
	private static final long serialVersionUID = 2735698517549158776L;
	
	// Scroll pane and nested table
	private JScrollPane scrollPane;
	private JTable table;
	
	// Border panels
	private JPanel southPanel;
	private JPanel northPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	
	// Buttons to modify table
	private JButton addButton;
	private JButton removeButton;
	private JButton upButton;
	private JButton downButton;
	
	
	/**
	 * Constructor
	 * @param rowData Two-dimensional array of data to fill the table
	 * @param colNames Array of names for the columns of the table
	 */
	public TablePanel (Object [][] rowData, Object [] colNames) {
			
		// Set a border layout for this panel
		setLayout(new BorderLayout());
		
		// Set up the north panel
		// Contains the title for the table
		northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);
		
		// Set up the table
		table = new JTable(new DefaultTableModel(rowData, colNames));
		scrollPane = new JScrollPane(table);
		Dimension d = table.getPreferredSize();
		scrollPane.setPreferredSize(new Dimension(d.width, table.getRowHeight()*4+23));
		table.setFillsViewportHeight(true);
		add(scrollPane, BorderLayout.CENTER);	
		
		// Set up the west panel
		// Sets a fixed 5px margin on the left side
		westPanel = new JPanel();
		westPanel.setSize(5, 5);
		add(westPanel, BorderLayout.WEST);
		
		// Set up the east panel
		// Sets a fixed 5px margin on the right side
		eastPanel = new JPanel();
		eastPanel.setSize(5, 5);
		add(eastPanel, BorderLayout.EAST);
		
		// Set up the south panel
		// Contains buttons to control modify table are located here
		southPanel = new JPanel();
		
		addButton = new JButton("Add Row");
		addButton.addActionListener(this);
		southPanel.add(addButton);
		
		removeButton = new JButton("Remove Row");
		removeButton.addActionListener(this);
		southPanel.add(removeButton);
		
		upButton = new JButton("Move Up");
		upButton.addActionListener(this);
		southPanel.add(upButton);
		
		downButton = new JButton("Move Down");
		downButton.addActionListener(this);
		southPanel.add(downButton);
		
		add(southPanel, BorderLayout.SOUTH);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Get the table model
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		// Respond to Add Row button
		if (e.getSource() == addButton) {
			
			// Add new row at end of table
			model.addRow(new Object[table.getColumnCount()]);
			
			// Select the newly added row
			table.setRowSelectionInterval(model.getRowCount() - 1, model.getRowCount() - 1);
		
		// Respond to Remove Row button
		} else if (e.getSource() == removeButton) {
			int [] rows = table.getSelectedRows();
	
			// Remove selected rows
			for (int i = 0; i < rows.length; i++) {
				model.removeRow(rows[0]);
			}
				
			// Select row immediately after removed row, or previous row if it was the last in the table
			if (model.getRowCount() > 0) {
				if (rows[0] == model.getRowCount())
					table.setRowSelectionInterval(rows[0] - 1, rows[0] - 1);
				else
					table.setRowSelectionInterval(rows[0], rows[0]);
			}
		
		// Respond to Move Up button
		} else if (e.getSource() == upButton) {
			
			// Get selected rows
			int [] selRows = table.getSelectedRows();
			
			// Move selected rows upward as long as rows are selected and below the top row
			if (selRows[0] > 0) {
				model.moveRow(selRows[0], selRows[selRows.length - 1], selRows[0] - 1);
				table.setRowSelectionInterval(selRows[0] - 1, selRows[selRows.length - 1] - 1);
			}
			
		// Respond to Move Down button
		} else if (e.getSource() == downButton){
			
			// Get selected rows
			int [] selRows = table.getSelectedRows();
			
			// Move selected rows downward as long as rows are selected and above the bottom row
			if (selRows[selRows.length - 1] != -1 && selRows[selRows.length - 1] < model.getRowCount() - 1) {
				model.moveRow(selRows[0], selRows[selRows.length - 1], selRows[0] + 1);
				table.setRowSelectionInterval(selRows[0] + 1, selRows[selRows.length - 1] + 1);
			}
		}	
	}
	
	
	/**
	 * Get the data contained in the table
	 * @return A 2-D array of the table data
	 */
	public Object [][] getRowData () {
		int columnCount = table.getColumnCount();
		int rowCount = table.getRowCount();
		
		// Initialize array to store table data
		Object rowData [][] = new Object[rowCount][columnCount];
		
		// Read data out of table into array
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				rowData[i][j] = table.getValueAt(i, j);
			}
		}
		
		return rowData;
	}
}
