package game.waves;

import game.*;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * After completing a boss, this screen appears.
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/17/19
 */

public class Upgrades extends GameLevel {
    private Supplier<Function<GameLevel, GameEntity>> pickup;

    public Upgrades(GameLevel g, Supplier<Function<GameLevel, GameEntity>> p) {
        super(g);
        pickup = p;
    }

    private boolean initialized = false;
    @Override
	public void tick() {
        super.tick();
        if(!initialized) {
            for(int i=0; i<getNumber(); i++) {
                GameEntity pu = pickup.get().apply(this);
                pu.setVelX(getRandom().random()*10);
                pu.setVelY(getRandom().random()*10);
                getEntities().add(pu);
            }
            initialized = true;
        }
        else if(getEntities().size() < getEntities().stream().filter(Trail.class::isInstance).count() + getNumber()) {
            getEntities().clear();
            getState().pop();
        }
	}

	public void render(Graphics g) {
        super.render(g);

        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 100), "Grab an Upgrade!", getDimension().width/2, 200);
	}
}
