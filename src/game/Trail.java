package game;

import java.awt.*;

public class Trail extends GameEntity {
	private Color color;

	public Trail(GameEntity e, Color c, double l) {
	    this(e.getPosX(), e.getPosY(), e.getWidth(), e.getHeight(), c, l, e.getLevel());
    }

	public Trail(double x, double y, double width, double height, Color c, double life, GameLevel level) {
		super(x, y, width, height, level);
		color = c;
		setHealth(Math.min(255, life));
	}

    @Override
    public void tick() {//slowly fades each square
        setHealth(getHealth()*.8 - .2);
	    super.tick();
	}

	@Override
	public void render(Graphics g) {
	    super.render(g, new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)getHealth()));
	}
}
