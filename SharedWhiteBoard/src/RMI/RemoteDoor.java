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

	@Override
	public void knock(String userId, String ip, int registerPort) throws RemoteException {
		System.out.println("A visitor knocks the door.");
		userManager.addVistor(userId, ip, registerPort);
	}

	@Override
	public void cancelKnock(String userId) throws RemoteException {
		System.out.println("A visitor leaves.");
		userManager.removeVistor(userId);
	}

	@Override
	public void leave(String userId) throws RemoteException {
		System.out.println("A guest leaves.");
		userManager.removeGuest(userId);
	}
}
