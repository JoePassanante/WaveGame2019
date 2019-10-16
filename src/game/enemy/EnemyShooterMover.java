package game.enemy;

import game.GameObject;
import game.Handler;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterMover extends GameObject.Bouncing {
	private int timer;
	private GameObject player;
	private int bulletSpeed;

	public EnemyShooterMover(Point.Double point, Handler handler) {
		super(point.x, point.y, 100, 75, handler);

        setVelX(10 * (getHandler().getRandom().random() < .5 ? -1 : 1));
        setVelY(10 * (getHandler().getRandom().random() < .5 ? -1 : 1));

		this.timer = 60;
        this.bulletSpeed = -20 + (int)(getHandler().getRandom().random()*5);

		for (int i = 0; i < handler.getPlayers().size(); i++) {
            player = handler.getPlayers().get(i);
		}
	}

	public void tick() {
        super.tick();
		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		timer -= 1;
		if (timer <= 0) {
			shoot();
			timer = 20;
		}
	}

	public void shoot() {
		if (player != null) {
            double
                diffX = getX() - player.getX(),
                diffY = getY() - player.getY(),
                distance = Math.hypot(diffX, diffY),
                bulletVelX = diffX * bulletSpeed / distance,
                bulletVelY = diffY * bulletSpeed / distance;

            getHandler().add(new EnemyShooterBullet(getX(), getY()-10, bulletVelX, bulletVelY, getHandler()));
		}
		else {
			System.err.println("player is null on shooter!");
			for (int i = 0; i < getHandler().getPlayers().size(); i++) {
                player = getHandler().getPlayers().get(i);
			}
		}
	}
}
