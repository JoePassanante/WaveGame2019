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
    //---------------------------------------------------------------------------------
    public static boolean devMode = false;//true - enable cheats and debug info | false - do not
    //---------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private GameMode currentGame;
    private AffineTransform screenTransform;

	/**
	 * Display the game window
	 */
	public Client() {
        super("Wave Game");
        currentGame = new Waves(screenSize);
		addKeyListener(currentGame);
		addMouseListener(currentGame);
		AudioUtil.closeGameClip();
		AudioUtil.playMenuClip(true, false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Set fullscreen
        if (System.getProperty("os.name").toLowerCase().contains("mac")) { //If user is on macOS
            try {
                com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
                com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
            } catch (Exception e) {
                System.err.println("Failed to load apple extensions package");
            }
        } else {
            setUndecorated(true);
        }
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(screenSize);
        pack();
        setLocationRelativeTo(null);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Main game loop that handles ticks. 
	 * Ticks 60 times a second.
	 */
	public void run() {
		requestFocus();
        final double fps = 60, nano = 1_000_000_000, frame=nano/fps;
		long tick = 0, tock = System.nanoTime(), delta = 0;
		while (true) {
		    tick = tock;
            while (delta > frame) {
				tick(); // 60 times a second, objects are being updated
				delta -= frame;
			}
            /*
             * BufferStrategies are used to prevent screen tearing. In other words, this
             * allows for all objects to be redrawn at the same time, and not individually
             */
            if (getWidth() > 0 && getHeight() > 0) {
                BufferStrategy bs = getBufferStrategy();
                if (bs == null) {
                    createBufferStrategy(3);
                }
                else {
                    render(bs.getDrawGraphics());// 60 times a second, objects are being drawn
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

	/**
	 * Constantly drawing to the many buffer screens of each entity requiring the
	 * Graphics objects (entities, screens, HUD's, etc).
	 */
	public void render(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        AffineTransform old = g.getTransform();

        g.clearRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());

        double scaleFactor = Math.min(
                getWidth()/screenSize.getWidth(),
                getHeight()/screenSize.getHeight()
        );

        g.translate(getWidth()/2,getHeight()/2);
        g.scale(scaleFactor, scaleFactor);
        g.translate(-screenSize.getWidth()/2,-screenSize.getHeight()/2);

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
            ((Graphics2D)getGraphics()).getTransform().inverseTransform(e.getPoint(), p);
            super.processMouseEvent(new MouseEvent(
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
