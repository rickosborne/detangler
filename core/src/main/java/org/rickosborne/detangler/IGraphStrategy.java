package org.rickosborne.detangler;

public interface IGraphStrategy {
    Tangle buildTangle(
        final int pointCount,
        final int width,
        final int height,
        final OnIntersectionChange onIntersectionChange
    );
}
