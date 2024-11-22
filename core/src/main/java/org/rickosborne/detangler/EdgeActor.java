package org.rickosborne.detangler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class EdgeActor extends Actor {
    public IntPoint a;
    public IntPoint b;
    public final Edge edge;
    public final int edgeIndex;
    private int intersectionCount = 0;
    private final ShapeRenderer shapeRenderer;
    private final Theme theme;
    public boolean touched = false;

    public EdgeActor(
        final Edge edge,
        final IntPoint a,
        final IntPoint b,
        final int width,
        final int edgeIndex,
        final ShapeRenderer shapeRenderer,
        final Theme theme
    ) {
        this.edge = edge;
        this.a = a;
        this.b = b;
        this.edgeIndex = edgeIndex;
        this.shapeRenderer = shapeRenderer;
        this.theme = theme;
        setTouchable(Touchable.disabled);
        setVisible(true);
        setWidth(width);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.end();
        final Color color = touched ? theme.edgeTouched : intersectionCount > 0 ? theme.edgeIntersected : theme.edgeDefault;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rectLine(a.x, a.y, b.x, b.y, getWidth());
        shapeRenderer.end();
        batch.begin();
    }

    public void onMoved(final IntPoint before, final IntPoint after) {
        if (this.a.equals(before)) {
            this.a = after;
        } else {
            this.b = after;
        }
    }

    public void setIntersectionCount(final int intersectionCount) {
        this.intersectionCount = intersectionCount;
    }
}
