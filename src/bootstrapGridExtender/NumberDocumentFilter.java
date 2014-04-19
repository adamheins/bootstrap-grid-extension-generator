/**
 * NumberDocumentFilter
 * Extends DocumentFilter to allow only digits as input on the document.
 * @author Adam Heins
 * @author stackoverflow.com
 * 2014-04-19
 */

package bootstrapGridExtender;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocumentFilter extends DocumentFilter {
	
	/**
	 * Constructor.
	 */
	public NumberDocumentFilter () {
		super();
	}
	
	
	@Override
	public void insertString (DocumentFilter.FilterBypass fb, int off, String str, AttributeSet attr) 
			throws BadLocationException {
	    
		// Remove non-digits in inserted text.
		fb.insertString(off, str.replaceAll("\\D++", ""), attr);
	} 
	
	
	@Override
	public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) throws BadLocationException {
	    
		// Remove non-digits in replaced text.
		fb.replace(off, len, str.replaceAll("\\D++", ""), attr);
	}
}
