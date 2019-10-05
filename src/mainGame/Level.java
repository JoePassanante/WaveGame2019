package mainGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
	// private int dif = 0;
    private LevelText t;
    private int levelPopTimer = 0;
    private int currentLevelNum = 0;
    private ID lastEnemy;
    private ID lastBoss = (Math.random()*1 == 0 ? ID.EnemyBoss:ID.EnemyRocketBoss);

    private Waves game;
	/**
	 * 
	 * @param g - The game class that the gamemode is apart of.
	 * @param maxTick - The time the level takes to complete, if boss level leave at -1
	 */
	public Level(Waves g, int maxTick, int currentLevelNum){
	    game = g;
	    this.currentLevelNum = currentLevelNum;
		this.enemyList = new ArrayList<>();
		this.spawnLimits = new ArrayList<>();

        if(this.currentLevelNum%5 == 0) {
            System.out.println("New Boss Level");

            enemyList = randomBoss();
            spawnLimits.add(1);
        } else {
            System.out.println("New Normal Level");
            createNewEnemyLists();

            double x = (Math.random()*(game.getHandler().getGameDimension().getWidth()-300))+150;
            double y = (Math.random()*(game.getHandler().getGameDimension().getHeight()-300))+150;
            switch ((int)(Math.random()*5)){
                case 0: game.getHandler().addPickup(new PickupSize(x, y, game.getHandler())); break;
                case 1: game.getHandler().addPickup(new PickupHealth(x, y, game.getHandler())); break;
                case 2: game.getHandler().addPickup(new PickupLife(x, y, game.getHandler())); break;
                case 3: game.getHandler().addPickup(new PickupScore(x, y, game.getHandler())); break;
                case 4: game.getHandler().addPickup(new PickupFreeze(x, y, game.getHandler())); break;
            }
        }
        System.out.println(this.enemyList.size());
        System.out.println(this.spawnLimits.size());

        this.maxTick = maxTick;
		this.spawnTicks = new ArrayList<>();
		for(int i = 0; i < enemyList.size(); i++){
			spawnTicks.add(0);
		}

		this.currentLevelNum = currentLevelNum;

        t = new LevelText(
            game.getHandler().getGameDimension().getWidth() / 2 - 675,
            game.getHandler().getGameDimension().getHeight() / 2 - 200,
            "Level " + currentLevelNum + (currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
            ID.Levels1to10Text,
            game.getHandler()
        );
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
     * Creates a new list of enemies for the next level to spawn.
     */
    private void createNewEnemyLists() {
        enemyList.clear();
        spawnLimits.clear();
        int curr = this.currentLevelNum/5;
        do{
            curr--;
            ID e = this.randomEnemy();
            if (curr >= 1) {//potential for a harder enemy to spawn
                if (curr >= 3 || Math.random() > .5) {
                    e = this.randomEnemyHard();
                    curr--;
                }
            }

            enemyList.add(e);
            int s = (e.getDifficuty() + (int)(Math.random()*((e.getDifficuty()*0.1))));
            if(e.getDifficuty()==1)
                s = 1;
            spawnLimits.add(s);
            System.out.println("-----" + e + "-----" + s);
        }
        while(curr>= 0);
    }

    //Links the ID of an enemy to actual creation.
    //This allows the gameMode to override the generic Level Spawning Scheme. IE if a boss doesn't care where a player is.
	private GameObject getEnemyFromID(ID enemy, Point spawnLoc){
		switch(enemy){
            case EnemyBasic: return new EnemyBasic(spawnLoc.getX(), spawnLoc.getY(), 9, 9, ID.EnemyBasic, game.getHandler());
            case EnemySmart: return new EnemySmart(spawnLoc.getX(), spawnLoc.getY(), -5, ID.EnemySmart, game.getHandler());
            case EnemySweep: return new EnemySweep(spawnLoc.getX(), spawnLoc.getY(), 9, 2, ID.EnemySweep, game.getHandler());
            case EnemyShooter: return new EnemyShooter(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooter, game.getHandler());
            case EnemyBurst: return new EnemyBurst(-200, 200, 15, 15, 200, new String[]{ "left", "right", "top", "bottom" }[(int)(Math.random()*4)], ID.EnemyBurst, game.getHandler());
            // case BossEye: return new EnemyBoss(ID.EnemyBoss, handler);
            case EnemyBoss: return new EnemyBoss(ID.EnemyBoss, game.getHandler(),currentLevelNum/10, game.getHUD());
            case EnemyRocketBoss: return new EnemyRocketBoss(100,100,ID.EnemyRocketBoss, game.getPlayer(), game.getHandler(), game.getHUD(), game,currentLevelNum/10);
            case EnemyFast: return new EnemyFast(spawnLoc.getX(), spawnLoc.getY(), ID.EnemyFast, game.getHandler());
            case EnemyShooterMover: return new EnemyShooterMover(spawnLoc.getX(),spawnLoc.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooterMover, game.getHandler());
            case EnemyShooterSharp: return new EnemyShooterSharp(spawnLoc.getX(),spawnLoc.getY(), 200, 200, -20 + (int)(Math.random()*5), ID.EnemyShooterSharp, game.getHandler());
            default:
                System.err.println("Enemy not found");
                return new EnemyBasic(spawnLoc.getX(),spawnLoc.getY(), 9, 9, ID.EnemyBasic, game.getHandler());
		}
	}

    /**
     * @return Returns an array of enemy bosses to be generated.
     * As of right now, enemy bosses are hard coded to only spawn once during a level.
     * See tick above.
     */
    private ArrayList<ID> randomBoss() {
        ArrayList<ID>bossReturn = new ArrayList<>();
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
	 * Tick spawns new enemies depending on their spawn limit and current tick.
	 * Takes the max level tick, and for every enemy divides it by the # of enemies.
	 * It then spawns that enemy every X ticks depending on the volume of spawns. 
	 * This ensures that the enemies are evenly spawned throughout the level. 
	 */
	public void tick(){
        game.getHandler().tick();// handler must always be ticked in order to draw all entities.

        currentTick++;
        this.levelPopTimer++;
        //after 3 seconds, remove the level text
        if(this.levelPopTimer == 2) {
            game.getHandler().addObject(t);
        }
        else if(this.levelPopTimer>=100){
            game.getHandler().removeObject(t);
        }

		if (game.getHUD() != null && maxTick >= 0) {
		    game.getHUD().levelProgress = (int) (((double)currentTick/(double)maxTick)*100);
		}
		if(currentTick>=maxTick && maxTick>=0) this.levelRunning = false;
		if(!running()) {
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
					game.getHandler().addObject(getEnemyFromID(this.enemyList.get(i), getSpawnLoc()));
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
	    Dimension dim = game.getHandler().getGameDimension();
		return new Point((int)((Math.random()+1)*dim.width/3),(int)((Math.random()+1)*dim.width/3));
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
		for(GameObject b: game.getHandler()){
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
