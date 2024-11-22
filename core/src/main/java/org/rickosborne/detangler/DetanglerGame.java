package org.rickosborne.detangler;

public class DetanglerGame {
    private Tangle tangle;
    private int width;
    private int height;

    public DetanglerGame(
        final int pointCount,
        final int width,
        final int height,
        final OnIntersectionChange onIntersectionChange
    ) {
        this.height = height;
        this.width = width;
        tangle = new SplitFaceStrategy().buildTangle(pointCount, width, height, onIntersectionChange);
    }

    public Edge[] getEdges() {
        return tangle.edges;
    }

    public IntPoint[] getPoints() {
        return tangle.points;
    }

    public boolean isDetangled() {
        return tangle.isDetangled();
    }

    public void onPointMoved(final int pointIndex, final IntPoint before, final IntPoint after) {
        tangle.onPointMoved(pointIndex, before, after);
    }

    public void resize(final int width, final int height) {
        if (this.width == width && this.height == height) {
            return;
        }
        this.width = width;
        this.height = height;
        tangle = Tangle.scale(tangle, IntRect.fromXYWH(0, 0, width, height));
    }

    public int intersectionCountForEdge(final int edgeIndex) {
        return tangle.intersectionsForEdge(edgeIndex);
    }
}
