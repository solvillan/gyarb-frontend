package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.scenes.AbstractScene;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by rickard on 2016-11-24.
 */
public class ColorGrid extends Component {

    private Color[][] tiles;
    private boolean editable;
    private ShapeRenderer shapes;
    private final float DIMENSION;
    private Color currentColor = Color.WHITE;

    public ColorGrid(final boolean editable, Color[][] tiles, float posX, float posY, AbstractScene parent) {
        super(posX, posY, parent);
        DIMENSION = (Gdx.graphics.getHeight() - 32) / 32;
        this.editable = editable;
        if (tiles != null) {
            this.tiles = tiles;
        } else {
            clear();
        }
        shapes = new ShapeRenderer();
    }

    @Override
    public void act(float delta) {
        if (editable) {
            Vector2 mousePos = getMousePos();
            if (mousePos.x > 0 && mousePos.x < getWidth() && mousePos.y > 0 && mousePos.y < getHeight()) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    changeTile(mousePos.x, mousePos.y, currentColor);
                } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    changeTile(mousePos.x, mousePos.y, Color.WHITE);
                }
            }
        }
    }

    private void changeTile(float xs, float ys, Color color) {
        int x = (int) Math.floor(xs / DIMENSION);
        int y = (int) Math.floor(ys / DIMENSION);
        if (x >= 32) x = 31;
        if (y >= 32) y = 31;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
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
    public void draw(Batch batch) {
        shapes.setAutoShapeType(true);
        shapes.begin();
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                shapes.setColor(tiles[x][y]);
                Vector2 pos = localToScreenCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            }
        }
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Vector2 pos = localToScreenCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            }
        }
        shapes.end();
    }

    public String toJson() {
        JsonArray array = new JsonArray();
        for (int x = 0; x < tiles.length; x++) {
            JsonArray yvalues = new JsonArray();
            for (int y = 0; y < tiles[x].length; y++) {
                yvalues.add(tiles[x][y].toString());
            }
            array.add(yvalues);
        }
        try {
            array.writeTo(new PrintWriter(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array.toString();
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public static Color[][] parseJson(String json) {
        Color[][] colors = new Color[32][32];
        JsonValue value = Json.parse(json);

        if (value.isArray()) {
            JsonArray xvalues = value.asArray();
            for (int x = 0; x < 32; x++) {
                JsonArray yvalues = xvalues.get(x).asArray();
                for (int y = 0; y < 32; y++) {
                    colors[x][y] = Color.valueOf(yvalues.get(y).asString());
                }
            }
        }

        return colors;
    }

    public void setPicture(Color[][] picture) {
        this.tiles = picture;
    }

    public void clear() {
        this.tiles = new Color[32][32];
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                this.tiles[x][y] = Color.WHITE;
            }
        }
    }
}
