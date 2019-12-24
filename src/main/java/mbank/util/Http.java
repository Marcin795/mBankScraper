package mbank.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mbank.exceptions.LoginFailedException;
import mbank.payload.response.Response;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Objects;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Http {

    OkHttpClient client;
    Gson gson;
    String prefix;

    public <T> Response<T> get(String path, Class<T> responseClass, Headers headers) throws IOException {
        var request = prepare("GET", path, headers);
        return send(request, responseClass);
    }

    public <T> Response<T> get(String path, Class<T> responseClass) throws IOException {
        return get(path, responseClass, null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject, Headers headers) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(requestObject), MediaType.parse("application/json"));
        var request = prepare("POST", path, requestBody, headers);
        return send(request, responseClass);
    }

    public <T> Response<T> post(String path, Class<T> responseClass) throws IOException {
        return post(path, responseClass, new Object(), null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Object requestObject) throws IOException {
        return post(path, responseClass, requestObject, null);
    }

    public <T> Response<T> post(String path, Class<T> responseClass, Headers headers) throws IOException {
        return post(path, responseClass, new Object(), headers);
    }

    private Request prepare(String method, String path, RequestBody requestBody, Headers headers) {
        var request = new Request.Builder()
                .url(prefix + path)
                .method(method, requestBody)
                .build();
        if(headers != null) {
            request = request.newBuilder().headers(headers).build();
        }
        return request;
    }

    private Request prepare(String method, String path, Headers headers) {
        return prepare(method, path, null, headers);
    }

    private <T> Response<T> send(Request request, Class<T> responseClass) throws IOException {
        var response = client.newCall(request).execute();
        if(responseClass != null) {
            try {
                var responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), responseClass);
                return new Response<>(response.code(), responseBody);
            } catch (JsonSyntaxException e) {
                throw new LoginFailedException("Response deserialization failed");
            }
        } else {
            return new Response<>(response.code(), null);
        }
    }
}
