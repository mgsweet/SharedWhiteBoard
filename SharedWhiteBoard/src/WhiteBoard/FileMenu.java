package WhiteBoard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import FileOperationListener.FileOpenListener;
import FileOperationListener.FileSaveListener;

/**
 * File menu in the menu bar.
 */
public class FileMenu extends JMenu {

	public FileMenu(WhiteBoardView wbv) {
		super("File(F)");

		JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wbv.getPaintBoardPanel().clearShapes();
			}
		});
		this.add(newMenuItem);

		JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMenuItem.addActionListener(new FileOpenListener(wbv));
		this.add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(new FileSaveListener(wbv, "save"));
		this.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(new FileSaveListener(wbv, "saveAs"));
		this.add(saveAsMenuItem);

		this.addSeparator();
		JMenuItem exitMenuItem = new JMenuItem("Exit to Lobby", KeyEvent.VK_E);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		// TODO
		exitMenuItem.setEnabled(false);
		this.add(exitMenuItem);
		

		JMenuItem closeMenuItem = new JMenuItem("Close");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(closeMenuItem);
	}
}
