package mbank.payload.response;

public class LoginResponse {

    public final boolean successful;
    public final String errorMessageTitle;

    public LoginResponse(boolean successful, String errorMessageTitle) {
        this.successful = successful;
        this.errorMessageTitle = errorMessageTitle;
    }

}
