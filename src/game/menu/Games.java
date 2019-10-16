package game.menu;

import game.Client;
import game.GameState;
import game.Player;
import game.Theme;
import game.waves.Waves;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Games extends GameState {
    private Menu menu;
    private Theme space, water;
    private Client client;

    public Games(Menu m, Client c) {
        menu = m;
        client = c;
        Theme fallback = new Theme("common", null);
        fallback.initialize();

        space = new Theme("space", fallback);
        water = new Theme("water", fallback);

        space.initialize();
        water.initialize();

        menu.getHandler().setTheme(space);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        // Display the background
        g.drawImage(menu.getHandler().getTheme().get(this), 0, 0, menu.getHandler().getGameDimension().width, menu.getHandler().getGameDimension().height, null);

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
        int shake = menu.getHandler().getRandom().nextInt(3);
        int blake = menu.getHandler().getRandom().nextInt(2);
        g.translate(shake, blake);
        g.drawString("Shakey", 233, 1000);
        g.translate(-shake,-blake);
        g.drawString("Blakey", 360, 1000);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Waves One Button
        if (mouseOver(e.getX(), e.getY(), 602, 300, 281, 250)) {
            menu.getHandler().clear();
            client.getPlayers().clear();
            client.getPlayers().add(new Player(
                client.getHandler().getGameDimension().getWidth() / 3,
                client.getHandler().getGameDimension().getHeight() / 2,
                    (Waves)client.getWaves()
            ));
            client.setState(client.getWaves());
        }
        // Waves Two Button
        if (mouseOver(e.getX(), e.getY(), 1052, 300, 281, 250)) {
            menu.getHandler().clear();
            client.getPlayers().clear();
            client.getPlayers().add(new Player(
                    client.getHandler().getGameDimension().getWidth() / 3,
                    client.getHandler().getGameDimension().getHeight() / 2,
                    (Waves)client.getWaves()
            ));
            client.getPlayers().add(new Player(
                    client.getHandler().getGameDimension().getWidth() * 2 / 3,
                    client.getHandler().getGameDimension().getHeight() / 2,
                    (Waves)client.getWaves()
            ));
            client.setState(client.getWaves());
        }
        // Help Button
        else if (mouseOver(e.getX(), e.getY(), 230, 360, 260, 200)) {
            menu.setState(menu.getHelp());
        }
        // Quit Button
        else if (mouseOver(e.getX(), e.getY(), 1390, 360, 260, 200)) {
            System.exit(1);
        }
        // Space Theme Button
        else if (mouseOver(e.getX(), e.getY(), 400, 730, 350, 120)) {
            menu.setMenuMusic(false);
            menu.getHandler().setTheme(space);
        }
        // Underwater Theme Button
        else if (mouseOver(e.getX(), e.getY(), 850, 730, 650, 120)) {
            menu.setMenuMusic(true);
            menu.getHandler().setTheme(water);
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
