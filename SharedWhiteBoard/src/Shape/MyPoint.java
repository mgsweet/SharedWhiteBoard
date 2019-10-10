package Shape;

import java.awt.Graphics;
import java.awt.Point;

public class MyPoint {
	private int x;
	private int y;
	
	public MyPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean equals(Point p) {
		// TODO Auto-generated method stub
		return (this.x == p.getX() && this.y == p.getY());
	}
}
