import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class FileSavingListener implements ActionListener{
	MyCanvas panel = null;
	String action = null;
	
	public FileSavingListener(MyCanvas panel, String action)
	{
		super();
		this.panel = panel;
		this.action = action;
	}
	
	public void actionPerformed(ActionEvent e) {
		try {	
			String name = JOptionPane.showInputDialog(null, "Image name:");

			if(name != null) {
				name = name.trim();
				if(name.equals("")) {
					JOptionPane.showMessageDialog(panel, "Please input image name");
				}else {
					savePic(name);
				}
			}
					
		} catch (Exception er) {
			JOptionPane.showMessageDialog(panel, "Failed to save image");
			er.printStackTrace();
		}
	}	
	
	public String savePic(String name) throws IOException{
		
		Dimension imagesize = panel.getSize();
		BufferedImage image = new BufferedImage(imagesize.width,imagesize.height,BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = image.createGraphics();
        panel.paint(graphics);
        graphics.dispose();
 
        //Resize the image to be saved
        //Image newImage = image.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        //BufferedImage myImage = new BufferedImage(28,28,BufferedImage.TYPE_INT_RGB);
        //Graphics graphics1 = myImage.getGraphics();
        //graphics1.drawImage(newImage, 0, 0, null);
        //graphics1.dispose();
 
        if(action=="saveAs") {
	        JFileChooser jf=new JFileChooser("d:/");
	        int value=jf.showSaveDialog(null);
	        if(value==JFileChooser.APPROVE_OPTION){ 
	        	File imagefile=jf.getSelectedFile(); //get the image file 
	        	File file=new File(imagefile.getPath());
	    		if( !file.exists() )
	    		{
	    			file.createNewFile();
	    			ImageIO.write(image, "jpg", file);
	    		} else {
	    			//Duplicated image name
	    			int confirm = JOptionPane.showConfirmDialog(panel, "This name has existed in this path. Replace the file?", "Warning", JOptionPane.YES_NO_OPTION);
	    			if(confirm == JOptionPane.YES_OPTION) {
	    				ImageIO.write(image, "jpg", file);
	    			}
	    		}
	        }
        } else {
        	File file=new File("src/"+name+".jpg");
    		if( !file.exists() )
    		{
    			file.createNewFile();
    			ImageIO.write(image, "jpg", file);
    		} else {
    			//Duplicated image name
    			int confirm = JOptionPane.showConfirmDialog(panel, "This name has existed in this path. Replace the file?", "Warning", JOptionPane.YES_NO_OPTION);
    			if(confirm == JOptionPane.YES_OPTION) {
    				ImageIO.write(image, "jpg", file);
    			}
    		}
        }
			
		return null;
	}

}
