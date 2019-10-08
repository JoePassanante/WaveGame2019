package game.upgrade;

import game.Player;
import game.waves.Waves;

/**
 * The upgrades that a user can have (they modify the game for the user)
 * 
 * @author Brandon Loehle 5/30/16
 *
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
		game.getHandler().clear();
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
		game.getHandler().clear();
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

	//returns the ability the player has
	public String getAbility() {
		return ability;
	}

	/**
	 * Activate an upgrade
	 * 
	 * @param name
	 */
	public void activateUpgrade(String name) {
		if (name.equals("clearscreenability")) {
			ability = "clearScreen";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(3);
		} else if (name.equals("decreaseplayersize")) {
			decreasePlayerSize();
		} else if (name.equals("extralife")) {
			extraLife();
		} else if (name.equals("healthincrease")) {
			healthIncrease();
		} else if (name.equals("healthregeneration")) {
			healthRegeneration();
		} else if (name.equals("improveddamageresistance")) {
			improvedDamageResistance();
		} else if (name.equals("levelskipability")) {
			ability = "levelSkip";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(1);
		} else if (name.equals("freezetimeability")) {
			ability = "freezeTime";
            game.getHUD().setAbility(ability);
            game.getHUD().setAbilityUses(5);
		} else if (name.equals("speedboost")) {
		    // speedBoost();
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
