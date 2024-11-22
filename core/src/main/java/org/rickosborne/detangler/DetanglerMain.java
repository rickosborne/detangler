package org.rickosborne.detangler;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class DetanglerMain extends ApplicationAdapter {
    private Camera camera;
    private DetanglerGame game;
    private int pointCount = 10;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Viewport viewport;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        shapeRenderer = new ShapeRenderer();
        startGame();
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        Gdx.app.log(DetanglerMain.class.getSimpleName(), String.format(Locale.US, "resize: %dx%d -> %dx%d", viewport.getScreenWidth(), viewport.getScreenHeight(), width, height));
        game.resize(width, height);
        camera.update();
        viewport.update(width, height, true);
        Gdx.graphics.requestRendering();
    }

    private void startGame() {
        final EdgeActor[] edgeActors;
        final Holder<EdgeActor[]> edgeActorsHolder = new Holder<>();
        final Holder<PointActor[]> pointActorsHolder = new Holder<>();
        final Holder<DetanglerGame> gameHolder = new Holder<>();
        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();
        final Theme theme = Theme.random();
        final DetanglerGame game = new DetanglerGame(pointCount, width, height, (edgeIndex1, edgeIndex2, intersecting) -> {
            edgeActorsHolder.withValue((ea) -> {
                gameHolder.withValue((g) -> {
                    final EdgeActor edgeActor1 = ea[edgeIndex1];
                    edgeActor1.setIntersectionCount(g.intersectionCountForEdge(edgeIndex1));
                    final EdgeActor edgeActor2 = ea[edgeIndex2];
                    edgeActor2.setIntersectionCount(g.intersectionCountForEdge(edgeIndex2));
                });
            });
        });
        gameHolder.setValue(game);
        this.game = game;
        if (stage != null) {
            Gdx.input.setInputProcessor(null);
            stage.dispose();
        }
        stage = new Stage(viewport);
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                Gdx.graphics.setContinuousRendering(true);
                return true;
            }

            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                if (game.isDetangled()) {
                    pointCount++;
                    startGame();
                } else {
                    Gdx.graphics.setContinuousRendering(false);
                }
            }
        });
        Gdx.input.setInputProcessor(stage);
        Gdx.graphics.setContinuousRendering(false);

        final IntPoint[] points = game.getPoints();
        final Edge[] edges = game.getEdges();
        final int edgeWidth = Math.min(width, height) / 64;
        edgeActors = new EdgeActor[edges.length];
        final Map<Integer, List<EdgeActor>> edgeActorsByPointIndex = new HashMap<>();
        for (int i = 0; i < edges.length; i++) {
            final Edge edge = edges[i];
            final IntPoint a = points[edge.low];
            final IntPoint b = points[edge.high];
            final EdgeActor edgeActor = new EdgeActor(edge, a, b, edgeWidth, i, shapeRenderer, theme);
            edgeActor.setIntersectionCount(game.intersectionCountForEdge(i));
            stage.addActor(edgeActor);
            edgeActors[i] = edgeActor;
            edgeActorsByPointIndex.computeIfAbsent(edge.low, (n) -> new LinkedList<>()).add(edgeActor);
            edgeActorsByPointIndex.computeIfAbsent(edge.high, (n) -> new LinkedList<>()).add(edgeActor);
        }
        edgeActorsHolder.setValue(edgeActors);
        final int pointWidth = edgeWidth * 4;

        for (int i = 0; i < points.length; i++) {
            final IntPoint point = points[i];
            final int pointIndex = i;
            final List<EdgeActor> edgeActorsForPoint = edgeActorsByPointIndex.getOrDefault(i, Collections.emptyList());
            stage.addActor(new PointActor(point, i, pointWidth, shapeRenderer, edgeActorsForPoint, theme, (before, after) -> {
                game.onPointMoved(pointIndex, before, after);
            }));
        }
        Gdx.graphics.requestRendering();
    }
}
