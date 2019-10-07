package mainGame;

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

public class MenuFireworks extends GameObject {
	private Random r;
	private int max = 5;
	private int min = -5;
	private Color color;
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
	public MenuFireworks(double x, double y, int sizeX, int sizeY, double velX, double velY, Color color,
			Handler handler) {
		super(x, y, sizeX, sizeY, handler);
		this.velX = velX;
		this.velY = velY;
		r = new Random();
		this.color = color;

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
		this.x += velX;
		this.y += velY;
		if (this.y <= 100) {// once it gets this high
			for (int i = 0; i < getHandler().size(); i++) {
				GameObject tempObject = getHandler().get(i);
				if (tempObject instanceof MenuFireworks) {// find the firework
					sparks(tempObject);// create sparks
                    getHandler().removeObject(tempObject);// delete big circle
				}
			}
		}
	}
	/**
	 * This function is what shows the "sparks," the smaller circles
	 * @param tempObject
	 */
	public void sparks(GameObject tempObject) {
		for (int ii = 0; ii < 3; ii++) {
		    for(int i=-5; i<=5; i++) {
                getHandler().addObject(new MenuFireworks(this.x, this.y, 20, 20, (r.nextInt((max - min) + 1) + min), i,
                        this.color, getHandler()));
            }
		}
	}
}
