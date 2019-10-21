package game;

import java.awt.*;

public class Trail extends GameObject.Disappearing {
	private Color color;

	public Trail(Point.Double loc, Color color, int width, int height, double life, GameLevel level) {
		super(loc, width, height, level);
		this.color = color;
		this.setWidth(width);
		this.setHeight(height);
		this.setHealth(Math.min(255, life));
	}

    @Override
    public void collide(Player p) {
        // TODO: fancy trail dissolve animation
    }

    public void tick() {//slowly fades each square
        setHealth(getHealth()*.8 - .2);
	    super.tick();
	}

	@Override
	public void render(Graphics g) {
		g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)getHealth()));
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}
}
