package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.annotation.LmlAction;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by rickard on 2016-11-23.
 */
public class ProfileScene extends AbstractScene {

    private Pixturation pixturation;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public ProfileScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @LmlAction("getName")
    public String getName() {
        return pixturation.getCurrentPlayer().getName();
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/profile.xml");
    }

    @Override
    public String getViewId() {
        return "profile";
    }

    @Override
    public void update(float delta) {

    }
}
