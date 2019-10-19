package WhiteBoard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

import RMI.IRemotePaint;
import Shape.MyShape;

/**
 * 
 * @author Aaron-Qiu
 *
 */

public class PaintManager {
	// Use to record all the shapes.
	private Vector<MyShape> paintHistory = null;
	// Use to store the current paintint area
	private PaintBoardPanel paintArea;
	// Store all the other client
	private Vector<IRemotePaint> clientRemotePaints;
	// Use to define whether the PaintManager belong to a server.
	private IRemotePaint serveRemotePaint;
	// There are three kind of mode: server, client and offline
	private int mode;
	// Default mode.
	public static final int SERVER_MODE = 0;
	public static final int CLIENT_MODE = 1;
	public static final int OFFLINE_MODE = 2;
	
	/**
	 * Use in the client mode, when 
	 */
	public void pullRemoteHistory() {
		if (mode == CLIENT_MODE) {
			try {
				if (serveRemotePaint != null) {
					paintHistory = serveRemotePaint.getHistory();
				} else {
					System.out.println("Err: serveRemotePaint is null!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			paintArea.repaint();
		}
	}

	/**
	 * 
	 * @param mode There are three kind of mode: server, client and offline
	 */
	public PaintManager(int mode) {
		this.mode = mode;
		paintHistory = new Vector<MyShape>();

		if (mode == SERVER_MODE) {
			clientRemotePaints = new Vector<IRemotePaint>();
		}
	}
	
	/**
	 * Add a client clientRMI, onlu work in SERVER_MODE.
	 * @param ip
	 * @param port
	 */
	public void addClientRMI(String ip, int port) {
		if (mode == SERVER_MODE) {
			try {
				Registry clientRegistry = LocateRegistry.getRegistry(ip, port);
				IRemotePaint remotePaint = (IRemotePaint) clientRegistry.lookup("paintRMI");
				clientRemotePaints.add(remotePaint);
			} catch (Exception e) {
				System.out.println("Can not get the client registry.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Set the set ServeRemotePaint, only work in CLIENT_MODE
	 * @param serverRemotePaint
	 */
	public void setServerRMI(IRemotePaint serverRemotePaint) {
		if (mode == CLIENT_MODE) {
			this.serveRemotePaint = serverRemotePaint;
		}
	}

	/**
	 * Use to get the paint area.
	 * 
	 * @return The current PaintBoardPanel object.
	 */
	public PaintBoardPanel getPaintArea() {
		return paintArea;
	}

	/**
	 * Set the current painting area.
	 * 
	 * @param paintArea the PaintBoardPanel where you want to paint.
	 */
	public void setPaintArea(PaintBoardPanel paintArea) {
		this.paintArea = paintArea;
	}

	/**
	 * Remove all things inside the current painting area.
	 */
	public void resetAll() {
		paintHistory.clear();
		paintArea.removeAll();
		paintArea.revalidate();
		paintArea.repaint();
	}

	/**
	 * Use to add shape to the history. Would affect both server and client.
	 * 
	 * @param shape a MyShape object which you want to add to the current painting
	 *              area
	 */
	public void addShape(MyShape shape) {
		if (mode == SERVER_MODE) {
			paintHistory.add(shape);
			for (IRemotePaint x : clientRemotePaints) {
				try {
					x.setHistory(paintHistory);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			paintArea.repaint();
		} else if (mode == CLIENT_MODE) {
			try {
				serveRemotePaint.addShape(shape);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// TODO
		}
	}

	/**
	 * Use to clear the history, which would affect both server and client.
	 */
	public void clearAll() {
		if (mode == SERVER_MODE) {
			paintHistory.clear();
			for (IRemotePaint x : clientRemotePaints) {
				try {
					x.clearHistory();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			paintArea.repaint();
		} else if (mode == CLIENT_MODE) {
			System.out.println("Err: No clear opeartion access!");
		} else {
			// TODO
		}
	}

	/**
	 * Get the painting history.
	 * 
	 * @return paint history.
	 */
	public Vector<MyShape> getPaintHistory() {
		return paintHistory;
	}

	/**
	 * Set the painting history.
	 * 
	 * @param paintHistory
	 */
	public void setPaintHistory(Vector<MyShape> paintHistory) {
		this.paintHistory = paintHistory;
		paintArea.repaint();
	}
}
