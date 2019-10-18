package CentralServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Central Server, act as a authentic third party to provide information about users and White Board room.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Modified: Oct 18, 2019 11:05:25 AM
 */

public class CentralServer {
	private int port;
	private ServerSocket server;
	private RoomManager rm;
	
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
			System.out.println("Lack of Parameters:\nPlease run like \"java - jar DictServer.jar <port> <dictionary-file>\"!");
		} catch (NumberFormatException e) {
			System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Default port is 4444
	 * @throws IOException
	 */
	public CentralServer() throws IOException {
		this.port = 4444;
		rm = new RoomManager();
		init();
	}
	
	/**
	 * Create with a specific port.
	 * @param port
	 * @throws IOException
	 */
	public CentralServer(int port) throws IOException {
		this.port = port;
		rm = new RoomManager();
		init();
	}
	
	
	public void run() throws IOException {
		while (true) {
			Socket client = server.accept();
			RequestHandler rh = new RequestHandler(client, rm);
			rh.start();
		}
	}
	
	/**
	 * Set the port.
	 * @param port
	 */
	public void setport(int port) {
		this.port = port;
	}
	
	private void init() throws IOException {
		server = new ServerSocket(this.port);
		printInitialStats();
	}
	
	private void printInitialStats() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Server Running...");
		System.out.println("Current IP address : " + ip.getHostAddress());
		System.out.println("Port = " + port);	
		System.out.println("Waiting for clinet connection...\n--------------");
	}
}
