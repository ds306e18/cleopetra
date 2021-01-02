package dk.aau.cs.ds306e18.tournament.utility;

public class PowMath {

    /** Returns 2 to the power of n >= 0. */
    public static int pow2(int n) {
        int res = 1;
        for (int i = 0; i < n; i++) {
            res *= 2;
        }
        return res;
    }

    /** Returns the 2-log of n ceiled to an integer. */
    public static int log2(int n) {
        return (int) Math.ceil(Math.log(n) / Math.log(2));
    }

    /** Returns true if the given number is a positive power of 2. */
    public static boolean isPowOf2(int n) {
        // from https://codereview.stackexchange.com/a/172853
        return n > 0 && ((n & (n - 1)) == 0);
    }
}
