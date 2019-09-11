package mainGame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * Handles all mouse input
 * 
 * @author Brandon Loehle 5/30/16
 *
 * @author Katie Rosell - DESCRIPTION
 *
 * @author Aaron Paterson 9/11/19
 * 
 * The mouse listener class is responsible for handling where the 
 * player clicks the screen. 
 *
 */

public class MouseListener extends MouseAdapter {
	//Initialization, what is needed in here
	private Game game;
	private AffineTransform space;
	public void setSpace(AffineTransform s) {
	    space = s;
    }
	//Constructors for the class (blue prints for what composes the class)
	public MouseListener(Game game) {
		this.game = game;
	}
	/** MOUSE PRESSED
	 * @param e A mouse click!
	 * @return Nothing returned
	 * This function uses the click from a mouse and 
	 */
	public void mousePressed(MouseEvent e) {
	    try {
            Point2D p = new Point();
            space.inverseTransform(e.getPoint(), p);
            game.getGameState().mousePressed(new MouseEvent(
                    e.getComponent(),
                    e.getID(),
                    e.getWhen(),
                    e.getModifiers(),
                    (int)p.getX(),
                    (int)p.getY(),
                    e.getClickCount(),
                    e.isPopupTrigger()
            ));
        } catch(NoninvertibleTransformException nite) {
            nite.printStackTrace();
        }
	}
}
