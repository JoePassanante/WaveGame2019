package mainGame;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class used for containing every instance of GameObject. These include all
 * enemies and players
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 10/30/17
 * @author Aaron Paterson 9/9/19
 */
public class Handler implements Animatable {
    private int highscore = 0;

    private Theme theme;
    public void setTheme(Theme t) {
        theme = t;
    }
    public Theme getTheme() {
        return theme;
    }

    /**
     *
     * @return int value of highscore saved in local Highscores.txt
     */
    public int getHighScore() {
        return highscore;
    }

    public void setHighScore(int h) {
        highscore = h;
    }

    ArrayList<GameObject> object = new ArrayList<>();
    ArrayList<GameObject> pickups = new ArrayList<>();
    public int timer = 0;

    private Dimension gameDimension;

    public Dimension getGameDimension() {
        return gameDimension;
    }

    public Handler(Dimension gd) {
        gameDimension = gd;

        Scanner fileInput;
        File inFile = new File("src/HighScores.txt");

        try {
            fileInput = new Scanner(inFile);
            highscore = Integer.parseInt(fileInput.next());
            inFile.canWrite();
            fileInput.close();
            System.out.print("success");
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            //System.out.print("OOPS");
        }
    }

    /**
     * Updates each entity in the game by looping through each ArrayList and calling
     * the tick() function on each object
     */
    int trails = 0;

    public void tick() {
        trails = 0;
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            try {
                if (tempObject.getId() == ID.Trail) {
                    trails++;
                }
            } catch (java.lang.NullPointerException e) {
                e.getStackTrace();
                System.err.println("Trail item removed mid count. Do not be alarmed.");
            }
            if (tempObject.getId() == ID.Player || tempObject.getId() == ID.Trail
                    || tempObject.getId() == ID.EnemyBurstWarning) {// we don't want these to ever be frozen by the
                // Screen Freeze ability

                // Every GameObject has a tick method, so this effectively updates every single
                // object
                tempObject.tick();

            } else {
                timer--;
                if (timer <= 0) {// if Screen Freeze power-up is unlocked, enemy ID's will pause for the length
                    // of the timer, and not update
                    tempObject.tick();
                }
            }

            this.checkForBounds(tempObject);
        }
        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).tick();
        }

    }

    /**
     * Redraws each entity in the game by looping through each ArrayList and calling
     * the render() function on each object
     */
    @Override
    public void render(Graphics g) {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            tempObject.render(g);
        }
        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).render(g);
        }
    }

    public int getTrails() {
        return trails;
    }

    public void pause() {
        timer = 1000;
    }

    public void addObject(GameObject object) {
        this.object.add(object);
    }

    public void removeObject(GameObject object) {
        this.object.remove(object);
    }

    public void addPickup(GameObject object) {
        this.pickups.add(object);
    }

    public void removePickup(GameObject object) {
        this.pickups.remove(object);
    }

    /**
     * Clears all entities that have an ID of some sort of enemy
     */
    public void clearEnemies() {
        for(int i=object.size()-1; i>=0; i--) {
            if(object.get(i).getId() != ID.Player) {
                object.remove(i);
            }
        }
    }

    /**
     * Clears all entities that have an ID of player
     */
    public void clearPlayer() {
        for(int i=object.size()-1; i>=0; i--) {
            if(object.get(i).getId() == ID.Player) {
                object.remove(i);
            }
        }
    }

    /**
     * Clears all pickups
     */
    public void clearPickups() {
        pickups.clear();
    }

    public void checkForBounds(GameObject i) {
        try {
            if (i.x >= gameDimension.getWidth() * 3 || i.x <= 0 - (gameDimension.getWidth() * 2) ||
                i.y >= gameDimension.getHeight() * 3 || i.y <= 0 - (gameDimension.getHeight() * 2)) { // 100% greater/smaller then game width/height.
                System.out.println("Object out of bounds: " + i.getId());
                object.remove(i);
            }
        } catch (java.lang.NullPointerException e) {
            e.getStackTrace();
            System.err.println("Trail item removed mid count. Do not be alarmed.");
        }

    }

    public int getNumObjects() {
        return object.size();
    }

    public int getNumPickUps() {
        return pickups.size();
    }
}
