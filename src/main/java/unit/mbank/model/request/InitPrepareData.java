package unit.mbank.model.request;

import com.google.gson.annotations.SerializedName;

class InitPrepareData {

    @SerializedName("ScaAuthorizationId")
    private final String scaAuthorizationId;

    InitPrepareData(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
