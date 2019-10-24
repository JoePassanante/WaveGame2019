package game;

import util.Random;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Stack;

public class GameLevel extends Performer implements MouseListener, KeyListener {
    private ArrayList<GameEntity> entities;
    private Stack<GameLevel> state;
    private Random random;
    private Dimension dimension;
    private Theme theme;
    private ArrayList<Player> players;
    private int number;
    private int score;

    public void setScore(int s) {
        score = s;
    }
    public void setTheme(Theme t) {
        theme = t;
        refer(t.get(this));
    }
    public ArrayList<GameEntity> getEntities() {
        return entities;
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

    public GameLevel(GameLevel gl) {
        this(
            gl.getEntities(),
            gl.getState(),
            gl.getRandom(),
            gl.getDimension(),
            gl.getTheme(),
            gl.getPlayers(),
            gl.getNumber() + 1,
            gl.getScore() + gl.getNumber()*100
        );
        entities.forEach(e -> e.setLevel(this));
        players.forEach(p -> p.setLevel(this));
    }

    public GameLevel(ArrayList<GameEntity> e, Stack<GameLevel> s, Random r, Dimension d, Theme t, ArrayList<Player> p, int n, int c) {
        entities = e;
        state = s;
        random = r;
        dimension = d;
        setTheme(t);
        players = p;
        number = n;
        score = c;
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

    public Point.Double spawnPoint() { // TODO: avoid spawning on top of players
        Point.Double loc = new Point2D.Double();

        loc.setLocation(
            getDimension().width*(getRandom().random()+1)/3,
            getDimension().height*(getRandom().random()+1)/3
        );

        return loc;
    }

    public Point.Double targetPoint() {
        return players.stream().filter(entities::contains)
            .min((l,r) -> (int) Math.hypot(l.getPosX() - r.getPosX(), l.getPosY() - r.getPosY()))
            .map(p -> new Point.Double(p.getPosX(), p.getPosY()))
            .orElse(new Point.Double(getDimension().getWidth()/2,getDimension().getHeight()/2));
    }

    private boolean[] wasd, arrows;

    private void changeDir(boolean[] keys, Player p) {
        p.setLevel(this); // TODO: add proper initialize() method
        int x = Boolean.compare(keys[3], keys[1]);
        int y = Boolean.compare(keys[2], keys[0]);
        double h = Math.max( Math.hypot(x, y), 1);

        p.setVelX(10 * x/h);
        p.setVelY(10 * y/h);
    }

    public void tick() {
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
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(dimension);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        entities.forEach(go -> go.render(g));
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
