package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import se.doverfelt.pixturation.Pixturation;

import java.util.ArrayList;

/**
 * Created by rickard.doverfelt on 2016-11-29.
 */
public class ColorPicker extends Actor {

    private final float DIMENSION;
    private final float DIMENSION_GRID;
    private GlyphLayout layout;
    private BitmapFontCache fontCache;
    private Color currentColor = Color.WHITE;
    private ShapeRenderer shapes;
    private Color[][] colors;
    private ArrayList<ColorListener> changeListeners = new ArrayList<ColorListener>();
    private Label.LabelStyle style;
    private Texture debug;
    private SpriteBatch batch = new SpriteBatch();

    public ColorPicker(Pixturation pixturation, Skin skin) {
        style = skin.get(Label.LabelStyle.class);
        /*if (pixturation.getAssets().isLoaded("Raleway.ttf")) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.kerning = true;
            parameter.genMipMaps = true;
            parameter.minFilter = Texture.TextureFilter.MipMap;
            parameter.magFilter = Texture.TextureFilter.MipMap;
            parameter.size = 48;
            font = pixturation.getAssets().get("Raleway.ttf", FreeTypeFontGenerator.class).generateFont(parameter);

        }*/
        layout = new GlyphLayout(style.font, "Test");
        fontCache = style.font.newFontCache();
        fontCache.setText(layout, getX(), getY());
        debug = pixturation.getAssets().get("badlogic.jpg");
        shapes = new ShapeRenderer();
        DIMENSION_GRID = (Gdx.graphics.getHeight() - 32) / 32;
        int colorW = 4;
        int colorH = (int) Math.ceil(Colors.getColors().size / (float)colorW);
        colors = new Color[colorW][colorH];
        Gdx.app.log("ColorPicker", "Colors: " + Colors.getColors().size + ", colorW: " + colorW + ", colorH: " + colorH);
        Array<Color> colorlist = Colors.getColors().values().toArray();
        int count = 0;
        for (int x = 0; x < colorW; x++) {
            for (int y = 0; y < colorH; y++) {
                Color c;
                try {
                    c = colorlist.get(count);
                } catch (IndexOutOfBoundsException e) {
                    c = Color.WHITE;
                }
                count++;
                colors[x][y] = c;
            }
        }
        Gdx.app.log("ColorPicker", "Actual Colors: " + Colors.getColors().size + " | Colors: " + colors[0].length*colors.length);
        DIMENSION = (2*(getHeight()/3f))/colors[0].length;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Vector2 mousePos = screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (mousePos.x > 0 && mousePos.x < getWidth() && mousePos.y > 0 && mousePos.y < 2*(getHeight()/3)) {
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
        return 32*DIMENSION_GRID + 1;
    }

    @Override
    public float getWidth() {
        return colors.length*DIMENSION + 1;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
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
        shapes.rect(cur.x, cur.y, getWidth(), getHeight()/3);

        layout.setText(fontCache.getFont(), currentColor.toString());
        cur = localToStageCoordinates(new Vector2(getWidth() / 2f - layout.width/2f, DIMENSION*colors[0].length + (getHeight()/3f) + layout.height/2f));
        this.batch.setProjectionMatrix(getStage().getCamera().combined);
        this.batch.begin();
        this.batch.setColor(Color.PINK);
        style.font.draw(this.batch, currentColor.toString(), cur.x, cur.y);
        //this.batch.draw(debug, cur.x, cur.y);
        this.batch.end();

        shapes.set(ShapeRenderer.ShapeType.Line);
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
