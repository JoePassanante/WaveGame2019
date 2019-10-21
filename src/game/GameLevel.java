package game;

import util.Random;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Stack;

public class GameLevel extends GameState  {
    private int number;
    private int score;
    private Theme theme;
    private Random random;
    private Dimension dimension;
    private List<Player> players;
    public void setScore(int s) {
        score = s;
    }
    public void setTheme(Theme t) {
        theme = t;
        img = t.get(this);
    }
    public int getNumber() {
        return number;
    }
    public int getScore() {
        return score;
    }
    public Theme getTheme() {
        return theme;
    }
    public Random getRandom() {
        return random;
    }
    public Dimension getDimension() {
        return dimension;
    }
    public List<Player> getPlayers() {
        return players;
    }

    private Image img;

    public GameLevel(GameLevel g) {
        this(g.getState(), g.getNumber() + 1, g.getScore(), g.getTheme(), g.getRandom(), g.getDimension(), g.getPlayers());
        addAll(g);
        forEach(f -> f.setLevel(this));
        if(img == null) {
            img = getTheme().get(g);
        }
    }

    public GameLevel(Stack<GameState> s, int n, int c, Theme t, Random r, Dimension d, List<Player> p) {
        super(s);
        number = n;
        score = c;
        theme = t;
        random = r;
        dimension = d;
        players = p;
        img = getTheme().get(this);
        wasd = new boolean[4];
        arrows = new boolean[4];
    }

    public Point.Double spawnPoint() { // TODO: avoid spawning on top of players
        Point.Double loc = new Point2D.Double();

        loc.setLocation(
            getDimension().width*(getRandom().random()+1)/3,
            getDimension().height*(getRandom().random()+1)/3
        );

        return loc;
    }

    private boolean[] wasd, arrows;

    private void changeDir(boolean[] keys, GameObject p) {
        p.setLevel(this); // TODO: add proper initialize() method
        int x = Boolean.compare(keys[3], keys[1]);
        int y = Boolean.compare(keys[2], keys[0]);
        double h = Math.max( Math.hypot(x, y), 1);

        p.setVelX(10 * x/h);
        p.setVelY(10 * y/h);
    }

    @Override
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

        for(int i = size()-1; i >= 0; i -= 1) {
            get(i).tick();
        }
    }

    @Override
    public void render(Graphics g) {
        if(img != null) {
            Dimension dim = getDimension();
            g.drawImage(img, 0, 0, dim.width, dim.height, null);
        }
        forEach(go -> go.render(g));
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // start moving if key is pressed
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
        int key = e.getKeyCode();

        // stop moving if key is pressed
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
