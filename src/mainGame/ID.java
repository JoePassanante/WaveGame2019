package mainGame;

/**
 * Used to easily identify each game entity
 * @author Brandon Loehle
 * 5/30/16
 */

public enum ID {
	// How many of these should be spawned (will be changed + or - 20% unless marked as 1(for boss)
	// -1 is a non-enemy entity
	EnemyBasic(8),
    EnemyBoss(1),
    EnemyFast(15),
	EnemySmart(10),
	EnemyBurst(10),
	EnemySweep(35),
	EnemyShooter(3),
	EnemyRocketBoss(2),
	BossEye(1),
	EnemyShooterMover(3),
	EnemyShooterSharp(1),
    EnemyBossBomb(1),
    EnemyBossBombBullet(1),
    EnemyBossBullet(1),
    EnemyShooterBullet(1),
    EnemyRocketBossMissile(1),
    // non-enemies
    PickupHealth(-1),
    PickupSize(-1),
    PickupLife(-1),
    PickupScore(-1),
    PickupFreeze(-1),
    EnemyBurstWarning(-1),
    EnemyRocketBossOff(-1),
	Firework(-1),
	FireworkSpark(-1),
	CircleTrail(-1),
    Levels1to10Text(-1),
	Player(-1),
	Trail(-1),
	Waves(-1),
	Menu(-1),
	Shield(-1);

    private int difficulty;

    ID(int d) {
        difficulty = d;
    }

    public int getDifficuty() {
        return difficulty;
    }
}
