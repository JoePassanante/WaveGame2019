package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;
import game.Trail;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemySweep extends GameObject.Bouncing {
	private Color[] colors= {Color.red, Color.blue, Color.green, Color.cyan, Color.magenta, Color.orange, Color.yellow, Color.pink};

	private Color random = colors[getLevel().getRandom().nextInt(8)];
	
	public EnemySweep(GameLevel level) {
		super(level.spawnPoint(), 16, 16, level);
		setVelX(10 * (level.getRandom().random() < .5 ? -1 : 1));
		setVelY(5 * (level.getRandom().random() < .5 ? -1 : 1));
	}

    @Override
    public void collide(Player p) {
        p.damage(3);
    }

    public void tick() {
	    super.tick();
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
        getLevel().add(new Trail(new Point2D.Double(getX(), getY()), random, (int)getWidth(), (int)getHeight(), 255, getLevel()));
	}

	public void render(Graphics g) {
		g.setColor(random);
		Rectangle bounds = new Rectangle();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
