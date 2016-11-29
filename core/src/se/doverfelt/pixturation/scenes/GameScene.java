package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.components.ColorGrid;
import se.doverfelt.pixturation.scenes.components.ColorPicker;

/**
 * Created by Rickard on 2016-11-29.
 */
public class GameScene extends AbstractScene {

    @LmlActor("gameWindow")
    private Window window;

    private ColorPicker colorPicker;
    private ColorGrid grid;
    private boolean added;
    private Pixturation pixturation;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public GameScene(Stage stage) {
        super(stage);
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/game.xml");
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public String getViewId() {
        return "game";
    }

    @Override
    public void update(float delta) {
        if (window != null && !added) {
            colorPicker = new ColorPicker(pixturation);
            colorPicker.addColorListener(new ColorPicker.ColorListener() {
                @Override
                public void changed(Color color) {
                    grid.setCurrentColor(color);
                }
            });
            grid = new ColorGrid(true, null);
            window.add(colorPicker);
            window.add(grid);
            window.pack();
            window.layout();
            added = true;
        }
    }

    @LmlAction("initGameScene")
    public void initGameScene(Actor actor) {

    }
}
