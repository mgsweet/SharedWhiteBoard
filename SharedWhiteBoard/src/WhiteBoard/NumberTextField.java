package WhiteBoard;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 * 
 * @author Aaron-Qiu
 *
 */
class NumberTextField extends PlainDocument {
	public NumberTextField() {
		super();
	}

	public void insertString(int offset, String str, AttributeSet attr) throws javax.swing.text.BadLocationException {
		if (str == null) {
			return;
		}
		
		char[] s = str.toCharArray();
		
		// Only allow number input
		int length = 0;
		for (int i = 0; i < s.length; i++) {
			if ((s[i] >= '0') && (s[i] <= '9') && (getLength() < 2)) {
				s[length++] = s[i];
			}
			// Insert text
			super.insertString(offset, new String(s, 0, length), attr);
		}
	}
}
