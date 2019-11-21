package game.menu;

import game.*;
import game.Transition;
import game.waves.RainbowText;
import game.waves.Waves;
import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Menu extends GameLevel.Unending {
    private Theme space, water, dvd;
    private Controller wasd, ijkl, arrows, mouse;
    private Random.RandomDifferentElement<Color> fireworkColor;
    private ArrayList<MenuButton> buttons;

    public Menu(GameLevel g) {
        super(g);

        space = new Theme("space", getTheme());
        water = new Theme("water", getTheme());
        dvd = new Theme("dvd", space);

        System.out.println("Loading themes...");
        Stream.of(space, water, dvd).parallel().forEach(Runnable::run); // still not fast enough >:L

        setTheme(space);

        wasd = new Controller.Keyboard(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.KEY_LOCATION_LEFT);
        ijkl = new Controller.Keyboard(KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.KEY_LOCATION_STANDARD);
        arrows = new Controller.Keyboard(KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.KEY_LOCATION_RIGHT);
        mouse = new Controller.Mouse();

        fireworkColor = getRandom().new RandomDifferentElement<>(RainbowText.rainbow);

        buttons = new ArrayList<>();
        buttons.add(new MenuButton(0, 0, 0, 0, this));
    }

    @Override
    public void tick() {
        super.tick();
        if (getEntities().isEmpty()) {
            getEntities().add(new Fireworks(
                getRandom().nextInt(getDimension().width),
                getDimension().getHeight(),
                this,
                fireworkColor.get()
            ));
        }
        for(int b = buttons.size()-1; b >= 0; b -= 1) {
            buttons.get(b).tick();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        buttons.forEach(b -> b.render(g));
        g.setColor(Color.white);

        g.setFont(new Font("Amoebic", 1, 130));
        // Waves button, the start game button
        g.drawRect(602, 300, 281, 250); //changes the rectangle size drawn
        g.drawString("One", 602, 465);//move the text down and center it inside the rectangle
        // Waves button two, the start game button for two players
        g.drawRect(1052, 300, 281, 250); //changes the rectangle size drawn
        g.drawString("Two", 1052, 465);//move the text down and center it inside the rectangle

        g.setFont(new Font("Amoebic", Font.BOLD, 100));
        // Main Title
        g.drawString("Loehle's Sandbox", 500, 100);
        // Help button
        g.drawRect(230, 360, 260, 200);
        g.drawString("Help", 250, 500);
        // The Quit button
        g.drawRect(1390, 360, 260, 200);
        g.drawString("Quit", 1400, 500);
        // Theme buttons
        g.drawString("Themes:", 330, 710);
        g.drawRect(400, 730, 350, 120);
        g.drawString("Space", 430, 815);
        g.drawRect(850, 730, 650, 120);
        g.drawString("Underwater", 870, 825);
        g.drawString("Coming soon...", 1111, 1025);

        //Button for avatar changing
        g.setFont(new Font("Amoebic", Font.BOLD, 55));
        g.drawRect(850, 900, 650, 100);
        g.drawString("Player Customization", 870, 975);

        g.setFont(new Font("Amoebic", Font.BOLD, 34));
        // Credits to team that worked on game last editor
        g.drawString("Credits: Team", 0, 1000);
        int shake = getRandom().nextInt(3);
        int blake = getRandom().nextInt(2);
        g.translate(shake, blake);
        g.drawString("Shakey", 233, 1000);
        g.translate(-shake, -blake);
        g.drawString("Blakey", 360, 1000);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Waves One Button
        if (new Rectangle(602, 300, 281, 250).contains(e.getPoint())) {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getPlayers().add(new Player(getDimension().getWidth() / 2, getDimension().getHeight() / 2, new Controller.Multi(wasd, arrows), this));
            getState().push(new Waves(this));
            setMaxTick(60);
            getState().push(Transition.Modulo.droplets(this).apply(this, getState().peek()));
        }
        // Waves Two Button
        else if (new Rectangle(1052, 300, 281, 250).contains(e.getPoint())) {
            getEntities().clear();
            setMaxTick(600);
            setClipped(true);
            getState().push(new GameOver(this));
            getPlayers().add(new Player(540, 640, wasd,this));
            getPlayers().add(new Player(640, 1280, arrows,this));
            getState().push(new Waves(this));
        }
        // Help Button
        else if (new Rectangle(230, 360, 260, 200).contains(e.getPoint())) {
            Help help = new Help(this);
            setMaxTick(15);
            getState().push(new Transition(this, help, this));
            getState().push(help);
            getState().push(new Transition(this, this, help));
        }
        // Quit Button
        else if (new Rectangle(1390, 360, 260, 200).contains(e.getPoint())) {
            System.exit(0);
        }
        // Space Theme Button
        else if (new Rectangle(400, 730, 350, 120).contains(e.getPoint())) {
            setTheme(space);
        }
        // Underwater Theme Button
        else if (new Rectangle(850, 730, 650, 120).contains(e.getPoint())) {
            setTheme(water);
        } else if (new Rectangle(1111, 1025, 1000, 120).contains(e.getPoint())) {
            setTheme(dvd);
        }
        else if (new Rectangle(850, 900, 650, 100).contains(e.getPoint())) {
            getState().push(new Avatar(this));
            getState().push(Transition.Slide.horizontal(this).apply(this, getState().peek()));
        }
        else { // TODO: all buttons should use this loop
            for (int i = getEntities().size() - 1; i >= 0; i -= 1) {
                if (getEntities().get(i).getBounds().contains(e.getPoint())) {
                    getEntities().get(i).collide(null);
                }
            }
        }
    }
}
