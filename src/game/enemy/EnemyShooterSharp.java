package game.enemy;

import game.GameObject;
import game.waves.Handler;
import game.Player;

import java.awt.Point;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterSharp extends GameObject.Bouncing {
	private int timer;

	public EnemyShooterSharp(Point.Double point, Handler handler) {
		super(point.x, point.y, 200, 150, handler);
		setVelX(0);
		setVelY(0);
		this.timer = 60;
	}

	public void tick() {
        super.tick();

		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer -= 1;
		
		if (timer <= 0) {
			shoot();
			timer = 30;
		}

	}

	public void shoot() {
        GameObject player = null;
        for (int i = 0; i < getHandler().getPlayers().size(); i++) {
            player = getHandler().getPlayers().get(i);
        }

        double
            diffX = player.getX() - getX(),
            diffY = player.getY() - getY(),
            diff = Math.hypot(diffX, diffY),
            aimX = diffX + player.getVelX() * 30,
            aimY = diffY + player.getVelY() * 30,
            aim = Math.hypot(aimX, aimY),
            shootX = Math.max(diffX / diff, aimX / aim) * 10,
            shootY = Math.max(diffY / diff, aimY / aim) * 10;

        //supposed to shoot where they're going, not 100% accurate in terms of time yet though

        getHandler().add(new EnemyShooterBullet(getX() + 50, getY() - 10, shootX, shootY, getHandler()));
        getHandler().add(new EnemyShooterBullet(getX() - 50, getY() - 10, shootX, shootY, getHandler()));
    }
}
