package game.enemy;

import game.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class EnemyRocketBossMissile extends GameObject.Disappearing {
	private double speed;
	private double angle;
	private Player target;
	private Path2D hitbox;

    public EnemyRocketBossMissile(Point.Double point, GameLevel level, double spd, Player player) {
		super(point, 32, -64, level);
		speed = spd;
		target = player;
		hitbox = new Path2D.Double(super.getBounds());
	}

    @Override
    public void collide(Player p) {
        p.damage(1);
    }

    public void tick() {
        super.tick();

		angle = Math.atan2(target.getY()-getY(), target.getX()-getX());
		setVelX(speed*Math.cos(angle));
		setVelY(speed*Math.sin(angle));
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        AffineTransform at = AffineTransform.getRotateInstance(angle, getX(), getY());
        g2d.transform(at);
        g2d.drawImage(getLevel().getTheme().get(this), 0, 0, (int)getWidth(), (int)getHeight(), null);

        hitbox = new Path2D.Double(super.getBounds(), at);
        g2d.setColor(Color.YELLOW);
        g2d.draw(hitbox);

        g2d.setTransform(old);
		//a.fill(bounds);
	}

	@Override
    public Rectangle getBounds() {
        return new Rectangle(getLevel().getDimension());
    }
}
