package game.pickup;

import game.GameLevel;
import game.Player;

/**
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 */

public class PickupFreeze extends Pickup {
    public PickupFreeze(GameLevel l) {
        super(l, 900);
    }

    @Override
    public void affect(Player player) {
        player.getLevel().getEntities().stream().filter(go -> !(go instanceof Player)).forEach(go -> {
            go.setPosX(go.getPosX() - go.getVelX());
            go.setPosY(go.getPosY() - go.getVelY());
        });
        super.affect(player);
    }
}
