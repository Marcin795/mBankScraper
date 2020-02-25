package mbank.model.response;

public class LoginResponseBody {

    public final boolean successful;
    public final String errorMessageTitle;

    public LoginResponseBody(boolean successful, String errorMessageTitle) {
        this.successful = successful;
        this.errorMessageTitle = errorMessageTitle;
    }

}
