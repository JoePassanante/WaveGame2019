package game;

import game.pickup.Pickup;

import javax.sound.sampled.Clip;
import java.awt.*;
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
	private double maxHealth; // health capacity
	private double armor; // higher value represents higher damage resistance
    private double speed; // movement speed
	private ArrayList<Pickup> inactive, active; // circling and stacking pickups
	private Controller controller; // controller to listen to

    public void setMaxHealth(double m) {
	    maxHealth = m;
    }
    public void setArmor(double d) {//set players damage
        armor = d;
    }
    public void damage(double d) {
        temporary = Color.red;
        setHealth(getHealth() - d/(1 + armor));
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
    public Controller getController() {
	    return controller;
    }

    private Color temporary, permanent; // current player color and normal player color

    public void setColor(Color c) {
        permanent = c;
    }

	public Player(double x, double y, Controller c, GameLevel level) {
		super(new Point.Double(x, y), 32, 32, level);
		setMaxHealth(100);
		setHealth(maxHealth);
		armor = 0;
        permanent = temporary = Color.white;
        inactive = new ArrayList<>();
        active = new ArrayList<>();
        controller = c;
        speed = 10;
	}

    @Override
    public void collide(Player p) { // trade health with p
	    if(p == this) { // reflexive case
            getLevel().getNonentities().add(new Trail(this, temporary, 255));
            temporary = permanent;
        }
        else if(p != null) {
            if(getHealth() > p.getHealth()) { // give health to p
                double heal = Math.min(.5, .5*(getHealth()-p.getHealth()));
                damage(heal);
                p.setHealth(p.getHealth() + heal);
            }
            else if(getHealth() < p.getHealth()) { // taking health from p
                temporary = Color.green;
            }
            if(getLevel().getScore() % 10 == 0) { // make a ripple every ten ticks
                Path2D.Double ripple = new Path2D.Double();
                ripple.append(getBounds().getPathIterator(null), false);
//              ripple.append(p.getBounds().getPathIterator(null), false);
                getLevel().getNonentities().add(new Trail.Outline(ripple, temporary, 255*getHealth(), getLevel()));
            }
        }
    }

    private double theta = 0; // inactive pickups slowly circle player

    @Override
	public void tick() {
	    super.tick();
	    double // move in the direction the controller is pointing
            x = controller.getX(getPosX()),
            y = controller.getY(getPosY()),
            h = Math.max(1, Math.hypot(x, y));
        setVelX(speed*x/h);
        setVelY(speed*y/h);
        for(int i = inactive.size()-1; i >= 0; i -= 1) { // make a circle of inactive pickups
            theta += .01 + 2*Math.PI/inactive.size();
            inactive.get(i).setPosX(getPosX() + 50*Math.cos(theta));
            inactive.get(i).setPosY(getPosY() + 50*Math.sin(theta));
        }
	    for(int i = active.size()-1; i >= 0; i -= 1) { // make a stack of active pickups
	        active.get(i).tick();
            active.get(i).setPosX(.8*active.get(i).getPosX() + .2*getPosX());
            active.get(i).setPosY(.8*active.get(i).getPosY() + .2*(
                getPosY() - 1.5*(getBounds().getHeight() + i*active.get(i).getBounds().getHeight())
            ));
            active.get(i).affect(this);
        }
	    if(controller.getUse() && !inactive.isEmpty()) {
 	        active.add(0, inactive.remove(0));
        }
	}

	@Override
	public void render(Graphics g) { // mirror any pickups that have textures
        super.render(g, temporary);
        inactive.forEach(ge -> ge.render(g));
        active.forEach(ge -> ge.render(g));
	}

	@Override
    public void render(Clip c, int i) { // echo any pickups that make noise
        super.render(c, i);
        inactive.forEach(ge -> ge.render(c,i));
        active.forEach(ge -> ge.render(c,i));
    }
}
