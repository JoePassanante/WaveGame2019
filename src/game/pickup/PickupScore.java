package game.pickup;

import game.GameLevel;
import game.Player;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupScore extends Pickup.Active {
	public PickupScore(GameLevel l) {
        super(l);
	}

    @Override
    public void affect(Player player) {
	    player.getLevel().setScore(getLevel().getScore() + 1000);
        super.affect(player);
    }
}
