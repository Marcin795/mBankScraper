package mbank.payload.request;

import com.google.gson.annotations.SerializedName;
import mbank.model.InitPrepareData;

public class InitPrepareRequest {

    @SerializedName("Url")
    private final String url = "sca/authorization/disposable";

    @SerializedName("Method")
    private final String method = "POST";

    @SerializedName("Data")
    private final InitPrepareData data;

    public InitPrepareRequest(String ScaAuthorizationId) {
        data = new InitPrepareData(ScaAuthorizationId);
    }

}
