package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import App.App;
import ClientUser.UserManager;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 3:23:39 PM
 */

public class RemoteUM  extends UnicastRemoteObject implements IRemoteUM{
	private UserManager um;
	private App app;
	
	public RemoteUM(App app) throws RemoteException {
		this.app = app;
	}

	@Override
	public void askIn(String hostId, String hostIp, int hostRegisterPort) throws RemoteException {
		app.joinRoom(hostId, hostIp, hostRegisterPort);
	}

	@Override
	public void askOut() throws RemoteException {
		app.switch2Lobby();	
	}
}
