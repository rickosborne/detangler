package org.rickosborne.detangler;

import java.util.Map;
import java.util.WeakHashMap;

public class IntRect {
    private static final Map<Integer, IntRect> instances = new WeakHashMap<>();
    public final int height;
    public final int width;
    public final int x;
    public final int y;

    private IntRect(
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static IntRect fromXYWH(
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        final int hc = hashCodeForXYWH(x, y, width, height);
        return instances.computeIfAbsent(hc, (c) -> new IntRect(x, y, width, height));
    }

    public static int hashCodeForXYWH(
        final int x,
        final int y,
        final int width,
        final int height
    ) {
        return (width << 24) + (height << 16) + (x << 8) + y;
    }
}
