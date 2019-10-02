package mainGame;

public class WaterTheme extends Theme {
    public WaterTheme() {
        super("src/images");
    }

    @Override
    public void initialize() {
        put(ID.EnemyBasic, "spaceship2_old.png");
        put(ID.EnemyBoss, "EnemyBossJellyfish.png");
        put(ID.EnemyBurst, "pufferfish.png");
        put(ID.EnemyFast, "Rocket_BossShark.png");
        put(ID.EnemyRocketBoss, "Rocket_Boss_Off_Sub.png");
        put(ID.EnemyRocketBossMissile, "Rocket_Boss.png");
        put(ID.EnemyShooter, "spaceship4sub.png");
        put(ID.EnemyShooterMover, "spaceship2_big_sub.png");
        put(ID.EnemyShooterSharp, "spaceship1sub.png");
        put(ID.EnemySmart, "spaceship3sub.png");
        put(ID.Waves, "Water.jpg");
        put(ID.Menu, "Water.jpg");
        put(ID.PickupSize, "ShrinkAbility0.png");
        put(ID.PickupScore, "PickupCoin.png");
        put(ID.PickupLife, "PickupLife.png");
        put(ID.PickupHealth, "PickupHealth.png");
        put(ID.PickupFreeze, "freezeAbilitySnowflake.png");
        put(ID.BossEye,"bosseye.png");
        put(ID.Shield, "shield1.png");
    }
}
