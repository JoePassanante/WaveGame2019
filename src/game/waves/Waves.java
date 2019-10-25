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
            if (getNumber() % 5 == 4) {
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
        else if(
            getEntities().size() <
            getPlayers().size() +
            getEntities().stream().filter(Trail.class::isInstance).count() +
            getNumber()*currentTick/maxTick +
            1
        ) {
            GameEntity ge = randomEnemy.get().apply(this);
//          GameEntity ge = new EnemyRocketBoss(this); // or test specific enemies like this
            System.out.println("Spawning: " + ge.getClass().getName());
            getEntities().add(ge);
        }

        currentTick += 1;
    }

    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);

        if(GameClient.devMode){
            g.setFont(new Font("Amoebic", Font.BOLD, 25));
            g.drawString("Entities: " + getEntities().size(), getDimension().width-300, getDimension().height-200);
//            g.drawString("Enemies: " + stream().filter(go -> go.getClass().getName().contains("Enemy")).count(), getDimension().width-300, getDimension().height-200);
//            g.drawString("Pickups: " + stream().filter(go -> go.getClass().getName().contains("Pickup")).count(), getDimension().width-300, getDimension().height-150);
//            g.drawString("Trails: " + stream().filter(go -> go.getClass().getName().contains("Trail")).count(), getDimension().width-300, getDimension().height-50);
//          g.drawString("FPS: " + fps, getGameDimension().width-300, getGameDimension().height-100);
        }

        g.setFont(new Font("Amoebic", Font.BOLD, 30));

        g.drawString("Score: " + getScore(), 15, 25);
        g.drawString("Level: " + getNumber(), 15, 75);
        g.drawString("Level Progress: " + 100*currentTick/maxTick + "%", 15, 175);
        g.drawString("Health: " + getPlayers().stream().mapToDouble(GameEntity::getHealth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 1050);
        g.drawString("Size: " + getPlayers().stream().mapToDouble(GameEntity::getWidth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 225);

//        Image shieldImg = getTheme().get("shield" + (int)getPlayers().stream().mapToDouble(Player::getArmor).average().orElse(1.0)*5.0 + 1);
//        g.drawImage(shieldImg, 440, 1010, 40, 40, null);
//        g.drawString(getPlayers().stream().mapToDouble(Player::getArmor).mapToObj(Double::toString).collect(Collectors.joining(",")), 500, 1040);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(GameClient.devMode) {
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
