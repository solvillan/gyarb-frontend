package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import se.doverfelt.pixturation.Pixturation;

/**
 * Created by rickard.doverfelt on 2016-11-22.
 */
public abstract class AbstractScene extends AbstractLmlView implements Screen {
    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public AbstractScene(Stage stage) {
        super(stage);
    }

    public abstract void create(Pixturation pixturation);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(getStage());
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/empty.lml");
    }
}
