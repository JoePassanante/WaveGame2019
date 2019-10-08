package game.pickup;

import game.GameObject;
import game.waves.Handler;

import java.awt.*;

/**
 * TO BE IMPLEMENTED - adds health to the player when they move over it
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupHealth extends GameObject {
    public PickupHealth(Point.Double p, Handler handler) {
        super(p.x, p.y, 30, 30, handler);
	}

	public void tick() {
	}
}
