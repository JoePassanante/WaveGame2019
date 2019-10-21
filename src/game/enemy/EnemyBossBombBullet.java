package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;
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
	public EnemyBossBombBullet(Point.Double point, GameLevel level, int velX, int velY) {
		super(point, 16, 16, level);
		setVelX(velX);
		setVelY(velY);
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
        getLevel().add(new Trail(new Point.Double(getX(), getY()), Color.PINK,(int)getWidth(),(int)getHeight(), 255, this.getLevel()));
	}
	
    @Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
