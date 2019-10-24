package game;

import java.awt.*;

public class Trail extends GameEntity.Disappearing {
	private Color color;

	public Trail(GameEntity e, Color c, double l) {
	    this(e.getPosX(), e.getPosY(), e.getWidth(), e.getHeight(), c, l, e.getLevel());
    }

	public Trail(double x, double y, double width, double height, Color color, double life, GameLevel level) {
		super(new Point.Double(x, y), width, height, level);
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
