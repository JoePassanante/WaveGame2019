package mainGame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

	/**
	 * Display the game window
	 */
	public Client() {
        super("Wave Game");
        currentGame = new Waves(screenSize);
		addKeyListener(currentGame.getKeyInput());
		addMouseListener(currentGame.getMouseInput());
		AudioUtil.closeGameClip();
		AudioUtil.playMenuClip(true, false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        // Set fullscreen
        if (System.getProperty("os.name").toLowerCase().contains("mac")) { //If user is on macOS
            try {
                com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
                com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
            } catch (Exception e) {
                System.err.println("Failed to load apple extensions package");
            }
        } else {
            setUndecorated(false);
        }
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(screenSize);
        pack();
        setLocationRelativeTo(null);
        run();
	}

	/**
	 * Main game loop that handles ticks. 
	 * Ticks 60 times a second.
	 */
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();// 60 times a second, objects are being updated
				delta--;
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
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
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

        double scaleFactor = Math.min(
                getWidth()/screenSize.getWidth(),
                getHeight()/screenSize.getHeight()
        );

        g.translate(getWidth()/2,getHeight()/2);
        g.scale(scaleFactor, scaleFactor);
        g.translate(-screenSize.getWidth()/2,-screenSize.getHeight()/2);

		g.setColor(Color.black);
		g.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
        currentGame.render(g);

		g.dispose();
		g.setTransform(old);
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
			new Client();
	}
}
