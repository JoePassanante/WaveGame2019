package game.menu;

import game.GameLevel;


import java.awt.*;
import java.awt.event.MouseEvent;

public class Avatar extends GameLevel.Unending {
    //inherit from the gamelevel class
    public Avatar(GameLevel gl) {
        super(gl);
    }

    @Override
    public void render(Graphics g) {
        super.render(g); //display background

        //The button to exit the menu
        g.setColor(Color.white);
        g.setFont(new Font("Amoebic", 1, 65));
        g.drawRect(850,900,200, 90);
        g.drawString("Back", 870, 970); //make it the back button

        g.setFont(new Font("Amoebic", 1, 80));
        g.drawString("Select your color!", 580, 200);

        //This draws all the different buttons with the colors you can choose from
        g.setColor(java.awt.Color.RED);
        g.fillRect(300,500,100,100);
        g.setColor(java.awt.Color.BLUE);
        g.fillRect(500,500,100,100);
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(700,500,100,100);
        g.setColor(java.awt.Color.CYAN);
        g.fillRect(900,500,100,100);
        g.setColor(java.awt.Color.YELLOW);
        g.fillRect(1100,500,100,100);
        g.setColor(Color.PINK);
        g.fillRect(1300,500,100,100);
        g.setColor(java.awt.Color.MAGENTA);
        g.fillRect(1500,500,100,100);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (new Rectangle(850, 900, 200, 90).contains(e.getPoint())) {
            getState().pop();
        }
    }
}



