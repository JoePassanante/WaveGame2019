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

public class Game extends JFrame implements Runnable, Animatable {
    //---------------------------------------------------------------------------------
    public static boolean devMode = false;//true - enable cheats and debug info | false - do not
    //---------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private boolean running = false;
	private HUD hud;
    public HUD getHUD() {
        return hud;
    }

    private MouseListener mouseListener;

    private GameState menu;
	public GameState getMenu() {
	    return menu;
    }

	private GameState gameOver;
	public GameState getGameOver() {
	    return gameOver;
    }

	private GameState upgradeScreen;
	public GameState getUpgradeScreen() {
	    return upgradeScreen;
    }

	private Upgrades upgrades;
	public Upgrades getUpgrades() {
	    return upgrades;
    }

	private Handler handler;
	public Handler getHandler() {
	    return handler;
    }

	private Player player;
	public Player getPlayer() {
	    return player;
    }

    private GameMode currentGame;
	public GameMode getCurrentGame() {
	    return currentGame;
    }

	private GameState gameState;
    public GameState getGameState() {
	    return gameState;
    }
    public void setGameState(GameState gs) {
        gameState = gs;
    }

	public GAME_AUDIO gameCurrentClip = GAME_AUDIO.Menu;

    private int FPS = 0;

	private boolean paused = false;
	public void setPaused(boolean p) {
	    paused = p;
    }

	private boolean africa = false;

	/**
	 * Used to switch between each of the screens shown to the user
	 */
	
	public enum GAME_AUDIO {
		Menu, Game, None
	}

	/**
	 * Initialize the core mechanics of the game
	 */
	public Game() {
        super("Wave Game");

        handler = new Handler(screenSize);
		handler.updateSprites();
		hud = new HUD(this);
		menu = new Menu(this);

		gameState = menu;
		
		player = new Player(screenSize.getWidth() / 2 - 32, screenSize.getHeight() / 2 - 32, ID.Player, this);
		upgradeScreen = new UpgradeScreen(this);
		upgrades = new Upgrades(this);
		gameOver = new GameOver(this);
		mouseListener = new MouseListener(this);
        currentGame = new Waves(this);
		addKeyListener(new KeyInput(this));
		addMouseListener(mouseListener);
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
        start();
        setLocationRelativeTo(null);
	}

	/**
	 * Starts the game loop.
	 */
	public void start() {
		running = true;
		run();
	}
	/**
	 * Stops the game loop.
	 */
	public void stop() {
			running = false;
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
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();// 60 times a second, objects are being updated
				delta--;
			}
			if (running) {
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
            }
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				FPS = frames;
				frames = 0;
			}
		}
		stop();

	} 
	/**
	 * Main tick function that calls it for all operating classes in the game. 
	 * Ticks classes based on current game state. 
	 */
	public void tick() {
		if (this.paused) {return;}
		
		handler.tick();// handler must always be ticked in order to draw all entities.
        gameState.tick();
		if (gameState == menu) {// user is on menu, update the menu items
			if (this.gameCurrentClip != GAME_AUDIO.Menu) {
				this.gameCurrentClip = GAME_AUDIO.Menu;
				AudioUtil.closeGameClip();
				AudioUtil.playMenuClip(true, false);
			}
		}
		if (gameState == currentGame) {// game is running
			if (this.gameCurrentClip != GAME_AUDIO.Game) {
				this.gameCurrentClip = GAME_AUDIO.Game;
				AudioUtil.closeMenuClip();
				AudioUtil.playGameClip(true);
			}
			hud.tick();
		}
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

        mouseListener.setSpace(g.getTransform());
		
		g.setColor(Color.black);
		g.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		gameState.render(g);
		if(gameState == currentGame) {
		    hud.render(g);
        }
		if(devMode){
			//debug menu
			Font font2 = new Font("Amoebic", 1, 25);
			g.setColor(Color.white);
			g.setFont(font2);
			g.drawString("Objects: " + handler.getNumObjects(), WIDTH-300, HEIGHT-200);
			g.drawString("Pickups: " + handler.getNumPickUps(), WIDTH-300, HEIGHT-150);
			g.drawString("FPS(?): " + this.FPS, WIDTH-300, HEIGHT-100);
			g.drawString("Trails: " + handler.getTrails() + " / " + handler.getNumObjects(), WIDTH-300, HEIGHT-50);
		}
		handler.render(g);

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
			new Game();	
	}

	public void toggleMenuMusic() {
	    // Toggle menu theme between Space Jam and Africa
		africa = !africa;
		// Restart menu music
		AudioUtil.closeMenuClip();
		AudioUtil.playMenuClip(true, africa);
	}
}
