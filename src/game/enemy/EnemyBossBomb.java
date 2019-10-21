package game.enemy;

import game.GameLevel;
import game.GameObject;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class EnemyBossBomb extends GameObject {
	private int explodeHeight;
	private int shots;

	public EnemyBossBomb(Point.Double point, GameLevel level, int shots) {
		super(point, 32, 32, level);
		this.explodeHeight = (int) (level.getRandom().random()*level.getDimension().getHeight());
		setVelY(5);
		this.shots = shots;
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
		super.tick();

		if (getY()>explodeHeight) {
            getLevel().remove(this);
			for (int i = 0; i < shots; i++) {
                getLevel().add( new EnemyBossBombBullet(
                    new Point2D.Double(getX(), getY()),
                    getLevel(),
                    (int)(16*Math.cos(Math.toRadians(360.0*i/shots))),
                    (int)(16*Math.sin(Math.toRadians(360.0*i/shots)))
                ));
			}
		}

	}
	
	public void render(Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}
}
