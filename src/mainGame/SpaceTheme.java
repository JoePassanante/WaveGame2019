package mainGame;

public class SpaceTheme extends Theme {
    public SpaceTheme() {
        super("src/images");
    }

    @Override
    public void initialize() {
        put(ID.EnemyBasic, "spaceship2_old.png");
        put(ID.EnemyBoss, "EnemyBossRed.png");
        put(ID.EnemyBurst, "asteroid.png");
        put(ID.EnemyFast, "Rocket_BossRed.png");
        put(ID.EnemyRocketBoss, "Rocket_Boss_White.png");
        put(ID.EnemyRocketBossOff, "Rocket_Boss_Off_White.png");
        put(ID.EnemyRocketBossMissile, "Rocket_Boss.png");
        put(ID.EnemyShooter, "spaceship1yellow.png");
        put(ID.EnemyShooterMover, "spaceship2_big.png");
        put(ID.EnemyShooterSharp, "spaceship1Red.png");
        put(ID.EnemySmart, "spaceship3blue.png");
        put(ID.Waves, "space2.jpg");
        put(ID.Menu, "Background.png");
        put(ID.PickupSize, "ShrinkAbility0.png");
        put(ID.PickupScore, "PickupCoin.png");
        put(ID.PickupLife, "PickupLife.png");
        put(ID.PickupHealth, "PickupHealth.png");
        put(ID.PickupFreeze, "freezeAbilitySnowflake.png");
        put(ID.BossEye,"bosseye.png");
        put(ID.Shield, "shield1.png");
    }
}
