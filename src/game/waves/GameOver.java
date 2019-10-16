package game.waves;

import game.Client;
import game.GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The game over screen
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class GameOver extends GameState {
	private int timer;
	private Color retryColor;
	private Waves game;
	private Client client;
	private String trueHighScore;
	public GameOver(Client c, Waves waves) {
        client = c;
        game = waves;
        timer = 90;
        this.retryColor = Color.white;
        //This is the high score from the text file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/HighScores.txt"));
            trueHighScore = reader.readLine();
            //draw the high score text string
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            //the background image is the same as the menu background S
        }
	}

	public void tick() {
		flash();
	}

	public void render(Graphics g) {
		//render the background image
		g.drawImage(game.getHandler().getTheme().get(this), 0, 0, (int)game.getHandler().getGameDimension().getWidth(), (int)game.getHandler().getGameDimension().getHeight(), null);

        g.setColor(Color.white);
		Font font = new Font("Amoebic", 1, 100);
		//Game Over Font
		g.setFont(font);
		g.drawString("Game Over", (int)game.getHandler().getGameDimension().getWidth() / 2 - 500 / 2, (int)game.getHandler().getGameDimension().getHeight() / 2 - 150);
		//The level the player died on

        Font font2 = new Font("Amoebic", 1, 60);
        g.setFont(font2);
		g.drawString("Level: " + game.getHUD().getLevel(), 100, 500);
		//Get the high score of the PLAYER
		g.drawString("Your Score: " + game.getHUD().getScore(), (int)game.getHandler().getGameDimension().getWidth() / 2 - 440 / 2, 500);
		g.drawString("High Score:" + trueHighScore, 1400, 500);
		//g.drawString(text, Game.WIDTH / 2 - getTextWidth(font2, text) / 2, Game.HEIGHT / 2 + 50);
		//Text flashing
		g.setColor(this.retryColor);
		g.drawString("Click anywhere to play again", (int)game.getHandler().getGameDimension().getWidth() / 2 - 780 / 2, (int)game.getHandler().getGameDimension().getHeight() / 2 + 150);
	}

    //This really isn't "flashing" so much as it's changing the color of the text to black then white
	private void flash() {
		timer--;
		if (timer == 45) {
			this.retryColor = Color.black;
		} else if (timer == 0) {
			this.retryColor = Color.white;
			timer = 90;
		}
	}

    @Override
    public void mousePressed(MouseEvent e) {
        game.resetMode();
        client.setState(client.getMenu());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
