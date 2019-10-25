package game.pickup;

import game.GameLevel;
import game.Player;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupSize extends Pickup.Active {
	public PickupSize(GameLevel l) {
		super(l);
	}

    @Override
    public void affect(Player player) {
        player.setSize(.25*(player.getWidth() + player.getHeight()));
        super.affect(player);
    }
}
