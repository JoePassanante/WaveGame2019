package game;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.ColorModel;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;

public class Transition extends GameLevel {
    private int currentTick, maxTick;
    private Performer source, destination;
    private IntFunction<Composite> composite;

    public Transition(GameLevel lev, int max, Performer src, Performer dst, IntFunction<Composite> comp) {
        super(lev);
        maxTick = max;
        source = src;
        destination = dst;
        composite = comp;
    }

    @Override
    public void tick() {
        super.tick();
        currentTick += 1;
        if(currentTick > maxTick) {
            getState().pop();
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Graphics2D g2d = (Graphics2D)g;
        source.render(g2d);
        Composite old = g2d.getComposite();
        g2d.setComposite(composite.apply(currentTick));
        destination.render(g2d);
        g2d.setComposite(old);
    }

    @Override
    public void render(Clip c, int i) {
        // let the menu music continue playing
    }

    public static class Fade extends Transition {
        public Fade(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, t -> AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f*t/max));
        }
    }

    @FunctionalInterface
    private interface TransitionComposite extends Composite {
        @Override
        TransitionCompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints);
    }

    @FunctionalInterface
    private interface TransitionCompositeContext extends CompositeContext {
        @Override
        default void dispose() { }
    }

    public static class Modulo extends Transition {
        public Modulo(GameLevel lev, int max, Performer src, Performer dst, IntBinaryOperator ibo) {
            super(lev, max, src, dst, t -> (TransitionComposite) (srcCM, dstCM, h) -> (srcR, dstR, dstWR) -> {
                for(int y = 0; y < dstWR.getHeight(); y += 1) {
                    for(int x = 0; x < dstWR.getWidth(); x += 1) {
                        dstWR.setPixel( x, y, (
                                ibo.applyAsInt(x,y) % Math.max(1,(max-t)) < t ? srcR : dstR
                            ).getPixel( x, y, new int[Math.max(srcR.getNumBands(), dstR.getNumBands())] )
                        );
                    }
                }
            });
        }
    }

    public static class Vertical extends Modulo {
        public Vertical(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, (x,y) -> x);
        }
    }

    public static class Horizontal extends Modulo {
        public Horizontal(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, (x,y) -> y);
        }
    }

    public static class Diagonal extends Modulo {
        public Diagonal(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, Integer::sum);
        }
    }

    public static class Radial extends Modulo {
        public Radial(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, (x,y) -> (int)Math.sqrt(x*x + y*y));
        }
    }

    public static class Droplets extends Modulo {
        public Droplets(GameLevel lev, int max, Performer src, Performer dst) {
            super(lev, max, src, dst, (x,y) -> (x*x + y*y));
        }
    }
}
