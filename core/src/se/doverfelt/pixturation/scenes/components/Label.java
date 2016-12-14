package se.doverfelt.pixturation.scenes.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.AbstractScene;

/**
 * Created by Rickard on 2016-12-14.
 */
public class Label extends Component {

    private String text;
    private BitmapFont font;
    private ShapeRenderer shape;
    private Pixturation pixturation;
    private int size;
    private GlyphLayout layout;

    public Label(float posX, float posY, AbstractScene parent, Pixturation pixturation, String text, int size) {
        super(posX, posY, parent);
        shape = new ShapeRenderer();
        this.pixturation = pixturation;
        this.size = size;
        this.text = text;
    }

    @Override
    public void act(float delta) {
        font = pixturation.getFont(size);
        if (layout == null) {
            layout = new GlyphLayout(font, "");
        } else {
            layout.setText(font, text);
        }
    }

    @Override
    public void draw(Batch batch) {
        if (font != null) {
            batch.begin();
            font.draw(batch, layout, getX(), getY());
            batch.end();
        }
    }

    @Override
    public float getWidth() {
        return layout.width;
    }

    @Override
    public float getHeight() {
        return layout.height;
    }
}
