package mainGame;

import java.awt.Point;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupFreeze extends GameObject {
	public PickupFreeze(Point.Double p, Handler handler) {
		super(p.x, p.y, 30, 30, ID.PickupFreeze, handler);
	}

	public void tick() {
	}
}
