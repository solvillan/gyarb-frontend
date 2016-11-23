package se.doverfelt.pixturation.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.models.Player;

import java.util.HashMap;

/**
 * Created by Rickard on 2016-11-22.
 */
public class LoginHandler implements Net.HttpResponseListener {

    private final Pixturation pixturation;

    public LoginHandler(Pixturation pixturation) {
        this.pixturation = pixturation;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        String response = httpResponse.getResultAsString();
        Gdx.app.log("Auth", httpResponse.getStatus().getStatusCode() +  ": " + response);
        if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
            JsonValue data = Json.parse(response);
            if (data.isObject()) {
                HttpUtils.setToken(data.asObject().get("token").asString());
            }
            HashMap<String, String> param = new HashMap<String, String>();
            HttpUtils.post("user/check-token", param, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    String response = httpResponse.getResultAsString();
                    Gdx.app.log("Check", httpResponse.getStatus().getStatusCode() +  ": " + response);
                    if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                        JsonValue data = Json.parse(response);
                        if (data.isObject()) {
                            pixturation.setCurrentPlayer(new Player(data.asObject().getString("name", "NO_NAME"), data.asObject().getString("email", "NO_EMAIL")));
                            pixturation.shouldSetScreen("menu");
                        }
                    }
                }

                @Override
                public void failed(Throwable t) {
                    Gdx.app.error("Check", t.getMessage());
                }

                @Override
                public void cancelled() {
                    Gdx.app.error("Check", "Request cancelled!");
                }
            });
        }
    }

    @Override
    public void failed(Throwable t) {
        Gdx.app.error("Auth", t.getMessage());
    }

    @Override
    public void cancelled() {
        Gdx.app.error("Auth", "Request cancelled!");
    }
}
