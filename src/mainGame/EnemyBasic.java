package mainGame;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyBasic extends GameObject {
	public EnemyBasic(double x, double y, double velX, double velY, Handler handler) {
		super(x, y, 125, 60, handler);
		if (Math.random() > .5) {
			velX *= -1;
		}
		if (Math.random() > .5) {
			velY *= -1;
		}
		this.velX = velX;
		this.velY = velY;
	}

	public void tick() {
		if (this.y <= 0){
			velY = Math.abs(velY);
		}
		if(this.y >= getHandler().getGameDimension().getHeight() - 40){
			velY = -Math.abs(velY);
		}
		if (this.x <= 0){
			velX = Math.abs(velX);
		}
		if(this.x >= getHandler().getGameDimension().getWidth() - 16){
			velX = -Math.abs(velX);
		}

		this.x += velX;
		this.y += velY;

		//handler.addObject(new Trail(x, y, ID.Trail, Color.white, 16, 16, 0.025, this.handler));
	}
}
