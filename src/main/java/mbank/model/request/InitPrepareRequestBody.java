package mbank.model.request;

import com.google.gson.annotations.SerializedName;

public class InitPrepareRequestBody {

    @SuppressWarnings("FieldMayBeStatic")
    @SerializedName("Url")
    private final String url = "sca/authorization/disposable";

    @SuppressWarnings("FieldMayBeStatic")
    @SerializedName("Method")
    private final String method = "POST";

    @SerializedName("Data")
    private final InitPrepareData data;

    public InitPrepareRequestBody(String ScaAuthorizationId) {
        data = new InitPrepareData(ScaAuthorizationId);
    }

}
