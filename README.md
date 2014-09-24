# Bootstrap Grid Extension Generator

### Description
An application that generates css files that extend the core grid functionality of bootstrap. The vanilla grid system is restrictive with only 12 columns and four somewhat arbitrary view-port sizes. This application changes that by allowing the user to easily generate as many columns as they like, across user-defined viewport widths.

#### But what about nesting?
Bootstrap allows you to nest columns, so you can have as small a column as you want. Why, then, is this tool useful?
Not only is continuous nesting verbose and messy, there are times when nesting just doesn't cut it. What if one element needs to have a width less than one twelfth of the container, but not be nested? This tool makes it easy.

#### Why would I want to add a new viewport width cut-off?
For those who want finer control over the look of their application, four categories of viewport sizes simply may not be enough. For example, what looks good with a viewport width of 350 pixels may not look good at a width of 600 pixels. Now you can create a new column type to split up that range, allowing you to fine-tune the look of your application easily at both sizes.

### Instructions
Enter the name you'd like for your new css file in the File Name field. Use the Column Properties table to adjust properties of all columns. Use the Column Types table to define new types of columns for different viewport widths. Enter the number of columns you would like to generate. Select the Minify box if you want the generated file to be compact. Press the Generate button to generate your new file!

### Example
[An example of a bootstrap grid extension in action.](http://adamheins.github.io/bootstrap-grid-extension-generator/)

### Contact
adamwheins@gmail.com
