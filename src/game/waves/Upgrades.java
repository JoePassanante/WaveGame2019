package game.waves;

import game.*;
import game.pickup.Pickup;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * After completing a boss, this screen appears.
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/17/19
 */

public class Upgrades extends GameLevel {
    public Upgrades(GameLevel g, Supplier<Function<GameLevel, GameObject>> pickup) {
        super(g);
        for(int i=0; i<6; i++) {
            GameObject pu = pickup.get().apply(this);
            pu.setVelX(getRandom().random()*10);
            pu.setVelY(getRandom().random()*10);
            add(pu);
        }
    }


    @Override
	public void tick() {
        super.tick();

        if(stream().filter(Pickup.class::isInstance).count() < 6 + getPlayers().size()) {
            getState().pop();
        }
	}

	public void render(Graphics g) {
        super.render(g);

        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 175), "Grab an Upgrade!", getDimension().width/2, 200);
	}

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
