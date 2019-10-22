package Menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import App.App;
import ClientUser.UserManager;
import Menus.FileOpenListener;
import Menus.FileSaveListener;
import WhiteBoard.PaintManager;
import WhiteBoard.WhiteBoardView;

/**
 * File menu in the menu bar.
 */
public class FileMenu extends JMenu {

	public FileMenu(App app, WhiteBoardView wbv, PaintManager paintManager, UserManager userManager) {
		super("File(F)");

		JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintManager.clearAll();
				app.setCurrentSavePath(null);
			}
		});
		if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
			newMenuItem.setEnabled(false);
		}
		this.add(newMenuItem);

		JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMenuItem.addActionListener(new FileOpenListener(wbv, app));
		if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
			openMenuItem.setEnabled(false);
		}
		this.add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(new FileSaveListener(wbv, app, "save"));
		this.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
		saveAsMenuItem.addActionListener(new FileSaveListener(wbv, app, "saveAs"));
		this.add(saveAsMenuItem);

		this.addSeparator();
		JMenuItem exitMenuItem = new JMenuItem("Exit to Lobby", KeyEvent.VK_E);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
					try {
						app.getTempRemoteDoor().leave(app.getUserId());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else {
					app.removeRoom();
					userManager.clear();
				}
				app.switch2Lobby();
			}
		});
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
