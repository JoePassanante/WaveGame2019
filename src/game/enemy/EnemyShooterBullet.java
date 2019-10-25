package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;
import game.Trail;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Should be replaced with a general purpose bullet trail class.
 *
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/20/19
 *
 */

public class EnemyShooterBullet extends GameEntity.Disappearing {
    @Override
    public void collide(Player p) {
        p.damage(1);
    }

    public EnemyShooterBullet(Point2D.Double loc, double velX, double velY, GameLevel level) {
		super(loc, 16, 16, level);
		setVelX(velX);
		setVelY(velY);
	}

	public void tick() {
        getLevel().getEntities().add(new Trail(this, Color.yellow, 255));
        super.tick();
	}

	public void render(Graphics g) {
        super.render(g, Color.red);
	}
}
