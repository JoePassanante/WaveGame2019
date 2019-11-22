package game.menu;

import game.GameLevel;
import game.GameWindow;
import game.waves.Waves;

import java.awt.*;
import java.util.function.BooleanSupplier;

public class GameMode extends Menu {
    private GameLevel mode;

    public GameMode(Menu m) {
        super(m);
    }
        /*
        Font f;

        f = new Font("Amoebic", Font.BOLD, 130);
        // Waves One button
        buttons.add(new MenuButton.TextButton(602, 300, this, p -> {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getState().push(new Waves(this));
            setMaxTick(60);
            getState().push(Transition.Modulo.droplets(this).apply(this, getState().peek()));
        }, "One", f));
        // Waves Two button
        buttons.add(new MenuButton.TextButton(1052, 300, this, p -> {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getState().push(new Waves(this));
        }, "Two", f));
         */

    /*
    // Player Customization button
    buttons.add(new MenuButton.TextButton(850, 900, this, p -> {
        setMaxTick(60);
        getState().push(new Avatar(this));
        getState().push(Transition.Slide.horizontal(this).apply(this, getState().peek()));
    }, "Player customization", f));
     */

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
        getEntities().add(new Button(960,  800, this, new Waves(this),"Tunnel", f));
        setClipped(false);
    }

    @Override
    public void end() {
        super.end();
        getEntities().removeIf(Button.class::isInstance);
        if(mode != null) {
            getState().push(mode);
        }
    }
}
