package mainGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is meant to be a generic level that classes implementing gamemode can use to generate and throw away levels of different parameters.
 * 
  * @author Joe Passanante 11/28/17
 */
public class Level extends GameState {
	private boolean levelRunning = true;
	private int maxTick = 0;
	private ArrayList<ID> enemyList;
	private ArrayList<Integer> spawnLimits;
	private ArrayList<Integer> spawnTicks;
	private int currentTick = 0;
	private boolean bossDead = false;
	int dif = 0;
	int x;
    private LevelText t;
    private int levelPopTimer = 0;
    Waves game;
	/**
	 * 
	 * @param g - The game class that the gamemode is apart of.
	 * @param dif - The level of difficulty (This can be left at 0)
	 * @param enemyList - The list of Enemy ID's that the level can spawn
	 * @param maxSpawn - The corresponding spawn rates of the enemyList (index for index, must be equal in size) 
	 * @param maxTick - The time the level takes to complete, if boss level leave at -1
	 * @param spawnPowerUp - True/False for spawning PowerUps(Not Implemented)
	 * @param upgrades - True/False if when the level is completed the player can choose a upgrade (Not Implemented)
	 */
	public Level(Waves g, int dif, ArrayList<ID> enemyList, ArrayList<Integer>maxSpawn, int maxTick, boolean spawnPowerUp, boolean upgrades, int currentLevelNum){
	    game = g;
		this.enemyList = enemyList;
		this.spawnLimits = maxSpawn;
		this.maxTick = maxTick;
		this.spawnTicks = new ArrayList<Integer>();
		for(int i = 0; i<enemyList.size();i++){
			spawnTicks.add(0);
		}

        t = new LevelText(
                game.getHandler().getGameDimension().getWidth() / 2 - 675,
                game.getHandler().getGameDimension().getHeight() / 2 - 200,
                "Level " + currentLevelNum + (currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
                ID.Levels1to10Text,
                game.getHandler()
        );
		if(currentLevelNum > 1) {
            game.getHandler().addObject(t);
        }
    }
	/**
	 * Tick spawns new enemies depending on their spawn limit and current tick.
	 * Takes the max level tick, and for every enemy divides it by the # of enemies.
	 * It then spawns that enemy every X ticks depending on the volume of spawns. 
	 * This ensures that the enemies are evenly spawned throughout the level. 
	 */
	public void tick(){
        game.getHandler().tick();// handler must always be ticked in order to draw all entities.

        currentTick++;
        this.levelPopTimer++;
        //after 3 seconds, the handler would remove the level text object "t".
        if(this.levelPopTimer>=100){
            game.getHandler().removeObject(t);
        }

		if (game.getHUD() != null && maxTick >= 0) {
		    game.getHUD().levelProgress = (int) (((double)currentTick/(double)maxTick)*100);
		}
		if(currentTick>=maxTick && maxTick>=0) this.levelRunning = false;
		if(running()==false) {
		    game.setState(game.getCurrentLevel());
		    return;
        }
		this.currentTick++;
	
		for(int i = 0; i<enemyList.size();i++){ //run through all the enemies we can spawn
			//check if we should even be spawning them(max spawn?)
			if(this.spawnTicks.get(i)==0){
				//check if its the right tick we should be checking?
				if(this.spawnLimits.get(i)>0){
					//if good time, spawn 1 enemy, subtract from max spawn and reset tick counter.
					game.getHandler().addObject(game.getEnemyFromID(this.enemyList.get(i), getSpawnLoc()));
					this.spawnLimits.set(i, this.spawnLimits.get(i)-1);
					this.spawnTicks.set(i,(this.maxTick-this.currentTick)/(this.spawnLimits.get(i)+1));
				}
			}else{
				this.spawnTicks.set(i, this.spawnTicks.get(i)-1);
			}
		}
		if(this.maxTick<=-1 && this.currentTick>=25){
			this.bossDead = this.checkIfBossDead();
			if(this.bossDead){
				System.err.println("Boss is dead");
				//spawn upgrade screen.
				//check if upgrade has been picked
				this.levelRunning = false; //end the level
			}
		}
		
	}
	private Point getSpawnLoc(){
	    Dimension d = game.getHandler().getGameDimension();
		return new Point((int)((Math.random()+1)*d.width/3),(int)((Math.random()+1)*d.width/3));
	}
	/**
	 * render anything that is specific to this level(not static content for the gamemode itself. 
	 * @param g Passed by parent (IE gamemode).
	 */
	public void render(Graphics g){
		game.getHandler().render(g);
	}
	private boolean checkIfBossDead(){
		boolean isDead = true;
		for(GameObject b: game.getHandler().object){
			for(ID id: this.enemyList){
				if (b.id==id) isDead = false;
			}
		}
		return isDead;
	}
	public boolean running(){
		return this.levelRunning;
	}

    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private boolean[] keyDown = new boolean[4];

    private void changeDir() {
        int goX = 0;
        int goY = 0;
        if (keyDown[0]) {goY--;}
        if (keyDown[2]) {goY++;}
        if (keyDown[1]) {goX--;}
        if (keyDown[3]) {goX++;}

        double h = Math.hypot(goX, goY);
        if(h < .5) {
            h = 1;
        }

        game.getPlayer().velX = goX * Player.playerSpeed/h;
        game.getPlayer().velY = goY * Player.playerSpeed/h;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // key events for player 1
        // if the p key is pressed, the game would paused, if the key is pressed again, it would unpaused
        if(key == KeyEvent.VK_P){
            if(Client.devMode) {
                game.setPaused(false);
                AudioUtil.playClip("../gameSound/pause.wav", false);
                AudioUtil.pauseGameClip();
            }
        }
        if(key == KeyEvent.VK_U){
            if(Client.devMode) {
                game.setState(game.getUpgradeScreen());
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            game.resetMode();
            game.setState(game.getMenu());
            game.getHandler().clearPlayer();
        }
        // if the w key is pressed, the player would move up
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            //tempObject.setVelY(-(this.speed));
            keyDown[0] = true;
        }
        // if the a key is pressed, the player would move left
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            //tempObject.setVelX(-(this.speed));
            keyDown[1] = true;
        }
        // if the s key is pressed, the player would move down
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            //tempObject.setVelY(this.speed);
            keyDown[2] = true;
        }
        // if the d key is pressed, the player would move right
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            //tempObject.setVelX(this.speed);
            keyDown[3] = true;
        }
        changeDir();
        // if the spacebar key is pressed while having an ability, the ability would be used
        if (key == KeyEvent.VK_SPACE) {
            game.getUpgrades().useAbility();
        }
        // if the enter key is pressed, the current level the player is currently in would skip to the next level
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E) {
            if(Client.devMode) {
                game.resetMode(false);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        // key events for player 1
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP)
            keyDown[0] = false;// tempObject.setVelY(0);
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT)
            keyDown[1] = false;// tempObject.setVelX(0);
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN)
            keyDown[2] = false;// tempObject.setVelY(0);
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            keyDown[3] = false;// tempObject.setVelX(0);
        }

        changeDir();
    }
}
