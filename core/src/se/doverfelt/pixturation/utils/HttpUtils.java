package se.doverfelt.pixturation.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Rickard on 2016-11-22.
 */
public class HttpUtils {

    private static HttpRequestBuilder builder = new HttpRequestBuilder();
    private static final String BASE_URL = "http://localhost/";
    private static String token;

    public static SyncHTTPResponse getSync(String path) {
        final int[] status = {0};
        final String[] responseBody = new String[1];
        final boolean[] done = {false};
        responseBody[0] = "";
        get(path, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                status[0] = httpResponse.getStatus().getStatusCode();
                responseBody[0] = httpResponse.getResultAsString();
                done[0] = true;
            }

            @Override
            public void failed(Throwable t) {
                status[0] = -1;
                Gdx.app.error("PostSync", t.getMessage(), t);
                done[0] = true;
            }

            @Override
            public void cancelled() {
                status[0] = -2;
                Gdx.app.error("PostSync", "Cancelled!");
                done[0] = true;
            }
        });
        while (!done[0]) {
            Thread.yield();
        }
        return new SyncHTTPResponse(responseBody[0], status[0]);
    }

    public static void get(String path, Net.HttpResponseListener listener) {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (token != null) {
            headers.put("Token", token);
        }
        get(path, listener, headers);
    }

    public static void get(String path, Net.HttpResponseListener listener, HashMap<String, String> headers) {
        HttpRequestBuilder request = builder.newRequest().method(Net.HttpMethods.GET).url(BASE_URL + path.trim());
        for (String k : headers.keySet()) {
            request = request.header(k, headers.get(k));
        }
        Gdx.net.sendHttpRequest(request.build(), listener);
    }

    public static void setToken(String newToken) {
        token = newToken;
        Gdx.app.log("Token", token);
    }

    public static String getToken() {
        return token;
    }

    public static void post(String path, HashMap<String, String> data, Net.HttpResponseListener listener) {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (token != null) {
            headers.put("Token", token);
        }
        post(path, data, listener, headers);
    }

    public static SyncHTTPResponse postSync(String path, HashMap<String, String> data) {
        final int[] status = {0};
        final String[] responseBody = new String[1];
        final boolean[] done = {false};
        responseBody[0] = "";
        post(path, data, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                status[0] = httpResponse.getStatus().getStatusCode();
                responseBody[0] = httpResponse.getResultAsString();
                done[0] = true;
            }

            @Override
            public void failed(Throwable t) {
                status[0] = -1;
                Gdx.app.error("PostSync", t.getMessage(), t);
                done[0] = true;
            }

            @Override
            public void cancelled() {
                status[0] = -2;
                Gdx.app.error("PostSync", "Cancelled!");
                done[0] = true;
            }
        });
        while (!done[0]) {
            Thread.yield();
        }
        return new SyncHTTPResponse(responseBody[0], status[0]);
    }

    public static void post(String path, HashMap<String, String> data, Net.HttpResponseListener listener, HashMap<String, String> headers) {
        HttpRequestBuilder request = builder.newRequest().method(Net.HttpMethods.POST).url(BASE_URL + path);
        for (String k : headers.keySet()) {
            request = request.header(k, headers.get(k));
        }
        request.formEncodedContent(data);
        Gdx.net.sendHttpRequest(request.build(), listener);
    }

    public static class SyncHTTPResponse {
        private String body;
        private int status;

        SyncHTTPResponse(String body, int status) {
            this.body = body;
            this.status = status;
        }

        public String getBody() {
            return body;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class HTTPException extends Exception {

    }

    public static class MalformedResponseException extends HTTPException {

    }
    public static class AccessForbiddenException extends HTTPException {

    }
    public static class WrongHTTPStatusException extends HTTPException {

    }

}
