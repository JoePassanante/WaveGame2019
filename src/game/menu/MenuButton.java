package game.menu;

import game.GameEntity;
import game.GameLevel;
import game.GameWindow;
import game.Performer;

import java.awt.*;

public class MenuButton extends Performer {
    private Rectangle bounds;

    public MenuButton(double x, double y, double w, double h, GameLevel level) {
        //super(x, y, w, h, level);
        refer(level.getTheme().get(this));
        bounds = new Rectangle.Double(x,y,w,h).getBounds();
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public static class TextButton extends MenuButton {
        private Rectangle bounds;
        private String text;
        private Font font;

        public TextButton(GameLevel level, double x, double y, double w, double h, String t, Font f) {
            super(x, y, w, h, level);
            text = t;
            font = f;
        }

        @Override
        public void render(Graphics g) {
            super.render(g);
            bounds = GameWindow.drawStringCentered(g, font, text, bounds.x, bounds.y);
        }
    }
}
