package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;
import game.Trail;

import java.awt.*;

/**
 * The bullets that EnemyBoss shoots
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBossBullet extends GameEntity.Disappearing {
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
        getLevel().getEntities().add( new Trail(this, Color.red, 255));
	}
	
    @Override
	public void render(Graphics g) {
	    super.render(g, Color.red);
	}
}
