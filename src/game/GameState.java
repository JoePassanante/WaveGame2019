package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class GameState implements Animatable, MouseListener, KeyListener {
    /** MOUSE OVER
     * Helper method to detect is the mouse is over a "button" drawn via Graphics
     * @param mx
     * 	mouse x position
     * @param my
     * 	mouse y position
     * @param x
     * 	button x position
     * @param y
     * 	button y position
     * @param width
     * 	button width
     * @param height
     * 	button height
     * @return boolean, true if the mouse is contained within the button
     */
    public static boolean mouseOver(double mx, double my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }
}
