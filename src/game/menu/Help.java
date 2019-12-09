package game.menu;

import game.pickup.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Help extends Menu {
    private Image armor, clear, freeze, health, life, regen, score, size, skip, speed;

    public Help(Menu m) {
        super(m);
        try {
            armor = ImageIO.read(getTheme().get(PickupArmor.class).getSight());
            clear = ImageIO.read(getTheme().get(PickupClear.class).getSight());
            freeze = ImageIO.read(getTheme().get(PickupFreeze.class).getSight());
            health = ImageIO.read(getTheme().get(PickupHealth.class).getSight());
            life = ImageIO.read(getTheme().get(PickupLife.class).getSight());
            regen = ImageIO.read(getTheme().get(PickupRegen.class).getSight());
            score = ImageIO.read(getTheme().get(PickupScore.class).getSight());
            size = ImageIO.read(getTheme().get(PickupSize.class).getSight());
            skip = ImageIO.read(getTheme().get(PickupSkip.class).getSight());
            speed = ImageIO.read(getTheme().get(PickupSpeed.class).getSight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // display the background
        g.setColor(new Color(0x11,0x11,0x11,0x99));
        g.fillRect(0, 0, getBounds().width, getBounds().height);
        g.setColor(Color.white);
        g.setFont(new Font("impact", Font.PLAIN, 50)); // set the font with its parameters above
        // help text
        g.drawString("Help", 900, 70); //this is the help text, yayy
        // instructions
        g.drawString("How to play:", 35, 310);
        g.drawString("  Control players with keyboard or mouse", 35, 390);
        g.drawString("  Avoid enemies as difficulty increases", 35, 450);
        g.drawString("  Boss fights once every five levels", 35, 510);
        g.drawString("  Players can pick up various powerups", 35, 570);
        g.drawString("  Players can find more powerups after every boss", 35, 630);
        g.drawString("  Press P to pause", 35, 690);
        g.drawString("Power-Ups:", 1100, 310);
        g.drawImage(armor, 1150, 330, 40, 40, null);
        g.drawImage(clear, 1150, 390, 40, 40, null);
        g.drawImage(freeze, 1150, 450, 40, 40, null);
        g.drawImage(health, 1150, 510, 40, 40, null);
        g.drawImage(life, 1150, 570, 40, 40, null);
        g.drawImage(regen, 1150, 630, 40, 40, null);
        g.drawImage(score, 1150, 690, 40, 40, null);
        g.drawImage(size, 1150, 750, 40, 40, null);
        g.drawImage(skip, 1150, 810, 40, 40, null);
        g.drawImage(speed, 1150, 870, 40, 40, null);
        g.drawString("  :  Reduce damage", 1200, 370);
        g.drawString("  :  Clear enemies", 1200, 430);
        g.drawString("  :  Freeze enemies", 1200, 490);
        g.drawString("  :  Fill health", 1200, 550);
        g.drawString("  :  Extra life", 1200, 610);
        g.drawString("  :  Regenerate health", 1200, 670);
        g.drawString("  :  Bonus points", 1200, 730);
        g.drawString("  :  Reduce size", 1200, 790);
        g.drawString("  :  Skip level", 1200, 850);
        g.drawString("  :  Increase speed", 1200, 910);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        end();
    } // exit when mouse is pressed
}
