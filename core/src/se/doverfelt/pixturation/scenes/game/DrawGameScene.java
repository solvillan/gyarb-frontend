package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.scenes.components.*;

/**
 * Created by Rickard on 2016-11-29.
 */
public class DrawGameScene extends AbstractScene {

    private ColorPicker colorPicker;
    private ColorGrid grid;
    private Batch batch;
    private Button back, submit;
    private Label word, title;
    private Pixturation pixturation;

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
        word = new Label(20 + colorPicker.getWidth() + grid.getWidth(), Gdx.graphics.getHeight() - 40, this, pixturation, (pixturation.getCurrentGame() != null ? pixturation.getCurrentGame().getWord() : "No Game Active"), 32);
        float space = Gdx.graphics.getWidth() - colorPicker.getWidth() - grid.getWidth() - 40;
        back = new Button(Gdx.graphics.getWidth() - space/3f - 10, 10, space/3f, 40, "Back", this, pixturation);
        back.setAction(new Button.Action() {
            @Override
            public void onClick() {
                pixturation.shouldSetScreen("menu");
            }
        });
        submit = new Button(back.getX() - 2*(space/3f) - 10, 10, 2*(space/3f), 40, "Submit", this, pixturation);
        submit.setAction(new Button.Action() {
            @Override
            public void onClick() {
                if (pixturation.getCurrentGame() != null) {
                    pixturation.getCurrentGame().submitPicture(grid);
                    pixturation.shouldSetScreen("menu");
                }
            }
        });
        title = new Label(10, Gdx.graphics.getHeight() - 10, this, pixturation, "Draw the word", 32);
        batch = new SpriteBatch();
    }

    @Override
    public String getViewId() {
        return "drawGame";
    }

    @Override
    public void update(float delta) {
        colorPicker.act(delta);
        grid.act(delta);
        back.act(delta);
        submit.act(delta);
        word.act(delta);
        title.act(delta);
        word.setText((pixturation.getCurrentGame() != null ? pixturation.getCurrentGame().getWord() : "No Game Active"));
        if (pixturation.getCurrentGame() != null) {
            if (!pixturation.getCurrentGame().getCurrentPlayer().equals(Pixturation.getCurrentPlayer())) {
                word.setText("Not Current Player!");
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        colorPicker.draw(this.batch);
        grid.draw(this.batch);
        back.draw(this.batch);
        submit.draw(this.batch);
        word.draw(this.batch);
        title.draw(this.batch);
    }
}
