package se.doverfelt.pixturation.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlParserBuilder;
import com.github.czyzby.lml.util.LmlUtilities;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.models.Player;
import se.doverfelt.pixturation.net.HttpUtils;

import java.util.Arrays;

/**
 * Created by rickard on 2016-11-23.
 */
public class ProfileScene extends AbstractScene {

    private Pixturation pixturation;

    @LmlActor("friends")
    private List<Label> friends;

    @LmlActor("players")
    private List<Label> playerList;

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
                        for (int i = 0; i < values.size(); i++) {
                            players.add(values.get(i).asObject().getString("name", "NO_NAME"));
                            Gdx.app.log("GetPlayers", values.get(i).asObject().getString("name", "NO_NAME") + " | Count: " + counter);
                        }
                        playerList.setItems(players);
                        Gdx.app.log("GetPlayers", playerList.getItems().toString());
                        playerList.pack();
                        playerList.layout();
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

    }
}
