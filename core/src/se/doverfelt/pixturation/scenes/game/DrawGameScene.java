package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.scenes.components.ColorGrid;
import se.doverfelt.pixturation.scenes.components.ColorPicker;
import se.doverfelt.pixturation.utils.CompressionUtils;

import java.io.IOException;

/**
 * Created by Rickard on 2016-11-29.
 */
public class DrawGameScene extends AbstractScene {

    @LmlActor("drawGameWindow")
    private HorizontalGroup window;

    @LmlActor("controls")
    private Window controls;

    private ColorPicker colorPicker;
    private ColorGrid grid;
    private boolean added;
    private Pixturation pixturation;

    public ColorGrid getGrid() {
        return grid;
    }

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public DrawGameScene(Stage stage) {
        super(stage);
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/drawGame.xml");
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public String getViewId() {
        return "game";
    }

    @LmlAction("back")
    public void back(Actor actor) {
        pixturation.shouldSetScreen("menu");
    }
    @LmlAction("submit")
    public void submit(Actor actor) {
        if (pixturation.getCurrentGame() != null) {
            pixturation.getCurrentGame().submitPicture(grid);
        }
    }

    @Override
    public void update(float delta) {
        if (window != null && !added) {
            colorPicker = new ColorPicker(pixturation, pixturation.skin);
            colorPicker.addColorListener(new ColorPicker.ColorListener() {
                @Override
                public void changed(Color color) {
                    grid.setCurrentColor(color);
                }
            });
            grid = new ColorGrid(true, null);
            window.addActor(colorPicker);
            window.addActor(grid);
            controls.pad(14, 2, 2, 2);
            window.pad(10);
            window.pack();
            window.layout();
            window.setPosition(0, 0, Align.bottomRight);
            controls.layout();
            controls.pack();
            controls.setPosition(getStage().getWidth()/2f, getStage().getHeight()/2f, Align.center);
            //window.setFillParent(true);
            added = true;
            //getStage().setDebugAll(true);
        }
    }

    @LmlAction("initGameScene")
    public void initGameScene(Actor actor) {

    }
}
