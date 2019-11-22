package game.menu;

import game.GameLevel;
import game.GameWindow;
import util.Random;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The game over screen
 *
 * @author Brandon Loehle 5/30/16
 */

public class GameOver extends GameLevel.Unending {
    private Random.RandomDifferentElement<Color> retryColor = getRandom().new RandomDifferentElement<>(Color.black, Color.white);
    private Color color;
    private AtomicInteger highscore;

    public GameOver(Menu m) {
        super(m);
        highscore = new AtomicInteger();
    }

    @Override
    public void start() {
        getEntities().clear();
        getNonentities().clear();
        new Thread(() -> { // Retrieves and saves the high score
            try (BufferedReader in = new BufferedReader(new FileReader("src/HighScores.txt"))) {
                highscore.set(Optional.ofNullable(in.readLine()).map(Integer::parseInt).orElse(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedWriter out = new BufferedWriter(new FileWriter("src/HighScores.txt"))) {
                if (getScore() >= highscore.get()) {
                    out.write(Integer.toString(getScore()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void tick() {
        super.tick();
        setScore(getScore() - 1);
        if (getCurrentTick() % 30 == 0) {
            color = retryColor.get();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 50), "Game Over", getDimension().width / 2, getDimension().height / 2 - 400);
        Font f = new Font("Amoebic", Font.BOLD, 60);
        GameWindow.drawStringCentered(g, f, "Level: " + getNumber(), getDimension().width / 2, getDimension().height / 2 - 200);
        GameWindow.drawStringCentered(g, f, "Your Score: " + getScore(), getDimension().width / 2, getDimension().height / 2);
        if (getScore() >= highscore.get()) {
            GameWindow.drawStringCentered(g, f, "New High Score!", getDimension().width / 2, getDimension().height / 2 + 200);
        } else {
            GameWindow.drawStringCentered(g, f, "High Score: " + highscore.get(), getDimension().width / 2, getDimension().height / 2 + 200);
        }
        g.setColor(color);
        GameWindow.drawStringCentered(g, f, "Click anywhere to return to the menu.", getDimension().width / 2, getDimension().height / 2 + 400);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        getPlayers().clear();
        end();
    }
}
