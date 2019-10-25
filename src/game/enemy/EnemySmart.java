package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySmart extends GameEntity.Stopping {
    public EnemySmart(GameLevel level) {
		super(level.spawnPoint(), 150,75, level);
	}

    @Override
    public void collide(Player player) {
        player.damage(2);
    }

    public void tick() {
        super.tick();

        Point.Double player = getLevel().targetPoint();

        double
            diffX = player.getX() - getPosX(),
            diffY = player.getY() - getPosY(),
            distance = Math.hypot(diffX, diffY),
            sides = Math.hypot(getLevel().getDimension().width, getLevel().getDimension().height) / 4,
            boost = 5.0 / Math.exp(distance / sides) + 1.0;

        setVelX(boost * diffX / distance);
        setVelY(boost * diffY / distance);
        //handler.addObject(new Trail(x, y, ID.Trail, Color.green, 16, 16, 0.025, this.handler));
	}
}
