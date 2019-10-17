package Remote;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import Shape.MyShape;

public interface IRemotePaint extends Remote {
	public void addShape(MyShape shape) throws RemoteException;
	public void setHistory(Vector<MyShape> paintHistory) throws RemoteException;
	public void clearHistory() throws RemoteException;
	public void addClientRMI(String ip, int port) throws RemoteException;
}
