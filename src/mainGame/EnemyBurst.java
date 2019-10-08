package mainGame;

import java.awt.*;

public class EnemyBurst extends GameObject {
	private int timer;

	public EnemyBurst(Point.Double point, Handler handler) {
		super(point.x, point.y, 150, 150, handler);

		double r = Math.random();
		if(r < .25) {
            x = -width;
            velX = 30;
            handler.add(new EnemyBurstWarning(0, 0, 25, (int)handler.getGameDimension().getHeight(), handler));
        }
        else if(r < .50) {
            x = handler.getGameDimension().width + width;
            velX = -30;
            handler.add(new EnemyBurstWarning(handler.getGameDimension().getWidth() - 25, 0, 25, (int)handler.getGameDimension().getHeight(), handler));
        }
        else if(r < .75) {
            y = -height;
            velY = 30;
            handler.add(new EnemyBurstWarning(0, 0, (int)handler.getGameDimension().getWidth(), 25, handler));
        }
        else {
            y = handler.getGameDimension().height + height;
            velY = -30;
            handler.add(new EnemyBurstWarning(0, handler.getGameDimension().getHeight() - 25, (int)handler.getGameDimension().getWidth(), 25, handler));
        }

		this.timer = 60;
	}

	public void tick() {
		//check for removal
		if (this.y <= -getHandler().getGameDimension().getHeight() || this.y >= getHandler().getGameDimension().getHeight()*2){ getHandler().remove(this); return;}
		if (this.x <= -getHandler().getGameDimension().getWidth() || this.x >= getHandler().getGameDimension().getWidth()*2){ getHandler().remove(this); return;}

		//handler.addObject(new Trail(x, y, ID.Trail, Color.orange, this.size, this.size, 0.025, this.handler));

		timer -= 1;
		if (timer <= 0) {
			this.x += velX;
			this.y += velY;
		}
		
	}
}
