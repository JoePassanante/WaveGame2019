package game.enemy;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * The last boss in the game, shown in a 3x3 grid of 9 instances of BossEye
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class BossEye extends GameEntity {
	private float alpha = 0;
	private double life = 0.005;
	private int tempCounter = 0;
	private int timer;
	private int spawnOrder = 1;// make them begin moving from left to right, top to bottom
	private int placement;// where they are in the 3x3 grid of eyes
	private double speed;
	private double[] speedTypes = { -5, -6, -7, -8, -9 };
	private GameEntity player;
	private double health;

	public BossEye(double x, double y, GameLevel level, int placement) {
		super(x, y, 300, 300, level);
		this.speed = speedTypes[getLevel().getRandom().nextInt(4)];
		this.placement = placement;
		this.timer = 400;
	}

    @Override
    public void collide(Player p) {
        p.damage(2);
    }

    public void tick() {
	    super.tick();

		if (tempCounter == 0) {
			if (alpha < 0.995) {// this handles each eye fading in to the game
				alpha += life + 0.001;
			} else {
				tempCounter++;
				for (int i = 0; i < getLevel().getPlayers().size(); i++) {
                    this.player = getLevel().getPlayers().get(i);
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
            double
                diffX = getPosX() - player.getPosX(),
			    diffY = getPosY() - player.getPosY(),
			    distance = Math.hypot(diffX,diffY);
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
		super.render(g);
		g2d.setComposite(makeTransparent(1));
	}

	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));

	}
}
