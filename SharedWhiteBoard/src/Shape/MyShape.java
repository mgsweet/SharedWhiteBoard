/**
 * My Shape interface
 * 
 * @author Aaron-Qiu
 *
 */
package Shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public interface MyShape extends Serializable {
	void draw(Graphics2D g);
}
