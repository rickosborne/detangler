package org.rickosborne.detangler;

import com.badlogic.gdx.math.Rectangle;

public class Tangle {
    public final Edge[] edges;
    private final IntersectionTracker intersectionTracker;
    private final OnIntersectionChange onIntersectionChange;
    public final IntPoint[] points;

    public Tangle(
        final IntPoint[] points,
        final Edge[] edges,
        final OnIntersectionChange onIntersectionChange
    ) {
        this.points = points;
        this.edges = edges;
        final int edgeCount = edges.length;
        this.onIntersectionChange = onIntersectionChange;
        intersectionTracker = new IntersectionTracker(edgeCount);
    }

    public static Rectangle bounds(final Tangle tangle) {
        final IntPoint[] points = tangle.points;
        final IntPoint first = points[0];
        float minX = first.x;
        float maxX = first.x;
        float minY = first.y;
        float maxY = first.y;
        for (final IntPoint point : points) {
            final float x = point.x;
            final float y = point.y;
            if (x < minX) minX = x;
            else if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            else if (y > maxY) maxY = y;
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public boolean isDetangled() {
        return intersectionTracker.isDetangled();
    }

    public static Tangle scale(
        final Tangle tangle,
        final IntRect box
    ) {
        final Rectangle original = Tangle.bounds(tangle);
        final IntPoint[] points = tangle.points;
        final int count = points.length;
        final int originalWidth = Math.max(1, Math.round(original.width));
        final int originalHeight = Math.max(1, Math.round(original.height));
        final int originalX = Math.round(original.x);
        final int originalY = Math.round(original.y);
        final float ratioX = ((float) box.width) / ((float) originalWidth);
        final float ratioY = ((float) box.height) / ((float) originalHeight);
        final int newX = box.x;
        final int newY = box.y;
        final IntPoint[] newPoints = new IntPoint[count];
        for (int i = 0; i < count; i++) {
            final IntPoint point = points[i];
            newPoints[i] = IntPoint.atXY(
                ((point.x - originalX) * ratioX) + newX,
                ((point.y - originalY) * ratioY) + newY
            );
        }
        return new Tangle(newPoints, tangle.edges, tangle.onIntersectionChange);
    }

    public int intersectionsForEdge(final int edgeIndex) {
        return intersectionTracker.intersectionsForEdge(edgeIndex);
    }

    public void onPointMoved(final int pointIndex, final IntPoint before, final IntPoint after) {
        if (before.equals(after)) return;
        points[pointIndex] = after;
        intersectionTracker.recalculatePoint(pointIndex);
    }

    private class IntersectionTracker {
        private final int edgeCount;
        private int intersecting = 0;
        private final boolean[][] intersects;

        private IntersectionTracker(final int edgeCount) {
            this.edgeCount = edgeCount;
            intersects = new boolean[edgeCount][edgeCount];
            for (int i = 0; i < edgeCount; i++) {
                for (int j = i + 1; j < edgeCount; j++) {
                    setFor(i, j);
                }
            }
        }

        public void recalculatePoint(final int pointIndex) {
            for (int edgeIndex = 0; edgeIndex < edgeCount; edgeIndex++) {
                final Edge edge = edges[edgeIndex];
                if (edge.low == pointIndex || edge.high == pointIndex) {
                    for (int j = 0; j < edgeIndex; j++) {
                        setFor(j, edgeIndex);
                    }
                    for (int j = edgeIndex + 1; j < edgeCount; j++) {
                        setFor(edgeIndex, j);
                    }
                }
            }
        }

        private void setFor(final int lowIndex, final int highIndex) {
            final Edge a = edges[lowIndex];
            final Edge b = edges[highIndex];
            if (a.low == b.low || a.high == b.low || a.high == b.high || a.low == b.high) {
                return;
            }
            final boolean doesIntersect = Intersect.points(points[a.low], points[a.high], points[b.low], points[b.high]);
            setAt(lowIndex, highIndex, doesIntersect);
        }

        private void setAt(final int low, final int high, final boolean after) {
            final boolean before = intersects[low][high];
            if (before == after) return;
            intersecting += after ? 1 : -1;
            intersects[low][high] = after;
            onIntersectionChange.onIntersectionChange(low, high, after);
        }

        public int intersectionsForEdge(final int edgeIndex) {
            int count = 0;
            for (int i = 0; i < edgeIndex; i++) {
                if (intersects[i][edgeIndex]) {
                    count++;
                }
            }
            for (int i = edgeIndex + 1; i < edgeCount; i++) {
                if (intersects[edgeIndex][i]) {
                    count++;
                }
            }
            return count;
        }

        public boolean isDetangled() {
            return intersecting == 0;
        }
    }
}
