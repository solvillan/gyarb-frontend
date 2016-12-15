package se.doverfelt.pixturation.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
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
    private Array<Player> players;
    private String word;

    private Game(long id, State state) {
        this.id = id;
        this.state = state;
        players = new Array<Player>();
    }

    public void submitPicture(ColorGrid picture) {
        HashMap<String, String> data = new HashMap<String, String>();
        JsonObject wrapper = new JsonObject();
        wrapper.add("word", word);
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
                String response = httpResponse.getResultAsString();
                Gdx.app.log(httpResponse.getStatus().toString() + ":", response);
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
            out.currentPlayer = new Player(game.get("current_player").asObject().getString("name", "NO_NAME"), game.get("current_player").asObject().getString("email", "NO_EMAIL"), game.get("current_player").asObject().getInt("id", -1));
            out.players.add(out.currentPlayer);
            HttpUtils.post("game/" + out.id + "/start", new HashMap<String, String>(), new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    out.active = true;
                    String result = httpResponse.getResultAsString();
                    JsonValue base = Json.parse(result);
                    if (base.isObject()) {
                        JsonObject game = base.asObject().get("game").asObject();
                        out.word = game.getString("current_word", "NO_WORD");
                    }
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

    public static Game createGame(JsonObject object) {
        Game game = new Game(object.getLong("id", -1), stateFromNum(object.getInt("status", -1)));
        game.word = object.getString("current_word", "NO_WORD");
        game.currentPlayer = Player.createPlayer(object.getLong("current_player", -1));
        game.players.add(game.currentPlayer);
        return game;
    }

    public static Game getGame(long id) {
        HttpUtils.SyncHTTPResponse response = HttpUtils.getSync("game/" + id + "/poll");
        return new Game(id, State.DRAW);
    }

    public String getWord() {
        return word;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static State stateFromNum(int num) {
        switch (num) {
            case 1:
                return State.DRAW;
            case 2:
                return State.GUESS;
            case 255:
                return State.WAIT;
            case 0:
                return State.WAIT_FOR_START;
            default:
                return State.INVALID;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Game ");
        builder.append(id);
        builder.append(" - ");
        builder.append(players.toString(", "));
        return builder.toString();
    }

    public Array<Player> getPlayers() {
        return players;
    }

    public State getState() {
        return state;
    }

    public enum State {
        DRAW((byte)1), GUESS((byte) 2), WAIT((byte)254), WAIT_FOR_START((byte)0), INVALID((byte)255);

        byte num;
        State(byte num) {
            this.num = num;
        }
    }

}
