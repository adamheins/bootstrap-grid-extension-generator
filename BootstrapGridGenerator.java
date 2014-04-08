/**
 * Bootstrap Grid Generator
 * Generates a css file extending the functionality of bootstraps's grid system
 * @author Adam Heins
 * 2014-04-08
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class BootstrapGridGenerator extends JPanel implements ActionListener {

	// Serial version UID
	private static final long serialVersionUID = 1L;
	
	// Formatting strings
	private String nl;
	private String sp;
	private String tb;
	
	// Tab pane and inner tables
	private JTabbedPane tabPane;
	private TablePanel propertyTable;
	private TablePanel divisionTable;
	
	// Border panels
	private JPanel northPanel;
	private JPanel southPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	
	// Label for title
	private JLabel titleLabel;
	
	// Components on south panel
	private JButton generateButton;
	private JLabel numberColumnsLabel;
	private JTextField numberColumnsField;
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
		northPanel = new JPanel();
		
		titleLabel = new JLabel("Bootstrap Grid Extension Generator");
		titleLabel.setFont(new Font("Calibri",Font.PLAIN,24));
		northPanel.add(titleLabel);
		northPanel.setBackground(Color.white);
		
		add(northPanel, BorderLayout.NORTH);
		
		// Set up the tabbed pane containing the panels
		tabPane = new JTabbedPane();
		
		String colNames [] = {"Property","Value"};
		String rowData [] [] = {{"position", "relative"},{"min-height", "1px"},{"padding-right", "0px"},{"padding-left","0px"}};
		propertyTable = new TablePanel(rowData, colNames);
		tabPane.addTab("Column Properties",propertyTable);
		
		String colNames2 [] = {"Column Name","Minimum Viewport Width"};
		String rowData2 [][] = {{"col-xs", "0px"},{"col-sm", "768px"},{"col-md", "992px"},{"col-lg","1200px"}};
		divisionTable = new TablePanel(rowData2, colNames2);
		tabPane.addTab("Column Types", divisionTable);
		
		add(tabPane, BorderLayout.CENTER);
		
		// Set up the south panel
		southPanel = new JPanel(new BorderLayout());
		southPanel.setSize(50,50);
		
		JPanel ps1 = new JPanel();
		ps1.setBackground(Color.white);
		JPanel ps2 = new JPanel();
		ps2.setBackground(Color.white);
		
		numberColumnsLabel = new JLabel("Number of columns: ");
		ps1.add(numberColumnsLabel);
		
		numberColumnsField = new JTextField(3);
		numberColumnsField.setText("20");
		ps1.add (numberColumnsField);
		
		minBox = new JCheckBox();
		ps1.add (minBox);
		
		minLabel = new JLabel("Minify");
		ps1.add (minLabel);
		
		generateButton = new JButton("Generate");
		generateButton.addActionListener(this);
		ps2.add(generateButton);
		
		southPanel.add(ps1, BorderLayout.NORTH);
		southPanel.add(ps2, BorderLayout.SOUTH);
		
		add(southPanel, BorderLayout.SOUTH);
		
		// Set up the west panel
		// Adds 5px border to west side
		westPanel = new JPanel();
		westPanel.setSize(5, 5);
		westPanel.setBackground(Color.white);
		add(westPanel, BorderLayout.WEST);
		
		// Set up the east panel
		// Adds 5px border to east side
		eastPanel = new JPanel();
		eastPanel.setSize(5, 5);
		eastPanel.setBackground(Color.white);
		add(eastPanel, BorderLayout.EAST);
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
			} else {
				nl = "\n";
				sp = " ";
			}
			
			// Open an output stream to the file
			BufferedWriter out = new BufferedWriter(new FileWriter("bettergrid.css"));
			
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
			
		} catch (IOException e) {
			e.printStackTrace();
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
		Object [][] tableData = propertyTable.getRowData(); // Consider using a list here
		for (int i = 0; i < tableData.length; i++) {
			out.write(sp + sp + tableData[i][0] + ":" + sp + tableData[i][1] + ";" + nl);
		}
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
		out.write(tb + name + num + sp + "{" + nl + tb + sp + sp + "float:" + sp + "left;" + nl + tb + "}" + nl);
		
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
			out.write(nl + tb + name + i + sp + "{" + nl + tb + sp + sp + property + ":" + sp + (inc * i) + "%;" + nl + tb + "}" + nl);
		}
	}
	
	
	/**
	 * Analyzes a string to determine whether or not it is indicating a viewport width of zero
	 * If the first character is 0 and is not followed by another digit or radix point, it is taken as zero 
	 * @param str String to be analyzed
	 * @return Returns true if string is determined to mean zero, false otherwise
	 */
	private boolean isZeroWidth (String str) {
		return (str.charAt(0) == '0' && (str.length() == 1 || (!Character.isDigit(str.charAt(1)) && str.charAt(1) != '.')));
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
