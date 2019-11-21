package game.waves;

import game.*;
import game.enemy.*;
import game.menu.Menu;
import game.pickup.*;
import game.Transition;
import util.Random;

import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.function.BiFunction;
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
    private RainbowText text;

    private static class Spawn {
        private Supplier<Function<GameLevel, GameEntity>>
            randomEasyEnemy,
            randomHardEnemy,
            randomBoss,
            randomPickup;

        private Supplier<Function<GameLevel, BiFunction<game.Performer, Performer, Transition>>>
            transition;

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
                PickupArmor::new,
                PickupClear::new,
                PickupFreeze::new,
                PickupHealth::new,
                PickupLife::new,
                PickupRegen::new,
                PickupScore::new,
                PickupSize::new,
                PickupSkip::new,
                PickupSpeed::new
            );
            transition = rng.new RandomDifferentElement<>( // java wont let us use ::new on inner class constructors ;-;
                Transition.Modulo::vertical,
                Transition.Modulo::horizontal,
                Transition.Modulo::diagonal,
                Transition.Modulo::radial
            );
        }
    }

    private Spawn spawn;
    private Supplier<Function<GameLevel, GameEntity>> randomEnemy;

    public Waves(Menu m) {
        this(m, new Spawn(m.getRandom()));
    }

    private Waves(Waves w) {
        this(w, w.spawn);
    }

    private Waves(GameLevel level, Spawn s) {
        super(level);
        spawn = s;
        System.out.println("New level with:");
        randomEnemy = getRandom().new RandomDifferentElement<>( IntStream // spawn a different enemy every time
            .rangeClosed(0, getNumber()/5 + 1)
            .boxed()
            .map(i -> i < 3 || getRandom().random() < .5 ? spawn.randomEasyEnemy : spawn.randomHardEnemy)
            .map(Supplier::get)
            .peek(go -> System.out.println(go.apply(this).getClass().getSimpleName()))
            .collect(Collectors.toList())
        );
        text = new RainbowText(
            getDimension().getWidth() / 2,  getDimension().getHeight() / 2,
            "Level " + getNumber(),
            this
        );
    }

    @Override
    public void start() {
        super.start();
        if(getNumber() == 1) {
            getEntities().addAll(getPlayers());
        }
        else if(getNumber() > 1) {
            getEntities().retainAll(getPlayers());
            getEntities().add(spawn.randomPickup.get().apply(this));
        }
        getEntities().add(text);
    }

    @Override
    public void end() {
        super.end();
        getState().push(new Waves(this));
        if (getNumber() % 5 == 0) {
            getState().push(new Upgrades(this, spawn.randomPickup));
            getState().push(new Boss(this, spawn.randomBoss));
            setMaxTick(60);
            getState().push(spawn.transition.get().apply(this).apply(this, getState().peek()));
        }
    }

    /**
     * Tick spawns new enemies and ends the game if all the players die.
     */
    public void tick() {
        super.tick();
        if(Collections.disjoint(getEntities(), getPlayers())) {
            getState().pop();
            getState().peek().setScore(getScore());
        }
        else if(getEntities().stream().filter(Enemy.class::isInstance).count() < 1 + getNumber()*getCurrentTick()/getMaxTick()) {
            GameEntity ge = randomEnemy.get().apply(this);
            System.out.println("Spawning: " + ge.getClass().getSimpleName());
            getEntities().add(ge);
        }
    }

     @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(GameClient.devMode || true) {
            if (key == KeyEvent.VK_U) {
                getState().push(new Upgrades(this, spawn.randomPickup));
            }
            else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E) {
                setCurrentTick(getMaxTick());
            }
            else if(key == KeyEvent.VK_P) {
                GameLevel pause = new Pause(this);
//                getState().push(new Transition(pause, pause, this));
                getState().push(pause);
//                getState().push(new Transition(pause, this, pause));
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            getEntities().clear();
            getPlayers().clear();
            getState().pop();
        }
        super.keyPressed(e);
    }
}
