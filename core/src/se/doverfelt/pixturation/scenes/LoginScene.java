package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;

/**
 * Created by rickard on 2016-11-17.
 */
public class LoginScene extends AbstractLmlView implements Screen {

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public LoginScene(Stage stage) {
        super(stage);
    }

    @Override
    public String getViewId() {
        return null;
    }

    @Override
    public void update(float delta) {

    }
}
