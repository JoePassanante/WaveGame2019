package game.enemy;

import game.GameLevel;
import game.Trail;
import game.waves.RainbowText;
import util.Random;

import java.awt.*;

/**
 * @author Brandon Loehle 5/30/16
 */

public class EnemySweep extends Enemy.Bouncing {
    private Random.RandomDifferentElement<Color> generator;
    private Color random;

    public EnemySweep(GameLevel level) {
        super(level.spawnPoint(), 16, 16, level);
        setVelX(10 * (level.getRandom().random() < .5 ? -1 : 1));
        setVelY(5 * (level.getRandom().random() < .5 ? -1 : 1));
        generator = getLevel().getRandom().new RandomDifferentElement<>(RainbowText.rainbow);
    }

    @Override
    public void tick() {
        super.tick();
        random = generator.get();
        //handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
        getLevel().getNonentities().add(new Trail(this, random, 255));
    }

    @Override
    public void render(Graphics g) {
        super.render(g, random);
    }
}
