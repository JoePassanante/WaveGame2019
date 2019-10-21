package game.pickup;

import game.GameLevel;
import game.Player;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupHealth extends Pickup {
    public PickupHealth(GameLevel l) {
        super(l);
	}

    @Override
    public void affect(Player player) {
        player.setHealth(player.getMaxHealth());
        super.affect(player);
    }
}
