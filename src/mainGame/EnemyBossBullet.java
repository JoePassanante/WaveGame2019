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

public class EnemyBossBullet extends GameObject {
	private Random r = new Random();
	private int max = 15;
	private int min = -15;

	public EnemyBossBullet(double x, double y, Handler handler) {
		super(x, y, 16, 16, handler);
		velX = (r.nextInt((max - min) + 1) + min);// OFFICIAL WAY TO GET A RANGE FOR randInt()
		velY = 30;
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y >= getHandler().getGameDimension().getHeight())
            getHandler().remove(this);

        getHandler().add(new Trail(x, y, Color.red,(int)width,(int)height, 0.025, getHandler()));
	}
	
    @Override
	public void render(Graphics g) {
		g.setColor(Color.red);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
