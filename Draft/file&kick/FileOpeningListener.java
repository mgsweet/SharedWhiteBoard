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

public class FileOpeningListener implements ActionListener{
	private JFileChooser chooser;
	private JLabel label;
	MyCanvas panel = null;
	
	public FileOpeningListener(MyCanvas panel) {
		super();
		this.panel = panel;
		label = new JLabel();  
		panel.add(label);
		chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    "JPG/JPEG/GIF/PNG files", "jpg", "jpeg", "gif", "png");
		chooser.setFileFilter(filter);
		chooser.setAccessory(new ImagePreviewer(chooser));
		chooser.setFileView(new FileImageView(filter,new ImageIcon()));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		chooser.setCurrentDirectory(new File("."));
		int returnVal = chooser.showOpenDialog(panel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File imagefile = chooser.getSelectedFile();
			panel.repaint();
			panel.clearPanel();
			label.setIcon(new ImageIcon(imagefile.getPath()));
		}
	 }
}

class FileImageView extends FileView {
	private FileNameExtensionFilter filter;
	private Icon icon;

	public FileImageView(FileNameExtensionFilter filter, Icon anIcon) {
		filter = filter;
		icon = anIcon;
	}
}

class ImagePreviewer extends JLabel {
	private static final long serialVersionUID = 1L;

	public ImagePreviewer(JFileChooser chooser) {
		setPreferredSize(new Dimension(100, 100));
		setBorder(BorderFactory.createEtchedBorder());

		chooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
			    if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
			    	File f = (File) event.getNewValue();
			    	if (f == null) {
			    		setIcon(null);
			    		return;
			    	}
			    	ImageIcon icon = new ImageIcon(f.getPath());
			    	if (icon.getIconWidth() > getWidth()) {
			    		icon = new ImageIcon(icon.getImage().getScaledInstance(
			    			getWidth(), -1, Image.SCALE_DEFAULT));
			    		setIcon(icon);
			    	}
			    }
			}
		 });
	 }
}
