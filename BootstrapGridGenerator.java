/**
 * Bootstrap Grid Generator
 * Generates a css file extending the functionality of bootstraps's grid system
 * @author Adam Heins
 * 2014-04-01
 */


import java.awt.BorderLayout;
import java.awt.Color;
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


	/**
	 * Constructor
	 */
	public BootstrapGridGenerator () {
		
		// Set properties of the panel
		setSize(200,200);
		setBackground(Color.white);
		
		numberColumnsLabel = new JLabel("Number of columns: ");
		add(numberColumnsLabel);
		
		// Add the Number of Rows field
		numberColumnsField = new JTextField(10);
		add (numberColumnsField);
		
		minLabel = new JLabel("Minify");
		add (minLabel);
		
		minBox = new JCheckBox();
		add (minBox);
		
		// Add the Generate button
		generateButton = new JButton("Generate");
		add(generateButton);
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
		
		// Increment in percentage each additional class has
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
