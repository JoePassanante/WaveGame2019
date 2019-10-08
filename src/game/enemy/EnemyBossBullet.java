package game.enemy;

import game.GameObject;
import game.waves.Handler;
import game.Trail;

import java.awt.*;
import java.util.Random;

/**
 * The bullets that the first boss shoots
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBossBullet extends GameObject.Disappearing {
	public EnemyBossBullet(double x, double y, Handler handler) {
		super(x, y, 16, 16, handler);
		setVelX((Math.random()*2-1)*15);
		setVelY(30);
	}

	public void tick() {
	    super.tick();
        getHandler().add(new Trail(getX(), getY(), Color.red,(int)getWidth(),(int)getHeight(), 0.025, getHandler()));
	}
	
    @Override
	public void render(Graphics g) {
		g.setColor(Color.red);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
