package Remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import Shape.MyShape;
import WhiteBoard.PaintBoardPanel;
import WhiteBoard.PaintManager;

public class RemotePaint extends UnicastRemoteObject implements IRemotePaint {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
}
