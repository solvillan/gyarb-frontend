package se.doverfelt.pixturation.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.StringBuilder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.scenes.GameScene;
import se.doverfelt.pixturation.scenes.components.ColorGrid;
import se.doverfelt.pixturation.utils.CompressionUtils;
import se.doverfelt.pixturation.utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by rickard on 2016-11-30.
 */
public class Game {

    private boolean active;
    private final long id;
    private State state;
    private Color[][] picture;
    private Player currentPlayer;

    private Game(long id, State state) {
        this.id = id;
        this.state = state;
    }

    public void submitPicture(ColorGrid picture) {
        HashMap<String, String> data = new HashMap<String, String>();
        JsonObject wrapper = new JsonObject();
        wrapper.add("word", "word");
        try {
            wrapper.add("data", CompressionUtils.toGzBase64(picture.toJson()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gdx.app.log("SubmitPicture", wrapper.toString());
        data.put("payload", wrapper.toString());
        HttpUtils.post("game/" + this.id + "/submit/picture", data, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public static Game createGame() throws HttpUtils.MalformedResponseException, HttpUtils.AccessForbiddenException, HttpUtils.WrongHTTPStatusException {
        HttpUtils.SyncHTTPResponse response = HttpUtils.postSync("game/create", new HashMap<String, String>());
        Gdx.app.log("CreateGame", response.getBody());
        if (response.getStatus() == HttpStatus.SC_CREATED) {
            JsonValue base = Json.parse(response.getBody());
            JsonObject game;
            if (base.isObject()) {
                JsonValue tmp = base.asObject().get("game");
                if (tmp.isObject()) {
                    game = tmp.asObject();
                } else {
                    throw new HttpUtils.MalformedResponseException();
                }
            } else {
                throw new HttpUtils.MalformedResponseException();
            }
            final Game out = new Game(game.getInt("id", Integer.MIN_VALUE), State.DRAW);
            out.currentPlayer = new Player(game.get("current_player").asObject().getString("name", "NO_NAME"), game.get("current_player").asObject().getString("email", "NO_EMAIL"));
            HttpUtils.post("game/" + out.id + "/start", new HashMap<String, String>(), new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    out.active = true;
                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void cancelled() {

                }
            });
            return out;
        } else if (response.getStatus() == HttpStatus.SC_FORBIDDEN) {
            throw new HttpUtils.AccessForbiddenException();
        } else {
            throw new HttpUtils.WrongHTTPStatusException();
        }
    }

    public static Game getGame(long id) {
        Net.HttpResponse response = HttpUtils.getSync("game/" + id + "/poll");
        return new Game(id, State.DRAW);
    }

    public enum State {
        DRAW, GUESS, WAIT, WAIT_FOR_START
    }

}
