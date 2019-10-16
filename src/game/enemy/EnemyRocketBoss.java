package game.enemy;

import game.*;
import game.Handler;
import game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class EnemyRocketBoss extends GameObject {
	private double dash_x,dash_y; //current target
	private boolean inDash = false; // in dash
	private int cooldown = 10; // dash cooldown
	private double drawAngle = 90;
	private int speed = 18;
	private boolean colliding = false;
	private int rocketTimer = 120;	private int health = 1000;
	private Path2D bounds;

    private int difficulty = 1;

    public EnemyRocketBoss(Point.Double p, Handler handler) {
		super(p.getX(), p.getY(), 80, 296, handler);
		difficulty = handler.getLevel()/10;
		setHealth(1000);
	}

	@Override
	public void tick() {
        super.tick();

        if(colliding) {
            setHealth(getHealth()-1);
        }

		if (difficulty > 1){
			rocketTimer--;
			if (rocketTimer < 0) {
				rocketTimer = 120;
                getHandler().add( new EnemyRocketBossMissile(
                    Math.cos(Math.toRadians(this.drawAngle+90))*40 + getX(),
                    Math.sin(Math.toRadians(this.drawAngle+90))*40 + getY(),
                    getHandler(),this.drawAngle,
                    10, getHandler().getRandomDifferentPlayer(), difficulty > 2 ? 0.5 : 0
                ) );
			}
		}
		if(this.health%150 == 0){
            getHandler().add( new EnemyBurst(
                new Point.Double(
                    getHandler().getGameDimension().getWidth() * getHandler().getRandom().random(),
                    getHandler().getGameDimension().getHeight() * getHandler().getRandom().random()
                ),
                getHandler()
            ));
		}
		health--;
//		handler.addObject(new Trail(this.x - 80, this.y-296, ID.Trail, Color.red, 20, 20, 0.05, this.handler));
	
		if(inDash){
			this.speed = 28 - this.health/100;
			move();
		}
		else{
			if (!colliding) {
			    Player player = getHandler().getRandomDifferentPlayer();
                this.dash_x = (player.getX()+player.getWidth()/2.0) + player.getVelX()*1.5;
                this.dash_y = (player.getY()+player.getHeight()/2.0) + player.getVelY()*1.5;
                double angle = EnemyRocketBoss.GetAngleOfLineBetweenTwoPoints(
                    new Point.Double(getX() + 40, getY()),
                    new Point.Double(dash_x, dash_y)
                );
                this.dash_x = (player.getX()+player.getWidth()/2.0)+Math.cos(Math.toRadians(angle))*100;
                this.dash_y = (player.getY()+player.getHeight()/2.0)+Math.sin(Math.toRadians(angle))*100;
                if(cooldown<=0){
                    AudioUtil.playClip("/sound/MissileSound.wav", false);
                    this.drawAngle = angle;
                    this.inDash = true;
                    cooldown = 60 - (int)(getHandler().getRandom().random()*25); //lazy way to make cooldown shorter
                }
                else{
                    cooldown--;
                    this.drawAngle = this.drawAngle-Math.max(-5,Math.min(angleDifference(this.drawAngle,angle),5));
                }
			}
		}

		if(health<=0){
			System.out.println("Removing Boss");
            getHandler().remove(this);
		}
	}
    public static double GetAngleOfLineBetweenTwoPoints(Point.Double p1, Point.Double p2)
    {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        return Math.toDegrees(Math.atan2(yDiff, xDiff));
    }

    public static double angleDifference(double angleFrom, double angleTo) {
        return ((((angleFrom - angleTo) % 360) + 540) % 360) - 180;
    }

	private void move(){
	// System.out.println("Moving");
		double diffX = dash_x - getX();
		double diffY = dash_y - getY();
		double distance = Math.hypot(diffX, diffY);

        setVelX(diffX * speed / distance);
        setVelY(diffY * speed / distance);

		if(distance < speed) {
			inDash = false;
			setVelX(0);
			setVelY(0);
		}
	}

	@Override
	public void render(Graphics g) {
	    //draw health bar
		g.setColor(Color.GRAY);
		g.fillRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, 1000, 50);
		g.setColor(Color.RED);
		g.fillRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, this.health, 50);
		g.setColor(Color.WHITE);
		g.drawRect((int)getHandler().getGameDimension().getWidth() / 2 - 500, (int)getHandler().getGameDimension().getHeight() - 150, 1000, 50);

		Graphics2D g2d = (Graphics2D)g;
		//DEV TOOLS
		/*
		g2d.drawLine((int)this.x, (int)this.y, (int)this.dash_x, (int)this.dash_y); //DEV TOOL
		Ellipse2D e = new Ellipse2D.Double(this.dash_x,this.dash_y,10,10);
		g2d.draw(e);
		 */
		
		//Draw Rocket
		AffineTransform old = g2d.getTransform();

		g2d.translate(Math.cos(Math.toRadians(this.drawAngle-90))*40 + getX(), Math.sin(Math.toRadians(this.drawAngle-90))*40 + getY());
		g2d.rotate(Math.toRadians(this.drawAngle + 90));

        g2d.drawImage(getHandler().getTheme().get(inDash ? getClass().getSimpleName() + "On" : getClass().getSimpleName()), 0, 0, 80, 296, null);

        Rectangle2D rec = new Rectangle.Double(30, 0, 20,inDash ?  230 : 180);
        AffineTransform trans = g2d.getTransform();

        Path2D hitbox = new Path2D.Double(rec, trans);
        colliding = getHandler().getPlayers().stream().map(Player::getBounds).anyMatch(hitbox::contains);

        g2d.setTransform(old);

        //g2d.setColor(Color.YELLOW);
	    //g2d.drawRect((int)this.dash_x-5,(int)this.dash_y-5,10,10);
	}
	//Unlike the other enemies, this boss will handle collisions internally with the player. This allows us to have an accurate hitbox
	//Despite being angled.
}
