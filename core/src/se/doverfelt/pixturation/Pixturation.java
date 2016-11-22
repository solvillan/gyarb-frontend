package se.doverfelt.pixturation;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.util.LmlParserBuilder;
import se.doverfelt.pixturation.scenes.LoadingScene;
import se.doverfelt.pixturation.scenes.MenuScene;
import se.doverfelt.pixturation.scenes.Screen;

import java.util.HashMap;

public class Pixturation extends LmlApplicationListener {

    private HashMap<String, AbstractLmlView> screens = new HashMap<String, AbstractLmlView>();
    private AbstractLmlView currentScreen = null;
    private AssetManager assets;
    public FitViewport viewport;

	
	@Override
	public void create () {
		initAssets();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        screens.put("loading", new LoadingScene(this));
        screens.put("menu", new MenuScene(this));

        setScreen("loading");
	}

    @Override
    protected LmlParser createParser() {
        return Lml.parser().build();
    }

    private void initAssets() {
        assets = new AssetManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        assets.load("badlogic.jpg", Texture.class);
        assets.load("logo.png", Texture.class);
        assets.load("Raleway.ttf", FreeTypeFontGenerator.class);
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (currentScreen != null) {
            currentScreen.update(Gdx.graphics.getDeltaTime());
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
	}

	@Override
    public void resize(int w, int h) {
        super.resize(w, h);
        viewport.setWorldHeight(h);
        viewport.setWorldWidth(w);
        viewport.update(w, h);
    }
	
	@Override
	public void dispose () {
		super.dispose();
	}

    public void setScreen(String screen) {
        currentScreen = screens.get(screen);
    }

    public AssetManager getAssets() {
        return assets;
    }
}