package mainGame;

import java.awt.Rectangle;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterMover extends GameObject {
	private int timer;
	private GameObject player;
	private double bulletVelX;
	private double bulletVelY;
	private int bulletSpeed;
	private double velX, velY;

	public EnemyShooterMover(double x, double y, int sizeX, int sizeY, int bulletSpeed, Handler handler) {
		super(x, y, 100, 75, handler);

		this.velX = 10;
		this.velY = 10;
		if (Math.random() > .5) {
			velX*=-1;
		}
		if (Math.random() > .5) {
			velY*=-1;
		}
		this.timer = 60;
		this.bulletSpeed = bulletSpeed;

		for (int i = 0; i < handler.size(); i++) {
			if (handler.get(i) instanceof Player)
				player = handler.get(i);
		}
	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y <= 0 || this.y >= getHandler().getGameDimension().getHeight() - height)
			velY *= -1;
		if (this.x <= 0 || this.x >= getHandler().getGameDimension().getWidth() - width)
			velX *= -1;

		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer--;
		
		
		if (timer <= 0) {
			shoot();
			
			timer = 20;
		}

	}

	public void shoot() {
		if (player != null) {
			
		double diffX = this.x - player.getX() - 16;
		double diffY = this.y - player.getY() - 16;
		double distance = Math.sqrt(((this.x - player.getX()) * (this.x - player.getX()))
				+ ((this.y - player.getY()) * (this.y - player.getY())));
		////////////////////////////// pythagorean theorem
		////////////////////////////// above//////////////////////////////////////////////////
		bulletVelX = ((this.bulletSpeed / distance) * diffX); // numerator affects speed of enemy
		bulletVelY = ((this.bulletSpeed / distance) * diffY);// numerator affects speed of enemy

        getHandler().addObject(
            new EnemyShooterBullet(this.x -10, this.y-10, bulletVelX, bulletVelY, getHandler()));
		}
		else {
			System.err.println("player is null on shooter!");//bpm
			for (int i = 0; i < getHandler().size(); i++) {
				if (getHandler().get(i) instanceof Player)
					player = getHandler().get(i);
			}
		}
	}

	public void updateEnemy() {
		this.width*= .95;
		this.height*=.95;

		if (width <= 1 || height <= 1) {
            getHandler().removeObject(this);
		}
	}
}
