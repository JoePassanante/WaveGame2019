package game.waves;

import game.GameState;
import game.MenuFireworks;
import game.Theme;
import game.GameState;
import game.MenuFireworks;
import game.Theme;
import game.pickup.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 * The main menu
 * 
 * @author Brandon Loehle 5/30/16
 * @author Aaron Paterson 9/11/19
 *
 */

public class Menu extends GameState {
	private int timer;
	private Random r;
	private ArrayList<Color> colorPick = new ArrayList<>();
	private int colorIndex;
	private boolean help;
	public void setHelp(boolean h) {
	    help = h;
    }
    public boolean getHelp() {
	    return help;
    }
    private Waves game;
	private Theme space, water;

	public Menu(Waves waves) {
	    game = waves;
		timer = 10;
		r = new Random();
		addColors();

		Theme fallback = new Theme("common", null);
		fallback.initialize();

        space = new Theme("space", fallback);
        water = new Theme("water", fallback);

        space.initialize();
        water.initialize();

        game.getHandler().setTheme(space);
	}

	//using the java color picker, which colors you will add to the scene
	public void addColors() {
		colorPick.add(Color.blue);
		colorPick.add(Color.white);
		colorPick.add(Color.green);
		colorPick.add(Color.red);
		colorPick.add(Color.cyan);
		colorPick.add(Color.magenta);
		colorPick.add(Color.yellow);
	}

	//tick method, allows game to check time and spawn fireworks
	public void tick() {
		timer--;
		if (timer <= 0) {
			colorIndex = r.nextInt(6);
            game.getHandler().add(new MenuFireworks((r.nextInt((int)game.getHandler().getGameDimension().getWidth()) - 25), 1080, 100, 100, 0, -4,
					colorPick.get(colorIndex), this.game.getHandler(), true));
			timer = 300;
		}
        game.getHandler().tick();
	}

