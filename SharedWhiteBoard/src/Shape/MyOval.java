package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MyOval implements MyShape {
	private MyPoint startP;
	private int width;
	private int height;
	private Color color;
	private int thickness;
	
	public MyOval(MyPoint startP, MyPoint endP, Color color, int thickness) {
		this.color = color;
		this.thickness = thickness;
		this.width = Math.abs(startP.getX() - endP.getX());
		this.height = Math.abs(startP.getY() - endP.getY());
		if (startP.getX() < endP.getX()) {
            if (startP.getY() < endP.getY()) {
                this.startP = startP;
            } else {
            	this.startP = new MyPoint(startP.getX(), endP.getY());
            }
        } else {
            if (startP.getY() < endP.getY()) {
            	this.startP = new MyPoint(endP.getX(), startP.getY());
            } else {
            	this.startP = endP;
            }
        }
	}
	
	public void draw(Graphics2D g) {
		BasicStroke bs = new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
		g.setStroke(bs);
        g.setColor(color);
		g.drawOval(startP.getX(), startP.getY(), width, height);
	}
}
