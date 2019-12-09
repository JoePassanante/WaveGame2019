package game.menu;

import game.GameLevel;
import game.GameWindow;
import game.Transition;
import game.walls.Walls;
import game.waves.Waves;

import java.awt.*;

public class GameMode extends Menu {
    private GameLevel mode;

    public GameMode(Menu m) {
        super(m);
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // display background

        g.setColor(Color.white);
        Font f = new Font("Amoebic", Font.BOLD, 80);
        GameWindow.drawStringCentered(g, f, "Select your game mode!", 960, 125);
    }

    public class Button extends MenuButton.TextButton { // button to change selected game mode
        private GameLevel other;
        Button(double x, double y, GameLevel l, GameLevel m, String t, Font f) {
            super(x,y,l,p -> mode = m,t,f); // set selected game mode when pressed
            other = m;
        }

        @Override
        public void tick() {
            setColor(mode == other ? Color.white : Color.gray);
        } // brighten when selected
    }

    @Override
    public void start() {
        super.start();
        getEntities().removeAll(getPlayers()); // hide players until game starts
        Font f = new Font("Amoebic", Font.BOLD, 100);
        setMaxTick(600);
        setClipped(true);
        Button
            waves = new Button(960,  500, this, new Waves(this),"Classic", f),
            walls = new Button(960,  800, this, new Walls(this),"Side scroller", f);
        getEntities().add(waves);
        getEntities().add(walls);
        waves.collide(null); // waves is selected by default
        setClipped(false);
    }

    @Override
    public void end() {
        super.end();
        getEntities().removeIf(Button.class::isInstance); // buttons are annoying in transitions
        if(mode != null) {
            mode.setTheme(getTheme());
            getEntities().addAll(getPlayers());
            setMaxTick(60);
            getState().push(mode);
            getState().push(Transition.Modulo.droplets(this).apply(this, getState().peek()));
        }
    }
}
