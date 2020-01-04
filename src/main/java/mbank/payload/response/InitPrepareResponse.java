package mbank.payload.response;

import com.google.gson.annotations.SerializedName;

public class InitPrepareResponse {

    @SerializedName("TranId")
    private final
    String tranId;

    public InitPrepareResponse(String tranId) {
        this.tranId = tranId;
    }

    public String getTranId() {
        return this.tranId;
    }

}
