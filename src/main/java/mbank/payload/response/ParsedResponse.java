package mbank.payload.response;

public class ParsedResponse<T> {

    public final int status;
    public final T body;

    public ParsedResponse(int status, T body) {
        this.status = status;
        this.body = body;
    }

    public ParsedResponse(int status) {
        this.status = status;
        this.body = null;
    }



}
