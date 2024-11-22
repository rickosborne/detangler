package org.rickosborne.detangler;

public final class Rand {
    public static int index(final int size) {
        return (int) Math.floor(Math.random() * size);
    }

    public static int around(final int radius) {
        return (int) Math.floor(Math.random() * radius * 2 - radius);
    }
}
