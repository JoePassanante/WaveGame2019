package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class EnemyBurst extends GameObject.Disappearing {
	private Point start;
	private EnemyBurstWarning warning;

	public EnemyBurst(GameLevel level) {
		super(level.spawnPoint(),150, 150, level);

		double r = getLevel().getRandom().random();
        double x=0, y=0, w=0, h=0;
		if(r < .25) {
            setX(-getWidth());
            setVelX(30);
            x = 0;
            y = 0;
            w = 25;
            h = level.getDimension().getHeight();
        }
        else if(r < .50) {
            setX(level.getDimension().width + getWidth());
            setVelX(-30);
            x = level.getDimension().getWidth() - 25;
            y = 0;
            w = 25;
            h = level.getDimension().getHeight();
        }
        else if(r < .75) {
            setY(-getHeight());
            setVelY(30);
            x = 0;
            y = 0;
            w = level.getDimension().getWidth();
            h = 25;
        }
        else {
            setY(level.getDimension().height + getHeight());
            setVelY(-30);
            x = 0;
            y = level.getDimension().getHeight() - 25;
            w = level.getDimension().getWidth();
            h = 25;
        }

        warning = new EnemyBurstWarning(new Point2D.Double(x,y),w,h,level);

        start = getBounds().getLocation();
	}

	private boolean warned = false, spawned = false;

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
        if(!getLevel().contains(warning)) {
            if(!warned) {
                getLevel().add(warning);
                warned = true;
            }
            else {
                super.tick();
            }
        }

        if(!getLevel().contains(this)) {
            if(!spawned) {
                getLevel().add(this);
            }
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.orange, this.size, this.size, 0.025, this.handler));
	}
}
