/**
 * My Image
 * 
 * @author Aaron-Qiu
 *
 */
package Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyImage implements MyShape {
	private MyPoint stratP;
	private Image image = null;

	public MyImage(MyPoint startP, File imageFile) {
    	this.stratP = startP;
    	try {
			this.image = ImageIO.read(imageFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
    }

	public void draw(Graphics2D g) {
		try {
			if (image != null) g.drawImage(image, 0, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
