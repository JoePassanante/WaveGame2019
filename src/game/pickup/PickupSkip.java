package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupSkip extends Pickup {
    public PickupSkip(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        player.getLevel().getEntities().removeIf(go -> !(go instanceof Player));
        super.affect(player);
    }
}
