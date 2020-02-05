package mbank.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import mbank.exceptions.CommunicationFailed;
import mbank.payload.request.FinalizeAuthorizationRequest;
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

    public Http() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(ACCEPT_ALL);
        JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);
        client = new OkHttpClient().newBuilder()
                .cookieJar(cookieJar)
                .build();
    }

    public <T> ParsedResponse<T> get(String path, Class<T> responseClass, Headers headers) {
        var request = prepare(path, headers);
        return send(request, responseClass);
    }

    public <T> ParsedResponse<T> get(String path, Class<T> responseClass) {
        return get(path, responseClass, null);
    }

    public <T> ParsedResponse<T> get(String path) {
        return get(path, null, null);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass, Object requestObject, Headers headers) {
        var requestBody = RequestBody.create(gson.toJson(requestObject), JSON_TYPE);
        var request = prepare("POST", path, requestBody, headers);
        return send(request, responseClass);
    }

    public <T> ParsedResponse<T> post(String path, Object requestObject, Headers headers) {
        return post(path, null, requestObject, headers);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass) {
        return post(path, responseClass, new Object(), null);
    }

    public <T> ParsedResponse<T> post(String path, Headers headers) {
        return post(path, null, new Object(), headers);
    }

    public <T> ParsedResponse<T> post(String path, Class<T> responseClass, Object requestObject) {
        return post(path, responseClass, requestObject, null);
    }

    private Request prepare(String method, String path, RequestBody requestBody, Headers headers) {
        var request = new Request.Builder()
                .url("https://online.mbank.pl" + path)
                .method(method, requestBody)
                .build();
        if(headers != null)
            request = request.newBuilder()
                    .headers(headers)
                    .build();
        return request;
    }

    private Request prepare(String path, Headers headers) {
        return prepare("GET", path, null, headers);
    }

    private <T> ParsedResponse<T> send(Request request, Class<T> responseClass) {
        try {
            var response = client.newCall(request).execute();
            return responseClass == null ? getRequestWithoutBody(response) : getRequestWithBody(response, responseClass);
        } catch (IOException e) {
            throw new CommunicationFailed(e);
        }
    }

    private <T> ParsedResponse<T> getRequestWithBody(Response response, Class<T> responseClass) {
        try {
            var responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), responseClass);
            return new ParsedResponse<>(response.code(), responseBody);
        } catch (JsonSyntaxException e) {
            throw new CommunicationFailed("Expected JSON response body");
        } catch (NullPointerException | IOException e) {
            throw new CommunicationFailed("Expected response body");
        }
    }

    private <T> ParsedResponse<T> getRequestWithoutBody(Response response) {
        return new ParsedResponse<>(response.code(), null);
    }

}
