package game.menu;

import game.*;
import game.waves.RainbowText;
import util.Random;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Menu extends GameLevel.Unending {
    private Theme space, water, dvd;
    private Random.RandomDifferentElement<Color> fireworkColor;
    private MenuButton.MuteButton mute;

    public Menu(Menu m) {
        super(m);
        space = m.space;
        water = m.water;
        dvd = m.dvd;
        fireworkColor = m.fireworkColor;
        mute = m.mute;
    }

    public Menu(GameLevel g) {
        super(g);

        space = new Theme("space", getTheme());
        water = new Theme("water", getTheme());
        dvd = new Theme("dvd", space);
        System.out.println("Loading themes...");
        Stream.of(space, water, dvd).parallel().forEach(Runnable::run); // still not fast enough >:L
        setTheme(space);

        fireworkColor = getRandom().new RandomDifferentElement<>(RainbowText.rainbow);
        mute = new Button.MuteButton(20, 900, 100, 100, this);
    }

    private void resetTheme(Theme t) {
        getState().forEach(s -> s.setTheme(t));
        getState().peek().setClipped(true);
    }

    private static class Button extends MenuButton.TextButton {
        public Button(double x, double y, GameLevel level, Consumer<Player> c, String t, Font f) {
            super(x, y, level, c, t, f);
        }

        @Override
        public void collide(Player p) {
            if(p == null) {
                super.collide(p);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(getClass() == Menu.class) { // effectively static
            Font f = new Font("Amoebic", Font.BOLD, 130);
            final Button
            back = new Button(100, 250, this, p -> {
                getState().clear();
                getState().push(this);
            }, "<", f),
            next = new Button(300, 250, this, p -> {
                getState().peek().end();
            }, ">", f),
            help = new Button(1620, 250, this, p -> {
                if(getState().peek().getClass() != Help.class) {
                    getState().peek().setMaxTick(15);
                    Help h = new Help(this);
                    getState().push(new Transition(this, h, this));
                    getState().push(h);
                    getState().push(new Transition(this, this, h));
                }
                else {
                    getState().peek().end();
                }
            }, "?", f),
            exit = new Button(1820, 250, this, p -> {
                System.exit(0);
            }, "X", f);

            getEntities().clear();

            getEntities().add(back);
            getEntities().add(next);
            getEntities().add(help);
            getEntities().add(exit);

            getEntities().add(mute);

            f = new Font("Amoebic", Font.BOLD, 70);
            getEntities().add(new Button(1300, 1060, this, p -> resetTheme(space), "Space", f));
            getEntities().add(new Button(1565, 1060, this, p -> resetTheme(water), "Water", f));
            getEntities().add(new Button(1800, 1060, this, p -> resetTheme(dvd), "DVD", f));

            setClipped(false);
            final GameLevel
            input = new GameInput(this),
            avatar = new GameAvatar(this),
            mode = new GameMode(this),
            over = new GameOver(this);
            setMaxTick(40);
            getState().push(over);
            getState().push(mode);
            getState().push(Transition.Slide.horizontal(this).apply(avatar, mode));
            getState().push(avatar);
            getState().push(Transition.Slide.horizontal(this).apply(input, avatar));
            getState().push(input);
            getState().peek().setClipped(true);
        }

        if(getEntities().stream().noneMatch(Fireworks.class::isInstance)) {
            getEntities().add(new Fireworks(
                getRandom().nextInt(getDimension().width),
                getDimension().getHeight(),
                this,
                fireworkColor.get()
            ));
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        getEntities().forEach(b -> b.render(g));
        g.setColor(Color.white);

        // Main Title
        GameWindow.drawStringCentered(g, new Font("Amoebic", Font.BOLD, 130), "Wavegame", 960, 275);

        Font f = new Font("Amoebic", Font.BOLD, 34);
        GameWindow.drawStringCentered(g, f, "Credits: Team", 120, 1070);
        int shake = getRandom().nextInt(3);
        int blake = getRandom().nextInt(2);
        GameWindow.drawStringCentered(g, f,"Shakey", 300 + shake, 1070 + blake);
        GameWindow.drawStringCentered(g, f,"Blakey", 420, 1070);
    }

    @Override
    public void render(Clip c, int i) {
        super.render(c,i);
        ((FloatControl)c.getControl(FloatControl.Type.MASTER_GAIN)).setValue(mute.getVolume());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        for (int i = getEntities().size() - 1; i >= 0; i -= 1) {
            if (getEntities().get(i).getBounds().contains(e.getPoint())) {
                getEntities().get(i).collide(null);
            }
        }
    }
}
