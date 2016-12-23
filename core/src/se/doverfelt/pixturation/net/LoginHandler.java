package se.doverfelt.pixturation.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.Pixturation;
import se.doverfelt.pixturation.net.models.Player;
import se.doverfelt.pixturation.scenes.LoginScene;
import se.doverfelt.pixturation.utils.HttpUtils;

import java.util.HashMap;

/**
 * Created by Rickard on 2016-11-22.
 */
public class LoginHandler implements Net.HttpResponseListener {

    private final Pixturation pixturation;
    private boolean tokenValid = false;
    private boolean tokenChecked = false;
    private TokenRefresher tokenRefresher = new TokenRefresher();
    private Thread tokenRefresherT = new Thread(tokenRefresher);

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
                pixturation.getPreferences().putString("token", data.asObject().get("token").asString());
                pixturation.getPreferences().flush();
            }
            authToken();
            tokenRefresherT.start();
        } else {
            JsonValue data = Json.parse(response);
            if (data.isObject()) {
                ((LoginScene) Pixturation.getScreen("login")).error(data.asObject().getString("error", "Error logging in!"));
            }
        }

    }

    public void authToken() {
        final HashMap<String, String> param = new HashMap<String, String>();
        HttpUtils.post("user/token/check", param, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                Gdx.app.log("Check", httpResponse.getStatus().getStatusCode() +  ": " + response);
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    JsonValue data = Json.parse(response);
                    if (data.isObject()) {
                        pixturation.setCurrentPlayer(new Player(data.asObject().getString("name", "NO_NAME"), data.asObject().getString("email", "NO_EMAIL"), data.asObject().getInt("id", -1)));
                        pixturation.getPreferences().putString("email", Pixturation.getCurrentPlayer().getEmail());
                        pixturation.getPreferences().flush();
                        Pixturation.shouldSetScreen("menu");
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

    public boolean checkOldToken(String token) {
        HashMap<String, String> param = new HashMap<String, String>();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Token", token);
        HttpUtils.post("user/token/check", param, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String response = httpResponse.getResultAsString();
                Gdx.app.log("Check", httpResponse.getStatus().getStatusCode() +  ": " + response);
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    tokenValid = true;
                    tokenRefresherT.start();
                }
                tokenChecked = true;
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("Check", t.getMessage());
                tokenChecked = true;
            }

            @Override
            public void cancelled() {
                Gdx.app.error("Check", "Request cancelled!");
                tokenChecked = true;
            }
        }, headers);
        while (!tokenChecked) {
            Thread.yield();
        }
        return tokenValid;
    }

    @Override
    public void failed(Throwable t) {
        Gdx.app.error("Auth", t.getMessage());
        ((LoginScene) Pixturation.getScreen("login")).error(t.getMessage());
    }

    @Override
    public void cancelled() {
        Gdx.app.error("Auth", "Request cancelled!");
        ((LoginScene) pixturation.getScreen("login")).error("Request cancelled!");
    }
}
