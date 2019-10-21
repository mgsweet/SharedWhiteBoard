package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import ClientUser.User;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 21, 2019 5:41:03 PM
 */

public interface IRemoteUM extends Remote {
	public void setGuests(Map<String, User> guest) throws RemoteException;
	public Map<String, User> pullGuests() throws RemoteException;
}
