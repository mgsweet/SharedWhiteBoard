/**
 * @author Aaron-Qiu
 */
package WhiteBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Shape.MyOval;
import Shape.MyPoint;
import Shape.MyShape;

public class PaintBoardPanel extends JPanel {
	private int x = 200;
	private int y = 200;

	private PaintManager paintManager;
	// Use to store the temporary shape.
	private MyShape bufferShape;

	public PaintBoardPanel(PaintManager paintManager) {
		super();
		this.paintManager = paintManager;
		bufferShape = null;
		this.setBackground(Color.WHITE);
	}

	public void clearShapes() {
		bufferShape = null;
		paintManager.clearAll();
	}

	public void setBufferShape(MyShape bufferShape) {
		this.bufferShape = bufferShape;
	}

	/**
	 * The function repaint can invoke this paint function.
	 */
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		
		// When you need to using the Windowbuilder to design the whiteBoardView,
		// annotate all the code below.
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for (MyShape myShape : paintManager.getPaintHistory()) {
			myShape.draw(g);
		}
		// When mouse haven't been released
		if (bufferShape != null) {
			bufferShape.draw(g);
		}
	}

	private Image offScreenImage;

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(getWidth(), getHeight());
		}
		Graphics gImage = offScreenImage.getGraphics();
		paint(gImage);
		g.drawImage(offScreenImage, 0, 0, null);
	}
}
