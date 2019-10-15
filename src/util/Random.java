package util;

// Implementation of http://prng.di.unimi.it/xoshiro256starstar.c
// seeded with http://prng.di.unimi.it/splitmix64.c

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
        int result = (int)((Long.rotateLeft(state[1] * 5, 7) * 9) >>> (48 - bits));
        long onetwo = state[1] << 17;
        state[2] ^= state[0];
        state[3] ^= state[1];
        state[1] ^= state[2];
        state[0] ^= state[3];
        state[2] ^= onetwo;
        state[3] = Long.rotateLeft(state[3], 45);
        return result;
    }

    public double random() {
        return (nextLong() >> 11) * 0x1.0p-53;
    }
}
