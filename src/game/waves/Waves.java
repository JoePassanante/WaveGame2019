package game.waves;

import game.*;
import game.enemy.*;
import game.pickup.*;
import game.upgrade.UpgradeScreen;
import game.upgrade.Upgrades;
import util.Random;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Waves extends GameMode {
	private int currentLevelNum;
    private Handler handler;
    private Client client;
    private HUD hud;
    private GameState upgradeScreen;
    private GameState gameOver;
    private Upgrades upgrades;

    public Handler getHandler() {
        return handler;
    }
    public HUD getHUD() {
        return hud;
    }

    public GameState getUpgradeScreen() {
        return upgradeScreen;
    }
    public GameState getGameOver() {
        return gameOver;
    }
    public Upgrades getUpgrades() {
        return upgrades;
    }

    private Supplier<BiFunction<Point.Double, Handler, GameObject>> randomEasyEnemy, randomHardEnemy, randomBoss, randomPickup;

    public Waves(Client c) {
        client = c;
        handler = client.getHandler();

        randomEasyEnemy = handler.getRandom().new RandomDifferentElement<>(
            EnemyBasic::new,
            EnemyBurst::new,
            EnemyFast::new,
            EnemyShooter::new,
            EnemySmart::new
        );
        randomHardEnemy = handler.getRandom().new RandomDifferentElement<>(
            EnemyShooterMover::new,
            EnemyShooterSharp::new,
            EnemySweep::new
        );
        randomBoss = handler.getRandom().new RandomDifferentElement<>(
            EnemyBoss::new,
            EnemyRocketBoss::new
//          BossEye::new
        );
        randomPickup = handler.getRandom().new RandomDifferentElement<>(
            PickupFreeze::new,
            PickupHealth::new,
            PickupLife::new,
            PickupScore::new,
            PickupSize::new
        );

        hud = new HUD(this);

        upgradeScreen = new UpgradeScreen(this);
        upgrades = new Upgrades(this);
        gameOver = new GameOver(c, this);
    }

	/**
	 * Instantiates and ticks currentLevel.
	 */

    @Override
	public void tick() {
        if(getState()==null) {
            currentLevelNum += 1;

            hud.setLevel(currentLevelNum);
            handler.setLevel(currentLevelNum);
            client.getPlayers().forEach(handler.getPlayers()::add);

            if(this.currentLevelNum%5 != 0) {
                System.out.println("New Normal Level");

                setState(new Level(client, this, 60 * (20), currentLevelNum, currentLevelNum,
                    Random.reduce(getHandler().getRandom().new RandomDifferentElement<>(IntStream
                        .rangeClosed(0, Math.min(5, currentLevelNum / 5))
                        .mapToObj(i -> i <= 3 || getHandler().getRandom().random() < .5 ? randomEasyEnemy : randomHardEnemy)
                        .map(Supplier::get)
                        .collect(Collectors.toList())
                    ))
                ));
                //you can test specific enemies like this:
                //currentLevel = new Level( this,1200, currentLevelNum, currentLevelNum, EnemyBurst::new);

                if (currentLevelNum > 1) {
                    Point.Double pick = new Point.Double(
                            getHandler().getGameDimension().getWidth()*(getHandler().getRandom().random()+1.0/3),
                            getHandler().getGameDimension().getHeight()*(getHandler().getRandom().random()+1.0/3)
                    );
                    getHandler().getPickups().add(randomPickup.get().apply(pick, getHandler()));
                }
            }
            else {
                System.out.println("New Boss Level");
                setState(new Level(client,this,-1, currentLevelNum, 1,  randomBoss.get()));
            }
		}

        super.tick();
    }

	/**
	 * Renders any static images for the level.
	 * IE Background. 
	 */
	@Override
	public void render(Graphics g) {
        Image img = getHandler().getTheme().get(this);
        if(img != null) {
            g.drawImage(img, 0, 0, (int) getHandler().getGameDimension().getWidth(), (int) getHandler().getGameDimension().getHeight(), null);
        }
        if(getState() != null) {
            super.render(g);
        }
	}

    /**
	 * @param hardReset - if false only enemies are wiped. If true gamemode is completely reset. 
	 */
	public void resetMode(boolean hardReset) {
        if(hardReset) {
            currentLevelNum = 0;
            getHandler().getPlayers().forEach(p -> p.setPlayerSize(32));
            getHUD().resetHealth();
            getHUD().setExtraLives(0);
            getHUD().setLevel(0);
            getHUD().setScore(0);
        }
        setState(null);
        getHandler().clear();
        getHandler().getPlayers().clear();
        getHandler().getPickups().clear();
    }

	public void resetMode() {
		resetMode(true);
	}
}
