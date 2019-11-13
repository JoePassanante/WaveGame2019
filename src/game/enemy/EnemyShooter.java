package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Brandon Loehle 5/30/16
 */

public class EnemyShooter extends Enemy.Bouncing {
    private int timer;
    private double bulletSpeed;

    public EnemyShooter(GameLevel level) {
        super(level.spawnPoint(), 100, 75, level);
        setVelX(0);
        setVelY(0);
        timer = 20;
        bulletSpeed = 5 + getLevel().getRandom().nextInt(Math.min(20, getLevel().getNumber()));
    }

    @Override
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
        Point.Double player = getLevel().targetPoint();
        double
                diffX = player.getX() - getPosX(),
                diffY = player.getY() - getPosY(),
                distance = Math.max(Math.hypot(diffX, diffY), 1);

        getLevel().getEntities().add(new EnemyShooterBullet(
                new Point2D.Double(getPosX(), getPosY()),
                bulletSpeed * diffX / distance,
                bulletSpeed * diffY / distance,
                getLevel()
        ));
    }
}
