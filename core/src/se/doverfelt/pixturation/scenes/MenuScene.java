package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.lml.annotation.LmlAction;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by rickard on 2016-11-17.
 */
public class MenuScene extends AbstractScene {

    private Pixturation pixturation;
    private BitmapFont font;
    private GlyphLayout layout;
    private SpriteBatch batch;
    private Texture img;
    private Stage newstage;

    public MenuScene(Stage stage) {
        super(stage);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) System.exit(0);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
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
        if (pixturation.getAssets().isLoaded("logo.png")) {
            img = pixturation.getAssets().get("logo.png");
        }
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/menu.xml");
    }

    @LmlAction("createGame")
    public void tmp(Actor actor) {
        Gdx.app.log("Menu", "Button pressed!");
    }

    @LmlAction("showProfile")
    public void showProfile(Button button) {
        pixturation.shouldSetScreen("profile");
    }

    @LmlAction("quit")
    public void quit(Actor actor) {
        System.exit(0);
    }

    @LmlAction("continueGame")
    public void continueGame(Actor actor) {
        pixturation.shouldSetScreen("game");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(img, pixturation.viewport.getWorldWidth()/2-(layout.width+48)/2, 3*(pixturation.viewport.getWorldHeight()/4)-layout.height/2-(35));
        font.draw(batch, pixturation.getCurrentPlayer().getName(), pixturation.viewport.getWorldWidth()/2-(layout.width-48)/2, 3*(pixturation.viewport.getWorldHeight()/4)-layout.height/2);
        batch.end();
        super.render(delta);
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
    public void dispose() {

    }

    @Override
    public String getViewId() {
        return "menu";
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }
}
