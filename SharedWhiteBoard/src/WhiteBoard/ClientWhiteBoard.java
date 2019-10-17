package WhiteBoard;

import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Remote.IRemotePaint;
import Remote.RemotePaint;

public class ClientWhiteBoard {
	
//	private PaintManager paintManager;
//	
//	public void ClientWhiteBoard() {
//		paintManager = new PaintManager();
//	}
//	
//	private void init() {
//		try {
//			ServerSocket serverSocket = new ServerSocket();
//			int localPort = new ServerSocket().getLocalPort();
//			System.out.println(localPort);
//			LocateRegistry.createRegistry(localPort);
//			Registry serverRegistry = LocateRegistry.getRegistry();
//			IRemotePaint remotePaint = new RemotePaint();
//			serverRegistry.bind("RemotePaint", remotePaint);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void run() {
//		try {
//			WhiteBoardView window = new WhiteBoardView();
//			window.getFrame().setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}