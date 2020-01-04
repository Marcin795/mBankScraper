package mbank.payload.request;

import com.google.gson.annotations.SerializedName;

public class StatusRequest {

    @SerializedName("TranId")
    private final String tranId;

    public StatusRequest(String tranId) {
        this.tranId = tranId;
    }

}
