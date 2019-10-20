package WhiteBoard;

import App.App;
import ClientUser.UserManager;
import RMI.IRemoteDoor;
import RMI.IRemotePaint;
import RMI.RemoteDoor;
import RMI.RemotePaint;

public class ServerWhiteBoard extends SharedWhiteBoard {
	
	private IRemoteDoor remoteDoor;
	
	public ServerWhiteBoard(App app) {
		super(app, PaintManager.SERVER_MODE);
		userManager = new UserManager(true, app.getUserId(), app.getIp(), app.getRegistryPort(), -1);
		initDoorRMI();
		initPaintRMI();
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
	
}
