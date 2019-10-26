package game.enemy;

import game.GameLevel;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * #author Aaron Paterson 10/24/19
 *
 */

public class EnemyBoss extends Enemy.Bouncing {
	private int timer = 80;
	private int timer2 = 50;
	private int spawn;
	private int difficulty;
	private int bombTimer = 120;

	public EnemyBoss(GameLevel level) {
		super(new Point2D.Double(1, -100), 96, 96, level);
		setVelX(0);
		setVelY(2);
		setHealth(600); //full health is 1000
		difficulty = level.getNumber()/10;
	}

    public void tick() {
	    super.tick();

		if (timer <= 0) {
            setVelY(0);
            timer2 -= 1;
        }
		else {
            timer -= 1;
        }
		if (timer2 <= 0) {
			if (getVelX() == 0) {
                setVelX(8);
            }
			spawn = getLevel().getRandom().nextInt(5);
			if (spawn == 0) {
                getLevel().getEntities().add(new EnemyBossBullet(new Point.Double(getPosX() + 48, getPosY() + 80), getLevel()));
//				setHealth(getHealth()-3);
			}
		}
        else if (timer2 == 1) {
            getLevel().getEntities().add(new EnemyBossBullet(new Point2D.Double(getPosX() + 48, getPosY() + 96), getLevel()));
            setHealth(getHealth()-1);
        }

		//prevents the alien boss from spawning bombs at the earlier levels
		if (difficulty > 0) {
			//if the timer is less than 0, trigger the bombs
			bombTimer -= 1;
			if (bombTimer < 0) {
				//resets the bomb timer
				bombTimer = 120;
			}
		}

		// handler.addObject(new Trail(x, y, ID.Trail, Color.red, 96, 96, 0.025,
		// this.handler));
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 138, (int)getLevel().getDimension().getWidth(), 138);
        super.render(g, super.getBounds());

		// HEALTH BAR
		g.setColor(Color.GRAY);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);
		g.setColor(Color.RED);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, (int)getHealth(), 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);
	}

	@Override
    public Rectangle getBounds() {
	    return new Rectangle(0,0, getLevel().getDimension().width, 200);
    }
}
