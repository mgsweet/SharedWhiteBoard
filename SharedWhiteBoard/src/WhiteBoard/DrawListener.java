package WhiteBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import Shape.MyCircle;
import Shape.MyFreeDraw;
import Shape.MyLine;
import Shape.MyOval;
import Shape.MyPoint;
import Shape.MyRect;
import Shape.MyShape;

public class DrawListener extends MouseAdapter implements ActionListener {
	private MyPoint startP, endP;
	private WhiteBoardView wbv;

	private String toolName = "pen";
	private Color color = null;
	private Image drawBuffer = null;
	private int thickness = 2;
	private MyFreeDraw currentFreeDraw = null;

	DrawListener(WhiteBoardView wbv) {
		currentFreeDraw = null;
		this.wbv = wbv;
		this.thickness = 2;
		color = wbv.getCurrentColor();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("")) {
			JButton button = (JButton) e.getSource();
			color = wbv.getCurrentColor();
		} else {
			JButton button = (JButton) e.getSource();
			toolName = button.getActionCommand();
		}
	}

	public void mousePressed(MouseEvent e) {
//		System.out.println("Operation: " + "mousePressed " + toolName);
		color = wbv.getCurrentColor();
		startP = new MyPoint(e.getX(), e.getY());
		drawBuffer = wbv.getPaintBoardPanel().createImage(wbv.getPaintBoardPanel().getWidth(),
				wbv.getPaintBoardPanel().getHeight());
	}

	public void mouseReleased(MouseEvent e) {
//		System.out.println("Operation: " + "mouseReleased " + toolName);
		endP = new MyPoint(e.getX(), e.getY());
		MyShape myShape = null;
		if (toolName.equals("line")) {
			myShape = new MyLine(startP, endP, color, thickness);
		} else if (toolName.equals("circle")) {
			myShape = new MyCircle(startP, endP, color, thickness);
		} else if (toolName.equals("rect")) {
			myShape = new MyRect(false, startP, endP, color, thickness);
		} else if (toolName.equals("roundrect")) {
			myShape = new MyRect(true, startP, endP, color, thickness);
		} else if (toolName.equals("oval")) {
			myShape = new MyOval(startP, endP, color, thickness);
		} else if (toolName.equals("pen")) {
			if (currentFreeDraw == null) {
				currentFreeDraw = new MyFreeDraw(MyFreeDraw.PEN, startP, endP, color, thickness);
			} else {
				currentFreeDraw.addPoints(endP);
			}
			myShape = currentFreeDraw;
			currentFreeDraw = null;
		} else if (toolName.equals("eraser")) {
			if (currentFreeDraw == null) {
				currentFreeDraw = new MyFreeDraw(MyFreeDraw.ERASER, startP, endP, wbv.getBackgroundColor(), thickness);
			} else {
				currentFreeDraw.addPoints(endP);
			}
			myShape = currentFreeDraw;
			currentFreeDraw = null;
		} else {
			System.out.println("Error: Unkown Tool Name!");
		}
//		wbv.getPaintBoardPanel().revalidate();
		wbv.getPaintBoardPanel().setBufferShape(null);
		wbv.getPaintManager().addShape(myShape);
	}

	public void mouseDragged(MouseEvent e) {
		endP = new MyPoint(e.getX(), e.getY());
		MyShape bufferShape = null;
		if (toolName.equals("pen")) {
			if (currentFreeDraw == null) {
				currentFreeDraw = new MyFreeDraw(MyFreeDraw.PEN, startP, endP, color, thickness);
			} else {
				currentFreeDraw.addPoints(endP);
			}
			bufferShape = currentFreeDraw;
		} else if (toolName.equals("eraser")) {
			if (currentFreeDraw == null) {
				currentFreeDraw = new MyFreeDraw(MyFreeDraw.ERASER, startP, endP, wbv.getBackgroundColor(), thickness);
			} else {
				currentFreeDraw.addPoints(endP);
			}
			bufferShape = currentFreeDraw;
		} else if (toolName.equals("line")) {
			bufferShape = new MyLine(startP, endP, color, thickness);
		} else if (toolName.equals("circle")) {
			bufferShape = new MyCircle(startP, endP, color, thickness);
		} else if (toolName.equals("rect")) {
			bufferShape = new MyRect(false, startP, endP, color, thickness);
		} else if (toolName.equals("roundrect")) {
			bufferShape = new MyRect(true, startP, endP, color, thickness);
		} else if (toolName.equals("oval")) {
			bufferShape = new MyOval(startP, endP, color, thickness);
		} else {
			System.out.println("Error: Unkown Tool Name!");
		}
		// When working on RMI, no need to upload bufferShape. Only
		// show in client window.
		wbv.getPaintBoardPanel().setBufferShape(bufferShape);
		wbv.getPaintBoardPanel().repaint();
	}

}
