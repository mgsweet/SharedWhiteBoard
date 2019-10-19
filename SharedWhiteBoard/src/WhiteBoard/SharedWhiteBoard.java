package WhiteBoard;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import PaintRMI.IRemotePaint;
import PaintRMI.RemotePaint;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 2:43:57 PM
 */

public abstract class SharedWhiteBoard {
	protected PaintManager paintManager;
	protected int registryPort;
	protected InetAddress ip;
	protected WhiteBoardView ui;

	// Room ID is unique in central server.
	private String roomId;
	
	public void setRoomID(String roomId) {
		this.roomId = roomId;
	}
	
	public SharedWhiteBoard(int mode) {
		paintManager = new PaintManager(mode);
	}

	public String getIpAddress() {
		return ip.getHostAddress();
	}
	
	public WhiteBoardView getView() {
		return ui;
	}

	public int getRegistryPort() {
		return registryPort;
	}

	public void initRMI() {
		try {
			// Get IP address of Localhost.
			ip = InetAddress.getLocalHost();

			// Get a random port (Available one).
			ServerSocket registrySocket = new ServerSocket(0);
			registryPort = registrySocket.getLocalPort();
			registrySocket.close();

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

	private void printInitialStates() throws UnknownHostException {
		System.out.println("Registry IP address : " + ip.getHostAddress());
		System.out.println("Registry Port = " + registryPort);
	}
}
