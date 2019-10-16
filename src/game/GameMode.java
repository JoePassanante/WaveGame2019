package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Team B3
 * @author Aaron Paterson 9/12/19
 */
public class GameMode extends GameState {
    private GameState state;
    public void setState(GameState s) {
        state = s;
    }
    public GameState getState() {
        return state;
    }

    @Override
    public void render(Graphics g) {
        state.render(g);
    }
    @Override
    public void tick() {
        state.tick();
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(state != null) {
            state.mousePressed(e);
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(state != null) {
            state.mouseReleased(e);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(state != null) {
            state.keyPressed(e);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(state != null) {
            state.keyReleased(e);
        }
    }
}
