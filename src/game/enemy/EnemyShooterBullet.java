package game.enemy;

import game.GameLevel;
import game.Trail;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Should be replaced with a general purpose bullet trail class.
 *
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/20/19
 */

public class EnemyShooterBullet extends Enemy.Disappearing { // should be combined with other bullet classes
    public EnemyShooterBullet(Point2D.Double loc, double velX, double velY, GameLevel level) {
        super(loc, 16, 16, level);
        setVelX(velX);
        setVelY(velY);
    }

    @Override
    public void tick() {
        getLevel().getNonentities().add(new Trail(this, Color.yellow, 127));
        super.tick();
    }

    @Override
    public void render(Graphics g) {
        super.render(g, Color.red);
    }
}
