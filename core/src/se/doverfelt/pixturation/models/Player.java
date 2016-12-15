package se.doverfelt.pixturation.models;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.utils.HttpUtils;

/**
 * Created by Rickard on 2016-11-22.
 */
public class Player {

    private final String name;
    private final String email;
    private final int id;

    public Player(String name, String email, int id) {
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
        return obj instanceof Player && ((Player) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static Player createPlayer(long id) {
        HttpUtils.SyncHTTPResponse response = HttpUtils.getSync("user/" + id + "/info");
        JsonValue base = Json.parse(response.getBody());
        String name, email;
        int pid;
        if (base.isObject()) {
            name = base.asObject().getString("name", "NO_NAME");
            email = base.asObject().getString("email", "NO_NAME");
            pid = base.asObject().getInt("id", -1);
        } else {
            return null;
        }
        return new Player(name, email, pid);
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
}
