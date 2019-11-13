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
    public GameWindow() {
        super("Wave Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // fill the screen with the window
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // make the window resizable if devMode is on
        setResizable(GameClient.devMode);
        setUndecorated(!GameClient.devMode);
        // center the window
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        // make the window fullscreen on OSX
        if (System.getProperty("os.name").contains("Mac OS X")) { // If user is on macOS
            try {
                Class.forName("com.apple.eawt.FullScreenUtilities")
                    .getMethod("setWindowCanFullScreen", Window.class, boolean.class)
                    .invoke(null, this, true);
                Object app = Class.forName("com.apple.eawt.Application")
                    .getMethod("getApplication")
                    .invoke(null);
                app.getClass().getMethod("requestToggleFullScreen", Window.class).invoke(app, this);
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    protected void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }

    /**
     * Inverts transformation of this JFrame to process mouse events in game space
     */
    private AffineTransform screenSpace; // The graphical transformation of this JFrame
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

    public void draw(Performer so) {
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
                    getWidth()/so.getBounds().getWidth(),
                    getHeight()/so.getBounds().getHeight()
                );

                g.translate(getWidth()/2,getHeight()/2);
                g.scale(scaleFactor, scaleFactor);
                g.translate(-so.getBounds().getWidth()/2,-so.getBounds().getHeight()/2);

                screenSpace = g.getTransform();
                so.render(g);

                g.setTransform(old);
                g.dispose();
                bs.show();
            }
        }
    }

    public static Rectangle drawStringCentered(Graphics g, Font f, String s, int x, int y) {
        Font old = g.getFont();
        g.setFont(f);
        Rectangle bounds = f.getStringBounds(
            s, new FontRenderContext(((Graphics2D)g).getTransform(), true, true)
        ).getBounds();
        g.drawString(s, x-bounds.width/2, y-bounds.height/2);
        g.setFont(old);
        return bounds;
    }
}