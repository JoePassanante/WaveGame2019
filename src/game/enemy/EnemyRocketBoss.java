package game.enemy;

import game.*;
import game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class EnemyRocketBoss extends GameObject {
	private Path2D hitbox;
	private double angle;
	private double dash;

    public EnemyRocketBoss(GameLevel level) {
		super(level.spawnPoint(), 80, 296, level);
		setHealth(1000);
		hitbox = new Path2D.Double(super.getBounds());
	}

    @Override
    public void collide(Player player) {
        player.damage(2);
        dash = 0;
    }

    @Override
	public void tick() {
        super.tick();

        Player target = getLevel().getPlayers().stream().filter(getLevel()::contains)
            .min((l,r) -> (int)(
                    Math.hypot(getX()-l.getX(),getY()-l.getY()) -
                    Math.hypot(getX()-r.getX(),getY()-r.getY()))
            ).orElse(null);

        if (target != null) {
            dash -= 1;
            if (dash > 0) {
                double speed = getHealth() / 100;
                setVelX(speed * Math.cos(angle));
                setVelY(speed * Math.sin(angle));
            }
            else if (dash > -60) {
                angle = Math.atan2(target.getY()-getY(),target.getX()-getX());
            }
            else {
                dash = 60;
            }

            if (getHealth() % 100 == 0 && getLevel().getNumber() > 10) {
                getLevel().add(new EnemyRocketBossMissile(
                    new Point2D.Double(getX(), getY()),
                    getLevel(),
                    10,
                    target
                ));
            }
        }

        if (getHealth() % 150 == 0) {
            getLevel().add(new EnemyBurst(getLevel()));
        }
        setHealth(getHealth() - 1);

        if (getHealth() <= 0) {
            System.out.println("Removing Boss");
            getLevel().remove(this);
        }
    }

	@Override
	public void render(Graphics g) {
	    //draw health bar
		g.setColor(Color.GRAY);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);
		g.setColor(Color.RED);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, (int)getHealth(), 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);

		Graphics2D g2d = (Graphics2D)g;
		//DEV TOOLS
		/*
		g2d.drawLine((int)this.x, (int)this.y, (int)this.dash_x, (int)this.dash_y); //DEV TOOL
		Ellipse2D e = new Ellipse2D.Double(this.dash_x,this.dash_y,10,10);
		g2d.draw(e);
		 */
		
		//Draw Rocket
		AffineTransform old = g2d.getTransform();

		AffineTransform at = AffineTransform.getRotateInstance(angle, getX(), getY());
		g2d.setTransform(at);
        g2d.drawImage(getLevel().getTheme().get(dash > 0 ? getClass().getSimpleName() + "On" : getClass().getSimpleName()), 0, 0, (int)getWidth(), (int)getHeight(), null);

        hitbox = new Path2D.Double(super.getBounds(), at);
        g2d.setColor(Color.YELLOW);
        g2d.draw(hitbox);

        g2d.setTransform(old);
    }
}
