package game.enemy;

import game.*;
import game.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class EnemyRocketBoss extends GameEntity.Stopping {
    private Path2D hitbox;
	private double angle;
	private double dash;
	private Performer on;

    public EnemyRocketBoss(GameLevel level) {
		super(new Point2D.Double(level.getDimension().getWidth()/2, level.getDimension().getHeight()/2), 80, 296, level);
		setHealth(1000);
		hitbox = new Path2D.Double(getBounds());
		on = getLevel().getTheme().get(getClass().getSimpleName() + "On");
	}

    @Override
    public void collide(Player player) {
        if(hitbox.intersects(player.getBounds())) {
            player.damage(2);
            if(dash > 0) {
                dash = 0;
            }
        }
    }

    @Override
	public void tick() {
        super.tick();

        Point2D.Double target = getLevel().targetPoint();

        if (dash > 0) {
            double speed = 100 - .1*getHealth();
            setVelX(speed * Math.cos(angle));
            setVelY(speed * Math.sin(angle));
        }
        else if (dash == 0) {
            getLevel().getEntities().add(new EnemyBurst(getLevel()));
            if (getLevel().getNumber() > 10) {
                getLevel().getEntities().add( new EnemyRocketBossMissile(
                        new Point2D.Double(getPosX(), getPosY()),
                        getLevel(),
                        10,
                        target
                ));
            }
            setHealth(getHealth() - 100);
            refer(getLevel().getTheme().get(this));
        }
        else if (dash > -60) {
            setVelX(0);
            setVelY(0);
            double arc = Math.atan2(target.getY()-getPosY(),target.getX()-getPosX());
            angle += .05*(Math.atan2(Math.sin(arc-angle), Math.cos(arc-angle)));
        }
        else {
            dash = 60;
            refer(on);
        }

        dash -= 1;
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

		AffineTransform old = g2d.getTransform();

		g2d.setTransform(AffineTransform.getRotateInstance(angle + Math.PI/2, getPosX(), getPosY()));
		Rectangle box = getBounds();
		if(dash <= 0) {
		    box = new Rectangle(box.x, box.y, box.width, 220);
        }
        hitbox = new Path2D.Double(box, g2d.getTransform());

        //Draw Rocket
		super.render(g2d);

        g2d.setTransform(old);

        //g2d.setColor(Color.YELLOW);
        //g2d.draw(hitbox);
    }
}
