package mainGame;

import java.awt.Point;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyShooterSharp extends GameObject {
	private int timer;
	private GameObject player;
	private double bulletVelX;
	private double bulletVelY, speed;
	private int bulletSpeed;

	public EnemyShooterSharp(double x, double y, int sizeX, int sizeY, int bulletSpeed, Handler handler) {
		super(x, y, 200, 150, handler);
		this.velX = 0;
		this.velY = 0;
		this.timer = 60;
		speed = 1;
		this.bulletSpeed = Math.abs(bulletSpeed);

		for (int i = 0; i < handler.size(); i++) {
			if (handler.get(i) instanceof Player)
				player = handler.get(i);
		}

	}

	public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y <= 0 || this.y >= getHandler().getGameDimension().getHeight() - 40)
			velY *= -1;
		if (this.x <= 0 || this.x >= getHandler().getGameDimension().getWidth() - 16)
			velX *= -1;

		//handler.addObject(new Trail(x, y, ID.Trail, Color.yellow, this.sizeX, this.sizeY, 0.025, this.handler));
		
		timer--;
		
		
		if (timer <= 0) {
			shoot();
			
			timer = 30;
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
			double timeTo = (distance/bulletSpeed);
			double newX = player.getX()+(player.getVelX()*timeTo);
			double newY = player.getY()+(player.getVelY()*timeTo);
			double dir = pointDirection(new Point.Double(this.x,this.y),new Point.Double(newX,newY));
			bulletVelX = Math.cos(dir)*bulletSpeed;
			bulletVelY = Math.sin(dir)*bulletSpeed;
			
			velX = Math.cos(dir)*speed;
			velY = Math.sin(dir)*speed;
			//supposed to shoot where they're going, not 100% accurate in terms of time yet though

            getHandler().addObject(
					new EnemyShooterBullet(this.x -10, this.y-10, bulletVelX, bulletVelY, getHandler()));
		bulletVelX = -((this.bulletSpeed / distance) * diffX); // numerator affects speed of enemy
		bulletVelY = -((this.bulletSpeed / distance) * diffY);// numerator affects speed of enemy}

            getHandler().addObject(
				new EnemyShooterBullet(this.x -10, this.y-10, bulletVelX, bulletVelY, getHandler()));
		}else {
			System.err.println("player is null on shooter!");//bpm
			for (int i = 0; i < getHandler().size(); i++) {
				if (getHandler().get(i) instanceof Player)
					player = getHandler().get(i);
			}
		}
	}

	public void updateEnemy() {
		width*= .95;
		height*=.95;

		if (width <= 1 || height <= 1) {
            getHandler().removeObject(this);
		}
	}
	
	public static double pointDirection(Point.Double p1, Point.Double p2)
    {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        return (Math.atan2(yDiff, xDiff));
    }
    
    public static double angleDifference(double angleFrom, double angleTo) {
        return ((((angleFrom - angleTo) % 360) + 540) % 360) - 180;
    }
}
