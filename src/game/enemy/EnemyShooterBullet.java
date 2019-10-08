package game.enemy;

import game.GameObject;
import game.waves.Handler;
import game.Trail;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterBullet extends GameObject.Disappearing {
	public EnemyShooterBullet(double x, double y, double velX, double velY, Handler handler) {
		super(x, y, 16, 16, handler);
		setVelX(velX);
		setVelY(velY);
	}

	public void tick() {
	    super.tick();

		// if (this.y <= 0 || this.y >= Game.HEIGHT - 40) velY *= -1;
		// if (this.x <= 0 || this.x >= Game.WIDTH - 16) velX *= -1;

        getHandler().add(new Trail(getX(), getY(), Color.yellow, (int)getWidth()/4, (int)getHeight()/4, 0.025, getHandler()));
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
