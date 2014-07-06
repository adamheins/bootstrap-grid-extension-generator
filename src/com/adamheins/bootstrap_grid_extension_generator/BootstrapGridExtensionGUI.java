/**
 * GUI for Bootstrap Grid Extension Generator
 * Generates a css file extending the functionality of bootstrap's grid system.
 * @author Adam Heins
 * 2014-07-06
 */

package com.adamheins.bootstrap_grid_extension_generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.text.AbstractDocument;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BootstrapGridExtensionGUI extends JPanel implements ActionListener {

    // Serial version UID.
    private static final long serialVersionUID = -6864487932468206456L;

    // Initial values of all fields and tables.
    private final String DEFAULT_NUMBER_OF_COLUMNS = "20";
    private final String DEFAULT_FILE_NAME = "grid_extension.css";

    private final String PROPERTY_TABLE_COLUMN_NAMES[] = { "Property", "Value" };
    private final String PROPERTY_TABLE_DEFAULT_ROW_DATA[][] = { { "position", "relative" }, { "min-height", "1px" },
            { "padding-right", "0px" }, { "padding-left", "0px" }, { "float", "left" }, { "width", "100%" } };

    private final String DIVISION_TABLE_COLUMN_NAMES[] = { "Column Name", "Minimum Viewport Width" };
    private final String DIVISION_TABLE_DEFAULT_ROW_DATA[][] = { { "col-xs", "0px" }, { "col-sm", "768px" },
            { "col-md", "992px" }, { "col-lg", "1200px" } };

    // Tables for column properties and types.
    private TablePanel propertyTable;
    private TablePanel divisionTable;

    // Input forms.
    private JTextField numberColumnsField;
    private JTextField fileNameField;
    private JCheckBox minifyBox;

    // Button to trigger generation of file.
    private JButton generateButton;

    // Icons for file generation messages.
    private ImageIcon errorIcon;
    private ImageIcon successIcon;

    // Generates the new css file.
    private BootstrapGridExtensionFileGenerator generator;


    /**
     * Constructor.
     */
    public BootstrapGridExtensionGUI() {

        generator = new BootstrapGridExtensionFileGenerator();

        // Load message icons.
        successIcon = new ImageIcon(getClass().getResource("/icon/Success32.png"));
        errorIcon = new ImageIcon(getClass().getResource("/icon/Error32.png"));

        // Set up this panel.
        setBackground(Color.white);
        setLayout(new BorderLayout());
        setSize(500, 500);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Add components to this panel.
        add(getNorthPanel(), BorderLayout.NORTH);
        add(getTableTabbedPane(), BorderLayout.CENTER);
        add(getSouthPanel(), BorderLayout.SOUTH);
    }


    /**
     * Sets up the <code>Panel</code> on the north of the GUI.
     * 
     * @return The north panel.
     */
    private JPanel getNorthPanel() {

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.white);

        // Set up the panel that contains the title.
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.white);
        JLabel titleLabel = new JLabel("Bootstrap Grid Extension Generator");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titlePanel.add(titleLabel);

        // Set up panel that contains the line separator.
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setBackground(Color.white);
        separatorPanel.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        separatorPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Set up panel that contains the "File Name" and "Number of Columns"
        // field.
        JPanel nameAndNumberPanel = new JPanel();
        nameAndNumberPanel.setBackground(Color.white);

        fileNameField = new JTextField(10);
        fileNameField.setText(DEFAULT_FILE_NAME);

        numberColumnsField = new JTextField(3);
        ((AbstractDocument) numberColumnsField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        numberColumnsField.setText(DEFAULT_NUMBER_OF_COLUMNS);

        // Add components to the nameAndNumberPanel.
        nameAndNumberPanel.add(new JLabel("File Name:"));
        nameAndNumberPanel.add(fileNameField);
        nameAndNumberPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        nameAndNumberPanel.add(new JLabel("Number of columns:"));
        nameAndNumberPanel.add(numberColumnsField);

        // Add components to the north panel.
        northPanel.add(titlePanel, BorderLayout.NORTH);
        northPanel.add(separatorPanel, BorderLayout.CENTER);
        northPanel.add(nameAndNumberPanel, BorderLayout.SOUTH);

        return northPanel;
    }


    /**
     * Sets up the <code>JTabbedPane</code> containing the input tables.
     * 
     * @return The table pane.
     */
    private JTabbedPane getTableTabbedPane() {

        JTabbedPane tableTabbedPane = new JTabbedPane();
        tableTabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Set up TablePanel for Column Properties.
        propertyTable = new TablePanel(PROPERTY_TABLE_DEFAULT_ROW_DATA, PROPERTY_TABLE_COLUMN_NAMES);
        tableTabbedPane.addTab("Column Properties", propertyTable);

        // Set up TablePanel for Column Types.
        divisionTable = new TablePanel(DIVISION_TABLE_DEFAULT_ROW_DATA, DIVISION_TABLE_COLUMN_NAMES);
        tableTabbedPane.addTab("Column Types", divisionTable);

        return tableTabbedPane;
    }


    /**
     * Sets up the <code>Panel</code> on the south of the GUI.
     * 
     * @return The south panel.
     */
    private JPanel getSouthPanel() {

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.white);

        generateButton = new JButton("Generate");
        generateButton.addActionListener(this);

        minifyBox = new JCheckBox();

        // Add components to the south panel.
        southPanel.add(generateButton);
        southPanel.add(minifyBox);
        southPanel.add(new JLabel("Minify"));

        return southPanel;
    }


    @Override
    public void actionPerformed(ActionEvent act) {

        // Respond to Generate button being pressed.
        if (act.getSource() == generateButton) {
            try {
                generator.generate(propertyTable.getRowData(), divisionTable.getRowData(), fileNameField.getText(),
                        Integer.parseInt(numberColumnsField.getText()), minifyBox.isSelected());

                // Display success message.
                JOptionPane.showMessageDialog(this, "File generated successfully.", "Success!",
                        JOptionPane.INFORMATION_MESSAGE, successIcon);
            } catch (Exception e) {
                // Display error message.
                JOptionPane.showMessageDialog(this, "Error generating file.", "Error!", JOptionPane.ERROR_MESSAGE,
                        errorIcon);
            }
        }
    }


    /**
     * Sets up the GUI.
     */
    private static void createAndShowGUI() {

        BootstrapGridExtensionGUI gridGenPanel = new BootstrapGridExtensionGUI();

        // Declare and set up the frame.
        JFrame frame = new JFrame();
        frame.setSize(gridGenPanel.getWidth(), gridGenPanel.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        setFrameIcons(frame);

        // Add the panel to the frame.
        frame.getContentPane().add(gridGenPanel, BorderLayout.CENTER);

        frame.validate();
    }


    /**
     * Loads and sets the icons for the frame.
     * 
     * @param frame
     *            - The frame for which icons are being set.
     */
    private static void setFrameIcons(JFrame frame) {

        // Load the icons.
        ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();
        try {
            icons.add(ImageIO.read(frame.getClass().getResource("/icon/icon16.png")));
            icons.add(ImageIO.read(frame.getClass().getResource("/icon/icon32.png")));
            icons.add(ImageIO.read(frame.getClass().getResource("/icon/icon64.png")));
            icons.add(ImageIO.read(frame.getClass().getResource("/icon/icon128.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the icons for the frame.
        frame.setIconImages(icons);
    }


    /**
     * Main method.
     * 
     * @param args
     */
    public static void main(String[] args) {

        // Schedule creating and showing GUI on EDT
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
