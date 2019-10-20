package WhiteBoard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import App.App;
import ClientUser.UserManager;
import RMI.IRemotePaint;

public class ClientWhiteBoard extends SharedWhiteBoard {
	
	public ClientWhiteBoard(App app, String hostId, String hostIp, int registerPort) {
		super(app, PaintManager.CLIENT_MODE);
		userManager = new UserManager(false, hostId, hostIp, registerPort, -1);
		initPaintRMI();
		connect2Server(hostIp, registerPort);
		initView();
	}
	
	private void connect2Server(String serverIp, int serverPort) {
		try {
			Registry registry = LocateRegistry.getRegistry(serverIp, serverPort);
			IRemotePaint serverRemotePaint = (IRemotePaint) registry.lookup("paintRMI");
			paintManager.setServerRMI(serverRemotePaint);
			serverRemotePaint.addClient(app.getIp(), app.getRegistryPort());
		} catch (Exception e) {
			System.out.println("Can not connect to server.");
			e.printStackTrace();
		}
	}
	
	public void initView() {
		String title = "Client-" + app.getIp() + ":" + app.getRegistryPort();
		ui = new WhiteBoardView(app, this.paintManager, userManager, title);
		paintManager.pullRemoteHistory();
	}
}