	public void render(Graphics g) {
		if (!help) {
			//display the background
			g.drawImage(game.getHandler().getTheme().get(this), 0, 0, (int)game.getHandler().getGameDimension().getWidth(), (int)game.getHandler().getGameDimension().getHeight(), null);
			//create the font objects
			Font font = new Font("Amoebic", 1, 100); //the title
			Font font2 = new Font("Amoebic", 1, 34); //help and quit
			Font font3 = new Font("Amoebic", 1, 130); //use for Waves
			//Main Title
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("Loehle's Sandbox", 500, 100);
			//Waves button, the start game button
			g.setColor(Color.white);
			g.drawRect(602, 300, 281, 250); //changes the rectangle size drawn
			g.setFont(font3);
			g.setColor(Color.white);
			g.drawString("One", 602, 465);//move the text down and center it inside the rectangle
			//Waves button two, the start game button for two players
			g.setColor(Color.white);
			g.drawRect(1052, 300, 281, 250); //changes the rectangle size drawn
			g.setFont(font3);
			g.setColor(Color.white);
			g.drawString("Two", 1052, 465);//move the text down and center it inside the rectangle
			//Help button
			g.setColor(Color.white);
			g.drawRect(230, 360, 260, 200);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("Help", 250, 500);
			//The Quit button 
			g.setColor(Color.white);
			g.drawRect(1390, 360, 260, 200);
			g.setFont(font);
			g.setColor(Color.white);
			g.drawString("Quit", 1400, 500);

            //Theme buttons
            g.setColor(Color.white);
            g.setFont(font);

            g.drawString("Themes:", 330,710);

            g.drawRect(400, 730, 350, 120);
            g.drawString("Space", 430, 815);

            g.drawRect(850, 730, 650, 120);
            g.drawString("Underwater", 870, 825);

			//Credits to team that worked on game last editor
			g.setColor(Color.white);
			g.setFont(font2);
			g.drawString("Credits: Team", 0, 1000);
			int shake = (int)(Math.random()*3);
			int blake = (int)(Math.random()*2);
			g.translate(shake, blake);
			g.drawString("Shakey", 233, 1000);
			g.translate(-shake,-blake);
			g.drawString(" Blakey", 350, 1000);
			//Now if the user clicked the Help button
		} else {// if the user clicks on "help"
			Font font = new Font("impact", Font.PLAIN, 50);
			Font font2 = new Font("impact", Font.PLAIN, 30);
			Font font3 = new Font("impact", Font.PLAIN, 30);
			//gets images, allows them to be used on help menu
			Image PowerCoin = game.getHandler().getTheme().get(PickupScore.class);
			Image PowerSlow = game.getHandler().getTheme().get(PickupFreeze.class);
			Image PowerHealth = game.getHandler().getTheme().get(PickupHealth.class);
			Image PowerLife = game.getHandler().getTheme().get(PickupLife.class);
			Image PowerSpeed = game.getHandler().getTheme().get(PickupSize.class);
			Image HUDshield1 = game.getHandler().getTheme().get("shield1");
			//Help text
			g.setFont(font); //set the font with its parameters above 
			g.setColor(Color.white);
			g.drawString("Help", 900, 70); //this is the help text, yayy
			//Instructions 
			g.setFont(font);
			g.drawString("  How to play:", 35, 160);
			g.drawString("  Player controls: WASD/Arrow Keys", 35, 240);
			g.drawString("  Avoid enemies as difficulty increases", 35, 300);
			g.drawString("  Boss levels occur every five levels", 35, 360);
			g.drawString("  Players can pick up various powerups", 35, 420);
			g.drawString("  Players obtain a new powerup after every boss", 35, 480);
			g.drawString("  Power-Ups:", 1100, 160);
			g.drawString("		-  Screen freeze powerup", 1125, 240);
			g.drawImage(PowerSlow, 1655, 205, 40, 40, null);
			g.drawString("		-  Make players smaller", 1125, 300);
			g.drawImage(PowerSpeed, 1635, 260, 40, 40, null);
			g.drawString("		-  More points", 1125, 360);
			g.drawImage(PowerCoin, 1430, 325, 40, 40, null);
			g.drawString("		-  Health refill", 1125, 420);
			g.drawImage(PowerHealth, 1430, 380, 40, 40, null);
			g.drawString("		-  Extra Life", 1125, 480);
			g.drawImage(PowerLife, 1370, 440, 40, 40, null);
			g.drawString("  Helpful Tips:", 35, 700);
			g.drawImage(HUDshield1, 55, 740, 40, 40, null);
			g.drawString("  : Represents amount of Damage Resistance you have", 80, 780);
			g.drawString("  Press E or Enter to activate abilities", 35, 860);
			g.drawString("  Git Gud", 35, 940);
			//Back button
			g.setFont(font3);
			g.setColor(Color.white);
			g.drawRect(910, 300, 150, 64); //ugly rectangle box
			g.drawString("Back", 955, 340); //make it the back button
		}

		game.getHandler().render(g);
	}

    public void mousePressed(MouseEvent e) {
        if (!getHelp()) {
            // Waves One Button
            if (mouseOver(e.getX(), e.getY(), 602, 300, 281, 250)) {
                game.getHandler().clear();
                game.setState(game.getCurrentLevel());
                game.getHandler().add(game.getPlayer());
            }
         // Waves Two Button
            if (mouseOver(e.getX(), e.getY(), 1052, 300, 281, 250)) {
                game.getHandler().clear();
                game.setState(game.getCurrentLevel());
            }
            // Help Button
            else if (mouseOver(e.getX(), e.getY(), 230, 360, 260, 200)) {
                setHelp(true);
            }
            // Quit Button
            else if (mouseOver(e.getX(), e.getY(), 1390, 360, 260, 200)) {
                System.exit(1);
            }
            // Space Theme Button
            else if (mouseOver(e.getX(), e.getY(), 400, 730, 350, 120)) {
                game.setMenuMusic(false);
                game.getHandler().setTheme(space);
            }
            // Underwater Theme Button
            else if (mouseOver(e.getX(), e.getY(), 850, 730, 650, 120)) {
                game.setMenuMusic(true);
                game.getHandler().setTheme(water);
            }
        }
        // Back Button for Help screen
        else if (mouseOver(e.getX(), e.getY(), 850, 300, 200, 64)) {
            setHelp(false);
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


