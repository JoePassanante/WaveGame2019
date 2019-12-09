package game.walls;

import game.GameLevel;
import game.enemy.Enemy;
import game.enemy.EnemyWall;
import game.waves.RainbowText;

import java.awt.*;
import java.util.Collections;

public class Walls extends GameLevel { // simple side scroller game mode
    private RainbowText text;

    public Walls(GameLevel gl) {
        super(gl);

        text = new RainbowText(
            getDimension().getWidth() / 2,  getDimension().getHeight() / 2,
            getNumber() + "km",
            this
        );
        text.setVelX(-2*getNumber());
    }

    @Override
    public void start() {
        super.start();
        getEntities().retainAll(getPlayers());
        getEntities().add(text);
    }

    @Override
    public void end() {
        super.end();
        getState().push(new Walls(this));
    }

    @Override
    public void render(Graphics g) {
        super.render(g, -getNumber()*getCurrentTick(), 0);
    }

    @Override
    public void tick() {
        super.tick();
        if(Collections.disjoint(getEntities(), getPlayers())) {
            getState().pop();
            getState().peek().setScore(getScore());
        }
        else if(getEntities().stream().filter(Enemy.class::isInstance).count() < 1 + getNumber()*getCurrentTick()/getMaxTick()) {
            double space = getBounds().getHeight() / (getNumber() + 1.0);
            double center = getBounds().getHeight()/2;
            center += Math.random()*(getBounds().getHeight()-space)/2;
            center -= Math.random()*(getBounds().getHeight()-space)/2;
            EnemyWall wall = new EnemyWall(getBounds().getWidth(), center, 100*getNumber(), space, this);
            wall.setVelX(-10-2*getNumber());
            getEntities().add(wall);
        }
    }
}
