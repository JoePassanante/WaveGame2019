package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 * A warning that is displayed before EnemyBurst comes across the screen
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyBurstWarning extends GameObject {
	private int timer;
	private Color color;

    @Override
    public void collide(Player p) {

    }

    public EnemyBurstWarning(Point2D.Double loc, double width, double height, GameLevel level) {
		super(loc, width, height, level);
		timer = 10;
		color = Color.red;
	}

	@Override
	public void tick() {
	    super.tick();
        timer += 1;
        if (this.timer >= 60) {
            getLevel().remove(this);
        }
	}

	@Override
	public void render(Graphics g) {
        if ((timer/5) % 2 == 0) {
            this.color = Color.black;
        }
        else {
            this.color = Color.red;
        }

        g.setColor(this.color);
		g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}
}
