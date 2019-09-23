/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package Client;

import java.awt.EventQueue;
import java.util.Map;

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
	protected Map<Integer, String> roomsList = null;
	
	public Client() {
		init();
	}
	
	private void init() {
		signInView = new SignInView(this);
	}
	
	protected void switchToLobby() {
		System.out.println("User: " + userId + " enter Lobby.");
		lobbyView = new LobbyView(this);
		signInView.frame.setVisible(false);
		lobbyView.frame.setVisible(true);
	}
	
	public void runAPP() {
		signInView.frame.setVisible(true);
		System.out.println("Client running");
	}
}
