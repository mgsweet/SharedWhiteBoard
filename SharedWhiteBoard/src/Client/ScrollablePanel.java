package Client;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

public class ScrollablePanel extends JPanel implements Scrollable {

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		if (getParent() == null) return getSize();
		Dimension d = getParent().getSize();
		int c = (int) Math.floor((d.width - getInsets().left - getInsets().right) / 50.0);
		if (c == 0) return d;
		int r = 20 / c;
		if (r * c < 20) ++r;
		return new Dimension(c * 50, r * 50);
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 50;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return getParent() != null ? getParent().getSize().width > getPreferredSize().width: true;
	}

}
