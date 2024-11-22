package org.rickosborne.detangler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    public static List<IntPoint> randomForPoints (
        final int pointCount,
        final int width,
        final int height
    ) {
        final double aspect = (double) width / (double) height;
        // aspect = cols / rows       -> cols = rows * aspect
        // pointCount = cols * rows   -> cols = pointCount / rows
        // -> rows * aspect = pointCount / rows
        // -> rows * rows = pointCount / aspect
        final int rows = (int) Math.ceil(Math.sqrt(pointCount / aspect));
        final int cols = (int) Math.ceil(rows * aspect);
        final int rowHeight = (int) Math.round(((double) height) / ((double) rows));
        final int colWidth = (int) Math.round(((double) width) / ((double) cols));
        final int halfHeight = rowHeight / 2;
        final int halfWidth = colWidth / 2;
        final ArrayList<IntPoint> points = new ArrayList<>(rows * cols);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                points.add(IntPoint.atXY(col * colWidth + halfWidth + Rand.around(halfWidth / 2), row * rowHeight + halfHeight + Rand.around(halfHeight / 2)));
            }
        }
        Collections.shuffle(points);
        return points;
    }
}
