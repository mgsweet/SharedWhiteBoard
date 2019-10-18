package Menus;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import WhiteBoard.WhiteBoardView;

/**
 * Edit menu in the menu bar.
 */
public class EditMenu extends JMenu {
	public EditMenu(WhiteBoardView wbv) {
		super("Edit(E)");
		this.setMnemonic(KeyEvent.VK_E);
		JMenuItem undoItem = new JMenuItem("Undo(U)", KeyEvent.VK_U);
		undoItem.setEnabled(false);
		this.add(undoItem);
	}
}
