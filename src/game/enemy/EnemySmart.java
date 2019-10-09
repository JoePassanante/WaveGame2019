package game.enemy;

import game.GameObject;
import game.waves.Handler;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySmart extends GameObject {

    public EnemySmart(Point.Double point, Handler handler) {
		super(point.x, point.y, 150, 75, handler);
	}

	public void tick() {
        super.tick();

        GameObject player = null;
        for (int i = 0; i < getHandler().getPlayers().size(); i++) {
            player = getHandler().getPlayers().get(i);
        }
        //handler.addObject(new Trail(x, y, ID.Trail, Color.green, 16, 16, 0.025, this.handler));
        Dimension bounds = getHandler().getGameDimension();
        double
            diffX = player.getX() - getX(),
            diffY = player.getY() - getY(),
            distance = Math.hypot(diffX, diffY),
            sides = Math.hypot(bounds.width,bounds.height)/4,
            boost = 5.0 / Math.exp(distance/sides) + 1.0;

		setVelX(boost * diffX / distance);
		setVelY(boost * diffY / distance);
	}
}
