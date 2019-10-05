package mainGame;

import java.awt.*;

public class Waves extends GameMode {
	private int currentLevelNum = 0;
	protected int maxTick = 2000,currentTick = 0;
	private Level currentLevel = null;
	public Level getCurrentLevel() {
	    return currentLevel;
    }

    private boolean paused = false;
    public void setPaused(boolean p) {
        paused = p;
    }

    private Handler handler;
    public Handler getHandler() {
        return handler;
    }

    private HUD hud;
    public HUD getHUD() {
        return hud;
    }

    private Player player;
    public Player getPlayer() {
        return player;
    }

    private GameState upgradeScreen;
    public GameState getUpgradeScreen() {
        return upgradeScreen;
    }

    private GameState menu;
    public GameState getMenu() {
        return menu;
    }

    private GameState gameOver;
    public GameState getGameOver() {
        return gameOver;
    }

    private Upgrades upgrades;
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

    public Waves(Dimension screenSize) {
        handler = new Handler(screenSize, player);
        hud = new HUD(this);
        menu = new Menu(this);

        setState(menu);

        player = new Player(screenSize.getWidth() / 2 - 32, screenSize.getHeight() / 2 - 32, ID.Player, this);
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

		if(currentLevel==null || !currentLevel.running()){
			currentLevelNum += 1;
			getHUD().setLevel(this.currentLevelNum);
            getHandler().clearEnemies();
            getHandler().clearPickups();

            if (currentLevelNum > 1 && currentLevelNum%5 == 1) { // upgrade after every boss
                setState(upgradeScreen);
            }
            else {
                currentLevel = new Level( this,60*(20), currentLevelNum);
            }
		}

		getState().tick();
        if (getState() == menu) {// user is on menu, update the menu items
            if (this.gameCurrentClip != GAME_AUDIO.Menu) {
                this.gameCurrentClip = GAME_AUDIO.Menu;
                AudioUtil.closeGameClip();
                AudioUtil.playMenuClip(true, false);
            }
        }
        if (getState() == currentLevel) {// game is running
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
	    if(getHandler().getTheme().get(ID.Waves) != null) {
            g.drawImage(getHandler().getTheme().get(ID.Waves), 0, 0, (int) getHandler().getGameDimension().getWidth(), (int) getHandler().getGameDimension().getHeight(), null);
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
		this.currentTick = 0;
		this.currentLevel =  null;
        getHandler().clearEnemies();
		if(hardReset) {
			this.currentLevelNum = 0;
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
