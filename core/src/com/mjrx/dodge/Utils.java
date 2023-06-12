package com.mjrx.dodge;

public final class Utils {
    private Utils() {}

    public static boolean isInRange(int n, int min, int max) {
        return (min <= n) && (n <= max);
    }
}
