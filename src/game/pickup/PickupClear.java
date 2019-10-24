package game.pickup;

import game.GameLevel;
import game.Player;
import game.enemy.EnemyBurst;

public class PickupClear extends PickupSkip {
    public PickupClear(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        super.affect(player);
        player.getLevel().getEntities().add(new EnemyBurst(getLevel()));
    }
}
