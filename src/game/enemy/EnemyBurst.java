package game.enemy;

import game.GameObject;
import game.waves.Handler;

import java.awt.*;

public class EnemyBurst extends GameObject.Disappearing {
	private Point start;
	private EnemyBurstWarning warning;

	public EnemyBurst(Point.Double point, Handler handler) {
		super(point.x, point.y, 150, 150, handler);

		double r = Math.random();
		if(r < .25) {
            setX(-getWidth());
            setVelX(30);
            warning = new EnemyBurstWarning(0, 0, 25, (int)handler.getGameDimension().getHeight(), handler);
        }
        else if(r < .50) {
            setX(handler.getGameDimension().width + getWidth());
            setVelX(-30);
            warning = new EnemyBurstWarning(handler.getGameDimension().getWidth() - 25, 0, 25, (int)handler.getGameDimension().getHeight(), handler);
        }
        else if(r < .75) {
            setY(-getHeight());
            setVelY(30);
            warning = new EnemyBurstWarning(0, 0, (int)handler.getGameDimension().getWidth(), 25, handler);
        }
        else {
            setY(handler.getGameDimension().height + getHeight());
            setVelY(-30);
            warning = new EnemyBurstWarning(0, handler.getGameDimension().getHeight() - 25, (int)handler.getGameDimension().getWidth(), 25, handler);
        }

        start = getBounds().getLocation();
	}

	private boolean warned = false;
	public void tick() {
        if(!getHandler().contains(warning)) {
            if(!warned) {
                getHandler().add(warning);
                warned = true;
            }
            else {
                super.tick();
            }
        }

        Dimension dim = getHandler().getGameDimension();
        if(!getHandler().contains(this) && start.distance(getBounds().getLocation()) < dim.getWidth() + dim.getHeight()) {
            getHandler().add(this);
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.orange, this.size, this.size, 0.025, this.handler));
	}
}
