package game;

import game.enemy.Enemy;
import game.pickup.Pickup;
import util.LambdaException;
import util.Random;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class GameLevel extends Performer implements MouseListener, KeyListener {
    private ArrayList<GameEntity> entities;
    private ArrayList<GameEntity> nonentities;
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
        wasd = new boolean[4];
        arrows = new boolean[4];
    }

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO: press animation
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO: release animation
    }

    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }

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

    private boolean[] wasd, arrows;
    private void changeDir(boolean[] keys, Player p) {
        int x = Boolean.compare(keys[3], keys[1]);
        int y = Boolean.compare(keys[2], keys[0]);
        double h = Math.max( Math.hypot(x, y), 1);

        p.setVelX(10 * x/h);
        p.setVelY(10 * y/h);
    }

    private boolean initialized;
    @Override
    public void tick() {
        super.tick();

        if(!initialized) { // TODO: add proper initialize() method
            entities.forEach(e -> e.setLevel(this));
            players.forEach(p -> p.setLevel(this));
            initialized = true;
        }

        if(players.size() == 1) {
            boolean[] or = new boolean[4];
            for (int b = 0; b < or.length; b += 1) {
                or[b] = wasd[b] || arrows[b];
            }
            changeDir(or, players.get(0));
        }
        else if(players.size() == 2) {
            changeDir(wasd, players.get(0));
            changeDir(arrows, players.get(1));
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
//          g.drawString("FPS: " + fps, getGameDimension().width-300, getGameDimension().height-100);
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
        for(GameEntity ge : entities) {
            Clip c = LambdaException.wraps(AudioSystem::getClip).get();
            ge.render(c, i);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // start moving if key is pressed

        wasd[0] |= key == KeyEvent.VK_W;
        wasd[1] |= key == KeyEvent.VK_A;
        wasd[2] |= key == KeyEvent.VK_S;
        wasd[3] |= key == KeyEvent.VK_D;

        arrows[0] |= key == KeyEvent.VK_UP;
        arrows[1] |= key == KeyEvent.VK_LEFT;
        arrows[2] |= key == KeyEvent.VK_DOWN;
        arrows[3] |= key == KeyEvent.VK_RIGHT;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode(); // stop moving if key is released

        wasd[0] &= key != KeyEvent.VK_W;
        wasd[1] &= key != KeyEvent.VK_A;
        wasd[2] &= key != KeyEvent.VK_S;
        wasd[3] &= key != KeyEvent.VK_D;

        arrows[0] &= key != KeyEvent.VK_UP;
        arrows[1] &= key != KeyEvent.VK_LEFT;
        arrows[2] &= key != KeyEvent.VK_DOWN;
        arrows[3] &= key != KeyEvent.VK_RIGHT;
    }
}
