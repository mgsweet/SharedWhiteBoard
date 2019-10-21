package WhiteBoard;

import App.App;
import ClientUser.UserManager;
import RMI.IRemoteDoor;
import RMI.IRemotePaint;
import RMI.RemotePaint;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 2:43:57 PM
 */

public abstract class SharedWhiteBoard {
	protected PaintManager paintManager;
	protected UserManager userManager;
	protected WhiteBoardView ui;
	protected App app;
	private IRemotePaint remotePaint;

	// Room ID is unique in central server.
	private int roomId;
	
	public void setRoomID(int roomId) {
		this.roomId = roomId;
	}
	
	public SharedWhiteBoard(App app) {
		this.app = app;
	}
	
	public WhiteBoardView getView() {
		return ui;
	}

	public void initPaintRMI() {
		try {
			remotePaint = new RemotePaint(paintManager);
			app.getRegistry().bind("paintRMI", remotePaint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
