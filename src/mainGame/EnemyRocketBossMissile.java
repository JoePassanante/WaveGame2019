package mainGame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class EnemyRocketBossMissile extends GameObject {
	private double direction;
	private double speed;
	private HUD hud;
	private Player player;
	private double trackSpeed;

    public EnemyRocketBossMissile(double x, double y, Handler handler, double dir, double spd, HUD _hud, Player play, double track) {
		super(x, y, 32, -64, handler);
		AudioUtil.playClip("/gameSound/MissileSound.wav", false);
		speed = spd;
		direction = dir;
		hud = _hud;
		player = play;
		trackSpeed = track;
	}

	public void tick() {
		this.x += Math.cos(Math.toRadians(direction))*speed;
		this.y += Math.sin(Math.toRadians(direction))*speed;

		if (this.y <= 0 || this.y >= getHandler().getGameDimension().getHeight() - 40) {
            getHandler().removeObject(this);
		}
		if (this.x <= 0 || this.x >= getHandler().getGameDimension().getWidth() - 16) {
            getHandler().removeObject(this);
		}
		double angle = EnemyRocketBoss.GetAngleOfLineBetweenTwoPoints(new Point.Double(Math.cos(Math.toRadians(direction-90))*5 +this.x, Math.sin(Math.toRadians(direction-90))*5 +this.y), new Point.Double(player.x,player.y));
		
		this.direction = this.direction-Math.max(-trackSpeed,Math.min(EnemyRocketBoss.angleDifference(this.direction,angle),trackSpeed));

		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	public void render(Graphics g) {
		Graphics2D a = (Graphics2D) g;
		AffineTransform old = a.getTransform();
		
		a.translate(Math.cos(Math.toRadians(direction-90))*20 +this.x, Math.sin(Math.toRadians(direction-90))*20 +this.y);
		a.rotate(Math.toRadians(direction - 90));
        a.drawImage(getHandler().getTheme().get(getClass()),0,64,32,-64, null);
        Rectangle2D rec = new Rectangle.Double(30, 0, 20,60);
		AffineTransform trans = new AffineTransform();
		trans.translate(Math.cos(Math.toRadians(this.direction-90))*-5 +this.x, Math.sin(Math.toRadians(this.direction-90))*-5 +this.y);
		trans.rotate(Math.toRadians(this.direction - 90));
		
		Path2D bounds = new Path2D.Double(rec,trans);
		
	    a.setTransform(old);
	    
	    Rectangle2D playerBounds = new Rectangle2D.Double(player.x,player.y,player.getWidth(),player.getHeight());
		
		if(bounds.intersects(playerBounds)){
	    	hud.health = hud.health - 1;
	    }
		
		a.setTransform(old);
		//a.fill(bounds);
	}
}
