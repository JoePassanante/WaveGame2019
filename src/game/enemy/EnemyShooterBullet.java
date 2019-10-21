package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;
import game.Trail;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterBullet extends GameObject.Disappearing {
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
        getLevel().add(new Trail(new Point.Double(getX(), getY()), Color.yellow, (int)getWidth()/4, (int)getHeight()/4, 255, getLevel()));

        super.tick();
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
