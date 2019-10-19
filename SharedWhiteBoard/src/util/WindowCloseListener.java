package util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Client.Client;

/**
 * Use when close in lobby.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 9:15:51 PM
 */

public class WindowCloseListener extends WindowAdapter {
	
	private Client client;
	
	public WindowCloseListener(Client client) {
		this.client = client;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		super.windowClosing(e);
		client.removeUser();
	}
}
