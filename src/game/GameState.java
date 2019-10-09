package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class GameState implements Animatable, MouseListener, KeyListener {
    public static boolean mouseOver(double mx, double my, int x, int y, int width, int height) {
        return x < mx && mx < x + width && y < my && my < y + height;
    }

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }
}
