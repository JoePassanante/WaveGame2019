package game.pickup;

import game.GameLevel;
import game.Player;
import game.enemy.Enemy;

/**
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 */

public class PickupFreeze extends Pickup {
    public PickupFreeze(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        player.getLevel().getEntities().stream().filter(Enemy.class::isInstance).forEach(go -> {
            go.setPosX(go.getPosX() - go.getVelX());
            go.setPosY(go.getPosY() - go.getVelY());
        });
        super.affect(player);
    }
}
