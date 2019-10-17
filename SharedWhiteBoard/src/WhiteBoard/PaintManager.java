package WhiteBoard;

import java.util.Vector;

import Shape.MyShape;

public class PaintManager {
	// Use to record all the shapes.
	private Vector<MyShape> paintHistory = null;
	
	public PaintManager() {
		paintHistory = new Vector<MyShape>();
	}
	
	public void paint(MyShape shape) {
		paintHistory.add(shape);
	}
	
	public Vector<MyShape> getPaintHistory(){
		return paintHistory;
	}
	
	public void clear() {
		paintHistory.clear();
	}
}
