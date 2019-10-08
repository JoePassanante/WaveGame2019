package game.enemy;

import game.*;
import game.waves.HUD;
import game.waves.Handler;
import game.Player;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class EnemyRocketBossMissile extends GameObject.Disappearing {
	private double direction;
	private double speed;
	private HUD hud;
	private Player player;
	private double trackSpeed;

    public EnemyRocketBossMissile(double x, double y, Handler handler, double dir, double spd, HUD _hud, Player play, double track) {
		super(x, y, 32, -64, handler);
		AudioUtil.playClip("/sound/MissileSound.wav", false);
		speed = spd;
		direction = dir;
		hud = _hud;
		player = play;
		trackSpeed = track;
	}

	public void tick() {
		setX(getX() + Math.cos(Math.toRadians(direction))*speed);
		setY(getY() + Math.sin(Math.toRadians(direction))*speed);

		double angle = EnemyRocketBoss.GetAngleOfLineBetweenTwoPoints(new Point.Double(Math.cos(Math.toRadians(direction-90))*5 + getX(), Math.sin(Math.toRadians(direction-90))*5 + getY()), new Point.Double(player.getX(),player.getY()));
		
		this.direction = this.direction-Math.max(-trackSpeed,Math.min(EnemyRocketBoss.angleDifference(this.direction,angle),trackSpeed));

		//handler.addObject(new Trail(x, y, ID.Trail, Color.cyan, 16, 16, 0.025, this.handler));
	}

	public void render(Graphics g) {
		Graphics2D a = (Graphics2D) g;
		AffineTransform old = a.getTransform();
		
		a.translate(Math.cos(Math.toRadians(direction-90))*20 + getX(), Math.sin(Math.toRadians(direction-90))*20 + getY());
		a.rotate(Math.toRadians(direction - 90));
        a.drawImage(getHandler().getTheme().get(getClass()),0,64,32,-64, null);
        Rectangle2D rec = new Rectangle.Double(30, 0, 20,60);
		AffineTransform trans = new AffineTransform();
		trans.translate(Math.cos(Math.toRadians(this.direction-90))*-5 + getX(), Math.sin(Math.toRadians(this.direction-90))*-5 + getY());
		trans.rotate(Math.toRadians(this.direction - 90));
		
		Path2D bounds = new Path2D.Double(rec,trans);
		
	    a.setTransform(old);
	    
	    Rectangle2D playerBounds = new Rectangle2D.Double(player.getX(),player.getY(),player.getWidth(),player.getHeight());
		
		if(bounds.intersects(playerBounds)){
	    	hud.health = hud.health - 1;
	    }
		
		a.setTransform(old);
		//a.fill(bounds);
	}
}
