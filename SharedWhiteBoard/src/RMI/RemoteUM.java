package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import ClientUser.User;
import ClientUser.UserManager;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 21, 2019 5:41:14 PM
 */

public class RemoteUM extends UnicastRemoteObject implements IRemoteUM {
	UserManager um;
	
	public RemoteUM(UserManager um) throws RemoteException {
		this.um = um;
	}

	@Override
	public void setGuests(Map<String, User> guest) throws RemoteException {
		System.out.println("Host sett the guest list.");
		um.setGuests(guest);
	}

	@Override
	public Map<String, User> pullGuests() throws RemoteException {
		System.out.println("A guest get the guest list.");
		return um.getGuests();
	}
}
