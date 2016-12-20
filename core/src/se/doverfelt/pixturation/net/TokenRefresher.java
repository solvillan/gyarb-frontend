package se.doverfelt.pixturation.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import se.doverfelt.pixturation.utils.HttpUtils;

/**
 * Created by rickard on 2016-12-20.
 */
public class TokenRefresher implements Runnable {

    private boolean running;

    @Override
    public void run() {
        while (running) {
            HttpUtils.get("user/token/refresh", new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    String body = httpResponse.getResultAsString();
                    if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                        JsonValue base = Json.parse(body);
                        if (base.isObject()) {
                            HttpUtils.setToken(base.asObject().getString("token", ""));
                        }
                    } else {
                        //TODO player logged out
                    }
                }

                @Override
                public void failed(Throwable t) {

                }

                @Override
                public void cancelled() {

                }
            });
            try {
                Thread.sleep(1200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        running = true;
    }

    public void stop() {
        running = false;
    }
}
