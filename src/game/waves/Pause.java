package game.waves;

import game.GameLevel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Pause extends GameLevel.Unending { // pause menu
    public Pause(GameLevel gl) {
        super(gl);
        getNonentities().addAll(gl.getNonentities());
    }

    @Override
    public void render(Graphics g) { // draw pause bars
        super.render(g);
        g.setColor(new Color(0x11,0x11,0x11,0x99));
        g.fillRect(0, 0, getBounds().width, getBounds().height);
        g.setColor(Color.white);
        g.fillRect(670, 290, 250, 500);
        g.fillRect(1000, 290, 250, 500);
    }

    @Override
    public void tick() {
        // do nothing, maybe add an animation or something
    }

    @Override
    public void keyPressed(KeyEvent e) { // exit when key is pressed
        end();
    }
}
