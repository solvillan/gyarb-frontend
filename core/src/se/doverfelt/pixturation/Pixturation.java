package se.doverfelt.pixturation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import se.doverfelt.pixturation.models.Game;
import se.doverfelt.pixturation.models.Player;
import se.doverfelt.pixturation.scenes.*;
import se.doverfelt.pixturation.utils.CompressionUtils;

import java.io.IOException;
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
    private OrthographicCamera camera;

    private float rstart = 0.45f, gstart = 0, bstart= 0;
    private float r = 0.45f, g = 0, b= 0;
    private float rend = 0.008f, gend = 0, bend= 0.29f;
    private float fadetime = 10;
    private boolean fade = true;

    private Game currentGame;

    private Preferences preferences;

    @Override
	public void create () {
		initAssets();
        super.create();

        try {
            String comp = CompressionUtils.toGzBase64("Hello, and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel and please feel welcome!");
            Gdx.app.log("Compressed", comp);
            Gdx.app.log("Decompressed", CompressionUtils.fromGzBase64(comp));
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        preferences = Gdx.app.getPreferences("pixturation");

        addScreen("loading", new LoadingScene(new Stage(viewport)));
        addScreen("menu", new MenuScene(new Stage(viewport)));
        addScreen("login", new LoginScene(new Stage(viewport)));
        addScreen("profile", new ProfileScene(new Stage(viewport)));
        addScreen("game", new GameScene(new Stage(viewport)));

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

    private void fadeBg() {
        float rchange = (rend - rstart) / fadetime;
        float bchange = (bend - bstart) / fadetime;
        float gchange = (gend - gstart) / fadetime;



        if (r > rend && fade) {
            r += rchange * Gdx.graphics.getDeltaTime();
            g += gchange * Gdx.graphics.getDeltaTime();
            b += bchange * Gdx.graphics.getDeltaTime();
        } else {
            r -= rchange * Gdx.graphics.getDeltaTime();
            g -= gchange * Gdx.graphics.getDeltaTime();
            b -= bchange * Gdx.graphics.getDeltaTime();
        }

        if (r <= rend) {
            fade = false;
        } else if (r >= rstart) {
            fade = true;
        }

        Gdx.gl.glClearColor(r, g, b, 1);
    }

    @Override
	public void render () {
        if (nextScreen != null) {
            setScreen(nextScreen);
            nextScreen = null;
        }
		fadeBg();
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
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.clear();
        }
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

    public AbstractScene getScreen(String scene) {
        return screens.get(scene);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }
}