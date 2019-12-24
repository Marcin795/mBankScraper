package mbank.payload.request;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginRequest {

    @SerializedName("UserName")
    String userName;

    @SerializedName("Password")
    String password;

}
