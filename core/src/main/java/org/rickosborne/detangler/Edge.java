package org.rickosborne.detangler;

import java.util.Map;
import java.util.WeakHashMap;

public class Edge {
    private static final Map<Integer, Edge> instances = new WeakHashMap<>();
    private final int _hashCode;
    public final int high;
    public final int low;

    private Edge(final int low, final int high) {
        this.low = low;
        this.high = high;
        this._hashCode = hashCodeFor(low, high);
    }

    public static Edge between(final int a, final int b) {
        final int low = Math.min(a, b);
        final int high = Math.max(a, b);
        final int hashCode = hashCodeFor(low, high);
        return instances.computeIfAbsent(hashCode, (c) -> new Edge(low, high));
    }

    public static int hashCodeFor(final int low, final int high) {
        return (low << 16) + high;
    }

    @Override
    public int hashCode() {
        return _hashCode;
    }
}
