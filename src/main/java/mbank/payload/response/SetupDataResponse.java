package mbank.payload.response;

public class SetupDataResponse {

    public final String antiForgeryToken;

    public SetupDataResponse(String antiForgeryToken) {
        this.antiForgeryToken = antiForgeryToken;
    }

}
