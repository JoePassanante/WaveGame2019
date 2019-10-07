package mainGame;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupSize extends GameObject {
	public PickupSize(Point.Double p, Handler handler) {
		super(p.x, p.y, 30, 30, handler);
	}

    @Override
    public void tick() {

    }
}
