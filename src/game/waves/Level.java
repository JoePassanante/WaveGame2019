package game.waves;

import game.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * This class is meant to be a generic level that classes implementing gamemode can use to generate and throw away levels of different parameters.
 * @author Joe Passanante 11/28/17
 */
public class Level extends GameState {
    private int maxTick;
    private BiFunction<Point.Double, Handler, GameObject> enemyFactory;
    private int enemyNumber;
    private int enemyLimit;
    private int enemyTick;
    private int currentTick;
    private LevelText text;
    private int currentLevelNum;
    private double bossMaxHealth;

    private Client client;
    private Waves game;
    /**
     * @param g - The game class that the gamemode is apart of.
     * @param maxTick - The time the level takes to complete, if boss level leave at -1
     */
    public Level(Client c, Waves g, int maxTick, int n, int l, BiFunction<Point.Double, Handler, GameObject> f){
        this.client = c;
        this.game = g;
        this.maxTick = maxTick;
        this.currentLevelNum = n;
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
        game.getHUD().tick();

        currentTick += 1;
        //after 3 seconds, remove the level text
        if(currentTick == 1) {
            AudioUtil.closeMenuClip();
            AudioUtil.playGameClip(true);
            game.getHandler().add(text);
        }
        else if(this.currentTick>=100){
            game.getHandler().remove(text);
        }

        if(game.getHandler().getPlayers().size() == 1) {
            boolean[] or = new boolean[4];
            for (int b = 0; b < or.length; b += 1) {
                or[b] = wasd[b] || arrows[b];
            }
            changeDir(or, game.getHandler().getPlayers().get(0));
        }
        else if(game.getHandler().getPlayers().size() == 2) {
            changeDir(wasd, game.getHandler().getPlayers().get(0));
            changeDir(arrows, game.getHandler().getPlayers().get(1));
        }
//        changeDir(wasd, game.getPlayer());
//        changeDir(arrows, game.getPlayer());

        if(game.getHUD() != null) {
            if (0 <= maxTick) {
                game.getHUD().levelProgress = (int) (100.0 * currentTick / maxTick);
            }
            else {
                bossMaxHealth = Math.max(bossMaxHealth, game.getHandler().get(0).getHealth());
                game.getHUD().levelProgress = (int) (bossMaxHealth - game.getHandler().get(0).getHealth()) / 10;
            }
        }

        if (enemyTick <= currentTick && enemyNumber < enemyLimit) {
            game.getHandler().add(enemyFactory.apply(getSpawnLoc(),game.getHandler()));
            enemyTick += Math.max(15, 120/currentLevelNum);
            enemyNumber += 1;
        }

        if(0 <= maxTick && maxTick <= currentTick || game.getHandler().stream().noneMatch(h -> h.getClass().getName().contains("Enemy"))) {
            game.resetMode(false);
            if(currentLevelNum > 1 && currentLevelNum%5==0) {
                game.setState(game.getUpgradeScreen());
            }
        }
    }

    public void render(Graphics g) {
        game.getHandler().render(g);
        game.getHUD().render(g);
    }

    public Point.Double getSpawnLoc() {
        Dimension dim = game.getHandler().getGameDimension();
        Point.Double loc = new Point2D.Double();
        do {
            loc.setLocation(
            dim.width*(game.getHandler().getRandom().random()+1)/3,
            dim.height*(game.getHandler().getRandom().random()+1)/3
            );
        }
        while( Stream.concat(
                game.getHandler().getPlayers().stream(),
                game.getHandler().getPickups().stream()
            )
            .map((GameObject go) -> new Point2D.Double(go.getX(),go.getY()))
            .map(loc::distance)
            .anyMatch(d -> d < Math.hypot(dim.getWidth(),dim.getHeight())/8)
        );

        return loc;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private boolean[]
        wasd = new boolean[4],
        arrows = new boolean[4];

    private void changeDir(boolean[] keys, Player p) {
        int x = Boolean.compare(keys[3], keys[1]);
        int y = Boolean.compare(keys[2], keys[0]);

        double h = Math.max( Math.hypot(x, y), 1);

        p.setVelX(10 * x/h);
        p.setVelY(10 * y/h);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // key events for player 1
        // if the p key is pressed, the game would paused, if the key is pressed again, it would unpaused
        if(key == KeyEvent.VK_P){
            if(Client.devMode) {
//                game.setPaused(false);
                AudioUtil.playClip("../sound/pause.wav", false);
                AudioUtil.pauseGameClip();
            }
        }
        if(key == KeyEvent.VK_U){
            if(Client.devMode) {
                game.setState(game.getUpgradeScreen());
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            game.getHUD().setHealth(0);
        }

        // keep moving or start moving if key is pressed
        wasd[0] |= key == KeyEvent.VK_W;
        wasd[1] |= key == KeyEvent.VK_A;
        wasd[2] |= key == KeyEvent.VK_S;
        wasd[3] |= key == KeyEvent.VK_D;

        arrows[0] |= key == KeyEvent.VK_UP;
        arrows[1] |= key == KeyEvent.VK_LEFT;
        arrows[2] |= key == KeyEvent.VK_DOWN;
        arrows[3] |= key == KeyEvent.VK_RIGHT;

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

        // keep moving if key is not pressed
        wasd[0] &= key != KeyEvent.VK_W;
        wasd[1] &= key != KeyEvent.VK_A;
        wasd[2] &= key != KeyEvent.VK_S;
        wasd[3] &= key != KeyEvent.VK_D;

        arrows[0] &= key != KeyEvent.VK_UP;
        arrows[1] &= key != KeyEvent.VK_LEFT;
        arrows[2] &= key != KeyEvent.VK_DOWN;
        arrows[3] &= key != KeyEvent.VK_RIGHT;
    }
}
