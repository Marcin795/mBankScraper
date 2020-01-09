package mbank.payload.response;

public class Response<T> {

    public final int status;
    public final T body;

    public Response(int status, T body) {
        this.status = status;
        this.body = body;
    }

}
