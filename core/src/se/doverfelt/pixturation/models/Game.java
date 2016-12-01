package se.doverfelt.pixturation.models;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.utils.HttpUtils;

import java.util.HashMap;

/**
 * Created by rickard on 2016-11-30.
 */
public class Game {

    private final long id;
    private State state;
    private Color[][] picture;
    private Player currentPlayer;

    private Game(long id, State state) {
        this.id = id;
        this.state = state;
    }

    public static Game createGame() throws HttpUtils.MalformedResponseException {
        Net.HttpResponse response = HttpUtils.postSync("game/create", new HashMap<String, String>());
        JsonValue base = Json.parse(response.getResultAsString());
        JsonObject game = null;
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
        Game out = new Game(game.getInt("id", Integer.MIN_VALUE), State.WAIT_FOR_START);
        out.currentPlayer = new Player(game.get("current_player").asObject().getString("name", "NO_NAME"), game.get("current_player").asObject().getString("email", "NO_EMAIL"));

        return out;
    }

    public static Game getGame(long id) {
        Net.HttpResponse response = HttpUtils.getSync("game/" + id + "/poll");
        return new Game(id, State.DRAW);
    }

    public enum State {
        DRAW, GUESS, WAIT, WAIT_FOR_START
    }

}
