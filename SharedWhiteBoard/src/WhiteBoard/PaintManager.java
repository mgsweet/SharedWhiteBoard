package WhiteBoard;

import java.util.Vector;

import Remote.IRemotePaint;
import Shape.MyShape;

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
	public static final int SERVER_MODE = 0;
	public static final int CLIENT_MODE = 0;
	public static final int OFFLINE_MODE = 0;

	public PaintManager(int mode) {
		this.mode = mode;
		paintHistory = new Vector<MyShape>();
		
		if (mode == SERVER_MODE) {
			clientRemotePaints = new Vector<IRemotePaint>();
		}
	}

	public PaintBoardPanel getPaintArea() {
		return paintArea;
	}
	
	public void setPaintArea(PaintBoardPanel paintArea) {
		this.paintArea = paintArea;
	}
	
	public void resetAll() {
		paintHistory.clear();
		paintArea.removeAll();
		paintArea.revalidate();
		paintArea.repaint();
	}

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

	public Vector<MyShape> getPaintHistory() {
		return paintHistory;
	}

	public void setPaintHistory(Vector<MyShape> paintHistory) {
		this.paintHistory = paintHistory;
		paintArea.repaint();
	}
}
