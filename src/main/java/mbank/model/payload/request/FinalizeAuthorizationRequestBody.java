package mbank.model.payload.request;

public class FinalizeAuthorizationRequestBody {

    private final String scaAuthorizationId;

    public FinalizeAuthorizationRequestBody(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
