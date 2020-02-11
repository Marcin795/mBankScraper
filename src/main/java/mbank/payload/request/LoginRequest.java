package mbank.payload.request;

import com.google.gson.annotations.SerializedName;
import mbank.model.Credentials;

public class LoginRequest {

    @SerializedName("UserName")
    private final String userName;

    @SerializedName("Password")
    private final String password;

    public LoginRequest(Credentials credentials) {
        this.userName = credentials.username;
        this.password = credentials.password;
    }

}
