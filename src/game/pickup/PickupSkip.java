package game.pickup;

import game.GameLevel;
import game.Player;

public class PickupSkip extends Pickup {
    public PickupSkip(GameLevel l) {
        super(l, 0);
    }

    @Override
    public void affect(Player player) {
        player.getLevel().end();
        super.affect(player);
    }
}
