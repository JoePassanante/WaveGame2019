package mainGame;


import java.util.Random;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyBurst extends GameObject {
	private int timer;
	private int size;
	private String side;
	private Random r = new Random();

	public EnemyBurst(double x, double y, double velX, double velY, int size, String side, Handler handler) {
		super(x, y, 150, 150, handler);
		this.velX = velX*2;
		this.velY = velY*2;
		this.timer = 60;
		this.side = side;
		this.size = size;

		if (this.side.equals("left")) {
			handler.add(new EnemyBurstWarning(0, 0, 25, (int)handler.getGameDimension().getHeight(), handler));
			setPos();
			setVel();
		} else if (this.side.equals("right")) {
			handler.add(new EnemyBurstWarning(handler.getGameDimension().getWidth() - 25, 0, 25, (int)handler.getGameDimension().getHeight(), handler));
			setPos();
			setVel();

		} else if (this.side.equals("top")) {
			handler.add(new EnemyBurstWarning(0, 0, (int)handler.getGameDimension().getWidth(), 25, handler));
			setPos();
			setVel();

		} else if (this.side.equals("bottom")) {
			handler.add(new EnemyBurstWarning(0, handler.getGameDimension().getHeight() - 25, (int)handler.getGameDimension().getWidth(), 25, handler));
			setPos();
			setVel();
		}
	}

	public void tick() {
		//check for removal
		if (this.y <= -getHandler().getGameDimension().getHeight() || this.y >= getHandler().getGameDimension().getHeight()*2){ getHandler().remove(this); return;}
		if (this.x <= -getHandler().getGameDimension().getWidth() || this.x >= getHandler().getGameDimension().getWidth()*2){ getHandler().remove(this); return;}

		//handler.addObject(new Trail(x, y, ID.Trail, Color.orange, this.size, this.size, 0.025, this.handler));

		timer--;
		if (timer <= 0) {
			this.x += velX;
			this.y += velY;

		}
		
	}

	public void setPos() {
		if (this.side.equals("left")) {
			this.y = r.nextInt(((int)(getHandler().getGameDimension().getHeight() - size)) + 1);
		} else if (this.side.equals("right")) {
			this.x = getHandler().getGameDimension().getWidth() + 200;
			this.y = r.nextInt(((int)(getHandler().getGameDimension().getHeight() - size)) + 1);

		} else if (this.side.equals("top")) {
			this.y = -(size);
			this.x = r.nextInt(((int)(getHandler().getGameDimension().getWidth() - size)) + 1);

		} else if (this.side.equals("bottom")) {
			this.y = getHandler().getGameDimension().getHeight() + 200;
			this.x = r.nextInt(((int)(getHandler().getGameDimension().getWidth() - size)) + 1);

		}
	}

	public void setVel() {
		if (this.side.equals("left")) {
			this.velY = 0;
		} else if (this.side.equals("right")) {
			this.velX = -(this.velX);
			this.velY = 0;

		} else if (this.side.equals("top")) {
			this.velX = 0;

		} else if (this.side.equals("bottom")) {
			this.velX = 0;
			this.velY = -(this.velY);
		}
	}
}
