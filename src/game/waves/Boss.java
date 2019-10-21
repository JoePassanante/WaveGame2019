package game.waves;

import game.GameLevel;
import game.GameObject;

import java.awt.geom.Point2D;
import java.util.function.Function;
import java.util.function.Supplier;

public class Boss extends GameLevel {
    private int currentTick;
    private GameObject enemy, text;
    public Boss(GameLevel g, Supplier<Function<GameLevel, GameObject>> boss) {
        super(g);
        enemy = boss.get().apply(this);
        text = new RainbowText(
            new Point2D.Double(getDimension().getWidth()/2, getDimension().getHeight()/2),
            "Level " + getNumber() + ": Boss Level!!!",
            this
        );
        addAll(getPlayers());
        add(enemy);
    }

    @Override
    public void tick() {
        super.tick();

        if(currentTick == 0) {
            add(text);
        }
        else if(currentTick == 200) {
            remove(text);
        }
        else if(!contains(enemy)) {
            getState().pop();
        }

        currentTick += 1;
    }
}
