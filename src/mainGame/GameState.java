package mainGame;

import java.awt.*;
import java.awt.event.MouseAdapter;

public abstract class GameState extends MouseAdapter {
    abstract void tick();
    abstract void render(Graphics g);

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
    public boolean mouseOver(double mx, double my, int x, int y, int width, int height) {
        if (mx > x && mx < x + width) {
            if (my > y && my < y + height) {
                return true;
            } else
                return false;
        } else
            return false;
    }
}
