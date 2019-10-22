package Menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import App.App;
import Shape.MyShape;
import WhiteBoard.WhiteBoardView;

public class FileOpenListener implements ActionListener {
	private JFileChooser chooser;
	private WhiteBoardView wbv;
	private String currentPath;
	
	private App app;

	public FileOpenListener(WhiteBoardView wbv, App app) {
		super();
		this.wbv = wbv;
		this.app = app;
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WhiteBoard files", "wb");
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setAccessory(new ImagePreviewer(chooser));
		chooser.setFileView(new FileImageView(filter, new ImageIcon()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Operation: Open File.");
		chooser.setCurrentDirectory(new File("."));
		int returnVal = chooser.showOpenDialog(wbv.getFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				currentPath = chooser.getSelectedFile().getPath();
				app.setCurrentSavePath(currentPath);
				System.out.println(currentPath);
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentPath));
				Vector<MyShape> history = (Vector<MyShape>) ois.readObject();
				wbv.getPaintManager().setPaintHistory(history);
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	protected String getImagePath() {
		return currentPath;
	}
}
