package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 3:39:27 PM
 */

public interface IRemoteDoor extends Remote {
	public void knock(String userId, String ip, int registerPort) throws RemoteException;
	public void cancelKnock(String userId) throws RemoteException;
}
