package mainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * The first boss in the game
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class EnemyBoss extends GameObject {
	// instances
	private int timer = 80;
	private int timer2 = 50;
	Random r = new Random();
	private int spawn;
	private int difficulty = 1;
	private int bombTimer = 120;
	private HUD hud;
	// constructor
	// used to initialize the state of the object
	public EnemyBoss(Handler handler, int diff, HUD h) {
		super(1, -120, 96, 96, handler);
		hud = h;
		velX = 0;
		velY = 2;
		this.health = 1000; //full health is 1000
		difficulty = diff;
	}
	
	// methods
	// is called every frame, allows game objects to update themselves before being rendered.
	public void tick() {
		this.x += velX;
		this.y += velY;

		if (timer <= 0)
			velY = 0;
		else
			timer--;
		drawFirstBullet();
		if (timer <= 0)
			timer2--;
		if (timer2 <= 0) {
			if (velX == 0)
				velX = 8;
			spawn = r.nextInt(5);
			if (spawn == 0) {
				getHandler().add(
						new EnemyBossBullet((int) this.x + 48, (int) this.y + 80, getHandler()));
				this.health -= 3;
			}
		}
		//prevents the alien boss from spawning bombs at the earlier levels
		if (difficulty > 0) {
			//if the timer is less than 0, trigger the bombs
			bombTimer--;
			if (bombTimer < 0) {
				//resets the bomb timer
				bombTimer = 120;
				//calls calls the EnemyBossBomb class
				getHandler().remove(
						new EnemyBossBomb((int) this.x + 48, (int) this.y + 80, getHandler(),difficulty > 1 ? ( difficulty > 2 ? 16 : 8 ) : 4));
			}
		}
		
		
		// if (this.y <= 0 || this.y >= Game.HEIGHT - 40) velY *= -1;
		if (this.x <= 0 || this.x >= getHandler().getGameDimension().getWidth() - 96) {
			velX *= -1;
			getHandler().add(new EnemyBossBullet((int) this.x, (int) this.y + 80, getHandler()));
			getHandler().add(new EnemyBossBullet((int) this.x + 96, (int) this.y + 80, getHandler()));
		}

		// handler.addObject(new Trail(x, y, ID.Trail, Color.red, 96, 96, 0.025,
		// this.handler));
		hud.levelProgress = (int)(1000-this.health)/10;
		if (this.health <= 0) {
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
		g.fillRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, (int)health, 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, 1000, 50);
	}

	// allows for grey line to be drawn, as well as first bullet shot
	public void drawFirstBullet() {
		if (timer2 == 1) {
            getHandler().add(new EnemyBossBullet((int) this.x + 48, (int) this.y + 96, getHandler()));
        }
	}
}
