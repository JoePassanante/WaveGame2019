package game;

import game.enemy.Enemy;
import game.pickup.Pickup;
import util.LambdaException;
import util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class GameLevel extends Performer implements KeyListener, MouseListener, MouseMotionListener { // any current state of the game
    private ArrayList<GameEntity> entities, nonentities; // entities render and collide with players, nonentities just render
    private Stack<GameLevel> state; // state transitions are accomplished by popping then pushing here
    private Random random; // faster and more random than java.util.Random
    private Dimension dimension; // size of level
    private Theme theme; // finds assets of performers
    private ArrayList<Player> players; // contains all living and dead players
    private int currentTick, maxTick, number, score; // current time, end, level number, and current score of the level
    private boolean clipped;

    public void setCurrentTick(int tick) {
        currentTick = tick;
    }
    public void setMaxTick(int tick) {
        maxTick = tick;
    }
    public void setScore(int s) {
        score = s;
    }
    public void setTheme(Theme t) {
        theme = t;
        refer(t.get(this));
    }
    public void setClipped(boolean c) {
        clipped = c;
    }

    public ArrayList<GameEntity> getEntities() {
        return entities;
    };
    public ArrayList<GameEntity> getNonentities() {
        return nonentities;
    };
    public Stack<GameLevel> getState() {
        return state;
    }
    public Random getRandom() {
        return random;
    }
    public Dimension getDimension() {
        return dimension;
    }
    public Theme getTheme() {
        return theme;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public int getMaxTick() {
        return maxTick;
    }
    public int getNumber() {
        return number;
    }
    public int getScore() {
        return score;
    }
    public boolean getClipped() { return clipped; }
    public int getCurrentTick() {
        return currentTick;
    }

    public GameLevel(GameLevel gl) { // copy constructor makes the next level from a previous level, should probably be moved to Waves
        this(
            gl.getEntities(), gl.getState(), gl.getRandom(), gl.getDimension(), gl.getTheme(), gl.getPlayers(),
            gl.getNumber() + 1, gl.getMaxTick() + 10, gl.getScore() + 100, gl.getClipped()
        );
    }

    public GameLevel(
        ArrayList<GameEntity> e, Stack<GameLevel> s, Random r, Dimension d, Theme t, ArrayList<Player> p,
        int n, int m, int c, boolean l
    ) {
        entities = e;
        nonentities = new ArrayList<>();
        state = s;
        random = r;
        dimension = d;
        setTheme(t);
        players = p;
        number = n;
        maxTick = m;
        setScore(c);
        setClipped(l);
    }

    public Point.Double spawnPoint() { // find a good place for enemies to spawn
        final Point.Double loc = new Point.Double();
        do { // random point in the level
            loc.setLocation(
                getRandom().random()*dimension.getWidth(),
                getRandom().random()*dimension.getHeight()
            );
        }
        while( getPlayers().stream().filter(entities::contains).anyMatch( p -> // not on top of a living player
            Math.hypot(loc.getX() - p.getPosX(), loc.getY() - p.getPosY()) < Math.hypot(p.getWidth(), p.getHeight())
        ));
        return loc;
    }

    public Point.Double targetPoint() { // find a good place for enemies to attack
        return players.stream().filter(entities::contains)
            .max(Comparator.comparing(Player::getHealth))
            .map(p -> new Point.Double(p.getPosX(), p.getPosY()))
            .orElse(spawnPoint()); // player with the highest health
    }

    public void start() { // called when the level starts ticking
        players.forEach(p -> p.setLevel(this));
        entities.forEach(e -> e.setLevel(this));
        nonentities.forEach(n -> n.setLevel(this));
    }

    public void end() {
        getState().pop();
    } // called when the level stops ticking

    @Override
    public void tick() { // update level and entities
        super.tick();

        setScore(getScore() + 1); // increase score every tick (sixty times a second)

        if(currentTick <= 0) {
            start();
        }
        for(int i = entities.size()-1; i >= 0; i -= 1) {
            entities.get(i).tick();
        }
        for(int i = nonentities.size()-1; i >= 0; i -= 1) {
            nonentities.get(i).tick();
        }
        for(int i = getEntities().size()-1; i >= 0; i -= 1) { // TODO: more efficient ADT than brute force collisions
            for(int j = getPlayers().size()-1; j >= 0; j -= 1) {
                if(getEntities().contains(getPlayers().get(j))) { // only collide living players
                    if (getEntities().get(i).getBounds().intersects(getPlayers().get(j).getBounds())) {
                        getEntities().get(i).collide(getPlayers().get(j));
                    }
                }
            }
        }
        if(currentTick >= maxTick) {
            end();
        }

        currentTick += 1;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(dimension);
    }

    @Override
    public void render(Graphics g) {
        render(g,0,0);
    }

    public void render(Graphics g, int x, int y) { // tile the background for moving levels
        g.translate(x%getBounds().width,y%getBounds().height);
        for(int i=-1; i<=1; i+=1)
            for(int j=-1; j<=1; j+=1) {
                g.translate(i*getBounds().width, j*getBounds().height);
                super.render(g);
                g.translate(-i*getBounds().width, -j*getBounds().height);
            }
        g.translate(-(x%getBounds().width),-(y%getBounds().height));

        entities.forEach(ge -> ge.render(g));
        nonentities.forEach(ge -> ge.render(g));

        g.setColor(Color.white);
        g.setFont(new Font("Amoebic", Font.BOLD, 30));

        if(GameClient.devMode){
            g.drawString("Entities: " + entities.size(), getDimension().width-300, getDimension().height-200);
            g.drawString("Enemies: " + entities.stream().filter(Enemy.class::isInstance).count(), getDimension().width-300, getDimension().height-200);
            g.drawString("Pickups: " + entities.stream().filter(Pickup.class::isInstance).count(), getDimension().width-300, getDimension().height-150);
            g.drawString("Trails: " + nonentities.size(), getDimension().width-300, getDimension().height-50);
        }

        if(getNumber() > 0) {
            g.drawString("Score: " + getScore(), 15, 25);
            g.drawString("Level: " + getNumber(), 15, 75);

            // HEALTH BAR
            g.fillRect(15, 1000, 400, 64);
            for (int p = getPlayers().size() - 1; p >= 0; p -= 1) {
                int h = (int) GameEntity.clamp(players.get(p).getHealth() * 255 / 100, 0, 255);
                g.setColor(new Color(255 - h, h, 0));
                g.fillRect(15, 1000 + 64 * p / players.size(), (int) players.get(p).getHealth() * 4, 64 / players.size());
            }
            g.setColor(Color.gray);
            g.drawRect(15, 1000, 400, 64);
        }
    }

    @Override
    public void render(Clip clip, int i) { // TODO: we should really be queuing sound frames from a bufffer that updates every tick
        if(clipped) { // start playing background music
            if(!clip.isActive()) {
                super.render(clip, Clip.LOOP_CONTINUOUSLY);
                ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-24f);
                setClipped(false);
            }
            else {
                clip.close();
            }
        }
        Clip c = null;
        for(GameEntity ge : entities) { // echo entities that make noise
            if(c == null || c.isActive()) {
                c = LambdaException.wraps(AudioSystem::getClip).get();
            }
            ge.render(c, i);
        }
    }
    // pass swing events to controllers
    @Override
    public void keyPressed(KeyEvent e) {
        players.forEach(p -> p.getController().keyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        players.forEach(p -> p.getController().keyReleased(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO: press animation
        players.forEach(p -> p.getController().mousePressed(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO: release animation
        players.forEach(p -> p.getController().mouseReleased(e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        players.forEach(p -> p.getController().mouseMoved(e));
    }

    // unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }
    @Override public final void mouseDragged(MouseEvent e) { }

    public static class Unending extends GameLevel { // level that does not run out of time, like menus and upgrades
        public Unending(GameLevel gl) {
            super(gl);
        }

        @Override
        public void tick() {
            setMaxTick(getCurrentTick() + 1);
            super.tick();
        }
    }
}
