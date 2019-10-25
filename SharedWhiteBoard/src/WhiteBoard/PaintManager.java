package WhiteBoard;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import ClientUser.UserManager;
import Menus.EditMenu;
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
	// Use to handel redo and undo, only host can use.
	private Stack<MyShape> redoHistory = null;
	// Use to store the current paintint area
	private PaintBoardPanel paintArea;
	// Use to control the editMenu UI.
	private EditMenu editMenu;
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
		if (mode == SERVER_MODE)
			redoHistory = new Stack<MyShape>();
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
		if (mode == SERVER_MODE) {
			redoHistory.clear();
			if (editMenu != null) editMenu.updateEnable();
		}
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
	public synchronized void addShape(MyShape shape) {
		if (mode == SERVER_MODE) {
			paintHistory.add(shape);
			redoHistory.clear();
			if (editMenu != null) editMenu.updateEnable();
			Map<String, IRemotePaint> guestRemotePaint = userManager.getGuestRemotePaints();
			for (String guestId : guestRemotePaint.keySet()) {
				try {
					updateRemoteHistory(guestRemotePaint.get(guestId));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					userManager.removeGuest(guestId);
					System.err.println("Can't connect to guest " + guestId + ", Remove.");
//					e.printStackTrace();
				}
			}
			paintArea.repaint();
		} else if (mode == CLIENT_MODE) {
			try {
				userManager.getHostRemotePaint().addShape(shape);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set a specific remoteHistory to current paint history.
	 * 
	 * @param remotePaint
	 * @throws RemoteException 
	 */
	public void updateRemoteHistory(IRemotePaint remotePaint) throws RemoteException {
		remotePaint.setHistory(paintHistory);
	}

	/**
	 * Use to clear the history, which would affect both server and client.
	 */
	public void clearAll() {
		paintArea.clearBuffer();
		if (mode == SERVER_MODE) {
			paintHistory.clear();
			redoHistory.clear();
			if (editMenu != null) editMenu.updateEnable();
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

		if (mode == SERVER_MODE) {
			if (editMenu != null) editMenu.updateEnable();
			Map<String, IRemotePaint> guestRemotePaint = userManager.getGuestRemotePaints();
			for (IRemotePaint x : guestRemotePaint.values()) {
				try {
					updateRemoteHistory(x);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		paintArea.repaint();
	}

	/**
	 * Get whether the manager belongs to a client or
	 * 
	 * @return
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Undo, only host can use.
	 */
	public void undo() {
		if (mode == SERVER_MODE) {
			redoHistory.push(paintHistory.lastElement());
			paintHistory.remove(paintHistory.size() - 1);
			setPaintHistory(paintHistory);
		}
	}

	/**
	 * Redo, only host can use.
	 */
	public void redo() {
		if (mode == SERVER_MODE) {
			paintHistory.add(redoHistory.pop());
			setPaintHistory(paintHistory);
		}
	}
	
	/**
	 * Check whether undo is allowed.
	 * @return
	 */
	public Boolean isUndoAllow() {
		if (mode == SERVER_MODE) {
			return !paintHistory.isEmpty();
		} else {
			return false;
		}
	}
	
	/**
	 * Check whether redo is allowed.
	 * @return
	 */
	public Boolean isRedoAllow() {
		if (mode == SERVER_MODE) {
			return !redoHistory.isEmpty();
		} else {
			return false;
		}
	}
	
	/**
	 * Set the editMenu UI.
	 * @param editMenu
	 */
	public void setEditMenu(EditMenu editMenu) {
		this.editMenu = editMenu;
	}
	
	public void clearRedoHistory() {
		if (redoHistory != null) redoHistory.clear();
	}
}
