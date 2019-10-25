package util;

import game.GameClient;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class provides a sane way to handle caught exceptions in a method reference, without sticking a try catch in
 * the middle of a map or something. It does this by overloading a function more times than previously thought possible.
 */

@FunctionalInterface
public interface LambdaException <T,R,E extends Throwable> {
    R apply(T t) throws E;

    Consumer<Throwable> consume = GameClient.devMode ? Throwable::printStackTrace : t -> { };

    static <T, R, E extends Throwable> Function<T, R> wrap(LambdaException<T, R, E> l) {
        return wrap(l, consume);
    }

    static <T, R, E extends Throwable> Function<T, R> wrap(LambdaException<T, R, E> l, Consumer<Throwable> c) {
        return wrap(l, c, null);
    }

    static <T, R, E extends Throwable> Function<T, R> wrap(LambdaException<T, R, E> l, Consumer<Throwable> c, R r) {
        return t -> {
            try {
                return l.apply(t);
            }
            catch(Throwable e) {
                c.accept(e);
                return r;
            }
        };
    }

    @FunctionalInterface
    interface LambdaExceptionConsumer<U, F extends Throwable> {
        void accept(U u) throws F;
    }

    @FunctionalInterface
    interface LambdaExceptionProducer<S, F extends Throwable> {
        S get() throws F;
    }

    static <U,F extends Throwable> Consumer<U> wrapc(LambdaExceptionConsumer<U,F> l) {
        return wrapc(l, consume);
    }

    static <U,F extends Throwable> Consumer<U> wrapc(LambdaExceptionConsumer<U,F> l, Consumer<Throwable> c) {
        return t -> {
            try {
                l.accept(t);
            }
            catch(Throwable e) {
                c.accept(e);
            }
        };
    }

    static<S,F extends Throwable> Supplier<S> wraps(LambdaExceptionProducer<S,F> l) {
        return wraps(l, consume);
    }

    static<S,F extends Throwable> Supplier<S> wraps(LambdaExceptionProducer<S,F> l, Consumer<Throwable> c) {
        return wraps(l,c,null);
    }

    static<S,F extends Throwable> Supplier<S> wraps(LambdaExceptionProducer<S,F> l, Consumer<Throwable> c, S s) {
        return () -> {
            try {
                return l.get();
            }
            catch (Throwable e) {
                c.accept(e);
                return s;
            }
        };
    }
}
