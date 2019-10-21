package game;

import game.pickup.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Player object class with collision
 * 
 * @author Brandon Loehle 5/30/16
 * @author William Joseph 12/9/18
 * @author Aaron Paterson 10/1/19
 */

public class Player extends GameObject.Stopping {
	private double maxHealth;
	private double armor; // value between zero and one that represents damage resistance
	private ArrayList<Pickup> inactive; // TODO: replace all arraylists and stacks with safe concurrent push/pop stack
	private ArrayList<Pickup> active;
	public void setMaxHealth(double m) {
	    maxHealth = m;
    }
    public double getMaxHealth() {
	    return maxHealth;
    }
    public void setArmor(double d) {//set players damage
        armor  = d;
    }
    public double getArmor() {//get damage done
        return armor;
    }
    public ArrayList<Pickup> getInactive() {
	    return inactive;
    }
    public ArrayList<Pickup> getActive() {
	    return active;
    }
    public void damage(double d) {
        playerColor = Color.RED;
	    setHealth(getHealth() - d*(1 - armor));
    }
    public void setSize(double size) {
        setWidth(size);
        setHeight(size);
    }

    private Color playerColor;

	public Player(Point2D.Double p, GameLevel level) {
		super(p, 32, 32, level);
		setMaxHealth(100);
		setHealth(maxHealth);
		armor = 0;
        playerColor = Color.white;
        inactive = new ArrayList<>();
        active = new ArrayList<>();
	}

    @Override
    public void collide(Player p) {
        // TODO: fun collision ripple animation
    }

    private double theta = 0;
    @Override
	public void tick() { // Heartbeat of the Player class
	    super.tick();
	    playerColor = Color.white;
        for(int i = inactive.size()-1; i >= 0; i -= 1) {
            theta += .01 + 2*Math.PI/inactive.size();
            inactive.get(i).setX(getX() + 50*Math.cos(theta));
            inactive.get(i).setY(getY() + 50*Math.sin(theta));
        }
	    for(int i = active.size()-1; i >= 0; i -= 1) {
	        active.get(i).affect(this);
        }
	    for(int i = getLevel().size()-1; i >= 0; i -= 1) {
	        if(this != getLevel().get(i) && getBounds().intersects(getLevel().get(i).getBounds())) {
                getLevel().get(i).collide(this);
            }
        }
        getLevel().add(new Trail(new Point2D.Double(getX(), getY()), playerColor, (int)getWidth(), (int)getHeight(), 255, getLevel()));
	}

	@Override
	public void render(Graphics g) {//renders player
		g.setColor(playerColor);
		Rectangle bounds = getBounds();
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		//g.drawImage(img, (int) this.x, (int) this.y, playerWidth, playerHeight, null);
        inactive.forEach(go -> go.render(g));
	}
}
