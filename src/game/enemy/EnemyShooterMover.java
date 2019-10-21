package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterMover extends GameObject.Bouncing {
	private int timer;
	private int bulletSpeed;

	public EnemyShooterMover(GameLevel level) {
		super(level.spawnPoint(), 100, 75, level);

        setVelX(10 * (level.getRandom().random() < .5 ? -1 : 1));
        setVelY(10 * (level.getRandom().random() < .5 ? -1 : 1));

		this.timer = 60;
        this.bulletSpeed = -20 + (int)(level.getRandom().random()*5);
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
			timer = 20;
		}
	}

	public void shoot() {
        getLevel().getPlayers().stream().filter(getLevel()::contains)
            .min((l,r) -> (int)(
                Math.hypot(getX()-l.getX(),getY()-l.getY()) -
                Math.hypot(getX()-r.getX(),getY()-r.getY()))
            )
            .ifPresent(player -> {
                double
                    diffX = getX() - player.getX(),
                    diffY = getY() - player.getY(),
                    distance = Math.hypot(diffX, diffY),
                    bulletVelX = diffX * bulletSpeed / distance,
                    bulletVelY = diffY * bulletSpeed / distance;

                    getLevel().add( new EnemyShooterBullet(
                        new Point2D.Double(getX(), getY()-10),
                        bulletVelX,
                        bulletVelY,
                        getLevel()
                    ));
            });
	}
}
