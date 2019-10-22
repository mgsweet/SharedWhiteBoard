package WhiteBoard;

import App.App;
import ClientUser.UserManager;

public class ClientWhiteBoard extends SharedWhiteBoard {
	public ClientWhiteBoard(App app, String hostId, String hostIp, int registerPort) {
		super(app);
		initManager(hostId, hostIp, registerPort);
		initUmRMI();
		initPaintRMI();
		initView();
	}
	
	public void initView() {
		String title = "Client-" + app.getIp() + ":" + app.getRegistryPort();
		ui = new WhiteBoardView(app, this.paintManager, userManager, title);
	}
	
	private void initManager(String hostId, String hostIp, int registerPort) {
		userManager = new UserManager(false, hostId, hostIp, registerPort, -1);
		paintManager = new PaintManager(paintManager.CLIENT_MODE, userManager);
	}
}