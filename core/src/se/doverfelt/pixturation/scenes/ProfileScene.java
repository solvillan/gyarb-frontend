package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.utils.HttpUtils;
import se.doverfelt.pixturation.scenes.components.ColorGrid;

/**
 * Created by rickard on 2016-11-23.
 */
public class ProfileScene extends AbstractScene {

    private Pixturation pixturation;

    @LmlActor("friends")
    private List<Label> friends;

    @LmlActor("players")
    private List<Label> playerList;

    @LmlActor("profileRoot")
    private Window window;

    private ColorGrid grid;
    private boolean added;

    /**
     * @param stage will be filled with actors when the view is passed to a LML parser. Should not be null.
     */
    public ProfileScene(Stage stage) {
        super(stage);
    }

    @Override
    public void create(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public void show() {
        super.show();
        HttpUtils.get("/user/list/friends", new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                Gdx.app.log("GetFriends", httpResponse.getStatus().getStatusCode() + ": " + response);
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    JsonValue data = Json.parse(response);
                    if (data.isArray()) {
                        Label[] players = new Label[data.asArray().size()];
                        int counter = 0;
                        while (data.asArray().iterator().hasNext()) {
                            JsonValue value = data.asArray().iterator().next();
                            if (value.isObject()) {
                                players[counter] = new Label(value.asObject().getString("name", "NO_NAME"), pixturation.skin);
                                counter++;
                            }
                        }
                        friends.setItems(players);
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("List Friends", t.getMessage(), t);
            }

            @Override
            public void cancelled() {
                Gdx.app.error("List Friends", "Cancelled!");
            }
        });
    }

    @LmlAction("getName")
    public String getName() {
        return pixturation.getCurrentPlayer().getName();
    }

    @LmlAction("getPlayers")
    public void getPlayers(TextField field) {
        HttpUtils.get("/user/list" + (!field.getText().isEmpty() ? "/filter/" + field.getText() : ""), new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                Gdx.app.log("GetPlayers", httpResponse.getStatus().getStatusCode() + ": " + response);
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    JsonValue data = Json.parse(response);
                    if (data.isArray()) {
                        Array<String> players = new Array<String>();
                        int counter = 0;
                        java.util.List<JsonValue> values = data.asArray().values();
                        for (JsonValue value : values) {
                            players.add(value.asObject().getString("name", "NO_NAME"));
                            Gdx.app.log("GetPlayers", value.asObject().getString("name", "NO_NAME"));
                        }
                        playerList.clearItems();
                        playerList.setItems(players);
                        Gdx.app.log("GetPlayers", playerList.getItems().toString());
                        playerList.pack();
                        playerList.layout();
                        window.pack();
                        window.layout();
                    }
                } else if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    playerList.clearItems();
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("List Friends", t.getMessage(), t);
            }

            @Override
            public void cancelled() {
                Gdx.app.error("List Friends", "Cancelled!");
            }
        });
    }

    @Override
    public FileHandle getTemplateFile() {
        return Gdx.files.internal("views/profile.xml");
    }

    @Override
    public String getViewId() {
        return "profile";
    }

    @Override
    public void update(float delta) {
        if (window != null && !added) {
            grid = new ColorGrid(true, ColorGrid.parseJson("[[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"ffffffff\",\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"],[\"0000ffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\",\"ffffffff\"]]"));
            window.add(grid);
            window.pack();
            window.layout();
            added = true;
        }
    }
}
