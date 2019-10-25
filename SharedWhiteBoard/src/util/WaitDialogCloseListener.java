package util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import App.App;
import Lobby.LobbyControler;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 25, 2019 2:18:07 PM
 */

public class WaitDialogCloseListener extends WindowAdapter {
	private LobbyControler controler;
	
	public WaitDialogCloseListener(LobbyControler controler) {
		this.controler = controler;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		controler.cancelKnock();
	}
}

