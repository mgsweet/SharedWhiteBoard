package Remote;
import java.rmi.Remote;
import java.rmi.RemoteException;

import Shape.MyShape;

public interface IRemotePaint extends Remote {
	public void paint(MyShape shape) throws RemoteException;
}
