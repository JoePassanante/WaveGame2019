package game.enemy;

import game.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class EnemyRocketBossMissile extends GameEntity.Disappearing {
	private double speed;
	private double angle;
	private Point2D.Double target;
	private Path2D hitbox;

    public EnemyRocketBossMissile(Point.Double point, GameLevel level, double spd, Point2D.Double player) {
		super(point, 32, -64, level);
		speed = spd;
		target = player;
		hitbox = new Path2D.Double(super.getBounds());
	}

    @Override
    public void collide(Player p) {
        p.damage(1);
        getLevel().getEntities().remove(this);
    }

    public void tick() {
        super.tick();

		angle = Math.atan2(target.getY()-getPosY(), target.getX()-getPosX());
		setVelX(speed*Math.cos(angle));
		setVelY(speed*Math.sin(angle));
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        AffineTransform at = AffineTransform.getRotateInstance(angle, getPosX(), getPosY());
        g2d.transform(at);
        super.render(g, new Rectangle(0,0,(int)getWidth(),(int)getHeight()));

        hitbox = new Path2D.Double(super.getBounds(), at);
        g2d.setColor(Color.YELLOW);
        g2d.draw(hitbox);

        g2d.setTransform(old);
		//a.fill(bounds);
	}
}
