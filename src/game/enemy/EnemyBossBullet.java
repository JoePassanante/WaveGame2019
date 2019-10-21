package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;
import game.Trail;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * The bullets that EnemyBoss shoots
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBossBullet extends GameObject.Disappearing {
	public EnemyBossBullet(Point.Double point, GameLevel level) {
		super(point, 16, 16, level);
		setVelX((level.getRandom().random()*2-1)*15);
		setVelY(30);
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
	    super.tick();
        getLevel().add(new Trail(new Point2D.Double(getX(), getY()), Color.red,(int)getWidth(),(int)getHeight(), 255, getLevel()));
	}
	
    @Override
	public void render(Graphics g) {
		g.setColor(Color.red);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
