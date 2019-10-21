package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;

public class GameWindow extends JFrame {
    private AffineTransform screenSpace; // The graphical transformation of this JFrame

    public GameWindow() {
        super("Wave Game");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set fullscreen
        if (System.getProperty("os.name").toLowerCase().contains("mac")) { //If user is on macOS
            setResizable(true);
            try {
                com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this, true);
                com.apple.eawt.Application.getApplication().requestToggleFullScreen(this);
            } catch (Exception e) {
                System.err.println("Failed to load apple extensions package");
            }
        } else {
            setResizable(GameClient.devMode);
            setUndecorated(!GameClient.devMode);
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        screenSpace = new AffineTransform();
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    /**
     * Inverts transformation of this JFrame to process mouse events in game space
     */
    @Override
    protected void processMouseEvent(MouseEvent e) {
        try {
            Point2D p = new Point();
            screenSpace.inverseTransform(e.getPoint(), p);
            super.processMouseEvent( new MouseEvent(
                e.getComponent(),
                e.getID(),
                e.getWhen(),
                e.getModifiers(),
                (int)p.getX(),
                (int)p.getY(),
                e.getClickCount(),
                e.isPopupTrigger()
            ));
        }
        catch(NoninvertibleTransformException nite) {
            nite.printStackTrace();
        }
    }

    public void draw(Animatable anm) {
        if (getWidth() > 0 && getHeight() > 0) {
            BufferStrategy bs = getBufferStrategy(); // Render each Animatable then draw them all at once
            if (bs == null) {
                createBufferStrategy(3);
            }
            else {
                Graphics2D g = (Graphics2D) bs.getDrawGraphics();
                AffineTransform old = g.getTransform();

                g.clearRect(0, 0, getWidth(), getHeight());

                double scaleFactor = Math.min(
                    getWidth()/getPreferredSize().getWidth(),
                    getHeight()/getPreferredSize().getHeight()
                );

                g.translate(getWidth()/2,getHeight()/2);
                g.scale(scaleFactor, scaleFactor);
                g.translate(-getPreferredSize().getWidth()/2,-getPreferredSize().getHeight()/2);

                screenSpace = g.getTransform();
                anm.render(g);

                g.setTransform(old);
                g.dispose();
                bs.show();
            }
        }
    }

    public static void drawStringCentered(Graphics g, Font f, String s, int x, int y) {
        Font old = g.getFont();
        g.setFont(f);
        Rectangle bounds = f.getStringBounds(
            s, new FontRenderContext(((Graphics2D)g).getTransform(), true, true)
        ).getBounds();
        g.drawString(s, x-bounds.width/2, y-bounds.height/2);
        g.setFont(old);
    }
}