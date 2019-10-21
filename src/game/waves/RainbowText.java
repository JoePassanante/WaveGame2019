package game.waves;

import game.GameLevel;
import game.GameObject;
import game.GameWindow;
import game.Player;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 * This is the text you see before each set of 10 levels
 * 
 * @author Brandon Loehle 5/30/16
 * @author David Nguyen 12/13/17
 *
 */

public class RainbowText extends GameObject {
	private String text;
	private int timer;
	private Color[] color = { Color.WHITE, Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE,
			Color.PINK, Color.YELLOW };
	private int index;

	public RainbowText(Point.Double point, String t, GameLevel level) {
		super(point, 0, 0, level);
		text = t;
		timer = 15;
	}

    @Override
    public void collide(Player p) {
        // TODO: fun rainbow ripple collision animation
    }

    @Override
	public void tick() {
	    timer -= 1;
        // Controls color switch
        if (timer == 0) {
            index = getLevel().getRandom().nextInt(9); // get a new random color
            timer = 15;
        }
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color[index]);// set the new random color
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 125), text, (int)getX(), (int)getY());
	}

    public double getTextWidth(Font font, String text) {
		AffineTransform affinetransform = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
		return font.getStringBounds(text, frc).getWidth();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)getX(), (int)getY(),0,0);
	}
}
