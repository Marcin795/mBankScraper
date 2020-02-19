package mbank.model.payload.request;

import com.google.gson.annotations.SerializedName;
import mbank.model.InitPrepareData;

public class InitPrepareRequestBody {

    @SerializedName("Url")
    private final String url = "sca/authorization/disposable";

    @SerializedName("Method")
    private final String method = "POST";

    @SerializedName("Data")
    private final InitPrepareData data;

    public InitPrepareRequestBody(String ScaAuthorizationId) {
        data = new InitPrepareData(ScaAuthorizationId);
    }

}
