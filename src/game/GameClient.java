package game;

import game.menu.Menu;
import util.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

/**
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/9/19
 */

public class GameClient extends GameState implements Runnable {
    public static boolean devMode = false; // enable cheats and debug info

    private GameWindow window;
    private final long frame;
    private long tick, tock, delta;

    private GameClient() {
        super(new Stack<>());

        Theme common = new Theme("common", null); // TODO: thread theme loading again
        common.initialize();

        getState().push(new Menu(getState(), new Random(), new Dimension(1920, 1080), common));

        window = new GameWindow();

        window.addMouseListener(this);
        window.addKeyListener(this);

        window.requestFocus();

        frame = 1_000_000_000 / 60; // time per tick, here it is one billion nanoseconds per sixty ticks
        tick = 0;
        tock = System.nanoTime();
        delta = frame;
    }

    @Override public void tick() {
        getState().peek().tick();
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

    @Override
	public void run() {
        tick = tock;

        while (delta >= frame) { // ticking at most sixty times a second
            window.setFocusable(true);
            tick();
            delta -= frame;
        }
        window.draw(this); // drawing as frequently as possible

        tock = System.nanoTime();
        delta += tock - tick;
    }

	public static void main(String[] args) {
        GameClient client = new GameClient();
        while(!client.getState().empty()) {
            try {
                SwingUtilities.invokeAndWait(client);
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
