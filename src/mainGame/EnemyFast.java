package mainGame;


import java.awt.*;

/**
 * A type of enemy in the game
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class EnemyFast extends GameObject {
	public EnemyFast(double x, double y, ID id, Handler handler) {
		super(x, y, 32, 64, id, handler);
		velX = 2*(Math.random()-Math.random());
		velY = -12;
	}

    public void tick() {
		this.x += velX;
		this.y += velY;

		if (this.y <= 0) {
			velY = Math.abs(velY);
		}
        if (this.y >= getHandler().getGameDimension().getHeight() - 40) {
            velY = -Math.abs(velY);
        }
		if (this.x <= 0) {
			velX = Math.abs(velX);
		}
		if (this.x >= getHandler().getGameDimension().getWidth() - 16) {
		    velX = -Math.abs(velX);
        }

		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	@Override
    public void render(Graphics g) {
        Graphics2D a = (Graphics2D) g;
        if (velY > 0) {
            a.drawImage(getHandler().getTheme().get(ID.EnemyFast), (int)x, (int)y+64,(int)width,(int)-height, null);
        }
        else {
            a.drawImage(getHandler().getTheme().get(ID.EnemyFast), (int)x, (int)y,(int)width,(int)height, null);
        }
    }
 }
