package game;

import java.awt.*;

/**
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/1/19
 */

public abstract class GameObject implements Animatable {
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
    public void setVelX(double v) {
        this.velX = v;
    }
    public void setVelY(double v) {
        this.velY = v;
    }
    public double getVelX() {
        return velX;
    }
    public double getVelY() {
        return velY;
    }
    public void setHealth(double h) {
        health = h;
    }
    public double getHealth() {
        return this.health;
    }

    public abstract void collide(Player p); // called when a GameObject touches a Player

    private GameLevel level;
    public GameLevel getLevel() {
        return level;
    }
    public void setLevel(GameLevel l) {
        level = l;
    }

    private Image img;

    public GameObject(Point.Double loc, double w, double h, GameLevel l) {
	    x = loc.getX();
	    y = loc.getY();
	    width = w;
	    height = h;
	    level = l;
        img = level.getTheme().get(this);
	}

    @Override
    public void tick() {
        x += velX;
        y += velY;
        if(getHealth() < 0) {
            getLevel().remove(this);
        }
    }

    public void render(Graphics g) {
        Rectangle bounds = getBounds();
        if(img != null) {
            g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, null);
        }
        else {
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public Rectangle getBounds() { // x and y are the center of a width by height hitbox
	    return new Rectangle(
            (int)(x - width/2),
            (int)(y - height/2),
            (int) width,
            (int) height
        );
    }

    public static abstract class Stopping extends GameObject {
        public Stopping(Point.Double p, double w, double h, GameLevel l) {
            super(p, w, h, l);
        }

        @Override
        public void tick() {
            super.tick();
            setX(clamp(getX(), 0, getLevel().getDimension().getWidth()));
            setY(clamp(getY(), 0, getLevel().getDimension().getHeight()));
        }
    }

    public static abstract class Bouncing extends GameObject {
        public Bouncing(Point.Double p, double w, double h, GameLevel l) {
            super(p, w, h, l);
        }

        @Override
        public void tick() {
            setVelX(changeSign(getVelX(), Boolean.compare(getX() < 0, getLevel().getDimension().getWidth() < getX())));
            setVelY(changeSign(getVelY(), Boolean.compare(getY() < 0, getLevel().getDimension().getHeight() < getY())));
            super.tick();
        }
    }

    public static abstract class Disappearing extends GameObject {
        public Disappearing(Point.Double p, double w, double h, GameLevel l) {
            super(p, w, h, l);
        }

        @Override
        public void tick() {
            if(!new Rectangle(getLevel().getDimension()).intersects(getBounds())) {
                getLevel().remove(this);
            }
            super.tick();
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
