package org.rickosborne.detangler;

public interface OnIntersectionChange {
    void onIntersectionChange(
        final int edgeIndex1,
        final int edgeIndex2,
        final boolean intersecting
    );
}
