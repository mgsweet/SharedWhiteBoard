package Menus;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import App.App;
import Shape.MyShape;
import WhiteBoard.WhiteBoardView;

public class FileSaveListener implements ActionListener {
	private WhiteBoardView wbv;
	private App app;
	private String action = null;

	public FileSaveListener(WhiteBoardView wbv, App app, String action) {
		super();
		this.wbv = wbv;
		this.app = app;
		this.action = action;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			savePic();
		} catch (Exception er) {
			JOptionPane.showMessageDialog(wbv.getFrame(), "Failed to save image");
			er.printStackTrace();
		}
	}
	
	private void savePic() throws IOException {
		System.out.println("Opeartion: Save-" + action);

		Dimension imagesize = wbv.getPaintBoardPanel().getSize();
		BufferedImage image = new BufferedImage(imagesize.width, imagesize.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = image.createGraphics();
		wbv.getPaintBoardPanel().paint(graphics);
		graphics.dispose();

		if (action == "saveAs") {
			saveAsPic(image);
			
		} else {
			// Save jpg
			String currentPath = app.getCurrentSavePath();
			if (currentPath == null) {
				saveAsPic(image);
			} else {
				// Not handle duplicate
				Vector<MyShape> history = wbv.getPaintManager().getPaintHistory();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(currentPath));
				oos.writeObject(history);
				oos.close();
					
			}
		}

	}
	
	private void saveAsPic(BufferedImage image) throws IOException {
		JFileChooser jf = new JFileChooser(".");
		jf.setAcceptAllFileFilterUsed(false);
		jf.addChoosableFileFilter(new FileNameExtensionFilter("WhiteBoard files", "wb"));
		jf.addChoosableFileFilter(new FileNameExtensionFilter("JPG files", "jpg"));
		jf.addChoosableFileFilter(new FileNameExtensionFilter("PNG files", "png"));
		
		int value = jf.showSaveDialog(null);
		if (value == JFileChooser.APPROVE_OPTION) {
			File imagefile = jf.getSelectedFile(); // get the image file
			FileNameExtensionFilter fileFilter = (FileNameExtensionFilter) jf.getFileFilter();
			String format = fileFilter.getExtensions()[0];
			String savePath = imagefile.getPath() + "." + format;
			if (format.equals("wb")) {
				Vector<MyShape> history = wbv.getPaintManager().getPaintHistory();
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savePath));
				oos.writeObject(history);
				oos.close();
				app.setCurrentSavePath(savePath);
				System.out.println(savePath);
			} else {
				File file = new File(savePath);
				if (!file.exists()) {
					file.createNewFile();
					ImageIO.write(image, format, file);
				} else {
					// Duplicated image name
					int confirm = JOptionPane.showConfirmDialog(wbv.getFrame(),
							"This name has existed in this path. Replace the file?", "Warning",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						ImageIO.write(image, format, file);
					}
				}
			}
		}
	}
	
}
