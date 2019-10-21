package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterSharp extends GameObject.Bouncing {
	private int timer;

	public EnemyShooterSharp(GameLevel level) {
		super(level.spawnPoint(), 200, 150, level);
		setVelX(0);
		setVelY(0);
		this.timer = 60;
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
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
        getLevel().getPlayers().stream().filter(getLevel()::contains)
            .min((l,r) -> (int)(
                Math.hypot(getX()-l.getX(),getY()-l.getY()) -
                Math.hypot(getX()-r.getX(),getY()-r.getY()))
            ).ifPresent(player -> {
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

            getLevel().add(new EnemyShooterBullet(new Point2D.Double(getX() + 50, getY() - 10), shootX, shootY, getLevel()));
            getLevel().add(new EnemyShooterBullet(new Point2D.Double(getX() - 50, getY() - 10), shootX, shootY, getLevel()));
        });
    }
}
