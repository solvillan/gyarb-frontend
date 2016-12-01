package se.doverfelt.pixturation.models;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import se.doverfelt.pixturation.utils.HttpUtils;

/**
 * Created by rickard on 2016-11-30.
 */
public class Game {

    private final long id;
    private State state;
    private Color[][] picture;

    private Game(long id, State state) {
        this.id = id;
        this.state = state;
    }

    public static Game createGame() {

        return new Game(0, State.WAIT_FOR_START);
    }

    public static Game getGame(long id) {
        Net.HttpResponse response = HttpUtils.getSync("game/" + id + "/poll");
        return new Game(id, State.DRAW);
    }

    public enum State {
        DRAW, GUESS, WAIT, WAIT_FOR_START
    }

}
