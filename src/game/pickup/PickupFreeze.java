package game.pickup;

import game.GameLevel;
import game.Player;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupFreeze extends Pickup {
	public PickupFreeze(GameLevel l) {
		super(l, 900);
	}

    @Override
    public void affect(Player player) {
        player.getLevel().stream().filter(go -> !(go instanceof Player)).forEach(go -> {
            go.setX(go.getX() - go.getVelX());
            go.setY(go.getY() - go.getVelY());
        });
        super.affect(player);
    }
}
