package org.rickosborne.detangler;

import java.util.Map;
import java.util.WeakHashMap;

public class IntPoint {
    public static Map<Integer, IntPoint> instances = new WeakHashMap<>();
    public final int x;
    public final int y;

    private IntPoint(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public static IntPoint atXY(final int x, final int y) {
        final int hashCode = hashCodeForXY(x, y);
        return instances.computeIfAbsent(hashCode, (hc) -> new IntPoint(x, y));
    }

    public static IntPoint atXY(final float x, final float y) {
        return atXY(Math.round(x), Math.round(y));
    }

    public static int hashCodeForXY(final int x, final int y) {
        return (x << 16) + y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntPoint)) {
            return false;
        }
        final IntPoint p = (IntPoint) o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return hashCodeForXY(x, y);
    }
}
