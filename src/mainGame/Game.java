package mainGame;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;
import java.awt.GraphicsEnvironment;

/**
 * Main game class. This class is the driver class and it follows the Holder
 * pattern. It houses references too ALL of the components of the game
 * 
 * @author Brandon Loehle 5/30/16
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 9/9/19
 */

public class Game extends JFrame {
    //---------------------------------------------------------------------------------
    protected boolean devMode = true;//true - display game information | false - do not
    //---------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private boolean running = false;
	private HUD hud;
	private Menu menu;
	private GameOver gameOver;
	private UpgradeScreen upgradeScreen;
	private MouseListener mouseListener;
	private Upgrades upgrades;
	private int FPS = 0;

	protected Handler handler;
	protected Player player;
	protected GameManager gm;

	public STATE gameState = STATE.Menu;
	public GAME_AUDIO gameCurrentClip = GAME_AUDIO.Menu;
	public boolean paused = false;

	public boolean africa = false;

	/**
	 * Used to switch between each of the screens shown to the user
	 */
	public enum STATE {
		Menu, Help, Game, GameOver, Upgrade
	};
	
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
		hud = new HUD(handler);
		menu = new Menu(this, this.handler, this.hud);
		
		player = new Player(screenSize.getWidth() / 2 - 32, screenSize.getHeight() / 2 - 32, ID.Player, handler, this.hud, this);
		upgradeScreen = new UpgradeScreen(this, handler, hud);
		upgrades = new Upgrades(this, this.handler, this.hud, this.upgradeScreen, this.player);
		gameOver = new GameOver(this, this.handler, this.hud);
		mouseListener = new MouseListener(this, this.handler, this.hud, this.upgradeScreen, this.player, this.upgrades);
		gm = new GameManager(this, hud);
		addKeyListener(new KeyInput(this.handler, this, this.hud, this.player, this.upgrades));
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
	public synchronized void start() {
		running = true;
		run();
	}
	/**
	 * Stops the game loop.
	 */
	public synchronized void stop() {
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
			if (running)
				render();// 60 times a second, objects are being drawn
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
	private void tick() {
		if (this.paused) {return;}
		
		handler.tick();// handler must always be ticked in order to draw all entities.
		if (gameState == STATE.Menu || gameState == STATE.Help) {// user is on menu, update the menu items
			if (this.gameCurrentClip != GAME_AUDIO.Menu) {
				this.gameCurrentClip = GAME_AUDIO.Menu;
				AudioUtil.closeGameClip();
				AudioUtil.playMenuClip(true, false);
			}
			menu.tick();
		} 
		if (gameState == STATE.Game) {// game is running
			if (this.gameCurrentClip != GAME_AUDIO.Game) {
				this.gameCurrentClip = GAME_AUDIO.Game;
				AudioUtil.closeMenuClip();
				AudioUtil.playGameClip(true);
			}
			hud.tick();
			gm.tick();
		} 
		else if (gameState == STATE.Upgrade) {// user is on upgrade screen, update the upgrade screen
			upgradeScreen.tick();
		} else if (gameState == STATE.GameOver) {// game is over, update the game over screen
			gameOver.tick();
		}

	}

	/**
	 * Constantly drawing to the many buffer screens of each entity requiring the
	 * Graphics objects (entities, screens, HUD's, etc).
	 */
	private void render() {
		/*
		 * BufferStrategies are used to prevent screen tearing. In other words, this
		 * allows for all objects to be redrawn at the same time, and not individually
		 */
		if (getWidth() <= 0 || getHeight() <= 0) {
	        return;
	    }
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        AffineTransform old = g.getTransform();

        double scaleFactor = Math.min(
                Double.valueOf(getWidth())/screenSize.getWidth(),
                Double.valueOf(getHeight())/screenSize.getHeight()
        );

        g.translate(getWidth()/2,getHeight()/2);
        g.scale(scaleFactor, scaleFactor);
        g.translate(-screenSize.getWidth()/2,-screenSize.getHeight()/2);

        mouseListener.setSpace(g.getTransform());
		
		g.setColor(Color.black);
		g.fillRect(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		if (gameState == STATE.Game) {
			gm.render(g); 
			hud.render(g);
		} else if (gameState == STATE.Menu || gameState == STATE.Help) {
			menu.render(g);
		} else if (gameState == STATE.Upgrade) {
			upgradeScreen.render(g);
		} else if (gameState == STATE.GameOver) {
			gameOver.render(g);
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
		bs.show();
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
