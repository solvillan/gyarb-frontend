package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.scenes.components.Button;
import se.doverfelt.pixturation.scenes.components.ColorGrid;
import se.doverfelt.pixturation.scenes.components.ColorPicker;
import se.doverfelt.pixturation.utils.CompressionUtils;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Rickard on 2016-11-29.
 */
public class DrawGameScene extends AbstractScene {

    private ColorPicker colorPicker;
    private ColorGrid grid;
    private boolean added;
    private Pixturation pixturation;
    private Batch batch;
    private Button back;

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
    public void create(final Pixturation pixturation) {
        this.pixturation = pixturation;
        colorPicker = new ColorPicker(pixturation, this, 10, 10);
        colorPicker.addColorListener(new ColorPicker.ColorListener() {
            @Override
            public void changed(Color color) {
                grid.setCurrentColor(color);
            }
        });
        grid = new ColorGrid(true, null, 10 + colorPicker.getWidth(), 10, this);
        back = new Button(Gdx.graphics.getWidth() - 110, Gdx.graphics.getHeight() - 50, 100, 40, "Back", this, pixturation);
        back.setAction(new Button.Action() {
            @Override
            public void onClick() {
                pixturation.shouldSetScreen("menu");
            }
        });
        batch = new SpriteBatch();
    }

    @Override
    public String getViewId() {
        return "game";
    }


    public void submit(Actor actor) {
        if (pixturation.getCurrentGame() != null) {
            pixturation.getCurrentGame().submitPicture(grid);
        }
    }

    @Override
    public void update(float delta) {
        colorPicker.act(delta);
        grid.act(delta);
        back.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        colorPicker.draw(this.batch);
        grid.draw(this.batch);
        back.draw(this.batch);
    }
}
