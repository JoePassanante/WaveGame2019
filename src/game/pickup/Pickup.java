package game.pickup;

import game.GameEntity;
import game.GameLevel;
import game.Player;

import java.awt.*;

public class Pickup extends GameEntity { // pickups that players can hold and use
    public Pickup(GameLevel level) {
        this(level, 100);
    }

    public Pickup(GameLevel level, int h) {
        super(level.getDimension().getWidth() / 2, level.getDimension().getHeight() / 2, 30, 30, level);
        setHealth(h);
    }

    @Override
    public void collide(Player player) { // player picks this up
        super.collide(player);
        getLevel().getEntities().remove(this);
        setVelX(0);
        setVelY(0);
        player.getInactive().add(0, this);
    }

    @Override
    public void tick() { // modulate around edges of screen
        super.tick();
        Rectangle bounds = getLevel().getBounds();
        if (getVelX() + getVelY() > 1 && !bounds.intersects(getBounds())) {
            setPosX(Math.floorMod((int) getPosX(), (int) bounds.getWidth()));
            setPosY(Math.floorMod((int) getPosY(), (int) bounds.getHeight()));
        }
    }

    public void affect(Player player) { // use health to affect player
        setHealth(getHealth() - 1);
        if (getHealth() < 0) {
            player.getActive().remove(this);
        }
    }

    public static class Active extends Pickup { // pickups that immediately have an effect
        public Active(GameLevel level) {
            super(level, 0);
        }

        public void collide(Player player) {
            super.collide(player);
            player.getActive().add(0, player.getInactive().remove(0));
        }
    }
}
