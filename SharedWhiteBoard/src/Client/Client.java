/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.awt.EventQueue;

public class Client {
	public static void main(String[] args) {
		Client client = new Client();
		client.runAPP();
	}
	
	protected SignInView signInView = null;
	protected LobbyView lobbyView = null;
	protected String userId = "";
	protected String address = "";
	protected int port = -1;
	
	public Client() {
		init();
	}
	
	private void init() {
		signInView = new SignInView(this);
		lobbyView = new LobbyView();
	}
	
	public void runAPP() {
		signInView.frame.setVisible(true);
	}
}
