package game.enemy;

import game.GameLevel;
import game.Trail;

import java.awt.*;

/**
 * The bullets that the first boss shoots
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 */

public class EnemyBossBombBullet extends Enemy.Disappearing {
    public EnemyBossBombBullet(Point.Double point, GameLevel level, double velX, double velY) {
        super(point, 16, 16, level);
        setVelX(velX);
        setVelY(velY);
    }

    @Override
    public void tick() {
        super.tick();
        getLevel().getNonentities().add(new Trail(this, Color.pink, 255));
    }

    @Override
    public void render(Graphics g) {
        super.render(g, Color.green);
    }
}
