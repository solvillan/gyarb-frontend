package se.doverfelt.pixturation.net.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.Pixturation;
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

    public void submitGuess(String guess) {
        HashMap<String, String> data = new HashMap<String, String>();
        JsonObject wrapper = new JsonObject();
        wrapper.add("guess", guess);
        Gdx.app.log("SubmitGuess", wrapper.toString());
        data.put("payload", wrapper.toString());
        HttpUtils.post("game/" + this.id + "/submit/guess", data, new Net.HttpResponseListener() {
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

    public boolean canMakeMove() {
        return (state == State.DRAW || state == State.GUESS) && !(state == State.DRAW && currentPlayer != Pixturation.getCurrentPlayer());
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
            out.currentPlayer = Player.createPlayer(game.get("current_player").asObject().getLong("id", -1));
            out.players.add(out.currentPlayer);
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

    public void start() {
        HttpUtils.SyncHTTPResponse response = HttpUtils.postSync("game/" + id + "/start", new HashMap<String, String>());
        active = true;
        JsonValue base = Json.parse(response.getBody());
        if (base.isObject()) {
            JsonObject game = base.asObject().get("game").asObject();
            word = game.getString("current_word", "NO_WORD");
            currentPlayer = Player.createPlayer(game.get("current_player").asObject());
        }
    }

    public void addPlayer(Player player) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("player_id", String.valueOf(player.getId()));
        HttpUtils.post("game/" + id + "/players/add", data, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Gdx.app.log("addPlayer", httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void update() {
        final Game game = this;
        HttpUtils.get("game/" + id + "/poll", new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String body = httpResponse.getResultAsString();
                if (httpResponse.getStatus().getStatusCode() == 200) {
                    JsonValue base = Json.parse(body);
                    if (base.isObject()) {
                        if (base.asObject().get("game").isObject()) {
                            JsonObject data = base.asObject().get("game").asObject();
                            game.word = data.getString("word", "NO_WORD");
                            game.state = Game.stateFromNum(data.getInt("status", -1));
                            JsonObject picture = data.get("current_picture").asObject();
                            try {
                                game.picture = ColorGrid.parseJson(CompressionUtils.fromGzBase64(picture.getString("data", "")));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (data.get("players").isArray()) {
                                JsonArray players = data.get("players").asArray();
                                game.players.clear();
                                for (JsonValue value : players) {
                                    game.players.add(Player.createPlayer(value.asObject().getInt("id", -1)));
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
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
            case 254:
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

    public Color[][] getPicture() {
        return picture;
    }

    public enum State {
        DRAW((byte)1), GUESS((byte) 2), WAIT((byte)254), WAIT_FOR_START((byte)0), INVALID((byte)255);

        byte num;
        State(byte num) {
            this.num = num;
        }
    }

}
