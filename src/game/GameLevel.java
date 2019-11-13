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

public class GameLevel extends Performer implements KeyListener, MouseListener, MouseMotionListener {
    private ArrayList<GameEntity> entities, nonentities;
    private Stack<GameLevel> state;
    private Random random;
    private Dimension dimension;
    private Theme theme;
    private ArrayList<Player> players;
    private int number;
    private int score;
    private boolean clipped;

    public void setScore(int s) {
        score = s;
    }
    public void setTheme(Theme t) {
        theme = t;
        refer(t.get(this));
        clipped = true;
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
    public int getNumber() {
        return number;
    }
    public int getScore() {
        return score;
    }
    public boolean getClipped() { return clipped; }

    public GameLevel(GameLevel gl) {
        this(
            gl.getEntities(), gl.getState(), gl.getRandom(), gl.getDimension(), gl.getTheme(), gl.getPlayers(),
            gl.getNumber() + 1, gl.getScore(), gl.getClipped()
        );
    }

    public GameLevel(
        ArrayList<GameEntity> e, Stack<GameLevel> s, Random r, Dimension d, Theme t, ArrayList<Player> p,
        int n, int c, boolean l
    ) {
        entities = e;
        state = s;
        random = r;
        dimension = d;
        setTheme(t);
        players = p;
        number = n;
        setScore(c);
        setClipped(l);
        nonentities = new ArrayList<>();
    }

    public Point.Double spawnPoint() {
        final Point.Double loc = new Point.Double();
        do {
            loc.setLocation(
                getRandom().random()*dimension.getWidth(),
                getRandom().random()*dimension.getHeight()
            );
        }
        while( getPlayers().stream().filter(entities::contains).anyMatch( p ->
            Math.hypot(loc.getX() - p.getPosX(), loc.getY() - p.getPosY()) < Math.hypot(p.getWidth(), p.getHeight())
        ));
        return loc;
    }

    public Point.Double targetPoint() {
        return players.stream().filter(entities::contains)
            .max(Comparator.comparing(Player::getHealth))
            .map(p -> new Point.Double(p.getPosX(), p.getPosY()))
            .orElse(spawnPoint());
    }


    private boolean initialized;
    @Override
    public void tick() {
        super.tick();

        setScore(getScore() + 1);

        if(!initialized) { // TODO: add proper initialize() method
            players.forEach(p -> p.setLevel(this));
            entities.forEach(e -> e.setLevel(this));
            nonentities.forEach(n -> n.setLevel(this));
            initialized = true;
        }

        for(int i = entities.size()-1; i >= 0; i -= 1) {
            entities.get(i).tick();
        }
        for(int i = nonentities.size()-1; i >= 0; i -= 1) {
            nonentities.get(i).tick();
        }
        for(int i = getEntities().size()-1; i >= 0; i -= 1) { // TODO: more efficient ADT than brute force collisions
            for(int j = getPlayers().size()-1; j >= 0; j -= 1) {
                if(getEntities().contains(getPlayers().get(j))) {
                    if (getEntities().get(i).getBounds().intersects(getPlayers().get(j).getBounds())) {
                        getEntities().get(i).collide(getPlayers().get(j));
                    }
                }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(dimension);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

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
//        g.drawString("Level Progress: " + 100*currentTick/maxTick + "%", 15, 175);
//        g.drawString("Health: " + getPlayers().stream().mapToDouble(GameEntity::getHealth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 1050);
//        g.drawString("Size: " + getPlayers().stream().mapToDouble(GameEntity::getWidth).mapToObj(Double::toString).collect(Collectors.joining(",")), 15, 225);

//        Image shieldImg = getTheme().get("shield" + (int)getPlayers().stream().mapToDouble(Player::getArmor).average().orElse(1.0)*5.0 + 1);
//        g.drawImage(shieldImg, 440, 1010, 40, 40, null);
//        g.drawString(getPlayers().stream().mapToDouble(Player::getArmor).mapToObj(Double::toString).collect(Collectors.joining(",")), 500, 1040);

        if(players.size() > 0) {
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
        if(clipped) {
            clip.close();
            super.render(clip, Clip.LOOP_CONTINUOUSLY);
            ((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-24f);
            setClipped(false);
        }
        Clip c = null;
        for(GameEntity ge : entities) {
            if(c == null || c.isActive()) {
                c = LambdaException.wraps(AudioSystem::getClip).get();
            }
            ge.render(c, i);
        }
    }

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

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }
    @Override public final void mouseDragged(MouseEvent e) { }
}
