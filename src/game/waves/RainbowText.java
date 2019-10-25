package game.waves;

import game.GameEntity;
import game.GameLevel;
import game.GameWindow;
import game.Player;
import util.Random;

import java.awt.*;

/**
 * This is the text you see before each set of 10 levels
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class RainbowText extends GameEntity {
	private String text;
	public static final Color[] rainbow = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.PINK,
            Color.RED,
            Color.YELLOW
	};

	private Random.RandomDifferentElement<Color> generator;
	private Color color;

	public RainbowText(double x, double y, String t, GameLevel level) {
		super(x, y, 0, 0, level);
		text = t;
		setHealth(150);
		generator = getLevel().getRandom().new RandomDifferentElement<>(rainbow);
		color = generator.get();
	}

    @Override
    public void collide(Player p) {
        // TODO: fun rainbow ripple collision animation
    }

    @Override
	public void tick() {
	    setHealth(getHealth() - 1);
        // Controls color switch
        if (getHealth() % 15 == 0) {
            color = generator.get(); // set the new random color
        }
        super.tick();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
        GameWindow.drawStringCentered(
            g, new Font("Amoebic", Font.BOLD, 125), text, (int)getPosX(), (int)getPosY()
        );
	}
}
