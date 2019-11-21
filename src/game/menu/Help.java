package game.menu;

import game.GameLevel;
import game.pickup.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Help extends GameLevel.Unending {
    public Help(GameLevel g) {
        super(g);
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // Display the background

        Image
            powerCoin = null,
            powerSlow = null,
            powerHealth = null,
            powerLife = null,
            powerSpeed = null,
            shield1 = null;

        try {
            powerCoin = ImageIO.read(getTheme().get(PickupScore.class).getSight());
            powerSlow = ImageIO.read(getTheme().get(PickupFreeze.class).getSight());
            powerHealth = ImageIO.read(getTheme().get(PickupHealth.class).getSight());
            powerLife = ImageIO.read(getTheme().get(PickupLife.class).getSight());
            powerSpeed = ImageIO.read(getTheme().get(PickupSize.class).getSight());
            shield1 = ImageIO.read(getTheme().get("shield1").getSight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.setColor(Color.white);

        g.setFont(new Font("impact", Font.PLAIN, 50)); //set the font with its parameters above
        //Help text
        g.drawString("Help", 900, 70); //this is the help text, yayy
        //Instructions
        g.drawString("  How to play:", 35, 160);
        g.drawString("  Player controls: WASD/Arrow Keys", 35, 240);
        g.drawString("  Avoid enemies as difficulty increases", 35, 300);
        g.drawString("  Boss levels occur every five levels", 35, 360);
        g.drawString("  Players can pick up various powerups", 35, 420);
        g.drawString("  Players obtain a new powerup after every boss", 35, 480);
        g.drawString("  Power-Ups:", 1100, 160);
        g.drawString("		-  Screen freeze powerup", 1125, 240);
        g.drawImage(powerSlow, 1655, 205, 40, 40, null);
        g.drawString("		-  Make players smaller", 1125, 300);
        g.drawImage(powerSpeed, 1635, 260, 40, 40, null);
        g.drawString("		-  More points", 1125, 360);
        g.drawImage(powerCoin, 1430, 325, 40, 40, null);
        g.drawString("		-  Health refill", 1125, 420);
        g.drawImage(powerHealth, 1430, 380, 40, 40, null);
        g.drawString("		-  Extra Life", 1125, 480);
        g.drawImage(powerLife, 1370, 440, 40, 40, null);
        g.drawString("  Helpful Tips:", 35, 700);
        g.drawImage(shield1, 55, 740, 40, 40, null);
        g.drawString("  : Represents amount of Damage Resistance you have", 80, 780);
        g.drawString("  Press E or Enter to activate abilities", 35, 860);
        g.drawString("  Git Gud", 35, 940);

        g.setFont(new Font("impact", Font.PLAIN, 30));
        //Back button
        g.drawRect(910, 300, 150, 64); //ugly rectangle box
        g.drawString("Back", 955, 340); //make it the back button
    }

    @Override
    public void render(Clip c, int i) {
        // let the menu music continue playing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (new Rectangle(850, 300, 200, 64).contains(e.getPoint())) {
            end();
        }
    }
}
