package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by rickard on 2016-11-24.
 */
public class ColorGrid extends Actor {

    private Color[][] tiles;
    private boolean editable;
    private ShapeRenderer shapes;
    private final float DIMENSION;
    private Texture img;

    public ColorGrid(final boolean editable, Color[][] tiles) {
        super();
        img = new Texture("logo.png");
        setBounds(0, 0, img.getWidth(), img.getHeight());
        DIMENSION = (Gdx.graphics.getHeight() - 32) / 32;
        this.editable = editable;
        if (tiles != null) {
            this.tiles = tiles;
        } else {
            this.tiles = new Color[32][32];
            for (int x = 0; x < 32; x++) {
                for (int y = 0; y < 32; y++) {
                    this.tiles[x][y] = Color.WHITE;
                }
            }
        }
        shapes = new ShapeRenderer();

        if (editable) {
            setTouchable(Touchable.enabled);
        } else {
            setTouchable(Touchable.disabled);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (editable) {
            Vector2 mousePos = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                changeTile(mousePos.x, mousePos.y, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1));
            } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                changeTile(mousePos.x, mousePos.y, Color.WHITE);
            }
        }
    }

    private void changeTile(float xs, float ys, Color color) {
        int x = (int) Math.floor(xs / DIMENSION);
        int y = (int) Math.floor(ys / DIMENSION);
        if (x >= 32) x = 31;
        if (y >= 32) y = 31;
        this.tiles[x][y] = color;
    }

    @Override
    public float getHeight() {
        return 32*DIMENSION + 1;
    }

    @Override
    public float getWidth() {
        return 32*DIMENSION + 1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapes.setAutoShapeType(true);
        shapes.begin();
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                shapes.setColor(tiles[x][y]);
                Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            }
        }
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            }
        }
        shapes.end();
    }
}
