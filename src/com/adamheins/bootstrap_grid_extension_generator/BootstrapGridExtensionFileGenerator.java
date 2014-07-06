/**
 * Generates a css file to extend the functionality of bootstrap's grid system.
 * @author Adam Heins
 * 2014-07-06
 */

package com.adamheins.bootstrap_grid_extension_generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BootstrapGridExtensionFileGenerator {

    
    // Default tab of four spaces.
    private final String DEFAULT_TAB = "    ";
    
    // Formatting strings.
    private String newLine, space, semiColon, tab;


    /**
     * Default constructor.
     */
    public BootstrapGridExtensionFileGenerator() {
    }


    /**
     * Generate a css file that extends bootstrap to have different grid
     * properties.
     */
    public void generate(Object[][] propertyData, Object[][] divisionData, String fileName, int numColumns,
            boolean minify) throws IOException {

        // Initialize formatting strings.
        // Dependent on whether the output file should be minified or not.
        if (minify) {
            newLine = "";
            space = "";
            semiColon = "";
            tab = "";
        } else {
            newLine = "\n";
            space = " ";
            semiColon = ";";
            tab = DEFAULT_TAB;
        }

        // Open an output stream to the file.
        BufferedWriter out = new BufferedWriter(new FileWriter(formatFileName(fileName)));

        // Print import of core bootstrap css.
        // Assumes you are using bootsrap.min if you generate a minified file.
        if (minify)
            out.write("@import 'bootstrap.min.css';");
        else
            out.write("@import 'bootstrap.css';\n\n\n");

        // Print initial properties common to all column.
        printInitProperties(out, divisionData, propertyData, numColumns);

        // Print properties of each class of each column type.
        for (int i = 0; i < divisionData.length; i++) {

            // Check if block needs to be within an @media size condition.
            if (isZeroWidth((String) divisionData[i][1]))
                printColumnProperties(out, "." + divisionData[i][0] + "-", numColumns, false);
            else {

                // Print media query.
                out.write(newLine + "@media" + space + "(min-width:" + space + divisionData[i][1] + ")" + space + "{"
                        + newLine);

                printColumnProperties(out, "." + divisionData[i][0] + "-", numColumns, true);

                // Closing bracket of media query.
                out.write("}" + newLine);
            }
        }

        // Close output stream.
        out.close();
    }


    /**
     * Print the initial properties that all columns share.
     * 
     * @param out
     *            - Output stream to file.
     * @param divisionData
     *            - Array of names of different column types.
     * @param num
     *            - Number of columns.
     * @throws IOException
     */
    private void printInitProperties(BufferedWriter out, Object[][] divisionData, Object[][] propertyData, int num)
            throws IOException {

        // Print names of all columns
        for (int i = 1; i < num; i++) {
            for (int j = 0; j < divisionData.length; j++)
                out.write("." + divisionData[j][0] + "-" + i + "," + newLine);
        }
        for (int j = 0; j < divisionData.length - 1; j++)
            out.write("." + divisionData[j][0] + "-" + num + "," + newLine);
        out.write("." + divisionData[divisionData.length - 1][0] + "-" + num + space + "{" + newLine);

        // Print initial column properties.
        for (int i = 0; i < propertyData.length - 1; i++)
            out.write(tab + propertyData[i][0] + ":" + space + propertyData[i][1] + ";" + newLine);
        out.write(tab + propertyData[propertyData.length - 1][0] + ":" + space
                + propertyData[propertyData.length - 1][1] + semiColon + newLine);

        out.write("}" + newLine + newLine);
    }


    /**
     * Print properties of each of each column type.
     * 
     * @param out
     *            - Output stream to file.
     * @param name
     *            - Name of the column.
     * @param num
     *            - Number of columns.
     * @throws IOException
     */
    private void printColumnProperties(BufferedWriter out, String name, int num, boolean indented) throws IOException {

        // Print out the different classes associated with each column.
        printColumnItem(out, name, "width", num, 1, indented);
        printColumnItem(out, name + "pull-", "right", num, 0, indented);
        printColumnItem(out, name + "push-", "left", num, 0, indented);
        printColumnItem(out, name + "offset-", "margin-left", num, 0, indented);
    }


    /**
     * Print properties of sub-types of each column type.
     * 
     * @param out
     *            - Output stream to file.
     * @param name
     *            - Name of the column.
     * @param property
     *            - Css property that is being defined.
     * @param num
     *            - Number of columns.
     * @param start
     *            - Starting point of classes (either 0 or 1).
     * @throws IOException
     */
    private void printColumnItem(BufferedWriter out, String name, String property, int num, int start, boolean indented)
            throws IOException {

        // Increment of percentage each additional class has.
        double inc = 100.0 / num;

        // Print property of the column class.
        for (int i = start; i < num + 1; i++) {
            out.write(newLine);
            printLine(out, indented, name + i + space + "{" + newLine);
            printLine(out, indented, tab + property + ":" + space + (inc * i) + "%" + semiColon + newLine);
            printLine(out, indented, "}" + newLine);
        }
    }


    /**
     * Print a line to output with an optional indent.
     * @param out - <code>BufferedWriter</code> output stream.
     * @param indented - True if line should be indented, false otherwise.
     * @param printStr - String to be printed.
     * @throws IOException
     */
    private void printLine(BufferedWriter out, boolean indented, String printStr) throws IOException {
        if (indented)
            out.write(tab);
        out.write(printStr);
    }


    /**
     * Analyzes a string to determine whether or not it is indicating a
     * view-port width of zero. If the string is empty, it is taken as zero. If
     * the first character is 0 and is not followed by another digit or radix
     * point, it is taken as zero.
     * 
     * @param str
     *            - String to be analyzed.
     * @return Returns true if the string is determined to mean zero, false
     *         otherwise.
     */
    private boolean isZeroWidth(String str) {
        return (str.equals("") || str.charAt(0) == '0'
                && (str.length() == 1 || (!Character.isDigit(str.charAt(1)) && str.charAt(1) != '.')));
    }


    /**
     * Reads the name of file from the text field and appends .css extension if
     * not already present.
     * 
     * @return String representing the file name with extension.
     */
    private String formatFileName(String fileName) {
        if (fileName.indexOf(".css") == -1)
            fileName = fileName.concat(".css");
        return fileName;
    }
}
