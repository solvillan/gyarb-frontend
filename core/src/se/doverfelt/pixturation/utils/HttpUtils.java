package se.doverfelt.pixturation.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.net.HttpRequestBuilder;

import java.util.HashMap;

/**
 * Created by Rickard on 2016-11-22.
 */
public class HttpUtils {

    private static HttpRequestBuilder builder = new HttpRequestBuilder();
    private static final String BASE_URL = "http://localhost/";
    private static String token;

    public static Net.HttpResponse getSync(String path) {
        final byte[] status = {0};
        final Net.HttpResponse[] response = new Net.HttpResponse[1];
        get(path, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                status[0] = 1;
                response[0] = httpResponse;
            }

            @Override
            public void failed(Throwable t) {
                status[0] = -1;
                Gdx.app.error("GetSync", t.getMessage(), t);
            }

            @Override
            public void cancelled() {
                status[0] = -2;
            }
        });
        while (status[0] == 0) {
            Thread.yield();
        }
        return response[0];
    }

    public static void get(String path, Net.HttpResponseListener listener) {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (token != null) {
            headers.put("Token", token);
        }
        get(path, listener, headers);
    }

    public static void get(String path, Net.HttpResponseListener listener, HashMap<String, String> headers) {
        HttpRequestBuilder request = builder.newRequest().method(Net.HttpMethods.GET).url(BASE_URL + path);
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

    public static Net.HttpResponse postSync(String path, HashMap<String, String> data) {
        final byte[] status = {0};
        final Net.HttpResponse[] response = new Net.HttpResponse[1];
        post(path, data, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                status[0] = 1;
                response[0] = httpResponse;
            }

            @Override
            public void failed(Throwable t) {
                status[0] = -1;
                Gdx.app.error("GetSync", t.getMessage(), t);
            }

            @Override
            public void cancelled() {
                status[0] = -2;
            }
        });
        while (status[0] == 0) {
            Thread.yield();
        }
        return response[0];
    }

    public static void post(String path, HashMap<String, String> data, Net.HttpResponseListener listener, HashMap<String, String> headers) {
        HttpRequestBuilder request = builder.newRequest().method(Net.HttpMethods.POST).url(BASE_URL + path);
        for (String k : headers.keySet()) {
            request = request.header(k, headers.get(k));
        }
        request.formEncodedContent(data);
        Gdx.net.sendHttpRequest(request.build(), listener);
    }

    public static class MalformedResponseException extends Exception {

    }

}
