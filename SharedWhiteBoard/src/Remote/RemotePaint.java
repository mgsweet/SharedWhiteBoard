package Remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Shape.MyShape;

public class RemotePaint extends UnicastRemoteObject implements IRemotePaint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemotePaint() throws RemoteException {
		
	}
	
	@Override
	public void paint(MyShape shape) throws RemoteException {
		
	}
}
