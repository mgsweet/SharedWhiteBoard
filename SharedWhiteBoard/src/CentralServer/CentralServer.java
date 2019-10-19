package CentralServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Central Server, act as a authentic third party to provide information about
 * users and White Board room.
 * 
 * The Central Server's default running port is DEFAULT_PORT
 * 
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Modified: Oct 18, 2019 11:05:25 AM
 */

public class CentralServer {
	private int port;
	private ServerSocket server;
	private CentralServerView ui;
	private InetAddress ip;
	
	private RoomManager rm;
	private Map<String, String> userList;
	
	public static final int DEFAULT_PORT = 4443;

	public static void main(String[] args) {
		try {
			CentralServer centralServer = null;

			if (args.length != 0) {
				if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
					System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
					System.exit(-1);
				} else {
					centralServer = new CentralServer(Integer.parseInt(args[0]));
				}
			} else {
				centralServer = new CentralServer();
			}
			centralServer.run();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					"Lack of Parameters:\nPlease run like \"java - jar DictServer.jar <port> <dictionary-file>\"!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Default port is DEFAULT_PORT
	 * 
	 * @throws IOException
	 */
	public CentralServer() throws IOException {
		this.port = DEFAULT_PORT;
		rm = new RoomManager();
		init();
	}

	/**
	 * Create with a specific port.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public CentralServer(int port) throws IOException {
		this.port = port;
		rm = new RoomManager();
		init();
	}

	/**
	 * Run the Program.
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException {
		ui.getFrame().setVisible(true);
		while (true) {
			Socket client = server.accept();
			RequestHandler rh = new RequestHandler(client, this);
			rh.start();
		}
	}

	/**
	 * Set the port.
	 * 
	 * @param port
	 */
	public void setport(int port) {
		this.port = port;
	}

	/**
	 * Print text on both command line and GUI.
	 * @param str
	 */
	public void printOnBoth(String str) {
		System.out.println(str);
		if (ui != null)
			ui.getlogArea().append(str + '\n');
	}
	
	/**
	 * Get the room manager.
	 * @return
	 */
	public RoomManager getRoomManager() {
		return rm;
	}
	
	/**
	 * Get the user list.
	 * @return
	 */
	public Map<String, String> getUserlist() {
		return userList;
	}

	private void init() throws IOException {
		// Get IP address of Localhost
		ip = InetAddress.getLocalHost();
		server = new ServerSocket(this.port);
		
		// Use to record the user add into the server.
		userList = new HashMap<String, String>();

		printInitialStats();
		this.ui = new CentralServerView(ip.getHostAddress(), String.valueOf(port));
	}

	private void printInitialStats() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Server Running...");
		System.out.println("Current IP address : " + ip.getHostAddress());
		System.out.println("Port = " + port);
		System.out.println("Waiting for clinet connection...\n--------------");
	}

}
