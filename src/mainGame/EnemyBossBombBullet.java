package mainGame;

import java.awt.*;
import java.util.Random;

/**
 * The bullets that the first boss shoots
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBossBombBullet extends GameObject {
	// instances
	Random r = new Random();
	private int max = 15;
	private int min = -15;

	// constructor
	// used to initialize the state of the object
	public EnemyBossBombBullet(double x, double y, ID id, Handler handler, int velX, int velY) {
		super(x, y, 16, 16, id, handler);
		this.velX = velX;
		this. velY = velY;
	}

	// methods
	// is called every frame, allows game objects to update themselves before being rendered.
	public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y >= getHandler().getGameDimension().getHeight() || this.y < 0 || this.x > getHandler().getGameDimension().getHeight()  || this.x < 0) {
            getHandler().removeObject(this);}

        getHandler().addObject(new Trail(x, y, ID.Trail, Color.PINK,(int)width,(int)height, 0.025, this.getHandler()));

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
