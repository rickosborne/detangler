package org.rickosborne.detangler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntersectTest {
    private final IntPoint a = IntPoint.atXY(1, 1);
    private final IntPoint b = IntPoint.atXY(3, 3);
    private final IntPoint c = IntPoint.atXY(5, 5);
    private final IntPoint d = IntPoint.atXY(1, 5);
    private final IntPoint e = IntPoint.atXY(5, 1);
    private final IntPoint f = IntPoint.atXY(7, 7);

    @Test
    void atXYSameInstance() {
        assertSame(IntPoint.atXY(6, 7), IntPoint.atXY(6, 7));
    }

    @Test
    void pointsEndsMeet() {
        assertTrue(Intersect.points(a, c, a, d));
        assertTrue(Intersect.points(a, c, e, c));
    }

    @Test
    void pointsNo() {
        assertFalse(Intersect.points(a, b, c, d));
    }

    @Test
    void pointsOverlappingSegments() {
        assertTrue(Intersect.points(a, c, b, f));
        assertTrue(Intersect.points(b, f, a, c));
    }

    @Test
    void pointsSameSegment() {
        assertTrue(Intersect.points(a, c, a, c));
    }

    @Test
    void pointsT() {
        assertTrue(Intersect.points(a, c, b, d));
    }

    @Test
    void pointsVerticalParallel() {
        assertFalse(Intersect.points(a, d, c, e));
    }

    @Test
    void pointsX() {
        assertTrue(Intersect.points(a, c, d, e));
    }
}
