package util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import App.App;

/**
 * Use when close in lobby.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 9:15:51 PM
 */

public class WindowCloseListener extends WindowAdapter {
	
	private App app;
	
	public WindowCloseListener(App app) {
		this.app = app;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		app.removeUser();
	}
}
