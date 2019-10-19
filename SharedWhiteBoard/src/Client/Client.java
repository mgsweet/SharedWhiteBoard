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

import CentralServer.CentralServer;
import StateCode.StateCode;

/**
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 2:53:21 PM
 */

public class Client {
	protected SignInView signInView = null;
	protected LobbyView lobbyView = null;
	// User information
	private InetAddress userIp;
	protected String userId = "";
	// Central server information
	protected String serverIp = "";
	protected int port = -1;
	protected Map<Integer, String> roomsList = null;

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
		signInView.frame.setVisible(true);
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

	private void init() throws UnknownHostException {
		userIp = InetAddress.getLocalHost();
		signInView = new SignInView(this);
	}

	protected void switchToLobby() {
		System.out.println("User: " + userId + " enter Lobby.");
		lobbyView = new LobbyView(this);
		signInView.frame.setVisible(false);
		lobbyView.frame.setVisible(true);
	}

	/**
	 * Use to pull remote roomlist from the central server.
	 */
	protected void pullRemoteRoomList() {
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
			roomsList = (Map<Integer, String>) resJson.get("roomsList");
			System.out.println("Get rooms list!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Use to register in the central server, so no one can use a same user name.
	 * @return isSuccess whether the user can register in the central server or not.
	 */
	protected int register() {
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
}
