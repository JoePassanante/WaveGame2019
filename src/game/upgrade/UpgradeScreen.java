package game.upgrade;

import game.GameState;
import game.Player;
import game.waves.Waves;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * After completing a boss, this screen appears. The upgrade stays effective the
 * rest of the game.
 * @author Brandon Loehle 5/30/16 
 */

public class UpgradeScreen extends GameState {
    private String text;
	private String[] abilities = {
        "clearscreenability",
        "decreaseplayersize",
        "extralife",
        "healthincrease",
        "healthregeneration",
        "improveddamageresistance",
        "levelskipability",
        "freezetimeability",
        "speedboost"
    };
	private ArrayList<String> currentAbilities = new ArrayList<>();
	private int index1, index2, index3;
	private Waves game;

	public UpgradeScreen(Waves waves) {
	    game = waves;
		addPaths();
		resetPaths();
		setIndex();
		text = "";
	}

    @Override
	public void tick() {

	}

	public void render(Graphics g) {
		Font font = new Font("Amoebic", 1, 175);
		text = "Select an Upgrade!";
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(text, (int)game.getHandler().getGameDimension().getWidth() / 2 - getTextWidth(font, text) / 2, 200);
		// All pictures are 1721 x 174
        g.drawImage(game.getHandler().getTheme().get(currentAbilities.get(index1)), 100, 300, 1721, 174, null);
        g.drawImage(game.getHandler().getTheme().get(currentAbilities.get(index2)), 100, 300 + (60 + (int) game.getHandler().getGameDimension().getHeight() / 6), 1721, 174, null);
        g.drawImage(game.getHandler().getTheme().get(currentAbilities.get(index3)), 100, 300 + 2 * (60 + (int) game.getHandler().getGameDimension().getHeight() / 6), 1721, 174, null);
	}

    /**
	 * Reset the paths to each picture
	 */
	public void resetPaths() {
        abilities[0] = "clearscreenability";
        abilities[1] = "decreaseplayersize";
        abilities[2] = "extralife";
        abilities[3] = "healthincrease";
        abilities[4] = "healthregeneration";
        abilities[5] = "improveddamageresistance";
        abilities[6] = "levelskipability";
        abilities[7] = "freezetimeability";
        abilities[8] = "speedboost";
	}
	//generate paths for locating the image
	public void addPaths() {
		for (int i = 0; i < 9; i++) {
			currentAbilities.add(abilities[i]);
		}
	}
	
	public int getIndex(int maxIndex) {
		int index = game.getHandler().getRandom().nextInt(maxIndex);
		if (index == 1 && game.getHandler().getPlayers().stream().map(Player::getHeight).allMatch(h -> h <= 3)) {
			return getIndex(maxIndex);
		}
		if (index == 4 && game.getHUD().getRegen()) {
			return getIndex(maxIndex);
		}
//		if (index == 8 && Player.playerSpeed > 10) {
//			return getIndex(maxIndex);
//		}
		if (index == 5 && game.getHandler().getPlayers().stream().map(Player::getDamage).allMatch(h -> h <= 1))
		{
			return getIndex(maxIndex);
		}
		return index;
	}

	/**
	 * Gets 3 index's of pictures, and ensures that they are all different. These 3
	 * index's will load 3 different upgrade options for the user
	 */
	public void setIndex() {
		index1 = getIndex(8);
		index2 = getIndex(8);
		if (index2 == index1) {
			index2++;
			if (index2 > 8) {
				index2 = 1;
			}
		}
		index3 = getIndex(8);
		while (index3 == index1 || index2 == index3 || index1 == index2)
		{
			index2 = getIndex(8);
			index3 = getIndex(8);
		}
	}

	public int getTextWidth(Font font, String text) {
		AffineTransform at = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(at, true, true);
		return (int) (font.getStringBounds(text, frc).getWidth());
	}

	/**
	 * Get the path of the image.
	 * 
	 * @param x
	 *            can be either a 1, 2, or 3 (as there are only three upgrade
	 *            options shown at one time)
	 * @return String path of image
	 */
	public String getPath(int x) {
		if (x == 1) {
			return abilities[index1];
		} else if (x == 2) {
			return abilities[index2];
		} else {
			return abilities[index3];
		}
	}

	/**
	 * Removes the path of the image that is chosen by the user, so that it is never
	 * offered again
	 * 
	 * @param x
	 *            can be either a 1, 2, or 3 (as there are only three upgrade
	 *            options shown at one time)
	 */
	public void removeUpgradeOption(int x) {
		if (x == 1) {
            abilities[index1] = null;
		} else if (x == 2) {
            abilities[index2] = null;
		} else {
            abilities[index3] = null;
		}
	}
	public void resetIndexes() {
		setIndex();
	}

    @Override
    public void mousePressed(MouseEvent e) {
        if (mouseOver(e.getX(), e.getY(), 100, 300, 1721, 174)) {
            game.getUpgrades().activateUpgrade(getPath(1));
            //upgradeScreen.removeUpgradeOption(1);//remove that upgrade option since it was chosen
            resetIndexes();
            game.setState(null);
        } else if (mouseOver(e.getX(), e.getY(), 100, 300 + (60 + (int) game.getHandler().getGameDimension().getHeight() / 6), 1721, 174)) {
            game.getUpgrades().activateUpgrade(getPath(2));
            //upgradeScreen.removeUpgradeOption(2);//remove that upgrade option since it was chosen
            resetIndexes();
            game.setState(null);
        } else if (mouseOver(e.getX(), e.getY(), 100, 300 + 2 * (60 + (int) game.getHandler().getGameDimension().getHeight() / 6), 1721, 174)) {
            game.getUpgrades().activateUpgrade(getPath(3));
            //upgradeScreen.removeUpgradeOption(3);//remove that upgrade option since it was chosen
            resetIndexes();
            game.setState(null);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
