package game.menu;

import game.GameLevel;
import game.GameWindow;
import game.Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameAvatar extends Menu {
    public GameAvatar(Menu m) {
        super(m);
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // display background

        g.setColor(Color.white);
        Font f = new Font("Amoebic", Font.BOLD, 80);
        GameWindow.drawStringCentered(g, f,"Select your colors!", 960, 125);
    }

    private static class Button extends MenuButton.TextButton { // button to change the color of a player
        public Button(double x, double y, GameLevel level, String t, Font f, Color r) {
            super(x, y, level, p -> p.setColor(r), t, f, r);
        }

        @Override
        public void collide(Player p) {
            if(p != null) {
                super.collide(p);
            }
        }
    }

    @Override
    public void start() {
        super.start();
        getEntities().addAll(getPlayers()); // display players so they can select their colors
        Font f = new Font("Amoebic", Font.PLAIN, 100);
        getEntities().add(new Button(300, 625, this, "\uD83D\uDCA6", f, Color.blue));
        getEntities().add(new Button(500, 625, this, "\u2744", f, Color.cyan));
        getEntities().add(new Button(700, 625, this, "\uD83C\uDF40", f, Color.green));
        getEntities().add(new Button(900, 625, this, "\uD83C\uDF38", f, Color.magenta));
        getEntities().add(new Button(1100, 625, this, "\uD83C\uDF4A", f, Color.orange));
        getEntities().add(new Button(1300, 625, this, "\ud83C\uDF51", f, Color.pink));
        getEntities().add(new Button(1500, 625, this, "\uD83D\uDD25", f, Color.red));
        getEntities().add(new Button(1700, 625, this, "\u2600", f, Color.yellow));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if(new Rectangle(850, 900, 200, 90).contains(e.getPoint())) {
            getState().pop();
        }
    }

    @Override
    public void end() {
        super.end();
        getEntities().removeIf(Button.class::isInstance); // remove color selections
    }
}



