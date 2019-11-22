package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A type of enemy in the game
 *
 * @author Brandon Loehle 5/30/16
 */

public class EnemyShooterSharp extends Enemy.Bouncing {
    private int timer;

    public EnemyShooterSharp(GameLevel level) {
        super(level.spawnPoint(), 200, 150, level);
        setVelX(0);
        setVelY(0);
        timer = 60;
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
        Point.Double player = getLevel().targetPoint();
        double
                diffX = player.getX() - getPosX(),
                diffY = player.getY() - getPosY(),
                diff = Math.hypot(diffX, diffY);

        //supposed to shoot where they're going, not 100% accurate in terms of time yet though

        getLevel().getEntities().add(new EnemyShooterBullet(new Point2D.Double(
                getPosX() + 50, getPosY() - 10), 10 * diffX / diff, 10 * diffY / diff, getLevel()
        ));
        getLevel().getEntities().add(new EnemyShooterBullet(new Point2D.Double(
                getPosX() - 50, getPosY() - 10), 10 * diffX / diff, 10 * diffY / diff, getLevel()
        ));
    }
}
