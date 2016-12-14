package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.utils.DrawUtils;

/**
 * Created by Rickard on 2016-12-14.
 */
public class Button extends Component {

    private float width, height;
    private ShapeRenderer shape;
    private String text;
    private BitmapFont font;
    private Pixturation pixturation;
    private GlyphLayout layout;
    private boolean hover = false;
    private Action action;

    public Button(float posX, float posY, float width, float height, String text, AbstractScene parent, Pixturation pixturation) {
        super(posX, posY, parent);
        this.width = width;
        this.height = height;
        shape = new ShapeRenderer();
        this.text = text;
        this.pixturation = pixturation;
    }

    @Override
    public void act(float delta) {
        font = pixturation.getFont(24);
        if (layout == null) {
            layout = new GlyphLayout(font, "");
        }
        Vector2 mousePos = getMousePos();
        if (mousePos.x > 0 && mousePos.x < getWidth() && mousePos.y > 0 && mousePos.y < getHeight()) {
            hover = true;
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                doAction();
            }
        } else {
            hover = false;
        }

    }

    private void doAction() {
        if (action != null) {
            action.onClick();
        }
    }

    @Override
    public void draw(Batch batch) {
        shape.setAutoShapeType(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.begin();
        shape.set(ShapeRenderer.ShapeType.Filled);
        if (hover) {
            shape.setColor(new Color(1, 1, 1, 0.25f));
            shape.rect(getX(), getY(), getWidth(), getHeight());
        }

        shape.end();
        DrawUtils.drawBoundingRect(getX(), getY(), width, height, 5, shape, Color.WHITE);
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (font != null) {
            batch.begin();
            layout.setText(font, text);
            font.draw(batch, text, getX() + (getWidth()/2f) - (layout.width/2f), getY() + getHeight() - layout.height/1.5f);
            batch.end();
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public interface Action {
        void onClick();
    }

}
