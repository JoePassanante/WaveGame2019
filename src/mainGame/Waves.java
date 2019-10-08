package mainGame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Waves extends GameMode {
	private int currentLevelNum = 1;
	private Level currentLevel = null;
    private boolean paused = false;
    private Handler handler;
    private HUD hud;
    private Player player;
    private GameState upgradeScreen;
    private GameState menu;
    private GameState gameOver;
    private Upgrades upgrades;

	public Level getCurrentLevel() {
	    return currentLevel;
    }
    public void setPaused(boolean p) {
        paused = p;
    }
    public Handler getHandler() {
        return handler;
    }
    public HUD getHUD() {
        return hud;
    }
    public Player getPlayer() {
        return player;
    }
    public GameState getUpgradeScreen() {
        return upgradeScreen;
    }
    public GameState getMenu() {
        return menu;
    }
    public GameState getGameOver() {
        return gameOver;
    }
    public Upgrades getUpgrades() {
        return upgrades;
    }

    private boolean africa = false;
    public void setMenuMusic(boolean a) {
        // Toggle menu theme between Space Jam and Africa
        africa = a;
        // Restart menu music
        AudioUtil.closeMenuClip();
        AudioUtil.playMenuClip(true, africa);
    }

    private RandomDifferentElement<BiFunction<Point.Double, Handler, GameObject>>
        randomEasyEnemy = new RandomDifferentElement<>(
            EnemyBasic::new,
            EnemyBurst::new,
            EnemyFast::new,
            EnemyShooter::new,
            EnemySmart::new
        ),
        randomHardEnemy = new RandomDifferentElement<>(
            EnemyShooterMover::new,
            EnemyShooterSharp::new,
            EnemySweep::new
        ),
        randomBoss = new RandomDifferentElement<>(
            (p,h) -> new EnemyBoss(h,currentLevelNum/10, getHUD()),
            (p,h) -> new EnemyRocketBoss(100,100, getPlayer(), h, getHUD(), this,currentLevelNum/10)
//          (p,h) -> new BossEye(0, 0, ID.BossEye, h, 0)
        );

    public Waves(Dimension screenSize) {
        handler = new Handler(screenSize, player);
        menu = new Menu(this);
        hud = new HUD(this);

        setState(menu);

        player = new Player(screenSize.getWidth() / 2 - 32, screenSize.getHeight() / 2 - 32, this);
        upgradeScreen = new UpgradeScreen(this);
        upgrades = new Upgrades(this);
        gameOver = new GameOver(this);
    }

    /**
     * Used to switch between each of the screens shown to the user
     */

    public enum GAME_AUDIO {
        Menu, Game, None
    }

	/**
	 * Ticks Level classes generated.
	 * Generates levels when they are completed. 
	 */
    public GAME_AUDIO gameCurrentClip = GAME_AUDIO.Menu;

    @Override
	public void tick() {
        if (this.paused) {return;}

		if(currentLevel==null || !currentLevel.running()) {
            getHandler().clearEnemies();
            getHandler().clearPickups();
            getHUD().setLevel(this.currentLevelNum);

            if (currentLevelNum > 1 && currentLevelNum%5 == 1 && getState() != upgradeScreen) { // upgrade after every boss
                setState(upgradeScreen);
            }
            else if(this.currentLevelNum%5 == 0) {
                System.out.println("New Boss Level");
                currentLevel = new Level( this,60*(20), currentLevelNum, 1,  randomBoss.get());
                currentLevelNum += 1;
            }
            else {
                System.out.println("New Normal Level");
                BiFunction<Point.Double, Handler, GameObject>[] enemyArray = IntStream
                    .rangeClosed(0, Math.min(5,currentLevelNum/5))
                    .mapToObj( i -> i > 3 && Math.random() > .5 ? randomHardEnemy : randomEasyEnemy)
                    .map(RandomDifferentElement::get)
                    .<BiFunction<Point.Double, Handler, GameObject>>toArray(BiFunction[]::new);

                Arrays.stream(enemyArray)
                    .map(e -> e.apply(new Point2D.Double(0,0),getHandler()))
                    .map(e -> e.getClass().getSimpleName())
                    .forEach(System.out::println);

                RandomDifferentElement<BiFunction<Point.Double, Handler, GameObject>> enemyFactory = new RandomDifferentElement<>(enemyArray);

                currentLevel = new Level( this,60*(20), currentLevelNum, currentLevelNum, (p,f) -> enemyFactory.get().apply(p,f));
                //currentLevel = new Level( this,60*(20), currentLevelNum, currentLevelNum, EnemyShooterSharp::new);

                Point.Double pd = new Point.Double(
                    (Math.random()*(getHandler().getGameDimension().getWidth()-300))+150,
                    (Math.random()*(getHandler().getGameDimension().getHeight()-300))+150
                );

                if(currentLevelNum > 1) {
                    getHandler().addPickup(
                        new RandomDifferentElement<BiFunction<Point.Double, Handler, GameObject>>(
                            PickupFreeze::new,
                            PickupHealth::new,
                            PickupLife::new,
                            PickupScore::new,
                            PickupSize::new
                        ).get().apply(pd, getHandler())
                    );
                }
                currentLevelNum += 1;
            }
            hud.setLevel(currentLevelNum);
		}

		getState().tick();
        if (getState() == menu) { // user is on menu, update the menu items
            if (this.gameCurrentClip != GAME_AUDIO.Menu) {
                this.gameCurrentClip = GAME_AUDIO.Menu;
                AudioUtil.closeGameClip();
                AudioUtil.playMenuClip(true, false);
            }
        }
        if (getState() == currentLevel) { // game is running
            if (this.gameCurrentClip != GAME_AUDIO.Game) {
                this.gameCurrentClip = GAME_AUDIO.Game;
                AudioUtil.closeMenuClip();
                AudioUtil.playGameClip(true);
            }
            hud.tick();
        }
    }

	/**
	 * Renders any static images for the level.
	 * IE Background. 
	 */
	@Override
	public void render(Graphics g) {
	    Image img = getHandler().getTheme().get(this);

        if(img != null) {
            g.drawImage(img, 0, 0, (int) getHandler().getGameDimension().getWidth(), (int) getHandler().getGameDimension().getHeight(), null);
        }

        getState().render(g);
        if(getState() == currentLevel) {
            hud.render(g);
        }
	}

    /**
	 * @param hardReset - if false only enemies are wiped. If true gamemode is completely reset. 
	 */
	@Override
	public void resetMode(boolean hardReset) {
		this.currentLevel = null;
        getHandler().clearEnemies();
		if(hardReset) {
			this.currentLevelNum = 1;
			getPlayer().setWidth(32);
			getPlayer().setHeight(32);
			getHUD().setExtraLives(0);
			getHUD().resetHealth();
		}
	}

	public void resetMode() {
		resetMode(true);
	}
}
