package mainGame;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySmart extends GameObject {
	private GameObject player;

    public EnemySmart(Point.Double point, Handler handler) {
		super(point.x, point.y, 150, 75, handler);

		for (int i = 0; i < handler.size(); i++) {
			if (handler.get(i) instanceof Player)
				player = handler.get(i);
		}

	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		//handler.addObject(new Trail(x, y, ID.Trail, Color.green, 16, 16, 0.025, this.handler));
        Dimension bounds = getHandler().getGameDimension();
        double
            diffX = player.getX() - x,
            diffY = player.getY() - y,
            distance = Math.hypot(diffX, diffY),
            sides = Math.hypot(bounds.width,bounds.height)/4,
            boost = 5.0 / Math.exp(distance/sides) + 1.0;

		velX = boost * diffX / distance;
		velY = boost * diffY / distance;
	}
}
