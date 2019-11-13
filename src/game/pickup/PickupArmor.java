package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupArmor extends Pickup.Active {
    public PickupArmor(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        player.setArmor(player.getArmor() + (1 - player.getArmor()) / 8);
        super.affect(player);
    }
}
