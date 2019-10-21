package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupSpeed extends Pickup.Active {
    public PickupSpeed(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        player.setVelX(1.2*player.getVelX());
        player.setVelY(1.2*player.getVelY());
        // super.affect(player); // pickup is permanent
    }
}
