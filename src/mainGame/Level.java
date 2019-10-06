package mainGame;

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is meant to be a generic level that classes implementing gamemode can use to generate and throw away levels of different parameters.
 * @author Joe Passanante 11/28/17
 */
public class Level extends GameState {
    private boolean levelRunning = true;
    private int maxTick;
    private Function<Point, GameObject> enemyFactory;
    private int enemyNumber;
    private int enemyLimit;
    private int enemyTick;
    private int currentTick;
    private LevelText text;
    private int currentLevelNum;

    private Waves game;
    /**
     * @param g - The game class that the gamemode is apart of.
     * @param maxTick - The time the level takes to complete, if boss level leave at -1
     */
    public Level(Waves g, int maxTick, int c){
        this.game = g;
        this.maxTick = maxTick;
        this.currentLevelNum = c;

        if(this.currentLevelNum%5 == 0) {
            System.out.println("New Boss Level");

            enemyFactory = p -> makeRandomBoss().get();
            enemyLimit = 1;
        } else {
            System.out.println("New Normal Level");
            List<Function<Point,GameObject>> enemyList = IntStream.range(0,Math.max(5,currentLevelNum/5))
                    .mapToObj( i -> i > 3 && Math.random() < .5
                            ? makeRandomHardEnemy()
                            : makeRandomEasyEnemy()
                    ).collect(Collectors.toList());

            enemyFactory = p -> randomElement(enemyList).apply(p);
            enemyLimit = currentLevelNum;

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

        text = new LevelText(
                game.getHandler().getGameDimension().getWidth() / 2 - 675,
                game.getHandler().getGameDimension().getHeight() / 2 - 200,
                "Level " + currentLevelNum + (currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
                ID.Levels1to10Text,
                game.getHandler()
        );
    }

    private static <T> T randomElement(List<T> list) {
        return list.get((int)(list.size()*Math.random()));
    }

    private static <T> T randomElementExcluding(List<T> list, int exclude, IntConsumer update) {
        int i = (int)(list.size() * Math.random());
        if(i != exclude) {
            update.accept(i);
            return list.get(i);
        }
        return randomElementExcluding(list, exclude, update);
    }

    private Function<Point,GameObject> makeRandomEasyEnemy() {
        return randomElementExcluding( Arrays.asList(
                p -> new EnemyBasic(p.getX(), p.getY(), 9, 9, ID.EnemyBasic, game.getHandler()),
                p -> new EnemyBurst(-200, 200, 15, 15, 200,
                        new String[]{ "left", "right", "top", "bottom" }[(int)(4*Math.random())],
                        ID.EnemyBurst, game.getHandler()),
                p -> new EnemyFast(p.getX(), p.getY(), ID.EnemyFast, game.getHandler()),
                p -> new EnemyShooter(p.getX(), p.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooter, game.getHandler()),
                p -> new EnemySmart(p.getX(), p.getY(), -5, ID.EnemySmart, game.getHandler())
        ), game.getLastEnemy(), game::setLastEnemy);
    }

    private Function<Point,GameObject> makeRandomHardEnemy() {
        return randomElementExcluding( Arrays.asList(
                p -> new EnemyShooterMover(p.getX(), p.getY(), 100, 100, -20 + (int)(Math.random()*5), ID.EnemyShooterMover, game.getHandler()),
                p -> new EnemyShooterSharp(p.getX(), p.getY(), 200, 200, -20 + (int)(Math.random()*5), ID.EnemyShooterSharp, game.getHandler()),
                p -> new EnemySweep(p.getX(), p.getY(), 9, 2, ID.EnemySweep, game.getHandler())
        ), game.getLastEnemy(), game::setLastEnemy);
    }

    private Supplier<GameObject> makeRandomBoss() {
        return randomElementExcluding( Arrays.asList(
                () -> new EnemyBoss(ID.EnemyBoss, game.getHandler(),currentLevelNum/10, game.getHUD()),
                () -> new EnemyRocketBoss(100,100,ID.EnemyRocketBoss, game.getPlayer(), game.getHandler(), game.getHUD(), game,currentLevelNum/10)
//          () -> new EnemyBoss(ID.EnemyBoss, game.getHandler(), 0, game.getHUD())
        ), game.getLastBoss(), game::setLastBoss);
    }

    /**
     * Tick spawns new enemies depending on their spawn limit and current tick.
     * Takes the max level tick, and for every enemy divides it by the # of enemies.
     * It then spawns that enemy every X ticks depending on the volume of spawns.
     * This ensures that the enemies are evenly spawned throughout the level.
     */
    public void tick(){
        game.getHandler().tick(); // handler ticked to update entities.

        currentTick += 1;
        //after 3 seconds, remove the level text
        if(this.currentTick == 2) {
            game.getHandler().add(text);
        }
        else if(this.currentTick>=100){
            game.getHandler().remove(text);
        }

        if (game.getHUD() != null && maxTick >= 0) {
            game.getHUD().levelProgress = (int) (((double)currentTick/(double)maxTick)*100);
        }
        if(currentTick>=maxTick && maxTick>=0) this.levelRunning = false;
        if(!running()) {
            game.setState(game.getCurrentLevel());
            return;
        }

        if (enemyTick <= currentTick && enemyNumber < enemyLimit) {
            game.getHandler().add(enemyFactory.apply(getSpawnLoc()));
            enemyTick += Math.max( 15, 120/currentLevelNum);
            enemyNumber += 1;
        }
        /*
        for(int i = 0; i< enemyList.size(); i++) { //run through all the enemies we can spawn
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
		*/

        if(game.getHandler().stream().noneMatch(h -> h.getId().getDifficuty() > 0)) {
            levelRunning = false;
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
        int x = Boolean.compare(keyDown[3], keyDown[1]);
        int y = Boolean.compare(keyDown[2], keyDown[0]);

        double h = Math.max( Math.hypot(x, y), 1);

        game.getPlayer().velX = x * Player.playerSpeed/h;
        game.getPlayer().velY = y * Player.playerSpeed/h;
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
