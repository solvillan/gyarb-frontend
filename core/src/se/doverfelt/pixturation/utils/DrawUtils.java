package se.doverfelt.pixturation.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Rickard on 2016-12-14.
 */
public class DrawUtils {

    public static void drawBoundingRect(float x, float y, float width, float height, float size, ShapeRenderer shape, Color color) {
        shape.begin();
        shape.setColor(color);
        shape.set(ShapeRenderer.ShapeType.Filled);
        shape.rect(x, y, width, size);
        shape.rect(x, y, size, height);
        shape.rect(x, y + height - size, width, size);
        shape.rect(x + width - size, y, size, height);
        shape.end();
    }

}
