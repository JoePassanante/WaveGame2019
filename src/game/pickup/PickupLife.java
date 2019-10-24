package game.pickup;

import game.GameLevel;
import game.Player;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * 
 */

public class PickupLife extends Pickup.Active {
	public PickupLife(GameLevel l) {
        super(l);
	}

    @Override
    public void affect(Player player) {
        if(player.getHealth() <= 0) {
            // TODO: fill health bar incrementally, maybe extend PickupRegen?
            player.setHealth(player.getMaxHealth());
            if(!player.getLevel().getEntities().contains(player)) {
                player.getLevel().getEntities().add(player);
            }
        }
        super.affect(player);
    }
}
