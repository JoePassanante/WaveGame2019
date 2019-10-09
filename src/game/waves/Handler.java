package game.waves;

import game.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class used for containing every instance of game.GameObject. These include all
 * enemies and players
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 10/30/17
 * @author Aaron Paterson 9/9/19
 */
public class Handler extends ArrayList<GameObject> implements Animatable {
    private ArrayList<Player> players;
    public ArrayList<Player> getPlayers() {
        return players;
    }

    private Theme theme;
    public void setTheme(Theme t) {
        theme = t;
    }
    public Theme getTheme() {
        return theme;
    }

    private int level = 0;
    public void setLevel(int l) {
        level = l;
    }
    public int getLevel() {
        return level;
    }

    private int highscore = 0;
    public int getHighScore() {
        return highscore;
    }
    public void setHighScore(int h) {
        highscore = h;
    }

    private ArrayList<GameObject> pickups = new ArrayList<>();
    public ArrayList<GameObject> getPickups() {
        return pickups;
    }
    public int timer = 0; // freeze ability timer

    private Dimension gameDimension;
    public Dimension getGameDimension() {
        return gameDimension;
    }

    public Handler(Dimension gd) {
        gameDimension = gd;
        players = new ArrayList<>();

        try {
            File inFile = new File("src/HighScores.txt");
            Scanner fileInput = new Scanner(inFile);
            highscore = Integer.parseInt(fileInput.next());
            fileInput.close();
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
    }

    /**
     * Updates each entity in the game by looping through each ArrayList and calling
     * the tick() function on each object
     */

    public void tick() {
        for (int i = 0; i < size(); i++) {
            GameObject tempObject = get(i);
            if (timer <= 0 || !tempObject.getClass().getName().contains("Enemy")) { // we don't want these to ever be frozen by the Screen Freeze ability
                tempObject.tick();
            } else {
                timer -= 1;
            }
        }
        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).tick();
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).tick();
        }
    }

    /**
     * Redraws each entity in the game by looping through each ArrayList and calling
     * the render() function on each object
     */

    @Override
    public void render(Graphics g) {
        for (int i = 0; i < size(); i++) {
            get(i).render(g);
        }
        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).render(g);
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).render(g);
        }
        if(Client.devMode){
            //debug menu
            Font font2 = new Font("Amoebic", 1, 25);
            g.setColor(Color.white);
            g.setFont(font2);
            g.drawString("Objects: " + size(), getGameDimension().width-300, getGameDimension().height-200);
            g.drawString("Pickups: " + pickups.size(), getGameDimension().width-300, getGameDimension().height-150);
//          g.drawString("FPS: " + fps, getGameDimension().width-300, getGameDimension().height-100);
//          g.drawString("Trails: " + getTrails(), getGameDimension().width-300, getGameDimension().height-50);
        }
    }

    public void pause() {
        timer = 1000;
    }
}
