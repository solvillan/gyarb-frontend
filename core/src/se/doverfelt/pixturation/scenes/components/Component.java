package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import se.doverfelt.pixturation.scenes.AbstractScene;

/**
 * Created by Rickard on 2016-12-14.
 */
public abstract class Component {

    private float posX, posY;
    private AbstractScene parent;

    public Component(float posX, float posY, AbstractScene parent) {
        this.posX = posX;
        this.posY = posY;
        this.parent = parent;
    }

    public abstract void act(float delta);
    public abstract void draw(Batch batch);
    public abstract float getWidth();
    public abstract float getHeight();

    Vector2 localToScreenCoordinates(Vector2 local) {
        return local.add(posX, posY);
    }

    Vector2 screenToLocalCoordinates(Vector2 screen) {
        return screen.sub(posX, posY);
    }

    Vector2 getMousePos() {
        return screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
    }

    public AbstractScene getParent() {
        return parent;
    }

    public float getX() {
        return posX;
    }

    float getY() {
        return posY;
    }
}
