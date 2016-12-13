package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.annotation.LmlAction;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.models.Game;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.utils.HttpUtils;

/**
 * Created by Rickard on 2016-12-01.
 */
public class CreateGameScene extends AbstractScene {
    private Pixturation pixturation;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public CreateGameScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/createGame.xml");
    }

    @LmlAction("createGame")
    public void createGame(Actor actor) {
        try {
            pixturation.setCurrentGame(Game.createGame());
            pixturation.shouldSetScreen("menu");
        } catch (HttpUtils.HTTPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getViewId() {
        return "createGame";
    }

    @Override
    public void update(float delta) {

    }
}
