package game.waves;

import game.*;
import game.pickup.Pickup;

import java.awt.*;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * After completing a boss, this screen appears.
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/17/19
 */

public class Upgrades extends GameLevel {
    private Supplier<Function<GameLevel, GameEntity>> pickup;
    private int currentTick;

    public Upgrades(GameLevel g, Supplier<Function<GameLevel, GameEntity>> p) {
        super(g);
        pickup = p;
    }

    @Override
	public void tick() {
        super.tick();

        if(Collections.disjoint(getEntities(), getPlayers())) {
            getState().pop();
        }
        else if(currentTick == 0) {
            getEntities().removeAll(getPlayers()); // TODO: Use a Set for entities.
            getEntities().addAll(getPlayers());

            for(int i=0; i<getNumber(); i+=1) {
                GameEntity pu = pickup.get().apply(this);
                double angle = getRandom().random()*2*Math.PI;
                pu.setVelX(Math.cos(angle)*10);
                pu.setVelY(Math.sin(angle)*10);
                getEntities().add(pu);
            }
        }
        else if(getEntities().stream().filter(Pickup.class::isInstance).count() + getPlayers().size() <= getNumber()) {
            getEntities().retainAll(getPlayers());
            getState().pop();
        }

        currentTick += 1;
	}

	public void render(Graphics g) {
        super.render(g);

        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 100), "Grab an Upgrade!", getDimension().width/2, 200);
	}
}
