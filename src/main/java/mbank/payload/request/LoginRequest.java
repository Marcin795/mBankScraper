package mbank.payload.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("UserName")
    private final String userName;

    @SerializedName("Password")
    private final String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
