package mainGame;

/**
 * TO BE IMPLEMENTED - adds health to the player when they move over it
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupFreeze extends GameObject {
	public PickupFreeze(double x, double y, Handler handler) {
		super(x, y, 30, 30, ID.PickupFreeze, handler);
	}

	public void tick() {
	}
}
