package mbank.payload.request;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import mbank.model.InitPrepareData;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InitPrepareRequest {

    @SerializedName("Url")
    String url = "sca/authorization/disposable";

    @SerializedName("Method")
    String method = "POST";

    @SerializedName("Data")
    InitPrepareData data;

    public InitPrepareRequest(String ScaAuthorizationId) {
        data = new InitPrepareData(ScaAuthorizationId);
    }
}
