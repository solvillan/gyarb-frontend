package se.doverfelt.pixturation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.Dtd;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.lml.util.LmlApplicationListener;
import se.doverfelt.pixturation.models.Player;
import se.doverfelt.pixturation.scenes.*;

import java.io.Writer;
import java.util.HashMap;

public class Pixturation extends LmlApplicationListener {

    private HashMap<String, AbstractScene> screens = new HashMap<String, AbstractScene>();
    private AbstractScene currentScreen = null;
    private AssetManager assets;
    public FitViewport viewport;
    public Skin skin;
    private String nextScreen;
    private Player currentPlayer;


    @Override
	public void create () {
		initAssets();
        super.create();

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        addScreen("loading", new LoadingScene(new Stage(viewport)));
        addScreen("menu", new MenuScene(new Stage(viewport)));
        addScreen("login", new LoginScene(new Stage(viewport)));
        addScreen("profile", new ProfileScene(new Stage(viewport)));

        setScreen("loading");
	}

	private void addScreen(String name, AbstractScene scene) {
	    screens.put(name, scene);
        scene.create(this);
    }

    @Override
    protected LmlParser createParser() {
        LmlParser parser = Lml.parser().skin(skin).build();
        parser.setStrict(false);
        // DTD schema will contain more tags and attributes.
        // If you have any custom tags or attributes, add them before generating!
        // Parse your templates with custom LML-defined macros - they will be added as well.

        Gdx.app.log("DTD", Gdx.files.getLocalStoragePath());
        try {
            Writer writer = Gdx.files.local("lml.dtd").writer(false);
            Dtd.saveSchema(parser, writer);
            writer.close();
        } catch (Exception exception) {
            throw new GdxRuntimeException(exception);
        }
        return parser;
    }

    private void initAssets() {
        skin = new Skin(Gdx.files.internal("neon-ui.json"));

        assets = new AssetManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        assets.load("badlogic.jpg", Texture.class);
        assets.load("logo.png", Texture.class);
        assets.load("Raleway.ttf", FreeTypeFontGenerator.class);
        //assets.load("neon-ui.atlas", TextureAtlas.class);
        //assets.load("neon-ui.json", Skin.class, new SkinLoader.SkinParameter("neon-ui.atlas"));
    }

    @Override
	public void render () {
        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        }
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*if (skin == null && assets.isLoaded("neon-ui.json")) {
            skin = assets.get("neon-ui.json", Skin.class);
        }*/
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
        if (currentScreen != null)currentScreen.hide();
        currentScreen = screens.get(screen);
        currentScreen.show();
        initiateView(currentScreen);
        setCurrentView(currentScreen);
    }

    public void shouldSetScreen(String screen) {
        nextScreen = screen;
    }

    public AssetManager getAssets() {
        return assets;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}