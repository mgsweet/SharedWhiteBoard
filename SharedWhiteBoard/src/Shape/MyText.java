package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MyText implements MyShape {
	private MyPoint startP;
	private String text;
	private int size;
	private Color color;

	public MyText(MyPoint startP, String text, int size, Color color) {
		this.startP = startP;
		this.text = text;
		this.size = size;
		this.color = color;
	}

	public void draw(Graphics2D g) {
		Font font = new Font(Font.DIALOG, Font.PLAIN, this.size);
		g.setFont(font);
		g.setColor(color);
		g.drawString(text, startP.getX(), startP.getY());
	}
}
