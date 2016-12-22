package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.scenes.components.*;

/**
 * Created by rickard.doverfelt on 2016-12-13.
 */
public class GuessGameScene extends AbstractScene {
    private Pixturation pixturation;
    private ColorPicker colorPicker;
    private ColorGrid grid;
    private Button back;
    private Button submit;
    private TextField textField;
    private Label title;
    private SpriteBatch batch;
    private boolean pictureSet;
    private long timestamp;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public GuessGameScene(Stage stage) {
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
        grid = new ColorGrid(false, null, 10 + colorPicker.getWidth(), 10, this);
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
                    pixturation.getCurrentGame().submitGuess(textField.getText(), (System.currentTimeMillis() - timestamp)/1000f);
                    pixturation.shouldSetScreen("menu");
                }
            }
        });
        textField = new TextField(submit.getX(), 60, space + 10, 40, this, pixturation);
        title = new Label(10, Gdx.graphics.getHeight() - 10, this, pixturation, "Guess the word", 32);
        batch = new SpriteBatch();
    }

    @Override
    public String getViewId() {
        return "guessGame";
    }

    @Override
    public void show() {
        super.show();
        pictureSet = false;
    }

    @Override
    public void update(float delta) {
        submit.act(delta);
        back.act(delta);
        title.act(delta);
        textField.act(delta);
        grid.act(delta);
        colorPicker.act(delta);
        if (!pictureSet && pixturation.getCurrentGame() != null) {
            if (pixturation.getCurrentGame().getPicture() != null) {
                grid.setPicture(pixturation.getCurrentGame().getPicture());
                timestamp = System.currentTimeMillis();
                pictureSet = true;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        submit.draw(batch);
        back.draw(batch);
        title.draw(batch);
        textField.draw(batch);
        grid.draw(batch);
        colorPicker.draw(batch);
    }
}
