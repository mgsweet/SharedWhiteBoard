package App;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import Lobby.LobbyView;
import RMI.IRemoteUM;
import RMI.RemoteUM;
import SignIn.SignInView;
import StateCode.StateCode;
import WhiteBoard.ClientWhiteBoard;
import WhiteBoard.ServerWhiteBoard;
import WhiteBoard.SharedWhiteBoard;
import util.Execute;

/**
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 2:53:21 PM
 */

public class App {
	// Views
	private SignInView signInView = null;
	private LobbyView lobbyView = null;
	// User information
	private InetAddress ip;
	private String userId = "";
	// Central server information
	private String serverIp = "";
	private int serverPort = -1;
	// RoomList
	public Map<Integer, String> roomList = null;
	// SharedWhiteBoard
	private SharedWhiteBoard sharedWhiteBoard = null;
	// RMI
	private int registryPort;
	private Registry registry;
	// Privite Remote interface for user manager.
	private IRemoteUM remoteUM;

	public static void main(String[] args) {
		App app = new App();
		app.run();
	}

	public App() {
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
		System.out.println("App running");
	}

	/**
	 * Get local registry
	 * 
	 * @return
	 */
	public Registry getRegistry() {
		return registry;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	/**
	 * Get the Host IP address.
	 * 
	 * @return
	 */
	public String getIp() {
		return ip.getHostAddress();
	}
	
	/**
	 * Get central server's ip.
	 * @return
	 */
	public String getServerIp() {
		return serverIp;
	}
	
	/**
	 * Get central server's port.
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * Set userId
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Get userId
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set server IP
	 * 
	 * @param serverIp
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * Set port.
	 * 
	 * @param port
	 */
	public void setServerPort(int port) {
		this.serverPort = port;
	}

	/**
	 * When user completes sign in, use this method to switch to Lobby.
	 */
	public void switch2Lobby() {
		System.out.println("User: " + userId + " enter Lobby.");
		lobbyView = new LobbyView(this);
		signInView.getFrame().setVisible(false);
		if (sharedWhiteBoard != null) {
			sharedWhiteBoard.getView().getFrame().setVisible(false);
			sharedWhiteBoard = null;
		}
		lobbyView.getFrame().setVisible(true);
	}

	/**
	 * Switch to whiteBoard.
	 */
	public void switch2WhiteBoard() {
		lobbyView.getFrame().setVisible(false);
		signInView.getFrame().setVisible(false);
		sharedWhiteBoard.getView().getFrame().setVisible(true);
	}

	/**
	 * Create a room and register in central server.
	 * 
	 * @param roomName
	 * @param password
	 */
	public void createRoom(String roomName, String password) {
		sharedWhiteBoard = new ServerWhiteBoard(this);
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.ADD_ROOM);
		reqJSON.put("roomName", roomName);
		reqJSON.put("password", password);
		reqJSON.put("hostName", userId);
		reqJSON.put("hostIp", ip);
		reqJSON.put("hostPort", registryPort);
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
		int state = resJSON.getInteger("state");
		if (state == StateCode.SUCCESS) {
			sharedWhiteBoard.setRoomID(resJSON.getInteger("roomId"));
			switch2WhiteBoard();
		} else {
			System.out.println("Fail to create room.");
		}
	}

	/**
	 * 
	 * @param hostId
	 * @param hostIp
	 * @param hostRegisterPort
	 */
	public void joinRoom(String hostId, String hostIp, int hostRegisterPort) {
		sharedWhiteBoard = new ClientWhiteBoard(this, hostId, hostIp, hostRegisterPort);
		lobbyView.setDialogVisable(false);
		switch2WhiteBoard();
	}
	
	public void exitRoom() {
		
	}

	/**
	 * Use to pull remote room list from the central server.
	 */
	public void pullRemoteRoomList() {
		// sent request to central server to gain roomlists
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.GET_ROOM_LIST);
		System.out.println("Request for rooms list...");
		JSONObject resJson = Execute.execute(reqJSON, serverIp, serverPort);
		roomList = (Map<Integer, String>) resJson.get("roomList");
		System.out.println("Get rooms list!");
	}

	/**
	 * Use to register in the central server, so no one can use a same user name.
	 * 
	 * @return isSuccess whether the user can register in the central server or not.
	 */
	public int register() {
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.ADD_USER);
		reqJSON.put("userId", userId);
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);;
		int state = resJSON.getIntValue("state");
		if (state == StateCode.CONNECTION_FAIL) {
			System.out.println("Connection Fail: " + state);
		} else {
			if (state == StateCode.SUCCESS) {
				System.out.println("Successfully register in the central server!");
			} else {
				System.out.println("User name exist!");
			}
		}
		return state;
	}

	/**
	 * Delete all the information about the user in the third party.
	 */
	public void removeUser() {
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.REMOVE_USER);
		reqJSON.put("userId", userId);
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);;
		int state = resJSON.getIntValue("state");
		if (state == StateCode.CONNECTION_FAIL) {
			System.out.println("Connection Fail: " + state);
		} else {
			if (state == StateCode.SUCCESS) {
				System.out.println("Successfully exit from the central server!");
			} else {
				System.out.println("Exit invalidly!");
			}
		}
	}

	private void init() throws UnknownHostException {
		initRMI();
		signInView = new SignInView(this);
	}

	private void initRMI() {
		try {
			// Get IP address of Localhost.
			ip = InetAddress.getLocalHost();

			// Get a random port (Available one).
			ServerSocket registrySocket = new ServerSocket(0);
			registryPort = registrySocket.getLocalPort();
			registrySocket.close();

			// Start RMI registry
			LocateRegistry.createRegistry(registryPort);
			registry = LocateRegistry.getRegistry(ip.getHostAddress(), registryPort);

			// Create a remote user manager
			remoteUM = new RemoteUM(this);
			registry.bind("umRMI", remoteUM);
			
			printInitialStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printInitialStates() throws UnknownHostException {
		System.out.println("IP address : " + ip.getHostAddress());
		System.out.println("Registry Port = " + registryPort);
	}
}
