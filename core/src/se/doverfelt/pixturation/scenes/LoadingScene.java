package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by Rickard on 2016-11-16.
 */
public class LoadingScene extends AbstractScene {
    private SpriteBatch batch;
    private Texture img;
    private Pixturation pixturation;
    private BitmapFont font;
    private GlyphLayout layout;
    private int width, height;
    private float counter = 0;

    public LoadingScene(Stage stage) {
        super(stage);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Raleway.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.kerning = true;
        parameter.genMipMaps = true;
        parameter.minFilter = Texture.TextureFilter.MipMap;
        parameter.magFilter = Texture.TextureFilter.MipMap;
        parameter.size = 48;
        font = generator.generateFont(parameter);
        generator.dispose();
        layout = new GlyphLayout();
        height = Gdx.graphics.getHeight();
        width = Gdx.graphics.getWidth();
    }

    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        pixturation.getAssets().finishLoadingAsset("logo.png");
        img = pixturation.getAssets().get("logo.png");
    }

    @Override
    public void render(float delta) {
        super.render();
        layout.setText(font, "Pixturation");
        batch.begin();
        batch.draw(img, pixturation.viewport.getWorldWidth()/2-(layout.width+48)/2, 2*(pixturation.viewport.getWorldHeight()/3)-layout.height/2-(35));
        font.draw(batch, "Pixturation", pixturation.viewport.getWorldWidth()/2-(layout.width-48)/2, 2*(pixturation.viewport.getWorldHeight()/3)-layout.height/2);
        String p = (pixturation.getAssets().getProgress()*100) + "%";
        layout.setText(font, p);
        font.draw(batch, p, pixturation.viewport.getWorldWidth()/2 - layout.width/2, pixturation.viewport.getWorldHeight()/3-layout.height/2);
        batch.end();
    }

    @Override
    public void update(float delta) {
        if (pixturation.getAssets().update() && counter >= 2) {
            pixturation.setScreen("login");
        } else {
            counter += delta;
        }
    }

    @Override
    public void resize(int width, int height) {
        this.height = height;
        this.width = width;
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

    @Override
    public String getViewId() {
        return "loading";
    }
}
