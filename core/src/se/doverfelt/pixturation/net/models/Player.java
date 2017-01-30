package se.doverfelt.pixturation.net.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.utils.HttpUtils;

import java.util.HashMap;

/**
 * Created by Rickard on 2016-11-22.
 */
public class Player {

    private final String name;
    private final String email;
    private final int id;
    private static HashMap<Integer, Player> players = new HashMap<Integer, Player>();

    private Player(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) {
            return ((Player) obj).getId() == this.id;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Player createPlayer(int id) {
        if (players.containsKey(id)) {
            return players.get(id);
        } else {
            HttpUtils.SyncHTTPResponse response = HttpUtils.getSync("user/" + id + "/info");
            Gdx.app.log("Player.createPlayer", response.getBody());
            JsonValue base = Json.parse(response.getBody());
            String name, email;
            int pid;
            if (base.isObject()) {
                name = base.asObject().getString("name", "NO_NAME");
                email = base.asObject().getString("email", "NO_NAME");
                pid = base.asObject().getInt("id", -1);
                Gdx.app.log("Player.createPlayer", name + " : " + email + " : " + pid);
            } else {
                return null;
            }
            Player player = new Player(name, email, pid);
            players.put(pid, player);
            return player;
        }
    }

    public static Player createPlayer(JsonObject object) {
        if (players.containsKey(object.getInt("id", -1))) {
            return players.get(object.getInt("id", -1));
        } else {
            Player player = new Player(object.getString("name", "NO_NAME"), object.getString("email", "NO_EMAIL"), object.getInt("id", -1));
            players.put(player.getId(), player);
            return player;
        }
    }

    public Array<Game> getGames() {
        Array<Game> games = new Array<Game>();
        HttpUtils.SyncHTTPResponse response = HttpUtils.getSync("user/list/games");
        JsonValue base = Json.parse(response.getBody());
        if (base.isArray()) {
            for (JsonValue tmp : base.asArray()) {
                if (tmp.isObject()) {
                    games.add(Game.createGame(tmp.asObject()));
                }
            }
        }

        return games;
    }

    public Array<Player> getAllPlayers() {
        Array<Player> players = new Array<Player>();
        HttpUtils.SyncHTTPResponse response = HttpUtils.getSync("user/list");
        JsonValue base = Json.parse(response.getBody());
        if (base.isArray()) {
            for (JsonValue tmp : base.asArray()) {
                if (tmp.isObject()) {
                    players.add(Player.createPlayer(tmp.asObject()));
                }
            }
        }
        return players;
    }

}
