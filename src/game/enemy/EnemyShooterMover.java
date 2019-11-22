package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Brandon Loehle 5/30/16
 */

public class EnemyShooterMover extends Enemy.Bouncing {
    private int timer;
    private int bulletSpeed;

    public EnemyShooterMover(GameLevel level) {
        super(level.spawnPoint(), 100, 75, level);

        setVelX(10 * (level.getRandom().random() < .5 ? -1 : 1));
        setVelY(10 * (level.getRandom().random() < .5 ? -1 : 1));

        this.timer = 60;
        this.bulletSpeed = -20 + (int) (level.getRandom().random() * 5);
    }

    @Override
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
        Point.Double player = getLevel().targetPoint();

        double
                diffX = getPosX() - player.getX(),
                diffY = getPosY() - player.getY(),
                distance = Math.hypot(diffX, diffY),
                bulletVelX = diffX * bulletSpeed / distance,
                bulletVelY = diffY * bulletSpeed / distance;

        getLevel().getEntities().add(new EnemyShooterBullet(
                new Point2D.Double(getPosX(), getPosY() - 10),
                bulletVelX,
                bulletVelY,
                getLevel()
        ));
    }
}
