package game.menu;

import game.GameLevel;
import game.GameWindow;
import game.Transition;
import game.walls.Walls;
import game.waves.Waves;

import java.awt.*;
import java.util.function.BooleanSupplier;

public class GameMode extends Menu {
    private GameLevel mode;

    public GameMode(Menu m) {
        super(m);
    }

    @Override
    public void render(Graphics g) {
        super.render(g); //display background

        g.setColor(Color.white);
        Font f = new Font("Amoebic", Font.BOLD, 80);
        GameWindow.drawStringCentered(g, f, "Select your game mode!", 960, 125);
    }

    public class Button extends MenuButton.TextButton {
        private BooleanSupplier bs;
        Button(double x, double y, GameLevel l, GameLevel m, String t, Font f) {
            super(x,y,l,p -> mode = m,t,f);
            bs = () -> mode == m;
        }

        @Override
        public void tick() {
            setColor(bs.getAsBoolean() ? Color.white : Color.gray);
        }
    }

    @Override
    public void start() {
        super.start();
        Font f = new Font("Amoebic", Font.BOLD, 100);
        setMaxTick(600);
        setClipped(true);
        getEntities().add(new Button(960,  500, this, new Waves(this),"Classic", f));
        getEntities().add(new Button(960,  800, this, new Walls(this),"Side scroller", f));
        setClipped(false);
    }

    @Override
    public void end() {
        super.end();
        getEntities().removeIf(Button.class::isInstance);
        if(mode != null) {
            setMaxTick(60);
            getState().push(mode);
            getState().push(Transition.Modulo.droplets(this).apply(this, getState().peek()));
        }
    }
}
