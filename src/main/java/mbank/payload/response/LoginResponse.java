package mbank.payload.response;

public class LoginResponse {

    public final boolean successful;
    public final String tabId;

    public LoginResponse(boolean successful, String tabId) {
        this.successful = successful;
        this.tabId = tabId;
    }

}
