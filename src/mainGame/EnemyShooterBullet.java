package mainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterBullet extends GameObject {
	public EnemyShooterBullet(double x, double y, double velX, double velY, ID id, Handler handler) {
		super(x, y, 16, 16, id, handler);
		this.velX = velX;
		this.velY = velY;
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		// if (this.y <= 0 || this.y >= Game.HEIGHT - 40) velY *= -1;
		// if (this.x <= 0 || this.x >= Game.WIDTH - 16) velX *= -1;

        getHandler().addObject(new Trail(x, y, ID.Trail, Color.yellow, 4, 4, 0.025, getHandler()));

		removeBullets();
	}

	public void removeBullets() {
		for (int i = 0; i < getHandler().object.size(); i++) {
			GameObject tempObject = getHandler().object.get(i);
			if (tempObject.getId() == ID.EnemyShooterBullet) {
				//check for removal
				if ((tempObject.getX() >= getHandler().getGameDimension().getWidth() || tempObject.getY() >= getHandler().getGameDimension().getHeight()) ||
					(tempObject.getX() < -100 || tempObject.getY() < -100)){
                    getHandler().removeObject(tempObject);
				}
			}

		}

	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect((int) x, (int) y, 4, 4);

	}
}
