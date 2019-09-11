package mainGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
/**
 * @author Team B3
 * This interface can be implemented in other classes
 * and these functions can be overridden
 */
public abstract class GameMode extends GameState {
	abstract void resetMode();
	abstract GameObject getEnemyFromID(ID x, Point spawnLoc);
	abstract void resetMode(boolean hardReset);
}
