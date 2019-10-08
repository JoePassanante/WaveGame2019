package game.enemy;


import game.GameObject;
import game.waves.Handler;

import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyFast extends GameObject.Bouncing {
	public EnemyFast(Point.Double point, Handler handler) {
		super(point.x, point.y, 32, 64, handler);
		setVelX(1 - 2*Math.random());
		setVelY(-12);
	}

	@Override
    public void render(Graphics g) {
        if (getVelY() > 0) {
            g.drawImage(getHandler().getTheme().get(this), (int)getX(), (int)getY()+64,(int)getWidth(),(int)-getHeight(), null);
        }
        else {
            g.drawImage(getHandler().getTheme().get(this), (int)getX(), (int)getY(),(int)getWidth(),(int)getHeight(), null);
        }
    }
 }
