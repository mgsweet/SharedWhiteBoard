package FileOperationListener;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

import Shape.MyImage;
import Shape.MyPoint;
import Shape.MyShape;
import WhiteBoard.WhiteBoardView;

public class FileOpenListener implements ActionListener {
	private JFileChooser chooser;
	private WhiteBoardView wbv;

	public FileOpenListener(WhiteBoardView wbv) {
		super();
		this.wbv = wbv;
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG/JPEG/PNG files", "jpg", "jpeg", "png");
		chooser.setFileFilter(filter);
		chooser.setAccessory(new ImagePreviewer(chooser));
		chooser.setFileView(new FileImageView(filter, new ImageIcon()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Image image=null;
		System.out.println("Operation: Open File.");
		chooser.setCurrentDirectory(new File("."));
		int returnVal = chooser.showOpenDialog(wbv.getFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File imageFile = chooser.getSelectedFile();
			wbv.getPaintBoardPanel().clearShapes();
			MyShape myShape = new MyImage(new MyPoint(0, 0), imageFile);
			wbv.getPaintManager().paint(myShape);
			wbv.getPaintBoardPanel().revalidate();
			wbv.getPaintBoardPanel().repaint();
		}
	}
}
