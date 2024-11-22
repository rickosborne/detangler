package org.rickosborne.detangler;

public final class Intersect {
    public static boolean points(final IntPoint a, final IntPoint b, final IntPoint c, final IntPoint d) {
        if (a.equals(c) || a.equals(d) || b.equals(c) || b.equals(d)) {
            return true;
        }
        final Orientation o1 = orientation(a, b, c);
        final Orientation o2 = orientation(a, b, d);
        final Orientation o3 = orientation(c, d, a);
        final Orientation o4 = orientation(c, d, b);
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        if (o1 == Orientation.Collinear && onSegment(a, c, b)) {
            return true;
        }
        if (o2 == Orientation.Collinear && onSegment(a, d, b)) {
            return true;
        }
        if (o3 == Orientation.Collinear && onSegment(c, a, d)) {
            return true;
        }
        //noinspection RedundantIfStatement
        if (o4 == Orientation.Collinear && onSegment(c, b, d)) {
            return true;
        }
        return false;
    }

    public static boolean onSegment(final IntPoint p, final IntPoint q, final IntPoint r) {
        return (q.x <= Math.max(p.x, r.x))
            && (q.x >= Math.min(p.x, r.x))
            && (q.y <= Math.max(p.y, r.y))
            && (q.y >= Math.min(p.y, r.y));
    }

    public static Orientation orientation(final IntPoint p, final IntPoint q, final IntPoint r) {
        final int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return Orientation.Collinear;
        } else if (val > 0) {
            return Orientation.Clockwise;
        } else {
            return Orientation.Counterclockwise;
        }
    }

    public enum Orientation {
        Collinear,
        Clockwise,
        Counterclockwise,
    }
}
