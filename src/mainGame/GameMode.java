package mainGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
/**
 * @author Team B3
 * This interface can be implemented in other classes
 * and these functions can be overridden
 */
public interface GameMode {
	void tick();
	void render(Graphics g);
	void resetMode();
	GameObject getEnemyFromID(ID x, Point spawnLoc);
	void resetMode(boolean hardReset);
}
