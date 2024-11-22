package org.rickosborne.detangler;

import java.util.Objects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FaceTest {

    @Test
    void split4() {
        final Face face4 = new Face(new int[]{0, 1, 2, 3});
        final Two<Face> split02 = face4.split(0, 2);
        assertEquals("0,1,2", split02.left.toString());
        assertEquals("0,2,3", split02.right.toString());
        final Two<Face> split13 = face4.split(1, 3);
        assertEquals("1,2,3", split13.left.toString());
        assertEquals("0,1,3", split13.right.toString());
    }

    @Test
    void split5() {
        final Face face5 = new Face(new int[]{0, 1, 2, 3, 4});
        final Two<Face> split02 = face5.split(0, 2);
        assertEquals("0,1,2", split02.left.toString());
        assertEquals("0,2,3,4", split02.right.toString());
        final Two<Face> split03 = face5.split(0, 3);
        assertEquals("0,1,2,3", split03.left.toString());
        assertEquals("0,3,4", split03.right.toString());
        final Two<Face> split13 = face5.split(1, 3);
        assertEquals("1,2,3", split13.left.toString());
        assertEquals("0,1,3,4", split13.right.toString());
        final Two<Face> split14 = face5.split(1, 4);
        assertEquals("1,2,3,4", split14.left.toString());
        assertEquals("0,1,4", split14.right.toString());
    }

    @Test
    void randomSplit4() {
        final Face face4 = new Face(new int[]{0, 1, 2, 3});
        final String a1 = "0,1,2";
        final String a2 = "0,2,3";
        final String b1 = "1,2,3";
        final String b2 = "0,1,3";
        for (int i = 0; i < 10_000; i++) {
            final Two<Face> two = face4.randomSplit();
            final boolean isA = Objects.equals(two.left.toString(), a1) && Objects.equals(two.right.toString(), a2);
            final boolean isB = Objects.equals(two.left.toString(), b1) && Objects.equals(two.right.toString(), b2);
            assertTrue(isA || isB);
        }
    }
}
