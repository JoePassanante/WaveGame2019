package mainGame;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Waves extends GameMode {
	private int currentLevelNum = 0;
	protected int maxTick = 2000,currentTick = 0;
	private Random r = new Random();
	private ArrayList<ID> currentEnemy;
	private String[] side = { "left", "right", "top", "bottom" };
	private Level currentLevel = null;
	public Level getCurrentLevel() {
	    return currentLevel;
    }
	private ArrayList<Integer> currentEnemySpawns;
	private static Image img;
	private ID lastEnemy = null;
	private ID lastBoss = (Math.random()*1 == 0 ? ID.EnemyBoss:ID.EnemyRocketBoss);

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
    public void toggleMenuMusic() {
        // Toggle menu theme between Space Jam and Africa
        africa = !africa;
        // Restart menu music
        AudioUtil.closeMenuClip();
        AudioUtil.playMenuClip(true, africa);
    }

    public Waves(Dimension screenSize) {
        handler = new Handler(screenSize);
        handler.updateSprites();
        hud = new HUD(this);
        menu = new Menu(this);

        setState(menu);

        player = new Player(screenSize.getWidth() / 2 - 32, screenSize.getHeight() / 2 - 32, ID.Player, this);
        upgradeScreen = new UpgradeScreen(this);
        upgrades = new Upgrades(this);
        gameOver = new GameOver(this);

        keyInput = new KeyInput(this);
    }

    //Links the ID of an enemy to actual creation.
	//This allows the gameMode to override the generic Level Spawning Scheme. IE if a boss doesn't care where a player is. 
	@Override
	public GameObject getEnemyFromID(ID enemy,Point spawnLoc){
		switch(enemy){
		case EnemyBasic:  return new EnemyBasic(spawnLoc.getX(), spawnLoc.getY(), 9, 9, ID.EnemyBasic, getHandler());
		case EnemySmart: return new EnemySmart(spawnLoc.getX(), spawnLoc.getY(), -5, ID.EnemySmart, getHandler());
		case EnemySweep: return new EnemySweep(spawnLoc.getX(), spawnLoc.getY(), 9, 2, ID.EnemySweep, getHandler());
		case EnemyShooter: return new EnemyShooter(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooter, getHandler());
		case EnemyBurst: return new EnemyBurst(-200, 200, 15, 15, 200, side[r.nextInt(4)], ID.EnemyBurst, getHandler());
		//case BossEye: return new EnemyBoss(ID.EnemyBoss, handler);
		case EnemyBoss: return new EnemyBoss(ID.EnemyBoss, getHandler(),currentLevelNum/10,getHUD());
		case EnemyRocketBoss: return new EnemyRocketBoss(100,100,ID.EnemyRocketBoss,getPlayer(), getHandler(), getHUD(), this,currentLevelNum/10);
		case EnemyFast: return new EnemyFast(spawnLoc.getX(), spawnLoc.getY(), ID.EnemySmart, getHandler());
		case EnemyShooterMover: return new EnemyShooterMover(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooterMover, getHandler());
		case EnemyShooterSharp: return new EnemyShooterSharp(spawnLoc.getX(),spawnLoc.getY(), 200, 200, -20 + (int)(Math.random()*5), ID.EnemyShooter, getHandler());
		default: 
			System.err.println("Enemy not found");
			return new EnemyBasic(spawnLoc.getX(),spawnLoc.getY(), 9, 9, ID.EnemyBasic, getHandler());
		}
	}
	
	/**
	 * Generates a random enemy ID
	 * @return ID (for entities)
	 */
	private ID randomEnemy(){	
		int r = (int)(Math.random()*5); //0-6 can be generated
		ID returnID = null;
		System.out.println("Enemy type of level " + this.currentLevelNum + " is " + r);
		switch(r){ //pick what enemy the random integer represents
			case 0: returnID = ID.EnemySmart; break;
			case 1: returnID = ID.EnemyBasic; break;
			case 2: returnID = ID.EnemyShooter; break;
			case 3: returnID = ID.EnemyBurst; break;
			case 4: returnID = ID.EnemyFast; break;
			default: returnID = randomEnemy(); break;
		}
		System.out.println(returnID + "| " + this.lastEnemy);
		if(returnID == this.lastEnemy){
			returnID = this.randomEnemy();
		}
		this.lastEnemy = returnID;
		return returnID;
	}
	
	/**
	 * Generates a random enemy ID
	 * @return ID (for entities)
	 */
	private ID randomEnemyHard(){	
		int r = (int)(Math.random()*3);
		ID returnID = null;
		System.out.println("Hard Enemy type of level " + this.currentLevelNum + " is " + r);
		switch(r){ //pick what enemy the random integer represents
			case 0: returnID = ID.EnemyShooterMover;break;
			case 1: returnID = ID.EnemySweep; break;
			case 2: returnID = ID.EnemyShooterSharp; break;
			default: returnID = randomEnemyHard(); break;
		}
		System.out.println(returnID + "| " + this.lastEnemy);
		if(returnID == this.lastEnemy){
			returnID = this.randomEnemyHard();
		}
		this.lastEnemy = returnID;
		return returnID;
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

			double tempx = (Math.random()*(getHandler().getGameDimension().getWidth()-300))+150;
			double tempy = (Math.random()*(getHandler().getGameDimension().getHeight()-300))+150;

			if(this.currentLevelNum%5 == 0){
				ArrayList<Integer>bossLimit = new ArrayList<>();
				bossLimit.add(1);
				System.out.println("New Boss Level");
				currentLevel = new Level(this, 0,randomBoss(), bossLimit, -1 , false, false, currentLevelNum);
			} else if (currentLevelNum%5 == 1 && currentLevelNum > 1) {
                setState(upgradeScreen);
            } else {
				System.out.println("New Normal Level");
                switch ((int)(Math.random()*5)){
                    case 0: getHandler().addPickup(new PickupSize(tempx,tempy));break;
                    case 1: getHandler().addPickup(new PickupHealth(tempx,tempy));break;
                    case 2: getHandler().addPickup(new PickupLife(tempx,tempy));break;
                    case 3: getHandler().addPickup(new PickupScore(tempx,tempy));break;
                    case 4: getHandler().addPickup(new PickupFreeze(tempx,tempy));break;
                }
				this.createNewEnemyLists();
				System.out.println(this.currentEnemy.size());
				System.out.println(this.currentEnemySpawns.size());
				currentLevel = new Level( this,0, this.currentEnemy,this.currentEnemySpawns,60*(20),false,false, currentLevelNum);
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
	 * Creates a new list of enemies for the next level to spawn.
	 * Sets the new list as a global variable for the game to access later. 
	 * (This can be later changed to return a list and can be passed into the level class | removing the global variable)
	 * Problem - Java Tuples cannot return both an arraylist of enemies, and the # of times they spawn. 
	 */
	private void createNewEnemyLists() {
		ArrayList<ID>newEnemy = new ArrayList<ID>();
		ArrayList<Integer>newSpawn = new ArrayList<Integer>();
		int curr = this.currentLevelNum/5;
		do{
			curr--;ID e = this.randomEnemy();
			if (curr >= 1) {//potential for a harder enemy to spawn
			if (curr >= 3 || Math.random() > .5) {
				 e = this.randomEnemyHard();
				 curr--;
			}
			}
			
			newEnemy.add(e);
			int s = (e.getDifficuty() + (int)(Math.random()*((e.getDifficuty()*0.1))));
			if(e.getDifficuty()==1)
				s = 1;
			newSpawn.add(s);
			System.out.println("----" + e + "-----" + s);
		}while(curr>= 0);
		this.currentEnemy = newEnemy;
		this.currentEnemySpawns = newSpawn;
		
	}
	/**
	 * 
	 * @return Returns an array of enemy bosses to be generated. 
	 * As of right now, enemy bosses are hard coded to only spawn once during a level. 
	 * See tick above.
	 */
	private ArrayList<ID> randomBoss() {
		ArrayList<ID>bossReturn = new ArrayList<ID>();
		if(this.lastBoss==ID.EnemyRocketBoss){
			System.out.println("Enemy Boss");
			bossReturn.add(ID.EnemyBoss);
			this.lastBoss = ID.EnemyBoss;
		}else{
			System.out.println("Enemy Rocket Boss");
			this.lastBoss = ID.EnemyRocketBoss;
			bossReturn.add(ID.EnemyRocketBoss);
		}
		return bossReturn;
	}
	/**
	 * Renders any static images for the level.
	 * IE Background. 
	 */
	@Override
	public void render(Graphics gfx) {
        Graphics2D g = (Graphics2D) gfx;

        g.drawImage(img, 0, 0, (int)getHandler().getGameDimension().getWidth(), (int)getHandler().getGameDimension().getHeight(), null);

        getState().render(g);
        if(getState() == currentLevel) {
            hud.render(g);
        }
        if(Client.devMode){
            //debug menu
            Font font2 = new Font("Amoebic", 1, 25);
            g.setColor(Color.white);
            g.setFont(font2);
//            g.drawString("Objects: " + handler.getNumObjects(), WIDTH-300, HEIGHT-200);
//            g.drawString("Pickups: " + handler.getNumPickUps(), WIDTH-300, HEIGHT-150);
//            g.drawString("FPS(?): " + this.FPS, WIDTH-300, HEIGHT-100);
//            g.drawString("Trails: " + handler.getTrails() + " / " + handler.getNumObjects(), WIDTH-300, HEIGHT-50);
        }
	}

    /**
	 * @param hardReset - if false only enemies are wiped. If true gamemode is completely reset. 
	 */
	@Override
	public void resetMode(boolean hardReset) {
		this.currentTick = 0;
		this.currentEnemy = null;
		this.currentLevel =  null;
        getHandler().clearEnemies();
		if(hardReset) {
			this.currentLevelNum = 0;
			getPlayer().playerWidth = 32;
			getPlayer().playerHeight = 32;
			getHUD().setExtraLives(0);
			getHUD().resetHealth();
		}
	}

    @Override
    public void mousePressed(MouseEvent e) {
	    getState().mousePressed(e);
    }

    private KeyInput keyInput;
    @Override
    KeyListener getKeyInput() {
	    return keyInput;
    }

    @Override
	public void resetMode() {
		resetMode(true);
	}

    public static void updateSprite(Themes theme) {
        // Set sprite based on current theme
        try {
            switch (theme) {
                case Space:
                    img = ImageIO.read(new File("src/images/space2.jpg"));
                    break;
                case Underwater:
                    img = ImageIO.read(new File("src/images/Water.jpg"));
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error reading sprite file for Waves (game background)");
        }
    }
}
