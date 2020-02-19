package model;

public class Response<T> {

    public final T body;

    public Response(T body) {
        this.body = body;
    }

}
