package mainGame;

import java.awt.*;

public class EnemyBossBomb extends GameObject {
	// instances
	int explodeHeight;
	private int shots;
	// constructor
	// used to initialize the state of the object
	public EnemyBossBomb(double x, double y, Handler handler, int shots) {
		super(x, y, 32, 32, handler);
		this.explodeHeight = (int) (Math.random()*handler.getGameDimension().getHeight());
		velY = 5;
		this.shots = shots;
	}

	// methods
	// is called every 1/60 seconds, allows game objects to update themselves before being rendered.
	public void tick() {
		this.y += velY;
		if (y>explodeHeight) {
            getHandler().removeObject(this);
			for (int i = 0; i < shots; i++) {
                getHandler().addObject( new EnemyBossBombBullet(
                    (int) this.x,
                    (int) this.y,
                    getHandler(),
                    (int)(16*Math.cos(Math.toRadians(360.0*i/shots))),
                    (int)(16*Math.sin(Math.toRadians(360.0*i/shots)))
                ));
			}
		}

	}
	
	// is the abstract base class for all graphics contexts that allow an application to draw 
	// onto components that are realized on various devices, as well as onto off-screen images
	public void render(Graphics g) {
		g.setColor(Color.PINK);
		g.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
	}
}
