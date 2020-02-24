package util;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.util.Objects;

import static java.net.CookiePolicy.ACCEPT_ALL;

public class Http {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json");
    private final OkHttpClient client;
    private final Gson gson = new Gson();
    private static final Headers EMPTY_HEADERS = new Headers.Builder().build();

    public Http() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        client = new OkHttpClient().newBuilder()
                .cookieJar(cookieJar)
                .build();
    }

    public int get(String url) {
        var request = prepare(url, EMPTY_HEADERS);
        return send(request).code();
    }

    public void post(String url, Headers headers) {
        post(url, new Object(), headers);
    }

    public void post(String url, Object requestObject) {
        post(url, requestObject, EMPTY_HEADERS);
    }

    public void post(String url, Object requestObject, Headers headers) {
        Request request = buildRequest(url, requestObject, headers);
        send(request);
    }

    public <T> T get(String url, Class<T> responseClass) {
        return get(url, responseClass, EMPTY_HEADERS);
    }

    public <T> T get(String url, Class<T> responseClass, Headers headers) {
        var request = prepare(url, headers);
        return sendAndGetResponse(request, responseClass);
    }

    public <T> T post(String url, Class<T> responseClass) {
        return post(url, responseClass, new Object(), EMPTY_HEADERS);
    }

    public <T> T post(String url, Class<T> responseClass, Object requestObject) {
        return post(url, responseClass, requestObject, EMPTY_HEADERS);
    }

    public <T> T post(String url, Class<T> responseClass, Object requestObject, Headers headers) {
        Request request = buildRequest(url, requestObject, headers);
        return sendAndGetResponse(request, responseClass);
    }

    private Request buildRequest(String url, Object requestObject, Headers headers) {
        var requestBody = RequestBody.create(gson.toJson(requestObject), JSON_TYPE);
        return prepare("POST", url, requestBody, headers);
    }

    private static Request prepare(String url, Headers headers) {
        return prepare("GET", url, null, headers);
    }

    private static Request prepare(String method, String url, RequestBody requestBody, Headers headers) {
        return new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .headers(headers)
                .build();
    }

    private <T> T sendAndGetResponse(Request request, Class<T> responseClass) {
        var response = send(request);
        return getRequestWithBody(response, responseClass);
    }

    private okhttp3.Response send(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new UncheckedIOException("Communication failed", e);
        }
    }

    private <T> T getRequestWithBody(okhttp3.Response response, Class<T> responseClass) {
        try {
            ResponseBody body = response.body();
            return gson.fromJson(Objects.requireNonNull(body).string(), responseClass);
        } catch (IOException e) {
            throw new UncheckedIOException("Body couldn't be read from okhttp3 response", e);
        }
    }

}
