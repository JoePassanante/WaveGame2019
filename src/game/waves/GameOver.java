package game.waves;

import game.GameLevel;
import game.GameWindow;
import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;

/**
 * The game over screen
 * 
 * @author Brandon Loehle 5/30/16
 *
 */

public class GameOver extends GameLevel {
	private Random.RandomDifferentElement<Color> retryColor = getRandom().new RandomDifferentElement<>(Color.black, Color.white);
	private String trueHighScore;
	public GameOver(Waves waves) {
        super(waves);
        new Thread( () -> {
            try { // Saves Highscore
                File set = new File("src/HighScores.txt");
                BufferedWriter out = new BufferedWriter(new FileWriter(set));
                out.write(Integer.toString(getScore()));
                out.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }).start();

        //This is the high score from the text file
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/HighScores.txt"));
            trueHighScore = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void tick() {

	}

	public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 50),"Game Over", getDimension().width/2, getDimension().height/2 - 400);
        Font f = new Font("Amoebic", Font.BOLD, 60);
        GameWindow.drawStringCentered(g, f,"Level: " + (getNumber() - 1), getDimension().width/2, getDimension().height/2 - 200);
        GameWindow.drawStringCentered(g, f,"Your Score: " + getScore(), getDimension().width/2, getDimension().height/2);
        GameWindow.drawStringCentered(g, f,"High Score: " + trueHighScore, getDimension().width/2, getDimension().height/2 + 200);
		g.setColor(retryColor.get());
        GameWindow.drawStringCentered(g, f,"Click anywhere to return to the menu.", getDimension().width/2, getDimension().height/2 + 400);
	}

    @Override
    public void mousePressed(MouseEvent e) {
	    getPlayers().clear();
	    getState().pop();
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
