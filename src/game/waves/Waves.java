package game.waves;

import game.*;
import game.enemy.*;
import game.menu.Menu;
import game.pickup.*;
import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class is the main level of the waves game mode.
 * @author Joe Passanante 11/28/17
 * @author Aaron Paterson 10/17/19
 */

public class Waves extends GameLevel {
    private int currentTick;
    private int maxTick;
    private RainbowText text;

    private static class Spawn {
        private Supplier<Function<GameLevel, GameEntity>>
            randomEasyEnemy,
            randomHardEnemy,
            randomBoss,
            randomPickup;

        private Spawn(Random rng) {
            randomEasyEnemy = rng.new RandomDifferentElement<>(
                EnemyBasic::new,
                EnemyBurst::new,
                EnemyFast::new,
                EnemyShooter::new,
                EnemySmart::new
            );
            randomHardEnemy = rng.new RandomDifferentElement<>(
                EnemyShooterMover::new,
                EnemyShooterSharp::new,
                EnemySweep::new
            );
            randomBoss = rng.new RandomDifferentElement<>(
                EnemyBoss::new,
                EnemyRocketBoss::new
                //BossEye::new
            );
            randomPickup = rng.new RandomDifferentElement<>(
                PickupFreeze::new,
                PickupHealth::new,
                PickupLife::new,
                PickupScore::new,
                PickupSize::new
            );
        }
    }

    private Spawn spawn;
    private Supplier<Function<GameLevel, GameEntity>> randomEnemy;

    public Waves(Menu m) {
        this(m, new Spawn(m.getRandom()), 600);
    }

    private Waves(Waves w) {
        this(w, w.spawn, w.maxTick);
    }

    private Waves(GameLevel level, Spawn s, int m) {
        super(level);
        spawn = s;
        maxTick = m;
        currentTick = 0;
        System.out.println("New level with:");
        randomEnemy = getRandom().new RandomDifferentElement<>( IntStream // spawn a different enemy every time
            .rangeClosed(0, getNumber()/5 + 1)
            .boxed()
            .map(i -> i < 3 || getRandom().random() < .5 ? spawn.randomEasyEnemy : spawn.randomHardEnemy)
            .map(Supplier::get)
            .peek(go -> System.out.println(go.apply(this).getClass().getName()))
            .collect(Collectors.toList())
        );
        text = new RainbowText(
            getDimension().getWidth() / 2,  getDimension().getHeight() / 2,
            "Level " + getNumber(),
            this
        );
    }

    /**
     * Tick spawns new enemies depending on the spawn limit.
     */
    public void tick() {
        super.tick();

        if(currentTick == 0) {
            getEntities().add(text);
            if(getNumber() == 1) {
                getEntities().addAll(getPlayers());
            }
            else if(getNumber() > 1) {
                getEntities().add(spawn.randomPickup.get().apply(this));
            }
        }
        else if(currentTick >= maxTick) {
            setScore(getScore() + 100);
            getEntities().retainAll(getPlayers());
            getState().pop();
            getState().push(new Waves(this));
            if (getNumber() % 5 == 0) {
                getState().push(new Upgrades(this, spawn.randomPickup));
                getState().push(new Boss(this, spawn.randomBoss));
            }
        }
        else if(Collections.disjoint(getEntities(), getPlayers())) {
            getEntities().clear();
            getState().pop();
            getState().peek().setScore(getScore());
            getState().peek().setScore(getScore());
        }
        else if(getEntities().stream().filter(Enemy.class::isInstance).count() < getNumber()*currentTick/maxTick + 1) {
            GameEntity ge = randomEnemy.get().apply(this);
            System.out.println("Spawning: " + ge.getClass().getName());
            getEntities().add(ge);
        }

        currentTick += 1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(GameClient.devMode || true) {
            if (key == KeyEvent.VK_U) {
                getState().push(new Upgrades(this, spawn.randomPickup));
            }
            else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E) {
                currentTick = maxTick;
            }
//          else if(key == KeyEvent.VK_P) {
            // game.setPaused(false);
//           }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            getEntities().clear();
            getPlayers().clear();
            getState().pop();
        }
        super.keyPressed(e);
    }
}
