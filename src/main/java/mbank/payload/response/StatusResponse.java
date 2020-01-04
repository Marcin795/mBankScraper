package mbank.payload.response;

import com.google.gson.annotations.SerializedName;

public class StatusResponse {

    @SerializedName("Status")
    private final String status;

    public StatusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
