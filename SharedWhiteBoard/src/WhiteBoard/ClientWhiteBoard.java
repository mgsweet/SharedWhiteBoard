package WhiteBoard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import RMI.IRemotePaint;

public class ClientWhiteBoard extends SharedWhiteBoard {
	
	public ClientWhiteBoard(String serverIp, int serverPort) {
		super(PaintManager.CLIENT_MODE);
		initRMI();
		connect2Server(serverIp, serverPort);
		initView();
	}
	
	private void connect2Server(String serverIp, int serverPort) {
		try {
			Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
			IRemotePaint serverRemotePaint = (IRemotePaint) registry.lookup("paintRMI");
			paintManager.setServerRMI(serverRemotePaint);
			serverRemotePaint.addClientRMI(ip.getHostAddress(), registryPort);
		} catch (Exception e) {
			System.out.println("Can not connect to server.");
			e.printStackTrace();
		}
	}
	
	public void initView() {
		String title = "Client-" + ip.getHostAddress() + ":" + registryPort;
		ui = new WhiteBoardView(this.paintManager, title);
		paintManager.pullRemoteHistory();
	}
}