package game;

import game.waves.Handler;

import java.awt.*;

/**
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/1/19
 */

public class GameObject implements Animatable {
	private Handler handler;
	public Handler getHandler() {
	    return handler;
    }

    private double x, y, width, height, velX, velY, health;
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
    public void setVelX(double v) {
        this.velX = v;
    }
    public double getVelY() {
        return velY;
    }
    public void setVelY(double v) {
        this.velY = v;
    }
    public double getHealth() {
        return this.health;
    }
    public void setHealth(double h) {
        health = h;
    }

    private Image img;

    public GameObject(double x, double y, double w, double h, Handler hand) {
	    this.x = x;
	    this.y = y;
	    this.width = w;
	    this.height = h;
		this.handler = hand;

        img = handler.getTheme().get(this);
	}

    public void drawHitBox(Graphics g) {
	    Rectangle bounds = getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
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

    public static class Bouncing extends GameObject {
        public Bouncing(double x, double y, double w, double h, Handler hand) {
            super(x, y, w, h, hand);
        }

        @Override
        public void tick() {
            super.tick();
            setVelX(changeSign(getVelX(), Boolean.compare(getX() < 0, getHandler().getGameDimension().getWidth() < getX())));
            setVelY(changeSign(getVelY(), Boolean.compare(getY() < 0, getHandler().getGameDimension().getHeight() < getY())));
        }
    }

    public static class Disappearing extends GameObject {
        public Disappearing(double x, double y, double w, double h, Handler hand) {
            super(x, y, w, h, hand);
        }

        @Override
        public void tick() {
            super.tick();
            if(!new Rectangle(getHandler().getGameDimension()).contains(getBounds())) {
                getHandler().remove(this);
            }
        }
    }

    public static class Stopping extends GameObject {
        public Stopping(double x, double y, double w, double h, Handler hand) {
            super(x, y, w, h, hand);
        }

        @Override
        public void tick() {
            super.tick();
            setX(clamp(getX(), 0, getHandler().getGameDimension().getWidth()));
            setY(clamp(getY(), 0, getHandler().getGameDimension().getHeight()));
        }
    }

    /**
     *
     * @param number
     * @param lower
     * @param upper
     *
     * @return value of the new position (x or y)
     */
    public static double clamp(double number, double lower, double upper) {
        return Math.max(lower, Math.min(upper, number));
    }

    public static double changeSign(double magnitude, double sign) {
        if(sign == 0) {
            return magnitude;
        }
        return Math.copySign(magnitude, sign);
    }
}
