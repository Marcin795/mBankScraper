package mbank.util;

import com.google.gson.Gson;
import mbank.exceptions.CommunicationFailed;
import mbank.payload.response.ParsedResponse;
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

    public <T> ParsedResponse<T> get(String path) {
        return get(path, null, EMPTY_HEADERS);
    }

    public <T> ParsedResponse<T> get(String path, Class<T> responseClass) {
        return get(path, responseClass, EMPTY_HEADERS);
    }

    public <T> ParsedResponse<T> get(String path, Class<T> responseClass, Headers headers) {
        var request = prepare(path, headers);
        return send(request, responseClass);
    }

    public <T> ParsedResponse<T> post(String path, Headers headers) {
        return post(path, null, new Object(), headers);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass) {
        return post(path, responseClass, new Object(), EMPTY_HEADERS);
    }

    public <T> ParsedResponse<T> post(String path, Object requestObject) {
        return post(path, null, requestObject);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass, Object requestObject) {
        return post(path, responseClass, requestObject, EMPTY_HEADERS);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass, Object requestObject, Headers headers) {
        var requestBody = RequestBody.create(gson.toJson(requestObject), JSON_TYPE);
        var request = prepare("POST", path, requestBody, headers);
        return send(request, responseClass);
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

    private <T> ParsedResponse<T> send(Request request, Class<T> responseClass) {
        try {
            var response = client.newCall(request).execute();
            if (responseClass == null)
                return new ParsedResponse<>(response.code());
            return getRequestWithBody(response, responseClass);
        } catch (IOException e) {
            throw new CommunicationFailed(e);
        }
    }

    private <T> ParsedResponse<T> getRequestWithBody(Response response, Class<T> responseClass) {
        try {
            var responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), responseClass);
            return new ParsedResponse<>(response.code(), responseBody);
        } catch (IOException e) {
            throw new CommunicationFailed("Expected response body");
        }
    }
}
