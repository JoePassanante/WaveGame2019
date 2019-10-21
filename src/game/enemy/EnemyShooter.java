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

public class EnemyShooter extends GameObject.Bouncing {
	private int timer;
	private double bulletSpeed;

    public EnemyShooter(GameLevel level) {
		super(level.spawnPoint(), 100, 75, level);
		setVelX(0);
		setVelY(0);
		timer = 20;
		bulletSpeed = 5;
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer -= 1;
		
		if (timer <= 0) {
			shoot();
			
			timer = 20;
		}

        super.tick();
	}

	public void shoot() {
        getLevel().getPlayers().stream().filter(getLevel()::contains)
            .min((l,r) -> (int)(
                Math.hypot(getX()-l.getX(),getY()-l.getY()) -
                Math.hypot(getX()-r.getX(),getY()-r.getY()))
            ).ifPresent( player -> {
                double
                    diffX = player.getX() - getX(),
                    diffY = player.getY() - getY(),
                    distance = Math.max( Math.hypot(diffX, diffY), 1);

                getLevel().add( new EnemyShooterBullet(
                    new Point2D.Double(getX(), getY()),
                    bulletSpeed * diffX / distance,
                    bulletSpeed * diffY / distance,
                    getLevel()
                ));
            });
	}
}
