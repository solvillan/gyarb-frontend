package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import se.doverfelt.pixturation.Pixturation;

import java.util.ArrayList;

/**
 * Created by rickard.doverfelt on 2016-11-29.
 */
public class ColorPicker extends Actor {

    private final float DIMENSION;
    private GlyphLayout layout;
    private BitmapFont font;
    private Color currentColor = Color.WHITE;
    private ShapeRenderer shapes;
    private Color[][] colors;
    private ArrayList<ColorListener> changeListeners = new ArrayList<ColorListener>();

    public ColorPicker(Pixturation pixturation) {

        if (pixturation.getAssets().isLoaded("Raleway.ttf")) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.kerning = true;
            parameter.genMipMaps = true;
            parameter.minFilter = Texture.TextureFilter.MipMap;
            parameter.magFilter = Texture.TextureFilter.MipMap;
            parameter.size = 48;
            font = pixturation.getAssets().get("Raleway.ttf", FreeTypeFontGenerator.class).generateFont(parameter);
            layout = new GlyphLayout(font, pixturation.getCurrentPlayer().getName());
        }

        shapes = new ShapeRenderer();
        DIMENSION = (Gdx.graphics.getHeight() - 32) / 32;
        int colorW = 8;
        int colorH = (int) Math.ceil((Colors.getColors().size / colorW));
        colors = new Color[colorW][colorH];
        Gdx.app.log("ColorPicker", "Colors: " + Colors.getColors().size + ", colorW: " + colorW + ", colorH: " + colorH);
        int x= 0, y = 0, xc = 0;
        for (Color color : Colors.getColors().values()) {
            Gdx.app.log("ColorPicker", "x: " + x + ", y: " + y);
            colors[x][y] = color;
            y++;
            if (y >= colorH) {
                y = 0;
                xc++;
                x++;
                if (x >= colors.length) break;
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 mousePos = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (mousePos.x > 0 && mousePos.x < getWidth() && mousePos.y > 0 && mousePos.y < getHeight()) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                setCurrentColor(mousePos.x, mousePos.y);
            }
        }
    }

    private void setCurrentColor(float xs, float ys) {
        int x = (int) Math.floor(xs / DIMENSION);
        int y = (int) Math.floor(ys / DIMENSION);
        x = Math.min(colors.length-1, x);
        y = Math.min(colors[0].length-1, y);
        x = Math.max(0, x);
        y = Math.max(0, y);
        currentColor = colors[x][y];
        colorChanged();
    }

    private void colorChanged() {
        for (ColorListener listener : changeListeners) {
            listener.changed(currentColor);
        }
    }

    public void addColorListener(ColorListener listener) {
        changeListeners.add(listener);
    }

    @Override
    public float getHeight() {
        return colors[0].length*DIMENSION + 33;
    }

    @Override
    public float getWidth() {
        return colors.length*DIMENSION + 1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        shapes.setAutoShapeType(true);
        shapes.begin();
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[x].length; y++) {
                if (colors[x][y] != null) {
                    shapes.setColor(colors[x][y]);
                    Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                    shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
                } else {
                    shapes.setColor(Color.WHITE);
                    Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                    shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
                }
            }
        }
        shapes.setColor(currentColor);
        Vector2 cur = localToStageCoordinates(new Vector2(0, DIMENSION*colors[0].length));
        shapes.rect(cur.x, cur.y, getWidth(), 32);
        shapes.set(ShapeRenderer.ShapeType.Line);
        layout.setText(font, currentColor.toString());
        font.draw(batch, currentColor.toString(), cur.x + (getWidth() / 2f) - (layout.width / 2f), cur.y + 16 - (layout.height / 2f));
        shapes.setColor(Color.LIGHT_GRAY);
        for (int x = 0; x < colors.length; x++) {
            for (int y = 0; y < colors[0].length; y++) {
                Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
                shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            }
        }
        shapes.end();
    }

    public interface ColorListener {
        void changed(Color color);
    }

}
