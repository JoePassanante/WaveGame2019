package mainGame;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooter extends GameObject {
	private int timer;
	private GameObject player;
	private int bulletSpeed;

    public EnemyShooter(Point.Double point, Handler handler) {
		super(point.x, point.y, 100, 75, handler);
		this.velX = 0;
		this.velY = 0;
		this.timer = 60;
		this.bulletSpeed = (int)(Math.random()*30);

		for (int i = 0; i < handler.size(); i++) {
			if (handler.get(i) instanceof Player)
				player = handler.get(i);
		}
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y <= 0) {
            velY = Math.abs(velY);
        }
        if (getHandler().getGameDimension().getHeight() - 40 <= this.y) {
            velY = -Math.abs(velY);
        }
		if (this.x <= 0) {
            velX = Math.abs(velX);
        }
        if(getHandler().getGameDimension().getWidth() - 16 <= this.x) {
            velX = -Math.abs(velX);
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer--;
		
		
		if (timer <= 0) {
			shoot();
			
			timer = 20;
		}
	}

	public void shoot() {
		if (player != null) {
            double
                diffX = player.getX() - x,
                diffY = player.getY() - y,
                scale = bulletSpeed / Math.hypot(diffX, diffY);

            getHandler().add( new EnemyShooterBullet(
                this.x,
                this.y-10,
                diffX * scale,
                diffY * scale,
                getHandler()
            ) );
		}
	}

	public void updateEnemy() {
		this.width*= .95;
		this.height*=.95;

		if (width <= 1 || height <= 1) {
            getHandler().remove(this);
		}
	}
}
