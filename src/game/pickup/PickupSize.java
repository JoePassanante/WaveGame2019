package game.pickup;

import game.GameLevel;
import game.Player;

/**
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 */

public class PickupSize extends Pickup.Active {
    public PickupSize(GameLevel l) {
        super(l);
    }

    private double shrink = 1;

    @Override
    public void affect(Player player) {
        shrink /= 5;
        player.setSize(Math.min(player.getWidth(), player.getHeight() * (1 - shrink)));
    }
}
