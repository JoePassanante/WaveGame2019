package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class Enemy extends GameEntity {
    public Enemy(double x, double y, double w, double h, GameLevel l) {
        super(x, y, w, h, l);
    }

    @Override
    public void collide(Player p) {
        super.collide(p);
        p.damage(1);
    }

    public static abstract class Bouncing extends Enemy {
        public Bouncing(Point.Double p, double w, double h, GameLevel l) {
            super(p.x, p.y, w, h, l);
        }

        @Override
        public void tick() {
            super.tick();
            setVelX(changeSign(getVelX(), Boolean.compare(getPosX() < 0, getLevel().getDimension().getWidth() < getPosX())));
            setVelY(changeSign(getVelY(), Boolean.compare(getPosY() < 0, getLevel().getDimension().getHeight() < getPosY())));
        }
    }

    public static abstract class Disappearing extends Enemy {
        public Disappearing(Point2D.Double p, double w, double h, GameLevel l) {
            super(p.x, p.y, w, h, l);
        }

        @Override
        public void tick() {
            super.tick();
            if(!getLevel().getBounds().intersects(getBounds())) {
                getLevel().getEntities().remove(this);
            }
        }
    }
}
