package mbank.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import mbank.exceptions.CommunicationFailed;
import mbank.payload.response.Response;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Objects;


public class Http {

    private final OkHttpClient client;
    private final String prefix;
    private final Gson gson;

    public Http(OkHttpClient client, String prefix) {
        this.client = client;
        this.prefix = prefix;
        this.gson = new Gson();
    }

    public <T> Response<T> get(String path, Class<T> responseClass, Headers headers) {
        var request = prepare(path, headers);
        return send(request, responseClass);
    }

    public <T> Response<T> get(String path, Class<T> responseClass) {
        return get(path, responseClass, null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject, Headers headers) {
        RequestBody requestBody = RequestBody.create(gson.toJson(requestObject), MediaType.parse("application/json"));
        var request = prepare("POST", path, requestBody, headers);
        return send(request, responseClass);
    }

    public <T> Response<T> post(String path, Class<T> responseClass) {
        return post(path, responseClass, new Object(), null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject) {
        return post(path, responseClass, requestObject, null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Headers headers) {
        return post(path, responseClass, new Object(), headers);
    }

    private Request prepare(String method, String path, RequestBody requestBody, Headers headers) {
        var request = new Request.Builder()
                .url(prefix + path)
                .method(method, requestBody)
                .build();
        if(headers != null)
            request = request.newBuilder().headers(headers).build();
        return request;
    }

    private Request prepare(String path, Headers headers) {
        return prepare("GET", path, null, headers);
    }

    private <T> Response<T> send(Request request, Class<T> responseClass) {
        try {
            var response = client.newCall(request).execute();
            return responseClass == null ? getRequestWithoutBody(response) : getRequestWithBody(response, responseClass);
        } catch (IOException e) {
            throw new CommunicationFailed(e);
        }
    }

    private <T> Response<T> getRequestWithBody(okhttp3.Response response, Class<T> responseClass) {
        try {
            var responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), responseClass);
            return new Response<>(response.code(), responseBody);
        } catch (JsonSyntaxException e) {
            throw new CommunicationFailed("Expected JSON response body");
        } catch (NullPointerException | IOException e) {
            throw new CommunicationFailed("Expected response body");
        }
    }

    private <T> Response<T> getRequestWithoutBody(okhttp3.Response response) {
        return new Response<>(response.code(), null);
    }

}
