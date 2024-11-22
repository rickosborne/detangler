package org.rickosborne.detangler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SplitFaceStrategy implements IGraphStrategy {
    @Override
    public Tangle buildTangle(
        final int pointCount,
        final int width,
        final int height,
        final OnIntersectionChange onIntersectionChange
    ) {
        assert pointCount > 4;
        final IntPoint[] points = new IntPoint[pointCount];
        final List<IntPoint> starts = Grid.randomForPoints(pointCount, width, height);
        for (int i = 0; i < pointCount; i++) {
            points[i] = starts.get(i);
        }
        final Face all = Face.ofSize(pointCount);
        final List<Face> splittable = new LinkedList<>();
        final List<Face> keep = new LinkedList<>();
        splittable.add(all);
        final int edgeCount = (pointCount * 2) - 2;
        for (int i = 0; i < edgeCount && !splittable.isEmpty(); i++) {
            final int index = Rand.index(splittable.size());
            final Face one = splittable.remove(index);
            final Two<Face> two = one.randomSplit();
            if (two.left.canSplit()) {
                splittable.add(two.left);
            } else {
                keep.add(two.left);
            }
            if (two.right.canSplit()) {
                splittable.add(two.right);
            } else {
                keep.add(two.right);
            }
        }
        keep.addAll(splittable);
        final Set<Edge> edgeSet = new HashSet<>();
        for (final Face face : keep) {
            int previous = face.indices[face.size() - 1];
            for (int next : face.indices) {
                edgeSet.add(Edge.between(previous, next));
                previous = next;
            }
        }
        final Edge[] edges = edgeSet.stream()
            .sorted((a, b) -> a.low != b.low ? (a.low - b.low) : (a.high - b.high))
            .toArray(Edge[]::new);
        return new Tangle(points, edges, onIntersectionChange);
    }
}
