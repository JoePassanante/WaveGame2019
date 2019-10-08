package mainGame;

import java.awt.Point;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterSharp extends GameObject {
	private int timer;
	private GameObject player;

	public EnemyShooterSharp(Point.Double point, Handler handler) {
		super(point.x, point.y, 200, 150, handler);
		this.velX = 0;
		this.velY = 0;
		this.timer = 60;

		for (int i = 0; i < handler.size(); i++) {
			if (handler.get(i) instanceof Player) {
                player = handler.get(i);
            }
		}
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

        if (this.y <= 0) {
            velY = Math.abs(velY);
        }
        if (this.y >= getHandler().getGameDimension().getHeight() - 40) {
            velY = -Math.abs(velY);
        }
        if (this.x <= 0) {
            velX = Math.abs(velX);
        }
        if (this.x >= getHandler().getGameDimension().getWidth() - 16) {
            velX = -Math.abs(velX);
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer -= 1;
		
		if (timer <= 0) {
			shoot();
			timer = 30;
		}

	}

	public void shoot() {
		if (player != null) {

            double
                diffX = player.getX() - this.x,
                diffY = player.getY() - this.y,
                diff= Math.hypot(diffX, diffY),
                aimX = diffX + player.velX * 30,
                aimY = diffY + player.velY * 30,
                aim = Math.hypot(aimX, aimY),
                shootX = Math.max(diffX/diff,aimX/aim) * 10,
                shootY = Math.max(diffY/diff,aimY/aim) * 10;

            //supposed to shoot where they're going, not 100% accurate in terms of time yet though

            getHandler().add(new EnemyShooterBullet(this.x+50, this.y-10, shootX, shootY, getHandler()));
            getHandler().add(new EnemyShooterBullet(this.x-50, this.y-10, shootX, shootY, getHandler()));
        }
        else {
			System.err.println("player is null on shooter!");//bpm
			for (int i = 0; i < getHandler().size(); i++) {
				if (getHandler().get(i) instanceof Player)
					player = getHandler().get(i);
			}
		}
	}

	public void updateEnemy() {
		width*= .95;
		height*=.95;

		if (width <= 1 || height <= 1) {
            getHandler().remove(this);
		}
	}
}
