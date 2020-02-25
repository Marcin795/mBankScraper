package mbank.model.response;

import com.google.gson.annotations.SerializedName;

public class StatusResponseBody {

    @SerializedName("Status")
    public final String status;

    public StatusResponseBody(String status) {
        this.status = status;
    }

}