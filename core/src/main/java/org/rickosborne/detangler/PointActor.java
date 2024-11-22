package org.rickosborne.detangler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import java.util.List;
import java.util.function.BiConsumer;

public class PointActor extends Actor {
    private final List<EdgeActor> edgeActors;
    private final ShapeRenderer shapeRenderer;
    public final int pointIndex;
    private boolean touched = false;
    private Theme theme;

    public PointActor(
        final IntPoint point,
        final int pointIndex,
        final int width,
        final ShapeRenderer shapeRenderer,
        final List<EdgeActor> edgeActors,
        final Theme theme,
        final BiConsumer<IntPoint, IntPoint> onMoved
    ) {
        this.shapeRenderer = shapeRenderer;
        this.edgeActors = edgeActors;
        this.pointIndex = pointIndex;
        this.theme = theme;
        setX(point.x);
        setY(point.y);
        //noinspection SuspiciousNameCombination
        setHeight(width);
        setWidth(width);
        setTouchable(Touchable.enabled);
        setVisible(true);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                touched = true;
                edgeActors.forEach((ea) -> ea.touched = true);
                return true;
            }

            @Override
            public void touchDragged(final InputEvent event, final float x, final float y, final int pointer) {
                final IntPoint before = IntPoint.atXY(getX(), getY());
                moveBy(x, y);
                final IntPoint after = IntPoint.atXY(getX(), getY());
                edgeActors.forEach((ea) -> ea.onMoved(before, after));
                onMoved.accept(before, after);
            }

            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                edgeActors.forEach((ea) -> ea.touched = false);
                touched = false;
            }
        });
    }

    public void setTheme(final Theme theme) {
        this.theme = theme;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.end();
        final float x = getX();
        final float y = getY();
        final float r = getHeight() / 2f;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(touched ? theme.pointTouched : theme.pointDefault);
        shapeRenderer.circle(x, y, r);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(x, y, r / 2);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        final int r = Math.round(getHeight() / 2);
        final boolean isHit = (x >= -r) && (x <= r) && (y >= -r) && (y <= r);
        if (isHit) {
            return this;
        }
        return null;
    }

    public void onMoved(final IntPoint before, final IntPoint after) {
        setX(after.x);
        setY(after.y);
        this.edgeActors.forEach((ea) -> ea.onMoved(before, after));
    }
}
