package game.enemy;

import game.GameLevel;
import game.Performer;
import game.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class EnemyRocketBoss extends Enemy { // a boss that follows the player and shoots guided missiles
    private Path2D.Double hitbox;
    private double angle, dash;
    private Performer off, on;

    public EnemyRocketBoss(GameLevel level) {
        super(level.getDimension().getWidth() / 2, level.getDimension().getHeight() / 2, 80, 296, level);
        setHealth(1000);
        off = getLevel().getTheme().get(getClass().getSimpleName());
        on = getLevel().getTheme().get(getClass().getSimpleName() + "On");
        hitbox = new Path2D.Double(super.getBounds());
    }

    @Override
    public void collide(Player player) { // broad phase collision
        if (hitbox.intersects(player.getBounds())) { // narrow phase collision
            super.collide(player);
            if (dash > 0) {
                dash = 0;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        Point2D.Double target = getLevel().targetPoint();

        if (dash > 0) { // boss is dashing
            double speed = 20 - getHealth() / 100;
            setVelX(speed * Math.cos(angle));
            setVelY(speed * Math.sin(angle));
            if (!hitbox.intersects(getLevel().getBounds())) {
                dash = 0;
            }
            setHealth(getHealth() - 1);
        } else if (dash == 0) { // boss stops dashing
            getLevel().getEntities().add(new EnemyBurst(getLevel()));
            if (getLevel().getNumber() > 10) {
                getLevel().getEntities().add(
                        new EnemyRocketBossMissile(
                                new Point2D.Double(getPosX(), getPosY()),
                                getLevel(),
                                10
                        ));
            }
            refer(off);
        } else if (dash > -60) { // boss is turning towards player
            setVelX(0);
            setVelY(0);
            double arc = Math.atan2(target.getY() - getPosY(), target.getX() - getPosX());
            angle += .10 * (Math.atan2(Math.sin(arc - angle), Math.cos(arc - angle)));
        } else { // boss is about to dash
            dash = 60;
            refer(on);
        }

        dash -= 1;
    }

    @Override
    public void render(Graphics g) {
        //draw health bar
        g.setColor(Color.GRAY);
        g.fillRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, 1000, 50);
        g.setColor(Color.RED);
        g.fillRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, (int) getHealth(), 50);
        g.setColor(Color.WHITE);
        g.drawRect((int) getLevel().getDimension().getWidth() / 2 - 500, (int) getLevel().getDimension().getHeight() - 150, 1000, 50);

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform old = g2d.getTransform();

        g2d.setTransform(AffineTransform.getRotateInstance(angle + Math.PI / 2, getPosX(), getPosY()));
        Rectangle box = super.getBounds();
        if (dash <= 0) {
            box = new Rectangle(box.x, box.y, box.width, 220);
        }
        hitbox = new Path2D.Double(box, g2d.getTransform()); // transform the hitbox
        super.render(g2d, super.getBounds()); // draw rocket
        g2d.setTransform(old);
    }

    @Override
    public Rectangle getBounds() {
        return hitbox.getBounds();
    } // rotated rectangle to match the graphical bounds
}
