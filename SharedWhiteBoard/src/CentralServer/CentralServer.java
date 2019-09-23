/**
 * @author Aaron-Qiu, mgsweet@126.com, student_id:1101584
 */
package CentralServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class CentralServer {
	private int port = 4444;
	private ServerSocket server;
	private RoomManager rm;
	
	public static void main() {
		
	}
	
	public CentralServer() {
		rm = new RoomManager();
	}
	
	public void run() throws IOException {
		try {
			while (true) {
				Socket client = server.accept();
				RequestHandler rh = new RequestHandler(client, rm);
				rh.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setport(int port) {
		this.port = port;
	}
	
	private void init() throws IOException {
		try {
			server = new ServerSocket(this.port);
			printInitialStats();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printInitialStats() throws UnknownHostException {
		InetAddress ip = InetAddress.getLocalHost();
		System.out.println("Server Running...");
		System.out.println("Current IP address : " + ip.getHostAddress());
		System.out.println("Port = " + port);	
		System.out.println("Waiting for clinet connection...\n--------------");
	}
}
