package mainGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Player object class w/ collision
 * 
 * @author Brandon Loehle 5/30/16
 * @Revised William Joseph 12/9/18
 */

public class Player extends GameObject {

	Random r = new Random();
	private Game game;
	private double damage;
	protected int playerWidth, playerHeight;
	public static int playerSpeed = 10;
	public static Image img;
	public static Color playerColor = Color.WHITE;

	public Player(double x, double y, ID id, Game game) {
		super(x, y, id);
		this.game = game;
		this.damage = 2;
		//Player Width and Height change the size of the image, use the same number for both for scaling
		playerWidth = 32;
		playerHeight = 32;
		
		if (img == null) { //Player Sprite
			try {
				img = ImageIO.read(new File("src/images/playership.png"));
			} catch (Exception e){
				e.printStackTrace();
			}
			}
		

	}

	@Override
	public void tick() {//Heartbeat of the Player class
		this.x += velX;
		this.y += velY;
		x = Game.clamp(x, 0, game.getHandler().getGameDimension().getWidth()  - playerWidth);
		y = Game.clamp(y, 0, game.getHandler().getGameDimension().getHeight() - playerHeight);
        game.getHandler().addObject(new Trail(x, y, ID.Trail, playerColor, playerWidth, playerHeight, 0.05, game.getHandler()));
		playerColor = Color.white; //player trail code
		collision();
		checkIfDead();
		
	}

	public void checkIfDead() {
		if (game.getHUD().health <= 0) {// player is dead, game over!
			if (game.getHUD().getExtraLives() == 0) {
				game.getCurrentGame().resetMode();
				AudioUtil.closeGameClip();
				AudioUtil.stopCurrentClip(); //Clears audio for game over sound
				AudioUtil.playClip("../gameSound/gameover.wav", false);
				
				try{//Saves Highscore
						File set = new File("src/HighScores.txt");
						BufferedWriter out = new BufferedWriter(new FileWriter(set));
						out.write(Integer.toString(game.getHandler().getHighScore()));
						out.close();				
					}
					catch (IOException e) {
						System.out.println(e);
						System.exit(1);
					}
				game.setGameState(game.getGameOver());

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
		for (int i = 0; i < game.getHandler().object.size(); i++) {
            GameObject tempObject = game.getHandler().object.get(i);

            if (Enemy.class.isInstance(tempObject)){//tempObject is an enemy

                // collision code
                if (getBounds().intersects(tempObject.getBounds())) {//Player, Enemy Collision
                    AudioUtil.playClip("../gameSound/explosion.wav", false);
                    game.getHUD().health -= damage;
                    playerColor = Color.RED;
                    game.getHUD().updateScoreColor(Color.red);
                }
            }
            if (tempObject.getId() == ID.EnemyBoss) {
                //Gives players safety window to move from boss restricted region
                if (this.y <= 138 && tempObject.isMoving) {
                    AudioUtil.playClip("../gameSound/damaged.wav", false);
                    game.getHUD().health -= 2;
                    game.getHUD().updateScoreColor(Color.red);
                }
            }
            //if player collides with powerup, trigger what that powerup does, remove the powerup and play the collision sound
            if (Pickup.class.isInstance(tempObject)) {//Power ups pickup
                if (tempObject.getId() == ID.PickupHealth && (getBounds().intersects(tempObject.getBounds()))) {
                    game.getHUD().restoreHealth();
                    game.getHandler().removeObject(tempObject);
                    AudioUtil.playClip("../gameSound/powerup.wav", false);
                }

                if (tempObject.getId() == ID.PickupSize && (getBounds().intersects(tempObject.getBounds()))) {
                    if (playerWidth>3) {
                    playerWidth/=1.2;
                    playerHeight/=1.2;}else {
                        game.getHUD().setScore(game.getHUD().getScore()+1000);
                    }
                    game.getHandler().removeObject(tempObject);
                    AudioUtil.playClip("../gameSound/powerup.wav", false);
                }

                if (tempObject.getId() == ID.PickupLife && (getBounds().intersects(tempObject.getBounds()))) {
                    game.getHUD().setExtraLives(game.getHUD().getExtraLives()+1);
                    game.getHandler().removeObject(tempObject);
                    AudioUtil.playClip("../gameSound/1up.wav", false);
                }

                if (tempObject.getId() == ID.PickupScore && (getBounds().intersects(tempObject.getBounds()))) {
                    game.getHUD().setScore(game.getHUD().getScore()+1000);
                    game.getHandler().removeObject(tempObject);
                    AudioUtil.playClip("../gameSound/coin.wav", false);
                }

                if (tempObject.getId() == ID.PickupFreeze && (getBounds().intersects(tempObject.getBounds()))) {
                    AudioUtil.playClip("../gameSound/freeze1.wav", false);
                    game.getHandler().timer = 900;
                    game.getHandler().removeObject(tempObject);
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
		g.fillRect((int) x, (int) y, playerWidth, playerHeight);
		//g.drawImage(img, (int) this.x, (int) this.y, playerWidth, playerHeight, null);
	}

	@Override
	public Rectangle getBounds() {//creates hitbox
		return new Rectangle((int) this.x, (int) this.y, playerWidth,playerHeight);
	}
	
	public void setDamage(double d) {//set players damage
		this.damage = d;
	}
	
	public void setPlayerSize(int size) {//changes player size
		this.playerWidth = size;
		this.playerHeight = size;
	}

	public int getPlayerWidth() {//get player width
		return playerWidth;
	}
	public int getPlayerHeight(){//get player width
		return this.playerHeight;
	}

	public double getDamage() {//get damage done
		return damage;
	}
	
}
