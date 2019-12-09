package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A type of enemy in the game
 *
 * @author Brandon Loehle 5/30/16
 */

public class EnemyFast extends Enemy.Bouncing { // a faster basic enemy
    public EnemyFast(GameLevel level) {
        super(level.spawnPoint(), 32, 96, level);
        setVelX(2 * level.getRandom().random() - 1);
        setVelY(Math.copySign(12, level.getRandom().random()));
    }

    @Override
    public void render(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;
        AffineTransform old = g.getTransform();
        if (0 < getVelY()) {
            g.setTransform(AffineTransform.getRotateInstance(Math.PI, getPosX(), getPosY()));
        }
        super.render(g);
        g.setTransform(old);
    }
}
