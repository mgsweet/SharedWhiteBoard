package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface of remote room manager.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 19, 2019 8:40:17 PM
 */

public interface IRemoteRefresh extends Remote{
	public void refreshLobby() throws RemoteException;
}
