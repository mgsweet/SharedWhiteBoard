package WhiteBoard;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Vector;

import ClientUser.UserManager;
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
	// User manager
	private UserManager userManager;
	// There are three kind of mode: server, client and offline
	private int mode;
	// Default mode.
	public static final int SERVER_MODE = 0;
	public static final int CLIENT_MODE = 1;
	public static final int OFFLINE_MODE = 2;
	
	/**
	 * 
	 * @param mode There are three kind of mode: server, client and offline
	 */
	public PaintManager(int mode, UserManager userManager) {
		this.mode = mode;
		this.userManager = userManager;
		paintHistory = new Vector<MyShape>();
	}
	
	
	/**
	 * Use in the client mode, when 
	 */
	public void pullRemoteHistory() {
		if (mode == CLIENT_MODE) {
			try {
				if (userManager.getHostRemotePaint() != null) {
					paintHistory = userManager.getHostRemotePaint().getHistory();
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
			Map<String, IRemotePaint> guestRemotePaint = userManager.getGuestRemotePaints();
			for (IRemotePaint x : guestRemotePaint.values()) {
				try {
					x.setHistory(paintHistory);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			paintArea.repaint();
		} else if (mode == CLIENT_MODE) {
			try {
				userManager.getHostRemotePaint().addShape(shape);
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
			Map<String, IRemotePaint> guestRemotePaint = userManager.getGuestRemotePaints();
			for (IRemotePaint x : guestRemotePaint.values()) {
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
