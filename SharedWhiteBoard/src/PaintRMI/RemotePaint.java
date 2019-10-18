package PaintRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import Shape.MyShape;
import WhiteBoard.PaintBoardPanel;
import WhiteBoard.PaintManager;

/**
 * Implement of IRemotePaint.
 * @author Aaron-Qiu E-mail: mgsweet@126.com
 * @version Created: Oct 18, 2019 11:20:36 AM
 */

public class RemotePaint extends UnicastRemoteObject implements IRemotePaint {
	private static final long serialVersionUID = 1L;
	// All the painting action should be do by the paintManager.
	PaintManager paintManager;
	// Use to store the current paintint area
	private PaintBoardPanel paintArea;

	public RemotePaint(PaintManager paintManager) throws RemoteException {
		this.paintManager = paintManager;
	}
	
	@Override
	public void addShape(MyShape shape) throws RemoteException {
		paintManager.addShape(shape);
	}
	
	@Override
	public void setHistory(Vector<MyShape> paintHistory) throws RemoteException {
		paintManager.setPaintHistory(paintHistory);
		paintManager.getPaintArea().repaint();
	}
	
	@Override
	public void clearHistory() throws RemoteException {
		paintManager.resetAll();
	}
	
	@Override
	public void addClientRMI(String ip, int port) throws RemoteException {
		System.out.println("A new client join: " + ip + ' ' + port);
		paintManager.addClientRMI(ip, port);
	}
	
	@Override
	public Vector<MyShape> getHistory() throws RemoteException {
		return paintManager.getPaintHistory();
	}
}
