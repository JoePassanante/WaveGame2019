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

public class EnemyRocketBoss extends GameEntity {
    private Path2D hitbox;
	private double angle;
	private double dash;

    private static class EnemyRocketBossOn extends EnemyRocketBoss {
        public EnemyRocketBossOn(GameLevel level) {
            super(level);
        }
    }

    EnemyRocketBossOn on;

    public EnemyRocketBoss(GameLevel level) {
		super(level.getDimension().getWidth()/2, level.getDimension().getHeight()/2, 80, 296, level);
		setHealth(1000);
		hitbox = new Path2D.Double(super.getBounds());
		on = new EnemyRocketBossOn(level);
	}

    @Override
    public void collide(Player player) {
        player.damage(2);
        dash = 0;
    }

    @Override
	public void tick() {
        super.tick();

        Point2D.Double target = getLevel().targetPoint();

        dash -= 1;
        if (dash > 0) {
            double speed = getHealth() / 100;
            setVelX(speed * Math.cos(angle));
            setVelY(speed * Math.sin(angle));
        }
        else if (dash > -60) {
            angle = Math.atan2(target.getY()-getPosY(),target.getX()-getPosX());
        }
        else {
            dash = 60;
        }

        if (getHealth() % 100 == 0 && getLevel().getNumber() > 10) {
            getLevel().getEntities().add( new EnemyRocketBossMissile(
                new Point2D.Double(getPosX(), getPosY()),
                getLevel(),
                10,
                target
            ));
        }

        if (getHealth() % 150 == 0) {
            getLevel().getEntities().add(new EnemyBurst(getLevel()));
        }
        setHealth(getHealth() - 1);

        if (getHealth() <= 0) {
            System.out.println("Removing Boss");
            getLevel().getEntities().remove(this);
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

		AffineTransform at = AffineTransform.getRotateInstance(angle, getPosX(), getPosY());
		g2d.setTransform(at);
		super.render(g, new Rectangle(0,0,(int)getWidth(),(int)getHeight()));

        hitbox = new Path2D.Double(super.getBounds(), at);
        g2d.setColor(Color.YELLOW);
        g2d.draw(hitbox);

        g2d.setTransform(old);
    }
}
