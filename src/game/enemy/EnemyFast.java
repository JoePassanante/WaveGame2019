package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyFast extends GameEntity.Bouncing {
	public EnemyFast(GameLevel level) {
		super(level.spawnPoint(), 32, 64, level);
		setVelX(2*level.getRandom().random() - 1);
		setVelY(Math.copySign(12, level.getRandom().random()));
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    @Override
    public void render(Graphics gfx) {
	    Graphics2D g = (Graphics2D)gfx;
	    AffineTransform old = g.getTransform();
	    if(0 < getVelY()) {
	        g.transform(AffineTransform.getRotateInstance(Math.PI, getWidth()/2, getHeight()/2));
        }
	    super.render(g);
	    g.setTransform(old);
    }
 }
