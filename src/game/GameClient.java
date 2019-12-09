package game;

import game.menu.Menu;
import util.LambdaException;
import util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/9/19
 */

public class GameClient extends GameLevel implements Runnable {
    public static boolean devMode = false; // enable cheats and windowed mode

    private GameWindow window; // swing JFrame that renders the game
    private Clip music; // clip that plays the background music
    private final long frame; // length in ticks of one frame
    private long tick, tock, delta; // the beginning, end, and duration of each frame

    private GameClient() { // proxy game level, passes loop and swing events to top of level stack
        super(
                new ArrayList<>(), // handler list
                new Stack<>(), // level stack
                new Random(), // in house rng
                new Dimension(1920,1080), // game dimensions
                new Theme("common", null), // shared theme assets
                new ArrayList<>(), // player list
                -2, // level number
                -1, // no maximum tick
                0, // no score
                true // play background music
        );
        // load shared theme assets
        System.out.println("Loading common...");
        getTheme().run();
        getState().push(new Menu(this));
        // instantiate window and background music
        window = new GameWindow();
        music = LambdaException.wraps(AudioSystem::getClip).get();
        // listen to swing events
        window.addKeyListener(this);
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
        // might not be necessary
        window.requestFocus();
        // initial state of game loop
        frame = 1_000_000_000 / 60; // one billion nanoseconds per sixty ticks
        tick = 0;
        tock = System.nanoTime();
        delta = frame;
    }

    // proxy methods passed off to the top of the state stack
    @Override public void tick() {
        getState().peek().tick();
    }
    @Override public void render(Clip c, int i) {
        getState().peek().render(c, i);
    }
    @Override public void render(Graphics g) {
        getState().peek().render(g);
    }
    @Override public void keyPressed(KeyEvent e) {
        getState().peek().keyPressed(e);
    }
    @Override public void keyReleased(KeyEvent e) {
        getState().peek().keyReleased(e);
    }
    @Override public void mousePressed(MouseEvent e) {
        getState().peek().mousePressed(e);
    }
    @Override public void mouseReleased(MouseEvent e) {
        getState().peek().mouseReleased(e);
    }
    @Override public void mouseMoved(MouseEvent e) {
        getState().peek().mouseMoved(e);
    }

    @Override
    public void run() { // runs and renders the game
        tick = tock;

        while (delta >= frame) { // ticking at most sixty times a second
            window.setFocusable(true);
            tick();
            delta -= frame;
        }
        window.draw(this); // drawing as frequently as possible
        render(music, 0);

        tock = System.nanoTime();
        delta += tock - tick;
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        while(!client.getState().empty()) { // slick way to exit, sadly System.exit works better
            try {
                SwingUtilities.invokeAndWait(client); // referencing objects on a different thread than swing is rendering is UB
            }
            catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}


