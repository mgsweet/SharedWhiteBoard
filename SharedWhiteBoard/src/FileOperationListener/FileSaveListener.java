package FileOperationListener;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import WhiteBoard.WhiteBoardView;

public class FileSaveListener implements ActionListener {
	private WhiteBoardView wbv;
	private String action = null;
	private String name;

	public FileSaveListener(WhiteBoardView wbv, String action) {
		super();
		this.wbv = wbv;
		this.action = action;
		this.name = "";
	}

	public void actionPerformed(ActionEvent e) {
		try {
			savePic();
		} catch (Exception er) {
			JOptionPane.showMessageDialog(wbv.getFrame(), "Failed to save image");
			er.printStackTrace();
		}
	}

	public String savePic() throws IOException {
		System.out.println("Opeartion: Save-" + action);

		Dimension imagesize = wbv.getPaintBoardPanel().getSize();
		BufferedImage image = new BufferedImage(imagesize.width, imagesize.height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = image.createGraphics();
		wbv.getPaintBoardPanel().paint(graphics);
		graphics.dispose();

		if (action == "saveAs") {
			JFileChooser jf = new JFileChooser(".");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG/JPEG/PNG files", "jpg", "jpeg",
					"png");
			jf.setFileFilter(filter);
			int value = jf.showSaveDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				File imagefile = jf.getSelectedFile(); // get the image file
				File file = new File(imagefile.getPath() + ".jpg");
				if (!file.exists()) {
					file.createNewFile();
					ImageIO.write(image, "jpg", file);
				} else {
					// Duplicated image name
					int confirm = JOptionPane.showConfirmDialog(wbv.getFrame(),
							"This name has existed in this path. Replace the file?", "Warning",
							JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						ImageIO.write(image, "jpg", file);
					}
				}
			}
		} else {
			// Save jpg
			if (name == "") {
				name = JOptionPane.showInputDialog(null, "Image name:");
			}
			
			if (name != null) {
				name = name.trim();
				if (name.equals("")) {
					JOptionPane.showMessageDialog(wbv.getFrame(), "Please input image name");
				} else {
					// Not handle duplicate
					File file = new File(name + ".jpg");
					file.createNewFile();
					ImageIO.write(image, "jpg", file);
				}
			}
		}

		return null;
	}
}
