package Menus;

import javax.swing.Icon;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;

class FileImageView extends FileView {
	private FileNameExtensionFilter filter;
	private Icon icon;

	public FileImageView(FileNameExtensionFilter filter, Icon anIcon) {
		filter = filter;
		icon = anIcon;
	}
}
