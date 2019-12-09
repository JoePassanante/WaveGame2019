package game;

import java.awt.*;
import java.awt.image.ColorModel;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;

public class Transition extends GameLevel { // the animations between menus and levels
    private Performer source, destination; // the levels to animate between

    public Composite getComposite() { // default fade transition
        return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f*getCurrentTick()/getMaxTick());
    }

    public Transition(GameLevel lev, Performer src, Performer dst) {
        super(lev);
        source = src;
        destination = dst;
    }

    @Override
    public void render(Graphics g) { // compose source and destination over time
        super.render(g);
        Graphics2D g2d = (Graphics2D)g;
        source.render(g2d);
        Composite old = g2d.getComposite();
        g2d.setComposite(getComposite());
        destination.render(g2d);
        g2d.setComposite(old);
    }

    @FunctionalInterface
    private interface TransitionComposite extends Composite { // allows us to use lambdas by using the next interface instead
        @Override
        TransitionCompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints);
    }

    @FunctionalInterface
    private interface TransitionCompositeContext extends CompositeContext { // allows us to use lambdas by ignoring the dispose method
        @Override
        default void dispose() { }
    }

    public static class Slide extends Transition { // slide destination across source
        private int xl, yl, xt, yt; // move from l in the direction of t
        public Slide(GameLevel lev, Performer src, Performer dst, int xt, int yt) {
            super(lev, src, dst);
            this.xt = xt;
            this.yt = yt;
            this.xl = Math.abs(xt)*getMaxTick();
            this.yl = Math.abs(yt)*getMaxTick();
        }

        @Override
        public Composite getComposite() { // decides which performer to draw at each pixel
            return (TransitionComposite) (srcCM, dstCM, h) -> (srcR, dstR, dstWR) -> { // slooow ;-;
                int[] read = new int[Math.max(srcR.getNumBands(), dstR.getNumBands())];
                for (int y = 0; y < dstWR.getHeight(); y += 1) {
                    for (int x = 0; x < dstWR.getWidth(); x += 1) {
                        dstWR.setPixel(x, y, (xl < x && yl < y ? srcR : dstR).getPixel(x, y, read));
                    }
                }
            };
        }

        @Override
        public void tick() {
            super.tick();
            xl += xt;
            yl += yt;
        }

        public static BiFunction<Performer,Performer,Transition> vertical(GameLevel lev) {
            return (s,d) -> new Slide(lev, s, d, 0, -lev.getBounds().height/lev.getMaxTick());
        } // make a new vertical slide transition
        public static BiFunction<Performer,Performer,Transition> horizontal(GameLevel lev) {
            return (s,d) -> new Slide(lev, s, d, -lev.getBounds().width/lev.getMaxTick(), 0);
        } // make a new horizontal slide transition
    }

    public static class Modulo extends Transition { // dissolve source into destination
        private IntBinaryOperator operator;

        public Modulo(GameLevel lev, Performer src, Performer dst, IntBinaryOperator o) {
            super(lev, src, dst);
            operator = o;
        }

        @Override
        public Composite getComposite() { // decides which performer to draw at each pixel
            return (TransitionComposite) (srcCM, dstCM, h) -> (srcR, dstR, dstWR) -> { // slooow ;-;
                int[] read = new int[Math.max(srcR.getNumBands(), dstR.getNumBands())];
                for (int y = 0; y < dstWR.getHeight(); y += 1) {
                    for (int x = 0; x < dstWR.getWidth(); x += 1) {
                        dstWR.setPixel(x, y, (
                            operator.applyAsInt(x, y) % Math.max(1, (getMaxTick() - getCurrentTick()))
                                < getCurrentTick() ? srcR : dstR
                        ).getPixel(x, y, read) );
                    }
                }
            };
        }

        public static BiFunction<Performer,Performer,Transition> vertical(GameLevel lev) {
            return (s,d) -> new Modulo(lev, s, d, (x,y) -> x);
        } // vertical ribbons
        public static BiFunction<Performer,Performer,Transition> horizontal(GameLevel lev) {
            return (s,d) ->  new Modulo(lev, s, d, (x,y) -> y);
        } // horizontal ribbons
        public static BiFunction<Performer,Performer,Transition> diagonal(GameLevel lev) {
            return (s,d) ->  new Modulo(lev, s, d, Integer::sum);
        } // diagonal ribbons
        public static BiFunction<Performer,Performer,Transition> radial(GameLevel lev) {
            return (s,d) ->  new Modulo(lev, s, d, (x,y) -> (int)Math.sqrt(x*x + y*y));
        } // circular ribbons
        public static BiFunction<Performer,Performer,Transition> droplets(GameLevel lev) {
            return (s,d) ->  new Modulo(lev, s, d, (x,y) -> (x*x + y*y));
        } // dissolving ripples
    }
}
