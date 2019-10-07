package mainGame;

import java.awt.*;

/**
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/1/19
 */

public abstract class GameObject implements Animatable {
	protected double x, y, width, height, velX, velY, health;

	private Handler handler;
	public Handler getHandler() {
	    return handler;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setWidth(double w) {
        this.width = w;
    }
    public void setHeight(double h) {
        this.height = h;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public double getVelX() {
        return velX;
    }
    public void setVelX(int velX) {
        this.velX = velX;
    }
    public double getVelY() {
        return velY;
    }
    public void setVelY(int velY) {
        this.velY = velY;
    }
    public double getHealth() {
        return this.health;
    }

    private Image img;

    public GameObject(double x, double y, double w, double h, Handler hand) {
	    this.x = x;
	    this.y = y;
	    this.width = w;
	    this.height = h;
		this.handler = hand;

        img = handler.getTheme().get(getClass());
	}

    public void drawHitBox(Graphics g) {
	    Rectangle bounds = getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void render(Graphics g) {
        //drawHitBox(g);
        if(img != null) {
            Rectangle bounds = getBounds();
            g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
        }
    }

    public Rectangle getBounds() { // x and y are the center of a width by height hitbox
	    return new Rectangle(
            (int)(x-Math.abs(width/2)),
            (int)(y-Math.abs(height/2)),
            (int) width,
            (int) height
        );
    }
}
