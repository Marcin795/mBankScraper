package mbank.model;

import com.google.gson.annotations.SerializedName;

public class InitPrepareData {

    @SerializedName("ScaAuthorizationId")
    private final String scaAuthorizationId;

    public InitPrepareData(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
