/**
 * Bootstrap Grid Generator
 * Generates a css file extending the functionality of bootstraps's grid system
 * @author Adam Heins
 * 2014-04-02
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
	
	// Declare variables
	JButton generateButton;
	
	JLabel numberColumnsLabel;
	JTextField numberColumnsField;
	JLabel minLabel;
	JCheckBox minBox;
	
	JLabel titleLabel;

	
	TablePanel propertyTable;
	TablePanel divisionTable;
	
	JPanel northPanel;
	JPanel southPanel;
	JPanel westPanel;
	JPanel eastPanel;
	
	JTabbedPane tabPane;


	/**
	 * Constructor
	 */
	public BootstrapGridGenerator () {
		
		// Set properties of the panel
		setSize(200,200);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
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
		String rowData2 [][] = {{"col-xs", "0"},{"col-sm", "---"},{"col-md", "---"},{"col-lg","---"}};
		divisionTable = new TablePanel(rowData2, colNames2);
		tabPane.addTab("Column Types", divisionTable);
		
		add(tabPane, BorderLayout.CENTER);
		
		// Set up the south panel
		southPanel = new JPanel();
		southPanel.setBackground(Color.white);
		
		numberColumnsLabel = new JLabel("Number of columns: ");
		southPanel.add(numberColumnsLabel);
		
		numberColumnsField = new JTextField(3);
		southPanel.add (numberColumnsField);
		
		minLabel = new JLabel("Minify");
		southPanel.add (minLabel);
		
		minBox = new JCheckBox();
		southPanel.add (minBox);
		
		generateButton = new JButton("Generate");
		southPanel.add(generateButton);
		
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
		if (act.getSource() == generateButton) {
			generate(Integer.parseInt(numberColumnsField.getText())); // Use number field for this section
		}
		
	}
	
	
	/**
	 * Generate a css file that extends bootstrap to have different grid properties
	 * @param num Number of columns
	 */
	private void generate(int num) {
		try {
			
			// Open an output stream to the file
			BufferedWriter out = new BufferedWriter(new FileWriter("bettergrid.css"));
			
			// Import core bootstrap css
			out.write("@import 'bootstrap.css';\n\n\n");
			
			// Print initial properties common to all column
			String name [] = {".col-xs-",".col-sm-",".col-md-",".col-lg-"};
			printInitProperties(out, name, num);
			
			// Print properties of each class of each column type
			printColProperties(out, ".col-xs-", num);		
			out.write("\n\n@media (min-width: 768px) {\n");
			printColProperties(out, ".col-sm-", num);
			out.write("}\n\n@media (min-width: 992px) {\n");
			printColProperties(out, ".col-md-", num);
			out.write("}\n\n@media (min-width: 1200px) {\n");
			printColProperties(out, ".col-lg-", num);
			out.write("\n}");
			
			// Close output stream
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Print the intial properties that all columns share
	 * @param out Output stream to file
	 * @param name Array of names of different column types
	 * @param num Number of columns
	 * @throws IOException
	 */
	private void printInitProperties(BufferedWriter out, String [] name, int num) throws IOException {
		for (int i = 1; i < num; i++) {
			for (int j = 0; j < name.length; j++) {
				out.write(name[j] + i + ",\n");
			}
		}
		for (int j = 0; j < name.length - 1; j++) {
			out.write(name[j] + num + ",\n");
		}
		out.write(name[name.length - 1] + num + " {\n  position: relative;\n  min-height: 1px;\n  padding-right: 0px;\n  padding-left: 0px;\n}\n\n");
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
			out.write(name + i + ",\n");
		}
		out.write(name + num + " {\n  float: left;\n}\n\n");
		
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
			out.write(name + i + " {\n  " + property + ": " + (inc * i) + "%;\n}\n\n");
		}
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
