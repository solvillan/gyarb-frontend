package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by rickard on 2016-11-17.
 */
public class LoginScene extends AbstractScene {

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public LoginScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        getStage().addActor(new Label("Test", pixturation.skin));
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/login.lml");
    }

    @Override
    public String getViewId() {
        return "login";
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

}
