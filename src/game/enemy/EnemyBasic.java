package game.enemy;

import game.GameLevel;

/**
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 11/6/19
 */

public class EnemyBasic extends Enemy.Bouncing {
    public EnemyBasic(GameLevel level) {
        super(level.spawnPoint(), 125, 60, level);
        setVelX(10 * (level.getRandom().random() < .5 ? -1 : 1));
        setVelY(10 * (level.getRandom().random() < .5 ? -1 : 1));
    }

    // for dvd theme
    @Override
    public void tick() {
        super.tick();

        if(!getLevel().getBounds().contains(getBounds()))
            refer( getLevel().getTheme().get(getClass().getSimpleName() +
                getLevel().getRandom().new RandomDifferentElement<>(
                    "Blue", "Cyan", "Green", "Magenta", "Orange", "Pink", "Red", "Yellow").get()));
    }
}
