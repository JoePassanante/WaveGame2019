package game;

import game.waves.Waves;
import util.Random;

import game.menu.Menu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game class. This class is the driver class and it follows the Holder
 * pattern. It houses references too ALL of the components of the game
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/9/19
 */

public class Client extends GameMode implements Runnable {
    public static boolean devMode = false; // true - enable cheats and debug info | false - do not

    private Handler handler;
    public Handler getHandler() {
        return handler;
    }
    private List<Player> players;
    public List<Player> getPlayers() {
        return players;
    }
    private AffineTransform screenSpace; // The graphical transformation of this JFrame

    private class GameWindow extends JFrame {
        public GameWindow() {
            super("Wave Game");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Set fullscreen
            if (System.getProperty("os.name").toLowerCase().contains("mac")) { //If user is on macOS
                setResizable(true);
                try {
                    com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
                    com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
                } catch (Exception e) {
                    System.err.println("Failed to load apple extensions package");
                }
            } else {
                setResizable(devMode);
                setUndecorated(!devMode);
            }

            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setPreferredSize(handler.getGameDimension());
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
        /**
         * Inverts transformation of this JFrame to process mouse events in game space
         */
        @Override
        protected void processMouseEvent(MouseEvent e) {
            try {
                Point2D p = new Point();
                screenSpace.inverseTransform(e.getPoint(), p);
                super.processMouseEvent( new MouseEvent(
                    e.getComponent(),
                    e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    (int)p.getX(),
                    (int)p.getY(),
                    e.getClickCount(),
                    e.isPopupTrigger()
                ));
            }
            catch(NoninvertibleTransformException nite) {
                nite.printStackTrace();
            }
        }
    }

    private GameWindow window;

    private GameState menu;
    private GameState waves;
    public GameState getMenu() {
        return menu;
    }
    public GameState getWaves() {
        return waves;
    }

	public Client() {
        handler = new Handler(new Random(), new Dimension(1920,1080));
        screenSpace = new AffineTransform();
        window = new GameWindow();
        window.addMouseListener(this);
        window.addKeyListener(this);
        menu = new Menu(this);
        waves = new Waves(this);
        players = new ArrayList<>();
        setState(menu);
    }

	public void run() {
        window.requestFocus();

        final long frame = 1_000_000_000 / 60; // time per ticks, here it is one billion nanoseconds per sixty ticks
        long tick = 0, tock = System.nanoTime(), delta = 0;

        while (tock >= tick) {
            tick = tock;

            while (delta >= frame) {
            	window.setFocusable(true);
                tick(); // Animatables are ticking at most sixty times a second
                delta -= frame;
            }

            if (window.getWidth() > 0 && window.getHeight() > 0) {
                BufferStrategy bs = window.getBufferStrategy(); // Render each Animatable then draw them all at once
                if (bs == null) {
                    window.createBufferStrategy(3);
                    tick = System.nanoTime();
                }
                else {
                    render(bs.getDrawGraphics()); // Frames are being drawn as frequently as possible
                    bs.show();
                }
            }

            tock = System.nanoTime();
            delta += tock-tick;
        }
	}


	public void render(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        AffineTransform old = g.getTransform();

        g.clearRect(0, 0, window.getWidth(), window.getHeight());

        double scaleFactor = Math.min(
            window.getWidth()/handler.getGameDimension().getWidth(),
            window.getHeight()/handler.getGameDimension().getHeight()
        );

        g.translate(window.getWidth()/2,window.getHeight()/2);
        g.scale(scaleFactor, scaleFactor);
        g.translate(-handler.getGameDimension().getWidth()/2,-handler.getGameDimension().getHeight()/2);

        screenSpace = g.getTransform();

        super.render(g);

		g.dispose();
		g.setTransform(old);
	}

	public static void main(String[] args) {
        new Client().run();
	}
}
