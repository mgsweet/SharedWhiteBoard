package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MyCircle implements MyShape {
	private final int type = 3;
    private MyPoint center;
    private int radius;
    private Color color;
    private int thickness;

    public MyCircle(MyPoint startP, MyPoint endP, Color color, int thickness) {
    	this.radius = (int) Math.sqrt(Math.pow(startP.getX() - endP.getX(), 2)
                + Math.pow(startP.getY() - endP.getY(), 2));
        int radiusX = startP.getX() - this.radius / 2;
        int radiusY = startP.getY() - this.radius / 2;
    	this.center = new MyPoint(radiusX, radiusY);
        this.color = color;
        this.thickness = thickness;
    }

    public void draw(Graphics2D g) {
        BasicStroke bs = new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        g.setStroke(bs);
        g.setColor(color);
        g.drawArc(center.getX(), center.getY(), radius, radius, 0, 360);
    }

}
