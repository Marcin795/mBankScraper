package mbank.model.payload.response;

public class SetupDataResponseBody {

    public final String antiForgeryToken;

    public SetupDataResponseBody(String antiForgeryToken) {
        this.antiForgeryToken = antiForgeryToken;
    }

}
