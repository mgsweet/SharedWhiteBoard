package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
import Shape.MyShape;

/**
 * Interface of RemotePaint
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 11:19:54 AM
 */

public interface IRemotePaint extends Remote {
	public void addShape(MyShape shape) throws RemoteException;
	public void setHistory(Vector<MyShape> paintHistory) throws RemoteException;
	public void clearHistory() throws RemoteException;
	public void addClientRMI(String ip, int port) throws RemoteException;
	public Vector<MyShape> getHistory() throws RemoteException;
}
