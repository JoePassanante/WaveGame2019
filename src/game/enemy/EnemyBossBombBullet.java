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

public class EnemyBossBombBullet extends GameObject.Disappearing {
	// instances
	Random r = new Random();
	private int max = 15;
	private int min = -15;

	// constructor
	// used to initialize the state of the object
	public EnemyBossBombBullet(double x, double y, Handler handler, int velX, int velY) {
		super(x, y, 16, 16, handler);
		setVelX(velX);
		setVelY(velY);
	}

	// methods
	// is called every frame, allows game objects to update themselves before being rendered.
	public void tick() {
        getHandler().add(new Trail(getX(), getY(), Color.PINK,(int)getWidth(),(int)getHeight(), 0.025, this.getHandler()));
	}
	
	// is the abstract base class for all graphics contexts that allow an application to draw 
	// onto components that are realized on various devices, as well as onto off-screen images
    @Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
