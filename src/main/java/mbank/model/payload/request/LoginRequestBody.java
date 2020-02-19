package mbank.model.payload.request;

import com.google.gson.annotations.SerializedName;
import model.Credentials;

public class LoginRequestBody {

    @SerializedName("UserName")
    private final String username;

    @SerializedName("Password")
    private final String password;

    public LoginRequestBody(Credentials credentials) {
        this.username = credentials.username;
        this.password = credentials.password;
    }

}
