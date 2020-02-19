package mbank.model.payload.response;

import com.google.gson.annotations.SerializedName;

public class ScaDataResponseBody {

    @SerializedName("ScaAuthorizationId")
    public final String scaAuthorizationId;

    public ScaDataResponseBody(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
