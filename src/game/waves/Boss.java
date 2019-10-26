package game.waves;

import game.GameEntity;
import game.GameLevel;

import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

public class Boss extends GameLevel {
    private int currentTick;
    private GameEntity enemy, text;
    public Boss(GameLevel g, Supplier<Function<GameLevel, GameEntity>> boss) {
        super(g);
        enemy = boss.get().apply(this);
        text = new RainbowText(
            getDimension().getWidth()/2, getDimension().getHeight()/2,
            "Level " + (getNumber()-1) + ": Boss Battle!!!",
            this
        );
    }

    @Override
    public void tick() {
        super.tick();

        if(currentTick == 0) {
            getEntities().add(enemy);
            getEntities().add(text);
        }
        else if(currentTick == 200) {
            getEntities().remove(text);
        }
        else if(!getEntities().contains(enemy) || Collections.disjoint(getEntities(), getPlayers())) {
            getEntities().retainAll(getPlayers());
            getState().pop();
        }

        currentTick += 1;
    }
}
