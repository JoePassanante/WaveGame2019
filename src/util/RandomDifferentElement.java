package util;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class RandomDifferentElement<T> implements Supplier<T> {
    private List<T> source;
    private int last;
    private Random generator;

    @SafeVarargs
    public RandomDifferentElement(T... s) {
        this(new Random(), s);
    }

    @SafeVarargs
    public RandomDifferentElement(Random g, T... s) {
        generator = g;
        source = Arrays.asList(s);
        last = -1;
    }

    @Override
    public T get() {
        if(source.size() == 0) {
            return null;
        }
        else if(source.size() == 1) {
            return source.get(0);
        }
        int l = last;
        while(l == last) {
            l = generator.nextInt(source.size());
        }
        last = l;
        return source.get(l);
    }

    public static <T,U,R> BiFunction<T,U,R> reduce(Supplier<BiFunction<T,U,R>> s) {
        return (t,u) -> s.get().apply(t,u);
    }
}
