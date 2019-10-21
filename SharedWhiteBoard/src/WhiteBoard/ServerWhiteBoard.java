package WhiteBoard;

import App.App;
import ClientUser.UserManager;
import RMI.IRemoteDoor;
import RMI.IRemoteUM;
import RMI.RemoteDoor;
import RMI.RemoteUM;

public class ServerWhiteBoard extends SharedWhiteBoard {
	
	private IRemoteDoor remoteDoor;
	
	public ServerWhiteBoard(App app) {
		super(app);
		initManager();
		initDoorRMI();
		super.initUmRMI();
		super.initPaintRMI();
		initView();
	}
	
	public void initView() {
		String title = "Server-" + app.getIp() + ":" + app.getRegistryPort();
		ui = new WhiteBoardView(app, this.paintManager, userManager, title);
	}
	
	private void initDoorRMI() {
		try {
			remoteDoor = new RemoteDoor(userManager);
			app.getRegistry().bind("door", remoteDoor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void initManager() {
		userManager = new UserManager(true, app.getUserId(), app.getIp(), app.getRegistryPort(), -1);
		paintManager = new PaintManager(paintManager.SERVER_MODE, userManager);
	}
	
}
