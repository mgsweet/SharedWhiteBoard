package Client;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Lobby.LobbyView;
import SignIn.SignInView;
import StateCode.StateCode;

/**
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 2:53:21 PM
 */

public class Client {
	private SignInView signInView = null;
	private LobbyView lobbyView = null;
	// User information
	private InetAddress userIp;
	private String userId = "";
	// Central server information
	private String serverIp = "";
	private int port = -1;
	// RoomList
	public Map<Integer, String> roomList = null;

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

	public Client() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run the programme.
	 */
	public void run() {
		signInView.getFrame().setVisible(true);
		System.out.println("Client running");
	}

	/**
	 * Get the Host IP address.
	 * 
	 * @return
	 */
	public String getUserIp() {
		return userIp.getHostAddress();
	}
	
	/**
	 * Set userId
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Set server IP
	 * @param serverIp
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	/**
	 * Set port.
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * When user completes sign in, use this method to switch to Lobby.
	 */
	public void switchToLobby() {
		System.out.println("User: " + userId + " enter Lobby.");
		lobbyView = new LobbyView(this);
		signInView.getFrame().setVisible(false);
		lobbyView.getFrame().setVisible(true);
	}

	/**
	 * Use to pull remote roomlist from the central server.
	 */
	public void pullRemoteRoomList() {
		// sent request to central server to gain roomlists
		try {
			System.out.println("Request for rooms list...");
			Socket client = new Socket(serverIp, port);
			DataInputStream reader = new DataInputStream(client.getInputStream());
			DataOutputStream writer = new DataOutputStream(client.getOutputStream());
			JSONObject reqJSON = new JSONObject();
			reqJSON.put("command", StateCode.GET_ROOM_LIST);
			writer.writeUTF(reqJSON.toJSONString());
			writer.flush();
			String res = reader.readUTF();
			JSONObject resJson = parseResString(res);
			roomList = (Map<Integer, String>) resJson.get("roomList");
			System.out.println("Get rooms list!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use to register in the central server, so no one can use a same user name.
	 * @return isSuccess whether the user can register in the central server or not.
	 */
	public int register() {
		int isSuccess = -1;
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.ADD_USER);
		reqJSON.put("userId", userId);
		JSONObject resJSON = execute(reqJSON);
		int connectState = (int) resJSON.get("connectState");
		if (connectState == StateCode.CONNECTION_SUCCESS) {
			isSuccess = Integer.parseInt(resJSON.get("operationState").toString());
			if (isSuccess == StateCode.SUCCESS) {
				System.out.println("Successfully register in the central server!");
			} else {
				System.out.println("User name exist!");
			}
			return isSuccess;
		} else {
			System.out.println("Connection Fail: " + connectState);
			return connectState;
		}
	}
	
	protected JSONObject execute(JSONObject reqJSON) {
		int state = StateCode.CONNECTION_FAIL;
		JSONObject resJSON = new JSONObject();
		try {
			System.out.println("Trying to connect to server...");
			ExecuteThread eThread = new ExecuteThread(serverIp, port, reqJSON);
			eThread.start();
			eThread.join(2000);
			if (eThread.isAlive()) {
				eThread.interrupt();
				throw new TimeoutException();
			}
			reqJSON = eThread.getResJSON();
			System.out.println("Connect Success!");
		} catch (TimeoutException e) {
			reqJSON.put("connectState", StateCode.TIMEOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqJSON;
	}

	private JSONObject parseResString(String res) {
		JSONObject resJSON = null;
		try {
			JSONParser parser = new JSONParser();
			resJSON = (JSONObject) parser.parse(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resJSON;
	}
	
	private void init() throws UnknownHostException {
		userIp = InetAddress.getLocalHost();
		signInView = new SignInView(this);
	}
}
