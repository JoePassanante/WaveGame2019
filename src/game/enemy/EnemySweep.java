package game.enemy;

import game.GameObject;
import game.Handler;
import game.Trail;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySweep extends GameObject.Bouncing {
	private Color[] colors= {Color.red, Color.blue, Color.green, Color.cyan, Color.magenta, Color.orange, Color.yellow, Color.pink};

	private Color random = colors[getHandler().getRandom().nextInt(8)];
	
	public EnemySweep(Point.Double point, Handler handler) {
		super(point.x, point.y, 16, 16, handler);
		setVelX(10 * (getHandler().getRandom().random() < .5 ? -1 : 1));
		setVelY(5 * (getHandler().getRandom().random() < .5 ? -1 : 1));
	}

	public void tick() {
	    super.tick();
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
        getHandler().add(new Trail(getX(), getY(), random, (int)getWidth(), (int)getHeight(), 0.025, getHandler()));
	}

	public void render(Graphics g) {
		g.setColor(random);
		Rectangle bounds = new Rectangle();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
