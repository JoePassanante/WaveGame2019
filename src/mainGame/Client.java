package mainGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

/**
 * Main game class. This class is the driver class and it follows the Holder
 * pattern. It houses references too ALL of the components of the game
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/9/19
 */

public class Client extends JFrame implements Runnable, Animatable {
    private static final long serialVersionUID = 1L;

    public static boolean devMode = true; // true - enable cheats and debug info | false - do not

	private Dimension gameSize;
    private GameMode currentGame;

	/**
	 * Display the game window
	 */
	public Client() {
        super("Wave Game");
        gameSize = new Dimension(1920,1080); // Toolkit.getDefaultToolkit().getScreenSize();
        currentGame = new Waves(gameSize);
		addKeyListener(currentGame);
		addMouseListener(currentGame);
		AudioUtil.closeGameClip();
		AudioUtil.playMenuClip(true, false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(devMode);
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
            setUndecorated(!devMode);
        }
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(gameSize);
        pack();
        setLocationRelativeTo(null);     
	}

	/**
	 * Main game loop that handles ticks. 
	 * Ticks 60 times a second.
	 */
	public void run() {
        requestFocus();

        final long frame = 1_000_000_000 / 60; // time per ticks, here it is one billion nanoseconds per sixty ticks
        long tick = 0, tock = System.nanoTime(), delta = 0;

        while (tock >= tick) {
            tick = tock;

            while (delta >= frame) {
            	setFocusable(true);
                tick(); // Objects are being updated at most sixty times a second
                delta -= frame;
            }

            if (getWidth() > 0 && getHeight() > 0) {
                BufferStrategy bs = getBufferStrategy(); // Draw entire frame all at once
                if (bs == null) {
                    createBufferStrategy(3);
                    tick = System.nanoTime();
                }
                else {
                    render(bs.getDrawGraphics()); // Objects are being drawn as frequently as possible
                    bs.show();
                }
            }

            tock = System.nanoTime();
            delta += tock-tick;
        }
	}
	/**
	 * Main tick function that calls it for all operating classes in the game.
	 * Ticks classes based on current game state.
	 */
	public void tick() {
        currentGame.tick();
	}

	private AffineTransform screenSpace; // The graphical transformation of this JFrame

	/**
	 * Constantly drawing to the many buffer screens of each entity requiring the
	 * Graphics objects (entities, screens, HUD's, etc).
	 */
	public void render(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        AffineTransform old = g.getTransform();

        g.clearRect(0, 0, (int)gameSize.getWidth(), (int)gameSize.getHeight());

        double scaleFactor = Math.min(
                getWidth()/gameSize.getWidth(),
                getHeight()/gameSize.getHeight()
        );

        g.translate(getWidth()/2,getHeight()/2);
        g.scale(scaleFactor, scaleFactor);
        g.translate(-gameSize.getWidth()/2,-gameSize.getHeight()/2);

        screenSpace = g.getTransform();

        currentGame.render(g);

		g.dispose();
		g.setTransform(old);
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
        } catch(NoninvertibleTransformException nite) {
            nite.printStackTrace();
        }
    }

    /**
	 * 
	 * Constantly checks bounds, makes sure players, enemies, and info doesn't leave
	 * screen
	 * 
	 * @param var
	 *            x or y location of entity
	 * @param min
	 *            minimum value still on the screen
	 * @param max
	 * 
	 *            maximum value still on the screen
	 * @return value of the new position (x or y)
	 */
	public static double clamp(double var, double min, double max) {
		if (var >= max)
			return var = max;
		else if (var <= min)
			return var = min;
		else
			return var;
	}

	public static void main(String[] args) {
			AudioUtil.playGameClip(true);
			new Client().run();
	}
}
