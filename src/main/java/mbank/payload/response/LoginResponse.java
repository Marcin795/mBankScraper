package mbank.payload.response;

public class LoginResponse {

    private final boolean successful;
    private final String tabId;

    public LoginResponse(boolean successful, String tabId) {
        this.successful = successful;
        this.tabId = tabId;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public String getTabId() {
        return this.tabId;
    }

}
