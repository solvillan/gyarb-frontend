package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.scenes.components.Button;
import se.doverfelt.pixturation.scenes.components.Label;

/**
 * Created by Rickard on 2016-12-22.
 */
public class ScoreScene extends AbstractScene {

    private ShapeRenderer shape;

    // Color values
    private float rCorrect1 = 0.34509805f, gCorrect1 = 0.5568627f, bCorrect1 = 0.08235293f;
    private float rCorrect2 = 0.12156863f, gCorrect2 = 0.5568629f, bCorrect2 = 0.08235295f;
    private float rWrong1 = 0.45f, gWrong1 = 0, bWrong1 = 0;
    private float rWrong2 = 0.61960787f, gWrong2 = 0.17254902f, bWrong2 = 0;

    // Fading background
    private float r = 0, g = 0, b = 0;
    private float fadetime = 10, time = 0;
    private boolean correct = false;
    private boolean fade = true;

    private Pixturation pixturation;

    private Label message, score, total;
    private Button cont;
    private Batch batch;


    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public ScoreScene(Stage stage) {
        super(stage);
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
        if (correct) {
            r = rCorrect1;
            g = gCorrect1;
            b = gCorrect1;
        } else {
            r = rWrong1;
            g = gWrong1;
            b = bWrong1;
        }
        time = 0;
        fade = true;
    }

    @Override
    public void create(final Pixturation pixturation) {
        this.pixturation = pixturation;
        shape = new ShapeRenderer();
        message = new Label(pixturation.viewport.getWorldWidth()/2, Gdx.graphics.getHeight() - pixturation.viewport.getWorldHeight()/3, this, pixturation, "", 48, Align.center);
        score = new Label(pixturation.viewport.getWorldWidth()/2, Gdx.graphics.getHeight() - pixturation.viewport.getWorldHeight()/3 - 75, this, pixturation, "", 36, Align.center);
        total = new Label(pixturation.viewport.getWorldWidth()/2, Gdx.graphics.getHeight() - pixturation.viewport.getWorldHeight()/3 - 150, this, pixturation, "", 24, Align.center);
        cont = new Button(Gdx.graphics.getWidth() - 210, 10, 200, 40, "Continue", this, pixturation);
        cont.setAction(new Button.Action() {
            @Override
            public void onClick() {
                Pixturation.shouldSetScreen("continueGame");
            }
        });
        batch = new SpriteBatch();
    }

    @Override
    public String getViewId() {
        return "score_scene";
    }

    @Override
    public void update(float delta) {
        float rchange, bchange, gchange;
        if (correct) {
            rchange = (rCorrect2 - rCorrect1) / fadetime;
            bchange = (bCorrect2 - bCorrect1) / fadetime;
            gchange = (gCorrect2 - gCorrect1) / fadetime;
        } else {
            rchange = (rWrong2 - rWrong1) / fadetime;
            bchange = (bWrong2 - bWrong1) / fadetime;
            gchange = (gWrong2 - gWrong1) / fadetime;
        }



        if (fade && correct) {
            r = MathUtils.clamp(r + rchange * delta, rCorrect2, rCorrect1);
            g = MathUtils.clamp(g + gchange * delta, gCorrect1, gCorrect2);
            b = MathUtils.clamp(b + bchange * delta, bCorrect1, bCorrect2);
        } else if (!fade && correct) {
            r = MathUtils.clamp(r - rchange * delta, rCorrect2, rCorrect1);
            g = MathUtils.clamp(g - gchange * delta, gCorrect1, gCorrect2);
            b = MathUtils.clamp(b - bchange * delta, bCorrect1, bCorrect2);
        } else if (fade) {
            r = MathUtils.clamp(r + rchange * delta, rWrong1, rWrong2);
            g = MathUtils.clamp(g + gchange * delta, gWrong1, gWrong2);
            b = MathUtils.clamp(b + bchange * delta, bWrong2, bWrong2);
        } else {
            r = MathUtils.clamp(r - rchange * delta, rWrong1, rWrong2);
            g = MathUtils.clamp(g - gchange * delta, gWrong1, gWrong2);
            b = MathUtils.clamp(b - bchange * delta, bWrong2, bWrong2);
        }

        time += delta;
        if (time >= fadetime) {
            time = 0;
            fade = !fade;
        }

        message.setText(correct ? "Correct!" : "Wrong!");
        score.setText("Score: " + pixturation.getCurrentGame().getScore());
        total.setText("Total Score: " + pixturation.getCurrentGame().getTotalScore());

        message.act(delta);
        score.act(delta);
        total.act(delta);
        cont.act(delta);
    }

    @Override
    public void render(float delta) {
        shape.setProjectionMatrix(getStage().getCamera().combined);
        shape.setAutoShapeType(true);
        shape.begin();
        shape.set(ShapeRenderer.ShapeType.Filled);
        shape.setColor(r, g, b, 1);
        shape.rect(0, 0, pixturation.viewport.getWorldWidth(), pixturation.viewport.getWorldHeight());
        shape.end();
        super.render(delta);
        message.draw(batch);
        score.draw(batch);
        total.draw(batch);
        cont.draw(batch);
    }
}
