package game;

import game.pickup.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Player object class with collision
 * 
 * @author Brandon Loehle 5/30/16
 * @author William Joseph 12/9/18
 * @author Aaron Paterson 10/1/19
 */

public class Player extends GameEntity.Stopping {
	private double maxHealth;
	private double armor; // value between zero and one that represents damage resistance
	private ArrayList<Pickup> inactive; // TODO: replace all array lists and stacks with concurrent push/pop deques
	private ArrayList<Pickup> active;

	public void setMaxHealth(double m) {
	    maxHealth = m;
    }
    public void setArmor(double d) {//set players damage
        armor  = d;
    }
    public void damage(double d) {
        playerColor = Color.RED;
        setHealth(getHealth() - d*(1 - armor));
    }
    public void setSize(double size) {
        setWidth(size);
        setHeight(size);
    }
    public double getMaxHealth() {
        return maxHealth;
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

    private Color playerColor;

	public Player(double x, double y, GameLevel level) {
		super(new Point.Double(x, y), 32, 32, level);
		setMaxHealth(100);
		setHealth(maxHealth);
		armor = 0;
        playerColor = Color.white;
        inactive = new ArrayList<>();
        active = new ArrayList<>();
	}

    @Override
    public void collide(Player p) {
        getLevel().getEntities().add(new Trail(this, playerColor, 255));
        playerColor = Color.white;
        // TODO: fun collision ripple animation and trade health
    }

    private double theta = 0;
    @Override
	public void tick() { // Heartbeat of the Player class
	    super.tick();
        for(int i = inactive.size()-1; i >= 0; i -= 1) {
            theta += .01 + 2*Math.PI/inactive.size();
            inactive.get(i).setPosX(getPosX() + 50*Math.cos(theta));
            inactive.get(i).setPosY(getPosY() + 50*Math.sin(theta));
        }
	    for(int i = active.size()-1; i >= 0; i -= 1) {
	        active.get(i).affect(this);
        }
	    for(int i = getLevel().getEntities().size()-1; i >= 0; i -= 1) {
	        if(getBounds().intersects(getLevel().getEntities().get(i).getBounds())) {
                getLevel().getEntities().get(i).collide(this);
            }
        }
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
