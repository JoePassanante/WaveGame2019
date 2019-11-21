package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupSpeed extends Pickup.Active {
    public PickupSpeed(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        setHealth(player.getHealth());
        player.setVelX(1.25 * player.getVelX());
        player.setVelY(1.25 * player.getVelY());
    }
}
