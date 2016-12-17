package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.utils.DrawUtils;

import java.util.ArrayList;

/**
 * Created by rickard on 2016-12-15.
 */
public class TextField extends Component implements InputProcessor {

    private float width, height;
    private BitmapFont font;
    private GlyphLayout layout;
    private Pixturation pixturation;
    private ShapeRenderer shape;
    private float counter = 0;
    private boolean blink = true;
    private ArrayList<Character> chars = new ArrayList<Character>();
    private int curPos = 0;

    public TextField(float posX, float posY, float width, float height, AbstractScene parent, Pixturation pixturation) {
        super(posX, posY, parent);
        this.height = height;
        this.width = width;
        shape = new ShapeRenderer();
        this.pixturation = pixturation;
    }

    @Override
    public void act(float delta) {
        font = pixturation.getFont(24);
        if (layout == null) {
            layout = new GlyphLayout(font, "");
        }
        String text = "";
        for (char c : chars) {
            text += c;
        }
        layout.setText(font, text);
        counter += delta;
        if (counter >= 1) {
            blink = !blink;
            counter = 0;
        }
        Vector2 mousePos = getMousePos();
        if (mousePos.x > 0 && mousePos.x < getWidth() && mousePos.y > 0 && mousePos.y < getHeight()) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                Gdx.input.setInputProcessor(this);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shape.setAutoShapeType(true);
        DrawUtils.drawBoundingRect(getX(), getY(), getWidth(), getHeight(), 5, shape, Color.WHITE);
        shape.begin();
        shape.set(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1, 1, 1, 0.25f);
        shape.rect(getX(), getY(), getWidth(), getHeight());
        if (blink) {
            shape.setColor(Color.WHITE);
            shape.rect(getX() + 10 + layout.width, getY() + 7.5f, 2.5f, getHeight() - 15);
        }
        shape.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        batch.begin();
        font.draw(batch, layout, getX() + 10, getY() + getHeight() - layout.height/1.5f);
        batch.end();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE && curPos > 0) {
            chars.remove(curPos-1);
            curPos--;
            return true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character != 8) {
            curPos++;
            chars.add(character);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public String getText() {
        String out = "";
        for (char c : chars) {
            out += c;
        }
        return out;
    }
}
