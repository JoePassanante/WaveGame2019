package mainGame;

import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * @author Team B3
 * This interface can be implemented in other classes
 * and these functions can be overridden
 *
 * @Author Aaron Paterson 9/12/19
 */
public abstract class GameMode implements Animatable {
    private GameState state;
    public void setState(GameState s) {
        state = s;
    }
    public GameState getState() {
        return state;
    }
    abstract void resetMode();
	abstract GameObject getEnemyFromID(ID x, Point spawnLoc);
	abstract void resetMode(boolean hardReset);
	abstract MouseListener getMouseInput();
	abstract KeyListener getKeyInput();
}
