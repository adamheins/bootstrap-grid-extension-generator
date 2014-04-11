/**
 * Bootstrap Grid Generator
 * Generates a css file extending the functionality of bootstrap's grid system
 * @author Adam Heins
 * 2014-04-11
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class BootstrapGridGenerator extends JPanel implements ActionListener {

	// Serial version UID
	private static final long serialVersionUID = -6864487932468206456L;
	
	// Formatting strings
	private String nl;
	private String sp;
	private String tb;
	private String sc;
	
	// Tab pane and inner tables
	private JTabbedPane tabPane;
	private TablePanel propertyTable;
	private TablePanel divisionTable;
	
	// Border panels
	private JPanel northPanel;
	private JPanel southPanel;
	
	// Components on north panel
	private JLabel titleLabel;
	private JLabel numberColumnsLabel;
	private JFormattedTextField numberColumnsField;
	private JLabel fileNameLabel;
	private JTextField fileNameField;
	
	// Components on south panel
	private JButton generateButton;
	private JLabel minLabel;
	private JCheckBox minBox;


	/**
	 * Constructor
	 */
	public BootstrapGridGenerator () {
		
		// Set properties of the panel
		setSize(200,200);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setSize(500, 500);
		
		// Set up north panel
		northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.white);
		
		JPanel pn1 = new JPanel();
		JPanel pn2 = new JPanel();
		pn1.setBackground(Color.white);
		pn2.setBackground(Color.white);
		
		titleLabel = new JLabel("Bootstrap Grid Extension Generator");
		titleLabel.setFont(new Font("Calibri",Font.PLAIN,24));
		pn1.add(titleLabel);
		
		fileNameLabel = new JLabel("File Name:");
		pn2.add(fileNameLabel);
		
		fileNameField = new JTextField(10);
		fileNameField.setText("gridExtension.css");
		pn2.add(fileNameField);

		pn2.add(Box.createRigidArea(new Dimension(20,0)));
		
		numberColumnsLabel = new JLabel("Number of columns: ");
		pn2.add(numberColumnsLabel);
		
		NumberFormat numFormat = NumberFormat.getInstance();
		numFormat.setMaximumFractionDigits(0);
		numberColumnsField = new JFormattedTextField(numFormat);
		numberColumnsField.setValue(new Double(20));
		numberColumnsField.setColumns(3);
		pn2.add (numberColumnsField);	
		
		northPanel.add(pn1, BorderLayout.NORTH);
		northPanel.add(pn2, BorderLayout.SOUTH);
		
		add(northPanel, BorderLayout.NORTH);
		
		// Set up the tabbed pane containing the panels
		tabPane = new JTabbedPane();
		
		// Set up TablePanel for Column Properties
		String colNames [] = {"Property","Value"};
		String rowData [] [] = {{"position", "relative"},{"min-height", "1px"},{"padding-right", "0px"},{"padding-left","0px"}};
		propertyTable = new TablePanel(rowData, colNames);
		tabPane.addTab("Column Properties",propertyTable);
		
		// Set up TablePanel for Column Types
		String colNames2 [] = {"Column Name","Minimum Viewport Width"};
		String rowData2 [][] = {{"col-xs", "0px"},{"col-sm", "768px"},{"col-md", "992px"},{"col-lg","1200px"}};
		divisionTable = new TablePanel(rowData2, colNames2);
		tabPane.addTab("Column Types", divisionTable);
		
		add(tabPane, BorderLayout.CENTER);
		
		// Set up the south panel
		southPanel = new JPanel();
		southPanel.setBackground(Color.white);
		
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		southPanel.add(generateButton);
		
		minBox = new JCheckBox();
		southPanel.add (minBox);
		
		minLabel = new JLabel("Minify");
		southPanel.add (minLabel);
		
		add(southPanel, BorderLayout.SOUTH);
		
		// Add margins on east and west sides
		add(Box.createRigidArea(new Dimension(10,0)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension(10,0)), BorderLayout.EAST);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent act) {
		
		// Respond to Generate button being pressed
		if (act.getSource() == generateButton) {
			generate(Integer.parseInt(numberColumnsField.getText()), minBox.isSelected()); // Use number field for this section
		}		
	}
	
	
	/**
	 * Generate a css file that extends bootstrap to have different grid properties
	 * @param num Number of columns
	 * @param minify True if generated code should be minified, false otherwise
	 */
	private void generate(int num, boolean minify) {
		try {
			
			// Initialize formatting strings
			if (minify) {	
				nl = "";
				sp = "";
				sc = "";
			} else {
				nl = "\n";
				sp = " ";
				sc = ";";
			}
			
			// Open an output stream to the file
			BufferedWriter out = new BufferedWriter(new FileWriter(readFileName()));
			
			// Import core bootstrap css
			out.write("@import 'bootstrap.css';" + nl + nl + nl);
			
			// Print initial properties common to all column
			String name [] = {".col-xs-",".col-sm-",".col-md-",".col-lg-"};
			printInitProperties(out, name, num);
			
			// Print properties of each class of each column type
			Object [][] tableData = divisionTable.getRowData();
			for (int i = 0; i < tableData.length; i++) {
				
				// Assign tab to default value of none
				tb = "";
				
				// Check if block needs to be within an @media size condition
				if (isZeroWidth((String)tableData[i][1]))
					printColProperties(out, "." + tableData[i][0] + "-", num);
				else {
					if (!minify)
						tb = "  ";
					out.write(nl + "@media" + sp + "(min-width:" + sp + tableData[i][1] + ")" + sp + "{" + nl);
					printColProperties(out, "." + tableData[i][0] + "-", num);
					out.write("}" + nl);
				}
			}
			
			// Close output stream
			out.close();
			
			JOptionPane.showMessageDialog(this, "File generated successfully.", "Success!", JOptionPane.INFORMATION_MESSAGE);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error generating file.", "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * Print the initial properties that all columns share
	 * @param out Output stream to file
	 * @param name Array of names of different column types
	 * @param num Number of columns
	 * @throws IOException
	 */
	private void printInitProperties(BufferedWriter out, String [] name, int num) throws IOException {
		for (int i = 1; i < num; i++) {
			for (int j = 0; j < name.length; j++) {
				out.write(name[j] + i + "," + nl);
			}
		}
		for (int j = 0; j < name.length - 1; j++) {
			out.write(name[j] + num + "," + nl);
		}
		out.write(name[name.length - 1] + num + sp + "{" + nl);
		
		// Set properties from table
		Object [][] tableData = propertyTable.getRowData();
		for (int i = 0; i < tableData.length - 1; i++) {
			out.write(sp + sp + tableData[i][0] + ":" + sp + tableData[i][1] + ";" + nl); // All but last one should be hard-coded semi-colons
		}
		out.write(sp + sp + tableData[tableData.length - 1][0] + ":" + sp + tableData[tableData.length - 1][1] + sc + nl);
		out.write("}" + nl + nl);
	}
	
	
	/**
	 * Print properties of each of each column type
	 * @param out Output stream to file
	 * @param name Name of the column
	 * @param num Number of columns
	 * @throws IOException
	 */
	private void printColProperties(BufferedWriter out, String name, int num) throws IOException {
		for (int i = 1; i < num; i++) {
			out.write(tb + name + i + "," + nl);
		}
		out.write(tb + name + num + sp + "{" + nl + tb + sp + sp + "float:" + sp + "left" + sc + nl + tb + "}" + nl);
		
		printColItem(out, name, "width", num, 1);
		printColItem(out, name + "pull-", "right", num, 0);
		printColItem(out, name + "push-", "left", num, 0);
		printColItem(out, name + "offset-", "margin-left", num, 0);		
	}
	
	
	/**
	 * Print properties of sub-types of each column type
	 * @param out Output stream to file
	 * @param name Name of the column
	 * @param property css property that is being defined
	 * @param num Number of columns
	 * @param start Starting point of classes (either 0 or 1)
	 * @throws IOException
	 */
	private void printColItem(BufferedWriter out, String name, String property, int num, int start) throws IOException {
		
		// Increment of percentage each additional class has
		double inc = 100.0 / num;
		
		for (int i = start; i < num + 1; i++) {
			out.write(nl + tb + name + i + sp + "{" + nl + tb + sp + sp + property + ":" + sp + (inc * i) + "%" + sc + nl + tb + "}" + nl);
		}
	}
	
	
	/**
	 * Analyzes a string to determine whether or not it is indicating a viewport width of zero
	 * If the first character is 0 and is not followed by another digit or radix point, it is taken as zero 
	 * @param str String to be analyzed
	 * @return Returns true if the string is determined to mean zero, false otherwise.
	 */
	private boolean isZeroWidth (String str) {
		return (str.charAt(0) == '0' && (str.length() == 1 || (!Character.isDigit(str.charAt(1)) && str.charAt(1) != '.')));
	}
	
	
	/**
	 * Reads the name of file from the text field and appends .css extension if not already present.
	 * @return String representing the file name with extension.
	 */
	private String readFileName() {
		String name = fileNameField.getText();
		if (name.indexOf(".css") == -1)
			name = name.concat(".css");
		return name;
	}
	
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create new instance of the CssGridGenerator class
		BootstrapGridGenerator gridGen = new BootstrapGridGenerator();
    	
		// Declare and set up the frame
		JFrame frame = new JFrame();
		frame.setSize(gridGen.getWidth(),gridGen.getHeight());
		frame.setVisible (true);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane ().setLayout (new BorderLayout ());
				
		// Add the panel to the frame
		frame.getContentPane ().add (gridGen, BorderLayout.CENTER);
				
		frame.validate();
	}

}
