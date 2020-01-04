package mbank.payload.response;

public class Response<T> {

    private final int status;
    private final T body;

    public Response(int status, T body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return this.status;
    }

    public T getBody() {
        return this.body;
    }

}
