package game;

import javax.sound.sampled.Clip;
import java.awt.*;

/**
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/1/19
 */

public class GameEntity extends Performer {
    private GameLevel level;
    public GameLevel getLevel() {
        return level;
    }

    private double posX, posY, width, height, velX, velY, health;
    public void setLevel(GameLevel l) {
        level = l;
    }
    public void setPosX(double x) {
        posX = x;
    }
    public void setPosY(double y) {
        posY = y;
    }
    public void setWidth(double w) {
        this.width = w;
    }
    public void setHeight(double h) {
        this.height = h;
    }
    public void setVelX(double v) {
        this.velX = v;
    }
    public void setVelY(double v) {
        this.velY = v;
    }
    public void setHealth(double h) {
        health = h;
    }
    public double getPosX() {
        return posX;
    }
    public double getPosY() {
        return posY;
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
    public double getVelY() {
        return velY;
    }
    public double getHealth() {
        return this.health;
    }

    private boolean clipped;
    public void clip() {
        clipped = true;
    }
    public void collide(Player p) { // called when a GameObject touches a Player
        clipped = true;
    }

    public GameEntity(double x, double y, double w, double h, GameLevel l) {
        posX = x;
        posY = y;
        width = w;
        height = h;
        level = l;
        refer(l.getTheme().get(this));
    }

    @Override
    public void tick() {
        super.tick();
        posX += velX;
        posY += velY;
        if(getHealth() < 0) {
            getLevel().getEntities().remove(this);
            getLevel().getNonentities().remove(this);
        }
    }

    private Clip playing;
    @Override
    public void render(Clip c, int i) {
        if(clipped) {
            clipped = false;
            if(playing == null || !playing.isActive()) {
                super.render(c, i);
                playing = c;
            }
        }
    }

    public void render(Graphics g, Color c) {
        g.setColor(c);
        Rectangle bounds = getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    @Override
    public Rectangle getBounds() { // x and y are the center of a width by height hitbox
        return new Rectangle.Double(
            posX - width/2,
            posY - height/2,
            width,
            height
        ).getBounds();
    }

    public static abstract class Stopping extends GameEntity {
        public Stopping(Point.Double p, double w, double h, GameLevel l) {
            super(p.x, p.y, w, h, l);
        }

        @Override
        public void tick() {
            super.tick();
            setPosX(clamp(getPosX(), 0, getLevel().getDimension().getWidth()));
            setPosY(clamp(getPosY(), 0, getLevel().getDimension().getHeight()));
        }
    }

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
