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

	public MyImage(MyPoint startP, Image image) {
    	this.stratP = startP;
    	this.image = image;
    }

	public void draw(Graphics2D g) {
		try {
			if (image != null) g.drawImage(image, 0, 0, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
