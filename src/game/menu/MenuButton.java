package game.menu;

import game.GameEntity;
import game.GameLevel;
import game.GameWindow;
import game.Player;

import java.awt.*;
import java.util.function.Consumer;

public class MenuButton extends GameEntity {
    private Rectangle bounds;
    private Consumer<Player> collide;

    public MenuButton(double x, double y, double w, double h, GameLevel level, Consumer<Player> c) {
        super(x, y, w, h, level);
        refer(level.getTheme().get(this));
        bounds = new Rectangle.Double(x,y,w,h).getBounds();
        collide = c;
    }

    @Override
    public void collide(Player p) {
        super.collide(p);
        collide.accept(p);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public static class TextButton extends MenuButton {
        private String text;
        private Font font;
        private Color color;
        private Rectangle bounds;

        public void setColor(Color c) {
            color = c;
        }
        @Override
        public Rectangle getBounds() {
            return bounds;
        }

        public TextButton(double x, double y, GameLevel level, Consumer<Player> c, String t, Font f) {
            this(x,y,level,c,t,f,Color.white);
        }

        public TextButton(double x, double y, GameLevel level, Consumer<Player> c, String t, Font f, Color r) {
            super(x, y, 0, 0, level, c);
            text = t;
            font = f;
            color = r;
            bounds = new Rectangle();
        }

        @Override
        public void render(Graphics g) {
            g.setColor(color);
            Rectangle draw = GameWindow.drawStringCentered(g, font, " "+text+" ", (int)getPosX(), (int)getPosY(), Color.black, color);
            bounds.setRect(getPosX() - draw.getWidth()/2, getPosY() - draw.getHeight()*5/4, draw.getWidth(), draw.getHeight());
            super.render(g);
        }
    }

    public static class MuteButton extends MenuButton {
        private boolean silence;

        public float getVolume() {
            return silence ? -48f : -24f;
        }

        public MuteButton(double x, double y, double w, double h, GameLevel l) {
            super(x, y, w, h, l, l.getPlayers()::remove);
        }

        @Override
        public void collide(Player p) {
            silence ^= true;
            refer(getLevel().getTheme().get((silence ? "Un" : "") + getClass().getSimpleName()));
        }
    }
}
