package game.enemy;

import game.GameEntity;
import game.GameLevel;
import util.Random;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A warning that is displayed before EnemyBurst comes across the screen
 *
 * @author Brandon Loehle 5/30/16
 */

public class EnemyBurstWarning extends GameEntity {
    private int timer;
    private Random.RandomDifferentElement<Color> generator;
    private Color color;

    public EnemyBurstWarning(Point2D.Double loc, double width, double height, GameLevel level) {
        super(loc.x, loc.y, width, height, level);
        timer = 10;
        generator = level.getRandom().new RandomDifferentElement<>(Color.white, Color.red);
        color = generator.get();
    }

    @Override
    public void tick() {
        super.tick();
        timer += 1;
        if (timer >= 60) {
            getLevel().getEntities().remove(this);
        } else if (timer % 10 == 0) {
            color = generator.get();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g, color);
    }
}
