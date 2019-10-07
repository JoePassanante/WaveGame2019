package mainGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Function;

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
    public Level(Waves g, int maxTick, int c, int l, Function<Point, GameObject> f){
        this.game = g;
        this.maxTick = maxTick;
        this.currentLevelNum = c;
        this.enemyLimit = l;
        this.enemyFactory = f;

        text = new LevelText(
                game.getHandler().getGameDimension().getWidth() / 2 - 675,
                game.getHandler().getGameDimension().getHeight() / 2 - 200,
                "Level " + currentLevelNum + (currentLevelNum%5 == 0 ? ": Boss Level!!!":""),
                game.getHandler()
        );
    }

    /**
     * Tick spawns new enemies depending on their spawn limit and current tick.
     * Takes the max level tick, and for every enemy divides it by the # of enemies.
     * It then spawns that enemy every X ticks depending on the volume of spawns.
     * This ensures that the enemies are evenly spawned throughout the level.
     */
    public void tick(){
        /*
        for(int i = game.getHandler().size()-1; i>=0; i -= 1) {
            Rectangle bounds = new Rectangle(game.getHandler().getGameDimension());
            if(!bounds.contains(game.getHandler().get(i).getBounds())) {
                game.getHandler().remove(game.getHandler().get(i));
                //enemyNumber -= 1;
            }
        }
        */
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
        if(currentTick>=maxTick && maxTick>=0) {
            this.levelRunning = false;
        }
        if(!running()) {
            game.setState(game.getCurrentLevel());
            return;
        }

        if (enemyTick <= currentTick && enemyNumber < enemyLimit) {
            game.getHandler().add(enemyFactory.apply(getSpawnLoc()));
            enemyTick += Math.max( 15, 120/currentLevelNum);
            enemyNumber += 1;
        }

        if(game.getHandler().stream().noneMatch(h -> h.getClass().getName().contains("Enemy"))) {
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
