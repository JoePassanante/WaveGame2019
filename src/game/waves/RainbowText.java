package game.waves;

import game.*;
import util.Random;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * This is the text you see before each set of 10 levels
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 * @author Aaron Paterson 11/22/19
 *
 */

public class RainbowText extends GameEntity {
	private String text;
	private double elevation;
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
		elevation = y;
		text = t;
		setHealth(150);
		generator = getLevel().getRandom().new RandomDifferentElement<>(rainbow);
		color = generator.get();
	}

    @Override
    public void collide(Player p) {
        if(getLevel().getScore() % 10 == 0) { // make a ripple every ten ticks
            Path2D.Double ripple = new Path2D.Double();
            ripple.append(p.getBounds().getPathIterator(null), false);
            getLevel().getNonentities().add(new Trail.Outline(ripple, color, 255 * p.getHealth(), getLevel()));
            p.setHealth(Math.min(p.getHealth()+.5, p.getMaxHealth()));
        }
    }

    @Override
	public void tick() {
	    setHealth(getHealth() - 1);
        // controls color switch
        if (getHealth() % 15 == 0) {
            color = generator.get(); // set the new random color
        }
        super.tick();
	}

	@Override
	public void render(Graphics g) {
        Rectangle r = GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 125), text,
            (int)getPosX(), (int)elevation, color, color);
        setWidth(r.getWidth());
        setHeight(r.getHeight()*3/4);
        setPosY(elevation-getHeight());
	}
}
