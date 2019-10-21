package game.menu;

import game.*;
import game.waves.Waves;
import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Stack;

public class Menu extends GameLevel {
    private Theme space, water;
    private Random.RandomDifferentElement<Color> fireworkColor;

    public Menu(Stack<GameState> state, Random random, Dimension dimension, Theme common) {
        super(state, 0, 0, common, random, dimension, new ArrayList<>());

        space = new Theme("space", common);
        water = new Theme("water", common);
        space.initialize();
        water.initialize();
        setTheme(space);

        fireworkColor = random.new RandomDifferentElement<>(
            Color.red,
            Color.orange,
            Color.yellow,
            Color.green,
            Color.blue
            // Color.indigo
            // Color.violet
        );
    }

    @Override
    public void tick() {
        super.tick();
        if(size() == 0) {
            add( new Fireworks(
                new Point2D.Double(getRandom().nextInt(getDimension().width), getDimension().getHeight()),
                this,
                fireworkColor.get()
            ));
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(Color.white);

        g.setFont(new Font("Amoebic", 1, 130));
        // Waves button, the start game button
        g.drawRect(602, 300, 281, 250); //changes the rectangle size drawn
        g.drawString("One", 602, 465);//move the text down and center it inside the rectangle
        // Waves button two, the start game button for two players
        g.drawRect(1052, 300, 281, 250); //changes the rectangle size drawn
        g.drawString("Two", 1052, 465);//move the text down and center it inside the rectangle

        g.setFont(new Font("Amoebic", 1, 100));
        // Main Title
        g.drawString("Loehle's Sandbox", 500, 100);
        // Help button
        g.drawRect(230, 360, 260, 200);
        g.drawString("Help", 250, 500);
        // The Quit button
        g.drawRect(1390, 360, 260, 200);
        g.drawString("Quit", 1400, 500);
        // Theme buttons
        g.drawString("Themes:", 330,710);
        g.drawRect(400, 730, 350, 120);
        g.drawString("Space", 430, 815);
        g.drawRect(850, 730, 650, 120);
        g.drawString("Underwater", 870, 825);

        g.setFont(new Font("Amoebic", 1, 34));
        // Credits to team that worked on game last editor
        g.drawString("Credits: Team", 0, 1000);
        int shake = getRandom().nextInt(3);
        int blake = getRandom().nextInt(2);
        g.translate(shake, blake);
        g.drawString("Shakey", 233, 1000);
        g.translate(-shake,-blake);
        g.drawString("Blakey", 360, 1000);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Waves One Button
        if (mouseOver(e.getX(), e.getY(), 602, 300, 281, 250)) {
            clear();
            getPlayers().add( new Player(
                new Point2D.Double(getDimension().getWidth()/2, getDimension().getHeight()/2), this
            ));
            getState().push(new Waves(this));
        }
        // Waves Two Button
        if (mouseOver(e.getX(), e.getY(), 1052, 300, 281, 250)) {
            clear();
            getPlayers().add( new Player(
                new Point.Double(getDimension().getWidth()/3, getDimension().getHeight()/2), this
            ));
            getPlayers().add( new Player(
                new Point.Double(getDimension().getWidth()*2/3, getDimension().getHeight()/2), this
            ));
            getState().push(new Waves(this));
        }
        // Help Button
        else if (mouseOver(e.getX(), e.getY(), 230, 360, 260, 200)) {
            getState().push(new Help(this));
        }
        // Quit Button
        else if (mouseOver(e.getX(), e.getY(), 1390, 360, 260, 200)) {
            System.exit(1);
        }
        // Space Theme Button
        else if (mouseOver(e.getX(), e.getY(), 400, 730, 350, 120)) {
            setTheme(space);
        }
        // Underwater Theme Button
        else if (mouseOver(e.getX(), e.getY(), 850, 730, 650, 120)) {
            setTheme(water);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
