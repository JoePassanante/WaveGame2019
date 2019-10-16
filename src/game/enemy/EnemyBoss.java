package game.enemy;

import game.GameObject;
import game.Handler;

import java.awt.*;

/**
 *
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBoss extends GameObject.Bouncing {
	// instances
	private int timer = 80;
	private int timer2 = 50;
	private int spawn;
	private int difficulty;
	private int bombTimer = 120;
	// constructor
	// used to initialize the state of the object
	public EnemyBoss(Point.Double point, Handler handler) {
		super(1, -100, 96, 96, handler);
		setVelX(0);
		setVelY(2);
		setHealth(1000); //full health is 1000
		difficulty = handler.getLevel()/10;
	}
	
	// methods
	// is called every frame, allows game objects to update themselves before being rendered.
	public void tick() {
	    super.tick();

		if (timer <= 0) {
            setVelY(0);
        }
		else {
            timer -= 1;
        }
		drawFirstBullet();
		if (timer <= 0) {
            timer2--;
        }
		if (timer2 <= 0) {
			if (getVelX() == 0) {
                setVelX(8);
            }
			spawn = getHandler().getRandom().nextInt(5);
			if (spawn == 0) {
				getHandler().add(new EnemyBossBullet((int) getX() + 48, (int) getY() + 80, getHandler()));
				setHealth(getHealth()-3);
			}
		}
		//prevents the alien boss from spawning bombs at the earlier levels
		if (difficulty > 0) {
			//if the timer is less than 0, trigger the bombs
			bombTimer--;
			if (bombTimer < 0) {
				//resets the bomb timer
				bombTimer = 120;
				//removes the EnemyBossBomb class
				getHandler().remove(
						new EnemyBossBomb((int) getX() + 48, (int) getY() + 80, getHandler(),difficulty > 1 ? ( difficulty > 2 ? 16 : 8 ) : 4));
			}
		}

		// handler.addObject(new Trail(x, y, ID.Trail, Color.red, 96, 96, 0.025,
		// this.handler));
		if (getHealth() <= 0) {
			System.out.println("Removing Boss");
			getHandler().remove(this);
		}
	}

	// cast the Graphics object passed into the rendering method to a Graphics2D object
	// is the abstract base class for all graphics contexts that allow an application to draw 
	// onto components that are realized on various devices, as well as onto off-screen images
	public void render(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(0, 138, (int)getHandler().getGameDimension().getWidth(), 138);
        super.render(g);

		// HEALTH BAR
		g.setColor(Color.GRAY);
		g.fillRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, 1000, 50);
		g.setColor(Color.RED);
		g.fillRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, (int)getHealth(), 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, 1000, 50);
	}

	// allows for grey line to be drawn, as well as first bullet shot
	public void drawFirstBullet() {
		if (timer2 == 1) {
            getHandler().add(new EnemyBossBullet((int) getX() + 48, (int) getY() + 96, getHandler()));
        }
	}
}
