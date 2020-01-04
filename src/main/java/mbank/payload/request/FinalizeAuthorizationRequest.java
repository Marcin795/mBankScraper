package mbank.payload.request;

public class FinalizeAuthorizationRequest {

    private final String scaAuthorizationId;

    public FinalizeAuthorizationRequest(String scaAuthorizationId) {
        this.scaAuthorizationId = scaAuthorizationId;
    }

}
