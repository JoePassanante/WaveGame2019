package mainGame;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupLife extends GameObject {
	public PickupLife(Point.Double p, Handler handler) {
        super(p.x, p.y, 30, 30, handler);
	}

	public void tick() {
	}
}
