package mainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Player object class w/ collision
 * 
 * @author Brandon Loehle 5/30/16
 * @author William Joseph 12/9/18
 * @author Aaron Paterson 10/1/19
 */

public class Player extends GameObject {
    private Waves game;
	private double damage;
	public static int playerSpeed = 10;
	public static Color playerColor = Color.WHITE;

	public Player(double x, double y, Waves waves) {
		super(x, y, 32, 32, waves.getHandler());
		game = waves;
		this.damage = 2;
		//Player Width and Height change the size of the image, use the same number for both for scaling
	}

	@Override
	public void tick() { // Heartbeat of the Player class
		this.x += velX;
		this.y += velY;
		x = Client.clamp(x, 0, game.getHandler().getGameDimension().getWidth()  - width);
		y = Client.clamp(y, 0, game.getHandler().getGameDimension().getHeight() - height);
        game.getHandler().addObject(new Trail(x, y, playerColor, (int)width, (int)height, 0.05, game.getHandler()));
		playerColor = Color.white; //player trail code
		collision();
		checkIfDead();
	}

	public void checkIfDead() {
		if (game.getHUD().health <= 0) {// player is dead, game over!
			if (game.getHUD().getExtraLives() == 0) {
				AudioUtil.closeGameClip();
				AudioUtil.stopCurrentClip(); //Clears audio for game over sound
				AudioUtil.playClip("../gameSound/gameover.wav", false);

				new Thread( () -> {
                    try { // Saves Highscore
                        File set = new File("src/HighScores.txt");
                        BufferedWriter out = new BufferedWriter(new FileWriter(set));
                        out.write(Integer.toString(game.getHandler().getHighScore()));
                        out.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }).start();

                game.resetMode();
				game.setState(game.getGameOver());
                game.getHandler().clearPlayer();
			}
			else if (game.getHUD().getExtraLives() > 0) {//Player has extra life
                game.getHUD().setExtraLives(game.getHUD().getExtraLives() - 1);
                game.getHUD().restoreHealth();
			}
		}
	}

	/**
	 * Checks for collisions with all of the enemies, and handles it accordingly
	 */
	public void collision() {
		try{
            game.getHUD().updateScoreColor(Color.white);
		for (int i = 0; i < game.getHandler().size(); i++) {
            GameObject tempObject = game.getHandler().get(i);

            if (tempObject.getClass().getName().contains("Enemy")) {//tempObject is an enemy
                // collision code
                if (getBounds().intersects(tempObject.getBounds())) {//Player, Enemy Collision
                    AudioUtil.playClip("../gameSound/explosion.wav", false);
                    game.getHUD().health -= damage;
                    playerColor = Color.RED;
                    game.getHUD().updateScoreColor(Color.red);
                }
            }
            if (tempObject instanceof EnemyBoss) {
                //Gives players safety window to move from boss restricted region
                if (this.y <= 138 && Math.hypot(tempObject.getVelX(),tempObject.getVelY()) < 1E-6 ) {
                    AudioUtil.playClip("../gameSound/damaged.wav", false);
                    game.getHUD().health -= 2;
                    game.getHUD().updateScoreColor(Color.red);
                }
            }
        }

        for(int i = 0; i < game.getHandler().getPickups().size(); i++) {
            //if player collides with powerup, trigger what that powerup does, remove the powerup and play the collision sound
            if(getBounds().intersects(game.getHandler().getPickups().get(i).getBounds())) {
                GameObject tempObject = game.getHandler().getPickups().remove(i);
                if (tempObject instanceof PickupHealth) {
                    game.getHUD().restoreHealth();
                    AudioUtil.playClip("../gameSound/powerup.wav", false);
                }
                if (tempObject instanceof PickupSize) {
                    if (width > 3) {
                        width /= 1.2;
                        height /= 1.2;
                    } else {
                        game.getHUD().setScore(game.getHUD().getScore() + 1000);
                    }
                    AudioUtil.playClip("../gameSound/powerup.wav", false);
                }

                if (tempObject instanceof PickupLife) {
                    game.getHUD().setExtraLives(game.getHUD().getExtraLives() + 1);
                    AudioUtil.playClip("../gameSound/1up.wav", false);
                }

                if (tempObject instanceof PickupScore) {
                    game.getHUD().setScore(game.getHUD().getScore() + 1000);
                    AudioUtil.playClip("../gameSound/coin.wav", false);
                }

                if (tempObject instanceof PickupFreeze) {
                    AudioUtil.playClip("../gameSound/freeze1.wav", false);
                    game.getHandler().timer = 900;
                }
            }
        }
		}catch(NullPointerException e){//Catches object glitches/errors
			System.err.println("Object removed while checking object");
		}
	}
	
	@Override
	public void render(Graphics g) {//renders player
		g.setColor(playerColor);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		//g.drawImage(img, (int) this.x, (int) this.y, playerWidth, playerHeight, null);
	}

	public void setDamage(double d) {//set players damage
		this.damage = d;
	}
	
	public void setPlayerSize(int size) {//changes player size
		width = size;
		height = size;
	}

	public double getDamage() {//get damage done
		return damage;
	}
}
