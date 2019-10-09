package game.enemy;

import game.GameObject;
import game.waves.Handler;
import game.Trail;

import java.awt.*;

/**
 * The bullets that the first boss shoots
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBossBombBullet extends GameObject.Disappearing {
	public EnemyBossBombBullet(double x, double y, Handler handler, int velX, int velY) {
		super(x, y, 16, 16, handler);
		setVelX(velX);
		setVelY(velY);
	}

	public void tick() {
        getHandler().add(new Trail(getX(), getY(), Color.PINK,(int)getWidth(),(int)getHeight(), 0.025, this.getHandler()));
	}
	
    @Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
