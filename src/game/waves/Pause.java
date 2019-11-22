package game.waves;

import game.GameLevel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Pause extends GameLevel {
    public Pause(GameLevel gl) {
        super(gl);
        getNonentities().addAll(gl.getNonentities());
    }

    @Override
    public void tick() {
        setMaxTick(getMaxTick() + 1);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);
        g.fillRect(670, 290, 250, 500);
        g.fillRect(1000, 290, 250, 500);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_P) {
            end();
        }
    }
}
