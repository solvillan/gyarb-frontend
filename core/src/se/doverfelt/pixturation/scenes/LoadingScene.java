package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Rickard on 2016-11-16.
 */
public class LoadingScene implements Screen {
    private SpriteBatch batch;
    private Texture img;

    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(img, 10, 10);
        batch.end();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
