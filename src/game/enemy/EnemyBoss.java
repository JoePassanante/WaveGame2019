package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBoss extends GameEntity.Bouncing {
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

    @Override
    public void collide(Player p) {
	    if(timer <= 0) {
            p.damage(2);
        }
    }

    public void tick() {
	    super.tick();

		if (timer <= 0) {
            setVelY(0);
            getLevel().getPlayers().forEach(p -> {
                if(p.getPosY() < 200) {
                    collide(p);
                }
            });
        }
		else {
            timer -= 1;
        }
		drawFirstBullet();
		if (timer <= 0) {
            timer2 -= 1;
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

		setHealth(getHealth()-1);

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

	// cast the Graphics object passed into the rendering method to a Graphics2D object
	// is the abstract base class for all graphics contexts that allow an application to draw 
	// onto components that are realized on various devices, as well as onto off-screen images
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 138, (int)getLevel().getDimension().getWidth(), 138);
        super.render(g);

		// HEALTH BAR
		g.setColor(Color.GRAY);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);
		g.setColor(Color.RED);
		g.fillRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, (int)getHealth(), 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getLevel().getDimension().getWidth() / 2 - 500, (int)getLevel().getDimension().getHeight() - 150, 1000, 50);
	}

	// allows for grey line to be drawn, as well as first bullet shot
	public void drawFirstBullet() {
		if (timer2 == 1) {
            getLevel().getEntities().add(new EnemyBossBullet(new Point2D.Double(getPosX() + 48, getPosY() + 96), getLevel()));
        }
	}
}
