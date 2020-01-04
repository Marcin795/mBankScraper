package mbank.payload.response;

import com.google.gson.annotations.SerializedName;

public class ScaDataResponse {

    @SerializedName("ScaAuthorizationId")
    private final String scaAuthorizationId;

    public ScaDataResponse(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

    public String getScaAuthorizationId() {
        return this.scaAuthorizationId;
    }

}
