package util;

public class SigmoidApproximation {
    private static int store(int x, int a, int b) {
        return x == 0 ? x : a/(b/x + Integer.signum(x));
    }

    private static int restore(int x, int a, int b) {
        return x == 0 ? x : b/(a/x - Integer.signum(x));
    }

    private static int div(int x, int y) {
        return y == 0 ? y : x/y;
    }

    private static int har(int x, int y) {
        int a = Integer.max(x,y), b = Integer.min(x,y);
        return div(a, a/b + 1);
    }

    private static int add(int x, int y, int a) {
        int l = div(a,x) - Integer.signum(x), r = div(a,y) - Integer.signum(y), h = har(l,r);
        return div(a, h + Integer.signum(h));
    }

    private static int mul(int x, int y, int a, int b) {
        int l = div(a,x) - Integer.signum(x), r = div(a,y) - Integer.signum(y);
        return div(a,l*r/b);
    }

    private static int pow(int x, int y, int a, int b) {
        return x;
    }

    public static void main(String... args) {
        final int a = Integer.MAX_VALUE, b = 1 << 28;
        System.out.println(restore(add(store(7, a, b), store(5 , a, b), a), a, b));
    }
}
