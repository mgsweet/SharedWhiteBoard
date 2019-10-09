package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class MyPenDraw implements MyShape{
    private Vector<MyPoint> points;
    private Color color;
    private int thickness;

    public MyPenDraw(MyPoint firstP, MyPoint nextP, Color color, int thickness) {
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
        BasicStroke bs = new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        g.setStroke(bs);
        g.setColor(color);
        for (int i = 0; i < points.size() - 1; i++) {
            g.drawLine(points.get(i).getX(), points.get(i).getY(),
                    points.get(i + 1).getX(), points.get(i + 1).getY());
        }
    }
}
