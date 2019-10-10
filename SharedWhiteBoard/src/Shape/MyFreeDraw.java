package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import java.util.Vector;

public class MyFreeDraw implements MyShape {
	public static final int PEN = 0;
	public static final int SPRAY = 1;
	public static final int ERASER = 1;

	private Vector<MyPoint> points;
	private Color color;
	private int thickness;
	private int isSpray;
	private int mode;

	public MyFreeDraw(int mode, MyPoint firstP, MyPoint nextP, Color color, int thickness) {
		this.mode = mode;
		this.points = new Vector<MyPoint>();
		this.points.add(firstP);
		this.points.add(nextP);
		this.color = color;
		this.thickness = thickness;
	}

	public void addPoints(MyPoint nextP) {
		this.points.add(nextP);
	}

	public void draw(Graphics2D g) {
		BasicStroke bs = null;
		if (mode == PEN) {
			g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		} else if (mode == ERASER) {
			g.setStroke(new BasicStroke(40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		}
		g.setColor(color);
		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
		}
	}
}
