package mbank.model.payload.request;

import com.google.gson.annotations.SerializedName;

public class StatusRequestBody {

    @SerializedName("TranId")
    private final String tranId;

    public StatusRequestBody(String tranId) {
        this.tranId = tranId;
    }

}
