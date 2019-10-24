package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class EnemyBossBomb extends GameEntity {
	private double explodeHeight;
	private int shots;

	public EnemyBossBomb(Point.Double point, GameLevel level, int s) {
		super(point.x, point.y, 32, 32, level);
		explodeHeight = level.getDimension().getHeight()*level.getRandom().random();
		setVelY(5);
		shots = s;
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
		super.tick();
		if (getPosY() > explodeHeight) {
            getLevel().getEntities().remove(this);
			for (int i = 0; i < shots; i++) {
                getLevel().getEntities().add( new EnemyBossBombBullet(
                    new Point2D.Double(getPosX(), getPosY()),
                    getLevel(),
                    16*Math.cos(Math.toRadians(360.0*i/shots)),
                    16*Math.sin(Math.toRadians(360.0*i/shots))
                ));
			}
		}

	}
	
	public void render(Graphics g) {
	    super.render(g, Color.pink);
	}
}
