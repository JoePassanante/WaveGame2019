package game.waves;

import game.*;
import game.enemy.*;
import game.menu.Menu;
import game.pickup.*;
import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
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
    private int maxTick;
    private int currentTick;
    private RainbowText text;

    private static class Spawn {
        private Supplier<Function<GameLevel, GameObject>>
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
                EnemyBoss::new
                //EnemyRocketBoss::new
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
    private Supplier<Function<GameLevel, GameObject>> randomEnemy;

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
            new Point.Double(getDimension().getWidth() / 2,  getDimension().getHeight() / 2),
            "Level " + getNumber(),
            this
        );
        currentTick = 0;
    }

    /**
     * Tick spawns new enemies depending on the spawn limit.
     */
    public void tick() {
        super.tick();

        if(currentTick == 0) {
            add(text);
            addAll(getPlayers());
            if(getNumber() > 1) {
                add(spawn.randomPickup.get().apply(this));
            }
        }
        else if(currentTick == 100) { // after 3 seconds, remove the level text
            remove(text);
        }
        else if(currentTick >= maxTick) {
            clear();
            getState().pop();
            getState().push(new Waves(this));
            if((getNumber()+1) % 5 == 0) {
//                getState().push(new Upgrades(this, spawn.randomPickup));
                getState().push(new Boss(this, spawn.randomBoss));
            }
        }
        else if(stream().noneMatch(Player.class::isInstance)) {
            clear();
            getState().pop();
            getState().push(new GameOver(this));
        }
        else if(stream().filter(go -> go.getClass().getName().contains("Enemy")).count() < getPlayers().size() + getNumber()*currentTick/maxTick) {
            add(randomEnemy.get().apply(this));
            System.out.println("Spawning: " + get(size()-1).getClass().getName());
        }

        currentTick += 1;
    }

    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);

        if(GameClient.devMode){
            g.setFont(new Font("Amoebic", Font.BOLD, 25));
            g.drawString("GameObjects: " + size(), getDimension().width-300, getDimension().height-200);
//            g.drawString("Enemies: " + stream().filter(go -> go.getClass().getName().contains("Enemy")).count(), getDimension().width-300, getDimension().height-200);
//            g.drawString("Pickups: " + stream().filter(go -> go.getClass().getName().contains("Pickup")).count(), getDimension().width-300, getDimension().height-150);
//            g.drawString("Trails: " + stream().filter(go -> go.getClass().getName().contains("Trail")).count(), getDimension().width-300, getDimension().height-50);
//          g.drawString("FPS: " + fps, getGameDimension().width-300, getGameDimension().height-100);
        }

        g.setFont(new Font("Amoebic", Font.BOLD, 30));

        g.drawString("Score: " + getScore(), 15, 25);
        g.drawString("Level: " + getNumber(), 15, 75);
        g.drawString("Level Progress: " + 100*currentTick/maxTick + "%", 15, 175);
        g.drawString("Health: " + getPlayers().stream().mapToDouble(GameObject::getHealth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 1050);
        g.drawString("Size: " + getPlayers().stream().mapToDouble(GameObject::getWidth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 225);

//        Image shieldImg = getTheme().get("shield" + (int)getPlayers().stream().mapToDouble(Player::getArmor).average().orElse(1.0)*5.0 + 1);
//        g.drawImage(shieldImg, 440, 1010, 40, 40, null);
//        g.drawString(getPlayers().stream().mapToDouble(Player::getArmor).mapToObj(Double::toString).collect(Collectors.joining(",")), 500, 1040);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(GameClient.devMode || true) {
            if (key == KeyEvent.VK_P) {
                // game.setPaused(false);
            }
            if (key == KeyEvent.VK_U) {
                getState().push(new Upgrades(this, spawn.randomPickup));
            }
            if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_E) {
                currentTick = maxTick;
            }
        }
        if (key == KeyEvent.VK_ESCAPE) {
            getPlayers().clear();
            getState().pop();
        }
        super.keyPressed(e);
    }
}
