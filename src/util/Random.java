package util;

// Implementation of http://prng.di.unimi.it/xoshiro256starstar.c
// seeded with http://prng.di.unimi.it/splitmix64.c

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Random extends java.util.Random {
    private long[] state;

    public Random(long seed) {
        state = new long[4];
        for(int s = 0; s < state.length; s += 1) {
            seed += 0x9e3779b97f4a7c15L;
            long result = seed;
            result ^= result >> 30;
            result *= 0xbf58476d1ce4e5b9L;
            result ^= result >> 27;
            result *= 0x94d049bb133111ebL;
            result ^= result >> 31;
            state[s] = result;
        }
    }

    public Random() {
        this(System.nanoTime());
    }

    @Override
    protected int next(int bits) {
        long result = Long.rotateLeft(state[1] * 5, 7) * 9;
        long onetwo = state[1] << 17;
        state[2] ^= state[0];
        state[3] ^= state[1];
        state[1] ^= state[2];
        state[0] ^= state[3];
        state[2] ^= onetwo;
        state[3] = Long.rotateLeft(state[3], 45);
        return (int)((result & ((1L << 48) - 1)) >>> (48 - bits)); // scuffed JDK generator only uses 48 state bits despite storing 64
    }

    public double random() {
        return (nextLong() >>> 11) * 0x1.0p-53;
    }

    public class RandomDifferentElement<T> implements Supplier<T> {
        private List<T> source;
        private int last;

        @SafeVarargs
        public RandomDifferentElement(T... s) {
            this(Arrays.asList(s));
        }

        public RandomDifferentElement(List<T> s) {
            source = s;
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
                l = nextInt(source.size());
            }
            last = l;
            return source.get(l);
        }
    }

    public static <T,U,R> BiFunction<T,U,R> reduce(Supplier<BiFunction<T,U,R>> s) {
        return (t,u) -> s.get().apply(t,u);
    }
}
