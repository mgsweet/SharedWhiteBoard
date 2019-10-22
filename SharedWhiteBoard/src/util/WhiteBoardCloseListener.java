package util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import App.App;
import ClientUser.UserManager;
import WhiteBoard.PaintManager;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 22, 2019 10:19:51 PM
 */

public class WhiteBoardCloseListener extends WindowAdapter {
	
	private App app;
	private PaintManager paintManager;
	private UserManager userManager;
	
	public WhiteBoardCloseListener(App app, PaintManager paintManager, UserManager userManager) {
		this.app = app;
		this.paintManager = paintManager;
		this.userManager = userManager;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		
		if (paintManager.getMode() == PaintManager.CLIENT_MODE) {
			try {
				app.getTempRemoteDoor().leave(app.getUserId());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		} else {
			userManager.clear();
		}
		
		app.removeUser();
	}
}
