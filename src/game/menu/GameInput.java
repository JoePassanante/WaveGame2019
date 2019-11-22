package game.menu;

import game.Controller;
import game.GameLevel;
import game.GameWindow;
import game.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameInput extends Menu {
    private Controller wasd, ijkl, arrows, mouse;

    private static class Button extends MenuButton.TextButton {
        private Player player;
        public Button(double x, double y, GameLevel level, String t, Font f, Controller r) {
            super(x, y, level, level.getPlayers()::add, t, f, Color.gray);
            player = new Player(getPosX(), getPosY() - 100, r, getLevel());
        }

        @Override
        public void collide(Player p) {
            if(p == null) {
                if(!getLevel().getPlayers().contains(player)) {
                    super.collide(player);
                    setColor(Color.white);
                }
                else {
                    getLevel().getPlayers().remove(player);
                    setColor(Color.gray);
                }
            }
        }
    }

    public GameInput(Menu m) {
        super(m);
        wasd = new Controller.Keyboard(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.KEY_LOCATION_LEFT);
        ijkl = new Controller.Keyboard(KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.KEY_LOCATION_STANDARD);
        arrows = new Controller.Keyboard(KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.KEY_LOCATION_RIGHT);
        mouse = new Controller.Mouse();
    }

    @Override
    public void render(Graphics g) {
        super.render(g); //display background

        g.setColor(Color.white);
        Font f = new Font("Amoebic", Font.BOLD, 80);
        GameWindow.drawStringCentered(g, f, "Select your controllers!", 960, 125);
    }

    @Override
    public void start() {
        super.start();
        getPlayers().clear();
        Font f = new Font("Amoebic", Font.BOLD, 100);
        getEntities().add(new Button(500,  500, this, "|MOUSE|", f, mouse));
        getEntities().add(new Button(500,  800, this, "|SHIFT|W|A|S|D|", f, wasd));
        getEntities().add(new Button(1500, 500, this, "|SPACE|I|J|K|L|", f, ijkl));
        getEntities().add(new Button(1500, 800, this, "|^|<|v|>|SHIFT|", f, arrows));
    }

    @Override
    public void end() {
        super.end();
        getEntities().removeIf(Button.class::isInstance);
        if(getPlayers().isEmpty()) {
            getPlayers().add(new Player(960, 400, new Controller.Multi(wasd, arrows), this));
        }
    }
}
