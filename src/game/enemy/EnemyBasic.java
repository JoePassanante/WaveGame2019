package game.enemy;

import game.GameObject;
import game.waves.Handler;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyBasic extends GameObject.Bouncing {
	public EnemyBasic(Point.Double point, Handler handler) {
		super(point.x, point.y, 125, 60, handler);
		setVelX(10 * (Math.random() < .5 ? -1 : 1));
		setVelY(10 * (Math.random() < .5 ? -1 : 1));
		System.out.println(getVelX());
	}
}
