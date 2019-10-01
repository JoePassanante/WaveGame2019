package mainGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Team B3
 * @author Aaron Paterson 9/12/19
 */
public abstract class GameMode extends GameState {
    private GameState state;
    public void setState(GameState s) {
        state = s;
    }
    public GameState getState() {
        return state;
    }
	abstract GameObject getEnemyFromID(ID x, Point spawnLoc);
    abstract void resetMode();
	abstract void resetMode(boolean hardReset);

    @Override
    public void keyPressed(KeyEvent e) {
        state.keyPressed(e);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        state.keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        state.mousePressed(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        state.mouseReleased(e);
    }
}
