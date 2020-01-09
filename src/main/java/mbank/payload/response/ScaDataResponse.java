package mbank.payload.response;

import com.google.gson.annotations.SerializedName;

public class ScaDataResponse {

    @SerializedName("ScaAuthorizationId")
    public final String scaAuthorizationId;

    public ScaDataResponse(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
