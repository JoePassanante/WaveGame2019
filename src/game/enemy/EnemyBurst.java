package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.geom.Point2D;

public class EnemyBurst extends GameEntity.Disappearing {
	private EnemyBurstWarning warning;

	public EnemyBurst(GameLevel level) {
		super(level.spawnPoint(),150, 150, level);

		double r = getLevel().getRandom().random();
        double x, y, w, h;
		if(r < .25) {
            setPosX(-getWidth());
            setVelX(30);
            w = 25;
            h = level.getDimension().getHeight();
            x = w/2;
            y = h/2;
        }
        else if(r < .50) {
            setPosX(level.getDimension().width + getWidth());
            setVelX(-30);
            w = 25;
            h = level.getDimension().getHeight();
            x = level.getDimension().getWidth() - w/2;
            y = h/2;
        }
        else if(r < .75) {
            setPosY(-getHeight());
            setVelY(30);
            w = level.getDimension().getWidth();
            h = 25;
            x = w/2;
            y = h/2;
        }
        else {
            setPosY(level.getDimension().height + getHeight());
            setVelY(-30);
            w = level.getDimension().getWidth();
            h = 25;
            x = w/2;
            y = level.getDimension().getHeight() - h/2;
        }

        warning = new EnemyBurstWarning(new Point2D.Double(x,y),w,h,level);
	}

	private boolean warned = false, spawned = false;

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
        if(!getLevel().getEntities().contains(warning)) {
            if(!warned) {
                getLevel().getEntities().add(warning);
                warned = true;
            }
            else {
                super.tick();
            }
        }

        if(!getLevel().getEntities().contains(this)) {
            if(!spawned) {
                getLevel().getEntities().add(this);
            }
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.orange, this.size, this.size, 0.025, this.handler));
	}
}
