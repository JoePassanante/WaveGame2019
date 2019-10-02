package mainGame;

/**
 * The upgrades that a user can have (they modify the game for the user)
 * 
 * @author Brandon Loehle 5/30/16
 *
 */
/**
 * 
 * @author Team B3
 * It contains all the upgrades
 * that are possibly avaiable for the player to obtain.
 * Descriptions of what the upgrades are are listed by the 
 * respective function 
 */
public class Upgrades {
	private Waves game;
	private String ability = "none";
	//constructor
	public Upgrades(Waves waves) {
		this.game = waves;
	}
	//Remove all enemies on the screen
	public void clearScreenAbility() {
		game.getHandler().clearEnemies();
		game.getHUD().setAbilityUses(game.getHUD().getAbilityUses() - 1);
		if (game.getHUD().getAbilityUses() == 0) {
			ability = "";
		}
	}
	//Shrink the player's size
	public void decreasePlayerSize() {
		game.getPlayer().setPlayerSize((int) (game.getPlayer().getHeight()/1.2));
	}
	//Add another life for the player
	public void extraLife() {
		game.getHUD().setExtraLives(game.getHUD().getExtraLives() + 1);
	}
	//increase the amount of health the player has
	public void healthIncrease() {
		game.getHUD().healthIncrease();
	}
	//Health comes back over time
	public void healthRegeneration() {
		game.getHUD().setRegen();
	}
	//The player takes less hit damage when an enemy hits them
	public void improvedDamageResistance() {
		game.getPlayer().setDamage(game.getPlayer().getDamage()-.25);
	}
	//Skip a level (p.s set dev mode to false to test if this works)
	public void levelSkipAbility() {
		game.getHandler().clearEnemies();
		game.getHUD().setLevel(game.getHUD().getLevel() + 1);
		game.getHUD().setAbilityUses(game.getHUD().getAbilityUses() - 1);
		if (game.getHUD().getAbilityUses() == 0) {
			ability = "";
		}

	}
	//Freeze the screen so enemies don't move, time would still tick on
	public void freezeTimeAbility() {
		game.getHandler().pause();
		game.getHUD().setAbilityUses(game.getHUD().getAbilityUses() - 1);
		if (game.getHUD().getAbilityUses() == 0) {
			ability = "";
		}
	}
	//increases the players speed
	public void speedBoost() {
		Player.playerSpeed *= 2;
	}
	//returns the ability the player has
	public String getAbility() {
		return ability;
	}

	/**
	 * Activate the correct upgrade
	 * 
	 * @param path
	 *            is to the image of the upgrade that was pressed by the user
	 */
	public void activateUpgrade(String path) {
		if (path.equals("/images/clearscreenability.png")) {
			ability = "clearScreen";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(3);
		} else if (path.equals("/images/decreaseplayersize.png")) {
			decreasePlayerSize();
		} else if (path.equals("/images/extralife.png")) {
			extraLife();
		} else if (path.equals("/images/healthincrease.png")) {
			healthIncrease();
		} else if (path.equals("/images/healthregeneration.png")) {
			healthRegeneration();
		} else if (path.equals("/images/improveddamageresistance.png")) {
			improvedDamageResistance();
		} else if (path.equals("/images/levelskipability.png")) {
			ability = "levelSkip";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(1);
		} else if (path.equals("/images/freezetimeability.png")) {
			ability = "freezeTime";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(5);
		} else if (path.equals("/images/speedboost.png")) {
			speedBoost();
		}
	}

	public void useAbility() {
	    if(ability.equals("clearScreen"))
	        clearScreenAbility();
	    if(ability.equals("levelSkip"))
	        levelSkipAbility();
	    if(ability.equals("freezeTime"))
            freezeTimeAbility();
    }
}
