package game.enemy;

import game.GameObject;
import game.waves.Handler;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A warning that is displayed before EnemyBurst comes across the screen
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyBurstWarning extends GameObject {
	private int width;
	private int height;
	private int timer;
	private Color color;
	private int hasFlashed;

	public EnemyBurstWarning(double x, double y, int width, int height, Handler handler) {
		super(x, y, 16, 16, handler);
		this.width = width;
		this.height = height;
		this.timer = 10;
		this.color = Color.red;
		this.hasFlashed = 0;

	}

	@Override
	public void tick() {
	    super.tick();
        if (this.hasFlashed == 5) {
            getHandler().remove(this);
        }
	}

	@Override
	public void render(Graphics g) {
        timer -= 1;
        if (timer == 5) {
            this.color = Color.black;
        } else if (timer == 0) {
            this.color = Color.red;
            this.hasFlashed += 1;
            timer = 10;
        }
        g.setColor(this.color);
		g.fillRect((int) getX(), (int) getY(), this.width, this.height);
	}
}
