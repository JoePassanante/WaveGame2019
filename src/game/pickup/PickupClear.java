package game.pickup;

import game.GameLevel;
import game.Player;
import game.enemy.EnemyBurst;

public class PickupClear extends Pickup {
    public PickupClear(GameLevel l) {
        super(l);
    }

    @Override
    public void affect(Player player) {
        player.getLevel().getEntities().retainAll(player.getLevel().getPlayers());
        player.getLevel().getEntities().add(new EnemyBurst(getLevel()));
    }
}
