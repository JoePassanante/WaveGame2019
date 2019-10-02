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
    private Theme theme;
    public void setTheme(Theme t) {
        theme = t;
    }
    public Theme getTheme() {
        return theme;
    }

    private int highscore = 0;
    public int getHighScore() {
        return highscore;
    }
    public void setHighScore(int h) {
        highscore = h;
    }

    ArrayList<GameObject> object = new ArrayList<>();
    ArrayList<GameObject> pickups = new ArrayList<>();
    public int timer = 0; // freeze ability timer

    private Dimension gameDimension;
    public Dimension getGameDimension() {
        return gameDimension;
    }

    public Handler(Dimension gd) {
        gameDimension = gd;

        try {
            File inFile = new File("src/HighScores.txt");
            Scanner fileInput = new Scanner(inFile);
            highscore = Integer.parseInt(fileInput.next());
            fileInput.close();
            System.out.print("success");
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        }
    }

    /**
     * Updates each entity in the game by looping through each ArrayList and calling
     * the tick() function on each object
     */

    public void tick() {
        for (int i = 0; i < object.size(); i++) {
            GameObject tempObject = object.get(i);
            if (timer <= 0 || tempObject.getId().getDifficuty() < 0) { // we don't want these to ever be frozen by the Screen Freeze ability
                tempObject.tick();
            } else {
                timer -= 1;
            }
            checkForBounds(tempObject);
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

    private void checkForBounds(GameObject i) {
        if (i.x >= gameDimension.getWidth() * 3 || i.x <= 0 - (gameDimension.getWidth() * 2) ||
            i.y >= gameDimension.getHeight() * 3 || i.y <= 0 - (gameDimension.getHeight() * 2)) { // 100% greater/smaller then game width/height.
            System.out.println("Object out of bounds: " + i.getId());
            object.remove(i);
        }
    }
}
