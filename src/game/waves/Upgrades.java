package game.waves;

import game.GameEntity;
import game.GameLevel;
import game.GameWindow;
import game.pickup.Pickup;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * After completing a boss, this screen appears.
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 10/17/19
 */

public class Upgrades extends GameLevel.Unending {
    private Supplier<Function<GameLevel, GameEntity>> pickup;

    public Upgrades(GameLevel g, Supplier<Function<GameLevel, GameEntity>> p) {
        super(g);
        pickup = p;
    }

    @Override
    public void start() {
        getPlayers().stream().filter(p -> !getEntities().contains(p)).forEach(p -> p.setHealth(p.getMaxHealth())); // revive dead players
        getEntities().clear();
        getEntities().addAll(getPlayers()); // respawn all players

        for(int i=0; i<getNumber(); i+=1) { // throw pickups around in random directions
            GameEntity pu = pickup.get().apply(this);
            double angle = getRandom().random()*2*Math.PI;
            Point.Double p = spawnPoint();
            pu.setPosX(p.getX());
            pu.setPosY(p.getY());
            pu.setVelX(Math.cos(angle)*10);
            pu.setVelY(Math.sin(angle)*10);
            getEntities().add(pu);
        }
    }

    @Override
	public void tick() {
        super.tick(); //
        if( getEntities().stream().filter(Pickup.class::isInstance).count() + // every player had a chance to get a pickup
            getPlayers().stream().filter(getEntities()::contains).count() <= getNumber()
        ) {
            getEntities().retainAll(getPlayers());
            end();
        }
	}

	public void render(Graphics g) { // renders level with text
        super.render(g);
        g.setColor(Color.white);
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 100), "Grab an Upgrade!", getDimension().width/2, 200);
	}
}
