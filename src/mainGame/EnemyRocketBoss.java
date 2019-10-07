package mainGame;

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
	private Player player;
	private double drawAngle = 90;
	private int speed = 18;
	private HUD hud;
	private GameMode mode;
	private boolean colliding = false;
	private int rocketTimer = 120;	private int health = 1000;

    private int difficulty = 1;

    public EnemyRocketBoss(double x, double y, Player p, Handler handler, HUD hud, GameMode mode, int diff) {
		super(x, y, 80, 296, handler);
		this.player = p;
		this.hud = hud;
		this.mode = mode;
		difficulty = diff;
	}

	@Override
	public void tick() {
		if (difficulty > 1){
			rocketTimer--;
			if (rocketTimer < 0) {
				rocketTimer = 120;
                getHandler().addObject( new EnemyRocketBossMissile(
                    Math.cos(Math.toRadians(this.drawAngle+90))*40  + this.x,
                    Math.sin(Math.toRadians(this.drawAngle+90))*40 +this.y,
                    getHandler(),this.drawAngle,
                    10,hud,player, difficulty > 2 ? 0.5 : 0
                ) );
			}
		}
		if(this.health%150 == 0){
            getHandler().addObject( new EnemyBurst(
                -200,
                200,
                15,
                15,
                200,
                new String[]{ "left", "right", "top", "bottom" }[(int)(Math.random()*4)],
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
                this.dash_x = (player.getX()+player.getWidth()/2.0) + player.velX*1.5;
                this.dash_y = (player.getY()+player.getHeight()/2.0) + player.velY*1.5;
                double angle = EnemyRocketBoss.GetAngleOfLineBetweenTwoPoints(
                    new Point.Double(this.x+40, this.y),
                    new Point.Double(dash_x,dash_y)
                );
                this.dash_x = (player.getX()+player.getWidth()/2.0)+Math.cos(Math.toRadians(angle))*100;
                this.dash_y = (player.getY()+player.getHeight()/2.0)+Math.sin(Math.toRadians(angle))*100;
                if(cooldown<=0){
                    AudioUtil.playClip("/gameSound/MissileSound.wav", false);
                    this.drawAngle = angle;
                    this.inDash = true;
                    cooldown = 60 - (int)(Math.random()*25); //lazy way to make cooldown shorter
                }
                else{
                    cooldown--;
                    this.drawAngle = this.drawAngle-Math.max(-5,Math.min(angleDifference(this.drawAngle,angle),5));
                }
			}
		}
		if(health<=0){
			System.out.println("Removing Boss");
            getHandler().removeObject(this);
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
		this.x = this.x + this.velX;
		this.y = this.y +  this.velY;

		double diffX = this.x - this.dash_x;
		double diffY = this.y - this.dash_y;
		double distance = Math.sqrt(((this.x - this.dash_x) * (this.x - this.dash_x))
				+ ((this.y - this.dash_y) * (this.y - this.dash_y)));

		this.velX = -((this.speed / distance) * diffX); // numerator affects speed of enemy
		this.velY = -((this.speed / distance) * diffY);// numerator affects speed of enemy

		if((Math.abs(this.dash_x-this.x)<=Math.abs(velX))&&(Math.abs(this.dash_y-this.y)<=Math.abs(velY))){
			this.inDash = false;
			this.velX = 0;
			this.velY = 0;
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

		g2d.translate(Math.cos(Math.toRadians(this.drawAngle-90))*40 +this.x, Math.sin(Math.toRadians(this.drawAngle-90))*40 +this.y);
		g2d.rotate(Math.toRadians(this.drawAngle + 90));

        g2d.drawImage(getHandler().getTheme().get(inDash ? getClass().getSimpleName() + "On" : getClass().getSimpleName()), 0, 0, 80, 296, null);

		AffineTransform trans = g2d.getTransform();

        Rectangle2D rec = new Rectangle.Double(30, 0, 20,inDash ?  230 : 180);
		Path2D bounds = new Path2D.Double(rec,trans);
		
	    g2d.setTransform(old);
	    
	    Rectangle2D playerBounds = new Rectangle2D.Double(player.x,player.y,player.getWidth(),player.getHeight());
		//g2d.fill(playerBounds);
	    //g2d.fill(bounds);
		
		if(bounds.intersects(playerBounds)){
	    	hud.health = hud.health - 1;
	    	colliding = true;
	    }else {
	    	colliding = false;
	    }
	    //g2d.setColor(Color.YELLOW);
	    //g2d.drawRect((int)this.dash_x-5,(int)this.dash_y-5,10,10);
	    
	    
	}
	//Unlike the other enemies, this boss will handle collisions internally with the player. This allows us to have an accurate hitbox
	//Despite being angled.
}
