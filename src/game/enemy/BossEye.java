package game.enemy;

import game.GameObject;
import game.waves.Handler;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

/**
 * The last boss in the game, shown in a 3x3 grid of 9 instances of BossEye
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class BossEye extends GameObject {
	private Image img;
	private Random r = new Random();;
	private float alpha = 0;
	private double life = 0.005;
	private int tempCounter = 0;
	private int timer;
	private int spawnOrder = 1;// make them begin moving from left to right, top to bottom
	private int placement;// where they are in the 3x3 grid of eyes
	private double speed;
	private double[] speedTypes = { -5, -6, -7, -8, -9 };
	private GameObject player;
	private double health;

	public BossEye(double x, double y, Handler handler, int placement) {
		super(x, y, 0, 0, handler);
		this.img = getHandler().getTheme().get(getClass());
        setWidth(img.getWidth(null));
        setHeight(img.getHeight(null));
		setVelX(0);
		setVelY(0);
		this.speed = speedTypes[r.nextInt(4)];
		this.placement = placement;
		this.timer = 400;
	}

	public void tick() {
	    super.tick();

		if (tempCounter == 0) {
			if (alpha < 0.995) {// this handles each eye fading in to the game
				alpha += life + 0.001;
			} else {
				tempCounter++;
				for (int i = 0; i < getHandler().getPlayers().size(); i++) {
                    this.player = getHandler().getPlayers().get(i);
				}
			}
		} else if (tempCounter == 1) {
			spawn();
			if (this.placement == 1 && this.spawnOrder >= 1) {
				attackPlayer();
			} else if (this.placement == 2 && this.spawnOrder >= 2) {
				attackPlayer();
			} else if (this.placement == 3 && this.spawnOrder >= 3) {
				attackPlayer();
			} else if (this.placement == 4 && this.spawnOrder >= 4) {
				attackPlayer();
			} else if (this.placement == 5 && this.spawnOrder >= 5) {
				attackPlayer();
			} else if (this.placement == 6 && this.spawnOrder >= 6) {
				attackPlayer();
			} else if (this.placement == 7 && this.spawnOrder >= 7) {
				attackPlayer();
			} else if (this.placement == 8 && this.spawnOrder >= 8) {
				attackPlayer();
			} else if (this.placement == 9 && this.spawnOrder >= 9) {
				attackPlayer();
			} else {
				this.health = 0;
			}
		}

	}

	public void spawn() {
		timer--;
		if (timer == 0) {
			this.spawnOrder++;
			timer = 200;
		}
	}

	public void attackPlayer() {
		if (player != null) {
            double diffX = getX() - player.getX();
			double diffY = getY() - player.getY();
			double distance = Math.hypot(diffX,diffY);
			setVelX(diffX * speed / distance);
			setVelY(diffY * speed / distance);
		}
	}

	public void render(Graphics g) {
		if (g.getColor() == Color.BLACK) {// prevent black text from showing "Game Over" if the player dies here, or
											// "Winner!" if the player survives
			g.setColor(Color.GREEN);
		}
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));
		g.drawImage(img, (int) getX(), (int) getY(), null);
		g2d.setComposite(makeTransparent(1));
	}

	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));

	}
}
