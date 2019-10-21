package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import javax.swing.JOptionPane;

import App.App;
import ClientUser.User;
import ClientUser.UserManager;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 3:23:39 PM
 */

public class RemoteApp extends UnicastRemoteObject implements IRemoteApp{
	private App app;
	
	public RemoteApp(App app) throws RemoteException {
		this.app = app;
	}

	@Override
	public void askIn(String hostId, String hostIp, int hostRegisterPort) throws RemoteException {
		app.joinRoom(hostId, hostIp, hostRegisterPort);
	}

	@Override
	public void askOut() throws RemoteException {
		app.switch2Lobby();	
		app.getLobbyView().setWaitDialogVisiable(false);
		app.getLobbyView().createBeKickedDialog();
	}
}
