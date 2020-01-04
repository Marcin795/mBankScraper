package mbank.payload.response;

public class SetupDataResponse {

    private final String antiForgeryToken;

    public SetupDataResponse(String antiForgeryToken) {
        this.antiForgeryToken = antiForgeryToken;
    }

    public String getAntiForgeryToken() {
        return this.antiForgeryToken;
    }

}
