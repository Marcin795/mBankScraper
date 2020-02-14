package mbank.util;

import com.google.gson.Gson;
import mbank.exceptions.CommunicationFailed;
import mbank.payload.response.Response;
import mbank.payload.response.ResponseWithoutBody;
import okhttp3.*;

import java.io.IOException;
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

    public ResponseWithoutBody get(String path) {
        var request = prepare(path, EMPTY_HEADERS);
        return new ResponseWithoutBody(send(request).code());
    }

    public void post(String path, Headers headers) {
        post(path, new Object(), headers);
    }

    public void post(String path, Object requestObject) {
        post(path, requestObject, EMPTY_HEADERS);
    }

    public void post(String path, Object requestObject, Headers headers) {
        Request request = buildRequest(path, requestObject, headers);
        send(request);
    }

    public <T> Response<T> get(String path, Class<T> responseClass) {
        return get(path, responseClass, EMPTY_HEADERS);
    }

    public <T> Response<T> get(String path, Class<T> responseClass, Headers headers) {
        var request = prepare(path, headers);
        return sendAndGetResponse(request, responseClass);
    }

    public <T> Response<T> post(String path, Class<T> responseClass) {
        return post(path, responseClass, new Object(), EMPTY_HEADERS);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject) {
        return post(path, responseClass, requestObject, EMPTY_HEADERS);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject, Headers headers) {
        Request request = buildRequest(path, requestObject, headers);
        return sendAndGetResponse(request, responseClass);
    }

    private Request buildRequest(String path, Object requestObject, Headers headers) {
        var requestBody = RequestBody.create(gson.toJson(requestObject), JSON_TYPE);
        return prepare("POST", path, requestBody, headers);
    }

    private static Request prepare(String path, Headers headers) {
        return prepare("GET", path, null, headers);
    }

    private static Request prepare(String method, String path, RequestBody requestBody, Headers headers) {
        return new Request.Builder()
                .url("https://online.mbank.pl" + path)
                .method(method, requestBody)
                .headers(headers)
                .build();
    }

    private <T> Response<T> sendAndGetResponse(Request request, Class<T> responseClass) {
        var response = send(request);
        return getRequestWithBody(response, responseClass);
    }

    private okhttp3.Response send(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new CommunicationFailed(e);
        }
    }

    private <T> Response<T> getRequestWithBody(okhttp3.Response response, Class<T> responseClass) {
        try {
            ResponseBody body = response.body();
            var responseBody = gson.fromJson(Objects.requireNonNull(body).string(), responseClass);
            return new Response<>(responseBody);
        } catch (IOException e) {
            throw new CommunicationFailed("Expected response body");
        }
    }

}
