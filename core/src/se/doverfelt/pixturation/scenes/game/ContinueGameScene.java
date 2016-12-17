package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.net.models.Game;
import se.doverfelt.pixturation.scenes.AbstractScene;

/**
 * Created by rickard.doverfelt on 2016-12-13.
 */
public class ContinueGameScene extends AbstractScene {

    @LmlActor("gamelist")
    private List<Game> gamelist;
    @LmlActor("gamePlayers")
    private List<Game> gamePlayers;
    @LmlActor("gameName")
    private Label gameName;
    @LmlActor("goBtn")
    private Button goBtn;
    @LmlActor("continueRoot")
    private Window root;
    private boolean added;
    private Pixturation pixturation;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public ContinueGameScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/continueGame.xml");
    }

    @Override
    public String getViewId() {
        return "continueGame";
    }

    @Override
    public void update(float delta) {
        if (!added && Pixturation.getCurrentPlayer() != null) {
            gamelist.setItems(Pixturation.getCurrentPlayer().getGames());
            gamelist.layout();
            gamelist.pack();
            root.layout();
            root.pack();
            added = true;
        }
        root.setPosition(getStage().getWidth()/2f, getStage().getHeight()/2f, Align.center);
    }

    @LmlAction("goToGame")
    public void goToGame(Actor actor) {
        if (!goBtn.isDisabled()) {
            pixturation.setCurrentGame(gamelist.getSelected());
            if (pixturation.getCurrentGame().getState() == Game.State.DRAW) {
                pixturation.shouldSetScreen("drawGame");
            } else {
                pixturation.shouldSetScreen("guessGame");
            }
        }
    }

    @LmlAction("previewGame")
    public void previewGame(Actor actor) {
        Game sel = gamelist.getSelected();
        gameName.setText(sel.toString() + " | State: " + sel.getState());
        gamePlayers.setItems(sel.getPlayers());
        goBtn.setDisabled(!sel.canMakeMove());
        root.layout();
    }

    @LmlAction("back")
    public void back(Actor actor) {
        pixturation.shouldSetScreen("menu");
    }
}
