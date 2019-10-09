import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class FileMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	public FileMenu(MyCanvas panel) {
		super();
		
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.repaint();
			}
		});
		//newMenuItem.setMnemonic(KeyEvent.VK_N);
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.addActionListener(new FileOpeningListener(panel));
	    JMenuItem saveMenuItem = new JMenuItem("Save");
	    saveMenuItem.addActionListener(new FileSavingListener(panel, "save"));
	    JMenuItem saveAsMenuItem = new JMenuItem("SaveAs");
	    saveMenuItem.addActionListener(new FileSavingListener(panel, "saveAs"));
	    JMenuItem closeMenuItem = new JMenuItem("Close");
	    closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	    this.add(newMenuItem);
	    this.add(openMenuItem);
	    this.add(saveMenuItem);
	    this.add(saveAsMenuItem);
	    this.add(closeMenuItem);
	}

}
