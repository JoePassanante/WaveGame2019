package game;

import game.pickup.*;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
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
	private ArrayList<Pickup> inactive; // TODO: replace all array lists and stacks with concurrent friendly stacks
	private ArrayList<Pickup> active;

	public void setMaxHealth(double m) {
	    maxHealth = m;
    }
    public void setArmor(double d) {//set players damage
        armor = d;
    }
    public void damage(double d) {
        playerColor = Color.red;
        setHealth(getHealth() - d/(armor + 1));
    }
    public void setSize(double size) {
        setWidth(size);
        setHeight(size);
    }
    public double getMaxHealth() {
        return maxHealth;
    }
    public double getArmor() {
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
	    if(this == p) {
            getLevel().getNonentities().add(new Trail(this, playerColor, 255));
            playerColor = Color.white;
        }
        else { // TODO: fun collision ripple animation and trade health
            if(getHealth() > p.getHealth()) {
                double heal = Math.min(.5, .5*(getHealth()-p.getHealth()));
                damage(heal);
                p.setHealth(p.getHealth() + heal);
            }
            else if(getHealth() < p.getHealth()) {
                playerColor = Color.green;
            }
            if(getLevel().getScore() % 10 == 0) {
                Path2D.Double ripple = new Path2D.Double();
                ripple.append(getBounds().getPathIterator(null), false);
                ripple.append(p.getBounds().getPathIterator(null), false);
                getLevel().getNonentities().add(new Trail.Outline(ripple, playerColor, 255*getHealth(), getLevel()));
            }
        }
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
	}

	@Override
	public void render(Graphics g) {
		//g.drawImage(img, (int) this.x, (int) this.y, playerWidth, playerHeight, null);
        super.render(g, playerColor);
        inactive.forEach(ge -> ge.render(g));
	}

	@Override
    public void render(Clip c, int i) {
        super.render(c, i);
        inactive.forEach(ge -> ge.render(c,i));
    }
}
