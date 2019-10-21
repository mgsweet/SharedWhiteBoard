package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of remote user manager.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 8:40:17 PM
 */

public interface IRemoteUM extends Remote {
	/**
	 * use to ask the client who has knock the door of host to come in.
	 * @throws RemoteException
	 */
	public void askIn(String hostId, String hostIp, int hostRegisterPort) throws RemoteException;
	public void askOut() throws RemoteException;
}
