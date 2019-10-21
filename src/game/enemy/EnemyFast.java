package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyFast extends GameObject.Bouncing {
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
    public void render(Graphics g) {
        g.drawImage(
            getLevel().getTheme().get(this),
            (int) getX(),
            (int) getY() + (0 < getVelY() ? 64 : 0),
            (int) getWidth(),
            (int) getHeight() * (0 < getVelY() ? -1 : 1),
            null
        );
    }
 }
