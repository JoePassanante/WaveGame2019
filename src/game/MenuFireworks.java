package game;

import game.waves.Handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 * The graphics behind the menu that resemble fireworks
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 *
 */

public class MenuFireworks extends GameObject.Disappearing {
	private Random r;
	private int max = 5;
	private int min = -5;
	private Color color;
	private boolean spark;
	/**
	 * 
	 * @param x Position of Fire Work on X-Axis (double).
	 * @param y Position of Fire Work on Y-Axis (double).
	 * @param sizeX Size of Fire Work X-Axis.
	 * @param sizeY Size of Fire Work Y-Axis.
	 * @param velX Velocity X.
	 * @param velY Velocity Y.
	 * @param color Fill Color. 
	 * @param handler handler object.
	 */
	public MenuFireworks(double x, double y, int sizeX, int sizeY, double velX, double velY, Color color, Handler handler, boolean s) {
		super(x, y, sizeX, sizeY, handler);
		setVelX(velX);
		setVelY(velY);
		r = new Random();
		this.color = color;
		this.spark = s;
	}
	//render the circle object
	public void render(Graphics g) {
		g.setColor(this.color);
		Rectangle bounds = getBounds();
		g.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	/**
	 * In the tick function it checks where the circle fire work is on
	 * the screen, and at a certain height, create smaller objects
	 */
	public void tick() {
	    super.tick();
	    if(!getHandler().contains(this) && spark) {
	        getHandler().add(this);
        }
		if (getY() <= 100 && spark) {// once it gets this high
            sparks();// create sparks
            getHandler().remove(this);
		}
	}
	/**
	 * This function is what shows the "sparks," the smaller circles
	 */
	public void sparks() {
		for (int ii = 0; ii < 3; ii++) {
		    for(int i=-5; i<=5; i++) {
                getHandler().add(new MenuFireworks(getX(), getY(), 20, 20, (r.nextInt((max - min) + 1) + min), i,
                    this.color, getHandler(), false));
            }
		}
	}
}
