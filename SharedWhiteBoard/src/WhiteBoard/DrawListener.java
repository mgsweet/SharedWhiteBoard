package WhiteBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Vector;

import javax.swing.JButton;

import Shape.MyLine;
import Shape.MyOval;
import Shape.MyPenDraw;
import Shape.MyLine;
import Shape.MyPoint;
import Shape.MyRect;
import Shape.MyShape;

public class DrawListener extends MouseAdapter implements ActionListener {
	private MyPoint startP, endP;
	private WhiteBoardView wbv;
	private boolean flag = false;
	private static final double BREADTH = 15.0;
	private int dragType;
	private static final int DRAG_MOVE = 1;
	private static final int DRAG_UP = 2;
	private static final int DRAG_UPLEFT = 3;
	private static final int DRAG_UPRIGHT = 4;
	private static final int DRAG_LEFT = 5;
	private static final int DRAG_RIGHT = 6;
	private static final int DRAG_BOTTOM = 7;
	private static final int DRAG_BOTTOMLEFT = 8;
	private static final int DRAG_BOTTOMRIGHT = 9;
	private static final String pictureBox1 = null;

	private String toolName = "pen";
	private Color color = null;
	private Image drawBuffer = null;
	private int thickness = 0;
	private MyPenDraw myPenDraw = null;

	DrawListener(WhiteBoardView wbv) {
		myPenDraw = null;
		this.wbv = wbv;
		this.thickness = 1;
		color = wbv.getCurrentColor();
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if (e.getActionCommand().equals("")) {
			JButton button = (JButton) e.getSource();
			color = wbv.getCurrentColor();
			System.out.println("color = " + color);
		} else {
			JButton button = (JButton) e.getSource();
			toolName = button.getActionCommand();
			System.out.println("String = " + toolName);
		}
	}

	public void mousePressed(MouseEvent e) {
		color = wbv.getCurrentColor();
		startP = new MyPoint(e.getX(), e.getY());
		drawBuffer = wbv.getPaintBoardPanel().createImage(wbv.getPaintBoardPanel().getWidth(), 
															wbv.getPaintBoardPanel().getHeight());
	}

	public void mouseReleased(MouseEvent e) {
		endP = new MyPoint(e.getX(), e.getY());
		MyShape myShape = null;
		if (toolName.equals("line")) {
			myShape = new MyLine(startP, endP, color, thickness);
		} else if (toolName.equals("circle")) {
			myShape = new MyOval(startP, endP);
		} else if (toolName.equals("rect")) {
			myShape = new MyRect(false, startP, endP, color, thickness);
		} else if (toolName.equals("roudrect")) {
			myShape = new MyRect(true, startP, endP, color, thickness);
		} else if (toolName.equals("oval")) {
			myShape = new MyOval(startP, endP);
		} else if (toolName.equals("pen")) {
			myShape = myPenDraw;
			myPenDraw = null;
		}
//		wbv.getPaintBoardPanel().revalidate();
		wbv.getPaintBoardPanel().setBufferShape(null);
		wbv.getPaintBoardPanel().addShape(myShape);
		wbv.getPaintBoardPanel().repaint();
	}

	public void mouseClicked(MouseEvent e) {
//		if (toolName.equals("triangle")) {
//			double a = -2, b = -2, c = -1.2, d = 2;
//			double x = 0, xo = 0;
//			double y = 0, yo = 0;
//			Color[] Col = { Color.BLUE, Color.cyan, Color.green, Color.magenta, Color.red, Color.yellow };
//			for (int i = 0; i <= 90000; i++) {
//				Random r = new Random(); // 增加颜色
//				int R = r.nextInt(Col.length);
//				g.setColor(Col[R]);
//				x = Math.sin(a * yo) - Math.cos(b * xo);
//				y = Math.sin(c * xo) - Math.cos(d * yo);
//				int temp_x = (int) (x * 50);
//				int temp_y = (int) (y * 50);
//				g.drawLine(temp_x + 500, temp_y + 300, temp_x + 500, temp_y + 300);
//				xo = x;
//				yo = y;
//			}
//		}
	}

	public void mouseDragged(MouseEvent e) {
		endP = new MyPoint(e.getX(), e.getY());
		MyShape bufferShape = null;
		if (toolName.equals("pen")) {
			if (myPenDraw == null) {
				myPenDraw = new MyPenDraw(startP, endP, color, thickness);
			} else {
				myPenDraw.addPoints(endP);
			}
			bufferShape = myPenDraw;
		} else if (toolName.equals("eraser")) {
//			g.setStroke(new BasicStroke(80));
//			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setColor(Color.WHITE);
//			g.drawLine(x1, y1, x2, y2);
//			x1 = x2;
//			y1 = y2;
		} else if (toolName.equals("spray")) {
//			// g.setStroke(new BasicStroke(2));
//			// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//			// RenderingHints.VALUE_ANTIALIAS_ON);
//			for (int k = 0; k < 20; k++) {
//				Random i = new Random();
//				int a = i.nextInt(8);
//				int b = i.nextInt(10);
//				g.drawLine(x2 + a, y2 + b, x2 + a, y2 + b);
//			}
		} else if (toolName.equals("line")) {
			bufferShape = new MyLine(startP, endP, color, thickness);
		} else if (toolName.equals("circle")) {
			bufferShape = new MyOval(startP, endP);
		} else if (toolName.equals("rect")) {
			bufferShape = new MyRect(false, startP, endP, color, thickness);
		} else if (toolName.equals("roudrect")) {
			bufferShape = new MyRect(true, startP, endP, color, thickness);
		} else if (toolName.equals("oval")) {
			bufferShape = new MyOval(startP, endP);
		} else {
			System.out.println("Error: Unkown Tool Name!");
		}
		wbv.getPaintBoardPanel().setBufferShape(bufferShape);
		wbv.getPaintBoardPanel().repaint();
	}

}
