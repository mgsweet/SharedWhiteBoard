package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * My Rectangle
 * @author Aaron-Qiu
 *
 */

public class MyRect implements MyShape{
    private MyPoint startP;
    private int width;
    private int height;
    private Color color;
    private int thickness;
    private Boolean isRoundRect;

    public MyRect(Boolean isRoundRect, MyPoint startP, MyPoint endP, Color color, int thickness) {
        this.startP = startP;
        this.width = Math.abs(startP.getX() - endP.getX());
        this.height = Math.abs(startP.getY() - endP.getY());;
        this.color = color;
        this.thickness = thickness;
        this.isRoundRect = isRoundRect;
    }

    public void draw(Graphics2D g) {
        BasicStroke bs = new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        g.setStroke(bs);
        g.setColor(color);
        if (isRoundRect) {
        	g.drawRoundRect(startP.getX(), startP.getY(), width, height, 2, 10);
        } else {
        	g.drawRect(startP.getX(), startP.getY(), width, height);
        }
    }
}
