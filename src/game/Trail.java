package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

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

	public static class Outline extends Trail {
	    private Path2D.Double outline;
        public Outline(Path2D.Double path, Color c, double life, GameLevel level) {
            super(
                path.getBounds().getX(), path.getBounds().getY(), path.getBounds().getWidth(), path.getBounds().getHeight(),
                c, life, level
            );
            outline = path;
            setHealth(life);
        }

        @Override
        public void tick() {
            setPosX(outline.getBounds().getX());
            setPosY(outline.getBounds().getY());
            setWidth(outline.getBounds().getWidth());
            setHeight(outline.getBounds().getHeight());
            outline.transform(AffineTransform.getTranslateInstance(-getPosX()-getWidth()/2, -getPosY()-getHeight()/2));
            outline.transform(AffineTransform.getScaleInstance(0x1.05p0,0x1.05p0));
            outline.transform(AffineTransform.getTranslateInstance(getPosX()+getWidth()/2, getPosY()+getHeight()/2));
            super.tick();
        }

        @Override
        public void render(Graphics g) {
            g.setColor(new Color(super.color.getRed(), super.color.getGreen(), super.color.getBlue(), (int)(255*Math.pow((1-(1/(1+getHealth()))),16))));
            ((Graphics2D)g).draw(outline);
        }
    }
}
