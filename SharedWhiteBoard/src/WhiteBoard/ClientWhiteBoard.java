package WhiteBoard;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import PaintRMI.IRemotePaint;
import PaintRMI.RemotePaint;

public class ClientWhiteBoard {
	private PaintManager paintManager;
	private int clientRegistryPort;
	private InetAddress clientIp;
	
	public static void main(String[] args) {
		ClientWhiteBoard clientWhiteBoard = new ClientWhiteBoard();
		clientWhiteBoard.run();
		
	}
	
	public ClientWhiteBoard() {
		this.paintManager = new PaintManager(PaintManager.CLIENT_MODE);
		clientInit();
		connect2Server("127.0.0.1", 4444);
	}
	
	private void clientInit() {
		try {
			// Get IP address.
			clientIp = InetAddress.getLocalHost();

			// Get a random port (Available one).
			ServerSocket registrySocket = new ServerSocket(0);
			clientRegistryPort = registrySocket.getLocalPort();
			registrySocket.close();

			// Start RMI registry
			LocateRegistry.createRegistry(clientRegistryPort);
			Registry serverRegistry = LocateRegistry.getRegistry(clientIp.getHostAddress(), clientRegistryPort);
			IRemotePaint remotePaint = new RemotePaint(paintManager);
			serverRegistry.bind("client", remotePaint);
			
			// Out put current connect state.
			printInitialStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void connect2Server(String serverIp, int serverPort) {
		try {
			Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
			IRemotePaint serverRemotePaint = (IRemotePaint) registry.lookup("server");
			paintManager.setServerRMI(serverRemotePaint);
			serverRemotePaint.addClientRMI(clientIp.getHostAddress(), clientRegistryPort);
		} catch (Exception e) {
			System.out.println("Can not connect to server.");
			e.printStackTrace();
		}
	}
	
	private void printInitialStates() throws UnknownHostException {
		System.out.println("Client Running...");
		System.out.println("Current IP address : " + clientIp.getHostAddress());
		System.out.println("Registry Port = " + clientRegistryPort);
	}
	
	private void run() {
		try {
			String title = ""
					+ "Client-" + clientIp.getHostAddress() + ":" + clientRegistryPort;
			WhiteBoardView window = new WhiteBoardView(this.paintManager, title);
			// Get current paint history from server. This should be done after the view creates.
			paintManager.pullRemoteHistory();
			window.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}