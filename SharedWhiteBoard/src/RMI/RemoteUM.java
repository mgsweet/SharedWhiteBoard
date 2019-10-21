package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import App.App;
import ClientUser.User;
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
	
	/**
	 * Open the um to the host.
	 * @param um
	 */
	public void setUM(UserManager um) {
		this.um = um;
	}
	
	public void updateGuest(Map<String, User> guests) {
		um.setGuests(guests);
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
