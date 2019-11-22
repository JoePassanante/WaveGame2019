package game.enemy;

import game.GameLevel;
import game.Player;

import java.awt.*;
import java.awt.geom.Point2D;

public class EnemyWall extends Enemy.Disappearing {
    private Color color;
    public EnemyWall(double x, double y, double w, double h, GameLevel l) {
        super(new Point2D.Double(x, y), w, h, l);
        color = Color.white;
    }

    public void collide(Player p) {
        p.setHealth(p.getHealth()/2);
        color = Color.red;
        super.collide(p);
    }

    @Override
    public void render(Graphics g) {
        super.render(g, color);
    }
}
