package App;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONObject;

import Lobby.LobbyView;
import RMI.IRemoteApp;
import RMI.IRemoteDoor;
import RMI.RemoteApp;
import RMI.RemoteDoor;
import SignIn.SignInView;
import StateCode.StateCode;
import WhiteBoard.ClientWhiteBoard;
import WhiteBoard.PaintManager;
import WhiteBoard.ServerWhiteBoard;
import WhiteBoard.SharedWhiteBoard;
import util.Execute;
import util.RealIp;

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
	private String ip;
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
	private IRemoteApp remoteApp;
	// before being accept
	private ClientWhiteBoard tempClientWhiteBoard = null;
	// Use to store the temporary remote door.
	private IRemoteDoor tempRemoteDoor;
	// Store the current save path.
	private String CurrentSavePath;

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
	 * Get the temporary remote door.
	 * 
	 * @return
	 */
	public IRemoteDoor getTempRemoteDoor() {
		return tempRemoteDoor;
	}

	/**
	 * Set the temporary remote door.
	 * 
	 * @param tempRemoteDoor
	 */
	public void setTempRemoteDoor(IRemoteDoor tempRemoteDoor) {
		this.tempRemoteDoor = tempRemoteDoor;
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
		return ip;
	}

	/**
	 * Get central server's ip.
	 * 
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
	 * 
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
		if (lobbyView == null)
			lobbyView = new LobbyView(this);

		lobbyView.setWaitDialogVisiable(false);
		lobbyView.setBeKickedDialogVisiable(false);

		signInView.getFrame().setVisible(false);

		unbindAndSetNull();

		lobbyView.getControler().refreshRoomsList();
		lobbyView.getFrame().setVisible(true);
	}

	/**
	 * Unbind um paint door, so that next time when user create a whiteboard, they can be bind.
	 */
	public void unbindAndSetNull() {
		try {
			registry.unbind("um");
			registry.unbind("paint");
			registry.unbind("door");
		} catch (NotBoundException e) {
			// do nothing
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sharedWhiteBoard != null) {
			sharedWhiteBoard.getView().getFrame().setVisible(false);
			sharedWhiteBoard = null;
		}
		tempClientWhiteBoard = null;
		tempRemoteDoor = null;
	}

	/**
	 * Switch to whiteBoard.
	 */
	public void switch2WhiteBoard() {
		lobbyView.setWaitDialogVisiable(false);
		lobbyView.setBeKickedDialogVisiable(false);

		lobbyView.getFrame().setVisible(false);
		signInView.getFrame().setVisible(false);
		sharedWhiteBoard.getView().getFrame().setVisible(true);
	}
	
	/**
	 * Switch to Signin window.
	 */
	public void switch2SignIn(Boolean isCentralServerCrush) {
		if (lobbyView != null) {
			lobbyView.getFrame().setVisible(false);
		}
		
		if (sharedWhiteBoard != null) {
			sharedWhiteBoard.getView().getFrame().setVisible(false);
		}
		
		signInView.getFrame().setVisible(true);
		
		if (isCentralServerCrush) {
			signInView.setTipsLabel("");
			JOptionPane.showMessageDialog(signInView.getFrame(), "Can not connect to Central Server. Please reconnect.");
		}
	}

	/**
	 * Create a room and register in central server.
	 * 
	 * @param roomName
	 * @param password
	 */
	public void createRoom(String roomName, String password) {
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
			sharedWhiteBoard = new ServerWhiteBoard(this);
			sharedWhiteBoard.setRoomId(resJSON.getInteger("roomId"));
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
	public void joinRoom(String hostIp, int chatPort) {
		tempClientWhiteBoard.createChatClient(hostIp, chatPort);
		sharedWhiteBoard = tempClientWhiteBoard;
		switch2WhiteBoard();
	}

	/**
	 * Create a temporary client white board. If not, when the host try to get the
	 * information of it, would fail.
	 * 
	 * @param hostId
	 * @param hostIp
	 * @param hostRegisterPort
	 */
	public void createTempClientWhiteBoard(String hostId, String hostIp, int hostRegisterPort) {
		tempClientWhiteBoard = new ClientWhiteBoard(this, hostId, hostIp, hostRegisterPort);
	}

	/**
	 * Get the Lobby view.
	 * 
	 * @return
	 */
	public LobbyView getLobbyView() {
		return lobbyView;
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
		int state = resJson.getIntValue("state");
		if (state == StateCode.SUCCESS) {
			roomList = (Map<Integer, String>) resJson.get("roomList");
		} else {
			System.out.println("Can not get rooms list!");
			switch2SignIn(true);
		}
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
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
		;
		int state = resJSON.getIntValue("state");
		if (state == StateCode.CONNECTION_FAIL) {
			System.out.println("Connection Fail: " + state);
		} else {
			if (state == StateCode.SUCCESS) {
				System.out.println("Successfully register in the central server!");
			} else if (state == StateCode.FAIL){
				System.out.println("User name exist!");
			} else {
				System.out.println("Can not connect to the server.");
			}
		}
		return state;
	}

	/**
	 * Delete all the information about the user in the third party. In the central
	 * server, since the user remove, it would auto remove the room of the user.
	 */
	public void removeUser() {
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.REMOVE_USER);
		reqJSON.put("userId", userId);
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
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

	public void removeRoom() {
		int roomId = sharedWhiteBoard.getRoomId();
		JSONObject reqJSON = new JSONObject();
		reqJSON.put("command", StateCode.REMOVE_ROOM);
		reqJSON.put("roomId", roomId);
		JSONObject resJSON = Execute.execute(reqJSON, serverIp, serverPort);
		int state = resJSON.getIntValue("state");
		if (state == StateCode.CONNECTION_FAIL) {
			System.out.println("Connection Fail: " + state);
		} else {
			if (state == StateCode.SUCCESS) {
				System.out.println("Successfully remove room from server!");
			} else {
				System.out.println("Remove room fail!");
			}
		}
	}

	public void setCurrentSavePath(String path) {
		this.CurrentSavePath = path;
	}

	public String getCurrentSavePath() {
		return CurrentSavePath;
	}

	private void init() throws UnknownHostException {
		initRMI();
		signInView = new SignInView(this);
	}

	private void initRMI() {
		try {
			System.setProperty("sun.rmi.transport.tcp.responseTimeout", "2000");
			System.setProperty("sun.rmi.transport.tcp.readTimeout", "2000");
			System.setProperty("sun.rmi.transport.connectionTimeout", "2000");
			System.setProperty("sun.rmi.transport.proxy.connectTimeout", "2000");
			System.setProperty("sun.rmi.transport.tcp.handshakeTimeout", "2000");
			
			// Get IP address of Localhost.
//			ip = InetAddress.getLocalHost();
			ip = RealIp.getHostIp();

			// Get a random port (Available one).
			ServerSocket registrySocket = new ServerSocket(0);
			registryPort = registrySocket.getLocalPort();
			registrySocket.close();

			// Start RMI registry
			LocateRegistry.createRegistry(registryPort);
			registry = LocateRegistry.getRegistry(ip, registryPort);

			// Create a remote user manager
			remoteApp = new RemoteApp(this);
			registry.bind("app", remoteApp);

			printInitialStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printInitialStates() throws UnknownHostException {
		System.out.println("IP address : " + ip);
		System.out.println("Registry Port = " + registryPort);
	}

}
