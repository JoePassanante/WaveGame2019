package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupRegen extends Pickup.Active {
    public PickupRegen(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        if (player.getHealth() < player.getMaxHealth()) {
            player.setHealth(player.getHealth() + .25);
        }
    }
}
