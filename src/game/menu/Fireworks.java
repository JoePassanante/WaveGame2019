package game.menu;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * The graphics behind the menu that resemble fireworks
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/19/19
 */

public class Fireworks extends GameObject.Disappearing {
	private Color color;
	private boolean spark;

	public Fireworks(Point.Double p, GameLevel l, Color c) {
	    this(p, 100, 100, l, l.getRandom().random() - .5, -4, c, true);
	}

    public Fireworks(Point.Double p, int w, int h, GameLevel l, double vx, double vy, Color c, boolean s) {
        super(p, w, h, l);
        setVelX(vx);
        setVelY(vy);
        color = c;
        spark = s;
    }

	public void render(Graphics g) {
		g.setColor(color);
		Rectangle bounds = getBounds();
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}

    @Override
    public void collide(Player p) {

    }

	public void tick() {
	    super.tick();
		if (getY() <= 200 && spark) { // once it gets this high
		    getLevel().remove(this);
            for(int t = 0; t < 10; t += 1) {
                for (int s = 0; s < 3; s += 1) {
                    double
                        theta = 2 * Math.PI * t / 10 + getLevel().getRandom().random(),
                        speed = theta*getLevel().getRandom().random() + s*2 + 2;
                    getLevel().add(new Fireworks(
                        new Point2D.Double(getX(), getY()),
                        s + 25, s + 25,
                        getLevel(),
                        speed * Math.cos(theta), speed * Math.sin(theta),
                        color,
                        false
                    ));
                }
            }
        }
	}
}
