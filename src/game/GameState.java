package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;

public abstract class GameState extends ArrayList<GameObject> implements Animatable, MouseListener, KeyListener {
    private Stack<GameState> state;
    public Stack<GameState> getState() {
        return state;
    }

    public GameState(Stack<GameState> s) {
        state = s;
    }

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }

    public static boolean mouseOver(double mx, double my, int x, int y, int w, int h) {
        return new Rectangle(x,y,w,h).contains(new Point.Double(mx,my));
    }
}
