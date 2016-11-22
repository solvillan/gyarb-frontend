package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by rickard on 2016-11-17.
 */
public class MenuScene implements Screen {

    private Pixturation pixturation;
    private BitmapFont font;
    private GlyphLayout layout;
    private SpriteBatch batch;
    private Texture img;

    public MenuScene(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) System.exit(0);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        if (pixturation.getAssets().isLoaded("Raleway.ttf")) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.kerning = true;
            parameter.genMipMaps = true;
            parameter.minFilter = Texture.TextureFilter.MipMap;
            parameter.magFilter = Texture.TextureFilter.MipMap;
            parameter.size = 48;
            font = pixturation.getAssets().get("Raleway.ttf", FreeTypeFontGenerator.class).generateFont(parameter);
            layout = new GlyphLayout(font, "Pixturation");
        }
        if (pixturation.getAssets().isLoaded("logo.png")) {
            img = pixturation.getAssets().get("logo.png");
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(img, pixturation.viewport.getWorldWidth()/2-(layout.width+48)/2, 2*(pixturation.viewport.getWorldHeight()/3)-layout.height/2-(35));
        font.draw(batch, "Pixturation", pixturation.viewport.getWorldWidth()/2-(layout.width-48)/2, 2*(pixturation.viewport.getWorldHeight()/3)-layout.height/2);
        batch.end();
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
