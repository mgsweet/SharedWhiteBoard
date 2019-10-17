package WhiteBoard;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Remote.IRemotePaint;
import Remote.RemotePaint;

public class ServerWhiteBoard {
	private PaintManager paintManager;
	private int registryPort;
	private InetAddress ip;

	public static void main(String[] args) {
		ServerWhiteBoard serverWhiteBoard = new ServerWhiteBoard();
		serverWhiteBoard.run();
	}

	public ServerWhiteBoard() {
		this.paintManager = new PaintManager(PaintManager.SERVER_MODE);
		serverInit();
	}

	private void printInitialStates() throws UnknownHostException {
		System.out.println("Server Running...");
		System.out.println("Current IP address : " + ip.getHostAddress());
		System.out.println("Registry Port = " + registryPort);
	}

	private void serverInit() {
		try {
			// Get IP address of Localhost.
			ip = InetAddress.getLocalHost();

			// Get a random port (Available one).
//			ServerSocket registrySocket = new ServerSocket(0);
//			registryPort = registrySocket.getLocalPort();
//			registrySocket.close();
			
			// Debug Only
			registryPort = 4444; 

			// Start RMI registry
			LocateRegistry.createRegistry(registryPort);
			Registry serverRegistry = LocateRegistry.getRegistry(ip.getHostAddress(), registryPort);
			IRemotePaint remotePaint = new RemotePaint(paintManager);
			serverRegistry.bind("server", remotePaint);

			printInitialStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() {
		try {
			WhiteBoardView window = new WhiteBoardView(this.paintManager);
			window.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
