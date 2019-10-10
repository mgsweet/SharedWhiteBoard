/**
 * My Shape
 * 
 * @author Aaron-Qiu
 *
 */
package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MyLine implements MyShape {
	private MyPoint startP;
	private MyPoint endP;
	Color color;
	int thickness;
	
	public MyLine(MyPoint startP, MyPoint endP, Color color, int thickness) {
		this.startP = startP;
		this.endP = endP;
		this.color = color;
		this.thickness = thickness;
	}
	
	public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL));
        g.setColor(color);
		g.drawLine(startP.getX(), startP.getY(), endP.getX(), endP.getY());
	}
}
