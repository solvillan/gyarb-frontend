package se.doverfelt.pixturation.scenes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.net.models.Game;
import se.doverfelt.pixturation.net.models.Player;
import se.doverfelt.pixturation.scenes.AbstractScene;
import se.doverfelt.pixturation.utils.HttpUtils;

/**
 * Created by Rickard on 2016-12-01.
 */
public class CreateGameScene extends AbstractScene {
    private Pixturation pixturation;

    @LmlActor("allPlayers")
    private List<Player> allPlayers;
    @LmlActor("gamePlayers")
    private List<Player> gamePlayers;
    private boolean added;
    @LmlActor("controls")
    private Window root;
    @LmlActor("addPlayerBtn")
    private Button addBtn;

    private Game createdGame;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public CreateGameScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/createGame.xml");
    }

    @LmlAction("createGame")
    public void createGame(Actor actor) {
        try {
            pixturation.setCurrentGame(Game.createGame());
            Pixturation.shouldSetScreen("menu");
        } catch (HttpUtils.HTTPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getViewId() {
        return "createGame";
    }

    @Override
    public void update(float delta) {
        if (!added && Pixturation.getCurrentPlayer() != null) {
            allPlayers.setItems(Pixturation.getCurrentPlayer().getAllPlayers());
            allPlayers.getItems().removeValue(Pixturation.getCurrentPlayer(), false);
            allPlayers.layout();
            allPlayers.pack();
            Array<Player> player = new Array<Player>();
            player.add(Pixturation.getCurrentPlayer());
            gamePlayers.setItems(player);
            root.layout();
            root.pack();
            added = true;
            try {
                createdGame = Game.createGame();
            } catch (HttpUtils.MalformedResponseException e) {
                e.printStackTrace();
            } catch (HttpUtils.AccessForbiddenException e) {
                e.printStackTrace();
            } catch (HttpUtils.WrongHTTPStatusException e) {
                e.printStackTrace();
            }
        }
        root.setPosition(getStage().getWidth()/2f, getStage().getHeight()/2f, Align.center);
    }

    @Override
    public void show() {
        super.show();
        added = false;
    }

    @LmlAction("playerSelected")
    public void playerSelected(Actor actor) {
        addBtn.setDisabled(gamePlayers.getItems().contains(allPlayers.getSelected(), false));
    }

    @LmlAction("addPlayer")
    public void addPlayer(Actor actor) {
        if (!gamePlayers.getItems().contains(allPlayers.getSelected(), false)) {
            gamePlayers.getItems().add(allPlayers.getSelected());
            createdGame.addPlayer(allPlayers.getSelected());
            addBtn.setDisabled(gamePlayers.getItems().contains(allPlayers.getSelected(), false));
        }
    }

    @LmlAction("goToGame")
    public void goToGame(Actor actor) {
        createdGame.start();
        pixturation.setCurrentGame(createdGame);
        if (pixturation.getCurrentGame().getState() == Game.State.DRAW && pixturation.getCurrentGame().canMakeMove()) {
            Pixturation.shouldSetScreen("drawGame");
        } else if (pixturation.getCurrentGame().getState() == Game.State.GUESS && pixturation.getCurrentGame().canMakeMove()) {
            Pixturation.shouldSetScreen("guessGame");
        } else {
            Pixturation.shouldSetScreen("continueGame");
        }
    }

    @LmlAction("back")
    public void back(Actor actor) {
        Pixturation.shouldSetScreen("menu");
        added = false;
    }
}
