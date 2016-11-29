package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by rickard.doverfelt on 2016-11-29.
 */
public class ColorPicker extends Actor {

    private final float DIMENSION;
    private Color currentColor = Color.WHITE;
    private ShapeRenderer shapes;

    public ColorPicker() {
        shapes = new ShapeRenderer();
        DIMENSION = (Gdx.graphics.getHeight() - 32) / 32;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int x= 0, y = 0;
        shapes.setAutoShapeType(true);
        shapes.begin();
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.set(ShapeRenderer.ShapeType.Filled);
        for (Color color : Colors.getColors().values()) {
            shapes.setColor(color);
            Vector2 pos = localToStageCoordinates(new Vector2(DIMENSION*x, DIMENSION*y));
            shapes.rect(pos.x, pos.y, DIMENSION, DIMENSION);
            x++;
            if (x > 8) {
                x = 0;
                y++;
            }
        }
        shapes.set(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.LIGHT_GRAY);
        shapes.end();
    }
}
