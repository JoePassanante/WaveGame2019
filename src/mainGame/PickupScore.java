package mainGame;

import java.awt.Rectangle;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupScore extends GameObject {
	public PickupScore(double x, double y, Handler handler) {
		super(x, y, 30, 30, ID.PickupScore, handler);
	}

	public void tick() {
	}
}
