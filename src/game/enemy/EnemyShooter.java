package game.enemy;

import game.GameObject;
import game.Handler;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooter extends GameObject.Bouncing {
	private int timer;
	private double bulletSpeed;

    public EnemyShooter(Point.Double point, Handler handler) {
		super(point.x, point.y, 100, 75, handler);
		setVelX(0);
		setVelY(0);
		this.timer = 60;
		this.bulletSpeed = 5 + getHandler().getRandom().random()*25;
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
        GameObject player = getHandler().getPlayers().stream()
            .min((l,r) -> (int)(
                Math.hypot(getX()-l.getX(),getY()-l.getY()) -
                Math.hypot(getX()-r.getX(),getY()-r.getY()))
            ).orElse(getHandler().getRandomDifferentPlayer());

        double
            diffX = player.getX() - getX(),
            diffY = player.getY() - getY(),
            scale = bulletSpeed / Math.hypot(diffX, diffY);

        getHandler().add( new EnemyShooterBullet(
            getX(),
            getY()-10,
            diffX * scale,
            diffY * scale,
            getHandler()
        ) );
	}
}
