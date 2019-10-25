package game.enemy;

import game.*;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class EnemyRocketBossMissile extends GameEntity.Disappearing {
	private double speed;
	private double angle;
	private Player target;
	private Path2D hitbox;
    private boolean clipped;

    public EnemyRocketBossMissile(Point.Double point, GameLevel level, double spd) {
		super(point, 32, 64, level);
		speed = spd;
		target = level.getRandom().new RandomDifferentElement<>(level.getPlayers()).get();
		clipped = true;
		setHealth(100);
        hitbox = new Path2D.Double(super.getBounds());
	}

    @Override
    public void collide(Player p) {
        if(hitbox.intersects(p.getBounds())) {
            p.damage(1);
            getLevel().getEntities().remove(this);
        }
    }

    public void tick() {
        super.tick();

		angle = Math.atan2(target.getPosY()-getPosY(), target.getPosX()-getPosX());
		setVelX(speed*Math.cos(angle));
		setVelY(speed*Math.sin(angle));
		setHealth(getHealth() - 1);
		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        AffineTransform at = AffineTransform.getRotateInstance(angle + Math.PI/2, getPosX(), getPosY());
        g2d.transform(at);
        hitbox = new Path2D.Double(super.getBounds(), at);
        super.render(g2d, super.getBounds());
        g2d.setTransform(old);

//        g2d.setColor(Color.YELLOW);
//        g2d.draw(hitbox);
	}

    @Override
    public void render(Clip c, int i) {
        if(clipped) {
            super.render(c,i);
            clipped = false;
        }
    }

    @Override
    public Rectangle getBounds() {
        return hitbox.getBounds();
    }
}

