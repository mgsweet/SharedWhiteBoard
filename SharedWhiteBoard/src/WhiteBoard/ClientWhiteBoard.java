package WhiteBoard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import App.App;
import ClientUser.UserManager;
import RMI.IRemotePaint;

public class ClientWhiteBoard extends SharedWhiteBoard {
	
	
	
	public ClientWhiteBoard(App app, String hostId, String hostIp, int registerPort) {
		super(app);
		initManager(hostId, hostIp, registerPort);
		initPaintRMI();
		initView();
	}
	
	public void initView() {
		String title = "Client-" + app.getIp() + ":" + app.getRegistryPort();
		ui = new WhiteBoardView(app, this.paintManager, userManager, title);
		paintManager.pullRemoteHistory();
	}
	
	private void initManager(String hostId, String hostIp, int registerPort) {
		userManager = new UserManager(false, hostId, hostIp, registerPort, -1);
		paintManager = new PaintManager(paintManager.SERVER_MODE, userManager);
	}
}