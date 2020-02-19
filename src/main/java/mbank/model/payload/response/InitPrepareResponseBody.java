package mbank.model.payload.response;

import com.google.gson.annotations.SerializedName;

public class InitPrepareResponseBody {

    @SerializedName("TranId")
    public final String tranId;

    public InitPrepareResponseBody(String tranId) {
        this.tranId = tranId;
    }

}
