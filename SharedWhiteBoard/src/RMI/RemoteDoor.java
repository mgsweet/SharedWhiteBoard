package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ClientUser.UserManager;

/**
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 20, 2019 3:41:09 PM
 */

public class RemoteDoor extends UnicastRemoteObject implements IRemoteDoor {

	UserManager userManager;

	public RemoteDoor(UserManager userManager) throws RemoteException{
		this.userManager = userManager;
	}

	public void knock(String userId, String ip, int registerPort) throws RemoteException {
		userManager.addVistor(userId, ip, registerPort);
	}

	public void cancelKnock(String userId) throws RemoteException {
		userManager.removeVistor(userId);
	}

}